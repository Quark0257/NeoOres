package neo_ores.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

import neo_ores.api.LargeItemStack;
import neo_ores.api.StackUtils;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresItems;
import neo_ores.packet.PacketItemsToClient;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityEnhancedPedestal extends AbstractTileEntityPedestal implements ISidedInventory
{
	public int slotsize;
	private NonNullList<ItemStack> itemList = NonNullList.withSize(this.slotsize, ItemStack.EMPTY);
	private int selectedSlot;
	private boolean canSuck;
	public int tickCount;

	public TileEntityEnhancedPedestal()
	{

	}

	public void setSize(int slotsize)
	{
		this.slotsize = 2 * (int) Math.pow(2.0D, (double) ((0 < slotsize && slotsize <= 8) ? slotsize : 1));
		this.itemList = NonNullList.withSize(this.slotsize, ItemStack.EMPTY);
	}

	public void setSuckable(boolean canSuck)
	{
		this.canSuck = canSuck;
	}

	public void setSlot(int slot)
	{
		this.selectedSlot = slot;
	}

	public int getSlot()
	{
		return this.selectedSlot;
	}

	public void addSlot(int slot)
	{
		int apply = this.getSlot();
		if (slot >= 0)
		{
			for (int n = 0; n < slot; n++)
			{
				for (int i = 1; i < this.getSizeInventory(); i++)
				{
					if (!this.getItems().get((apply + i >= this.getSizeInventory()) ? apply + i - this.getSizeInventory() : apply + i).isEmpty())
					{
						apply = (apply + i >= this.getSizeInventory()) ? apply + i - this.getSizeInventory() : apply + i;
						break;
					}
				}
			}
		}
		else
		{
			for (int n = 0; n < (-slot); n++)
			{
				for (int i = 1; i < this.getSizeInventory(); i++)
				{
					if (!this.getItems().get((apply - i < 0) ? this.getSizeInventory() + apply - i : apply - i).isEmpty())
					{
						apply = (apply - i < 0) ? this.getSizeInventory() + apply - i : apply - i;
						break;
					}
				}
			}
		}

		this.setSlot(apply);
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		this.slotsize = compound.getInteger("slotSize");
		this.selectedSlot = compound.getInteger("selectedSlot");
		this.canSuck = compound.getBoolean("canSuck");

		if (compound.hasKey("display", 10))
		{
			this.display = new ItemStack(compound.getCompoundTag("display"));
		}

		this.itemList = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		StackUtils.loadAllItems(compound, this.itemList);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		StackUtils.saveAllItems(compound, this.itemList);

		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound = display.writeToNBT(nbttagcompound);
		compound.setTag("display", nbttagcompound);
		compound.setInteger("slotSize", this.slotsize);
		compound.setInteger("selectedSlot", this.selectedSlot);
		compound.setBoolean("canSuck", this.canSuck);

		return compound;
	}

	@Override
	public boolean isEmpty()
	{
		boolean flag = true;
		for (int i = 0; i < this.itemList.size(); i++)
		{
			flag = this.itemList.get(i).isEmpty();
			if (!flag)
				break;
		}
		return flag;
	}

	public ItemStack getStackInSlot(int index)
	{
		return this.itemList.get(index);
	}

	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(itemList, index, count);
	}

	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(itemList, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		if (index >= 0 && index < this.itemList.size())
		{
			this.itemList.set(index, stack);
			if (stack.getCount() > this.getInventoryStackLimit())
			{
				stack.setCount(this.getInventoryStackLimit());
			}
			this.markDirty();
		}
	}

	public ItemStack addItemStackToInventory(ItemStack stack)
	{
		IItemHandler handler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
		if (handler == null)
		{
			return stack;
		}
		for (int i = 0; i < handler.getSlots(); i++)
		{
			if (!stack.isEmpty() && StackUtils.compareWith(handler.getStackInSlot(i), stack))
			{
				stack = handler.insertItem(i, stack, false);
			}
		}
		for (int i = 0; i < handler.getSlots(); i++)
		{
			stack = handler.insertItem(i, stack, false);
		}
		return stack;
	}

	public boolean isFull()
	{
		if (itemList.isEmpty())
			return true;
		for (ItemStack stack : itemList)
		{
			if (stack.isEmpty())
				return false;
			else if (stack.getCount() < this.getInventoryStackLimit())
				return false;
		}
		return true;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return (int) Math.pow(slotsize / 2, 3) * 64;
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
		return StackUtils.compareWith(this.itemList.get(index), stack) || this.itemList.get(index).isEmpty();
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
		for (int i = 0; i < this.itemList.size(); i++)
		{
			this.itemList.set(i, ItemStack.EMPTY);
		}
	}

	@Override
	public String getName()
	{
		return "container.enhance_pedestal";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public void update()
	{
		int meta = this.getBlockMetadata();
		if (meta != -1 && this.itemList.size() == 0)
		{
			this.setSlot(meta % 8 + 1);
			this.setSuckable((meta / 8) == 1);
		}

		if (!this.getWorld().isRemote)
		{
			Map<NBTTagCompound, Integer> pedestalData = new HashMap<>();
			boolean flag = false;
			for (int i = 0; i < this.getSizeInventory(); i++)
			{
				if (this.itemList.get(i).isEmpty() || this.itemList.get(i).getCount() >= this.getInventoryStackLimit())
				{
					continue;
				}
				NBTTagCompound key = StackUtils.getNBT(this.itemList.get(i), true);
				if (!pedestalData.containsKey(key))
				{
					pedestalData.put(key, 0);
				}
				else
				{
					flag = true;
				}
			}
			if (flag)
			{
				for (int i = 0; i < this.getSizeInventory(); i++)
				{
					if (!this.itemList.get(i).isEmpty() && this.itemList.get(i).getCount() < this.getInventoryStackLimit())
					{
						for (int j = i + 1; j < this.getSizeInventory(); j++)
						{
							ItemStack stackJ = this.itemList.get(j).copy();
							ItemStack stackI = this.itemList.get(i).copy();
							if (!stackJ.isEmpty() && !stackI.isEmpty())
							{
								if (StackUtils.compareWith(stackI, stackJ))
								{
									if (this.getInventoryStackLimit() < stackI.getCount() + stackJ.getCount())
									{
										stackJ.setCount((stackI.getCount() + stackJ.getCount()) - this.getInventoryStackLimit());
										stackI.setCount(this.getInventoryStackLimit());
									}
									else
									{
										stackI.grow(stackJ.getCount());
										stackJ.shrink(stackJ.getCount());
									}
								}
							}
							this.itemList.set(j, stackJ.isEmpty() ? ItemStack.EMPTY : stackJ);
							this.itemList.set(i, stackI);

							if (this.itemList.get(i).getCount() >= this.getInventoryStackLimit())
							{
								break;
							}
						}
					}
				}
			}
		}

		if (this.canSuck && !this.getWorld().isRemote)
		{
			List<EntityItem> list = TileEntityHopper.getCaptureItems(this.getWorld(), (double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 1.0625D, (double) this.pos.getZ() + 0.5D);
			for (EntityItem ei : list)
			{
				if (ei.getItem().getItem() != NeoOresItems.mana_wrench)
				{
					ei.setItem(this.addItemStackToInventory(ei.getItem()));
					if (ei.getItem().isEmpty())
						ei.setDead();
				}
			}
		}

		if (!this.getWorld().isRemote)
		{
			NBTTagCompound packet = new NBTTagCompound();
			packet.setInteger("x", this.pos.getX());
			packet.setInteger("y", this.pos.getY());
			packet.setInteger("z", this.pos.getZ());
			packet.setDouble("offset", offset);
			packet.setInteger("slotsize", this.slotsize);
			ItemStack stack = (this.getDisplay().isEmpty()) ? this.getStackInSlot(this.getSlot()).copy() : this.getDisplay();
			stack.setCount(1);
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound = stack.writeToNBT(nbttagcompound);
			packet.setTag("display", nbttagcompound);
			packet.setInteger("dim", this.world.provider.getDimension());
			PacketItemsToClient pic = new PacketItemsToClient(packet);
			NeoOres.PACKET.sendToAll(pic);
		}

		super.update();

		if (!this.getWorld().isRemote && this.itemList.get(this.getSlot()).isEmpty())
		{
			for (int i = 0; i < this.getSizeInventory(); i++)
			{
				if (!this.itemList.get(i).isEmpty())
				{
					this.setSlot(i);
					break;
				}
			}
		}
	}

	@Override
	public int getSizeInventory()
	{
		return this.slotsize;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		if (side != EnumFacing.UP)
		{
			List<Integer> list = new ArrayList<Integer>();
			int size = this.getSizeInventory();
			for (int i = 0; i < size; i++)
			{
				list.add(i);
			}
			return ArrayUtils.toPrimitive(list.toArray(new Integer[] {}));
		}
		return new int[] {};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return true;
	}

	public NonNullList<ItemStack> getItems()
	{
		return this.itemList;
	}

	public static void dropInventoryItems(World worldIn, BlockPos pos, TileEntityEnhancedPedestal tileentity)
	{
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
		for (ItemStack stackI : tileentity.getItems())
		{
			if (!stackI.isEmpty())
			{
				LargeItemStack stackWS = new LargeItemStack(stackI, stackI.getCount());
				for (ItemStack stack : stackWS.asList(stackWS.getStack().getMaxStackSize()))
				{
					InventoryHelper.spawnItemStack(worldIn, x, y, z, stack);
				}
			}
		}
	}

	@Override
	public boolean canExtract(int index, ItemStack stack, EnumFacing direction)
	{
		return direction != EnumFacing.UP;
	}

	@Override
	public boolean canInsert(int index, ItemStack stack, EnumFacing direction)
	{
		return direction != EnumFacing.UP && this.isItemValidForSlot(index, stack);
	}

	IItemHandler handlerTop = new EnhancedPedestalWrapper(this, EnumFacing.UP);
	IItemHandler handlerBottom = new EnhancedPedestalWrapper(this, EnumFacing.DOWN);
	IItemHandler handlerWest = new EnhancedPedestalWrapper(this, EnumFacing.WEST);
	IItemHandler handlerEast = new EnhancedPedestalWrapper(this, EnumFacing.EAST);
	IItemHandler handlerSouth = new EnhancedPedestalWrapper(this, EnumFacing.SOUTH);
	IItemHandler handlerNorth = new EnhancedPedestalWrapper(this, EnumFacing.NORTH);

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
