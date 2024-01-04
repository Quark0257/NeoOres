package neo_ores.main;

import java.util.Arrays;
import java.util.List;

import neo_ores.api.spell.BasicData;
import neo_ores.api.spell.MageKnowledgeTableData;
import neo_ores.api.spell.SpellItem;
import neo_ores.api.spell.SpellItemType;
import neo_ores.spell.correction.SpellCanApplyNBT;
import neo_ores.spell.correction.SpellCollidableFilter;
import neo_ores.spell.correction.SpellContinuation;
import neo_ores.spell.correction.SpellDamageLevel;
import neo_ores.spell.correction.SpellGather;
import neo_ores.spell.correction.SpellHarvestLevel;
import neo_ores.spell.correction.SpellLuck;
import neo_ores.spell.correction.SpellNoAnyResistance;
import neo_ores.spell.correction.SpellNoGravity;
import neo_ores.spell.correction.SpellNoInertia;
import neo_ores.spell.correction.SpellRange;
import neo_ores.spell.correction.SpellSilk;
import neo_ores.spell.correction.SpellSpeed;
import neo_ores.spell.correction.SpellSupportLiquid;
import neo_ores.spell.correction.SpellTier;
import neo_ores.spell.effect.SpellComposition;
import neo_ores.spell.effect.SpellDig;
import neo_ores.spell.effect.SpellEarthDamage;
import neo_ores.spell.effect.SpellOreGen;
import neo_ores.spell.effect.SpellPullItem;
import neo_ores.spell.effect.SpellSummon;
import neo_ores.spell.form.SpellBullet;
import neo_ores.spell.form.SpellTouch;
import net.minecraft.util.ResourceLocation;

