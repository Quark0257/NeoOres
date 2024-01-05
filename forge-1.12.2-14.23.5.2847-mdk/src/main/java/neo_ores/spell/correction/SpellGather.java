package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasGather;

public class SpellGather extends SpellCorrectionSingle<HasGather>
{
	public SpellGather()
	{
		super(0);
	}

	@Override
	public void onApply(HasGather spell)
	{
		spell.setCanGather();
	}
}
