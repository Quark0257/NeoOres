package neo_ores.core;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@Name("NeoOresCore")
@TransformerExclusions({"neo_ores.core"})
public class NeoOresCore implements IFMLLoadingPlugin
{
	public NeoOresCore() {
		System.out.println("NeoOresCore loaded");
	}

	@Override
	public String[] getASMTransformerClass()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModContainerClass()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSetupClass()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAccessTransformerClass()
	{
		return "neo_ores.core.NeoOresTransformer";
	}

}
