package neo_ores.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public interface ICompareBlockState
{
	public static final ICompareBlockState DEFAULT = new ICompareBlockState()
	{
		@Override
		public boolean compare(BlockPos pos, IBlockState a, IBlockState b)
		{
			return a == b;
		}
	};

	public boolean compare(BlockPos pos, IBlockState a, IBlockState b);
}
