package neo_ores.item;

import neo_ores.block.INeoOresBlock;
import neo_ores.main.NeoOresBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockNeoSapling extends ItemBlockNeoOres
{
	public <T extends Block & INeoOresBlock> ItemBlockNeoSapling(T block)
	{
		super(block);
	}

	public int getItemBurnTime(ItemStack stack)
	{
		if (NeoOresBlocks.color_saplings.contains(this.block))
		{
			int i = NeoOresBlocks.color_saplings.indexOf(this.block);
			if (3 <= i && i <= 5)
				return 0;
			else if (6 <= i && i <= 8)
				return 200;
		}
		return 100;
	}
}
