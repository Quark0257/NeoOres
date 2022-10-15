package neo_ores.jei.mana_composition;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import neo_ores.api.RecipeOreStack;
import neo_ores.api.recipe.ManaCompositionRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ManaCompositionWrapper  implements IRecipeWrapper {

	public final List<List<ItemStack>> input;
	public final ItemStack result;
	public final String information;
	
	public ManaCompositionWrapper(List<List<ItemStack>> input,ItemStack output,String info)
	{
		this.input = input;
		this.result = output;
		this.information = info;
	}
	
	@Override
	public void getIngredients(IIngredients arg0) 
	{
		arg0.setInputLists(VanillaTypes.ITEM,input);
		arg0.setOutput(VanillaTypes.ITEM, this.result);
	}
	
	@SuppressWarnings("deprecation")
	public static List<ManaCompositionWrapper> getRecipe(IJeiHelpers helper)
	{
		List<ManaCompositionWrapper> jeiRecipes = new ArrayList<ManaCompositionWrapper>();
		for(ManaCompositionRecipe mcr : GameRegistry.findRegistry(ManaCompositionRecipe.class).getValues())
		{
			List<List<ItemStack>> list = new ArrayList<List<ItemStack>>();
			int count = 0;
			for(RecipeOreStack iswsfr : mcr.getRecipe())
			{
				List<ItemStack> items = new ArrayList<ItemStack>();

				for(ItemStack original : iswsfr.getListTogether())
				{
					ItemStack copied = original.copy();
					copied.setCount(iswsfr.getSize());
					items.add(copied);
				}
				list.add(items);
				count++;
				if(ManaCompositionCategory.inputSizeAll <= count)
				{
					break;
				}
			}
			for(int i = 0;i < ManaCompositionCategory.inputSizeAll - count;i++)
			{
				list.add(new ArrayList<ItemStack>());
			}
			jeiRecipes.add(new ManaCompositionWrapper(list,mcr.getResult(),TextFormatting.BLUE + I18n.format("jei.gui.tier") + " : " + mcr.getTier()));
		}
		return jeiRecipes;
	}
	
	
	@Override
	public void drawInfo(Minecraft mc,int width,int height,int mouseX,int mouseY)
	{
		mc.fontRenderer.drawString(this.information, 96 - mc.fontRenderer.getStringWidth(this.information), 107, -1);
	}
}