public class NeoOresSpells 
{
	public static final SpellItem spell_touch = new SpellItem(new BasicData(Reference.MOD_ID,"touch",1,SpellItemType.AIR,4,1),"touch",new MageKnowledgeTableData(null,0,0,new ResourceLocation(Reference.MOD_ID, "touch"),NeoOres.neo_ores),new SpellTouch());
	public static final SpellItem spell_dig = new SpellItem(new BasicData(Reference.MOD_ID,"dig",1,SpellItemType.EARTH,1,1),"dig",new MageKnowledgeTableData(spell_touch,0,1,new ResourceLocation(Reference.MOD_ID, "dig"),NeoOres.neo_ores),new SpellDig());
	public static final SpellItem spell_support_liquid = new SpellItem(new BasicData(Reference.MOD_ID,"support_liquid",5,SpellItemType.AIR,20,1),"support_liquid",new MageKnowledgeTableData(spell_dig,0,2,new ResourceLocation(Reference.MOD_ID,"support_liquid"),NeoOres.neo_ores),new SpellSupportLiquid());
	public static final SpellItem spell_composition = new SpellItem(new BasicData(Reference.MOD_ID,"composition",1,SpellItemType.EARTH,1,3),"composition",new MageKnowledgeTableData(NeoOresSpells.spell_dig,1,1,new ResourceLocation(Reference.MOD_ID,"composition"),NeoOres.neo_ores),new SpellComposition());
	public static final SpellItem spell_earth_damage = new SpellItem(new BasicData(Reference.MOD_ID,"earth_damage",1,SpellItemType.EARTH,1,1),"earth_damage",new MageKnowledgeTableData(NeoOresSpells.spell_dig,-1,1,new ResourceLocation(Reference.MOD_ID, "damage"),NeoOres.neo_ores),new SpellEarthDamage());
	public static final SpellItem spell_harvestLv1 = new SpellItem(new BasicData(Reference.MOD_ID,"harvest1",1,SpellItemType.EARTH,1,1.6f),"harvest_level",new MageKnowledgeTableData(NeoOresSpells.spell_dig,1,2,new ResourceLocation(Reference.MOD_ID,"dig"),NeoOres.neo_ores),new SpellHarvestLevel(1));
	public static final SpellItem spell_harvestLv2 = new SpellItem(new BasicData(Reference.MOD_ID,"harvest2",2,SpellItemType.EARTH,1,1.6f),"harvest_level",new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv1,1,3,new ResourceLocation(Reference.MOD_ID,"dig"),NeoOres.neo_ores),new SpellHarvestLevel(2));
	public static final SpellItem spell_harvestLv3 = new SpellItem(new BasicData(Reference.MOD_ID,"harvest3",3,SpellItemType.EARTH,1,1.6f),"harvest_level",new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv2,1,4,new ResourceLocation(Reference.MOD_ID,"dig"),NeoOres.neo_ores),new SpellHarvestLevel(3));
	public static final SpellItem spell_harvestLv4 = new SpellItem(new BasicData(Reference.MOD_ID,"harvest4",4,SpellItemType.EARTH,1,1.6f),"harvest_level",new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv3,1,5,new ResourceLocation(Reference.MOD_ID,"dig"),NeoOres.neo_ores),new SpellHarvestLevel(4));
	public static final SpellItem spell_harvestLv5 = new SpellItem(new BasicData(Reference.MOD_ID,"harvest5",5,SpellItemType.EARTH,1,1.6f),"harvest_level",new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv4,1,6,new ResourceLocation(Reference.MOD_ID,"dig"),NeoOres.neo_ores),new SpellHarvestLevel(5));
	public static final SpellItem spell_harvestLv6 = new SpellItem(new BasicData(Reference.MOD_ID,"harvest6",6,SpellItemType.EARTH,1,1.6f),"harvest_level",new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv5,1,7,new ResourceLocation(Reference.MOD_ID,"dig"),NeoOres.neo_ores),new SpellHarvestLevel(6));
	public static final SpellItem spell_harvestLv7 = new SpellItem(new BasicData(Reference.MOD_ID,"harvest7",7,SpellItemType.EARTH,1,1.6f),"harvest_level",new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv6,1,8,new ResourceLocation(Reference.MOD_ID,"dig"),NeoOres.neo_ores),new SpellHarvestLevel(7));
	public static final SpellItem spell_harvestLv8 = new SpellItem(new BasicData(Reference.MOD_ID,"harvest8",8,SpellItemType.EARTH,1,1.6f),"harvest_level",new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv7,1,9,new ResourceLocation(Reference.MOD_ID,"dig"),NeoOres.neo_ores),new SpellHarvestLevel(8));
	public static final SpellItem spell_harvestLv9 = new SpellItem(new BasicData(Reference.MOD_ID,"harvest9",9,SpellItemType.EARTH,1,1.6f),"harvest_level",new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv8,1,10,new ResourceLocation(Reference.MOD_ID,"dig"),NeoOres.neo_ores),new SpellHarvestLevel(9));
	public static final SpellItem spell_harvestLv10 = new SpellItem(new BasicData(Reference.MOD_ID,"harvest10",10,SpellItemType.EARTH,1,1.6f),"harvest_level",new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv9,1,11,new ResourceLocation(Reference.MOD_ID,"dig"),NeoOres.neo_ores),new SpellHarvestLevel(10));
	public static final SpellItem spell_harvestLv11 = new SpellItem(new BasicData(Reference.MOD_ID,"harvest11",11,SpellItemType.EARTH,1,1000000.0f),"harvest_level",new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv10,1,12,new ResourceLocation(Reference.MOD_ID,"dig"),NeoOres.neo_ores),new SpellHarvestLevel(11));
	public static final SpellItem spell_luck1 = new SpellItem(new BasicData(Reference.MOD_ID,"luck1",1,SpellItemType.WATER,10,1),"luck",new MageKnowledgeTableData(NeoOresSpells.spell_dig,-1,2,new ResourceLocation(Reference.MOD_ID,"luck"),NeoOres.neo_ores),new SpellLuck(1));
	public static final SpellItem spell_luck2 = new SpellItem(new BasicData(Reference.MOD_ID,"luck2",2,SpellItemType.WATER,10,2),"luck",new MageKnowledgeTableData(NeoOresSpells.spell_luck1,-1,3,new ResourceLocation(Reference.MOD_ID,"luck"),NeoOres.neo_ores),new SpellLuck(2));
	public static final SpellItem spell_luck3 = new SpellItem(new BasicData(Reference.MOD_ID,"luck3",3,SpellItemType.WATER,10,3),"luck",new MageKnowledgeTableData(NeoOresSpells.spell_luck2,-1,4,new ResourceLocation(Reference.MOD_ID,"luck"),NeoOres.neo_ores),new SpellLuck(3));
	public static final SpellItem spell_luck4 = new SpellItem(new BasicData(Reference.MOD_ID,"luck4",4,SpellItemType.WATER,10,4),"luck",new MageKnowledgeTableData(NeoOresSpells.spell_luck3,-1,5,new ResourceLocation(Reference.MOD_ID,"luck"),NeoOres.neo_ores),new SpellLuck(4));
	public static final SpellItem spell_luck5 = new SpellItem(new BasicData(Reference.MOD_ID,"luck5",5,SpellItemType.WATER,10,5),"luck",new MageKnowledgeTableData(NeoOresSpells.spell_luck4,-1,6,new ResourceLocation(Reference.MOD_ID,"luck"),NeoOres.neo_ores),new SpellLuck(5));
	public static final SpellItem spell_luck6 = new SpellItem(new BasicData(Reference.MOD_ID,"luck6",6,SpellItemType.WATER,10,6),"luck",new MageKnowledgeTableData(NeoOresSpells.spell_luck5,-1,7,new ResourceLocation(Reference.MOD_ID,"luck"),NeoOres.neo_ores),new SpellLuck(6));
	public static final SpellItem spell_luck7 = new SpellItem(new BasicData(Reference.MOD_ID,"luck7",7,SpellItemType.WATER,10,7),"luck",new MageKnowledgeTableData(NeoOresSpells.spell_luck6,-1,8,new ResourceLocation(Reference.MOD_ID,"luck"),NeoOres.neo_ores),new SpellLuck(7));
	public static final SpellItem spell_luck8 = new SpellItem(new BasicData(Reference.MOD_ID,"luck8",8,SpellItemType.WATER,10,8),"luck",new MageKnowledgeTableData(NeoOresSpells.spell_luck7,-1,9,new ResourceLocation(Reference.MOD_ID,"luck"),NeoOres.neo_ores),new SpellLuck(8));
	public static final SpellItem spell_luck9 = new SpellItem(new BasicData(Reference.MOD_ID,"luck9",9,SpellItemType.WATER,10,9),"luck",new MageKnowledgeTableData(NeoOresSpells.spell_luck8,-1,10,new ResourceLocation(Reference.MOD_ID,"luck"),NeoOres.neo_ores),new SpellLuck(9));
	public static final SpellItem spell_luck10 = new SpellItem(new BasicData(Reference.MOD_ID,"luck10",10,SpellItemType.WATER,10,10),"luck",new MageKnowledgeTableData(NeoOresSpells.spell_luck9,-1,11,new ResourceLocation(Reference.MOD_ID,"luck"),NeoOres.neo_ores),new SpellLuck(10));
	public static final SpellItem spell_silk = new SpellItem(new BasicData(Reference.MOD_ID,"silk",4,SpellItemType.AIR,40,1),"silktouch",new MageKnowledgeTableData(NeoOresSpells.spell_support_liquid,0,3,new ResourceLocation(Reference.MOD_ID,"silktouch"),NeoOres.neo_ores),new SpellSilk());
	public static final SpellItem spell_tier1 = new SpellItem(new BasicData(Reference.MOD_ID,"tier1",1,SpellItemType.AIR,10,1.0F),"tier",new MageKnowledgeTableData(NeoOresSpells.spell_composition,2,2,new ResourceLocation(Reference.MOD_ID,"composition"),NeoOres.neo_ores),new SpellTier(1));
	public static final SpellItem spell_tier2 = new SpellItem(new BasicData(Reference.MOD_ID,"tier2",2,SpellItemType.AIR,1,1.1F),"tier",new MageKnowledgeTableData(NeoOresSpells.spell_tier1,2,3,new ResourceLocation(Reference.MOD_ID,"composition"),NeoOres.neo_ores),new SpellTier(2));
	public static final SpellItem spell_tier3 = new SpellItem(new BasicData(Reference.MOD_ID,"tier3",3,SpellItemType.AIR,1,1.2F),"tier3",new MageKnowledgeTableData(NeoOresSpells.spell_tier2,2,4,new ResourceLocation(Reference.MOD_ID,"composition"),NeoOres.neo_ores),new SpellTier(3));
	public static final SpellItem spell_bullet = new SpellItem(new BasicData(Reference.MOD_ID,"spell_bullet",4,SpellItemType.AIR,1,5),"spell_bullet",new MageKnowledgeTableData(NeoOresSpells.spell_tier2,3,3,new ResourceLocation(Reference.MOD_ID,"spell_bullet"),NeoOres.neo_ores),new SpellBullet());
	public static final SpellItem spell_speed1 = new SpellItem(new BasicData(Reference.MOD_ID,"spell_speed1",4,SpellItemType.EARTH,2,2),"speed",new MageKnowledgeTableData(NeoOresSpells.spell_bullet,3,4,new ResourceLocation(Reference.MOD_ID,"speed"),NeoOres.neo_ores),new SpellSpeed(1));
	public static final SpellItem spell_speed2 = new SpellItem(new BasicData(Reference.MOD_ID,"spell_speed2",5,SpellItemType.EARTH,2,2),"speed",new MageKnowledgeTableData(NeoOresSpells.spell_speed1,3,5,new ResourceLocation(Reference.MOD_ID,"speed"),NeoOres.neo_ores),new SpellSpeed(2));
	public static final SpellItem spell_speed3 = new SpellItem(new BasicData(Reference.MOD_ID,"spell_speed3",6,SpellItemType.EARTH,2,2),"speed",new MageKnowledgeTableData(NeoOresSpells.spell_speed2,3,6,new ResourceLocation(Reference.MOD_ID,"speed"),NeoOres.neo_ores),new SpellSpeed(3));
	public static final SpellItem spell_speed4 = new SpellItem(new BasicData(Reference.MOD_ID,"spell_speed4",7,SpellItemType.EARTH,2,2),"speed",new MageKnowledgeTableData(NeoOresSpells.spell_speed3,3,7,new ResourceLocation(Reference.MOD_ID,"speed"),NeoOres.neo_ores),new SpellSpeed(4));
	public static final SpellItem spell_continuation1 = new SpellItem(new BasicData(Reference.MOD_ID,"spell_continuation1",4,SpellItemType.AIR,2,2),"continuation",new MageKnowledgeTableData(NeoOresSpells.spell_bullet,4,4,new ResourceLocation(Reference.MOD_ID,"continuation"),NeoOres.neo_ores),new SpellContinuation(1));
	public static final SpellItem spell_continuation2 = new SpellItem(new BasicData(Reference.MOD_ID,"spell_continuation2",5,SpellItemType.AIR,2,2),"continuation",new MageKnowledgeTableData(NeoOresSpells.spell_continuation1,4,5,new ResourceLocation(Reference.MOD_ID,"continuation"),NeoOres.neo_ores),new SpellContinuation(2));
	public static final SpellItem spell_continuation3 = new SpellItem(new BasicData(Reference.MOD_ID,"spell_continuation3",6,SpellItemType.AIR,2,2),"continuation",new MageKnowledgeTableData(NeoOresSpells.spell_continuation2,4,6,new ResourceLocation(Reference.MOD_ID,"continuation"),NeoOres.neo_ores),new SpellContinuation(3));
	public static final SpellItem spell_continuation4 = new SpellItem(new BasicData(Reference.MOD_ID,"spell_continuation4",7,SpellItemType.AIR,2,2),"continuation",new MageKnowledgeTableData(NeoOresSpells.spell_continuation3,4,7,new ResourceLocation(Reference.MOD_ID,"continuation"),NeoOres.neo_ores),new SpellContinuation(4));
	public static final SpellItem spell_noGravity = new SpellItem(new BasicData(Reference.MOD_ID,"spell_noGravity",8,SpellItemType.AIR,100,1),"no_gravity",new MageKnowledgeTableData(NeoOresSpells.spell_speed4,3,8,new ResourceLocation(Reference.MOD_ID,"no_gravity"),NeoOres.neo_ores),new SpellNoGravity());
	public static final SpellItem spell_noAnyResistance = new SpellItem(new BasicData(Reference.MOD_ID,"spell_noAnyResistance",8,SpellItemType.EARTH,100,1),"no_resistance",new MageKnowledgeTableData(NeoOresSpells.spell_continuation4,4,8,new ResourceLocation(Reference.MOD_ID,"no_resistance"),NeoOres.neo_ores),new SpellNoAnyResistance());
	public static final SpellItem spell_tier4 = new SpellItem(new BasicData(Reference.MOD_ID,"tier4",4,SpellItemType.AIR,1,1.3F),"tier",new MageKnowledgeTableData(NeoOresSpells.spell_tier3,2,5,new ResourceLocation(Reference.MOD_ID,"composition"),NeoOres.neo_ores),new SpellTier(4));
	public static final SpellItem spell_tier5 = new SpellItem(new BasicData(Reference.MOD_ID,"tier5",5,SpellItemType.AIR,1,1.4F),"tier5",new MageKnowledgeTableData(NeoOresSpells.spell_tier4,2,6,new ResourceLocation(Reference.MOD_ID,"composition"),NeoOres.neo_ores),new SpellTier(5));
	public static final SpellItem spell_tier6 = new SpellItem(new BasicData(Reference.MOD_ID,"tier6",6,SpellItemType.AIR,1,1.5F),"tier",new MageKnowledgeTableData(NeoOresSpells.spell_tier5,2,7,new ResourceLocation(Reference.MOD_ID,"composition"),NeoOres.neo_ores),new SpellTier(6));
	public static final SpellItem spell_tier7 = new SpellItem(new BasicData(Reference.MOD_ID,"tier7",7,SpellItemType.AIR,1,1.6F),"tier7",new MageKnowledgeTableData(NeoOresSpells.spell_tier6,2,8,new ResourceLocation(Reference.MOD_ID,"composition"),NeoOres.neo_ores),new SpellTier(7));
	public static final SpellItem spell_tier8 = new SpellItem(new BasicData(Reference.MOD_ID,"tier8",8,SpellItemType.AIR,1,1.7F),"tier",new MageKnowledgeTableData(NeoOresSpells.spell_tier7,2,9,new ResourceLocation(Reference.MOD_ID,"composition"),NeoOres.neo_ores),new SpellTier(8));
	public static final SpellItem spell_tier9 = new SpellItem(new BasicData(Reference.MOD_ID,"tier9",9,SpellItemType.AIR,1,1.8F),"tier9",new MageKnowledgeTableData(NeoOresSpells.spell_tier8,2,10,new ResourceLocation(Reference.MOD_ID,"composition"),NeoOres.neo_ores),new SpellTier(9));
	public static final SpellItem spell_tier10 = new SpellItem(new BasicData(Reference.MOD_ID,"tier10",10,SpellItemType.AIR,1,1.9F),"tier",new MageKnowledgeTableData(NeoOresSpells.spell_tier9,2,11,new ResourceLocation(Reference.MOD_ID,"composition"),NeoOres.neo_ores),new SpellTier(10));
	public static final SpellItem spell_tier11 = new SpellItem(new BasicData(Reference.MOD_ID,"tier11",11,SpellItemType.AIR,1,2.0F),"tier",new MageKnowledgeTableData(NeoOresSpells.spell_tier10,2,12,new ResourceLocation(Reference.MOD_ID,"composition"),NeoOres.neo_ores),new SpellTier(11));
	public static final SpellItem spell_range1 = new SpellItem(new BasicData(Reference.MOD_ID,"range1",2,SpellItemType.EARTH,1,9),"range",new MageKnowledgeTableData(NeoOresSpells.spell_silk,0,4,new ResourceLocation(Reference.MOD_ID,"range"),NeoOres.neo_ores),new SpellRange(1));
	public static final SpellItem spell_gather = new SpellItem(new BasicData(Reference.MOD_ID,"gather",4,SpellItemType.WATER,2,1),"gather",new MageKnowledgeTableData(NeoOresSpells.spell_range1,0,5,new ResourceLocation(Reference.MOD_ID,"gather"),NeoOres.neo_ores),new SpellGather());
	public static final SpellItem spell_range2 = new SpellItem(new BasicData(Reference.MOD_ID,"range2",5,SpellItemType.EARTH,1,25),"range",new MageKnowledgeTableData(NeoOresSpells.spell_gather,0,6,new ResourceLocation(Reference.MOD_ID,"range"),NeoOres.neo_ores),new SpellRange(2));
	public static final SpellItem spell_range3 = new SpellItem(new BasicData(Reference.MOD_ID,"range3",8,SpellItemType.EARTH,1,49),"range",new MageKnowledgeTableData(NeoOresSpells.spell_range2,0,7,new ResourceLocation(Reference.MOD_ID,"range"),NeoOres.neo_ores),new SpellRange(3));
	public static final SpellItem spell_range4 = new SpellItem(new BasicData(Reference.MOD_ID,"range4",11,SpellItemType.EARTH,1,81),"range",new MageKnowledgeTableData(NeoOresSpells.spell_range3,0,8,new ResourceLocation(Reference.MOD_ID,"range"),NeoOres.neo_ores),new SpellRange(4));
	public static final SpellItem spell_damageLv1 = new SpellItem(new BasicData(Reference.MOD_ID,"damage1",1,SpellItemType.FIRE,1,1.6f),"damage_level",new MageKnowledgeTableData(NeoOresSpells.spell_earth_damage,-2,2,new ResourceLocation(Reference.MOD_ID,"damage"),NeoOres.neo_ores),new SpellDamageLevel(1));
	public static final SpellItem spell_damageLv2 = new SpellItem(new BasicData(Reference.MOD_ID,"damage2",2,SpellItemType.FIRE,1,1.6f),"damage_level",new MageKnowledgeTableData(NeoOresSpells.spell_damageLv1,-2,3,new ResourceLocation(Reference.MOD_ID,"damage"),NeoOres.neo_ores),new SpellDamageLevel(2));
	public static final SpellItem spell_damageLv3 = new SpellItem(new BasicData(Reference.MOD_ID,"damage3",3,SpellItemType.FIRE,1,1.6f),"damage_level",new MageKnowledgeTableData(NeoOresSpells.spell_damageLv2,-2,4,new ResourceLocation(Reference.MOD_ID,"damage"),NeoOres.neo_ores),new SpellDamageLevel(3));
	public static final SpellItem spell_damageLv4 = new SpellItem(new BasicData(Reference.MOD_ID,"damage4",4,SpellItemType.FIRE,1,1.6f),"damage_level",new MageKnowledgeTableData(NeoOresSpells.spell_damageLv3,-2,5,new ResourceLocation(Reference.MOD_ID,"damage"),NeoOres.neo_ores),new SpellDamageLevel(4));
	public static final SpellItem spell_damageLv5 = new SpellItem(new BasicData(Reference.MOD_ID,"damage5",5,SpellItemType.FIRE,1,1.6f),"damage_level",new MageKnowledgeTableData(NeoOresSpells.spell_damageLv4,-2,6,new ResourceLocation(Reference.MOD_ID,"damage"),NeoOres.neo_ores),new SpellDamageLevel(5));
	public static final SpellItem spell_damageLv6 = new SpellItem(new BasicData(Reference.MOD_ID,"damage6",6,SpellItemType.FIRE,1,1.6f),"damage_level",new MageKnowledgeTableData(NeoOresSpells.spell_damageLv5,-2,7,new ResourceLocation(Reference.MOD_ID,"damage"),NeoOres.neo_ores),new SpellDamageLevel(6));
	public static final SpellItem spell_damageLv7 = new SpellItem(new BasicData(Reference.MOD_ID,"damage7",7,SpellItemType.FIRE,1,1.6f),"damage_level",new MageKnowledgeTableData(NeoOresSpells.spell_damageLv6,-2,8,new ResourceLocation(Reference.MOD_ID,"damage"),NeoOres.neo_ores),new SpellDamageLevel(7));
	public static final SpellItem spell_damageLv8 = new SpellItem(new BasicData(Reference.MOD_ID,"damage8",8,SpellItemType.FIRE,1,1.6f),"damage_level",new MageKnowledgeTableData(NeoOresSpells.spell_damageLv7,-2,9,new ResourceLocation(Reference.MOD_ID,"damage"),NeoOres.neo_ores),new SpellDamageLevel(8));
	public static final SpellItem spell_damageLv9 = new SpellItem(new BasicData(Reference.MOD_ID,"damage9",9,SpellItemType.FIRE,1,1.6f),"damage_level",new MageKnowledgeTableData(NeoOresSpells.spell_damageLv8,-2,10,new ResourceLocation(Reference.MOD_ID,"damage"),NeoOres.neo_ores),new SpellDamageLevel(9));
	public static final SpellItem spell_damageLv10 = new SpellItem(new BasicData(Reference.MOD_ID,"damage10",10,SpellItemType.FIRE,1,1.6f),"damage_level",new MageKnowledgeTableData(NeoOresSpells.spell_damageLv9,-2,11,new ResourceLocation(Reference.MOD_ID,"damage"),NeoOres.neo_ores),new SpellDamageLevel(10));
	public static final SpellItem spell_damageLv11 = new SpellItem(new BasicData(Reference.MOD_ID,"damage11",11,SpellItemType.FIRE,1,1.6f),"damage_level",new MageKnowledgeTableData(NeoOresSpells.spell_damageLv10,-2,12,new ResourceLocation(Reference.MOD_ID,"damage"),NeoOres.neo_ores),new SpellDamageLevel(11));
	public static final SpellItem spell_summon = new SpellItem(new BasicData(Reference.MOD_ID,"summon",5,SpellItemType.EARTH,100,10),"summon",new MageKnowledgeTableData(NeoOresSpells.spell_damageLv5,-3,7,new ResourceLocation(Reference.MOD_ID, "summon"),NeoOres.neo_ores),new SpellSummon());
	public static final SpellItem spell_nbt_applying = new SpellItem(new BasicData(Reference.MOD_ID,"nbt_apply",11,SpellItemType.WATER,1,100),"nbt_apply",new MageKnowledgeTableData(NeoOresSpells.spell_summon,-3,8,new ResourceLocation(Reference.MOD_ID, "nbt_apply"),NeoOres.neo_ores),new SpellCanApplyNBT());
	public static final SpellItem spell_ore_gen = new SpellItem(new BasicData(Reference.MOD_ID,"ore_gen",11,SpellItemType.EARTH,100,100),"ore_gen",new MageKnowledgeTableData(NeoOresSpells.spell_luck10,-1,12,new ResourceLocation(Reference.MOD_ID, "ore_gen"),NeoOres.neo_ores),new SpellOreGen());
	public static final SpellItem spell_no_inertia = new SpellItem(new BasicData(Reference.MOD_ID,"no_inertia",1,SpellItemType.EARTH,10,1),"no_inertia",new MageKnowledgeTableData(NeoOresSpells.spell_bullet,4,3,new ResourceLocation(Reference.MOD_ID, "no_inertia"),NeoOres.neo_ores),new SpellNoInertia());
	public static final SpellItem spell_pull_item = new SpellItem(new BasicData(Reference.MOD_ID,"pull_item",1,SpellItemType.AIR,1,1),"pull_item",new MageKnowledgeTableData(NeoOresSpells.spell_composition,3,2,new ResourceLocation(Reference.MOD_ID, "pull_item"),NeoOres.neo_ores),new SpellPullItem());
	public static final SpellItem spell_collidable_filter = new SpellItem(new BasicData(Reference.MOD_ID,"collidable_filter",1,SpellItemType.FIRE,1,1),"collidable_filter",new MageKnowledgeTableData(NeoOresSpells.spell_earth_damage,-2,1,new ResourceLocation(Reference.MOD_ID, "collidable_filter"),NeoOres.neo_ores),new SpellCollidableFilter());
	
	public static final List<SpellItem> registry = Arrays.asList(
			spell_touch,
			spell_dig,
			spell_support_liquid,
			spell_composition,
			spell_earth_damage,
			spell_harvestLv1,
			spell_harvestLv2,
			spell_harvestLv3,
			spell_harvestLv4,
			spell_harvestLv5,
			spell_harvestLv6,
			spell_harvestLv7,
			spell_harvestLv8,
			spell_harvestLv9,
			spell_harvestLv10,
			spell_harvestLv11,
			spell_luck1,
			spell_luck2,
			spell_luck3,
			spell_luck4,
			spell_luck5,
			spell_luck6,
			spell_luck7,
			spell_luck8,
			spell_luck9,
			spell_luck10,
			spell_silk,
			spell_tier1,
			spell_tier2,
			spell_tier3,
			spell_tier4,
			spell_tier5,
			spell_tier6,
			spell_tier7,
			spell_tier8,
			spell_tier9,
			spell_tier10,
			spell_tier11,
			spell_bullet,
			spell_speed1,
			spell_speed2,
			spell_speed3,
			spell_speed4,
			spell_continuation1,
			spell_continuation2,
			spell_continuation3,
			spell_continuation4,
			spell_noGravity,
			spell_noAnyResistance,
			spell_gather,
			spell_range1,
			spell_range2,
			spell_range3,
			spell_range4,
			spell_damageLv1,
			spell_damageLv2,
			spell_damageLv3,
			spell_damageLv4,
			spell_damageLv5,
			spell_damageLv6,
			spell_damageLv7,
			spell_damageLv8,
			spell_damageLv9,
			spell_damageLv10,
			spell_damageLv11,
			spell_summon,
			spell_nbt_applying,
			spell_ore_gen,
			spell_no_inertia,
			spell_pull_item,
			spell_collidable_filter
			);	
}
