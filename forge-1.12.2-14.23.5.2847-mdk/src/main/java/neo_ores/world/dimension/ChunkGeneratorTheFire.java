package neo_ores.world.dimension;

import java.util.List;
import java.util.Random;

import neo_ores.api.LongUtils;
import neo_ores.block.BlockDimension;
import neo_ores.main.NeoOresBlocks;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import neo_ores.world.gen.structures.fire.MapGenFireStructure;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorSimplex;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent.InitNoiseField;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextEnd;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class ChunkGeneratorTheFire implements IChunkGenerator
{
	private final Random rand;
	protected static final IBlockState MAINBLOCK = NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, DimensionName.FIRE);
	public static final IBlockState SUBBLOCK = Blocks.AIR.getDefaultState();
	protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
	private final boolean generateStructures;
	private NoiseGeneratorOctaves lperlinNoise1;
	private NoiseGeneratorOctaves lperlinNoise2;
	private NoiseGeneratorOctaves perlinNoise1;
	public NoiseGeneratorOctaves noiseGen5;
	public NoiseGeneratorOctaves noiseGen6;
	private final World world;
	private NoiseGeneratorSimplex islandNoise;
	private double[] buffer;
	private Biome[] biomesForGeneration;
	private MapGenFireStructure genStructure = new MapGenFireStructure();
	double[] pnr;
	double[] ar;
	double[] br;
	private int chunkX = 0;
	private int chunkZ = 0;

	public ChunkGeneratorTheFire(World world, boolean isGeneratingStructure, long seed)
	{
		this.world = world;
		this.rand = new Random(LongUtils.trimAdd(seed, 2));
		this.lperlinNoise1 = new NoiseGeneratorOctaves(this.rand, 17);
		this.lperlinNoise2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.perlinNoise1 = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
		this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
		this.islandNoise = new NoiseGeneratorSimplex(this.rand);
		ContextEnd ctx = new ContextEnd(lperlinNoise1, lperlinNoise2, perlinNoise1, noiseGen5, noiseGen6, islandNoise);
		ctx = TerrainGen.getModdedNoiseGenerators(world, this.rand, ctx);
		this.lperlinNoise1 = ctx.getLPerlin1();
		this.lperlinNoise2 = ctx.getLPerlin2();
		this.perlinNoise1 = ctx.getPerlin();
		this.noiseGen5 = ctx.getDepth();
		this.noiseGen6 = ctx.getScale();
		this.islandNoise = ctx.getIsland();
		this.generateStructures = isGeneratingStructure;
	}

	public void setBlocksInChunk(int x, int z, ChunkPrimer primer)
	{
		this.buffer = this.getHeights(this.buffer, x * 2, 0, z * 2, 3, 33, 3);

		for (int i1 = 0; i1 < 2; ++i1)
		{
			for (int j1 = 0; j1 < 2; ++j1)
			{
				for (int k1 = 0; k1 < 32; ++k1)
				{
					double d1 = this.buffer[((i1 + 0) * 3 + j1 + 0) * 33 + k1 + 0];
					double d2 = this.buffer[((i1 + 0) * 3 + j1 + 1) * 33 + k1 + 0];
					double d3 = this.buffer[((i1 + 1) * 3 + j1 + 0) * 33 + k1 + 0];
					double d4 = this.buffer[((i1 + 1) * 3 + j1 + 1) * 33 + k1 + 0];
					double d5 = (this.buffer[((i1 + 0) * 3 + j1 + 0) * 33 + k1 + 1] - d1) * 0.25D;
					double d6 = (this.buffer[((i1 + 0) * 3 + j1 + 1) * 33 + k1 + 1] - d2) * 0.25D;
					double d7 = (this.buffer[((i1 + 1) * 3 + j1 + 0) * 33 + k1 + 1] - d3) * 0.25D;
					double d8 = (this.buffer[((i1 + 1) * 3 + j1 + 1) * 33 + k1 + 1] - d4) * 0.25D;

					for (int l1 = 0; l1 < 4; ++l1)
					{
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * 0.125D;
						double d13 = (d4 - d2) * 0.125D;

						for (int i2 = 0; i2 < 8; ++i2)
						{
							double d15 = d10;
							double d16 = (d11 - d10) * 0.125D;

							for (int j2 = 0; j2 < 8; ++j2)
							{
								IBlockState iblockstate = AIR;

								if (d15 > 0.0D)
								{
									iblockstate = MAINBLOCK;
								}

								int k2 = i2 + i1 * 8;
								int l2 = l1 + k1 * 4;
								int i3 = j2 + j1 * 8;
								primer.setBlockState(k2, l2, i3, iblockstate);
								d15 += d16;
							}

							d10 += d12;
							d11 += d13;
						}

						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
					}
				}
			}
		}
	}

	private void buildSurfacesWithBiomeBlocks(int x, int y, int z, ChunkPrimer primer)
	{
		IBlockState topblock = this.world.getBiome(new BlockPos(x, y, z)).topBlock;
		IBlockState fillerblock = this.world.getBiome(new BlockPos(x, y, z)).fillerBlock;

		IBlockState iblockstate2 = primer.getBlockState(x, y, z);
		IBlockState iblockstate3 = primer.getBlockState(x, y + 1, z);

		if (iblockstate2.getBlock() != null && iblockstate2.getBlock() != SUBBLOCK.getBlock() && iblockstate3.getBlock() == SUBBLOCK.getBlock())
		{
			if (iblockstate2.getBlock() == MAINBLOCK.getBlock())
			{
				int fillamount = 3;

				for (int filling = 0; filling < 3; filling++)
				{
					if (y - 1 - filling > 0)
					{
						IBlockState iblockstate4 = primer.getBlockState(x, y - 1 - filling, z);
						if (!(iblockstate4.getBlock() != null && iblockstate4.getBlock() == MAINBLOCK.getBlock()))
						{
							fillamount = filling;
							break;
						}
					}
				}

				primer.setBlockState(x, y, z, topblock);

				for (int filler = 0; filler < fillamount; filler++)
				{
					if (y - 1 - filler > 0)
					{
						primer.setBlockState(x, y - 1 - filler, z, fillerblock);
						if (fillamount == 3 && filler == 1 && this.rand.nextBoolean())
						{
							primer.setBlockState(x, y - 1 - filler, z, fillerblock);
							break;
						}
					}
				}
			}
		}
	}

	public void buildSurfaces(ChunkPrimer primer)
	{
		if (!ForgeEventFactory.onReplaceBiomeBlocks(this, this.chunkX, this.chunkZ, primer, this.world))
			return;
		for (int i = 0; i < 16; ++i)
		{
			for (int j = 0; j < 16; ++j)
			{
				for (int i1 = 127; i1 >= 0; --i1)
				{
					this.buildSurfacesWithBiomeBlocks(i, i1, j, primer);
				}
			}
		}
	}

	public Chunk generateChunk(int x, int z)
	{
		this.chunkX = x;
		this.chunkZ = z;
		this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
		ChunkPrimer chunkprimer = new ChunkPrimer();
		this.biomesForGeneration = this.world.getBiomeProvider().getBiomes(this.biomesForGeneration, x * 16, z * 16, 16, 16);
		this.setBlocksInChunk(x, z, chunkprimer);
		this.buildSurfaces(chunkprimer);

		if (this.generateStructures)
		{
			this.genStructure.generate(this.world, x, z, chunkprimer);
		}

		Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
		byte[] abyte = chunk.getBiomeArray();

		for (int i = 0; i < abyte.length; ++i)
		{
			abyte[i] = (byte) Biome.getIdForBiome(this.biomesForGeneration[i]);
		}

		chunk.generateSkylightMap();
		return chunk;
	}

	private float getIslandHeightValue(int p_185960_1_, int p_185960_2_, int p_185960_3_, int p_185960_4_)
	{
		float f = (float) (p_185960_1_ * 2 + p_185960_3_);
		float f1 = (float) (p_185960_2_ * 2 + p_185960_4_);
		float f2 = 100.0F - MathHelper.sqrt(f * f + f1 * f1) * 8.0F;

		if (f2 > 80.0F)
		{
			f2 = 80.0F;
		}

		if (f2 < -100.0F)
		{
			f2 = -100.0F;
		}

		for (int i = -12; i <= 12; ++i)
		{
			for (int j = -12; j <= 12; ++j)
			{
				long k = (long) (p_185960_1_ + i);
				long l = (long) (p_185960_2_ + j);

				if (this.islandNoise.getValue((double) k, (double) l) < -0.8999999761581421D)
				{
					float f3 = (MathHelper.abs((float) k) * 3439.0F + MathHelper.abs((float) l) * 147.0F) % 13.0F + 9.0F;
					f = (float) (p_185960_3_ - i * 2);
					f1 = (float) (p_185960_4_ - j * 2);
					float f4 = 100.0F - MathHelper.sqrt(f * f + f1 * f1) * f3;

					if (f4 > 80.0F)
					{
						f4 = 80.0F;
					}

					if (f4 < -100.0F)
					{
						f4 = -100.0F;
					}

					if (f4 > f2)
					{
						f2 = f4;
					}
				}
			}
		}

		return f2;
	}

	public boolean isIslandChunk(int x, int y)
	{
		return this.getIslandHeightValue(x, y, 1, 1) >= 0.0F;
	}

	private double[] getHeights(double[] height, int p_185963_2_, int p_185963_3_, int p_185963_4_, int p_185963_5_, int p_185963_6_, int p_185963_7_)
	{
		InitNoiseField event = new InitNoiseField(this, height, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_, p_185963_6_, p_185963_7_);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.getResult() == Result.DENY)
			return event.getNoisefield();

		if (height == null)
		{
			height = new double[p_185963_5_ * p_185963_6_ * p_185963_7_];
		}

		double d0 = 684.412D;
		d0 = d0 * 2.0D;
		this.pnr = this.perlinNoise1.generateNoiseOctaves(this.pnr, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_, p_185963_6_, p_185963_7_, d0 / 80.0D, 4.277575000000001D, d0 / 80.0D);
		this.ar = this.lperlinNoise1.generateNoiseOctaves(this.ar, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_, p_185963_6_, p_185963_7_, d0, 684.412D, d0);
		this.br = this.lperlinNoise2.generateNoiseOctaves(this.br, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_, p_185963_6_, p_185963_7_, d0, 684.412D, d0);
		int i = p_185963_2_ / 2;
		int j = p_185963_4_ / 2;
		int k = 0;

		for (int l = 0; l < p_185963_5_; ++l)
		{
			for (int i1 = 0; i1 < p_185963_7_; ++i1)
			{
				float f = this.getIslandHeightValue(i, j, l, i1);

				for (int j1 = 0; j1 < p_185963_6_; ++j1)
				{
					double d2 = this.ar[k] / 512.0D;
					double d3 = this.br[k] / 512.0D;
					double d5 = (this.pnr[k] / 10.0D + 1.0D) / 2.0D;
					double d4;

					if (d5 < 0.0D)
					{
						d4 = d2;
					}
					else if (d5 > 1.0D)
					{
						d4 = d3;
					}
					else
					{
						d4 = d2 + (d3 - d2) * d5;
					}

					d4 = d4 - 8.0D;
					d4 = d4 + (double) f;
					int k1 = 2;

					if (j1 > p_185963_6_ / 2 - k1)
					{
						double d6 = (double) ((float) (j1 - (p_185963_6_ / 2 - k1)) / 64.0F);
						d6 = MathHelper.clamp(d6, 0.0D, 1.0D);
						d4 = d4 * (1.0D - d6) + -3000.0D * d6;
					}

					k1 = 8;

					if (j1 < k1)
					{
						double d7 = (double) ((float) (k1 - j1) / ((float) k1 - 1.0F));
						d4 = d4 * (1.0D - d7) + -30.0D * d7;
					}

					height[k] = d4;
					++k;
				}
			}
		}

		return height;
	}

	@SuppressWarnings("deprecation")
	public void populate(int x, int z)
	{
		BlockFalling.fallInstantly = true;
		ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, false);
		int i = x * 16;
		int j = z * 16;
		BlockPos blockpos = new BlockPos(i, 0, j);
		Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
		ChunkPos chunkpos = new ChunkPos(x, z);

		if (this.generateStructures)
			this.genStructure.generateStructure(this.world, this.rand, chunkpos);

		ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, x, z, false);
		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(this.world, this.rand, chunkpos));

		biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));

		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(this.world, this.rand, blockpos));

		BlockFalling.fallInstantly = false;
	}

	public boolean generateStructures(Chunk chunkIn, int x, int z)
	{
		return false;
	}

	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
	{
		return this.world.getBiome(pos).getSpawnableList(creatureType);
	}

	public void recreateStructures(Chunk chunkIn, int x, int z)
	{
	}

	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
	{
		return null;
	}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
	{
		return false;
	}
}
