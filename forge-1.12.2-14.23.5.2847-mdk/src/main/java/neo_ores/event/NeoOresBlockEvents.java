package neo_ores.event;

import neo_ores.block.BlockFluidNeoOres;
import neo_ores.main.Reference;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class NeoOresBlockEvents
{
	@SubscribeEvent
	public void onFillBucket(FillBucketEvent event) 
	{
		EntityPlayer playerIn = event.getEntityPlayer();
		RayTraceResult raytraceresult = event.getTarget();
		ItemStack itemstack = event.getEmptyBucket();
		World worldIn = event.getWorld();
		if (raytraceresult == null)
        {
        }
        else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK)
        {
        }
        else
        {
            BlockPos blockpos = raytraceresult.getBlockPos();

            if (!worldIn.isBlockModifiable(playerIn, blockpos))
            {
            }
            else if (itemstack.getItem() == Items.BUCKET)
            {
                if (!playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack))
                {
                }
                else
                {
                    IBlockState iblockstate = worldIn.getBlockState(blockpos);
                    Material material = iblockstate.getMaterial();

                    if (material == Material.WATER && ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0)
                    {
                    	if(iblockstate.getBlock() instanceof BlockFluidNeoOres) {
                    		FluidStack fluid = ((BlockFluidNeoOres)iblockstate.getBlock()).drain(worldIn, blockpos, true);
                    		worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
                            playerIn.addStat(StatList.getObjectUseStats(Items.BUCKET));
                            playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                            event.setFilledBucket(FluidUtil.getFilledBucket(fluid));
                            event.setResult(Result.ALLOW);
                    	}
                    }
                }
            }
        }
	}
}
