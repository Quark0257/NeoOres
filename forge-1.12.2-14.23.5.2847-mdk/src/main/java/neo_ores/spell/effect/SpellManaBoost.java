package neo_ores.spell.effect;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.util.RayTraceUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class SpellManaBoost extends SpellEffect
{
	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{		
	}

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		return RayTraceUtils.getSimpleResult(runner);
	}
}
