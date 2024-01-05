package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasRange;

public class SpellRange extends SpellCorrectionSingle<HasRange>
{
	public SpellRange(int level)
	{
		super(level);
	}

	@Override
	protected void onApply(HasRange spell)
	{
		spell.setRange(this.getLevel());
	}
}
