package neo_ores.spell.effect;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasAmplify;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class SpellGrow extends SpellEffect implements HasRange, HasAmplify
{
	private int range = 0;
	private int amp = 0;

	@Override
	public void setRange(int value)
	{
		this.range = value;
	}

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		return RayTraceUtils.getMissResult();
	}

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result == null || result.typeOfHit == Type.MISS)
			return;
		if (result.typeOfHit == Type.BLOCK)
		{
			EnumFacing face = result.sideHit;
			for (BlockPos pos : HasRange.rangedPos(result.getBlockPos(), face, this.range))
			{
				IBlockState state = world.getBlockState(pos);
				if (state.getBlock() instanceof IPlantable)
				{
					SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(1, 1, 1), SpellUtils.getColor(stack), 8);
					for (int i = 0; i < (this.amp * this.amp * 2 + 1) * 10; i++)
					{
						state.getBlock().updateTick(world, pos, state, world.rand);
					}
					
					if (runner instanceof EntityPlayerMP)
					{
						PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
						pmds.addMXP(1L + this.amp * 3);
					}
				}
				else if (state.getBlock() instanceof IGrowable)
				{
					SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(1, 1, 1), SpellUtils.getColor(stack), 8);
					if (((IGrowable) state.getBlock()).canGrow(world, pos, state, false))
					{
						for (int i = 0; i < (this.amp * 2 + 1); i++)
						{
							((IGrowable) state.getBlock()).grow(world, world.rand, pos, state);
						}
					}
					
					if (runner instanceof EntityPlayerMP)
					{
						PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
						pmds.addMXP(1L + this.amp * 3);
					}
				}
			}
		}
		else if (result.typeOfHit == Type.ENTITY)
		{
			Entity entity = result.entityHit;
			if (entity == null)
				return;
			for (Entity temp : HasRange.getRangedEntities(world, this.range, entity, runner, false, true))
			{
				if (temp instanceof EntityAgeable)
				{
					EntityAgeable age = (EntityAgeable) temp;
					age.ageUp(this.amp * 2 + 1, true);
					SpellUtils.onDisplayParticleTypeAEntity(world, temp, SpellUtils.getColor(stack), 16);
				}
				
				if (runner instanceof EntityPlayerMP)
				{
					PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
					pmds.addMXP(1L + this.amp * 3);
				}
			}
		}
	}

	@Override
	public void setAmplify(int level)
	{
		this.amp = level;
	}
}
