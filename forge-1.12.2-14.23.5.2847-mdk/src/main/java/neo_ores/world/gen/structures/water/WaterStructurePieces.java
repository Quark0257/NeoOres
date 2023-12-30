package neo_ores.world.gen.structures.water;

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

public class WaterStructurePieces {
	public static final StructurePiece BOSS = new StructurePiece("water_str_boss", new BlockPos(7,7,7), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(3,6,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 4));
			return pieces;
		}
	};
	
	public static final StructurePiece END1 = new StructurePiece("water_str_end1", new BlockPos(7,7,7), true) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(6,3,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(null, 4));
			return pieces;
		}
	};
	
	public static final StructurePiece END2 = new StructurePiece("water_str_end2", new BlockPos(7,7,7), true) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(3,6,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(null, 4));
			return pieces;
		}
	};
	
	public static final StructurePiece END3 = new StructurePiece("water_str_end3", new BlockPos(7,7,7), true) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(3,0,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(null, 4));
			return pieces;
		}
	};
	
	public static final StructurePiece s2_1 = new StructurePiece("water_str_2_1", new BlockPos(11,11,7), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(3,0,3)}));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(10,7,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s2_2 = new StructurePiece("water_str_2_2", new BlockPos(11,11,7), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(3,10,3)}));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(10,3,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s2_3 = new StructurePiece("water_str_2_3", new BlockPos(7,15,7), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(3,14,3)}));
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(3,0,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	public static final StructurePiece s2_4 = new StructurePiece("water_str_2_4", new BlockPos(15,7,7), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,3,3)}));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,3,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s3_1 = new StructurePiece("water_str_3_1", new BlockPos(15,11,7), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(7,10,3)}));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,3,3)}));
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,3,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s3_6 = new StructurePiece("water_str_3_6", new BlockPos(15,11,7), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(7,0,3)}));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,7,3)}));
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,7,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s3_2 = new StructurePiece("water_str_3_2", new BlockPos(11,15,7), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(3,0,3)}));
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(3,14,3)}));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(10,7,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s4_1 = new StructurePiece("water_str_4_1", new BlockPos(15,15,7), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(7,0,3)}));
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(7,14,3)}));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,7,3)}));
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,7,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s5_1 = new StructurePiece("water_str_5_1", new BlockPos(15,15,11), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(7,0,3)}));
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(7,14,3)}));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,7,3)}));
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,7,3)}));
			bridges.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(7,7,10)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s3_3 = new StructurePiece("water_str_3_3", new BlockPos(15,7,11), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,3,3)}));
			bridges.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(7,3,10)}));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,3,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s2_5 = new StructurePiece("water_str_2_5", new BlockPos(11,7,11), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,3,3)}));
			bridges.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(7,3,10)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s4_2 = new StructurePiece("water_str_4_2", new BlockPos(15,11,11), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,3,3)}));
			bridges.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(7,3,10)}));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,3,3)}));
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(7,10,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s4_3 = new StructurePiece("water_str_4_3", new BlockPos(15,11,11), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,7,3)}));
			bridges.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(7,7,10)}));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,7,3)}));
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(7,0,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s3_4 = new StructurePiece("water_str_3_4", new BlockPos(11,11,11), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,3,3)}));
			bridges.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(7,3,10)}));
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(7,10,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s3_5 = new StructurePiece("water_str_3_5", new BlockPos(11,11,11), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,7,3)}));
			bridges.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(7,7,10)}));
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(7,0,3)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s6 = new StructurePiece("water_str_6", new BlockPos(15,15,15), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,7,7)}));
			bridges.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(7,7,14)}));
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(7,0,7)}));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,7,7)}));
			bridges.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] {new BlockPos(7,7,0)}));
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(7,14,7)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s5_2 = new StructurePiece("water_str_5_2", new BlockPos(15,11,15), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,7,7)}));
			bridges.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(7,7,14)}));
			bridges.put(EnumFacing.DOWN, Arrays.asList(new BlockPos[] {new BlockPos(7,0,7)}));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,7,7)}));
			bridges.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] {new BlockPos(7,7,0)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s5_3 = new StructurePiece("water_str_5_3", new BlockPos(15,11,15), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,3,7)}));
			bridges.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(7,3,14)}));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,3,7)}));
			bridges.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] {new BlockPos(7,3,0)}));
			bridges.put(EnumFacing.UP, Arrays.asList(new BlockPos[] {new BlockPos(7,10,7)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static final StructurePiece s4_4 = new StructurePiece("water_str_4_4", new BlockPos(15,7,15), false) {
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts() {
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] {new BlockPos(0,3,7)}));
			bridges.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] {new BlockPos(7,3,14)}));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] {new BlockPos(14,3,7)}));
			bridges.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] {new BlockPos(7,3,0)}));
			map.put(Type.TUNNEL, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type) {
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s5_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s4_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s3_6, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_1, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_2, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_3, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_4, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.s2_5, 1));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END1, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END2, 4));
			pieces.add(new StructurePieceAndWeight(WaterStructurePieces.END3, 4));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};
	
	public static void registerPieces()
    {
        MapGenStructureIO.registerStructureComponent(WaterStructurePieceComponent.class, "WSPC");
    }
}
