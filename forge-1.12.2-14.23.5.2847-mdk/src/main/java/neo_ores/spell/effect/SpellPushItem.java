package neo_ores.spell.effect;

import neo_ores.api.spell.Spell.SpellEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class SpellPushItem extends SpellEffect {

	@Override
	public void onEffectRunToSelf(World world, EntityLivingBase runner, ItemStack stack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEffectRunToOther(World world, RayTraceResult result, ItemStack stack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEffectRunToSelfAndOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack) {
		if(result != null && result.typeOfHit == Type.BLOCK) {
			
		}
	}
}
