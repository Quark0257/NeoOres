package neo_ores.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.minecraftforge.fml.common.FMLLog;

public class CoreConfigManager
{
	private static CoreConfigManager instance = null;
	private final CoreConfig config;
	
	private CoreConfigManager() 
	{
		this.config = load();
	}
	
	public CoreConfig getConfig() 
	{
		return this.config;
	}
	
	public static CoreConfigManager getInstance() 
	{
		if (instance == null) 
		{
			instance = new CoreConfigManager();
		}
		return instance;
	}
	
	public CoreConfig load() 
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			File configDir = new File("config");
			if (!configDir.isDirectory()) 
			{
				configDir.mkdir();
			}
			File config = new File("config/neo_ores_core.json");
			boolean exist = !config.createNewFile();
			if (exist) 
			{
				if (this.config != null) 
				{
					return this.config;
				}
				FMLLog.log.info("[NeoOresCore] CoreConfig {} will be loaded.", config.getAbsolutePath());
				JsonReader jsonReader = new JsonReader(new BufferedReader(new FileReader(config)));
				CoreConfig loadedConfig = gson.fromJson(jsonReader, CoreConfig.class);
				jsonReader.close();
				return loadedConfig;
			}
			else 
			{
				FMLLog.log.info("[NeoOresCore] New CoreConfig {} will be created.", config.getAbsolutePath());
				CoreConfig defaultConfig = new CoreConfig();
				JsonWriter writer = new JsonWriter(new BufferedWriter(new FileWriter(config)));
				writer.setIndent("  ");
				gson.toJson(gson.toJsonTree(defaultConfig), writer);
				writer.close();
				return defaultConfig;
			}
		}
		catch (IOException exception) 
		{
			FMLLog.log.warn("[NeoOresCore] CoreConfig can't be loaded. The default settings will apply to the config.");
			return new CoreConfig();
		}
	}
}
