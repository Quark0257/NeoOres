package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasChain;

public class SpellChain extends SpellCorrectionSingle<HasChain>
{
	public SpellChain(int level)
	{
		super(level);
	}

	@Override
	protected void onApply(HasChain spell)
	{
		spell.setChain(this.getLevel());
	}
}
