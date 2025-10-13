package neo_ores.entity.ai;

import java.util.List;

import net.minecraft.util.math.BlockPos;

public interface IArrangeBlocks
{
	public void arrangeBlocks(List<BlockPos> blocks, boolean targetUpward);
}
