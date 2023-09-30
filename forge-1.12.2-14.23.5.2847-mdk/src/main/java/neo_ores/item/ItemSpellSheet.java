package neo_ores.item;

import java.util.List;

import neo_ores.api.spell.SpellItem;
import neo_ores.main.NeoOresItems;
import neo_ores.util.SpellUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemSpellSheet extends INeoOresItem.Impl implements ISpellWritable
{
	@Override
	public ItemStack writeActiveSpells(List<SpellItem> list,ItemStack stack) 
	{
		ItemStack stack1 = new ItemStack(NeoOresItems.spell);
		stack1.setTagCompound(new NBTTagCompound());
		stack1.getTagCompound().setTag("activeSpells", SpellUtils.getNBTFromList(list));
		stack1.getTagCompound().setInteger("metadata", SpellUtils.getSpellMetadata(list));
		stack1.getTagCompound().setInteger("color", SpellUtils.getSpellColor(list));
		return stack1;
	}	
}
