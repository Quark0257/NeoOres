package neo_ores.entity.monster;

import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

public abstract class EntitySpellingMeleeMob extends EntityMob
{
	public EntitySpellingMeleeMob(World worldIn)
	{
		super(worldIn);
	}

	public abstract SpellData getAttackSpell();
	
	public boolean attackEntityAsMob(Entity entityIn)
	{
		this.getAttackSpell().run(this, RayTraceUtils.getSimpleResult(entityIn));
		return true;
	}
}
