package neo_ores.pi;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IPIListener
{
	public void setItemList(List<ItemStack> list);
	
	public void setResult(ItemStack stack);
	
	public void executed();
}
