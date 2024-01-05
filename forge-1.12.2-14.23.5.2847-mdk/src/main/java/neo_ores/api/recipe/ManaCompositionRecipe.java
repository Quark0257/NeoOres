package neo_ores.api.recipe;

import java.util.List;

import javax.annotation.Nonnull;

import neo_ores.api.RecipeOreStack;
import neo_ores.main.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistryEntry;

@ObjectHolder(Reference.MOD_ID)
public class ManaCompositionRecipe extends IForgeRegistryEntry.Impl<ManaCompositionRecipe>
{
	public List<RecipeOreStack> recipe;
	public int tier;
	public ItemStack result;

	public ManaCompositionRecipe(int tier, @Nonnull ItemStack result, List<RecipeOreStack> objects)
	{
		this.tier = tier;
		this.result = result;
		this.recipe = objects;

	}

	public ManaCompositionRecipe setRegistryKey(String modid, String key)
	{
		return this.setRegistryName(new ResourceLocation(modid, "manaCompositionRecipe." + key));
	}

	public List<RecipeOreStack> getRecipe()
	{
		return this.recipe;
	}

	public ItemStack getResult()
	{
		return this.result;
	}

	public int getTier()
	{
		return this.tier;
	}
}
