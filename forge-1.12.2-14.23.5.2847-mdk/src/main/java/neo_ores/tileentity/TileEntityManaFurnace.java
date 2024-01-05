package neo_ores.tileentity;

import neo_ores.api.RingArray;
import neo_ores.api.RingArrayElement;
import neo_ores.api.TierUtils;
import neo_ores.block.BlockManaFurnace;
import neo_ores.inventory.ContainerManaFurnace;
import neo_ores.item.IItemNeoTool;
import neo_ores.item.ItemNeoArmor;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.NeoOresItems;
import neo_ores.world.dimension.DimensionHelper.ToolType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityManaFurnace extends TileEntityLockable implements ITickable, ISidedInventory
{
	private static final int[] SLOTS_TOP = new int[] { 5, 4 };
	private static final int[] SLOTS_BOTTOM = new int[] { 6 };
	private static final int[] SLOTS_EAST = new int[] { 0 };
	private static final int[] SLOTS_SOUTH = new int[] { 1 };
	private static final int[] SLOTS_WEST = new int[] { 2 };
	private static final int[] SLOTS_NORTH = new int[] { 3 };

	private NonNullList<ItemStack> manaFurnaceItemStacks = NonNullList.<ItemStack>withSize(7, ItemStack.EMPTY);
	private int manaFurnaceBurnTime;
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;
	private String manaFurnaceCustomName;

	public int getSizeInventory()
	{
		return this.manaFurnaceItemStacks.size();
	}

	public boolean isEmpty()
	{
		for (ItemStack itemstack : this.manaFurnaceItemStacks)
		{
			if (!itemstack.isEmpty())
			{
				return false;
			}
		}

		return true;
	}

	public ItemStack getStackInSlot(int index)
	{
		return this.manaFurnaceItemStacks.get(index);
	}

	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(this.manaFurnaceItemStacks, index, count);
	}

	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.manaFurnaceItemStacks, index);
	}

	public void setInventorySlotContents(int index, ItemStack stack)
	{
		ItemStack itemstack = this.manaFurnaceItemStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.manaFurnaceItemStacks.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit())
		{
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 4 && !flag)
		{
			this.totalCookTime = this.getCookTime(stack);
			this.cookTime = 0;
			this.markDirty();
		}
	}

	public String getName()
	{
		return this.hasCustomName() ? this.manaFurnaceCustomName : "container.mana_furnace";
	}

	public boolean hasCustomName()
	{
		return this.manaFurnaceCustomName != null && !this.manaFurnaceCustomName.isEmpty();
	}

	public void setCustomInventoryName(String p_145951_1_)
	{
		this.manaFurnaceCustomName = p_145951_1_;
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.manaFurnaceItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.manaFurnaceItemStacks);
		this.manaFurnaceBurnTime = compound.getInteger("BurnTime");
		this.cookTime = compound.getInteger("CookTime");
		this.totalCookTime = compound.getInteger("CookTimeTotal");
		this.currentItemBurnTime = getItemBurnTime(this.manaFurnaceItemStacks.get(5));

		if (compound.hasKey("CustomName", 8))
		{
			this.manaFurnaceCustomName = compound.getString("CustomName");
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("BurnTime", (short) this.manaFurnaceBurnTime);
		compound.setInteger("CookTime", (short) this.cookTime);
		compound.setInteger("CookTimeTotal", (short) this.totalCookTime);
		ItemStackHelper.saveAllItems(compound, this.manaFurnaceItemStacks);

		if (this.hasCustomName())
		{
			compound.setString("CustomName", this.manaFurnaceCustomName);
		}

		return compound;
	}

	public int getInventoryStackLimit()
	{
		return 64;
	}

	public boolean isBurning()
	{
		return this.manaFurnaceBurnTime > 0;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isBurning(IInventory inventory)
	{
		return inventory.getField(0) > 0;
	}

	public void update()
	{
		boolean flag = this.isBurning();
		boolean flag1 = false;

		if (this.isBurning())
		{
			--this.manaFurnaceBurnTime;
		}

		if (!this.world.isRemote)
		{
			ItemStack itemstack = this.manaFurnaceItemStacks.get(5);

			if (this.isBurning() || !itemstack.isEmpty() && !((ItemStack) this.manaFurnaceItemStacks.get(4)).isEmpty())
			{
				if (!this.isBurning() && this.canSmelt())
				{
					this.manaFurnaceBurnTime = getItemBurnTime(itemstack);
					this.currentItemBurnTime = this.manaFurnaceBurnTime;

					if (this.isBurning())
					{
						flag1 = true;

						if (!itemstack.isEmpty())
						{
							Item item = itemstack.getItem();
							itemstack.shrink(1);

							if (itemstack.isEmpty())
							{
								ItemStack item1 = item.getContainerItem(itemstack);
								this.manaFurnaceItemStacks.set(5, item1);
							}
						}
					}
				}

				if (this.isBurning() && this.canSmelt())
				{
					++this.cookTime;

					if (this.cookTime == this.totalCookTime)
					{
						this.cookTime = 0;
						this.totalCookTime = this.getCookTime(this.manaFurnaceItemStacks.get(4));
						this.smeltItem();
						flag1 = true;
					}
				}
				else
				{
					this.cookTime = 0;
				}
			}
			else if (!this.isBurning() && this.cookTime > 0)
			{
				this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
			}

			if (flag != this.isBurning())
			{
				flag1 = true;
				BlockManaFurnace.setState(this.isBurning(), this.world, this.pos);
			}
		}

		if (flag1)
		{
			this.markDirty();
		}
	}

	public int getCookTime(ItemStack stack)
	{
		int cost = 2;
		TierUtils utils = new TierUtils(stack);
		int cost_calc = (utils.getAir() + utils.getEarth() + utils.getFire() + utils.getWater()) / 2;
		for (int i = 0; i < cost_calc + 1; i++)
		{
			cost *= 2;
		}
		return 600 * cost + 1;
	}

	private boolean canSmelt()
	{
		if (((ItemStack) this.manaFurnaceItemStacks.get(4)).isEmpty())
		{
			return false;
		}
		else
		{
			ItemStack itemstack = this.getSmeltItem((ItemStack) this.manaFurnaceItemStacks.get(4), (ItemStack) this.manaFurnaceItemStacks.get(0), (ItemStack) this.manaFurnaceItemStacks.get(1),
					(ItemStack) this.manaFurnaceItemStacks.get(2), (ItemStack) this.manaFurnaceItemStacks.get(3));

			if (itemstack.isEmpty())
			{
				return false;
			}
			else
			{
				ItemStack itemstack1 = this.manaFurnaceItemStacks.get(6);

				if (itemstack1.isEmpty())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
	}

	public void smeltItem()
	{
		if (this.canSmelt())
		{
			ItemStack itemstack = this.manaFurnaceItemStacks.get(4);
			ItemStack itemstack1 = this.getSmeltItem(itemstack, (ItemStack) this.manaFurnaceItemStacks.get(0), (ItemStack) this.manaFurnaceItemStacks.get(1),
					(ItemStack) this.manaFurnaceItemStacks.get(2), (ItemStack) this.manaFurnaceItemStacks.get(3));
			itemstack1.setRepairCost(0);
			ItemStack itemstack2 = this.manaFurnaceItemStacks.get(6);

			if (itemstack2.isEmpty())
			{
				this.manaFurnaceItemStacks.set(6, itemstack1);
			}

			this.onSmeltShrink(itemstack, (ItemStack) this.manaFurnaceItemStacks.get(0), (ItemStack) this.manaFurnaceItemStacks.get(1), (ItemStack) this.manaFurnaceItemStacks.get(2),
					(ItemStack) this.manaFurnaceItemStacks.get(3));
			itemstack.shrink(1);
		}
	}

	public ItemStack getSmeltItem(ItemStack input, ItemStack air, ItemStack fire, ItemStack water, ItemStack earth)
	{
		RingArrayElement<ItemStack> e = new RingArrayElement<ItemStack>(air, earth, fire, water);
		TierUtils in = new TierUtils(input);
		ItemStack itemStack = input.copy();
		TierUtils out = new TierUtils(itemStack);
		RingArray<ToolType> tiers = TierUtils.tiers;
		if (input.getItem() instanceof IItemNeoTool)
		{
			IItemNeoTool tool = (IItemNeoTool) input.getItem();
			if (tool.getToolType() != ToolType.CREATIVE)
			{
				tiers.setTop(tiers.getGlobalIndex(tool.getToolType()));
				e.setTop(tiers.getGlobalIndex(tool.getToolType()));
				boolean flag = false;
				for (int i = 0; i < 4; i++)
				{
					if ((i == 0 || in.get(tiers.get(i)) * (4 - i) / 4 > e.get(i).getMetadata()) && this.isType(input, e.get(i), tiers.get(i)))
					{
						out.set(tiers.get(i), in.get(tiers.get(i)) + 1);
						flag = true;
					}
				}
				if (flag)
				{
					return itemStack;
				}
			}
			else if (tool.getToolType() == ToolType.CREATIVE)
			{
				e.setTop(0);
				tiers.setTop(0);
				boolean flag = false;
				for (int i = 0; i < 4; i++)
				{
					if (this.isType(input, e.get(i), tiers.get(i)))
					{
						out.set(tiers.get(i), in.get(tiers.get(i)) + 1);
						flag = true;
					}
				}
				if (flag)
					return itemStack;
			}
		}
		return ItemStack.EMPTY;
	}

	public void onSmeltShrink(ItemStack input, ItemStack air, ItemStack fire, ItemStack water, ItemStack earth)
	{
		RingArrayElement<ItemStack> e = new RingArrayElement<ItemStack>(air, earth, fire, water);
		TierUtils in = new TierUtils(input);
		RingArray<ToolType> tiers = TierUtils.tiers;
		if (input.getItem() instanceof IItemNeoTool)
		{
			IItemNeoTool tool = (IItemNeoTool) input.getItem();
			if (tool.getToolType() != ToolType.CREATIVE)
			{
				tiers.setTop(tiers.getGlobalIndex(tool.getToolType()));
				e.setTop(tiers.getGlobalIndex(tool.getToolType()));
				for (int i = 0; i < 4; i++)
				{
					if ((i == 0 || in.get(tiers.get(i)) * (4 - i) / 4 > e.get(i).getMetadata()) && this.isType(input, e.get(i), tiers.get(i)))
					{
						e.get(i).shrink(1);
					}
				}
			}
			else if (tool.getToolType() == ToolType.CREATIVE)
			{
				e.setTop(0);
				tiers.setTop(0);
				for (int i = 0; i < 4; i++)
				{
					if (this.isType(input, e.get(i), tiers.get(i)))
					{
						e.get(i).shrink(1);
					}
				}
			}
		}
	}

	public boolean isType(ItemStack input, ItemStack item, ToolType type)
	{
		TierUtils utils = new TierUtils(input);
		return utils.get(type) == item.getMetadata() && item.getItem() == getItemWithType(type);
	}

	public static Item getItemWithType(ToolType type)
	{
		switch (type)
		{
			case AIR:
			{
				return NeoOresItems.air_essence_core;
			}
			case EARTH:
			{
				return NeoOresItems.earth_essence_core;
			}
			case FIRE:
			{
				return NeoOresItems.fire_essence_core;
			}
			default:
				return NeoOresItems.water_essence_core;
		}
	}

	public static int getItemBurnTime(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return 0;
		}
		else
		{
			Item item = stack.getItem();

			if (item == NeoOresItems.mana_ingot)
			{
				return 600;
			}
			else if (item == Item.getItemFromBlock(NeoOresBlocks.mana_block))
			{
				return 6000;
			}
		}

		return 0;
	}

	public static boolean isItemFuel(ItemStack stack)
	{
		return getItemBurnTime(stack) > 0;
	}

	public boolean isUsableByPlayer(EntityPlayer player)
	{
		if (this.world.getTileEntity(this.pos) != this)
		{
			return false;
		}
		else
		{
			return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	public void openInventory(EntityPlayer player)
	{
	}

	public void closeInventory(EntityPlayer player)
	{
	}

	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		if (index == 6)
		{
			return false;
		}
		else if (index == 4)
		{
			return stack.getItem() instanceof IItemNeoTool || stack.getItem() instanceof ItemNeoArmor;
		}
		else if (index == 0)
		{
			return stack.getItem() == NeoOresItems.air_essence_core;
		}
		else if (index == 1)
		{
			return stack.getItem() == NeoOresItems.fire_essence_core;
		}
		else if (index == 2)
		{
			return stack.getItem() == NeoOresItems.water_essence_core;
		}
		else if (index == 3)
		{
			return stack.getItem() == NeoOresItems.earth_essence_core;
		}
		else
		{
			return isItemFuel(stack);
		}
	}

	public int[] getSlotsForFace(EnumFacing side)
	{
		if (side == EnumFacing.DOWN)
			return SLOTS_BOTTOM;
		else if (side == EnumFacing.EAST)
			return SLOTS_EAST;
		else if (side == EnumFacing.WEST)
			return SLOTS_WEST;
		else if (side == EnumFacing.SOUTH)
			return SLOTS_SOUTH;
		else if (side == EnumFacing.NORTH)
			return SLOTS_NORTH;
		else
			return SLOTS_TOP;
	}

	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return this.isItemValidForSlot(index, itemStackIn);
	}

	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		if (direction == EnumFacing.DOWN && index == 1)
		{
			Item item = stack.getItem();

			if (item != Items.WATER_BUCKET && item != Items.BUCKET)
			{
				return false;
			}
		}

		return true;
	}

	public String getGuiID()
	{
		return "neo_ores:mana_furnace";
	}

	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerManaFurnace(playerInventory, this);
	}

	public int getField(int id)
	{
		switch (id)
		{
			case 0:
				return this.manaFurnaceBurnTime;
			case 1:
				return this.currentItemBurnTime;
			case 2:
				return this.cookTime;
			case 3:
				return this.totalCookTime;
			default:
				return 0;
		}
	}

	public void setField(int id, int value)
	{
		switch (id)
		{
			case 0:
				this.manaFurnaceBurnTime = value;
				break;
			case 1:
				this.currentItemBurnTime = value;
				break;
			case 2:
				this.cookTime = value;
				break;
			case 3:
				this.totalCookTime = value;
		}
	}

	public int getFieldCount()
	{
		return 4;
	}

	public void clear()
	{
		this.manaFurnaceItemStacks.clear();
	}

	net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.UP);
	net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.DOWN);
	net.minecraftforge.items.IItemHandler handlerWest = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.WEST);
	net.minecraftforge.items.IItemHandler handlerEast = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.EAST);
	net.minecraftforge.items.IItemHandler handlerSouth = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.SOUTH);
	net.minecraftforge.items.IItemHandler handlerNorth = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.NORTH);

	@SuppressWarnings("unchecked")
	@Override
	@javax.annotation.Nullable
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable EnumFacing facing)
	{
		if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
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
