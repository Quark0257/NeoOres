package neo_ores.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public abstract class EntityAISpellingBase<T extends EntityCreature & ISpelling> extends EntityAIBase
{
	protected final T entity;
	protected final World world;
	protected final int interval;
	protected int timer;
	protected boolean isSpelling;
	protected final int spellingTime;
	protected final int rangeInterval;
	protected int nextInterval;
	
	public EntityAISpellingBase(T entity, int baseInterval, int rangeInterval, int spellingTime) 
	{
		this.entity = entity;
		this.world = entity.world;
        this.setMutexBits(4);
        this.interval = baseInterval;
        this.rangeInterval = rangeInterval;
        this.timer = 0;
        this.isSpelling = false;
        this.spellingTime = spellingTime;
        this.setNextInterval();
	}
	
	public void setNextInterval() 
	{
		this.nextInterval = this.entity.world.rand.nextInt(this.rangeInterval) + this.interval - this.rangeInterval / 2;
	}

	@Override
	public boolean shouldExecute()
	{
		if (this.entity.isSpelling() && !this.isSpelling) 
		{
			return false;
		}
		return true;
	}
	
	public void resetTask()
    {
		this.entity.setSpelling(false);
		this.timer = 0;
		this.isSpelling = false;
		this.setNextInterval();
    }
	
	public void updateTask()
    {
    	if (this.timer > this.nextInterval) 
    	{
    		if (!this.entity.isSpelling()) 
    		{
    			this.entity.setSpelling(true);
    			this.isSpelling = true;
    		}
    		else if (this.timer > this.nextInterval + this.spellingTime && this.isSpelling) 
    		{
    			this.runSpell();
    			this.setNextInterval();
    			this.entity.setSpelling(false);
    			this.isSpelling = false;
    			this.timer = 0;
    		}
    	}
    	this.timer++;
    }
	
	public abstract void runSpell();
}
