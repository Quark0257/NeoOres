package neo_ores.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class StairsManager implements IStateManager
{

	@Override
	public List<IBlockState> getAvailableStateFromBlocks(BlockPos target, Map<BlockPos, IBlockState> states)
	{
		List<IBlockState> list = new ArrayList<IBlockState>();
		if (states.get(target) != null)
		{
			for (int i = 0; i < 4; i++)
			{
				BlockPos pos = target.add(MathUtils.sin(i), 0, MathUtils.sin(i + 1));
				BlockPos pos1 = target.add(MathUtils.sin(i + 1), 0, MathUtils.sin(i + 2));
				if (states.containsKey(pos) && states.containsKey(pos1))
				{
					if (states.get(target).getBlock() == states.get(pos).getBlock() && states.get(target).getBlock() == states.get(pos1).getBlock())
					{
						if (existAnotherState(target, pos, states.get(pos), pos1, states.get(pos1)))
						{
							list.add(states.get(pos));
							list.add(states.get(pos1));
						}
					}
				}
			}
		}
		return list;
	}

	private static boolean existAnotherState(BlockPos target, BlockPos pos, IBlockState state, BlockPos pos1, IBlockState state1)
	{
		if (state1.getValue(BlockStairs.HALF) == state.getValue(BlockStairs.HALF))
		{
			EnumFacing face = state.getValue(BlockStairs.FACING);
			EnumFacing face1 = state1.getValue(BlockStairs.FACING);
			if (EnumFacing.getHorizontal(face.getHorizontalIndex() - 1) == face1 || EnumFacing.getHorizontal(face.getHorizontalIndex() + 1) == face1)
			{
				return target.subtract(pos1).equals(face.getDirectionVec()) && target.subtract(pos).equals(face1.getDirectionVec())
						|| target.subtract(pos1).equals(face.getOpposite().getDirectionVec()) && target.subtract(pos).equals(face1.getOpposite().getDirectionVec());
			}
		}
		return false;
	}
}
