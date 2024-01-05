package neo_ores.inventory;

import neo_ores.main.NeoOresItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotWaterEssenceCore extends Slot
{
	public SlotWaterEssenceCore(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition)
	{
		super(inventoryIn, slotIndex, xPosition, yPosition);
	}

	public boolean isItemValid(ItemStack stack)
	{
		return stack.getItem() == NeoOresItems.water_essence_core;
	}
}
