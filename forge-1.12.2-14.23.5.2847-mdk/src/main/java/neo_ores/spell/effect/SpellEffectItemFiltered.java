package neo_ores.spell.effect;

import java.util.List;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.spell.SpellItemInterfaces.HasChain;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.SpellUtils;
import net.minecraft.item.ItemStack;

public abstract class SpellEffectItemFiltered extends SpellEffect implements HasRange, HasChain
{
	protected int range = 0;
	protected int chain = 0;
	protected boolean rangeMode = true;

	protected boolean match(ItemStack target, ItemStack spell)
	{
		List<ItemStack> whiteList = SpellUtils.getFilteredItems(spell, false);
		List<ItemStack> blackList = SpellUtils.getFilteredItems(spell, true);
		for (ItemStack white : whiteList)
		{
			if (SpellUtils.isMatch(target, white))
			{
				return true;
			}
		}
		for (ItemStack black : blackList)
		{
			if (SpellUtils.isMatch(target, black))
			{
				return false;
			}
		}
		if (whiteList.isEmpty())
		{
			return true;
		}
		return false;
	}

	@Override
	public void setRange(int value)
	{
		this.range = value;
		this.rangeMode = true;
	}
	
	@Override
	public void setChain(int level)
	{
		this.rangeMode = false;
		this.chain = level;
	}
}
