package neo_ores.api.guide;

public class GuideTab
{
	private final String translateKey;
	
	public GuideTab(String key) 
	{
		this.translateKey = key;
	}
	
	public String getKey() 
	{
		return this.translateKey;
	}
}
