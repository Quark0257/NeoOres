package neo_ores.study;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import neo_ores.mana.data.PlayerDataStorage;
import neo_ores.spell.SpellUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class StudyItemManagerServer 
{	
	public final PlayerDataStorage pds;
	
	public StudyItemManagerServer(EntityPlayerMP player)
	{
		this.pds = new PlayerDataStorage(player);
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
	}
	
	public void remove(String id,String i)
	{
		pds.setBoolean(id + "@" + i, false);
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
