package neo_ores.inventory;

import neo_ores.item.IItemTotem;
import neo_ores.item.IItemUpgrade;
import neo_ores.item.ItemSpell;
import neo_ores.tileentity.TileEntityMechanicalMagician;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerMechanicalMagician extends Container
{
	private TileEntityMechanicalMagician tileMM;
	private int liquidMana;
	private int isLiquid;

	public ContainerMechanicalMagician(InventoryPlayer playerInventory, TileEntityMechanicalMagician tileMM)
	{
		this.tileMM = tileMM;

		this.addSlotToContainer(new Slot(this.tileMM, 0, 80, 54) // ItemSpell
		{
			public boolean isItemValid(ItemStack stack)
			{
				return stack.getItem() instanceof ItemSpell;
			}
		});

		this.addSlotToContainer(new Slot(this.tileMM, 1, 80, 18)
		{
			public boolean isItemValid(ItemStack stack)
			{
				return stack.getItem() instanceof IItemTotem;
			}
		}); // ItemTotem

		for (int i = 0; i < 3; i++)
		{
			this.addSlotToContainer(new Slot(this.tileMM, 2 + i, 44, 18 + i * 18)
			{
				public boolean isItemValid(ItemStack stack)
				{
					if (stack.getItem() instanceof IItemUpgrade)
					{
						if (((IItemUpgrade) stack.getItem()).maxApply(stack) <= 0)
						{
							return true;
						}
						else
						{
							int count = 0;
							for (int i = 2; i < 5; i++)
								if (this.inventory.getStackInSlot(i).getItem() == stack.getItem())
									count++;
							return ((IItemUpgrade) stack.getItem()).maxApply(stack) > count;
						}
					}
					return false;
				}

				public int getSlotStackLimit()
				{
					return 1;
				}
			}); // ItemUpgrade
		}

		for (int i = 0; i < 9; i++)
		{
			this.addSlotToContainer(new Slot(this.tileMM, 5 + i, 116 + (i % 3) * 18, 18 + 18 * (i / 3))); // Inventory
		}

		// Player Inventory
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

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return !(playerIn instanceof FakePlayer);
	}

	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tileMM);
	}

	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int i = 0; i < this.listeners.size(); ++i)
		{
			IContainerListener icontainerlistener = this.listeners.get(i);
			if (this.liquidMana != this.tileMM.getField(0))
			{
				icontainerlistener.sendWindowProperty(this, 0, this.tileMM.getField(0));
			}
			if (this.isLiquid != this.tileMM.getField(1))
			{
				icontainerlistener.sendWindowProperty(this, 1, this.tileMM.getField(1));
			}
		}

		this.liquidMana = this.tileMM.getField(0);
		this.isLiquid = this.tileMM.getField(1);
	}
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data)
	{
		this.tileMM.setField(id, data);
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (0 <= index && index <= 13)
			{
				if (!this.mergeItemStack(itemstack1, 14, 50, true))
				{
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (!(0 <= index && index <= 13))
			{
				if (!this.mergeItemStack(itemstack1, 0, 14, false))
				{
					return ItemStack.EMPTY;
				}
				else if (14 <= index && index <= 40)
				{
					if (!this.mergeItemStack(itemstack1, 41, 50, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (41 <= index && index <= 49 && !this.mergeItemStack(itemstack1, 14, 41, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 14, 50, false))
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

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}
}
