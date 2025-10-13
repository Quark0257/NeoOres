package neo_ores.entity.ai;

import net.minecraft.entity.EntityCreature;

public class EntityAISelfSpelling<T extends EntityCreature & ISpellingSelf> extends EntityAISpellingBase<T>
{
	public EntityAISelfSpelling(T entity, int intervalBase, int intervalRange, int spellingTime) 
	{
		super(entity, intervalBase, intervalRange, spellingTime);
	}
	
	@Override
	public boolean shouldExecute()
	{
		return this.entity.allowSelf() && super.shouldExecute();
	}

	@Override
	public void runSpell()
	{
		this.entity.getSelfSpell().run(this.entity, null);
	}
}
