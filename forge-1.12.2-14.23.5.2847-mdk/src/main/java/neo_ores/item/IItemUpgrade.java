package neo_ores.item;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public interface IItemUpgrade
{
	public void upgrade(TileEntity tileentity, ItemStack stack);
	
	public int maxApply(ItemStack stack);
}
