package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasNegative;

public class SpellNegative extends SpellCorrectionSingle<HasNegative>
{
	public SpellNegative()
	{
		super(0);
	}

	@Override
	protected void onApply(HasNegative spell)
	{
		spell.setNegative();
	}
}
