package neo_ores.spell.correction;

import neo_ores.api.spell.Spell;
import neo_ores.api.spell.Spell.SpellCorrection;
import neo_ores.spell.SpellItemInterfaces.HasTier;

public class SpellTier  extends SpellCorrection
{
	public SpellTier(int level) 
	{
		super(level);
	}
	
	@Override
	public void onCorrection(Spell spell) 
	{
		if(spell instanceof HasTier)
		{
			((HasTier) spell).setTier(this.level);
		}
	}
}
