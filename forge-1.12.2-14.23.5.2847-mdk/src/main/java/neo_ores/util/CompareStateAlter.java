package neo_ores.util;

import java.util.Map;

import neo_ores.api.ICompareBlockState;
import neo_ores.api.IStateManager;
import neo_ores.api.StairsManager;
import neo_ores.api.Structure;
import neo_ores.api.StructureUtils;
import neo_ores.block.BlockEnhancedPedestal;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class CompareStateAlter implements ICompareBlockState {

	private final Map<BlockPos,IBlockState> map;
	private final IStateManager sm;
	
	public CompareStateAlter(Structure str) {
		this.map = StructureUtils.makeMap(str);
		this.sm = new StairsManager();
	}
	
	/**
	 * @param a state from world
	 * @param b state from recipe or structure
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean compare(BlockPos pos, IBlockState a, IBlockState b) {
		if(a.getBlock() instanceof BlockStairs) {
			for(IBlockState state : this.sm.getAvailableStateFromBlocks(pos, this.map)) {
				if(a.getValue(BlockStairs.HALF) == state.getValue(BlockStairs.HALF) && a.getValue(BlockStairs.FACING) == state.getValue(BlockStairs.FACING)) {
					return true;
				}
			}
		}
		if(b.getBlock() == Blocks.GLOWSTONE) return a.getBlock().getLightValue(a) == 15;
		if(b.getBlock() instanceof BlockEnhancedPedestal) return a.getBlock() instanceof BlockEnhancedPedestal;
		return a == b;
	}
}
