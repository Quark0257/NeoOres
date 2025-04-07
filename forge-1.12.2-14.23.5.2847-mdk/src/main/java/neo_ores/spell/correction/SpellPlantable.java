package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasPlantable;

public class SpellPlantable extends SpellCorrectionSingle<HasPlantable>
{
	public SpellPlantable()
	{
		super(0);
	}

	@Override
	protected void onApply(HasPlantable spell)
	{
		spell.setPlantable();
	}
}
