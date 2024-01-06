package neo_ores.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class BlockDimensionSaplingWater extends BlockDimensionSapling
{

	public BlockDimensionSaplingWater(String registername, Block sustainStates, float hardness, float resistant, float light, SoundType sound)
	{
		super(registername, Material.WATER, sustainStates, hardness, resistant, light, sound);
		this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE, 0).withProperty(BlockLiquid.LEVEL, 15));
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { STAGE, BlockLiquid.LEVEL });
	}
	
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }
	
	public boolean isTopSolid(IBlockState state)
    {
        return state.getMaterial().isOpaque() && state.isFullCube();
    }

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getBlock() == this) // Forge: This function is called during world gen and placement, before this
										// block is set, so if we are not 'here' then assume it's the pre-check.
		{
			IBlockState soil = worldIn.getBlockState(pos.down());
			return soil.getBlock() == this.sustainState
					&& (worldIn.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.FLOWING_WATER || worldIn.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.WATER);
		}
		return this.canSustainBush(worldIn.getBlockState(pos.down()))
				&& (worldIn.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.FLOWING_WATER || worldIn.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.WATER);
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Water;
	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return super.canPlaceBlockAt(worldIn, pos) && (worldIn.getBlockState(pos).getBlock() == Blocks.FLOWING_WATER || worldIn.getBlockState(pos).getBlock() == Blocks.WATER)
				&& (worldIn.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.FLOWING_WATER || worldIn.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.WATER);
	}

	@SuppressWarnings("rawtypes")
	public IProperty[] setNoRenderProperties()
	{
		return new IProperty[] { BlockLiquid.LEVEL };
	}
}
