package neo_ores.api.spell;

public class BasicData
{
	final String modid;
	final String registerID;
	final int tier;
	final SpellItemType type;
	long costsum;
	float costproduct;

	/*
	 * Texture size is 64*64
	 */
	public BasicData(String modid, String registerID, int tier, SpellItemType type, long costsum, float costproduct)
	{
		this.modid = modid;
		this.registerID = registerID;
		this.tier = tier;
		this.type = type;
		this.costsum = costsum;
		this.costproduct = costproduct;
	}
}
