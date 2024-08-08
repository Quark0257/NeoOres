package neo_ores.inventory;

import java.util.List;

import neo_ores.api.spell.SpellItem;
import neo_ores.item.ISpellRecipeWritable;
import neo_ores.item.ISpellWritable;
import neo_ores.tileentity.TileEntitySpellRecipeCreationTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSpellRecipeCreationTable extends Container
{
	private final TileEntitySpellRecipeCreationTable tileSRCT;
	private List<SpellItem> selectedSpells;

	public ContainerSpellRecipeCreationTable(InventoryPlayer playerInventory, TileEntitySpellRecipeCreationTable SRCT)
	{
		this.tileSRCT = SRCT;
		this.selectedSpells = SRCT.getSpellItems();
		this.addSlotToContainer(new Slot(SRCT, 0, 320, 154)
		{
			public boolean isItemValid(ItemStack stack)
			{
				return stack.getItem() instanceof ISpellRecipeWritable || stack.getItem() instanceof ISpellWritable;
			}

			public int getSlotStackLimit()
			{
				return 1;
			}
		});

		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 117 + j * 18, 126 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k)
		{
			this.addSlotToContainer(new Slot(playerInventory, k, 117 + k * 18, 184));
		}
	}

	public List<SpellItem> getSpellItems()
	{
		return this.selectedSpells;
	}

	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tileSRCT);
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.tileSRCT.isUsableByPlayer(playerIn);
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 0)
			{
				if (!this.mergeItemStack(itemstack1, 1, 37, true))
				{
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index != 0)
			{
				if (itemstack1.getItem() instanceof ISpellRecipeWritable)
				{
					if (!this.mergeItemStack(itemstack1, 0, 1, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (index >= 1 && index < 28)
				{
					if (!this.mergeItemStack(itemstack1, 28, 37, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (index >= 28 && index < 37 && !this.mergeItemStack(itemstack1, 1, 28, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 1, 28, false))
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
