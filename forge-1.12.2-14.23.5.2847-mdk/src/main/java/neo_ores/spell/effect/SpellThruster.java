package neo_ores.spell.effect;

import neo_ores.entity.EntityThruster;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasAmplify;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SpellThruster extends SpellEffectEntityBase implements HasAmplify
{
	private int amp = 0;

	@Override
	protected void onEffect(World world, Entity elb, EntityLivingBase runner, ItemStack stack)
	{
		if (this.isFakePlayer(elb))
		{
			return;
		}

		if (elb.isRiding())
		{
			return;
		}

		if (elb instanceof EntityLivingBase)
		{
			SpellUtils.onDisplayParticleTypeAEntity(world, elb, SpellUtils.getColor(stack), 16);
			EntityLivingBase base = (EntityLivingBase) elb;
			EntityThruster thruster = new EntityThruster(world, SpellUtils.getColor(stack), this.amp, base);
			world.spawnEntity(thruster);
		}

		if (runner instanceof EntityPlayerMP)
		{
			PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
			pmds.addMXP(1L + this.amp * 2);
		}
	}

	@Override
	public void setAmplify(int level)
	{
		this.amp = level;
	}
}
