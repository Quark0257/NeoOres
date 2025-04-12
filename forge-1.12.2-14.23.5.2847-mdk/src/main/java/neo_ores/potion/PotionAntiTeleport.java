package neo_ores.potion;

import net.minecraft.entity.EntityLivingBase;

public class PotionAntiTeleport extends PotionNeoOres
{
	public PotionAntiTeleport(String name)
	{
		super(true, 0x7B10E6, name);
	}

	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
	{
	}

	public boolean isReady(int duration, int amplifier)
	{
		return true;
	}
}
