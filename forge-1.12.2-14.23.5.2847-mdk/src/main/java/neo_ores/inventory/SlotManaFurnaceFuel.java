package neo_ores.inventory;

import neo_ores.tileentity.TileEntityManaFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotManaFurnaceFuel extends Slot
{
	public SlotManaFurnaceFuel(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition)
	{
		super(inventoryIn, slotIndex, xPosition, yPosition);
	}

	public boolean isItemValid(ItemStack stack)
	{
		return TileEntityManaFurnace.isItemFuel(stack);
	}

	public int getItemStackLimit(ItemStack stack)
	{
		return super.getItemStackLimit(stack);
	}
}
