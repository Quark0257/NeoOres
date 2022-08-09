package neo_ores.potion;

import java.util.UUID;

import neo_ores.entity.fakeattribute.FakeAttributeMaxMana;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayerMP;

public class PotionManaWeakness  extends PotionNeoOres implements PotionNeoOres.IFakeAttributeModified
{
	private final UUID uid;
	
	public PotionManaWeakness(String name)
	{
		super(false, 0xCEA1FF,name);
		uid = new UUID((long) ((this.random.nextDouble() * 2.0D - 1.0D) * Long.MAX_VALUE),(long) ((this.random.nextDouble() * 2.0D - 1.0D) * Long.MAX_VALUE));
	}
	
	public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
	{
		if(entityLivingBaseIn != null && entityLivingBaseIn instanceof EntityPlayerMP)
		{
			FakeAttributeMaxMana famm = new FakeAttributeMaxMana((EntityPlayerMP)entityLivingBaseIn);
			if(famm.getModifierByUUID(uid) != null) famm.removeModifierByUUID(uid);;
		}
	}
	
	public static float getAdditionFromMagnification(int amplifier)
	{
		return -amplifier;
	}
	
	public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
	{
		if(entityLivingBaseIn != null && entityLivingBaseIn instanceof EntityPlayerMP)
		{
			FakeAttributeMaxMana famm = new FakeAttributeMaxMana((EntityPlayerMP)entityLivingBaseIn);
			famm.modifierToPlayerWithUUID(uid,getAdditionFromMagnification(amplifier),1);
		}
	}
	
	public UUID getID()
	{
		return uid;
	}

	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, int amplifier) 
	{
		this.applyAttributesModifiersToEntity(entityLivingBaseIn, null, amplifier);
	}
}
