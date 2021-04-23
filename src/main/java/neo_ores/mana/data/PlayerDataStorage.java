package neo_ores.mana.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerDataStorage implements IEntityDataHandler
{
	private final EntityPlayerMP player;
	
	public PlayerDataStorage(EntityPlayerMP playermp) 
	{ 
		this.player = playermp;
	}

	@SuppressWarnings("static-access")
	@Override
	public Map<String,Float> writeToMap() 
	{   
		String s = "";
        String[] s_colon;
        Map<String,Float> map = new HashMap<String,Float>();
        
        try 
        {
        	File file = new File(player.world.getSaveHandler().getWorldDirectory().getPath() + "/neo_ores/" + player.getOfflineUUID(player.getName()).toString() + ".npdata");
        	if(!file.exists()) 
        	{
        		File folder = new File(player.world.getSaveHandler().getWorldDirectory().getPath() + "/neo_ores");
        		folder.mkdir();
        		try 
        		{
        			FileWriter writer = new FileWriter(folder.getPath() + "/" + player.getOfflineUUID(player.getName()).toString() + ".npdata");
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
	@Override
	public void writeToFile(Map<String,Float> map)
	{
		String s = "";
		for (Map.Entry<String, Float> entry : map.entrySet()) s += (String)entry.getKey() + ":" + entry.getValue() + ";";
		File folder = new File(player.world.getSaveHandler().getWorldDirectory().getPath() + "/neo_ores");
		folder.mkdir();
		try 
		{
			FileWriter writer = new FileWriter(folder.getPath() + "/" + player.getOfflineUUID(player.getName()).toString() + ".npdata");
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
	}
	
	public float getValue(String key) 
	{
		Map<String,Float> map = this.writeToMap();
		if(map.containsKey(key) && map.get(key) != null)
		{
			return map.get(key);
		}
		else
		{
			return 0;
		}
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
