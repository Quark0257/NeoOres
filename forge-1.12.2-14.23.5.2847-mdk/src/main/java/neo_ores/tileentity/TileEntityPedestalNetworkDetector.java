package neo_ores.tileentity;

import javax.annotation.Nullable;

import neo_ores.block.BlockPedestalNetworkDetector;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresBlocks;
import neo_ores.packet.PacketItemsToClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityPedestalNetworkDetector extends AbstractTileEntityPedestal implements ISidedInventory
{
	private ItemStack stack = ItemStack.EMPTY;
	private int count = 0;
	private static final int MAX_COUNT = 4;
	private boolean inputPower = false;

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		if (compound.hasKey("display", 10))
		{
			this.display = new ItemStack(compound.getCompoundTag("display"));
		}
		
		this.inputPower = compound.getBoolean("inputPower");
		this.count = compound.getInteger("count");
		this.stack = new ItemStack(compound.getCompoundTag("stack"));
	}
	
	public void setClient(int count, boolean inputPower) 
	{
		this.count = count;
		this.inputPower = inputPower;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{

		NBTTagCompound nbttagcompound = stack.writeToNBT(new NBTTagCompound());
		compound.setTag("stack", nbttagcompound);
		nbttagcompound = display.writeToNBT(new NBTTagCompound());
		compound.setTag("display", nbttagcompound);
		compound.setInteger("count", this.count);
		compound.setBoolean("inputPower", this.inputPower);
		super.writeToNBT(compound);
		return compound;
	}
	
	@Override
	public void update()
	{
		if (!this.getWorld().isRemote)
		{
			this.offset = 0.0625D;
			NBTTagCompound packet = new NBTTagCompound();
			packet.setInteger("x", this.pos.getX());
			packet.setInteger("y", this.pos.getY());
			packet.setInteger("z", this.pos.getZ());
			packet.setDouble("offset", offset);
			ItemStack stack = (this.getDisplay().isEmpty()) ? this.stack : this.getDisplay();
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound = stack.writeToNBT(nbttagcompound);
			packet.setTag("display", nbttagcompound);
			packet.setInteger("dim", this.world.provider.getDimension());
			packet.setInteger("count", this.count);
			packet.setBoolean("inputPower", this.inputPower);
			PacketItemsToClient pic = new PacketItemsToClient(packet);
			NeoOres.PACKET.sendToAll(pic);
			
			if (this.count > 0) 
			{
				this.count--;
			}
			
			if (this.count <= 0 && this.world.getBlockState(this.pos).getBlock() == NeoOresBlocks.lit_detector_pedestal)
			{
				BlockPedestalNetworkDetector.setState(false, this.world, this.pos);
				this.markDirty();
			}
			
			if (this.count > 0 && this.world.getBlockState(this.pos).getBlock() == NeoOresBlocks.detector_pedestal) 
			{
				BlockPedestalNetworkDetector.setState(true, this.world, this.pos);
				this.markDirty();
			}
		}
		super.update();
	}

	@Override
	public int getSizeInventory()
	{
		int size = (this.inputPower) ? 0 : 1;
		this.markDirty();
		return size;
	}

	@Override
	public boolean isEmpty()
	{
		return this.stack.isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return this.stack;
	}
	
	public void setItemStack(ItemStack stack) 
	{
		this.stack = stack.copy();
		this.stack.setCount(1);
		this.markDirty();
	}
	
	public void clearItemStack() 
	{
		this.stack = ItemStack.EMPTY;
		this.markDirty();
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		this.setOn();
		return ItemStack.EMPTY;
	}
	
	public void setOn() 
	{
		this.count = MAX_COUNT;
		this.markDirty();
	}
	
	public void setRedstone(boolean flag) 
	{
		this.inputPower = flag;
		this.markDirty();
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return false;
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		this.stack = ItemStack.EMPTY;
	}

	@Override
	public String getName()
	{
		return "container.detector_pedestal";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return (this.inputPower) ? new int[] {} : new int[] {0};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return false;
	}
	
	IItemHandler handlerTop = new DetectorWrapper(this, EnumFacing.UP);
	IItemHandler handlerBottom = new DetectorWrapper(this, EnumFacing.DOWN);
	IItemHandler handlerWest = new DetectorWrapper(this, EnumFacing.WEST);
	IItemHandler handlerEast = new DetectorWrapper(this, EnumFacing.EAST);
	IItemHandler handlerSouth = new DetectorWrapper(this, EnumFacing.SOUTH);
	IItemHandler handlerNorth = new DetectorWrapper(this, EnumFacing.NORTH);
	
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
