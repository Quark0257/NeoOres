package neo_ores.api.recipe;

import java.util.List;

import neo_ores.api.ItemStackWithSizeForRecipe;
import neo_ores.api.spell.SpellItem;
import neo_ores.main.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistryEntry;
import scala.actors.threadpool.Arrays;

@ObjectHolder(Reference.MOD_ID)
public class SpellRecipe extends IForgeRegistryEntry.Impl<SpellRecipe>
{
	private final SpellItem result;
	private final List<ItemStackWithSizeForRecipe> consumption;
	@SuppressWarnings("unchecked")
	public SpellRecipe(SpellItem spell,ItemStackWithSizeForRecipe... recipe)
	{
		this.result = spell;
		this.consumption = Arrays.asList(recipe);
		
		this.setRegistryName(new ResourceLocation(spell.getModId(), "spellRecipe." + spell.getRegisteringId()));
	}
	
	public SpellItem getSpell()
	{
		return this.result;
	}
	
	public List<ItemStackWithSizeForRecipe> getRecipe()
	{
		return this.consumption;
	}
}
