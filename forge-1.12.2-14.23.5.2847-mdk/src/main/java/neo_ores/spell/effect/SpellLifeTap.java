package neo_ores.spell.effect;

import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasAmplify;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SpellLifeTap extends SpellEffectEntityBase implements HasAmplify
{
	private int amp = 0;

	@Override
	public void setAmplify(int level)
	{
		this.amp = level;
	}

	@Override
	protected void onEffect(World world, Entity elb, EntityLivingBase runner, ItemStack stack)
	{
		float pay = this.amp * 2.0f + 1.0f;
		SpellUtils.onDisplayParticleTypeAEntity(world, elb, SpellUtils.getColor(stack), 16);
		if (!SpellUtils.spellPay(runner, pay))
			return;
		if (elb instanceof EntityPlayerMP)
		{
			double rate = 0.1 * pay / runner.getMaxHealth();
			PlayerMagicData pmd = NeoOresData.instance.getPMD((EntityPlayerMP) elb);
			pmd.addMana((long) (rate * pmd.getMaxMana()));

			if (runner instanceof EntityPlayerMP)
			{
				PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
				pmds.addMXP(1L + this.amp * 5);
			}
		}
	}
}
