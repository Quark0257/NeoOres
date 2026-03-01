package neo_ores.spell.effect;

import neo_ores.api.ICompareBlockState;
import neo_ores.api.RecipeOreStackWildCardPostScript;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasChain;
import neo_ores.spell.SpellItemInterfaces.HasOffsetDown;
import neo_ores.spell.SpellItemInterfaces.HasOffsetUp;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class SpellPlaceInfinity extends SpellEffectItemFiltered implements HasOffsetUp, HasOffsetDown
{
	private boolean offsetUp = false;
	private boolean offsetDown = false;
	
	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		BlockPos pos = new BlockPos(runner.posX, runner.posY, runner.posZ);
		return RayTraceUtils.getSimpleResult(pos, null);
	}

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result1, ItemStack stack)
	{
		RayTraceResult result = this.getResultBlockFromEntity(world, result1, stack, this.offsetUp, this.offsetDown);
		if (result == null)
			return;
		if (world.isRemote)
		{
			return;
		}
		if (result.typeOfHit == Type.BLOCK)
		{
			EnumFacing face = result.sideHit;
			boolean posAir = world.isAirBlock(result.getBlockPos());
			ItemStack item = RecipeOreStackWildCardPostScript.INFINITY_SPELL_MATERIAL.reverse(stack.getTagCompound().getCompoundTag(SpellUtils.NBTTagUtils.ADDITIONAL));
			if (item.isEmpty())
			{
				return;
			}
			for (BlockPos pos : this.rangeMode ? HasRange.rangedPos(result.getBlockPos(), face, this.range) : HasChain.getChainedPos(world, chain, result.getBlockPos(), ICompareBlockState.ITEM))
			{
				if (!this.canEditBlocksBySpells(runner, stack, world, pos, face)) 
				{
					continue;
				}
				BlockPos targetPos = posAir ? pos : pos.add(face.getDirectionVec());
				if (item.getItem() instanceof ItemBlock)
				{
					IBlockState state = ((ItemBlock) item.getItem()).getBlock().getStateForPlacement(world, targetPos, face, (float) result.hitVec.x, (float) result.hitVec.y, (float) result.hitVec.z,
							item.getMetadata(), runner, EnumHand.MAIN_HAND);

					if (runner instanceof EntityPlayer)
					{
						if (world.mayPlace(state.getBlock(), targetPos, false, face, runner))
						{
							SpellUtils.onDisplayParticleTypeA(world, new Vec3d(targetPos.getX(), targetPos.getY(), targetPos.getZ()), new Vec3d(1, 1, 1), SpellUtils.getColor(stack), 8);
							if (((ItemBlock) item.getItem()).placeBlockAt(item, (EntityPlayer) runner, world, targetPos, face, (float) result.hitVec.x, (float) result.hitVec.y,
									(float) result.hitVec.z, state))
							{
								if (runner instanceof EntityPlayerMP)
								{
									PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
									pmds.addMXP(10);
								}
							}
						}
					}
					else if (world.mayPlace(state.getBlock(), targetPos, true, face, runner))
					{
						System.out.println(stack.getTagCompound());
						SpellUtils.onDisplayParticleTypeA(world, new Vec3d(targetPos.getX(), targetPos.getY(), targetPos.getZ()), new Vec3d(1, 1, 1), SpellUtils.getColor(stack), 8);
						if (world.setBlockState(targetPos, state, 11))
						{
							IBlockState newState = world.getBlockState(targetPos);
							if (state.getBlock() == newState.getBlock())
							{
								state.getBlock().onBlockPlacedBy(world, targetPos, state, runner, item);
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void setOffsetDown()
	{
		this.offsetDown = true;
	}

	@Override
	public void setOffsetUp()
	{
		this.offsetUp = true;
	}
}
