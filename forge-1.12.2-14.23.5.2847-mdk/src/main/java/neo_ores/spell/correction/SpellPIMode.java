package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasPI;

public class SpellPIMode extends SpellCorrectionSingle<HasPI>
{

	public SpellPIMode()
	{
		super(0);
	}

	@Override
	protected void onApply(HasPI spell)
	{
		spell.setPIMode();
	}

}
