package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasNoInertia;

public class SpellNoInertia extends SpellCorrectionSingle<HasNoInertia>
{
	public SpellNoInertia()
	{
		super(0);
	}

	@Override
	public void onApply(HasNoInertia spell)
	{
		spell.setNoInertia();
	}
}
