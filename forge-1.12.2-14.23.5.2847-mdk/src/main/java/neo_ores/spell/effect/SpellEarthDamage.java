package neo_ores.spell.effect;

import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresData;
import neo_ores.util.EntityDamageSourceWithItem;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.ServerUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SpellEarthDamage extends SpellDamageBase
{
	@Override
	protected void onDamage(World world, Entity elb, EntityLivingBase runner, ItemStack stack)
	{
		float amount = (float) (3.5 * Math.pow(1.5, this.damageLevel)) + 3.0f;
		if (ServerUtils.damageEntity(elb, EntityDamageSourceWithItem.setDamageByEntityWithItem(NeoOres.EARTH, runner, stack), 0.6f * amount))
		{
			if (elb instanceof EntityLivingBase)
			{
				elb.hurtResistantTime = 0;
			}
		}
		ServerUtils.damageEntity(elb, EntityDamageSourceWithItem.setDamageByEntityWithItem(EntityDamageSourceWithItem.getPhysicalDamage(runner), runner, stack), 0.4f * amount);
		if (elb instanceof EntityLivingBase)
		{
			elb.hurtResistantTime = ((EntityLivingBase) elb).maxHurtResistantTime;
		}
		SpellUtils.onDisplayParticleTypeAEntity(world, elb, SpellUtils.getColor(stack), 16);
		if (runner instanceof EntityPlayerMP)
		{
			PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
			pmds.addMXP((long) amount + (long) Math.pow(3, luck));
		}
	}
}
