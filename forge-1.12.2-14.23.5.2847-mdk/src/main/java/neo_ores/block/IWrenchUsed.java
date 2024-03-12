package neo_ores.block;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IWrenchUsed
{
	public void wrench(World world, BlockPos pos, ItemStack stack);
}
