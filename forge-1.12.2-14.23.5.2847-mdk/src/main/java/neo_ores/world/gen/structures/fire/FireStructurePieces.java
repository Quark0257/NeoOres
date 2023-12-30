package neo_ores.world.gen.structures.fire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import neo_ores.world.gen.structures.StructurePiece;
import neo_ores.world.gen.structures.StructurePieceAndWeight;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.MapGenStructureIO;

public class FireStructurePieces {
	public static final StructurePiece BOSS = new StructurePiece("fire_str_boss", new BlockPos(17,17,17), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bottoms = new HashMap<EnumFacing, List<BlockPos>>();
			bottoms.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,0,0)}));
			bottoms.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(0,0,16)}));
			bottoms.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(16,0,16)}));
			bottoms.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] {new BlockPos(16,0,0)}));
			map.put(Type.UP_BRIDGE, bottoms);
			Map<EnumFacing, List<BlockPos>> tops = new HashMap<EnumFacing, List<BlockPos>>();
			tops.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,16,0)}));
			tops.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(0,16,16)}));
			tops.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(16,16,16)}));
			tops.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] {new BlockPos(16,16,0)}));
			map.put(Type.DOWN_BRIDGE, tops);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.ARCH3,4));
			pieces.add(new StructurePieceAndWeight(null,1));
			return pieces;
		}
	};
	
	public static final StructurePiece ARCH1 = new StructurePiece("fire_str_arch1", new BlockPos(6,6,6), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> up = new HashMap<EnumFacing, List<BlockPos>>();
			up.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(5,6,6)}));
			map.put(Type.UP_BRIDGE, up);
			Map<EnumFacing, List<BlockPos>> down = new HashMap<EnumFacing, List<BlockPos>>();
			down.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,-1,-1)}));
			map.put(Type.DOWN_BRIDGE, down);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.SPAWNER1,4));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.SPAWNER1,4));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.SPAWNER3,16));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.ROOM2,4));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.ROOM1,4));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.END1,1));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.END2,1));
			return pieces;
		}
		
		@Override
		public BlockPos offset() {
			return new BlockPos(-3,-3,-3);
		}
	};
	
	public static final StructurePiece ARCH3 = new StructurePiece("fire_str_arch3", new BlockPos(2,2,2), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> up = new HashMap<EnumFacing, List<BlockPos>>();
			up.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(1,2,2)}));
			map.put(Type.UP_BRIDGE, up);
			Map<EnumFacing, List<BlockPos>> down = new HashMap<EnumFacing, List<BlockPos>>();
			down.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,-1,-1)}));
			map.put(Type.DOWN_BRIDGE, down);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.SPAWNER1,4));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.SPAWNER1,4));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.SPAWNER3,16));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.ROOM2,4));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.ROOM1,4));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.END1,1));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.END2,1));
			return pieces;
		}
		
		@Override
		public BlockPos offset() {
			return new BlockPos(-3,-3,-3);
		}
	};
	
	public static final StructurePiece ARCH2 = new StructurePiece("fire_str_arch2", new BlockPos(12,12,12), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> up = new HashMap<EnumFacing, List<BlockPos>>();
			up.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(11,12,12)}));
			map.put(Type.UP_BRIDGE, up);
			Map<EnumFacing, List<BlockPos>> down = new HashMap<EnumFacing, List<BlockPos>>();
			down.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,-1,-1)}));
			map.put(Type.DOWN_BRIDGE, down);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.SPAWNER1,4));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.SPAWNER1,4));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.SPAWNER3,16));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.ROOM2,4));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.ROOM1,4));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.END1,1));
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.END2,1));
			return pieces;
		}
		
		@Override
		public BlockPos offset() {
			return new BlockPos(-3,-3,-3);
		}
	};
	
	public static final StructurePiece SPAWNER1 = new StructurePiece("fire_str_spawner1", new BlockPos(15,15,15), true) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bottoms = new HashMap<EnumFacing, List<BlockPos>>();
			bottoms.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,0,0)}));
			bottoms.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(0,0,14)}));
			bottoms.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,0,14)}));
			bottoms.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] {new BlockPos(14,0,0)}));
			map.put(Type.UP_BRIDGE, bottoms);
			Map<EnumFacing, List<BlockPos>> tops = new HashMap<EnumFacing, List<BlockPos>>();
			tops.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,14,0)}));
			tops.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(0,14,14)}));
			tops.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,14,14)}));
			tops.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] {new BlockPos(14,14,0)}));
			map.put(Type.DOWN_BRIDGE, tops);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.ARCH3,4));
			pieces.add(new StructurePieceAndWeight(null,4));
			return pieces;
		}
	};
	
	public static final StructurePiece SPAWNER2 = new StructurePiece("fire_str_spawner2", new BlockPos(15,15,15), true) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bottoms = new HashMap<EnumFacing, List<BlockPos>>();
			bottoms.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,0,0)}));
			bottoms.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(0,0,14)}));
			bottoms.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,0,14)}));
			bottoms.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] {new BlockPos(14,0,0)}));
			map.put(Type.UP_BRIDGE, bottoms);
			Map<EnumFacing, List<BlockPos>> tops = new HashMap<EnumFacing, List<BlockPos>>();
			tops.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,14,0)}));
			tops.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(0,14,14)}));
			tops.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,14,14)}));
			tops.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] {new BlockPos(14,14,0)}));
			map.put(Type.DOWN_BRIDGE, tops);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.ARCH3,4));
			pieces.add(new StructurePieceAndWeight(null,4));
			return pieces;
		}
	};
	
	public static final StructurePiece SPAWNER3 = new StructurePiece("fire_str_spawner3", new BlockPos(15,15,15), true) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bottoms = new HashMap<EnumFacing, List<BlockPos>>();
			bottoms.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,0,0)}));
			bottoms.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(0,0,14)}));
			bottoms.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,0,14)}));
			bottoms.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] {new BlockPos(14,0,0)}));
			map.put(Type.UP_BRIDGE, bottoms);
			Map<EnumFacing, List<BlockPos>> tops = new HashMap<EnumFacing, List<BlockPos>>();
			tops.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,14,0)}));
			tops.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(0,14,14)}));
			tops.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,14,14)}));
			tops.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] {new BlockPos(14,14,0)}));
			map.put(Type.DOWN_BRIDGE, tops);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.ARCH3,4));
			pieces.add(new StructurePieceAndWeight(null,4));
			return pieces;
		}
	};
	
	public static final StructurePiece ROOM2 = new StructurePiece("fire_str_room2", new BlockPos(15,15,15), true) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bottoms = new HashMap<EnumFacing, List<BlockPos>>();
			bottoms.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,0,0)}));
			bottoms.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(0,0,14)}));
			bottoms.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,0,14)}));
			bottoms.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] {new BlockPos(14,0,0)}));
			map.put(Type.UP_BRIDGE, bottoms);
			Map<EnumFacing, List<BlockPos>> tops = new HashMap<EnumFacing, List<BlockPos>>();
			tops.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,14,0)}));
			tops.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(0,14,14)}));
			tops.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,14,14)}));
			tops.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] {new BlockPos(14,14,0)}));
			map.put(Type.DOWN_BRIDGE, tops);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.ARCH3,4));
			pieces.add(new StructurePieceAndWeight(null,4));
			return pieces;
		}
	};
	
	public static final StructurePiece ROOM1 = new StructurePiece("fire_str_room1", new BlockPos(13,13,13), true) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bottoms = new HashMap<EnumFacing, List<BlockPos>>();
			bottoms.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,0,0)}));
			bottoms.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(0,0,12)}));
			bottoms.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(12,0,12)}));
			bottoms.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] {new BlockPos(12,0,0)}));
			map.put(Type.UP_BRIDGE, bottoms);
			Map<EnumFacing, List<BlockPos>> tops = new HashMap<EnumFacing, List<BlockPos>>();
			tops.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,12,0)}));
			tops.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(0,12,12)}));
			tops.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(12,12,12)}));
			tops.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] {new BlockPos(12,12,0)}));
			map.put(Type.DOWN_BRIDGE, tops);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(FireStructurePieces.ARCH3,4));
			pieces.add(new StructurePieceAndWeight(null,4));
			return pieces;
		}
	};
	
	public static final StructurePiece END2 = new StructurePiece("fire_str_end2", new BlockPos(8,8,8), true) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> tops = new HashMap<EnumFacing, List<BlockPos>>();
			tops.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(7,7,7)}));
			map.put(Type.DOWN_BRIDGE, tops);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			return pieces;
		}
	};
	
	public static final StructurePiece END1 = new StructurePiece("fire_str_end1", new BlockPos(8,8,8), true) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bottoms = new HashMap<EnumFacing, List<BlockPos>>();
			bottoms.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(7,0,7)}));
			map.put(Type.UP_BRIDGE, bottoms);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			return pieces;
		}
	};
	
	public static void registerPieces()
    {
        MapGenStructureIO.registerStructureComponent(FireStructurePieceComponent.class, "FSPC");
    }
}
