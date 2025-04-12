package neo_ores.spell.effect;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.event.NeoOresRegisterEvents;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class SpellLight extends SpellEffect implements HasRange
{
	private int range = 0;

	@Override
	public void setRange(int value)
	{
		this.range = value;
	}

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		BlockPos pos = new BlockPos(runner.posX, runner.posY, runner.posZ);
		return RayTraceUtils.getSimpleResult(pos, null);
	}

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result == null)
			return;
		if (!(runner instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) runner;
		if (result.typeOfHit == Type.BLOCK)
		{
			EnumFacing face = result.sideHit;
			boolean posAir = world.isAirBlock(result.getBlockPos());
			for (BlockPos pos : HasRange.rangedPos(result.getBlockPos(), face, this.range))
			{
				BlockPos targetPos = posAir ? pos : pos.add(face.getDirectionVec());
				SpellUtils.onDisplayParticleTypeA(world, new Vec3d(targetPos.getX(), targetPos.getY(), targetPos.getZ()), new Vec3d(1, 1, 1), NeoOresRegisterEvents.particle0,
						SpellUtils.getColor(stack), 8);
				IBlockState state = NeoOresBlocks.light.getStateForPlacement(world, targetPos, face, (float) result.hitVec.x, (float) result.hitVec.y, (float) result.hitVec.z, 0, player,
						player.getActiveHand());
				if (state.getBlock().canPlaceBlockOnSide(world, targetPos, face))
				{
					boolean flag = ((ItemBlock) Item.getItemFromBlock(NeoOresBlocks.light)).placeBlockAt(stack, player, world, targetPos, face, (float) result.hitVec.x, (float) result.hitVec.y,
							(float) result.hitVec.z, state);
					if (flag) 
					{
						if (runner instanceof EntityPlayerMP)
						{
							PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
							pmds.addMXP(1L);
						}
					}
				}
			}
		}
	}
}
