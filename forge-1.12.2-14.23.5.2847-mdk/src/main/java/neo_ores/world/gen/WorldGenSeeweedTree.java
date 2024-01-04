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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenSeeweedTree extends WorldGenAbstractTree {

	private final IBlockState trunk;
	private final IBlockState leaf;
	private final int baseHeight;
	private final int extraHeight;
	private final int range;
	
	public WorldGenSeeweedTree(boolean notify,int baseHeight,int extraHeight, int range,IBlockState trunk, IBlockState leaf) {
		super(notify);
		this.trunk = trunk;
		this.leaf = leaf;
		this.baseHeight = baseHeight;
		this.extraHeight = extraHeight;
		this.range = range;
	}
	
	public static WorldGenSeeweedTree make(boolean notify, int baseHeight, int extraHeight, Block trunk, Block leaf, DimensionName dim) {
    	IBlockState trunkState = trunk.getDefaultState().withProperty(BlockDimension.DIM, dim);
    	IBlockState leafState = leaf.getDefaultState().withProperty(BlockDimension.DIM, dim).withProperty(BlockDimensionLeaves.CHECK_DECAY, false).withProperty(BlockDimensionLeaves.DECAYABLE, true);
    	return new WorldGenSeeweedTree(notify,baseHeight, extraHeight, 2,trunkState,leafState);
    }

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		int base = this.baseHeight + rand.nextInt(3) + rand.nextInt(this.extraHeight);
		List<BlockPos> logs = new ArrayList<BlockPos>();
		BlockPos tempPos = position;
		for(int y0 = 1;y0 < base;y0++) {
			int x = rand.nextInt(3) - 1;
			int z = rand.nextInt(3) - 1;
			while(true) {
				if(this.isInRange(position, tempPos.add(x,1,z))) {
					tempPos = tempPos.add(x,1,z);
					break;
				}
				x = rand.nextInt(3) - 1;
				z = rand.nextInt(3) - 1;
			}
			
			if(!this.isReplaceable(worldIn, tempPos)) return false;
			logs.add(tempPos);
		}
		List<BlockPos> leaves1 = new ArrayList<BlockPos>();
		for(BlockPos pos : logs) {
			for(int i = 0;i < 4;i++) {
				if(rand.nextInt(2) == 0) {
					BlockPos leafPos = pos.add(EnumFacing.HORIZONTALS[i % 4].getDirectionVec());
					if(this.isReplaceable(worldIn,leafPos)) leaves1.add(leafPos);
				}
			}
			for(int i = 0;i < 4;i++) {
				if(rand.nextInt(2) == 0) {
					BlockPos leafPos = pos.add(EnumFacing.VALUES[i % 2].getDirectionVec());
					if(this.isReplaceable(worldIn,leafPos)) leaves1.add(leafPos);
				}
			}
		}
		List<BlockPos> leaves2 = new ArrayList<BlockPos>();
		for(BlockPos pos : leaves1) {
			for(int i = 0;i < 4;i++) {
				if(rand.nextInt(2) == 0) {
					BlockPos leafPos = pos.add(EnumFacing.HORIZONTALS[i % 4].getDirectionVec());
					if(this.isReplaceable(worldIn,leafPos)) leaves2.add(leafPos);
				}
			}
		}
		leaves1.addAll(leaves2);
		for(BlockPos pos : leaves1) this.setBlockAndNotifyAdequately(worldIn, pos, this.leaf);
		for(BlockPos pos : logs) this.setBlockAndNotifyAdequately(worldIn, pos, this.trunk);
		this.setBlockAndNotifyAdequately(worldIn, position, this.trunk);
		return true;
	}
	
	public boolean isInRange(BlockPos basePos, BlockPos compare) {
		return basePos.getX() - this.range <= compare.getX() && compare.getX() <= basePos.getX() + this.range
				&& basePos.getZ() - this.range <= compare.getZ() && compare.getZ() <= basePos.getZ() + this.range;
	}
	
	public boolean isReplaceable(World world, BlockPos pos) {
    	IBlockState state = world.getBlockState(pos);
        return state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos) 
        		|| state.getBlock() == Blocks.FLOWING_WATER || state.getBlock() == Blocks.WATER;
    }
}
