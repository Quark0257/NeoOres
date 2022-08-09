package neo_ores.item;

import neo_ores.block.BlockNeoOre;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockNeoOre extends ItemBlock
{
	public ItemBlockNeoOre(Block block) 
	{
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}
	
	public int getMetadata(int damage)
	{
		return damage;
	}
	
	public String getUnlocalizedName(ItemStack stack)
	{
		return ((BlockNeoOre)this.getBlock()).getUnlocalizedName(stack);
	}
}
