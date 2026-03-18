package neo_ores.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class EntityAttackEvent extends EntityEvent
{
	private final Entity source;
	private final float amount;
	private final boolean baseAttackResult;
	
	public EntityAttackEvent(Entity target, Entity source, float amount, boolean baseAttackResult)
	{
		super(target);
		this.source = source;
		this.amount = amount;
		this.baseAttackResult = baseAttackResult;
		this.setCanceled(!baseAttackResult);
	}

	public Entity getSource()
	{
		return source;
	}

	public float getAmount()
	{
		return amount;
	}
	
	public boolean getBaseAttackResult() 
	{
		return this.baseAttackResult;
	}
	
	public static class PlayerAttackEvent extends EntityAttackEvent
	{
		private final EntityPlayer player;

		public PlayerAttackEvent(Entity target, EntityPlayer player, float amount, boolean baseAttackResult)
		{
			super(target, player, amount, baseAttackResult);
			this.player = player;
		}
		
		public EntityPlayer getPlayer() 
		{
			return this.player;
		}
	}
	
	public static class MobAttackEvent extends EntityAttackEvent
	{
		private final EntityMob mob;

		public MobAttackEvent(Entity target, EntityMob mob, float amount, boolean baseAttackResult)
		{
			super(target, mob, amount, baseAttackResult);
			this.mob = mob;
		}
		
		public EntityMob getMob() 
		{
			return this.mob;
		}
	}
	
	public static boolean attackWithItem(Entity entity, EntityPlayer player, float f)
	{
		return !MinecraftForge.EVENT_BUS.post(new PlayerAttackEvent(entity, player, f, entity.attackEntityFrom(DamageSource.causePlayerDamage(player), f)));
	}
	
	public static boolean attackWithMob(Entity entity, EntityMob mob, float f)
	{
		return !MinecraftForge.EVENT_BUS.post(new MobAttackEvent(entity, mob, f, entity.attackEntityFrom(DamageSource.causeMobDamage(mob), f)));
	}
}
