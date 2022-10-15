package neo_ores.jei.make_portal;

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

public class MakePortalCategory implements IRecipeCategory<MakePortalWrapper>
{
	@SuppressWarnings("unused")
	private final ICraftingGridHelper gridHelper;
	
	public MakePortalCategory(IGuiHelper helper)
	{
		this.gridHelper = helper.createCraftingGridHelper(0, 2);
	}
	
	@Override
	public IDrawable getBackground() 
	{
		return NeoOresJEIPlugin.make_portal;
	}

	@Override
	public String getModName() 
	{
		return Reference.MOD_NAME;
	}

	@Override
	public String getTitle() 
	{
		return I18n.format("recipe.mana_composition");
	}

	@Override
	public String getUid() 
	{
		return "recipe.make_portal";
	}

	@Override
	public void setRecipe(IRecipeLayout arg0, MakePortalWrapper arg1, IIngredients arg2) 
	{
		IGuiItemStackGroup guiItems = arg0.getItemStacks();
		guiItems.init(0, true, 48, 45);
		guiItems.init(1, true, 48, 11);
		guiItems.init(2,false,118,44);
		guiItems.set(arg2);
	}

}
