package neo_ores.block;

import neo_ores.world.dimension.DimensionHelper.DimensionName;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockDimensionPlanks extends BlockDimensionSingleModel
{
	public BlockDimensionPlanks(String registername, Material materialIn, float hardness, float resistant, String harvest_key, int harvest_level, float light, SoundType sound)
	{
		super(registername, materialIn, hardness, resistant, harvest_key, harvest_level, light, sound);
	}

	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		if (world.getBlockState(pos).getValue(DIM) == DimensionName.FIRE || world.getBlockState(pos).getValue(DIM) == DimensionName.WATER)
			return 0;
		return 5;
	}

	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		if (world.getBlockState(pos).getValue(DIM) == DimensionName.FIRE || world.getBlockState(pos).getValue(DIM) == DimensionName.WATER)
			return 0;
		return 20;
	}
}
