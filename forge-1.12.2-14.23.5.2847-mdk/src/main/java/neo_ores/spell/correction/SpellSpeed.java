package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasSpeed;

public class SpellSpeed extends SpellCorrectionSingle<HasSpeed>
{
	public SpellSpeed(int level) 
	{
		super(level);
	}

	@Override
	public void onApply(HasSpeed spell) 
	{
		spell.setSpeed(level);
	}
}
