package neo_ores.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PotionFlying extends PotionNeoOres
{
	public PotionFlying(String name)
	{
		super(false, 0xE5E5E5, name);
	}

	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
	{
		if (entityLivingBaseIn instanceof EntityPlayerMP)
		{
			EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
			player.capabilities.allowFlying = true;
			player.sendPlayerAbilities();
		}
	}
	
	public boolean isReady(int duration, int amplifier)
    {
		return true;
    }
}
