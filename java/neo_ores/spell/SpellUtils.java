package neo_ores.spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import neo_ores.main.Reference;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class SpellUtils 
{
	
	public static final IForgeRegistry<SpellItem> registry = GameRegistry.findRegistry(SpellItem.class);;
	
	@SuppressWarnings("deprecation")
	public static SpellItem getFromID(String modid,String id)
	{
		for(SpellItem spell : registry.getValues())
		{
			if(spell.getModId().equals(modid) && spell.getRegisteringId().equals(id))
			{
				return spell;
			}
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static Map<String,List<String>> getAll()
	{
		Map<String,List<String>> map = new HashMap<String,List<String>>();
		Set<String> sset = new HashSet<String>();
		for(SpellItem spell : registry.getValues())
		{
			sset.add(spell.getModId());
		}
		
		for(String key : sset)
		{
			List<String> spellids = new ArrayList<String>();
			for(SpellItem spell : registry.getValues())
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
	
	@SuppressWarnings("deprecation")
	public static List<SpellItem> getListFromNBT(NBTTagCompound nbt)
	{
		List<SpellItem> spells = new ArrayList<SpellItem>();
		for(int tier = 11;tier > 0;tier--)
		{
			for(String key : nbt.getKeySet())
			{
				if(nbt.hasKey(key, 9))
				{
					for(int i = 0;i < nbt.getTagList(key, 8).tagCount();i++)
					{
						String id = nbt.getTagList(key, 8).getStringTagAt(i);
						for(SpellItem spell : registry.getValues())
						{
							if(spell.getModId().equals(key) && spell.getRegisteringId() == id && spell.getTier() == tier)
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
	
	public static NBTTagCompound getItemStackNBTFromList(List<SpellItem> spells, NBTTagCompound nbt)
	{
		NBTTagCompound output = nbt.copy();
		output.setTag(NBTTagUtils.SPELL, (NBTBase)getNBTFromList(spells));
		return output;
	}
	
	@SuppressWarnings("deprecation")
	public static SpellItem getFromXY(int x,int y)
	{
		for(SpellItem spell : registry.getValues())
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
		String path = "textures/gui/study_table/spell_";
		
		if(spellitem.getSpellClass().getSpellItemType() == SpellType.CONDITIONAL) path += "conditional_";
		else if(spellitem.getSpellClass().getSpellItemType() == SpellType.CORRECTION) path += "correction_";
		else if(spellitem.getSpellClass().getSpellItemType() == SpellType.EFFECT) path += "effect_";
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
		String path = "textures/gui/study_table/spell_";
		
		if(spellitem.getSpellClass().getSpellItemType() == SpellType.CONDITIONAL) path += "conditional_";
		else if(spellitem.getSpellClass().getSpellItemType() == SpellType.CORRECTION) path += "correction_";
		else if(spellitem.getSpellClass().getSpellItemType() == SpellType.EFFECT) path += "effect_";
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
		if(spellitem.getSpellClass().getSpellItemType() == SpellType.CORRECTION) return 10;
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
}
