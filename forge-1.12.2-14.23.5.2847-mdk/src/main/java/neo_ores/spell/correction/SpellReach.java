package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasReach;

public class SpellReach extends SpellCorrectionSingle<HasReach>
{
	public SpellReach(int level)
	{
		super(level);
	}

	@Override
	protected void onApply(HasReach spell)
	{
		spell.setReach(this.getLevel());
	}
}
