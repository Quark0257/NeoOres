package neo_ores.block;

import java.util.Random;

import neo_ores.main.NeoOresBlocks;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockLiquidMana extends BlockFluidNeoOres
{
	private static final Random random = new Random();
	
	public BlockLiquidMana(Fluid fluid)
	{
		super(fluid, new MaterialLiquid(MapColor.ICE));
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockFluidBase.LEVEL, 0));
	}
	
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighbor, BlockPos neighborPos) 
	{
		super.neighborChanged(state, worldIn, pos, neighbor, neighborPos);
		this.onAdjacent(worldIn, pos);
	}
	
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) 
	{
		super.onBlockAdded(worldIn, pos, state);
		this.onAdjacent(worldIn, pos);
	}
	
	public void onAdjacent(World world, BlockPos pos) 
	{
		boolean flag = false;
		for(EnumFacing side : EnumFacing.VALUES) 
		{
			IBlockState sideState = world.getBlockState(pos.offset(side));
			Block sideBlock = sideState.getBlock();
			if(sideBlock != NeoOresBlocks.liquid_mana && sideState.getMaterial().isLiquid()) 
			{
				int waterTemp = FluidRegistry.WATER.getTemperature();
				int lavaTemp = FluidRegistry.LAVA.getTemperature();
				int temperature = sideState.getBlock() instanceof BlockFluidBase ? ((BlockFluidBase)sideState.getBlock()).getFluid().getTemperature() : 300;
				if(sideBlock == Blocks.FLOWING_WATER || sideBlock == Blocks.WATER) 
				{
					temperature = waterTemp;
				}
				else if(sideBlock == Blocks.FLOWING_LAVA || sideBlock == Blocks.LAVA) 
				{
					temperature = lavaTemp;
				}
				
				boolean isWater = (waterTemp - waterTemp / 2.0 <= temperature && temperature <= waterTemp + waterTemp / 2.0);
				if(side != EnumFacing.DOWN) 
				{
					world.setBlockState(pos.offset(side), NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, isWater ? DimensionName.WATER : DimensionName.FIRE));
				}
				else 
				{
					world.setBlockState(pos.offset(side), NeoOresBlocks.dim_stone.getDefaultState().withProperty(BlockDimension.DIM, isWater ? DimensionName.AIR : DimensionName.EARTH));
				}
				world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + random.nextDouble(), pos.getY() + 1.0D, pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
				flag = true;
			}
		}
		
		if(flag) 
		{
			world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.5F);
		}
	}
}
