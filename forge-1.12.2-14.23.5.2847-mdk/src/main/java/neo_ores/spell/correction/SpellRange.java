package neo_ores.spell.correction;

import neo_ores.api.spell.Spell;
import neo_ores.api.spell.Spell.SpellCorrection;
import neo_ores.spell.SpellItemInterfaces.HasRange;

public class SpellRange  extends SpellCorrection
{
	public SpellRange(int level) 
	{
		super(level);
	}
	
	@Override
	public void onCorrection(Spell spell) 
	{
		if(spell instanceof HasRange)
		{
			((HasRange) spell).setRange(this.level);
		}
	}
}
