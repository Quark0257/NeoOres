package neo_ores.spell.correction;

import neo_ores.api.spell.Spell.SpellCorrectionSingle;
import neo_ores.spell.SpellItemInterfaces.HasCollidableFilter;

public class SpellCollidableFilter extends SpellCorrectionSingle<HasCollidableFilter> {

	public SpellCollidableFilter() {
		super(0);
	}

	@Override
	protected void onApply(HasCollidableFilter spell) {
		spell.setCollidableFilter();
	}

}
