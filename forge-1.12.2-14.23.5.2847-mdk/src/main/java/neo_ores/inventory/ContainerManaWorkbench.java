package neo_ores.inventory;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.recipe.ManaCraftingRecipes;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.NeoOresData;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.PlayerMagicDataClient;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerManaWorkbench extends Container
{
	private final IInventory outputSlot;
	private final IInventory inputSlots;
	private final World world;
	private final BlockPos selfPosition;
	public long cost;
	public ManaCraftingRecipes recipe = new ManaCraftingRecipes();
	private final EntityPlayer player;
	private int count;

	@SideOnly(Side.CLIENT)
	public ContainerManaWorkbench(InventoryPlayer playerInventory, World worldIn, EntityPlayer player)
	{
		this(playerInventory, worldIn, BlockPos.ORIGIN, player);
		count = 0;
	}

	public ContainerManaWorkbench(InventoryPlayer playerInventory, final World worldIn, final BlockPos blockPosIn, EntityPlayer player)
	{
		count = 0;
		this.outputSlot = new InventoryCraftResult()
		{
			public void markDirty()
			{
				super.markDirty();
				ContainerManaWorkbench.this.onCraftMatrixChanged(ContainerManaWorkbench.this.inputSlots);
			}
		};
		this.inputSlots = new InventoryBasic("ManaWorkbench", true, 9)
		{
			public int getInventoryStackLimit()
			{
				return 64;
			}

			public void markDirty()
			{
				super.markDirty();
				ContainerManaWorkbench.this.onCraftMatrixChanged(this);
			}
		};
		this.selfPosition = blockPosIn;
		this.world = worldIn;
		this.player = player;
		this.addSlotToContainer(new Slot(this.inputSlots, 0, 26, 18));
		this.addSlotToContainer(new Slot(this.inputSlots, 1, 44, 18));
		this.addSlotToContainer(new Slot(this.inputSlots, 2, 62, 18));
		this.addSlotToContainer(new Slot(this.inputSlots, 3, 26, 36));
		this.addSlotToContainer(new Slot(this.inputSlots, 4, 44, 36));
		this.addSlotToContainer(new Slot(this.inputSlots, 5, 62, 36));
		this.addSlotToContainer(new Slot(this.inputSlots, 6, 26, 54));
		this.addSlotToContainer(new Slot(this.inputSlots, 7, 44, 54));
		this.addSlotToContainer(new Slot(this.inputSlots, 8, 62, 54));

		this.addSlotToContainer(new Slot(this.outputSlot, 9, 121, 36)
		{
			public boolean isItemValid(ItemStack stack)
			{
				return false;
			}

			public boolean canTakeStack(EntityPlayer playerIn)
			{
				if (!player.world.isRemote)
				{
					EntityPlayerMP playermp = (EntityPlayerMP) playerIn;
					PlayerMagicData pmd = NeoOresData.instance.getPMD(playermp);
					return (playerIn.capabilities.isCreativeMode || pmd.getMana() >= ContainerManaWorkbench.this.cost) && ContainerManaWorkbench.this.cost > 0 && this.getHasStack();
				}
				else
				{
					EntityPlayerSP playersp = (EntityPlayerSP) playerIn;
					PlayerMagicDataClient pmdc = NeoOresData.getPMDC(EntityPlayer.getUUID(playersp.getGameProfile()));
					return (playerIn.capabilities.isCreativeMode || pmdc.getMana() >= ContainerManaWorkbench.this.cost) && ContainerManaWorkbench.this.cost > 0 && this.getHasStack();
				}
			}

			public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
			{
				if (!thePlayer.capabilities.isCreativeMode)
				{
					if (!thePlayer.world.isRemote)
					{
						EntityPlayerMP playermp = (EntityPlayerMP) thePlayer;
						PlayerMagicData pmd = NeoOresData.instance.getPMD(playermp);
						pmd.addMana(-ContainerManaWorkbench.this.cost);
					}
					else
					{
						EntityPlayerSP playersp = (EntityPlayerSP) thePlayer;
						PlayerMagicDataClient pmdc = NeoOresData.getPMDC(EntityPlayer.getUUID(playersp.getGameProfile()));
						pmdc.addMana(-ContainerManaWorkbench.this.cost);
					}
				}

				for (int i = 0; i < 9; i++)
				{
					if (ContainerManaWorkbench.this.inputSlots.getStackInSlot(i).getCount() == 1)
					{
						ContainerManaWorkbench.this.inputSlots.setInventorySlotContents(i, ItemStack.EMPTY);
					}
					else if (!ContainerManaWorkbench.this.inputSlots.getStackInSlot(i).isEmpty())
					{
						ContainerManaWorkbench.this.inputSlots.getStackInSlot(i).shrink(1);
					}
				}

				return stack;
			}
		});

		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k)
		{
			this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
		}
	}

	public void onCraftMatrixChanged(IInventory inventoryIn)
	{
		super.onCraftMatrixChanged(inventoryIn);

		if (inventoryIn == this.inputSlots)
		{
			this.updateManaCraftOutput();
		}
	}

	public void updateManaCraftOutput()
	{
		List<ItemStack[]> items = new ArrayList<ItemStack[]>();
		items.add(new ItemStack[] { this.inputSlots.getStackInSlot(0), this.inputSlots.getStackInSlot(1), this.inputSlots.getStackInSlot(2) });
		items.add(new ItemStack[] { this.inputSlots.getStackInSlot(3), this.inputSlots.getStackInSlot(4), this.inputSlots.getStackInSlot(5) });
		items.add(new ItemStack[] { this.inputSlots.getStackInSlot(6), this.inputSlots.getStackInSlot(7), this.inputSlots.getStackInSlot(8) });

		this.cost = (Long) recipe.getResult(items)[1];
		ItemStack outputItem = (ItemStack) recipe.getResult(items)[0];

		if (outputItem.isEmpty())
		{
			this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
			this.cost = 0;
		}
		else
		{
			this.outputSlot.setInventorySlotContents(0, outputItem);
			this.detectAndSendChanges();
		}
	}

	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
	}

	/*
	 * @SideOnly(Side.CLIENT) public void updateProgressBar(int id, int data) { if
	 * (id == 0) { this.cost = data; } }
	 */

	public long getMana(EntityPlayer playerIn)
	{
		if (!player.world.isRemote)
		{
			EntityPlayerMP playermp = (EntityPlayerMP) playerIn;
			PlayerMagicData pmd = NeoOresData.instance.getPMD(playermp);
			return pmd.getMana();
		}
		else
		{
			EntityPlayerSP playersp = (EntityPlayerSP) playerIn;
			PlayerMagicDataClient pmdc = NeoOresData.getPMDC(EntityPlayer.getUUID(playersp.getGameProfile()));
			return pmdc.getMana();
		}
	}

	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);

		if (!this.world.isRemote)
		{
			this.clearContainer(playerIn, this.world, this.inputSlots);
		}
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		if (this.world.getBlockState(this.selfPosition).getBlock() != NeoOresBlocks.mana_workbench)
		{
			return false;
		}
		else
		{
			return playerIn.getDistanceSq((double) this.selfPosition.getX() + 0.5D, (double) this.selfPosition.getY() + 0.5D, (double) this.selfPosition.getZ() + 0.5D) <= 64.0D;
		}
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 9)
			{
				this.onCraftMatrixChanged(this.inputSlots);
				if ((world.isRemote && this.getMana(playerIn) / cost <= (long) count) || (!world.isRemote && !slot.canTakeStack(playerIn)))
				{
					return ItemStack.EMPTY;
				}

				itemstack1.getItem().onCreated(itemstack1, this.world, playerIn);

				if (!this.mergeItemStack(itemstack1, 10, 46, true))
				{
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index >= 10 && index < 37)
			{
				if (!this.mergeItemStack(itemstack1, 37, 46, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (index >= 37 && index < 46)
			{
				if (!this.mergeItemStack(itemstack1, 10, 37, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 10, 46, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}

			ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);
			count++;
			if (index == 9)
			{
				playerIn.dropItem(itemstack2, false);
			}
		}

		return itemstack;
	}

	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		count = 0;
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}
}
