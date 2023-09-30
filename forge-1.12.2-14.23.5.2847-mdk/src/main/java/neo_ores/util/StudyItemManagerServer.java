package neo_ores.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import neo_ores.data.PlayerDataStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class StudyItemManagerServer 
{	
	private final PlayerDataStorage pds;
	private final EntityPlayerMP player;
	
	public StudyItemManagerServer(EntityPlayerMP player)
	{
		this.pds = new PlayerDataStorage(player);
		this.player = player;
	}
	
	public boolean didGet(String modid,String id)
	{
		return pds.getBoolean(modid + "@" + id);
	}
	
	public boolean canGet(String pmodid,String parent,String modid,String id)
	{
		if(parent.equals(id) && pmodid.equals(modid)) 
		{
			return !didGet(modid,id);
		}
		else if(didGet(pmodid,parent))
		{
			return !didGet(modid,id);
		}
		else
		{
			return false;
		}
	}
	
	public boolean canGetRoot(String modid,String id)
	{
		return this.canGet(modid,id,modid,id);
	}
	
	public void set(String id,String i)
	{
		pds.setBoolean(id + "@" + i, true);
		PlayerManaDataServer pmds = new PlayerManaDataServer(this.player);
		pmds.sendToClient();
	}
	
	public void remove(String id,String i)
	{
		pds.setBoolean(id + "@" + i, false);
		PlayerManaDataServer pmds = new PlayerManaDataServer(this.player);
		pmds.sendToClient();
	}
	
	public NBTTagCompound alreadyGottenList()
	{
		NBTTagCompound studyData = new NBTTagCompound();
		for(Map.Entry<String, List<String>> entry : SpellUtils.getAll().entrySet())
		{
			List<String> ids = new ArrayList<String>();
			for(String id : entry.getValue())
			{
				if(didGet(entry.getKey(),id))
				{
					ids.add(id);
				}
			}
			
			if(!ids.isEmpty())
			{
				studyData.setTag(entry.getKey(), new NBTTagList());
				for(String id : ids)
				{
					studyData.getTagList(entry.getKey(), 8).appendTag(new NBTTagString(id));
				}
			}
		}
		
		return studyData;
	}
}
