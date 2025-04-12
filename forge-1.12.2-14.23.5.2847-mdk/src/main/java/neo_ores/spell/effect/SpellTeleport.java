package neo_ores.spell.effect;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.event.NeoOresRegisterEvents;
import neo_ores.main.NeoOres;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class SpellTeleport extends SpellEffect
{
	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		return null;
	}

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result == null || result.typeOfHit == Type.MISS || runner instanceof FakePlayer) 
		{
			return;
		}
		
		if (runner.isPotionActive(NeoOres.antienderteleport) && runner.getActivePotionEffect(NeoOres.antienderteleport).getAmplifier() >= 1)
		{
			return;
		}
		
		double targetX = 0;
		double targetY = 0;
		double targetZ = 0;
		if (result.typeOfHit == Type.BLOCK) 
		{
			targetX = result.hitVec.x;
			targetY = result.hitVec.y;
			targetZ = result.hitVec.z;
		} 
		else if (result.typeOfHit == Type.ENTITY) 
		{
			if (result.entityHit == null || result.entityHit == runner) 
			{
				return;
			}
			targetX = result.entityHit.posX;
			targetY = result.entityHit.posY;
			targetZ = result.entityHit.posZ;
		}
		else
		{
			return;
		}
		
		if (runner.isRiding())
        {
			runner.dismountRidingEntity();
        }
		
		SpellUtils.onDisplayParticleTypeAEntity(world, runner, NeoOresRegisterEvents.particle0, SpellUtils.getColor(stack), 16);
		runner.setPositionAndUpdate(targetX, targetY, targetZ);
	}
}
