package neo_ores.world.gen.structures.air;

import java.util.Random;

import neo_ores.world.dimension.ChunkGeneratorTheAir;
import neo_ores.world.gen.structures.RecursiveGenerator;
import neo_ores.world.gen.structures.StructurePieceAndOption;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureStart;

public class AirStructureStart extends StructureStart
{

	private boolean isSizeable;

	public AirStructureStart()
	{
	}

	public AirStructureStart(World worldIn, Random random, int chunkX, int chunkZ, ChunkGeneratorTheAir cg)
	{
		super(chunkX, chunkZ);
		this.create(worldIn, random, chunkX, chunkZ, cg);
	}

	public void create(World worldIn, Random rnd, int chunkX, int chunkZ, ChunkGeneratorTheAir cg)
	{
		Random random = new Random((long) (chunkX + chunkZ * 10387313));
		Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];

		int i = MapGenAirStructure.getYPosForStructure(chunkX, chunkZ, cg);
		if (i < 56)
		{
			this.updateBoundingBox();
			this.isSizeable = false;
			return;
		}

		if (worldIn instanceof WorldServer)
		{
			BlockPos blockpos = new BlockPos(chunkX * 16 + 8, i, chunkZ * 16 + 8);
			WorldServer server = (WorldServer) worldIn;
			RecursiveGenerator rg;
			while (true)
			{
				rg = new RecursiveGenerator();
				rg.getRecursiveList(AirStructurePieces.BOSS, blockpos, rotation, null, random, 18);
				if (rg.chestCount > 0)
					break;
			}

			int bosskeyIndex = random.nextInt(rg.chestCount);
			int count = 0;
			for (StructurePieceAndOption sp : rg.list)
			{
				if (sp.piece.hasChest)
				{
					if (count == bosskeyIndex)
					{
						sp.bossKey = true;
					}
					count++;
				}
				AirStructurePieceComponent sn = new AirStructurePieceComponent(server, sp);
				this.components.add(sn);
			}
			this.isSizeable = true;
			this.updateBoundingBox();
		}
	}

	public boolean isSizeableStructure()
	{
		return this.isSizeable;
	}
}
