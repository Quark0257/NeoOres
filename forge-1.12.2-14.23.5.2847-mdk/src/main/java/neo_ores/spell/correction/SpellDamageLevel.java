package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasDamageLevel;

public class SpellDamageLevel extends SpellCorrectionSingle<HasDamageLevel>
{
	public SpellDamageLevel(int level)
	{
		super(level);
	}

	@Override
	protected void onApply(HasDamageLevel spell)
	{
		spell.setDamageLevel(this.getLevel());
	}
}