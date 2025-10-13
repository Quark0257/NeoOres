package neo_ores.spell.effect;

import neo_ores.spell.SpellItemInterfaces.HasNegative;
import neo_ores.spell.SpellItemInterfaces.HasPositive;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class SpellDispel extends SpellEffectEntityBuffBase implements HasNegative, HasPositive
{
	private boolean negative = false;
	private boolean positive = false;

	@Override
	protected void onBuff(World world, Entity elb, EntityLivingBase runner, ItemStack stack)
	{
		if (!this.negative && !this.positive)
		{
			runner.clearActivePotions();
		}
		else
		{
			for (PotionEffect potion : runner.getActivePotionEffects())
			{
				if (this.negative && potion.getPotion().isBadEffect() || this.positive && !potion.getPotion().isBadEffect())
				{
					runner.removeActivePotionEffect(potion.getPotion());
				}
			}
		}
	}

	@Override
	public void setPositive()
	{
		this.positive = true;
	}

	@Override
	public void setNegative()
	{
		this.negative = true;
	}
}
