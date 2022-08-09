package neo_ores.spell.correction;

import neo_ores.api.spell.Spell;
import neo_ores.api.spell.Spell.SpellCorrection;
import neo_ores.spell.SpellItemInterfaces.HasDamageLevel;

public class SpellDamageLevel  extends SpellCorrection
{
	public SpellDamageLevel(int level) 
	{
		super(level);
	}
	
	@Override
	public void onCorrection(Spell spell) 
	{
		if(spell instanceof HasDamageLevel)
		{
			((HasDamageLevel) spell).setDamageLevel(this.level);
		}
	}
}