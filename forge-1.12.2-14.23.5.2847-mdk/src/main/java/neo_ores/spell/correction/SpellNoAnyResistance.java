package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasNoAnyResistance;

public class SpellNoAnyResistance  extends SpellCorrectionSingle<HasNoAnyResistance>
{
	public SpellNoAnyResistance() 
	{
		super(0);
	}

	@Override
	public void onApply(HasNoAnyResistance spell) 
	{
		spell.setNoAnyResistance();
	}
}
