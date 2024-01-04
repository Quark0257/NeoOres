package neo_ores.item;

import neo_ores.block.INeoOresBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockDimension extends ItemBlock
{
	public <T extends Block & INeoOresBlock> ItemBlockDimension(T block) 
	{
		super(block);
		if(block.getMaxMeta() > 0) this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}
	
	public int getMetadata(int damage)
	{
		return damage;
	}
	
	public String getUnlocalizedName(ItemStack stack)
	{
		return ((INeoOresBlock)this.getBlock()).getUnlocalizedName(stack);
	}
}
