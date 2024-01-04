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

public class WorldGenMegaTree1 extends WorldGenHugeTrees {
    private final boolean useBaseHeight;

    public WorldGenMegaTree1(boolean notify, boolean useBaseHeight, IBlockState trunk, IBlockState leaf)
    {
        super(notify, 13, 10, trunk, leaf);
        this.useBaseHeight = useBaseHeight;
    }
    
    public static WorldGenMegaTree1 make(boolean notify, boolean useBaseHeight, Block trunk, Block leaf, DimensionName dim) {
    	IBlockState trunkState = trunk.getDefaultState().withProperty(BlockDimension.DIM, dim);
    	IBlockState leafState = leaf.getDefaultState().withProperty(BlockDimension.DIM, dim).withProperty(BlockDimensionLeaves.CHECK_DECAY, false).withProperty(BlockDimensionLeaves.DECAYABLE, true);
    	return new WorldGenMegaTree1(notify,useBaseHeight,trunkState,leafState);
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
            this.createCrown(worldIn, position.getX(), position.getZ(), position.getY() + i, 0, rand);

            for (int j = 0; j < i; ++j)
            {
                if (isAirLeaves(worldIn, position.up(j)))
                {
                    this.setBlockAndNotifyAdequately(worldIn, position.up(j), this.woodMetadata);
                }

                if (j < i - 1)
                {
                    if (isAirLeaves(worldIn, position.add(1, j, 0)))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, position.add(1, j, 0), this.woodMetadata);
                    }

                    if (isAirLeaves(worldIn, position.add(1, j, 1)))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, position.add(1, j, 1), this.woodMetadata);
                    }


                    if (isAirLeaves(worldIn, position.add(0, j, 1)))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, position.add(0, j, 1), this.woodMetadata);
                    }
                }
            }

            return true;
        }
    }

    private void createCrown(World worldIn, int x, int z, int y, int p_150541_5_, Random rand)
    {
        int i = rand.nextInt(5) + (this.useBaseHeight ? this.baseHeight : 3);
        int j = 0;

        for (int k = y - i; k <= y; ++k)
        {
            int l = y - k;
            int i1 = p_150541_5_ + MathHelper.floor((float)l / (float)i * 3.5F);
            this.growLeavesLayerStrict(worldIn, new BlockPos(x, k, z), i1 + (l > 0 && i1 == j && (k & 1) == 0 ? 1 : 0));
            j = i1;
        }
    }

    public void generateSaplings(World worldIn, Random random, BlockPos pos)
    {
    }
    //Helper macro
    private boolean isAirLeaves(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos);
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
    
    protected boolean ensureGrowable(World worldIn, Random rand, BlockPos treePos, int height)
    {
        return this.isSpaceAt(worldIn, treePos, height);
    }
    
    private boolean isSpaceAt(World worldIn, BlockPos leavesPos, int height)
    {
        boolean flag = true;

        if (leavesPos.getY() >= 1 && leavesPos.getY() + height + 1 <= 256)
        {
            for (int i = 0; i <= 1 + height; ++i)
            {
                int j = 2;

                if (i == 0)
                {
                    j = 1;
                }
                else if (i >= 1 + height - 2)
                {
                    j = 2;
                }

                for (int k = -j; k <= j && flag; ++k)
                {
                    for (int l = -j; l <= j && flag; ++l)
                    {
                        if (leavesPos.getY() + i < 0 || leavesPos.getY() + i >= 256 || !this.isReplaceable(worldIn,leavesPos.add(k, i, l)))
                        {
                            flag = false;
                        }
                    }
                }
            }

            return flag;
        }
        else
        {
            return false;
        }
    }
    
    public boolean isReplaceable(World world, BlockPos pos) {
    	IBlockState state = world.getBlockState(pos);
        return state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos);
    }
}
