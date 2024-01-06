package neo_ores.item;

import neo_ores.block.INeoOresBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockDimensionWood extends ItemBlock
{
	public <T extends Block & INeoOresBlock> ItemBlockDimensionWood(T block)
	{
		super(block);
		if (block.getMaxMeta() > 0)
			this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	public int getMetadata(int damage)
	{
		return damage;
	}
	
	public int getItemBurnTime(ItemStack stack) {
		int i = stack.getMetadata() % 4;
		if(i == 0 || i == 2) return 300;
		else if(i == 3) return 600;
		return 0;
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return ((INeoOresBlock) this.getBlock()).getUnlocalizedName(stack);
	}
}
