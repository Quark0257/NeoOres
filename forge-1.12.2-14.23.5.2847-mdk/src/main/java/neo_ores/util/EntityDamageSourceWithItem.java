package neo_ores.util;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class EntityDamageSourceWithItem extends EntityDamageSource
{

	private final ItemStack stack;

	public EntityDamageSourceWithItem(String damageTypeIn, Entity damageSourceEntityIn, ItemStack stack)
	{
		super(damageTypeIn, damageSourceEntityIn);
		this.stack = stack;
	}

	public ItemStack getStack()
	{
		return this.stack;
	}

	public static EntityDamageSource setDamageByEntity(DamageSource source, Entity entity)
	{
		EntityDamageSource eds = new EntityDamageSource(source.getDamageType(), entity);
		if (source.isDamageAbsolute())
			eds.setDamageIsAbsolute();
		if (source.isUnblockable())
			eds.setDamageBypassesArmor();
		return eds;
	}

	public static EntityDamageSourceWithItem setDamageByEntityWithItem(DamageSource source, Entity entity, ItemStack stack)
	{
		EntityDamageSourceWithItem eds = new EntityDamageSourceWithItem(source.getDamageType(), entity, stack);
		if (source.isDamageAbsolute())
			eds.setDamageIsAbsolute();
		if (source.isUnblockable())
			eds.setDamageBypassesArmor();
		return eds;
	}
}
