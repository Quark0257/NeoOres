package neo_ores.core;

import java.util.Map;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@Name("NeoOresCore")
@TransformerExclusions({"neo_ores.core", "com.google.gson"})
public class NeoOresCore implements IFMLLoadingPlugin
{
	public NeoOresCore() 
	{
		FMLLog.log.info("NeoOresCore loaded");
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return null;
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
	}

	@Override
	public String getAccessTransformerClass()
	{
		return "neo_ores.core.NeoOresTransformer";
	}

}
