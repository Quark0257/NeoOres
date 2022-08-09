package neo_ores.spell.correction;

import neo_ores.api.spell.Spell;
import neo_ores.api.spell.Spell.SpellCorrection;
import neo_ores.spell.SpellItemInterfaces.HasChanceLiquid;

public class SpellSupportLiquid extends SpellCorrection
{
	public SpellSupportLiquid() 
	{
		super(0);
	}

	@Override
	public void onCorrection(Spell spell) 
	{
		if(spell instanceof HasChanceLiquid)
		{
			((HasChanceLiquid) spell).setSupport();
		}
	}
}
