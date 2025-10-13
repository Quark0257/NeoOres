package neo_ores.api;

import neo_ores.main.NeoOresBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ICompareBlockState
{
	public static final ICompareBlockState DEFAULT = new ICompareBlockState()
	{
		@Override
		public boolean compare(World world, BlockPos pos, IBlockState a, IBlockState b)
		{
			return a == b;
		}
	};

	public static final ICompareBlockState ITEM = new ICompareBlockState()
	{
		@SuppressWarnings("deprecation")
		@Override
		public boolean compare(World world, BlockPos pos, IBlockState a, IBlockState b)
		{
			ItemStack stackA = a.getBlock().getItem(world, pos, a);
			ItemStack stackB = b.getBlock().getItem(world, pos, b);
			return stackA.getItem() == stackB.getItem() && stackA.getMetadata() == stackB.getMetadata();
		}
	};

	public static final ICompareBlockState PI = new ICompareBlockState()
	{
		@Override
		public boolean compare(World world, BlockPos pos, IBlockState a, IBlockState b)
		{
			return (a.getBlock() == NeoOresBlocks.enhanced_pedestal || a.getBlock() == NeoOresBlocks.pedestal)
					&& (b.getBlock() == NeoOresBlocks.enhanced_pedestal || b.getBlock() == NeoOresBlocks.pedestal);
		}
	};

	public boolean compare(World world, BlockPos pos, IBlockState a, IBlockState b);
}
