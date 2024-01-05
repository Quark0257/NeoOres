package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasNoGravity;

public class SpellNoGravity extends SpellCorrectionSingle<HasNoGravity>
{

	public SpellNoGravity()
	{
		super(0);
	}

	@Override
	public void onApply(HasNoGravity spell)
	{
		spell.setNoGravity();
	}
}
