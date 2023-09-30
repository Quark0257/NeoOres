package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasSilk;

public class SpellSilk extends SpellCorrectionSingle<HasSilk>
{
	public SpellSilk() 
	{
		super(0);
	}

	@Override
	protected void onApply(HasSilk spell) {
		spell.setSilkTouch();
	}
}
