package neo_ores.spell.effect;

import neo_ores.spell.SpellItemInterfaces.HasAmplify;
import neo_ores.spell.SpellItemInterfaces.HasDuration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class SpellEffectEntityBuffBase extends SpellEffectEntityBase implements HasDuration, HasAmplify
{
	protected int amp = 0;
	protected int duration = 0;
	
	protected void onEffect(World world, Entity elb, EntityLivingBase runner, ItemStack stack) 
	{
		if (!this.isFakePlayer(elb)) 
		{
			this.onBuff(world, elb, runner, stack);
		}
	}
	
	protected abstract void onBuff(World world, Entity elb, EntityLivingBase runner, ItemStack stack);
	
	@Override
	public void setAmplify(int level)
	{
		this.amp = level;
	}
	
	@Override
	public void setDuration(int level)
	{
		this.duration = level;
	}
}
