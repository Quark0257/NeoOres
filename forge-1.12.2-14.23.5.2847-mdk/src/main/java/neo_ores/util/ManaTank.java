package neo_ores.util;

import neo_ores.main.NeoOresBlocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class ManaTank extends FluidCapabilityTank
{
	protected ManaTank()
	{
	}

	public ManaTank(int caps)
	{
		super(caps);
	}

	@Override
	public boolean canFillFluidType(FluidStack fluidStack)
	{
		if (fluidStack.getFluid() != NeoOresBlocks.fluid_mana)
		{
			return false;
		}
		return super.canFillFluidType(fluidStack);
	}

	public static ManaTank getFromNBT(NBTTagCompound nbt)
	{
		ManaTank tank = new ManaTank();
		tank.readFromNBT(nbt);
		return tank;
	}
}
