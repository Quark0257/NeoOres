package neo_ores.world.gen;

import java.util.Random;

import neo_ores.block.BlockDimension;
import neo_ores.block.BlockDimensionLeaves;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenHugeTrees;

public class WorldGenMegaTree2 extends WorldGenHugeTrees
{
	private static final float[] cos = {1.0F,0.866F,0.5F,0.0F,-0.5F,-0.866F,-1.0F,-0.866F,-0.5F,0.0F,0.5F,0.866F};
	private static final float[] sin = {0.0F,0.5F,0.866F,1.0F,0.866F,0.5F,0.0F,-0.5F,-0.866F,-1.0F,-0.866F,-0.5F};

	public WorldGenMegaTree2(boolean notify, int baseHeightIn, int extraRandomHeightIn, IBlockState woodMetadataIn, IBlockState leavesMetadataIn)
	{
		super(notify, baseHeightIn, extraRandomHeightIn, woodMetadataIn, leavesMetadataIn);
	}

	public static WorldGenMegaTree2 make(boolean notify, int baseHeight, int extraRandomHeightIn, Block trunk, Block leaf, DimensionName dim)
	{
		IBlockState trunkState = trunk.getDefaultState().withProperty(BlockDimension.DIM, dim);
		IBlockState leafState = leaf.getDefaultState().withProperty(BlockDimension.DIM, dim).withProperty(BlockDimensionLeaves.CHECK_DECAY, false).withProperty(BlockDimensionLeaves.DECAYABLE, true);
		return new WorldGenMegaTree2(notify, baseHeight, extraRandomHeightIn, trunkState, leafState);
	}

	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		int i = this.getHeight(rand);

		if (!this.ensureGrowable(worldIn, rand, position, i))
		{
			return false;
		}
		else
		{
			this.createCrown(worldIn, position.up(i), 2);

			for (int j = position.getY() + i - 2 - rand.nextInt(4); j > position.getY() + i / 2; j -= 2 + rand.nextInt(4))
			{
				int f = rand.nextInt(12);
				float cos = WorldGenMegaTree2.cos[f];
				float sin = WorldGenMegaTree2.sin[f];
				int k = position.getX() + (int) (0.5F + cos * 4.0F);
				int l = position.getZ() + (int) (0.5F + sin * 4.0F);

				for (int i1 = 0; i1 < 3; ++i1)
				{
					k = position.getX() + (int) (1.5F + cos * (float) i1);
					l = position.getZ() + (int) (1.5F + sin * (float) i1);
					this.setBlockAndNotifyAdequately(worldIn, new BlockPos(k, j - 3 + i1 / 2, l), this.woodMetadata);
				}
				
				int j2 = 1 + rand.nextInt(2);
				int j1 = j;

				for (int k1 = j - j2; k1 <= j1; ++k1)
				{
					int l1 = k1 - j1;
					this.growLeavesLayer(worldIn, new BlockPos(k, k1, l), 1 - l1);
				}
			}

			for (int i2 = 0; i2 < i; ++i2)
			{
				BlockPos blockpos = position.up(i2);

				if (this.isAirLeaves(worldIn, blockpos))
				{
					this.setBlockAndNotifyAdequately(worldIn, blockpos, this.woodMetadata);
				}

				if (i2 < i - 1)
				{
					BlockPos blockpos1 = blockpos.east();

					if (this.isAirLeaves(worldIn, blockpos1))
					{
						this.setBlockAndNotifyAdequately(worldIn, blockpos1, this.woodMetadata);
					}

					BlockPos blockpos2 = blockpos.south().east();

					if (this.isAirLeaves(worldIn, blockpos2))
					{
						this.setBlockAndNotifyAdequately(worldIn, blockpos2, this.woodMetadata);
					}

					BlockPos blockpos3 = blockpos.south();

					if (this.isAirLeaves(worldIn, blockpos3))
					{
						this.setBlockAndNotifyAdequately(worldIn, blockpos3, this.woodMetadata);
					}
				}
			}

			return true;
		}
	}

	private void createCrown(World worldIn, BlockPos p_175930_2_, int p_175930_3_)
	{
		for (int j = -2; j <= 0; ++j)
		{
			this.growLeavesLayerStrict(worldIn, p_175930_2_.up(j), p_175930_3_ + 1 - j);
		}
	}

	// Helper macro
	private boolean isAirLeaves(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		return state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos);
	}

	public void generateSaplings(World worldIn, Random random, BlockPos pos)
	{
	}

	protected int getHeight(Random rand)
	{
		int i = rand.nextInt(3) + this.baseHeight;

		if (this.extraRandomHeight > 1)
		{
			i += rand.nextInt(this.extraRandomHeight) + 5;
		}

		return i;
	}

	public boolean isReplaceable(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		return state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos);
	}
}
