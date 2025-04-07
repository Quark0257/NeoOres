package neo_ores.spell.effect;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.RayTraceUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public abstract class SpellEffectEntityBase extends SpellEffect implements HasRange
{
	protected int range0 = 0;

	@Override
	public void setRange(int value)
	{
		this.range0 = value;
	}

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result != null && result.typeOfHit == Type.ENTITY && result.entityHit != null)
		{
			Entity entity = (Entity) result.entityHit;
			for (Entity elb : HasRange.getRangedEntities(world, this.range0, entity, runner, true, false))
			{
				this.onEffect(world, elb, runner, stack);
			}
		}
	}

	protected abstract void onEffect(World world, Entity elb, EntityLivingBase runner, ItemStack stack);
	
	protected boolean isFakePlayer(Entity elb) 
	{
		return elb instanceof FakePlayer;
	}

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		return RayTraceUtils.getSimpleResult(runner);
	}
}

