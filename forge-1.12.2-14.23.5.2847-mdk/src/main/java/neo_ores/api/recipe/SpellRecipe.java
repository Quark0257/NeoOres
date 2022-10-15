package neo_ores.api.recipe;

import java.util.Arrays;
import java.util.List;

import neo_ores.api.RecipeOreStack;
import neo_ores.api.spell.SpellItem;
import neo_ores.main.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistryEntry;

@ObjectHolder(Reference.MOD_ID)
public class SpellRecipe extends IForgeRegistryEntry.Impl<SpellRecipe>
{
	private final SpellItem result;
	private final List<RecipeOreStack> consumption;
	public SpellRecipe(SpellItem spell,RecipeOreStack... recipe)
	{
		this.result = spell;
		this.consumption = Arrays.asList(recipe);
		
		this.setRegistryName(new ResourceLocation(spell.getModId(), "spellRecipe." + spell.getRegisteringId()));
	}
	
	public SpellItem getSpell()
	{
		return this.result;
	}
	
	public List<RecipeOreStack> getRecipe()
	{
		return this.consumption;
	}
}
