package neo_ores.world.gen.structures.air;

import java.util.Random;

import neo_ores.world.dimension.ChunkGeneratorTheAir;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenAirStructure extends MapGenStructure
{
	private final int spacing = 30;
	private final int separation = 11;
	private final ChunkGeneratorTheAir cg;

	public MapGenAirStructure(ChunkGeneratorTheAir cg)
	{
		this.cg = cg;
	}

	public String getStructureName()
	{
		return "RaphaSanctuary";
	}

	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
	{
		int i = chunkX;
		int j = chunkZ;

		if (chunkX < 0)
		{
			chunkX -= (this.spacing - 1);
		}

		if (chunkZ < 0)
		{
			chunkZ -= (this.spacing - 1);
		}

		int k = chunkX / this.spacing;
		int l = chunkZ / this.spacing;
		Random random = this.world.setRandomSeed(k, l, 10387313);
		k = k * this.spacing;
		l = l * this.spacing;
		int weight = this.spacing / 2 - 1;
		k = k + (random.nextInt(weight) + random.nextInt(weight)) / 2;
		l = l + (random.nextInt(weight) + random.nextInt(weight)) / 2;

		if (i == k && j == l)
		{
			int i1 = getYPosForStructure(i, j, this.cg);
			return i1 >= 56;
		}
		else
		{
			return false;
		}
	}

	protected StructureStart getStructureStart(int chunkX, int chunkZ)
	{
		return new AirStructureStart(this.world, this.rand, chunkX, chunkZ, this.cg);
	}

	public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored)
	{
		this.world = worldIn;
		return findNearestStructurePosBySpacing(worldIn, this, pos, this.spacing, this.separation, 10387313, true, 100, findUnexplored);
	}

	public static int getYPosForStructure(int chunkX, int chunkZ, ChunkGeneratorTheAir cg)
	{
		Random random = new Random((long) (chunkX + chunkZ * 10387313));
		Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
		ChunkPrimer chunkprimer = new ChunkPrimer();
		cg.setBlocksInChunk(chunkX, chunkZ, chunkprimer);
		int i = 5;
		int j = 5;

		if (rotation == Rotation.CLOCKWISE_90)
		{
			i = -5;
		}
		else if (rotation == Rotation.CLOCKWISE_180)
		{
			i = -5;
			j = -5;
		}
		else if (rotation == Rotation.COUNTERCLOCKWISE_90)
		{
			j = -5;
		}

		int k = chunkprimer.findGroundBlockIdx(7, 7);
		int l = chunkprimer.findGroundBlockIdx(7, 7 + j);
		int i1 = chunkprimer.findGroundBlockIdx(7 + i, 7);
		int j1 = chunkprimer.findGroundBlockIdx(7 + i, 7 + j);
		int k1 = Math.min(Math.min(k, l), Math.min(i1, j1));
		return k1;
	}
}
