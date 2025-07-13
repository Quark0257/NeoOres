package neo_ores.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.WorldServer;

public class PlayerStatusData
{
	private boolean login;
	private boolean hasInitialItems;
	private boolean isDirty;
	private boolean isSneak;
	private List<UUID> tamedEntities;
	private UUID prevAttackedEntity;

	public PlayerStatusData()
	{
		this.login = false;
		this.hasInitialItems = false;
		this.isDirty = false;
		this.isSneak = false;
		this.tamedEntities = new ArrayList<UUID>();
		this.prevAttackedEntity = null;
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		this.login = nbt.getBoolean("loggedIn");
		this.hasInitialItems = nbt.getBoolean("hasInitialItems");
		this.isSneak = nbt.getBoolean("isSneak");
		this.prevAttackedEntity = nbt.getUniqueId("prevAttackedEntity");

		this.tamedEntities.clear();
		NBTTagList list = nbt.getTagList("tamedEntities", 8);
		for (int i = 0; i < list.tagCount(); i++)
		{
			this.tamedEntities.add(UUID.fromString(list.getStringTagAt(i)));
		}
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setBoolean("loggedIn", this.login);
		nbt.setBoolean("hasInitialItems", this.hasInitialItems);
		nbt.setBoolean("isSneak", this.isSneak);
		
		if (this.prevAttackedEntity != null) 
		{
			nbt.setUniqueId("prevAttackedEntity", this.prevAttackedEntity);
		}
		
		NBTTagList list = new NBTTagList();
		for (UUID uuid : this.tamedEntities)
		{
			list.appendTag(new NBTTagString(uuid.toString()));
		}
		nbt.setTag("tamedEntities", list);
	}

	public boolean isLoggedIn()
	{
		return this.login;
	}

	public boolean hasInitialItems()
	{
		return this.hasInitialItems;
	}

	public void setLoggedIn(boolean value)
	{
		this.login = value;
		this.isDirty = true;
	}

	public void setInitialItems(boolean value)
	{
		this.hasInitialItems = value;
		this.isDirty = true;
	}

	public boolean isSneak()
	{
		return this.isSneak;
	}

	public void setSneak(boolean value)
	{
		this.isSneak = value;
		this.isDirty = true;
	}

	public void markDirty()
	{
		this.isDirty = true;
	}

	public boolean isDirty()
	{
		return this.isDirty;
	}

	public void addEntity(Entity value)
	{
		if (value instanceof EntityPlayer)
		{
			return;
		}
		this.tamedEntities.add(value.getUniqueID());
		this.isDirty = true;
	}

	public void removeEntity(Entity value)
	{
		if (this.tamedEntities.contains(value.getUniqueID()))
		{
			this.tamedEntities.remove(value.getUniqueID());
		}
		this.isDirty = true;
	}

	public void removeEntities()
	{
		this.tamedEntities.clear();
		this.isDirty = true;
	}

	public List<Entity> getEntities(WorldServer world)
	{
		List<Entity> entities = new ArrayList<Entity>();
		for (UUID uuid : this.tamedEntities)
		{
			entities.add(world.getEntityFromUuid(uuid));
		}
		return entities;
	}
	
	public void setAttackingEntity(EntityLivingBase entity) 
	{
		this.prevAttackedEntity = ServerUtils.getUUID(entity);
		this.isDirty = true;
	}
	
	@Nullable
	public EntityLivingBase getPrevAttackedEntity(WorldServer server) 
	{
		if (this.prevAttackedEntity == null) 
		{
			return null;
		}
		Entity entity = ServerUtils.getEntity(this.prevAttackedEntity, server);
		if (entity instanceof EntityLivingBase) 
		{
			return (EntityLivingBase) entity;
		}
		return null;
	}
}
