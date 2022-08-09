package neo_ores.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;

public class PotionFreeze  extends PotionNeoOres
{
	public PotionFreeze(String name) 
	{
		super(true, 0x7CECFF, name);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "00000000-0000-4498-935B-2F7F68070635",-1.0D, 1);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, "00000000-1111-4498-935B-2F7F68070635",-1.0D, 1);
	}
	
	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
	{
		if(entityLivingBaseIn != null)
		{
			entityLivingBaseIn.motionX = 0.0;
			entityLivingBaseIn.motionZ = 0.0;
		}
	}
	
	public boolean isReady(int duration, int amplifier)
	{
		return true;
	}
}
