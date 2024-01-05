package neo_ores.jei.make_portal;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.NeoOresItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class MakePortalWrapper implements IRecipeWrapper
{
	public final List<ItemStack> input;
	public final ItemStack result;
	private final String information;

	public MakePortalWrapper(List<ItemStack> input, ItemStack output, String info)
	{
		this.input = input;
		this.result = output;
		this.information = info;
	}

	@Override
	public void getIngredients(IIngredients arg0)
	{
		arg0.setInputs(VanillaTypes.ITEM, input);
		arg0.setOutput(VanillaTypes.ITEM, this.result);
	}

	public static List<MakePortalWrapper> getRecipe(IJeiHelpers helper)
	{
		List<MakePortalWrapper> jeiRecipes = new ArrayList<MakePortalWrapper>();
		List<ItemStack> input = new ArrayList<ItemStack>();
		input.add(new ItemStack(NeoOresBlocks.pedestal_water));
		input.add(new ItemStack(NeoOresItems.essence, 64, 0));
		jeiRecipes.add(new MakePortalWrapper(input, new ItemStack(NeoOresBlocks.earth_portal), TextFormatting.BLUE + I18n.format("jei.gui.tier") + " : 3"));
		input = new ArrayList<ItemStack>();
		input.add(new ItemStack(NeoOresBlocks.pedestal_water));
		input.add(new ItemStack(NeoOresItems.essence, 64, 1));
		jeiRecipes.add(new MakePortalWrapper(input, new ItemStack(NeoOresBlocks.water_portal), TextFormatting.BLUE + I18n.format("jei.gui.tier") + " : 5"));
		input = new ArrayList<ItemStack>();
		input.add(new ItemStack(NeoOresBlocks.pedestal_water));
		input.add(new ItemStack(NeoOresItems.essence, 64, 3));
		jeiRecipes.add(new MakePortalWrapper(input, new ItemStack(NeoOresBlocks.air_portal), TextFormatting.BLUE + I18n.format("jei.gui.tier") + " : 7"));
		input = new ArrayList<ItemStack>();
		input.add(new ItemStack(NeoOresBlocks.pedestal_water));
		input.add(new ItemStack(NeoOresItems.essence, 64, 2));
		jeiRecipes.add(new MakePortalWrapper(input, new ItemStack(NeoOresBlocks.fire_portal), TextFormatting.BLUE + I18n.format("jei.gui.tier") + " : 9"));
		return jeiRecipes;
	}

	@Override
	public void drawInfo(Minecraft mc, int width, int height, int mouseX, int mouseY)
	{
		mc.fontRenderer.drawString(this.information, 88 - mc.fontRenderer.getStringWidth(this.information) / 2, 70, -1);
	}
}
