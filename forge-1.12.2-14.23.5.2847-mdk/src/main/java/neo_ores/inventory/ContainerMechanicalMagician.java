package neo_ores.inventory;

import neo_ores.tileentity.TileEntityMechanicalMagician;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerMechanicalMagician extends Container
{
	public ContainerMechanicalMagician(InventoryPlayer playerInventory, TileEntityMechanicalMagician tileMM) {
		
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

}
