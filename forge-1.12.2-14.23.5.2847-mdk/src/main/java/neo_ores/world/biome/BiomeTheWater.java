package neo_ores.world.biome;

import java.util.Random;

import neo_ores.main.NeoOresBlocks;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import neo_ores.world.gen.WorldGenSeeweedTree;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeTheWater extends Biome
{
	protected static final WorldGenSeeweedTree TREE = WorldGenSeeweedTree.make(false, 7, 5, NeoOresBlocks.dim_log, NeoOresBlocks.dim_leaves, DimensionName.WATER);
	protected static final WorldGenSeeweedTree CD_TREE = WorldGenSeeweedTree.make(false, 7, 5, NeoOresBlocks.dim_log, NeoOresBlocks.corroded_dim_leaves, DimensionName.WATER);
	protected static final WorldGenSeeweedTree CI_TREE = WorldGenSeeweedTree.make(false, 7, 5, NeoOresBlocks.dim_log, NeoOresBlocks.corroding_dim_leaves, DimensionName.WATER);

	public BiomeTheWater(Biome.BiomeProperties properties)
	{
		super(properties);
		this.topBlock = Blocks.SAND.getDefaultState();
		this.fillerBlock = Blocks.SANDSTONE.getDefaultState();
		this.decorator.treesPerChunk = 1;
	}

	public WorldGenAbstractTree getRandomTreeFeature(Random rand)
	{
		int i = rand.nextInt(3);
		if (i == 0)
			return TREE;
		else if (i == 1)
			return CD_TREE;
		else
			return CI_TREE;
	}

	public BlockFlower.EnumFlowerType pickRandomFlower(Random rand, BlockPos pos)
	{
		return super.pickRandomFlower(rand, pos);
	}

	public void decorate(World worldIn, Random rand, BlockPos pos)
	{
		if (TerrainGen.decorate(worldIn, rand, new ChunkPos(pos), DecorateBiomeEvent.Decorate.EventType.FLOWERS))
		{ // no tab for patch
			int i = rand.nextInt(5) - 3;

			this.addDoublePlants(worldIn, rand, pos, i);
		}
		super.decorate(worldIn, rand, pos);
	}

	public void addDoublePlants(World p_185378_1_, Random p_185378_2_, BlockPos p_185378_3_, int p_185378_4_)
	{
		for (int i = 0; i < p_185378_4_; ++i)
		{
			int j = p_185378_2_.nextInt(3);

			if (j == 0)
			{
				DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.SYRINGA);
			}
			else if (j == 1)
			{
				DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.ROSE);
			}
			else if (j == 2)
			{
				DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.PAEONIA);
			}

			for (int k = 0; k < 5; ++k)
			{
				int l = p_185378_2_.nextInt(16) + 8;
				int i1 = p_185378_2_.nextInt(16) + 8;
				int j1 = p_185378_2_.nextInt(p_185378_1_.getHeight(p_185378_3_.add(l, 0, i1)).getY() + 32);

				if (DOUBLE_PLANT_GENERATOR.generate(p_185378_1_, p_185378_2_, new BlockPos(p_185378_3_.getX() + l, j1, p_185378_3_.getZ() + i1)))
				{
					break;
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public int getGrassColorAtPos(BlockPos pos)
	{
		int i = super.getGrassColorAtPos(pos);
		return i;
	}

	public Class<? extends Biome> getBiomeClass()
	{
		return BiomeTheEarth.class;
	}

	public BiomeDecorator createBiomeDecorator()
	{
		return getModdedBiomeDecorator(new BiomeDecoratorUnderground(true));
	}
}
