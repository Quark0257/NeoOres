package neo_ores.pi;

import net.minecraft.inventory.InventoryBasic;

public class InventoryPI extends InventoryBasic
{
	public InventoryPI(String title, boolean customName, int slotCount)
    {
		super(title, customName, slotCount);
    }

	public int getInventoryStackLimit()
    {
        return Integer.MAX_VALUE;
    }
}
