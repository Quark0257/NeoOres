package neo_ores.block;

import neo_ores.world.dimension.DimensionHelper.DimensionName;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDimensionLog extends BlockDimensionPillarSingleModel {

	public BlockDimensionLog(String registername, Material materialIn, float hardness, float resistant,
			String harvest_key, int harvest_level, float light, SoundType sound) {
		super(registername, materialIn, hardness, resistant, harvest_key, harvest_level, light, sound);
		this.setTickRandomly(true);
	}
	
	@Override 
	public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos){ 
		return true; 
	}
	
    @Override 
    public boolean isWood(IBlockAccess world, BlockPos pos){ 
    	return true; 
    }
    
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
    	if(world.getBlockState(pos).getValue(DIM) == DimensionName.FIRE || world.getBlockState(pos).getValue(DIM) == DimensionName.WATER) return 0; 
		return 5;
    }
	
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) 
	{
		if(world.getBlockState(pos).getValue(DIM) == DimensionName.FIRE || world.getBlockState(pos).getValue(DIM) == DimensionName.WATER) return 0; 
		return 5;
	}
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (worldIn.isAreaLoaded(pos.add(-5, -5, -5), pos.add(5, 5, 5)))
        {
            for (BlockPos blockpos : BlockPos.getAllInBox(pos.add(-4, -4, -4), pos.add(4, 4, 4)))
            {
                IBlockState iblockstate = worldIn.getBlockState(blockpos);

                if (iblockstate.getBlock().isLeaves(iblockstate, worldIn, blockpos))
                {
                    iblockstate.getBlock().beginLeavesDecay(iblockstate, worldIn, blockpos);
                }
            }
        }
    }
}
