package neo_ores.spell.effect;

import neo_ores.api.MathUtils;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasAmplify;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
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

		// TODO adjust
		double length = 3 + this.amp * 2;
		Vec3d vec = MathUtils.getFromYawPitch(elb.rotationYaw, elb.rotationPitch);
		Vec3d vec2 = vec.normalize().scale(length);
		if (elb.isRiding())
		{
			return;
		}

		SpellUtils.onDisplayParticleTypeAEntity(world, elb, SpellUtils.getColor(stack), 16);
		elb.motionX = vec2.x;
		elb.motionY = vec2.y;
		elb.motionZ = vec2.z;

		if (runner instanceof EntityPlayerMP)
		{
			PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
			pmds.addMXP(1L);
		}
	}

	@Override
	public void setAmplify(int level)
	{
		this.amp = level;
	}
}
