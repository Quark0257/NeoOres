package neo_ores.inventory;

import neo_ores.main.NeoOresItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotEarthEssenceCore extends Slot
{
	public SlotEarthEssenceCore(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition)
    {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem() == NeoOresItems.earth_essence_core;
    }
}
