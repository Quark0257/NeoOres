package neo_ores.entity.ai;

import neo_ores.util.SpellData;

public interface ISpellingSelf extends ISpelling
{	
	public boolean allowSelf();
	
	public SpellData getSelfSpell();
}
