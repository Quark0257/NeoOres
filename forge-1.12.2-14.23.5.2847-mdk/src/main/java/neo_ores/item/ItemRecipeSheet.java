package neo_ores.item;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.LongUtils;
import neo_ores.api.RecipeOreStack;
import neo_ores.api.spell.Spell;
import neo_ores.api.spell.SpellItem;
import neo_ores.api.spell.Spell.SpellCorrection;
import neo_ores.util.SpellUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemRecipeSheet extends INeoOresItem.Impl implements ISpellRecipeWritable
{

	@Override
	public void writeRecipeSpells(List<SpellItem> list, ItemStack stack) 
	{
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setTag("recipeSpells", SpellUtils.getNBTFromList(list));
	}

	@Override
	public List<SpellItem> readRecipeSpells(ItemStack stack) 
	{
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("recipeSpells", 10))
		{
			return SpellUtils.getListFromNBT(stack.getTagCompound().getCompoundTag("recipeSpells"));
		}
		return new ArrayList<SpellItem>();
	}	
	
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		if(!playerIn.world.isRemote || !this.hasRecipe(playerIn.getHeldItem(handIn)))return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
		ITextComponent itextcomponent = new TextComponentString(I18n.format("chat.displayRecipe"));
		playerIn.sendStatusMessage(itextcomponent, false);
		for(RecipeOreStack recipe : SpellUtils.getClumpedRecipeFromList(this.readRecipeSpells(playerIn.getHeldItem(handIn))))
		{
			if(recipe.isItemStack())
			{
				ITextComponent itextcomponent0 = new TextComponentString(recipe.getStack().getDisplayName() + " (" + recipe.getStack().getItem().getRegistryName().getResourceDomain() + ")" + " : x" + recipe.getSize());
				playerIn.sendStatusMessage(itextcomponent0, false);
			}
			else if(recipe.isOreDic())
			{
				ITextComponent itextcomponent0 = new TextComponentString(recipe.getOreDic() + I18n.format("chat.displayOreDic") + " : x" + recipe.getSize());
				playerIn.sendStatusMessage(itextcomponent0, false);
			}
			
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
	
	public boolean hasEffect(ItemStack stack)
	{
		return this.hasRecipe(stack);
	}

	@Override
	public boolean hasRecipe(ItemStack stack) 
	{
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("recipeSpells", 10);
	}
	
	public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag)
	{
		if(hasRecipe(itemStack))
		{
			List<SpellItem> spells = new ArrayList<SpellItem>(this.readRecipeSpells(itemStack));
			for(SpellItem spell : spells)
			{
				list.add(TextFormatting.GRAY + getName(spell) + (flag.isAdvanced() ? TextFormatting.DARK_GRAY + " (" + spell.toString() + ")" : ""));
			}
			long manaConsume = SpellUtils.getMPConsume(spells);
			list.add(TextFormatting.GRAY + I18n.format("tooltip.mana").trim() + " : " + LongUtils.convertString(manaConsume));
		}
	}
	
	public static String getName(SpellItem spellitem)
	{
		if(spellitem.getSpellClass() instanceof Spell.SpellCorrection)
		{
			SpellCorrection correction = (SpellCorrection)spellitem.getSpellClass();
			return I18n.format("spell." + spellitem.getTranslateKey() + ".name") + I18n.format("correction." + correction.getLevel());
		}
		else
		{
			return I18n.format("spell." + spellitem.getTranslateKey() + ".name");
		}
	}
}
