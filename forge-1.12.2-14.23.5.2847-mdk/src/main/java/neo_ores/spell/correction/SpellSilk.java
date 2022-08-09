package neo_ores.spell.correction;

import neo_ores.api.spell.Spell;
import neo_ores.api.spell.Spell.SpellCorrection;
import neo_ores.spell.SpellItemInterfaces.HasSilk;

public class SpellSilk extends SpellCorrection
{
	public SpellSilk() 
	{
		super(0);
	}
	
	@Override
	public void onCorrection(Spell spell) 
	{
		if(spell instanceof HasSilk)
		{
			((HasSilk) spell).setSilkTouch();
		}
	}
}
