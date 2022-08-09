package neo_ores.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemManaBlock extends ItemBlock
{
	public ItemManaBlock(Block block) 
	{
		super(block);
	}
	public boolean hasEffect(ItemStack itemStack)
	{
		return true;
	}
}
