package neo_ores.world.gen.structures;

import java.util.List;
import java.util.Map;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public abstract class StructurePiece
{
	public static enum Type
	{
		BRIDGE(0), TUNNEL(1), PILLAR(2), DOWN_BRIDGE(3), UP_BRIDGE(4);
		private int meta;

		Type(int meta)
		{
			this.meta = meta;
		}

		public int getMeta()
		{
			return this.meta;
		}
	}

	public final String structureName;
	public final BlockPos size;
	public final boolean hasChest;

	public StructurePiece(String name, BlockPos size, boolean hasChest)
	{
		this.structureName = name;
		this.size = size;
		this.hasChest = hasChest;
	}

	/**
	 * Face == Up or Down : Set port pos that applying any rotation is ok.
	 * 
	 * @return
	 */
	public abstract Map<StructurePiece.Type, Map<EnumFacing, List<BlockPos>>> getPorts();

	public abstract List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type);

	public BlockPos offset()
	{
		return new BlockPos(0, 0, 0);
	}
}
