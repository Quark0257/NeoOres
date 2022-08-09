package neo_ores.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import neo_ores.api.spell.Spell;
import neo_ores.api.spell.SpellItem;
import neo_ores.api.spell.SpellItemType;
import neo_ores.api.recipe.SpellRecipe;
import neo_ores.api.spell.KnowledgeTab;
import neo_ores.main.Reference;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SpellUtils 
{
	
	@SuppressWarnings("deprecation")
	public static final List<SpellItem> registry = GameRegistry.findRegistry(SpellItem.class).getValues();
	
	@SuppressWarnings("deprecation")
	public static final List<SpellRecipe> recipes = GameRegistry.findRegistry(SpellRecipe.class).getValues();
	
	public static SpellItem getFromID(String modid,String id)
	{
		for(SpellItem spell : registry)
		{
			if(spell.getModId().equals(modid) && spell.getRegisteringId().equals(id))
			{
				return spell;
			}
		}
		return null;
	}
	
	public static Map<String,List<String>> getAll()
	{
		Map<String,List<String>> map = new HashMap<String,List<String>>();
		Set<String> sset = new HashSet<String>();
		for(SpellItem spell : registry)
		{
			sset.add(spell.getModId());
		}
		
		for(String key : sset)
		{
			List<String> spellids = new ArrayList<String>();
			for(SpellItem spell : registry)
			{
				if(spell.getModId().equals(key))
				{
					spellids.add(spell.getRegisteringId());
				}
			}
			map.put(key,spellids);
		}
		
		return map;
	}
	
	public static List<KnowledgeTab> getAllStudyTabs()
	{
		List<KnowledgeTab> list = new ArrayList<KnowledgeTab>();
		for(SpellItem spell : registry)
		{
			boolean flag = false;
			for(KnowledgeTab tab : list)
			{
				if(tab == spell.getTab()) flag = true;
			}
			if(!flag) list.add(spell.getTab());
		}
		
		return list;
	}
	
	public static List<SpellItem> getListFromNBT(NBTTagCompound nbt)
	{
		List<SpellItem> spells = new ArrayList<SpellItem>();
		for(int tier = 1;tier <= 11;tier++)
		{
			for(String key : nbt.getKeySet())
			{
				if(nbt.hasKey(key, 9))
				{
					NBTTagList list = nbt.getTagList(key, 8);
					for(int i = 0;i < list.tagCount();i++)
					{
						String id = list.getStringTagAt(i);
						for(SpellItem spell : registry)
						{
							if(spell.getModId().equals(key) && spell.getRegisteringId().equals(id) && spell.getTier() == tier)
							{
								spells.add(spell);
							}
						}
					}
				}
			}
		}
		return spells;
	}
	
	public static NBTTagCompound getNBTFromList(List<SpellItem> spells)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		Set<String> sset = new HashSet<String>();
		for(SpellItem spell : spells)
		{
			sset.add(spell.getModId());
		}
		
		for(String key : sset)
		{
			NBTTagList list = new NBTTagList();
			for(SpellItem spell : spells)
			{
				if(spell.getModId().equals(key))
				{
					list.appendTag(new NBTTagString(spell.getRegisteringId()));
				}
			}
			nbt.setTag(key, list);
		}
		
		return nbt;
		
	}
	
	public static List<SpellItem> getListFromItemStackNBT(NBTTagCompound nbt)
	{
		NBTTagCompound copied = nbt.copy();
		if(copied != null && copied.hasKey(NBTTagUtils.SPELL,10))
		{
			return getListFromNBT(copied.getCompoundTag(NBTTagUtils.SPELL));
		}
		return new ArrayList<SpellItem>();
	}
	
	public static List<SpellItem> getListInitialized(List<SpellItem> list)
	{
		List<SpellItem> init = new ArrayList<SpellItem>();
		for(SpellItem spell : list)
		{
			spell.getSpellClass().initialize();
			init.add(spell);
		}
		return init;
	}
	
	public static NBTTagCompound getItemStackNBTFromList(List<SpellItem> spells, NBTTagCompound nbt)
	{
		NBTTagCompound output = nbt.copy();
		output.setTag(NBTTagUtils.SPELL, (NBTBase)getNBTFromList(spells));
		return output;
	}
	
	public static SpellItem getFromXY(int x,int y)
	{
		for(SpellItem spell : registry)
		{
			if(spell.getPositionX() == x && spell.getPositionY() == y)
			{
				return spell;
			}
		}
		return null;
	}

	/*
	public static NBTTagCompound putStudyIntArrayToNBT(String modid,int value,NBTTagCompound neo_ores)
	{
		if(neo_ores.hasKey(NBTTagUtils.STUDY, 10))
		{
			NBTTagCompound study = neo_ores.getCompoundTag(NBTTagUtils.STUDY).copy();
			if(study.hasKey(modid, 11))
			{
				int[] array = study.getIntArray(modid);
				study.setIntArray(modid, SpellUtils.addValueToIntArray(value, array));
				neo_ores.setTag(NBTTagUtils.STUDY, study);
			}
		}
		
		return neo_ores;
	}
	*/
	public static ResourceLocation textureFromSpellItem(SpellItem spellitem)
	{
		String path = "textures/gui/spell/spell_";
		
		if(spellitem.getSpellClass() instanceof Spell.SpellConditional) path += "conditional_";
		else if(spellitem.getSpellClass() instanceof Spell.SpellCorrection) path += "correction_";
		else if(spellitem.getSpellClass() instanceof Spell.SpellEffect) path += "effect_";
		else path += "form_";
		
		if(spellitem.getType() == SpellItemType.AIR) path += "air";
		else if(spellitem.getType() == SpellItemType.EARTH) path += "earth";
		else if(spellitem.getType() == SpellItemType.FIRE) path += "fire";
		else path += "water";
		
		path += ".png";
		
		return new ResourceLocation(Reference.MOD_ID,path);
	}
	
	public static ResourceLocation textureFromSpellItemInactive(SpellItem spellitem)
	{
		String path = "textures/gui/spell/spell_";
		
		if(spellitem.getSpellClass() instanceof Spell.SpellConditional) path += "conditional_";
		else if(spellitem.getSpellClass() instanceof Spell.SpellCorrection) path += "correction_";
		else if(spellitem.getSpellClass() instanceof Spell.SpellEffect) path += "effect_";
		else path += "form_";
		
		path += "inactive.png";
		return new ResourceLocation(Reference.MOD_ID,path);
	}
	
	public static int offsetX(SpellItem spellitem)
	{
		return 8;
	}
	
	public static int offsetY(SpellItem spellitem)
	{
		if(spellitem.getSpellClass() instanceof Spell.SpellCorrection) return 10;
		return 8;
	}
	
	public static TextFormatting colorFromSpellItem(SpellItem spellitem)
	{
		if(spellitem.getType() == SpellItemType.AIR) return TextFormatting.AQUA;
		else if(spellitem.getType() == SpellItemType.EARTH) return TextFormatting.GREEN;
		else if(spellitem.getType() == SpellItemType.FIRE) return TextFormatting.GOLD;
		else return TextFormatting.DARK_PURPLE;
	}
	
	public static String typeFromSpellItem(SpellItem spellitem)
	{
		if(spellitem.getType() == SpellItemType.AIR) return "spell.air";
		else if(spellitem.getType() == SpellItemType.EARTH) return "spell.earth";
		else if(spellitem.getType() == SpellItemType.FIRE) return "spell.fire";
		else return "spell.water";
	}
	
	public static class NBTTagUtils
	{
		public static final String MAGIC = "magicData";
		public static final String STUDY = "studyData";
		public static final String SPELL = "activeSpells";
		public static final String MANA = "mana";
		public static final String MAX_MANA = "maxMana";
		public static final String MXP = "mxp";
		public static final String MAX_MXP = "maxMXP";
		public static final String MAGIC_POINT = "magicpoint";
		public static final String LEVEL = "level";
	}
	
	public static List<ItemStackWithSizeForRecipe> getRecipeFromList(List<SpellItem> spells)
	{
		List<ItemStackWithSizeForRecipe> recipe = new ArrayList<ItemStackWithSizeForRecipe>();
		for(SpellItem spell : spells)
		{
			for(SpellRecipe sr : recipes)
			{
				if(sr.getSpell().equals(spell))
				{
					recipe.addAll(sr.getRecipe());
				}
			}
		}
		return recipe;
	}
	
	public static List<ItemStackWithSizeForRecipe> getClumpedRecipeFromList(List<SpellItem> spells)
	{
		List<ItemStackWithSizeForRecipe> recipe = new ArrayList<ItemStackWithSizeForRecipe>();
		for(SpellItem spell : spells)
		{
			for(SpellRecipe sr : recipes)
			{
				if(sr.getSpell().equals(spell))
				{
					for(ItemStackWithSizeForRecipe iswsfr : sr.getRecipe())
					{
						int n = recipe.size();
						boolean flag = false;
						for(int i = 0;i < n;i++)
						{
							if(recipe.get(i).isItemStack() && iswsfr.isItemStack())
							{
								if(recipe.get(i).compareStackWith(iswsfr.getStack()))
								{
									recipe.set(i, new ItemStackWithSizeForRecipe(recipe.get(i).getStack(),recipe.get(i).getSize() + iswsfr.getSize()));
									flag = true;
									break;
								}
							}
							else if(recipe.get(i).isOreDic() && iswsfr.isOreDic())
							{
								if(recipe.get(i).getOreDic().equals(iswsfr.getOreDic()))
								{
									recipe.set(i, new ItemStackWithSizeForRecipe(recipe.get(i).getOreDic(),recipe.get(i).getSize() + iswsfr.getSize()));
									flag = true;
									break;
								}
							}
						}
						if(!flag) recipe.add(iswsfr);
					}
				}
			}	
		}
		return recipe;
	}
}
