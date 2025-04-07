package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasAmplify;

public class SpellAmplify extends SpellCorrectionSingle<HasAmplify>
{
	public SpellAmplify(int level)
	{
		super(level);
	}

	@Override
	protected void onApply(HasAmplify spell)
	{
		spell.setAmplify(this.getLevel());
	}
}
