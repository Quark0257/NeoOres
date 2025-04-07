package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasDimensionOver;

public class SpellDimensionOver extends SpellCorrectionSingle<HasDimensionOver>
{
	public SpellDimensionOver()
	{
		super(0);
	}

	@Override
	protected void onApply(HasDimensionOver spell)
	{
		spell.setDimensionOver();
	}
}
