package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasOffsetUp;

public class SpellOffsetUp extends SpellCorrectionSingle<HasOffsetUp>
{
	public SpellOffsetUp()
	{
		super(0);
	}

	@Override
	protected void onApply(HasOffsetUp spell)
	{
		spell.setOffsetUp();
	}
}
