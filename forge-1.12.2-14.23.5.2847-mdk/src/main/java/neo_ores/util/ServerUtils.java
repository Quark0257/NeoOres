package neo_ores.util;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import neo_ores.main.NeoOresData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.CombatEntry;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ServerUtils
{
	private static final String[] combatEntries = new String[] { "field_94556_a", "combatEntries" };

	public static void sendSoundToClient(World world, double x, double y, double z, SoundEvent event, SoundCategory category, float volume, float pitch)
	{
		for (EntityPlayer player : world.playerEntities)
		{
			if (player instanceof EntityPlayerMP)
			{
				((EntityPlayerMP) player).connection.sendPacket(new SPacketCustomSound(event.getRegistryName().toString(), category, x, y, z, volume, pitch));
			}
		}
	}

	public static boolean damageEntity(Entity entity, DamageSource source, float amount)
	{
		if (entity instanceof IEntityMultiPart)
		{
			return ((IEntityMultiPart) entity).attackEntityFromPart(null, source, amount);
		}
		if (entity instanceof MultiPartEntityPart)
		{
			IEntityMultiPart parent = ((MultiPartEntityPart) entity).parent;
			return parent.attackEntityFromPart((MultiPartEntityPart) entity, source, amount);
		}
		return entity.attackEntityFrom(source, amount);
	}

	public static UUID getUUID(Entity entity)
	{
		if (entity instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) entity;
			return EntityPlayer.getUUID(player.getGameProfile());
		}
		return entity.getUniqueID();
	}

	@Nullable
	public static Entity getEntity(UUID uuid, WorldServer server)
	{
		Entity result = server.getPlayerEntityByUUID(uuid);
		if (result == null)
		{
			result = server.getEntityFromUuid(uuid);
		}

		return result;
	}

	public static void resetEntityTarget(Entity entity)
	{
		World worldIn = entity.world;
		if (worldIn.isRemote)
		{
			return;
		}
		NBTTagCompound tag = entity.getEntityData();
		if (tag.hasKey("ownerUUID"))
		{
			UUID uuid = UUID.fromString(tag.getString("ownerUUID"));
			boolean isOwnerPlayer = tag.getBoolean("ownerPlayer");
			WorldServer server = (WorldServer) worldIn;
			Entity owner = ServerUtils.getEntity(uuid, server);
			if (owner != null && entity instanceof EntityLiving)
			{
				EntityLiving living = (EntityLiving) entity;
				EntityLivingBase target = living.getAttackTarget();
				EntityLivingBase newTarget = null;

				if (newTarget == null)
				{
					CombatTracker entityCombat = living.getCombatTracker();
					List<CombatEntry> entries = ServerUtils.<List<CombatEntry>, CombatTracker>getPrivateValue(CombatTracker.class, entityCombat, combatEntries);
					if (entries != null)
					{
						newTarget = ServerUtils.getAlivedBestAttackerExcluded(entries, owner);
					}

					if (newTarget == null && owner instanceof EntityLivingBase)
					{
						CombatTracker ownerCombat = ((EntityLivingBase) owner).getCombatTracker();
						List<CombatEntry> ownerEntries = ServerUtils.<List<CombatEntry>, CombatTracker>getPrivateValue(CombatTracker.class, ownerCombat, combatEntries);
						if (ownerEntries != null)
						{
							newTarget = ServerUtils.getAlivedBestAttackerExcluded(ownerEntries, owner);
						}
					}

					if (newTarget == null)
					{
						if (owner instanceof EntityLiving)
						{
							newTarget = ((EntityLiving) owner).getAttackTarget();
						}
						else if (owner instanceof EntityPlayerMP && NeoOresData.instance != null)
						{
							newTarget = NeoOresData.instance.getPSD((EntityPlayerMP) owner).getPrevAttackedEntity(server);
						}
					}
				}

				EntityLivingBase finalTarget = newTarget;
				if (newTarget != null)
				{
					if (!newTarget.isEntityAlive())
					{
						finalTarget = null;
					}

					if (owner.equals(newTarget) || living.equals(newTarget))
					{
						finalTarget = null;
					}

					if (owner instanceof EntityPlayer && newTarget instanceof EntityPlayer)
					{
						if (!((EntityPlayer) owner).canAttackPlayer((EntityPlayer) newTarget))
						{
							finalTarget = null;
						}
					}
				}

				if (!compareEntity(target, finalTarget))
				{
					living.setAttackTarget(finalTarget);
				}

				if (owner.onGround && owner.getPositionVector().subtract(living.getPositionVector()).lengthVector() > 32.0)
				{
					living.setPositionAndUpdate(owner.posX, owner.posY, owner.posZ);
				}
			}
			else if (entity instanceof EntityLiving && isOwnerPlayer)
			{
				EntityLiving living = (EntityLiving) entity;
				EntityLivingBase target = null;
				
				CombatTracker entityCombat = living.getCombatTracker();
				List<CombatEntry> entries = ServerUtils.<List<CombatEntry>, CombatTracker>getPrivateValue(CombatTracker.class, entityCombat, combatEntries);
				if (entries != null)
				{
					target = ServerUtils.getAlivedBestAttacker(entries);
				}
				
				if (target == null && NeoOresData.instance != null)
				{
					target = NeoOresData.instance.getPSD(uuid).getPrevAttackedEntity(server);
				}
				
				if (target != null) 
				{
					if (!target.isEntityAlive()) 
					{
						target = null;
					}
				}

				if (!compareEntity(target, living.getAttackTarget()))
				{
					((EntityLiving) entity).setAttackTarget(target);
				}
			}
		}
	}

	public static boolean compareEntity(@Nullable Entity e1, @Nullable Entity e2)
	{
		return (e1 == null && e2 == null) || (e1 != null && e2 != null && e1.equals(e2));
	}

	public static <T, E> void setPrivateValue(Class<? super T> classToAccess, T instance, E value, String... fieldNames)
	{
		for (String field : fieldNames)
		{
			if (ObfuscationReflectionHelper.findField(classToAccess, field) != null)
			{
				ObfuscationReflectionHelper.setPrivateValue(classToAccess, instance, value, field);
			}
		}
	}

	@Nullable
	public static <T, E> T getPrivateValue(Class<? super E> classToAccess, E instance, String... fieldNames)
	{
		for (String field : fieldNames)
		{
			if (ObfuscationReflectionHelper.findField(classToAccess, field) != null)
			{
				return ObfuscationReflectionHelper.getPrivateValue(classToAccess, instance, field);
			}
		}
		return null;
	}

	@Nullable
	public static EntityLivingBase getAlivedBestAttacker(List<CombatEntry> entries)
	{
		return getAlivedBestAttackerExcluded(entries, null);
	}
	
	@Nullable
	public static EntityLivingBase getAlivedBestAttackerExcluded(List<CombatEntry> entries, Entity entity)
	{
		EntityLivingBase entitylivingbase = null;
		EntityPlayer entityplayer = null;
		float f = 0.0F;
		float f1 = 0.0F;

		for (CombatEntry combatentry : entries)
		{
			if (combatentry.getDamageSrc().getTrueSource() instanceof EntityPlayer && (entityplayer == null || combatentry.getDamage() > f1)
					&& combatentry.getDamageSrc().getTrueSource().isEntityAlive() && !compareEntity(combatentry.getDamageSrc().getTrueSource(), entity))
			{
				f1 = combatentry.getDamage();
				entityplayer = (EntityPlayer) combatentry.getDamageSrc().getTrueSource();
			}

			if (combatentry.getDamageSrc().getTrueSource() instanceof EntityLivingBase && (entitylivingbase == null || combatentry.getDamage() > f)
					&& combatentry.getDamageSrc().getTrueSource().isEntityAlive() && !compareEntity(combatentry.getDamageSrc().getTrueSource(), entity))
			{
				f = combatentry.getDamage();
				entitylivingbase = (EntityLivingBase) combatentry.getDamageSrc().getTrueSource();
			}
		}

		if (entityplayer != null && f1 >= f / 3.0F)
		{
			return entityplayer;
		}
		else
		{
			return entitylivingbase;
		}
	}
}
