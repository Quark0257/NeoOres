package neo_ores.items;

import neo_ores.blocks.BlockDimension;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockDimension extends ItemBlock
{
	public ItemBlockDimension(Block block) 
	{
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setRegistryName(this.getBlock().getRegistryName());
	}
	
	public int getMetadata(int damage)
	{
		return damage;
	}
	
	public String getUnlocalizedName(ItemStack stack)
	{
		return ((BlockDimension)this.getBlock()).getUnlocalizedName(stack);
	}
}
