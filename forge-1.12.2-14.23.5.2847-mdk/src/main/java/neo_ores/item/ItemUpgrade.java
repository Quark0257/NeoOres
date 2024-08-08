package neo_ores.item;

import neo_ores.tileentity.TileEntityMechanicalMagician;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ItemUpgrade extends INeoOresItem.Impl implements IItemUpgrade
{
	public ItemUpgrade() {
		
	}

	@Override
	public void upgrade(TileEntity tileentity, ItemStack stack)
	{
		if(tileentity instanceof TileEntityMechanicalMagician) 
		{
			TileEntityMechanicalMagician temm = (TileEntityMechanicalMagician)tileentity;
			this.upgrade(temm);
		}
	}
	
	public void upgrade(TileEntityMechanicalMagician temm) 
	{
	}

	@Override
	public int maxApply(ItemStack stack)
	{
		return 1;
	}
}
