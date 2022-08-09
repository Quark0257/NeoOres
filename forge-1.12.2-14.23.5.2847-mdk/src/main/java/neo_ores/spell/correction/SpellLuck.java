package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasLuck;

public class SpellLuck  extends SpellCorrectionSingle<HasLuck>
{
	public SpellLuck(int level) 
	{
		super(level);
	}

	@Override
	public void onApply(HasLuck spell) 
	{
		spell.setLuck(level);
	}
}