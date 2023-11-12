package neo_ores.api;

import java.util.List;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public interface IStateManager {
	public List<IBlockState> getAvailableStateFromBlocks(BlockPos target, Map<BlockPos,IBlockState> state);
}
