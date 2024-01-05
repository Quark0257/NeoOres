package neo_ores.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;

public class StructureUtils
{
	public static boolean isMatch(World world, Structure str, ICompareBlockState icbs)
	{
		for (Entry<BlockPos, IBlockState> entry : makeMap(str).entrySet())
		{
			if (!icbs.compare(entry.getKey(), world.getBlockState(entry.getKey()), entry.getValue()))
			{
				return false;
			}
		}
		return true;
	}

	public static Map<BlockPos, IBlockState> makeMap(Structure str)
	{
		Map<BlockPos, IBlockState> map = new HashMap<BlockPos, IBlockState>();
		for (BlockInfo blockinfo : str.getStrTemplate().getBlocks())
		{
			map.put(blockinfo.pos.add(str.getPos()), blockinfo.blockState);
		}
		return map;
	}
}
