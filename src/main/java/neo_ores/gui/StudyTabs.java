package neo_ores.gui;

import net.minecraft.util.ResourceLocation;

public class StudyTabs 
{
	private final String key;
	private final ResourceLocation icon;
	
	public StudyTabs(String key,ResourceLocation icon)
	{
		this.key = key;
		this.icon = icon;
	}
	
	public String getKey()
	{
		return this.key;
	}
	
	public ResourceLocation getIcon()
	{
		return this.icon;
	}
}
