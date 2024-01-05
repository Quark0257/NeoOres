package neo_ores.potion;

import net.minecraft.entity.EntityLivingBase;

public class PotionGravity extends PotionNeoOres
{
	public PotionGravity(String name)
	{
		super(true, 0x250052, name);
	}

	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
	{
		if (entityLivingBaseIn != null)
		{
			entityLivingBaseIn.motionY = -(amplifier + 1) * 10;
		}
	}

	public boolean isReady(int duration, int amplifier)
	{
		return true;
	}
}
