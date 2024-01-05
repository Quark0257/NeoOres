package neo_ores.world.gen.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import neo_ores.api.MathUtils;
import neo_ores.world.gen.structures.StructurePiece.Type;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

public class StructurePieceUtils
{
	public static List<SPOsAndWeight> getConnectablePieces(BlockPos portPos, Type portType, EnumFacing portFace, List<StructurePieceAndWeight> candidates)
	{
		List<SPOsAndWeight> spoaw = new ArrayList<SPOsAndWeight>();
		for (StructurePieceAndWeight candidate : candidates)
		{
			List<StructurePieceAndOption> pieces = new ArrayList<StructurePieceAndOption>();

			if (candidate.piece == null)
			{
				pieces.add(null);
				spoaw.add(new SPOsAndWeight(pieces, candidate.weight));
				continue;
			}

			Map<EnumFacing, List<BlockPos>> map = candidate.piece.getPorts().getOrDefault(portType, null);
			if (map == null || map.isEmpty())
				continue;

			if (portFace.getAxis() != Axis.Y)
			{
				EnumFacing comparedFace = portFace;
				for (int i = 0; i < 4; i++)
				{
					List<BlockPos> poses = map.getOrDefault(comparedFace.getOpposite(), null);
					if (!(poses == null || poses.isEmpty()))
					{
						for (BlockPos pos : poses)
						{
							BlockPos offset = MathUtils.rot(pos.offset(comparedFace.getOpposite(), 1), Rotation.values()[i]);
							BlockPos relativePos = portPos.subtract(offset);
							pieces.add(new StructurePieceAndOption(candidate.piece, relativePos, Rotation.values()[i], pos, false));
						}
					}
					comparedFace = Rotation.COUNTERCLOCKWISE_90.rotate(comparedFace);
				}
			}
			else
			{
				List<BlockPos> poses = map.getOrDefault(portFace.getOpposite(), null);
				if (poses == null || poses.isEmpty())
					continue;
				for (int i = 0; i < 4; i++)
				{
					for (BlockPos pos : poses)
					{
						BlockPos offset = MathUtils.rot(pos.offset(portFace.getOpposite(), 1), Rotation.values()[i]);
						BlockPos relativePos = portPos.subtract(offset);
						pieces.add(new StructurePieceAndOption(candidate.piece, relativePos, Rotation.values()[i], pos, false));
					}
				}
			}
			if (!pieces.isEmpty())
				spoaw.add(new SPOsAndWeight(pieces, candidate.weight));
		}
		return spoaw;
	}
}
