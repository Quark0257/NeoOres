package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasTier;

public class SpellTier extends SpellCorrectionSingle<HasTier>
{
	public SpellTier(int level)
	{
		super(level);
	}

	@Override
	protected void onApply(HasTier spell)
	{
		spell.setTier(this.getLevel());
	}
}
