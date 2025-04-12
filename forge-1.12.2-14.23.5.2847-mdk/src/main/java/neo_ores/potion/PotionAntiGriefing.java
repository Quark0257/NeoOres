package neo_ores.potion;

import net.minecraft.entity.EntityLivingBase;

public class PotionAntiGriefing extends PotionNeoOres
{
	public PotionAntiGriefing(String name)
	{
		super(true, 0xE490E8, name);
	}

	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
	{
	}

	public boolean isReady(int duration, int amplifier)
	{
		return true;
	}
}
