package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasContinuation;

public class SpellContinuation extends SpellCorrectionSingle<HasContinuation>
{

	public SpellContinuation(int level)
	{
		super(level);
	}

	@Override
	public void onApply(HasContinuation spell)
	{
		spell.setContinuation(level);
	}

}
