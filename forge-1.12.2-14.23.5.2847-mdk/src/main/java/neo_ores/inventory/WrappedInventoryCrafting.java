package neo_ores.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;

public class WrappedInventoryCrafting extends InventoryCrafting
{
	public WrappedInventoryCrafting(int width, int height)
	{
		super(new Container()
		{
			@Override
			public void onCraftMatrixChanged(IInventory inventoryIn)
			{
			}

			@Override
			public boolean canInteractWith(EntityPlayer playerIn)
			{
				return false;
			}
		}, width, height);
	}
}
