package neo_ores.entity.ai;

import neo_ores.util.SpellData;

public interface ISpellingSpread extends ISpelling
{
	public boolean allowSpread();
	
	public enum EnumTargetType 
	{
		ENTITIES,
		BLOCKS
	}
	
	public SpellData getSpreadSpell();
}
