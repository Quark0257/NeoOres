package neo_ores.spell.correction;

import neo_ores.api.spell.Spell;
import neo_ores.api.spell.Spell.SpellCorrection;
import neo_ores.spell.SpellItemInterfaces.HasHarvestLevel;

public class SpellHarvestLevel  extends SpellCorrection
{
	public SpellHarvestLevel(int level) 
	{
		super(level);
	}
	
	@Override
	public void onCorrection(Spell spell) 
	{
		if(spell instanceof HasHarvestLevel)
		{
			((HasHarvestLevel) spell).setHavestLevel(this.level);
		}
	}
}