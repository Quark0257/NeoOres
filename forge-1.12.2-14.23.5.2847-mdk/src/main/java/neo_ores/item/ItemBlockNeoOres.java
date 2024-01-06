package neo_ores.item;

import neo_ores.block.INeoOresBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockNeoOres extends ItemBlock
{
	public <T extends Block & INeoOresBlock> ItemBlockNeoOres(T block)
	{
		super(block);
	}

	public int getMetadata(int damage)
	{
		return 0;
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return ((INeoOresBlock) this.getBlock()).getUnlocalizedName(stack);
	}
}
