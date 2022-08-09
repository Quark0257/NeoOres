package neo_ores.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerDataStorage
{
	private final EntityPlayerMP player;
	private static final String extension = "";
	
	public PlayerDataStorage(EntityPlayerMP playermp) 
	{ 
		this.player = playermp;
	}

	@SuppressWarnings("static-access")
	public Map<String,Float> writeToMap() 
	{   
		String s = "";
        String[] s_colon;
        Map<String,Float> map = new HashMap<String,Float>();
        
        try 
        {
        	File file = new File(player.world.getSaveHandler().getWorldDirectory().getPath() + "/neo_ores/" + player.getOfflineUUID(player.getName()).toString() + extension);
        	if(!file.exists()) 
        	{
        		File folder = new File(player.world.getSaveHandler().getWorldDirectory().getPath() + "/neo_ores");
        		folder.mkdir();
        		try 
        		{
        			FileWriter writer = new FileWriter(folder.getPath() + "/" + player.getOfflineUUID(player.getName()).toString() + extension);
        			writer.write("");
        			writer.close();
                }
        		catch (IOException ex) 
        		{
                    ex.printStackTrace();
                }
        	}
        	FileReader fileReader = new FileReader(file);
        	BufferedReader bufferedReader = new BufferedReader(fileReader);
        	String data;
        	while ((data = bufferedReader.readLine()) != null) 
        	{
        		s = s + data;
        	}
        	bufferedReader.close();
        } 
        catch (IOException e) 
        {
        	e.printStackTrace();
        }
        
        s_colon = s.split(";");
        
        for(int i = 0;i < s_colon.length;i++)
        {
        	if(s_colon[i].split(":") != null && s_colon[i].split(":").length == 2)
        	{
        		map.put(s_colon[i].split(":")[0],Float.parseFloat(s_colon[i].split(":")[1]));
        	}
        }
        
        return map;
	}

	@SuppressWarnings("static-access")
	public void writeToFile(Map<String,Float> map)
	{
		String s = "";
		for (Map.Entry<String, Float> entry : map.entrySet()) s += (String)entry.getKey() + ":" + entry.getValue() + ";";
		File folder = new File(player.world.getSaveHandler().getWorldDirectory().getPath() + "/neo_ores");
		folder.mkdir();
		try 
		{
			FileWriter writer = new FileWriter(folder.getPath() + "/" + player.getOfflineUUID(player.getName()).toString() + extension);
			writer.write(s);
			writer.close();
        }
		catch (IOException ex) 
		{
            ex.printStackTrace();
        }
	}
	
	public void setValue(String key, float value)
	{
		/*
		Map<String,Float> map = this.writeToMap();
		if(map.containsKey(key))
		{
			map.replace(key, value);
		}
		else
		{
			map.put(key, value);
		}
		
		this.writeToFile(map);
		*/
		if(!this.player.getEntityData().hasKey("neo_ores",10))
		{
			this.player.getEntityData().setTag("neo_ores", new NBTTagCompound());
		}
		this.player.getEntityData().getCompoundTag("neo_ores").setFloat(key, value);
	}
	
	public float getValue(String key) 
	{
		/*
		Map<String,Float> map = this.writeToMap();
		if(map.containsKey(key) && map.get(key) != null)
		{
			return map.get(key);
		}
		else
		{
			return 0;
		}
		*/

		if(!this.player.getEntityData().hasKey("neo_ores",10))
		{
			this.player.getEntityData().setTag("neo_ores", new NBTTagCompound());
		}
		return this.player.getEntityData().getCompoundTag("neo_ores").getFloat(key);
	}
	
	public void setBoolean(String key, boolean bool)
	{
		if(bool) 
		{
			this.setValue(key, 1);
		}
		else
		{
			this.setValue(key, 0);
		}
	}
	
	public boolean getBoolean(String key)
	{
		if(this.getValue(key) >= 1)
		{
			return true;
		}
		
		return false;
	}
}
