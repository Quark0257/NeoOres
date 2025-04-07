package neo_ores.potion;

import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;

public class PotionAntiKnockback extends PotionNeoOres
{
	private final UUID uid;

	public PotionAntiKnockback(String name)
	{
		super(false, 0xB5FF00, name);
		this.uid = UUID.randomUUID();
		this.registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, this.uid.toString(), 0.2D, 0);
	}
}
