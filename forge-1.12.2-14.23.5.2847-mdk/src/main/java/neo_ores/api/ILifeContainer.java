package neo_ores.api;

import net.minecraft.util.DamageSource;

public interface ILifeContainer
{
	public boolean damageWith(DamageSource source, float amount);
}
