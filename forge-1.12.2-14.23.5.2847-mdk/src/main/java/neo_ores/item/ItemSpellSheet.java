package neo_ores.item;

import java.util.List;

import neo_ores.api.spell.SpellItem;
import neo_ores.main.NeoOresItems;
import net.minecraft.item.ItemStack;

public class ItemSpellSheet extends INeoOresItem.Impl implements ISpellWritable
{
	@Override
	public ItemStack writeActiveSpells(List<SpellItem> list, ItemStack stack)
	{
		ItemStack stack1 = new ItemStack(NeoOresItems.spell);
		ISpellWritable.writeNBT(list, stack1);
		return stack1;
	}
}
