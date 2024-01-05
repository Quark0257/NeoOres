package neo_ores.item;

import java.util.List;

import neo_ores.api.spell.SpellItem;
import net.minecraft.item.ItemStack;

public interface ISpellWritable
{
	public ItemStack writeActiveSpells(List<SpellItem> list, ItemStack stack);
}
