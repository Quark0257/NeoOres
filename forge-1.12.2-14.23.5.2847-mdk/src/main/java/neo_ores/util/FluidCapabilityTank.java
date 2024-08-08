package neo_ores.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidCapabilityTank implements IFluidTank, IFluidTankProperties, IFluidHandler
{
	private int amount;
	private Fluid fluid;
	private int capacity;

	protected FluidCapabilityTank()
	{
	}

	public FluidCapabilityTank(int caps)
	{
		this.capacity = caps;
		this.amount = 0;
	}

	@Override
	public IFluidTankProperties[] getTankProperties()
	{
		return new IFluidTankProperties[] { this };
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		if(!this.canDrainFluidType(resource))
			return null;
		return this.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack getContents()
	{
		return this.getFluid();
	}

	@Override
	public boolean canFill()
	{
		return this.amount < this.capacity;
	}

	@Override
	public boolean canDrain()
	{
		return this.amount > 0 && this.fluid != null;
	}

	@Override
	public boolean canFillFluidType(FluidStack fluidStack)
	{
		return this.canFill() && (this.fluid == null || fluidStack.getFluid().equals(this.fluid));
	}

	@Override
	public boolean canDrainFluidType(FluidStack fluidStack)
	{
		return this.canDrain() && (this.fluid == null || fluidStack.getFluid().equals(this.fluid));
	}

	@Override
	public FluidStack getFluid()
	{
		if (this.fluid == null)
			return null;
		return new FluidStack(this.fluid, this.amount);
	}

	@Override
	public int getFluidAmount()
	{
		return this.amount;
	}

	@Override
	public int getCapacity()
	{
		return this.capacity;
	}

	@Override
	public FluidTankInfo getInfo()
	{
		return new FluidTankInfo(this);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		if(!this.canFillFluidType(resource)) return 0;
		int fill = Math.min(resource.amount, this.capacity - this.amount);
		if(fill > 0 && this.fluid == null && doFill) {
			this.fluid = resource.getFluid();
		}
		if(doFill)
		{
			this.amount += fill;
		}
		return fill;
	}
	
	public void readFromNBT(NBTTagCompound nbt) 
	{
		this.amount = nbt.getInteger("amount");
		this.capacity = nbt.getInteger("capacity");
		if(nbt.hasKey("fluid"))
			this.fluid = FluidRegistry.getFluid(nbt.getString("fluid"));
		else
			this.fluid = null;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) 
	{
		nbt.setInteger("amount", this.amount);
		nbt.setInteger("capacity", this.capacity);
		if(this.fluid != null)
			nbt.setString("fluid", this.fluid.getName());
		return nbt;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		if (this.fluid == null || this.amount <= 0)
			return null;
		int drain = Math.min(maxDrain, this.amount);
		Fluid drainedFluid = this.fluid;
		if (doDrain)
		{
			this.amount -= drain;
			if (this.amount <= 0)
				this.fluid = null;
		}
		return new FluidStack(drainedFluid, drain);
	}
	
	public static FluidCapabilityTank getFromNBT(NBTTagCompound nbt)
	{
		FluidCapabilityTank tank = new FluidCapabilityTank();
		tank.readFromNBT(nbt);
		return tank;
	}
}
