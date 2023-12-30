package neo_ores.world.gen.structures.air;

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

public class AirStructurePieces {
	public static final StructurePiece BOSS = new StructurePiece("air_str_boss", new BlockPos(25,9,25), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(13,8,12)}));
			map.put(Type.PILLAR, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.BRANCH1, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.BRANCH2, 10));
			return pieces;
		}
	};
	
	public static final StructurePiece END1 = new StructurePiece("air_str_end1", new BlockPos(7,2,7), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(3,0,3)}));
			map.put(Type.PILLAR, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			return pieces;
		}
	};
	
	public static final StructurePiece END2 = new StructurePiece("air_str_end2", new BlockPos(11,4,11), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(5,0,5)}));
			map.put(Type.PILLAR, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			return pieces;
		}
	};
	
	public static final StructurePiece PILLAR1 = new StructurePiece("air_str_pillar1", new BlockPos(3,7,3), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(0,0,2)}));
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(1,6,1)}));
			map.put(Type.PILLAR, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.BRANCH1, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.BRANCH2, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.END1, 2));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.END2, 2));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.FLOOR, 8));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.SPAWNER, 10));
			return pieces;
		}
	};
	public static final StructurePiece PILLAR2 = new StructurePiece("air_str_pillar2", new BlockPos(3,7,3), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(0,0,2)}));
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(2,6,1)}));
			map.put(Type.PILLAR, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.BRANCH1, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.BRANCH2, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.END1, 2));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.END2, 2));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.FLOOR, 8));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.SPAWNER, 10));
			return pieces;
		}
	};
	
	public static final StructurePiece PILLAR_CHEST = new StructurePiece("air_str_pillar_chest", new BlockPos(13,7,13), true) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(6,0,6)}));
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(6,6,5)}));
			map.put(Type.PILLAR, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.BRANCH1, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.BRANCH2, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.END1, 2));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.END2, 2));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.FLOOR, 8));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.SPAWNER, 10));
			return pieces;
		}
	};
	
	public static final StructurePiece FLOOR = new StructurePiece("air_str_floor1", new BlockPos(27,6,27), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(21,0,5)}));
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(13,5,13)}));
			map.put(Type.PILLAR, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.PILLAR1, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.PILLAR2, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.PILLAR_CHEST, 3));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.BRANCH1, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.BRANCH2, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.END1, 2));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.END2, 2));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.FLOOR, 8));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.SPAWNER, 10));
			return pieces;
		}
	};
	
	public static final StructurePiece SPAWNER = new StructurePiece("air_str_spawner", new BlockPos(27,7,27), true) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(21,0,5)}));
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(13,6,12)}));
			map.put(Type.PILLAR, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.PILLAR1, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.PILLAR2, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.END1, 1));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.END2, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece BRANCH1 = new StructurePiece("air_str_branch1", new BlockPos(31,8,19), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(25,0,5)}));
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(2,7,9),new BlockPos(27,7,9)}));
			map.put(Type.PILLAR, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.PILLAR1, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.PILLAR2, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.PILLAR_CHEST, 3));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.BRANCH1, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.BRANCH2, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.END1, 2));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.END2, 2));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.FLOOR, 8));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.SPAWNER, 10));
			return pieces;
		}
	};
	
	public static final StructurePiece BRANCH2 = new StructurePiece("air_str_branch2", new BlockPos(31,8,25), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(25,0,6)}));
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(7,7,12), new BlockPos(24,7,12)}));
			map.put(Type.PILLAR, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.PILLAR1, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.PILLAR2, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.PILLAR_CHEST, 3));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.BRANCH1, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.BRANCH2, 10));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.END1, 2));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.END2, 2));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.FLOOR, 8));
			pieces.add(new StructurePieceAndWeight(AirStructurePieces.SPAWNER, 10));
			return pieces;
		}
	};
	
	public static void registerPieces()
    {
        MapGenStructureIO.registerStructureComponent(AirStructurePieceComponent.class, "ASPC");
    }
}
