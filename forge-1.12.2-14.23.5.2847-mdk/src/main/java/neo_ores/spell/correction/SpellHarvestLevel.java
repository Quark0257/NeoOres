package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasHarvestLevel;

public class SpellHarvestLevel extends SpellCorrectionSingle<HasHarvestLevel>
{
	public SpellHarvestLevel(int level)
	{
		super(level);
	}

	@Override
	protected void onApply(HasHarvestLevel spell)
	{
		spell.setHarvestLevel(this.getLevel());
	}
}