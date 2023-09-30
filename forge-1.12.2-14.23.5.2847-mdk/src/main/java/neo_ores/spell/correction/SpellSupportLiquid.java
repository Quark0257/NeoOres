package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasChanceLiquid;

public class SpellSupportLiquid extends SpellCorrectionSingle<HasChanceLiquid>
{
	public SpellSupportLiquid() 
	{
		super(0);
	}

	@Override
	protected void onApply(HasChanceLiquid spell) {
		spell.setSupport();
	}
}
