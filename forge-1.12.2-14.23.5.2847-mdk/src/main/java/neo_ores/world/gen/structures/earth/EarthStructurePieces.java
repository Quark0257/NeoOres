package neo_ores.world.gen.structures.earth;

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

public class EarthStructurePieces
{

	public static final StructurePiece BRIDGE_STRAIGHT = new StructurePiece("earth_str_bridge_straight", new BlockPos(23, 11, 7), false)
	{

		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts()
		{
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] { new BlockPos(0, 5, 3) }));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] { new BlockPos(22, 5, 3) }));
			map.put(Type.BRIDGE, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type)
		{
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(BRIDGE_CROSS, 12));
			pieces.add(new StructurePieceAndWeight(BRIDGE_STRAIGHT, 12));
			pieces.add(new StructurePieceAndWeight(DOWNSTAIRS_BRIDGE, 12));
			pieces.add(new StructurePieceAndWeight(null, 4));
			return pieces;
		}
	};
	public static final StructurePiece BRIDGE_CROSS = new StructurePiece("earth_str_bridge_cross", new BlockPos(23, 11, 23), false)
	{
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts()
		{
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] { new BlockPos(0, 5, 11) }));
			bridges.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] { new BlockPos(11, 5, 22) }));
			bridges.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] { new BlockPos(22, 5, 11) }));
			bridges.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] { new BlockPos(11, 5, 0) }));
			map.put(Type.BRIDGE, bridges);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type)
		{
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(BRIDGE_CROSS, 12));
			pieces.add(new StructurePieceAndWeight(BRIDGE_STRAIGHT, 12));
			pieces.add(new StructurePieceAndWeight(DOWNSTAIRS_BRIDGE, 12));
			pieces.add(new StructurePieceAndWeight(null, 4));
			return pieces;
		}
	};
	public static final StructurePiece DOWNSTAIRS_BRIDGE = new StructurePiece("earth_str_downstairs_bridge", new BlockPos(7, 11, 10), false)
	{

		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts()
		{
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] { new BlockPos(3, 5, 9) }));
			map.put(Type.BRIDGE, bridges);
			Map<EnumFacing, List<BlockPos>> tunnels = new HashMap<EnumFacing, List<BlockPos>>();
			tunnels.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] { new BlockPos(3, 5, 0) }));
			map.put(Type.TUNNEL, tunnels);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type connectType)
		{
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			if (connectType == Type.TUNNEL)
				pieces.add(new StructurePieceAndWeight(STAIRS, 1));
			else
			{
				pieces.add(new StructurePieceAndWeight(BRIDGE_CROSS, 12));
				pieces.add(new StructurePieceAndWeight(BRIDGE_STRAIGHT, 12));
				pieces.add(new StructurePieceAndWeight(DOWNSTAIRS_BRIDGE, 12));
				pieces.add(new StructurePieceAndWeight(null, 4));
			}
			return pieces;
		}
	};
	public static final StructurePiece STAIRS = new StructurePiece("earth_str_stairs", new BlockPos(18, 20, 11), false)
	{
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts()
		{
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> bridges = new HashMap<EnumFacing, List<BlockPos>>();
			bridges.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] { new BlockPos(0, 10, 5) }));
			map.put(Type.BRIDGE, bridges);
			Map<EnumFacing, List<BlockPos>> tunnels = new HashMap<EnumFacing, List<BlockPos>>();
			tunnels.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] { new BlockPos(12, 2, 10) }));
			tunnels.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] { new BlockPos(17, 2, 5) }));
			tunnels.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] { new BlockPos(12, 2, 0) }));
			map.put(Type.TUNNEL, tunnels);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type connectType)
		{
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			if (connectType == Type.BRIDGE)
			{
				pieces.add(new StructurePieceAndWeight(BRIDGE_CROSS, 12));
				pieces.add(new StructurePieceAndWeight(BRIDGE_STRAIGHT, 12));
				pieces.add(new StructurePieceAndWeight(DOWNSTAIRS_BRIDGE, 12));
				pieces.add(new StructurePieceAndWeight(null, 4));
			}
			else
			{
				pieces.add(new StructurePieceAndWeight(TUNNEL_CORNER, 10));
				pieces.add(new StructurePieceAndWeight(TUNNEL_CORNER_CHEST, 10));
				pieces.add(new StructurePieceAndWeight(TUNNEL_T, 5));
				pieces.add(new StructurePieceAndWeight(TUNNEL_CROSS, 5));
				pieces.add(new StructurePieceAndWeight(TUNNEL_STRAIGHT, 10));
				pieces.add(new StructurePieceAndWeight(null, 10));
			}
			return pieces;
		}
	};
	public static final StructurePiece BOSS = new StructurePiece("earth_str_boss", new BlockPos(19, 10, 19), false)
	{
		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts()
		{
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> tunnels = new HashMap<EnumFacing, List<BlockPos>>();
			tunnels.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] { new BlockPos(0, 3, 9) }));
			tunnels.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] { new BlockPos(18, 3, 9) }));
			tunnels.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] { new BlockPos(9, 3, 0) }));
			tunnels.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] { new BlockPos(9, 3, 18) }));
			map.put(Type.TUNNEL, tunnels);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type)
		{
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(TUNNEL_CORNER, 1));
			pieces.add(new StructurePieceAndWeight(TUNNEL_CORNER_CHEST, 1));
			pieces.add(new StructurePieceAndWeight(TUNNEL_T, 1));
			pieces.add(new StructurePieceAndWeight(TUNNEL_CROSS, 1));
			pieces.add(new StructurePieceAndWeight(TUNNEL_STRAIGHT, 1));
			return pieces;
		}
	};

	public static final StructurePiece SPAWNER = new StructurePiece("earth_str_spawner", new BlockPos(11, 10, 13), true)
	{

		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts()
		{
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> tunnels = new HashMap<EnumFacing, List<BlockPos>>();
			tunnels.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] { new BlockPos(5, 3, 0) }));
			map.put(Type.TUNNEL, tunnels);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type connectType)
		{
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(SPAWNER, 1));
			pieces.add(new StructurePieceAndWeight(null, 1));
			return pieces;
		}
	};

	public static final StructurePiece TUNNEL_CORNER = new StructurePiece("earth_str_tunnel_corner", new BlockPos(11, 10, 11), false)
	{

		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts()
		{
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> tunnels = new HashMap<EnumFacing, List<BlockPos>>();
			tunnels.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] { new BlockPos(0, 3, 3) }));
			tunnels.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] { new BlockPos(7, 3, 10) }));
			map.put(Type.TUNNEL, tunnels);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type)
		{
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(TUNNEL_CORNER, 10));
			pieces.add(new StructurePieceAndWeight(TUNNEL_CORNER_CHEST, 10));
			pieces.add(new StructurePieceAndWeight(TUNNEL_T, 5));
			pieces.add(new StructurePieceAndWeight(TUNNEL_CROSS, 5));
			pieces.add(new StructurePieceAndWeight(TUNNEL_STRAIGHT, 10));
			pieces.add(new StructurePieceAndWeight(SPAWNER, 10));
			pieces.add(new StructurePieceAndWeight(STAIRS, 20));
			pieces.add(new StructurePieceAndWeight(null, 10));
			return pieces;
		}
	};
	public static final StructurePiece TUNNEL_CORNER_CHEST = new StructurePiece("earth_str_tunnel_corner_chest", new BlockPos(11, 10, 11), true)
	{

		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts()
		{
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> tunnels = new HashMap<EnumFacing, List<BlockPos>>();
			tunnels.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] { new BlockPos(0, 3, 3) }));
			tunnels.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] { new BlockPos(7, 3, 10) }));
			map.put(Type.TUNNEL, tunnels);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type type)
		{
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(TUNNEL_CORNER, 10));
			pieces.add(new StructurePieceAndWeight(TUNNEL_CORNER_CHEST, 10));
			pieces.add(new StructurePieceAndWeight(TUNNEL_T, 5));
			pieces.add(new StructurePieceAndWeight(TUNNEL_CROSS, 5));
			pieces.add(new StructurePieceAndWeight(TUNNEL_STRAIGHT, 10));
			pieces.add(new StructurePieceAndWeight(SPAWNER, 10));
			pieces.add(new StructurePieceAndWeight(STAIRS, 20));
			pieces.add(new StructurePieceAndWeight(null, 10));
			return pieces;
		}
	};

	public static final StructurePiece TUNNEL_CROSS = new StructurePiece("earth_str_tunnel_cross", new BlockPos(15, 10, 15), false)
	{

		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts()
		{
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> tunnels = new HashMap<EnumFacing, List<BlockPos>>();
			tunnels.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] { new BlockPos(0, 3, 7) }));
			tunnels.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] { new BlockPos(7, 3, 14) }));
			tunnels.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] { new BlockPos(14, 3, 7) }));
			tunnels.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] { new BlockPos(7, 3, 0) }));
			map.put(Type.TUNNEL, tunnels);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type connectType)
		{
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(TUNNEL_CORNER, 10));
			pieces.add(new StructurePieceAndWeight(TUNNEL_CORNER_CHEST, 10));
			pieces.add(new StructurePieceAndWeight(TUNNEL_T, 5));
			pieces.add(new StructurePieceAndWeight(TUNNEL_CROSS, 5));
			pieces.add(new StructurePieceAndWeight(TUNNEL_STRAIGHT, 10));
			pieces.add(new StructurePieceAndWeight(SPAWNER, 10));
			pieces.add(new StructurePieceAndWeight(STAIRS, 20));
			pieces.add(new StructurePieceAndWeight(null, 10));
			return pieces;
		}
	};
	public static final StructurePiece TUNNEL_T = new StructurePiece("earth_str_tunnel_t", new BlockPos(15, 10, 11), false)
	{

		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts()
		{
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> tunnels = new HashMap<EnumFacing, List<BlockPos>>();
			tunnels.put(EnumFacing.WEST, Arrays.asList(new BlockPos[] { new BlockPos(0, 3, 3) }));
			tunnels.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] { new BlockPos(7, 3, 10) }));
			tunnels.put(EnumFacing.EAST, Arrays.asList(new BlockPos[] { new BlockPos(14, 3, 3) }));
			map.put(Type.TUNNEL, tunnels);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type connectType)
		{
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(TUNNEL_CORNER, 10));
			pieces.add(new StructurePieceAndWeight(TUNNEL_CORNER_CHEST, 10));
			pieces.add(new StructurePieceAndWeight(TUNNEL_T, 5));
			pieces.add(new StructurePieceAndWeight(TUNNEL_CROSS, 5));
			pieces.add(new StructurePieceAndWeight(TUNNEL_STRAIGHT, 10));
			pieces.add(new StructurePieceAndWeight(SPAWNER, 10));
			pieces.add(new StructurePieceAndWeight(STAIRS, 20));
			pieces.add(new StructurePieceAndWeight(null, 10));
			return pieces;
		}
	};

	public static final StructurePiece TUNNEL_STRAIGHT = new StructurePiece("earth_str_tunnel_straight", new BlockPos(7, 10, 15), false)
	{

		@Override
		public Map<Type, Map<EnumFacing, List<BlockPos>>> getPorts()
		{
			Map<Type, Map<EnumFacing, List<BlockPos>>> map = new HashMap<Type, Map<EnumFacing, List<BlockPos>>>();
			Map<EnumFacing, List<BlockPos>> tunnels = new HashMap<EnumFacing, List<BlockPos>>();
			tunnels.put(EnumFacing.SOUTH, Arrays.asList(new BlockPos[] { new BlockPos(3, 3, 14) }));
			tunnels.put(EnumFacing.NORTH, Arrays.asList(new BlockPos[] { new BlockPos(3, 3, 0) }));
			map.put(Type.TUNNEL, tunnels);
			return map;
		}

		@Override
		public List<StructurePieceAndWeight> getNextConnectable(StructurePiece.Type connectType)
		{
			List<StructurePieceAndWeight> pieces = new ArrayList<StructurePieceAndWeight>();
			pieces.add(new StructurePieceAndWeight(TUNNEL_CORNER, 10));
			pieces.add(new StructurePieceAndWeight(TUNNEL_CORNER_CHEST, 10));
			pieces.add(new StructurePieceAndWeight(TUNNEL_T, 5));
			pieces.add(new StructurePieceAndWeight(TUNNEL_CROSS, 5));
			pieces.add(new StructurePieceAndWeight(TUNNEL_STRAIGHT, 10));
			pieces.add(new StructurePieceAndWeight(SPAWNER, 10));
			pieces.add(new StructurePieceAndWeight(STAIRS, 20));
			pieces.add(new StructurePieceAndWeight(null, 10));
			return pieces;
		}
	};

	public static void registerPieces()
	{
		MapGenStructureIO.registerStructureComponent(EarthStructurePieceComponent.class, "ESPC");
	}
}
