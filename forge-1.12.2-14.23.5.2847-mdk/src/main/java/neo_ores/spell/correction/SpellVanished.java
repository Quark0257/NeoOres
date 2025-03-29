package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasVanished;

// when spell bullet life ended
public class SpellVanished extends SpellCorrectionSingle<HasVanished>
{
	public SpellVanished()
	{
		super(0);
	}

	@Override
	protected void onApply(HasVanished spell)
	{
		spell.setVanished();
	}

}