package neo_ores.jei.mana_crafting;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import neo_ores.api.LongUtils;
import neo_ores.api.recipe.ManaCraftingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ManaCraftingWrapper implements IRecipeWrapper 
{
	public final List<List<ItemStack>> input;
	public final ItemStack result;
	private final String information;
	
	public ManaCraftingWrapper(List<List<ItemStack>> input,ItemStack output,String info)
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
	public static List<ManaCraftingWrapper> getRecipe(IJeiHelpers helper)
	{
		List<ManaCraftingWrapper> jeiRecipes = new ArrayList<ManaCraftingWrapper>();
		IStackHelper stackHelper = helper.getStackHelper();
		for(ManaCraftingRecipe mcr : GameRegistry.findRegistry(ManaCraftingRecipe.class).getValues())
		{
			List<List<ItemStack>> list = new ArrayList<List<ItemStack>>();
			if(mcr.isShapeless()) 
			{
				for(Object o : mcr.getShapelessRecipe())
				{
					list.add(stackHelper.toItemStackList(o));
				}
				int length = mcr.getShapelessRecipe().size();
				for(int i = 0;i < 9 - length;i++)
				{
					list.add(new ArrayList<ItemStack>());
				}
			}
			else
			{
				for(Object[] os : mcr.getShapedRecipe())
				{
					for(Object o : os)
					{
						list.add(stackHelper.toItemStackList(o));
					}
					for(int i = 0;i < 3 - os.length;i++)
					{
						list.add(new ArrayList<ItemStack>());
					}
				}
				int length = mcr.getShapedRecipe().size();
				for(int i = 0;i < 3 - length;i++)
				{
					list.add(new ArrayList<ItemStack>());
				}
			}
			jeiRecipes.add(new ManaCraftingWrapper(list,mcr.getResult(),TextFormatting.BLUE + I18n.format("container.mana_workbench.cost") + ":" + LongUtils.convertString(mcr.mana())));
		}
		return jeiRecipes;
	}
	
	@Override
	public void drawInfo(Minecraft mc,int width,int height,int mouseX,int mouseY)
	{
		mc.fontRenderer.drawString(this.information, 128 - mc.fontRenderer.getStringWidth(this.information) / 2, 65, -1);
	}
}
