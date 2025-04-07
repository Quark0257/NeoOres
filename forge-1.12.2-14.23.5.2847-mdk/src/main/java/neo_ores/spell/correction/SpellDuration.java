package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasDuration;

public class SpellDuration extends SpellCorrectionSingle<HasDuration>
{
	public SpellDuration(int level)
	{
		super(level);
	}

	@Override
	protected void onApply(HasDuration spell)
	{
		spell.setDuration(this.getLevel());
	}
}
