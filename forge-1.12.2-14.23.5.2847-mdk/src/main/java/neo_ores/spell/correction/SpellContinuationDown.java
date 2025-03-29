package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasContinuationDown;

public class SpellContinuationDown extends SpellCorrectionSingle<HasContinuationDown>
{
	public SpellContinuationDown(int level)
	{
		super(level);
	}

	@Override
	protected void onApply(HasContinuationDown spell)
	{
		spell.setContinuationDown(level);
	}
}
