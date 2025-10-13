package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasPositive;

public class SpellPositive extends SpellCorrectionSingle<HasPositive>
{
	public SpellPositive()
	{
		super(0);
	}

	@Override
	protected void onApply(HasPositive spell)
	{
		spell.setPositive();
	}
}
