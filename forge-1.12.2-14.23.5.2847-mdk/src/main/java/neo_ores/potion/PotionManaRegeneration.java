package neo_ores.potion;

import neo_ores.api.PlayerManaDataServer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;

public class PotionManaRegeneration extends PotionNeoOres
{
	public PotionManaRegeneration(String name) 
	{
		super(false, 0x9963FF, name);
	}
	
	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
	{
		if(entityLivingBaseIn instanceof EntityPlayerMP) 
		{
			EntityPlayerMP playermp = (EntityPlayerMP) entityLivingBaseIn;
			PlayerManaDataServer pmd = new PlayerManaDataServer(playermp);
			pmd.addMana((long)(1.0D / 50.0D * (double)pmd.getTrueMaxMana()));
		}
	}
	
	public boolean isReady(int duration, int amplifier)
	{
		return duration % (20 / (amplifier + 1)+1) == 0;
	}
}
