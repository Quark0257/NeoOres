package neo_ores.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIHurtByTarget;

public class EntityAIHurtByTargetNoSight extends EntityAIHurtByTarget
{
	public EntityAIHurtByTargetNoSight(EntityCreature creatureIn, boolean entityCallsForHelpIn, Class<?>[] excludedReinforcementTypes)
	{
		super(creatureIn, entityCallsForHelpIn, excludedReinforcementTypes);
		this.shouldCheckSight = false;
	}
}
