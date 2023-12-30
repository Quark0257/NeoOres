package neo_ores.world.gen.structures;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

public class StructurePieceData {
	
	public final StructurePiece piece;
	public final Rotation rotation;
	public final Mirror mirror;
	public final BlockPos relativePos;
	public final String id;
	public StructurePieceData(StructurePiece piece,String connectingID, BlockPos relativePos,Rotation rot, Mirror mirror) {
		this.piece = piece;
		this.rotation = rot;
		this.mirror = mirror;
		this.relativePos = relativePos;
		this.id = connectingID;
	}

}
