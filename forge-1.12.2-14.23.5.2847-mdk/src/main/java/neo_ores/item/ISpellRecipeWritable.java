package neo_ores.item;

import java.util.List;

import neo_ores.api.spell.SpellItem;
import net.minecraft.item.ItemStack;

public interface ISpellRecipeWritable
{
	public void writeRecipeSpells(List<SpellItem> list,ItemStack stack);
	
	public List<SpellItem> readRecipeSpells(ItemStack stack);
	
	public boolean hasRecipe(ItemStack stack);
}
