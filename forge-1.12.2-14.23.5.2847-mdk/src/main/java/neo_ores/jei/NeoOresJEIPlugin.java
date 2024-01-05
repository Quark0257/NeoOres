package neo_ores.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import neo_ores.client.gui.GuiManaWorkbench;
import neo_ores.inventory.ContainerManaWorkbench;
import neo_ores.jei.make_portal.MakePortalCategory;
import neo_ores.jei.make_portal.MakePortalWrapper;
import neo_ores.jei.mana_composition.ManaCompositionCategory;
import neo_ores.jei.mana_composition.ManaCompositionWrapper;
import neo_ores.jei.mana_crafting.ManaCraftingCategory;
import neo_ores.jei.mana_crafting.ManaCraftingWrapper;
import neo_ores.main.NeoOresBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JEIPlugin
public class NeoOresJEIPlugin implements IModPlugin
{
	public static IDrawableStatic mana_crafting;
	public static IDrawableStatic mana_composition;
	public static IDrawableStatic make_portal;

	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		registry.addRecipeCategories(new IRecipeCategory[] { new ManaCraftingCategory(registry.getJeiHelpers().getGuiHelper()) });
		registry.addRecipeCategories(new IRecipeCategory[] { new ManaCompositionCategory(registry.getJeiHelpers().getGuiHelper()) });
		registry.addRecipeCategories(new IRecipeCategory[] { new MakePortalCategory(registry.getJeiHelpers().getGuiHelper()) });
	}

	public void register(IModRegistry registry)
	{
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IRecipeTransferRegistry irtr = registry.getRecipeTransferRegistry();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		setupDrawables(guiHelper);
		registry.addRecipes(ManaCraftingWrapper.getRecipe(jeiHelpers), "neo_ores.mana_workbench");
		registry.addRecipeCatalyst(new ItemStack(NeoOresBlocks.mana_workbench), new String[] { "neo_ores.mana_workbench" });
		irtr.addRecipeTransferHandler(ContainerManaWorkbench.class, "neo_ores.mana_workbench", 0, 9, 9, 37);
		registry.addRecipeClickArea(GuiManaWorkbench.class, 84, 35, 26, 18, new String[] { "neo_ores.mana_workbench" });
		registry.addRecipes(ManaCompositionWrapper.getRecipe(jeiHelpers), "recipe.mana_composition");
		for (int i = 0; i < 16; i++)
			registry.addRecipeCatalyst(new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, i), new String[] { "recipe.mana_composition" });
		registry.addRecipes(MakePortalWrapper.getRecipe(jeiHelpers), "recipe.make_portal");
		registry.addRecipeCatalyst(new ItemStack(NeoOresBlocks.pedestal_water), new String[] { "recipe.make_portal" });
	}

	private static void setupDrawables(IGuiHelper helper)
	{
		ResourceLocation location = new ResourceLocation("neo_ores:textures/gui/jei/jei_mana_crafting.png");
		mana_crafting = helper.createDrawable(location, 0, 0, 176, 88);
		ResourceLocation location1 = new ResourceLocation("neo_ores:textures/gui/jei/jei_mana_compositing.png");
		mana_composition = helper.createDrawable(location1, 0, 0, 176, 122);
		ResourceLocation location2 = new ResourceLocation("neo_ores:textures/gui/jei/jei_make_portal.png");
		make_portal = helper.createDrawable(location2, 0, 0, 176, 88);
	}
}
