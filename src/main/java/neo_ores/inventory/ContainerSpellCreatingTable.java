package neo_ores.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerSpellCreatingTable extends Container
{
	
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		return false;
	}
}
