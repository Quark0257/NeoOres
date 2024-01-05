package neo_ores.jei.mana_crafting;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import neo_ores.jei.NeoOresJEIPlugin;
import neo_ores.main.Reference;
import net.minecraft.client.resources.I18n;

public class ManaCraftingCategory implements IRecipeCategory<ManaCraftingWrapper>
{
	private static final int outputSlot = 9;

	private static final int inputSlot0 = 0;

	@SuppressWarnings("unused")
	private final ICraftingGridHelper gridHelper;

	public ManaCraftingCategory(IGuiHelper helper)
	{
		this.gridHelper = helper.createCraftingGridHelper(inputSlot0, outputSlot);
	}

	@Override
	public IDrawable getBackground()
	{
		return NeoOresJEIPlugin.mana_crafting;
	}

	@Override
	public String getModName()
	{

		return Reference.MOD_NAME;
	}

	@Override
	public String getTitle()
	{
		return I18n.format("container.mana_workbench");
	}

	@Override
	public String getUid()
	{
		return "neo_ores.mana_workbench";
	}

	@Override
	public void setRecipe(IRecipeLayout arg0, ManaCraftingWrapper arg1, IIngredients arg2)
	{
		IGuiItemStackGroup guiItems = arg0.getItemStacks();
		for (int y = 0; y < 3; y++)
		{
			for (int x = 0; x < 3; x++)
			{
				int index = x + y * 3 + inputSlot0;
				guiItems.init(index, true, x * 18 + 25, y * 18 + 17);
			}
		}
		guiItems.init(outputSlot, false, 120, 35);
		guiItems.set(arg2);
	}

}
