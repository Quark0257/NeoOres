package neo_ores.world.gen.structures;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

public class StructurePieceAndOption {
	public final StructurePiece piece;
	public final Rotation rotation;
	public final BlockPos relativePos;
	public final BlockPos usedPortPos;
	public boolean bossKey;
	public StructurePieceAndOption(StructurePiece piece, BlockPos relativePos, Rotation rot, BlockPos usedPortPos, boolean bossKey) {
		this.piece = piece;
		this.rotation = rot;
		this.relativePos = relativePos;
		this.usedPortPos = usedPortPos;
		this.bossKey = bossKey;
	}
	
	public String toString() {
		return piece.structureName;
	}
}
