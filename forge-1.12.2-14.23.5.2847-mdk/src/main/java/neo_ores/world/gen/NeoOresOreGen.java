package neo_ores.world.gen;

import java.util.Random;

import neo_ores.block.BlockDimension;
import neo_ores.block.BlockMatcherNeoOre;
import neo_ores.block.BlockNeoOre;
import neo_ores.config.NeoOresConfig;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresBlocks;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class NeoOresOreGen implements IWorldGenerator
{
	private WorldGenerator sanitite_ore_gen;
	private WorldGenerator marlite_ore_gen;
	private WorldGenerator aerite_ore_gen;
	private WorldGenerator drenite_ore_gen;
	private WorldGenerator guardite_ore_gen;
	private WorldGenerator landite_ore_gen;
	private WorldGenerator forcite_ore_gen;
	private WorldGenerator flamite_ore_gen;

	public NeoOresOreGen()
	{
		this.aerite_ore_gen = new WorldGenMinable(NeoOresBlocks.aerite_ore.getDefaultState(), NeoOresConfig.ore_gen.aerite.size,
				BlockMatcherNeoOre.forBlock(NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, DimensionName.AIR)));
		this.drenite_ore_gen = new WorldGenMinable(NeoOresBlocks.drenite_ore.getDefaultState(), NeoOresConfig.ore_gen.drenite.size,
				BlockMatcherNeoOre.forBlock(NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, DimensionName.AIR)));
		this.flamite_ore_gen = new WorldGenMinable(NeoOresBlocks.flamite_ore.getDefaultState(), NeoOresConfig.ore_gen.flamite.size,
				BlockMatcherNeoOre.forBlock(NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, DimensionName.FIRE)));
		this.forcite_ore_gen = new WorldGenMinable(NeoOresBlocks.forcite_ore.getDefaultState(), NeoOresConfig.ore_gen.forcite.size,
				BlockMatcherNeoOre.forBlock(NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, DimensionName.FIRE)));
		this.guardite_ore_gen = new WorldGenMinable(NeoOresBlocks.guardite_ore.getDefaultState(), NeoOresConfig.ore_gen.guardite.size,
				BlockMatcherNeoOre.forBlock(NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, DimensionName.EARTH)));
		this.landite_ore_gen = new WorldGenMinable(NeoOresBlocks.landite_ore.getDefaultState(), NeoOresConfig.ore_gen.landite.size,
				BlockMatcherNeoOre.forBlock(NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, DimensionName.EARTH)));
		this.marlite_ore_gen = new WorldGenMinable(NeoOresBlocks.marlite_ore.getDefaultState(), NeoOresConfig.ore_gen.marlite.size,
				BlockMatcherNeoOre.forBlock(NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, DimensionName.WATER)));
		this.sanitite_ore_gen = new WorldGenMinable(NeoOresBlocks.sanitite_ore.getDefaultState(), NeoOresConfig.ore_gen.sanitite.size,
				BlockMatcherNeoOre.forBlock(NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, DimensionName.WATER)));
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		int dim = world.provider.getDimension();

		if (this.isAir(dim))
		{
			this.generateOres(this.aerite_ore_gen, world, random, chunkX, chunkZ, NeoOresConfig.ore_gen.aerite.tries, NeoOresConfig.ore_gen.aerite.min_height, NeoOresConfig.ore_gen.aerite.max_height);
			this.generateOres(this.drenite_ore_gen, world, random, chunkX, chunkZ, NeoOresConfig.ore_gen.drenite.tries, NeoOresConfig.ore_gen.drenite.min_height,
					NeoOresConfig.ore_gen.drenite.max_height);
		}
		else if (this.isEarth(dim))
		{
			this.generateOres(this.landite_ore_gen, world, random, chunkX, chunkZ, NeoOresConfig.ore_gen.landite.tries, NeoOresConfig.ore_gen.landite.min_height,
					NeoOresConfig.ore_gen.landite.max_height);
			this.generateOres(this.guardite_ore_gen, world, random, chunkX, chunkZ, NeoOresConfig.ore_gen.guardite.tries, NeoOresConfig.ore_gen.guardite.min_height,
					NeoOresConfig.ore_gen.guardite.max_height);
		}
		else if (this.isFire(dim))
		{
			this.generateOres(this.flamite_ore_gen, world, random, chunkX, chunkZ, NeoOresConfig.ore_gen.flamite.tries, NeoOresConfig.ore_gen.flamite.min_height,
					NeoOresConfig.ore_gen.flamite.max_height);
			this.generateOres(this.forcite_ore_gen, world, random, chunkX, chunkZ, NeoOresConfig.ore_gen.forcite.tries, NeoOresConfig.ore_gen.forcite.min_height,
					NeoOresConfig.ore_gen.forcite.max_height);
		}
		else if (this.isWater(dim))
		{
			this.generateOres(this.marlite_ore_gen, world, random, chunkX, chunkZ, NeoOresConfig.ore_gen.marlite.tries, NeoOresConfig.ore_gen.marlite.min_height,
					NeoOresConfig.ore_gen.marlite.max_height);
			this.generateOres(this.sanitite_ore_gen, world, random, chunkX, chunkZ, NeoOresConfig.ore_gen.sanitite.tries, NeoOresConfig.ore_gen.sanitite.min_height,
					NeoOresConfig.ore_gen.sanitite.max_height);
		}

		if (this.isAir(dim) || this.isEarth(dim) || this.isFire(dim) || this.isWater(dim))
		{
			this.generateOres(this.redstone(dim), world, random, chunkX, chunkZ, NeoOresConfig.ore_gen.redstone.tries, NeoOresConfig.ore_gen.redstone.min_height,
					NeoOresConfig.ore_gen.redstone.max_height);
			this.generateOres(this.diamond(dim), world, random, chunkX, chunkZ, NeoOresConfig.ore_gen.diamond.tries, NeoOresConfig.ore_gen.diamond.min_height,
					NeoOresConfig.ore_gen.diamond.max_height);
			this.generateOres(this.emerald(dim), world, random, chunkX, chunkZ, NeoOresConfig.ore_gen.emerald.tries, NeoOresConfig.ore_gen.emerald.min_height,
					NeoOresConfig.ore_gen.emerald.max_height);
			this.generateOres(this.coal(dim), world, random, chunkX, chunkZ, NeoOresConfig.ore_gen.coal.tries, NeoOresConfig.ore_gen.coal.min_height, NeoOresConfig.ore_gen.coal.max_height);
			this.generateOres(this.gold(dim), world, random, chunkX, chunkZ, NeoOresConfig.ore_gen.gold.tries, NeoOresConfig.ore_gen.gold.min_height, NeoOresConfig.ore_gen.gold.max_height);
			this.generateOres(this.iron(dim), world, random, chunkX, chunkZ, NeoOresConfig.ore_gen.iron.tries, NeoOresConfig.ore_gen.iron.min_height, NeoOresConfig.ore_gen.iron.max_height);
			this.generateOres(this.lapis(dim), world, random, chunkX, chunkZ, NeoOresConfig.ore_gen.lapis.tries, NeoOresConfig.ore_gen.lapis.min_height, NeoOresConfig.ore_gen.lapis.max_height);
			this.generateOres(this.quartz(dim), world, random, chunkX, chunkZ, NeoOresConfig.ore_gen.quartz.tries, NeoOresConfig.ore_gen.quartz.min_height, NeoOresConfig.ore_gen.quartz.max_height);
		}
	}

	private void generateOres(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int chance, int minH, int maxH)
	{
		int range = maxH - minH;
		int min = minH;
		int chances = chance;

		int dim = world.provider.getDimension();
		if (this.isAir(dim) && minH < NeoOresConfig.ore_gen.all.air_min_height)
		{
			min = NeoOresConfig.ore_gen.all.air_min_height;
			if (NeoOresConfig.ore_gen.all.air_max_height < maxH)
				range = NeoOresConfig.ore_gen.all.air_max_height - NeoOresConfig.ore_gen.all.air_min_height;
			else
				range = maxH - NeoOresConfig.ore_gen.all.air_min_height;
		}

		if (this.isEarth(dim) && minH < NeoOresConfig.ore_gen.all.earth_min_height)
		{
			min = NeoOresConfig.ore_gen.all.earth_min_height;
			if (NeoOresConfig.ore_gen.all.earth_max_height < maxH)
				range = NeoOresConfig.ore_gen.all.earth_max_height - NeoOresConfig.ore_gen.all.earth_min_height;
			else
				range = maxH - NeoOresConfig.ore_gen.all.earth_min_height;
		}

		if (this.isFire(dim) && minH < NeoOresConfig.ore_gen.all.fire_min_height)
		{
			min = NeoOresConfig.ore_gen.all.fire_min_height;
			if (NeoOresConfig.ore_gen.all.fire_max_height < maxH)
				range = NeoOresConfig.ore_gen.all.fire_max_height - NeoOresConfig.ore_gen.all.fire_min_height;
			else
				range = maxH - NeoOresConfig.ore_gen.all.fire_min_height;
		}

		if (this.isWater(dim) && minH < NeoOresConfig.ore_gen.all.water_min_height)
		{
			min = NeoOresConfig.ore_gen.all.water_min_height;
			if (NeoOresConfig.ore_gen.all.water_max_height < maxH)
				range = NeoOresConfig.ore_gen.all.water_max_height - NeoOresConfig.ore_gen.all.water_min_height;
			else
				range = maxH - NeoOresConfig.ore_gen.all.water_min_height;
		}

		if (range < 1)
			range = 1;

		chances *= (this.getDimName(dim).getMeta() + 1);

		for (int i = 0; i < chances; i++)
		{
			int x = chunkX * 16 + rand.nextInt(16);
			int y = min + rand.nextInt(range);
			int z = chunkZ * 16 + rand.nextInt(16);

			gen.generate(world, rand, new BlockPos(x, y, z));
		}
	}

	private final DimensionName getDimName(int dimtype)
	{
		if (this.isAir(dimtype))
			return DimensionName.AIR;
		else if (this.isEarth(dimtype))
			return DimensionName.EARTH;
		else if (this.isFire(dimtype))
			return DimensionName.FIRE;
		else if (this.isWater(dimtype))
			return DimensionName.WATER;
		return DimensionName.EARTH;
	}

	private boolean isAir(int dimtype)
	{
		return dimtype == NeoOres.THE_AIR.getId();
	}

	private boolean isEarth(int dimtype)
	{
		return dimtype == NeoOres.THE_EARTH.getId();
	}

	private boolean isFire(int dimtype)
	{
		return dimtype == NeoOres.THE_FIRE.getId();
	}

	private boolean isWater(int dimtype)
	{
		return dimtype == NeoOres.THE_WATER.getId();
	}

	private final WorldGenerator redstone(int dimtype)
	{
		return new WorldGenMinable(NeoOresBlocks.custom_redstone_ore.getDefaultState().withProperty(BlockNeoOre.DIM, this.getDimName(dimtype)), NeoOresConfig.ore_gen.redstone.size,
				BlockMatcherNeoOre.forBlock(NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, this.getDimName(dimtype))));
	}

	private final WorldGenerator diamond(int dimtype)
	{
		return new WorldGenMinable(NeoOresBlocks.custom_diamond_ore.getDefaultState().withProperty(BlockNeoOre.DIM, this.getDimName(dimtype)), NeoOresConfig.ore_gen.diamond.size,
				BlockMatcherNeoOre.forBlock(NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, this.getDimName(dimtype))));
	}

	private final WorldGenerator emerald(int dimtype)
	{
		return new WorldGenMinable(NeoOresBlocks.custom_emerald_ore.getDefaultState().withProperty(BlockNeoOre.DIM, this.getDimName(dimtype)), NeoOresConfig.ore_gen.emerald.size,
				BlockMatcherNeoOre.forBlock(NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, this.getDimName(dimtype))));
	}

	private final WorldGenerator coal(int dimtype)
	{
		return new WorldGenMinable(NeoOresBlocks.custom_coal_ore.getDefaultState().withProperty(BlockNeoOre.DIM, this.getDimName(dimtype)), NeoOresConfig.ore_gen.coal.size,
				BlockMatcherNeoOre.forBlock(NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, this.getDimName(dimtype))));
	}

	private final WorldGenerator gold(int dimtype)
	{
		return new WorldGenMinable(NeoOresBlocks.custom_gold_ore.getDefaultState().withProperty(BlockNeoOre.DIM, this.getDimName(dimtype)), NeoOresConfig.ore_gen.gold.size,
				BlockMatcherNeoOre.forBlock(NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, this.getDimName(dimtype))));
	}

	private final WorldGenerator iron(int dimtype)
	{
		return new WorldGenMinable(NeoOresBlocks.custom_iron_ore.getDefaultState().withProperty(BlockNeoOre.DIM, this.getDimName(dimtype)), NeoOresConfig.ore_gen.iron.size,
				BlockMatcherNeoOre.forBlock(NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, this.getDimName(dimtype))));
	}

	private final WorldGenerator lapis(int dimtype)
	{
		return new WorldGenMinable(NeoOresBlocks.custom_lapis_ore.getDefaultState().withProperty(BlockNeoOre.DIM, this.getDimName(dimtype)), NeoOresConfig.ore_gen.lapis.size,
				BlockMatcherNeoOre.forBlock(NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, this.getDimName(dimtype))));
	}

	private final WorldGenerator quartz(int dimtype)
	{
		return new WorldGenMinable(NeoOresBlocks.custom_quartz_ore.getDefaultState().withProperty(BlockNeoOre.DIM, this.getDimName(dimtype)), NeoOresConfig.ore_gen.quartz.size,
				BlockMatcherNeoOre.forBlock(NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, this.getDimName(dimtype))));
	}
}
