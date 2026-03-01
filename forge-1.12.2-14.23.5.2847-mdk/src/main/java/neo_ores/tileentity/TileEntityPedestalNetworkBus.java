package neo_ores.tileentity;

import javax.annotation.Nullable;

import neo_ores.block.BlockPedestalNetworkBus;
import neo_ores.block.IPedestalInterfaceComponent;
import neo_ores.util.SpellUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class TileEntityPedestalNetworkBus extends AbstractTileEntityPedestal implements ISidedInventory
{
	private EnumFacing face = EnumFacing.UP;
	private boolean minusPriorityMode = false;
	
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		compound.setInteger("face", this.face.getIndex());
		compound.setBoolean("minusPriorityMode", this.minusPriorityMode);
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		this.face = EnumFacing.getFront(compound.getInteger("face"));
		this.minusPriorityMode = compound.getBoolean("minusPriorityMode");
		super.writeToNBT(compound);
		return compound;
	}
	
	public void changePriorityMode() 
	{
		this.minusPriorityMode = !this.minusPriorityMode;
		int color = this.minusPriorityMode ? 0xFF3F3F : 0x3FFF3F;
		SpellUtils.displayParticleTypeB(this.world, new Vec3d(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D), 0.5D, 5, 10, 2.0F, 5.0F, color, true);
	}
	
	public int getPriority() 
	{
		int priority = 0;
		IBlockState state = this.world.getBlockState(this.getPos());
		if (state.getBlock() instanceof BlockPedestalNetworkBus) 
		{
			for (EnumFacing facing : EnumFacing.values()) 
			{
				if (facing == this.face) 
				{
					continue;
				}
				int value = ((BlockPedestalNetworkBus) state.getBlock()).getPowerOnSide(this.world, this.getPos().add(facing.getDirectionVec()), facing);
				priority = Math.max(value, priority);
			}
		}
		
		return (this.minusPriorityMode ? -1 : 1) * priority;
	}

	@Override
	public int getSizeInventory()
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return 0;
		}
		return inventory.getSizeInventory();
	}

	@Override
	public boolean isEmpty()
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return false;
		}
		return inventory.isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return ItemStack.EMPTY;
		}
		return inventory.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return ItemStack.EMPTY;
		}
		return inventory.decrStackSize(index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return ItemStack.EMPTY;
		}
		return inventory.removeStackFromSlot(index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return;
		}
		inventory.setInventorySlotContents(index, stack);
	}

	@Override
	public int getInventoryStackLimit()
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return 0;
		}
		return inventory.getInventoryStackLimit();
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return false;
		}
		return inventory.isUsableByPlayer(player);
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return;
		}
		inventory.openInventory(player);
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return;
		}
		inventory.closeInventory(player);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return false;
		}
		return inventory.isItemValidForSlot(index, stack);
	}

	@Override
	public int getField(int id)
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return 0;
		}
		return inventory.getField(id);
	}

	@Override
	public void setField(int id, int value)
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return;
		}
		inventory.setField(id, value);
	}

	@Override
	public int getFieldCount()
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return 0;
		}
		return inventory.getFieldCount();
	}

	@Override
	public void clear()
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return;
		}
		inventory.clear();
	}

	@Override
	public String getName()
	{
		return "pedestal_network_bus";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return new int[] {};
		}
		if (inventory instanceof ISidedInventory) 
		{
			((ISidedInventory) inventory).getSlotsForFace(side);
		}
		int[] slots = new int[inventory.getSizeInventory()];
		for (int i = 0; i < inventory.getSizeInventory(); i++) 
		{
			slots[i] = i;
		}
		return slots;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return false;
		}
		if (inventory instanceof ISidedInventory) 
		{
			((ISidedInventory) inventory).canInsertItem(index, itemStackIn, direction);
		}
		return inventory.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		IInventory inventory = this.getTargetInventory();
		if (inventory == null) 
		{
			return false;
		}
		if (inventory instanceof ISidedInventory) 
		{
			((ISidedInventory) inventory).canExtractItem(index, stack, direction);
		}
		return true;
	}

	@Override
	public void update()
	{
		if (!this.world.isRemote) 
		{
			IBlockState state = this.world.getBlockState(this.getPos());
			if (state.getBlock() instanceof BlockPedestalNetworkBus) 
			{
				 EnumFacing facing = BlockPedestalNetworkBus.getFacing(state.getBlock().getMetaFromState(state));
				 this.face = facing == null ? EnumFacing.UP : facing;
			}
		}
	}

	@Nullable
	private IInventory getTargetInventory() 
	{
		IBlockState state = this.world.getBlockState(this.getPos().add(this.face.getDirectionVec()));
		if (state.getBlock() instanceof IPedestalInterfaceComponent) 
		{
			return null;
		}
		TileEntity te = this.world.getTileEntity(this.getPos().add(this.face.getDirectionVec()));
		if (te != null && te instanceof IInventory) 
		{
			return (IInventory) te;
		}
		return null;
	}
	
	IItemHandler handlerTop = new SidedInvWrapper(this, EnumFacing.UP);
	IItemHandler handlerBottom = new SidedInvWrapper(this, EnumFacing.DOWN);
	IItemHandler handlerWest = new SidedInvWrapper(this, EnumFacing.WEST);
	IItemHandler handlerEast = new SidedInvWrapper(this, EnumFacing.EAST);
	IItemHandler handlerSouth = new SidedInvWrapper(this, EnumFacing.SOUTH);
	IItemHandler handlerNorth = new SidedInvWrapper(this, EnumFacing.NORTH);
	
	@Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }
	
	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (!this.hasCapability(capability, facing))
			return null;
		if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			if (facing == EnumFacing.DOWN)
				return (T) handlerBottom;
			else if (facing == EnumFacing.UP)
				return (T) handlerTop;
			else if (facing == EnumFacing.WEST)
				return (T) handlerWest;
			else if (facing == EnumFacing.EAST)
				return (T) handlerEast;
			else if (facing == EnumFacing.SOUTH)
				return (T) handlerSouth;
			else if (facing == EnumFacing.NORTH)
				return (T) handlerNorth;
		return super.getCapability(capability, facing);
	}
}
