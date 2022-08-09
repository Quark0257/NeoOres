package neo_ores.potion;

import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;

public class PotionAntiKnockback extends PotionNeoOres
{
	private final UUID uid;
	public PotionAntiKnockback(String name) 
	{
		super(false, 0xB5FF00, name);
		uid = new UUID((long) ((this.random.nextDouble() * 2.0D - 1.0D) * Long.MAX_VALUE),(long) ((this.random.nextDouble() * 2.0D - 1.0D) * Long.MAX_VALUE));
		this.registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, uid.toString(), 0.2D, 0);
	}
}
