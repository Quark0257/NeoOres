package neo_ores.world.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import neo_ores.block.BlockDimension;
import neo_ores.block.BlockDimensionLeaves;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenSimpleTree extends WorldGenAbstractTree
{

	private final IBlockState trunk;
	private final IBlockState leaf;
	private final int baseHeight;
	private final int extraHeight;
	private final int range;

	public WorldGenSimpleTree(boolean notify, int baseHeight, int extraHeight, int range, IBlockState trunk, IBlockState leaf)
	{
		super(notify);
		this.trunk = trunk;
		this.leaf = leaf;
		this.baseHeight = baseHeight;
		this.extraHeight = extraHeight;
		this.range = range;
	}

	public static WorldGenSimpleTree make(boolean notify, int baseHeight, int extraHeight, Block trunk, Block leaf, DimensionName dim)
	{
		IBlockState trunkState = trunk.getDefaultState().withProperty(BlockDimension.DIM, dim);
		IBlockState leafState = leaf.getDefaultState().withProperty(BlockDimension.DIM, dim).withProperty(BlockDimensionLeaves.CHECK_DECAY, false).withProperty(BlockDimensionLeaves.DECAYABLE, true);
		return new WorldGenSimpleTree(notify, baseHeight, extraHeight, 2, trunkState, leafState);
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		int base = this.baseHeight + rand.nextInt(this.extraHeight);
		List<BlockPos> logs = new ArrayList<BlockPos>();
		for (int y0 = 0; y0 < base; y0++)
		{
			if (!this.isReplaceable(worldIn, position.add(0, y0, 0)))
				return false;
			logs.add(position.add(0, y0, 0));
		}
		List<BlockPos> leaves1 = new ArrayList<BlockPos>();
		for (int x = 0; x < 3; x++)
		{
			for (int z = 0; z < 3; z++)
			{
				if (!this.isReplaceable(worldIn, position.add(x - 1, base + 2, z - 1)))
					return false;
				leaves1.add(position.add(x - 1, base + 1, z - 1));
			}
		}
		for (int x = 0; x < 5; x++)
		{
			for (int z = 0; z < 5; z++)
			{
				if (!this.isReplaceable(worldIn, position.add(x - 2, base + 1, z - 2)))
					return false;
				leaves1.add(position.add(x - 2, base, z - 2));
			}
		}
		for (int x = 0; x < 7; x++)
		{
			for (int z = 0; z < 7; z++)
			{
				if (18 <= (x - 3) * (x - 3) + (z - 3) * (z - 3))
					continue;
				if (!this.isReplaceable(worldIn, position.add(x - 3, base + 1, z - 3)))
					return false;
				leaves1.add(position.add(x - 3, base - 1, z - 3));
			}
		}
		for (BlockPos pos : leaves1)
			this.setBlockAndNotifyAdequately(worldIn, pos, this.leaf);
		for (BlockPos pos : logs)
			this.setBlockAndNotifyAdequately(worldIn, pos, this.trunk);
		return true;
	}

	public boolean isInRange(BlockPos basePos, BlockPos compare)
	{
		return basePos.getX() - this.range <= compare.getX() && compare.getX() <= basePos.getX() + this.range && basePos.getZ() - this.range <= compare.getZ()
				&& compare.getZ() <= basePos.getZ() + this.range;
	}

	public boolean isReplaceable(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		return state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos) || state.getBlock() == Blocks.FLOWING_WATER || state.getBlock() == Blocks.WATER;
	}
}
