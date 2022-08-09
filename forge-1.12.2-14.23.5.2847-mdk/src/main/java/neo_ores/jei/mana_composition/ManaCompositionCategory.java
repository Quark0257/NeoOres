package neo_ores.jei.mana_composition;

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

public class ManaCompositionCategory implements IRecipeCategory<ManaCompositionWrapper>{

	public static final int inputSlot0 = 0;
	public static final int inputSizeX = 10;
	public static final int inputSizeY = 5;
	public static final int inputSizeAll = inputSizeX * inputSizeY;
	public static final int outputSlot = inputSizeAll + 1;
	
	@SuppressWarnings("unused")
	private final ICraftingGridHelper gridHelper;
	
	public ManaCompositionCategory(IGuiHelper helper)
	{
		this.gridHelper = helper.createCraftingGridHelper(inputSlot0, outputSlot);
	}
	
	@Override
	public IDrawable getBackground() 
	{
		return NeoOresJEIPlugin.mana_composition;
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
		return "recipe.mana_composition";
	}

	@Override
	public void setRecipe(IRecipeLayout arg0, ManaCompositionWrapper arg1, IIngredients arg2) 
	{
		IGuiItemStackGroup guiItems = arg0.getItemStacks();
		for(int y = 0;y < inputSizeY;y++)
		{
			for(int x = 0;x < inputSizeX;x++)
			{
				int index = x + y * inputSizeY + inputSlot0;
				guiItems.init(index, true, x * 16 + 7, y * 16 + 5);
			}
		}
		guiItems.init(outputSlot,false,116,94);
		guiItems.set(arg2);
	}

}
