package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasDominant;

public class SpellDominant extends SpellCorrectionSingle<HasDominant>
{
	public SpellDominant()
	{
		super(0);
	}

	@Override
	protected void onApply(HasDominant spell)
	{
		spell.setDominant();
	}
}
