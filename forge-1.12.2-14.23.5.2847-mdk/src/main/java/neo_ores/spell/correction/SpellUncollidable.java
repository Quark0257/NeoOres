package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasUncollidable;

public class SpellUncollidable extends SpellCorrectionSingle<HasUncollidable>
{

	public SpellUncollidable()
	{
		super(0);
	}

	@Override
	protected void onApply(HasUncollidable spell)
	{
		spell.setUncollidable();
	}

}
