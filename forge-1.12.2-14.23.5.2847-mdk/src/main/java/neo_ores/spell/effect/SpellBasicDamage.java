package neo_ores.spell.effect;

import neo_ores.main.NeoOresData;
import neo_ores.util.EntityDamageSourceWithItem;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.ServerUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class SpellBasicDamage extends SpellDamageBase
{
	protected float baseAmount;
	protected float spellDamageRate;
	protected DamageSource source;

	public SpellBasicDamage(DamageSource spellSource, float spellDamageRate, float baseAmount)
	{
		this.baseAmount = baseAmount;
		this.source = spellSource;
		this.spellDamageRate = spellDamageRate;
	}

	@Override
	protected void onDamage(World world, Entity elb, EntityLivingBase runner, ItemStack stack)
	{
		float amount = (float) (this.baseAmount * (Math.pow(1.5, this.damageLevel) + 1.0));
		boolean flag = true;
		if (elb instanceof EntityLivingBase)
		{
			flag = elb.hurtResistantTime <= ((EntityLivingBase) elb).maxHurtResistantTime * 0.5F;
		}
		
		if (flag)
		{
			if (ServerUtils.damageEntity(elb, EntityDamageSourceWithItem.setDamageByEntityWithItem(this.source, runner, stack), this.spellDamageRate * amount)) 
			{
				if (elb instanceof EntityLivingBase)
				{
					elb.hurtResistantTime = 0;
					((EntityLivingBase) elb).hurtTime = 0;
				}
			}
			ServerUtils.damageEntity(elb, EntityDamageSourceWithItem.setDamageByEntityWithItem(EntityDamageSourceWithItem.getPhysicalDamage(runner), runner, stack),
					(1.0f - this.spellDamageRate) * amount);
			if (elb instanceof EntityLivingBase)
			{
				elb.hurtResistantTime = ((EntityLivingBase) elb).maxHurtResistantTime;
				((EntityLivingBase) elb).hurtTime = ((EntityLivingBase) elb).maxHurtTime;
			}
		}
		
		SpellUtils.onDisplayParticleTypeAEntity(world, elb, SpellUtils.getColor(stack), 16);
		if (runner instanceof EntityPlayerMP)
		{
			PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
			pmds.addMXP((long) amount + (long) Math.pow(3, luck));
		}
	}
}
