package neo_ores.spell.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class SpellPotionEffect extends SpellEffectEntityBuffBase
{
	protected final PotionEffect effect;

	public SpellPotionEffect(PotionEffect potionEffect)
	{
		this.effect = potionEffect;
	}

	@Override
	protected void onBuff(World world, Entity elb, EntityLivingBase runner, ItemStack stack)
	{
		if (elb instanceof EntityLivingBase)
		{
			EntityLivingBase entity = (EntityLivingBase) elb;
			PotionEffect correctedEffect = new PotionEffect(this.effect.getPotion(), this.effect.getDuration() * (this.duration + 1), (this.effect.getAmplifier() + 1) * (this.amp + 1) - 1,
					this.effect.getIsAmbient(), this.effect.doesShowParticles());
			entity.addPotionEffect(correctedEffect);
		}
	}
}
