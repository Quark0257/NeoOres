package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasOffsetDown;

public class SpellOffsetDown extends SpellCorrectionSingle<HasOffsetDown>
{
	public SpellOffsetDown()
	{
		super(0);
	}

	@Override
	protected void onApply(HasOffsetDown spell)
	{
		spell.setOffsetDown();
	}
}
