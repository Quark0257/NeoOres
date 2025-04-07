package neo_ores.spell.effect;

import java.util.List;

import neo_ores.spell.SpellItemInterfaces.HasChanceLiquid;
import neo_ores.util.SpellUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public abstract class SpellEffectItemFilteredOrFluid extends SpellEffectItemFiltered implements HasChanceLiquid
{
	protected boolean liquidMode = false;

	public void setSupport()
	{
		this.liquidMode = true;
	}

	protected boolean match(Fluid target, ItemStack spell)
	{
		List<ItemStack> whiteList = SpellUtils.getFilteredItems(spell, false);
		List<ItemStack> blackList = SpellUtils.getFilteredItems(spell, true);
		boolean flag = false;
		for (ItemStack white : whiteList)
		{
			if (!SpellUtils.isFluidContainer(white))
			{
				continue;
			}
			flag = true;
			if (SpellUtils.isMatch(target, white))
			{
				return true;
			}
		}
		for (ItemStack black : blackList)
		{
			if (!SpellUtils.isFluidContainer(black))
			{
				continue;
			}
			if (SpellUtils.isMatch(target, black))
			{
				return false;
			}
		}
		if (!flag)
		{
			return true;
		}
		return false;
	}
}
