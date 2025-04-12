package neo_ores.main;

import java.util.Arrays;
import java.util.List;

import neo_ores.api.spell.BasicData;
import neo_ores.api.spell.MageKnowledgeTableData;
import neo_ores.api.spell.SpellItem;
import neo_ores.api.spell.SpellItemType;
import neo_ores.spell.conditional.SpellAttacked;
import neo_ores.spell.conditional.SpellAttacking;
import neo_ores.spell.conditional.SpellBreaking;
import neo_ores.spell.conditional.SpellDamaged;
import neo_ores.spell.conditional.SpellDead;
import neo_ores.spell.conditional.SpellFallen;
import neo_ores.spell.conditional.SpellJumped;
import neo_ores.spell.conditional.SpellSneaking;
import neo_ores.spell.correction.SpellAmplify;
import neo_ores.spell.correction.SpellCanApplyNBT;
import neo_ores.spell.correction.SpellUncollidable;
import neo_ores.spell.correction.SpellVanished;
import neo_ores.spell.correction.SpellContinuation;
import neo_ores.spell.correction.SpellContinuationDown;
import neo_ores.spell.correction.SpellDamageLevel;
import neo_ores.spell.correction.SpellDimensionOver;
import neo_ores.spell.correction.SpellDuration;
import neo_ores.spell.correction.SpellGather;
import neo_ores.spell.correction.SpellHarvestLevel;
import neo_ores.spell.correction.SpellLuck;
import neo_ores.spell.correction.SpellNoAnyResistance;
import neo_ores.spell.correction.SpellNoGravity;
import neo_ores.spell.correction.SpellNoInertia;
import neo_ores.spell.correction.SpellOffsetDown;
import neo_ores.spell.correction.SpellOffsetUp;
import neo_ores.spell.correction.SpellPlantable;
import neo_ores.spell.correction.SpellRange;
import neo_ores.spell.correction.SpellReach;
import neo_ores.spell.correction.SpellSilk;
import neo_ores.spell.correction.SpellSpeed;
import neo_ores.spell.correction.SpellSupportLiquid;
import neo_ores.spell.correction.SpellTier;
import neo_ores.spell.effect.SpellBlink;
import neo_ores.spell.effect.SpellComposition;
import neo_ores.spell.effect.SpellDay;
import neo_ores.spell.effect.SpellDig;
import neo_ores.spell.effect.SpellDisarm;
import neo_ores.spell.effect.SpellDiscombine;
import neo_ores.spell.effect.SpellEarthDamage;
import neo_ores.spell.effect.SpellFilterBlackList;
import neo_ores.spell.effect.SpellFilterWhiteList;
import neo_ores.spell.effect.SpellGrow;
import neo_ores.spell.effect.SpellHeal;
import neo_ores.spell.effect.SpellLifeTap;
import neo_ores.spell.effect.SpellLight;
import neo_ores.spell.effect.SpellLightningBolt;
import neo_ores.spell.effect.SpellNight;
import neo_ores.spell.effect.SpellOreGen;
import neo_ores.spell.effect.SpellPipeItem;
import neo_ores.spell.effect.SpellPlace;
import neo_ores.spell.effect.SpellPullItem;
import neo_ores.spell.effect.SpellPushItem;
import neo_ores.spell.effect.SpellRain;
import neo_ores.spell.effect.SpellSummon;
import neo_ores.spell.effect.SpellSunny;
import neo_ores.spell.effect.SpellTeleport;
import neo_ores.spell.effect.SpellThunder;
import neo_ores.spell.effect.SpellTranslocate;
import neo_ores.spell.form.SpellBullet;
import neo_ores.spell.form.SpellPlaceable;
import neo_ores.spell.form.SpellSelf;
import neo_ores.spell.form.SpellTouch;
import neo_ores.spell.form.SpellWornTick;
import net.minecraft.util.ResourceLocation;

public class NeoOresSpells
{
	public static final SpellItem spell_touch = new SpellItem(new BasicData(Reference.MOD_ID, "touch", 1, SpellItemType.AIR, 0, 1), "touch",
			new MageKnowledgeTableData(null, 0, 0, new ResourceLocation(Reference.MOD_ID, "touch"), NeoOres.neo_ores), SpellTouch.class);
	public static final SpellItem spell_dig = new SpellItem(new BasicData(Reference.MOD_ID, "dig", 1, SpellItemType.EARTH, 10, 1), "dig",
			new MageKnowledgeTableData(spell_touch, 0, -1, new ResourceLocation(Reference.MOD_ID, "dig"), NeoOres.neo_ores), SpellDig.class);
	public static final SpellItem spell_support_liquid = new SpellItem(new BasicData(Reference.MOD_ID, "support_liquid", 5, SpellItemType.AIR, 20, 1), "support_liquid",
			new MageKnowledgeTableData(spell_dig, 0, -2, new ResourceLocation(Reference.MOD_ID, "support_liquid"), NeoOres.neo_ores), SpellSupportLiquid.class);
	public static final SpellItem spell_place = new SpellItem(new BasicData(Reference.MOD_ID, "place", 1, SpellItemType.EARTH, 5, 1), "place",
			new MageKnowledgeTableData(NeoOresSpells.spell_dig, 1, -1, new ResourceLocation(Reference.MOD_ID, "place"), NeoOres.neo_ores), SpellPlace.class);
	public static final SpellItem spell_composition = new SpellItem(new BasicData(Reference.MOD_ID, "composition", 1, SpellItemType.EARTH, 15, 1), "composition",
			new MageKnowledgeTableData(NeoOresSpells.spell_place, 1, 0, new ResourceLocation(Reference.MOD_ID, "composition"), NeoOres.neo_ores), SpellComposition.class);
	public static final SpellItem spell_earth_damage = new SpellItem(new BasicData(Reference.MOD_ID, "earth_damage", 1, SpellItemType.EARTH, 30, 1), "earth_damage",
			new MageKnowledgeTableData(NeoOresSpells.spell_touch, 0, 1, new ResourceLocation(Reference.MOD_ID, "damage"), NeoOres.neo_ores), SpellEarthDamage.class);
	public static final SpellItem spell_harvestLv1 = new SpellItem(new BasicData(Reference.MOD_ID, "harvest1", 1, SpellItemType.EARTH, 1, 1.6f), "harvest_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_dig, 0, -2, new ResourceLocation(Reference.MOD_ID, "dig"), NeoOres.neo_ores), SpellHarvestLevel.class, (int) 1);
	public static final SpellItem spell_harvestLv2 = new SpellItem(new BasicData(Reference.MOD_ID, "harvest2", 2, SpellItemType.EARTH, 1, 1.6f), "harvest_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv1, 0, -3, new ResourceLocation(Reference.MOD_ID, "dig"), NeoOres.neo_ores), SpellHarvestLevel.class, (int) 2);
	public static final SpellItem spell_harvestLv3 = new SpellItem(new BasicData(Reference.MOD_ID, "harvest3", 3, SpellItemType.EARTH, 1, 1.6f), "harvest_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv2, 0, -4, new ResourceLocation(Reference.MOD_ID, "dig"), NeoOres.neo_ores), SpellHarvestLevel.class, (int) 3);
	public static final SpellItem spell_harvestLv4 = new SpellItem(new BasicData(Reference.MOD_ID, "harvest4", 4, SpellItemType.EARTH, 1, 1.6f), "harvest_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv3, 0, -5, new ResourceLocation(Reference.MOD_ID, "dig"), NeoOres.neo_ores), SpellHarvestLevel.class, (int) 4);
	public static final SpellItem spell_harvestLv5 = new SpellItem(new BasicData(Reference.MOD_ID, "harvest5", 5, SpellItemType.EARTH, 1, 1.6f), "harvest_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv4, 0, -6, new ResourceLocation(Reference.MOD_ID, "dig"), NeoOres.neo_ores), SpellHarvestLevel.class, (int) 5);
	public static final SpellItem spell_harvestLv6 = new SpellItem(new BasicData(Reference.MOD_ID, "harvest6", 6, SpellItemType.EARTH, 1, 1.6f), "harvest_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv5, 0, -7, new ResourceLocation(Reference.MOD_ID, "dig"), NeoOres.neo_ores), SpellHarvestLevel.class, (int) 6);
	public static final SpellItem spell_harvestLv7 = new SpellItem(new BasicData(Reference.MOD_ID, "harvest7", 7, SpellItemType.EARTH, 1, 1.6f), "harvest_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv6, 0, -8, new ResourceLocation(Reference.MOD_ID, "dig"), NeoOres.neo_ores), SpellHarvestLevel.class, (int) 7);
	public static final SpellItem spell_harvestLv8 = new SpellItem(new BasicData(Reference.MOD_ID, "harvest8", 8, SpellItemType.EARTH, 1, 1.6f), "harvest_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv7, 0, -9, new ResourceLocation(Reference.MOD_ID, "dig"), NeoOres.neo_ores), SpellHarvestLevel.class, (int) 8);
	public static final SpellItem spell_harvestLv9 = new SpellItem(new BasicData(Reference.MOD_ID, "harvest9", 9, SpellItemType.EARTH, 1, 1.6f), "harvest_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv8, 0, -10, new ResourceLocation(Reference.MOD_ID, "dig"), NeoOres.neo_ores), SpellHarvestLevel.class, (int) 9);
	public static final SpellItem spell_harvestLv10 = new SpellItem(new BasicData(Reference.MOD_ID, "harvest10", 10, SpellItemType.EARTH, 1, 1.6f), "harvest_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv9, 0, -11, new ResourceLocation(Reference.MOD_ID, "dig"), NeoOres.neo_ores), SpellHarvestLevel.class, (int) 10);
	public static final SpellItem spell_harvestLv11 = new SpellItem(new BasicData(Reference.MOD_ID, "harvest11", 11, SpellItemType.EARTH, 1, 1000000.0f), "harvest_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_harvestLv10, 0, -12, new ResourceLocation(Reference.MOD_ID, "dig"), NeoOres.neo_ores), SpellHarvestLevel.class, (int) 11);
	public static final SpellItem spell_silk = new SpellItem(new BasicData(Reference.MOD_ID, "silk", 4, SpellItemType.AIR, 40, 1), "silktouch",
			new MageKnowledgeTableData(NeoOresSpells.spell_dig, -1, -1, new ResourceLocation(Reference.MOD_ID, "silktouch"), NeoOres.neo_ores), SpellSilk.class);
	public static final SpellItem spell_gather = new SpellItem(new BasicData(Reference.MOD_ID, "gather", 4, SpellItemType.WATER, 2, 1), "gather",
			new MageKnowledgeTableData(NeoOresSpells.spell_silk, -1, 0, new ResourceLocation(Reference.MOD_ID, "gather"), NeoOres.neo_ores), SpellGather.class);
	public static final SpellItem spell_self = new SpellItem(new BasicData(Reference.MOD_ID, "self", 1, SpellItemType.WATER, 0, 0.5F), "self",
			new MageKnowledgeTableData(NeoOresSpells.spell_earth_damage, -1, 1, new ResourceLocation(Reference.MOD_ID, "self"), NeoOres.neo_ores), SpellSelf.class);
	public static final SpellItem spell_luck1 = new SpellItem(new BasicData(Reference.MOD_ID, "luck1", 1, SpellItemType.WATER, 10, 1), "luck",
			new MageKnowledgeTableData(NeoOresSpells.spell_gather, -2, 0, new ResourceLocation(Reference.MOD_ID, "luck"), NeoOres.neo_ores), SpellLuck.class, 1);
	public static final SpellItem spell_luck2 = new SpellItem(new BasicData(Reference.MOD_ID, "luck2", 2, SpellItemType.WATER, 10, 2), "luck",
			new MageKnowledgeTableData(NeoOresSpells.spell_luck1, -3, 0, new ResourceLocation(Reference.MOD_ID, "luck"), NeoOres.neo_ores), SpellLuck.class, 2);
	public static final SpellItem spell_luck3 = new SpellItem(new BasicData(Reference.MOD_ID, "luck3", 3, SpellItemType.WATER, 10, 3), "luck",
			new MageKnowledgeTableData(NeoOresSpells.spell_luck2, -4, 0, new ResourceLocation(Reference.MOD_ID, "luck"), NeoOres.neo_ores), SpellLuck.class, 3);
	public static final SpellItem spell_luck4 = new SpellItem(new BasicData(Reference.MOD_ID, "luck4", 4, SpellItemType.WATER, 10, 4), "luck",
			new MageKnowledgeTableData(NeoOresSpells.spell_luck3, -5, 0, new ResourceLocation(Reference.MOD_ID, "luck"), NeoOres.neo_ores), SpellLuck.class, 4);
	public static final SpellItem spell_luck5 = new SpellItem(new BasicData(Reference.MOD_ID, "luck5", 5, SpellItemType.WATER, 10, 5), "luck",
			new MageKnowledgeTableData(NeoOresSpells.spell_luck4, -6, 0, new ResourceLocation(Reference.MOD_ID, "luck"), NeoOres.neo_ores), SpellLuck.class, 5);
	public static final SpellItem spell_luck6 = new SpellItem(new BasicData(Reference.MOD_ID, "luck6", 6, SpellItemType.WATER, 10, 6), "luck",
			new MageKnowledgeTableData(NeoOresSpells.spell_luck5, -7, 0, new ResourceLocation(Reference.MOD_ID, "luck"), NeoOres.neo_ores), SpellLuck.class, 6);
	public static final SpellItem spell_luck7 = new SpellItem(new BasicData(Reference.MOD_ID, "luck7", 7, SpellItemType.WATER, 10, 7), "luck",
			new MageKnowledgeTableData(NeoOresSpells.spell_luck6, -8, 0, new ResourceLocation(Reference.MOD_ID, "luck"), NeoOres.neo_ores), SpellLuck.class, 7);
	public static final SpellItem spell_luck8 = new SpellItem(new BasicData(Reference.MOD_ID, "luck8", 8, SpellItemType.WATER, 10, 8), "luck",
			new MageKnowledgeTableData(NeoOresSpells.spell_luck7, -9, 0, new ResourceLocation(Reference.MOD_ID, "luck"), NeoOres.neo_ores), SpellLuck.class, 8);
	public static final SpellItem spell_luck9 = new SpellItem(new BasicData(Reference.MOD_ID, "luck9", 9, SpellItemType.WATER, 10, 9), "luck",
			new MageKnowledgeTableData(NeoOresSpells.spell_luck8, -10, 0, new ResourceLocation(Reference.MOD_ID, "luck"), NeoOres.neo_ores), SpellLuck.class, 9);
	public static final SpellItem spell_luck10 = new SpellItem(new BasicData(Reference.MOD_ID, "luck10", 10, SpellItemType.WATER, 10, 10), "luck",
			new MageKnowledgeTableData(NeoOresSpells.spell_luck9, -11, 0, new ResourceLocation(Reference.MOD_ID, "luck"), NeoOres.neo_ores), SpellLuck.class, 10);
	public static final SpellItem spell_ore_gen = new SpellItem(new BasicData(Reference.MOD_ID, "ore_gen", 11, SpellItemType.WATER, 100, 100), "ore_gen",
			new MageKnowledgeTableData(NeoOresSpells.spell_luck10, -12, 0, new ResourceLocation(Reference.MOD_ID, "ore_gen"), NeoOres.neo_ores), SpellOreGen.class);
	public static final SpellItem spell_tier1 = new SpellItem(new BasicData(Reference.MOD_ID, "tier1", 1, SpellItemType.AIR, 5, 1.0F), "tier",
			new MageKnowledgeTableData(NeoOresSpells.spell_composition, 2, 0, new ResourceLocation(Reference.MOD_ID, "composition"), NeoOres.neo_ores), SpellTier.class, 1);
	public static final SpellItem spell_tier2 = new SpellItem(new BasicData(Reference.MOD_ID, "tier2", 2, SpellItemType.AIR, 1, 1.1F), "tier",
			new MageKnowledgeTableData(NeoOresSpells.spell_tier1, 3, 0, new ResourceLocation(Reference.MOD_ID, "composition"), NeoOres.neo_ores), SpellTier.class, 2);
	public static final SpellItem spell_tier3 = new SpellItem(new BasicData(Reference.MOD_ID, "tier3", 3, SpellItemType.AIR, 1, 1.2F), "tier3",
			new MageKnowledgeTableData(NeoOresSpells.spell_tier2, 4, 0, new ResourceLocation(Reference.MOD_ID, "composition"), NeoOres.neo_ores), SpellTier.class, 3);
	public static final SpellItem spell_tier4 = new SpellItem(new BasicData(Reference.MOD_ID, "tier4", 4, SpellItemType.AIR, 1, 1.3F), "tier",
			new MageKnowledgeTableData(NeoOresSpells.spell_tier3, 5, 0, new ResourceLocation(Reference.MOD_ID, "composition"), NeoOres.neo_ores), SpellTier.class, 4);
	public static final SpellItem spell_tier5 = new SpellItem(new BasicData(Reference.MOD_ID, "tier5", 5, SpellItemType.AIR, 1, 1.4F), "tier5",
			new MageKnowledgeTableData(NeoOresSpells.spell_tier4, 6, 0, new ResourceLocation(Reference.MOD_ID, "composition"), NeoOres.neo_ores), SpellTier.class, 5);
	public static final SpellItem spell_tier6 = new SpellItem(new BasicData(Reference.MOD_ID, "tier6", 6, SpellItemType.AIR, 1, 1.5F), "tier",
			new MageKnowledgeTableData(NeoOresSpells.spell_tier5, 7, 0, new ResourceLocation(Reference.MOD_ID, "composition"), NeoOres.neo_ores), SpellTier.class, 6);
	public static final SpellItem spell_tier7 = new SpellItem(new BasicData(Reference.MOD_ID, "tier7", 7, SpellItemType.AIR, 1, 1.6F), "tier7",
			new MageKnowledgeTableData(NeoOresSpells.spell_tier6, 8, 0, new ResourceLocation(Reference.MOD_ID, "composition"), NeoOres.neo_ores), SpellTier.class, 7);
	public static final SpellItem spell_tier8 = new SpellItem(new BasicData(Reference.MOD_ID, "tier8", 8, SpellItemType.AIR, 1, 1.7F), "tier",
			new MageKnowledgeTableData(NeoOresSpells.spell_tier7, 9, 0, new ResourceLocation(Reference.MOD_ID, "composition"), NeoOres.neo_ores), SpellTier.class, 8);
	public static final SpellItem spell_tier9 = new SpellItem(new BasicData(Reference.MOD_ID, "tier9", 9, SpellItemType.AIR, 1, 1.8F), "tier9",
			new MageKnowledgeTableData(NeoOresSpells.spell_tier8, 10, 0, new ResourceLocation(Reference.MOD_ID, "composition"), NeoOres.neo_ores), SpellTier.class, 9);
	public static final SpellItem spell_tier10 = new SpellItem(new BasicData(Reference.MOD_ID, "tier10", 10, SpellItemType.AIR, 1, 1.9F), "tier",
			new MageKnowledgeTableData(NeoOresSpells.spell_tier9, 11, 0, new ResourceLocation(Reference.MOD_ID, "composition"), NeoOres.neo_ores), SpellTier.class, 10);
	public static final SpellItem spell_tier11 = new SpellItem(new BasicData(Reference.MOD_ID, "tier11", 11, SpellItemType.AIR, 1, 2.0F), "tier",
			new MageKnowledgeTableData(NeoOresSpells.spell_tier10, 12, 0, new ResourceLocation(Reference.MOD_ID, "composition"), NeoOres.neo_ores), SpellTier.class, 11);
	public static final SpellItem spell_damageLv1 = new SpellItem(new BasicData(Reference.MOD_ID, "damage1", 1, SpellItemType.FIRE, 1, 1.6f), "damage_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_earth_damage, 0, 2, new ResourceLocation(Reference.MOD_ID, "damage"), NeoOres.neo_ores), SpellDamageLevel.class, 1);
	public static final SpellItem spell_damageLv2 = new SpellItem(new BasicData(Reference.MOD_ID, "damage2", 2, SpellItemType.FIRE, 1, 1.6f), "damage_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_damageLv1, 0, 3, new ResourceLocation(Reference.MOD_ID, "damage"), NeoOres.neo_ores), SpellDamageLevel.class, 2);
	public static final SpellItem spell_damageLv3 = new SpellItem(new BasicData(Reference.MOD_ID, "damage3", 3, SpellItemType.FIRE, 1, 1.6f), "damage_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_damageLv2, 0, 4, new ResourceLocation(Reference.MOD_ID, "damage"), NeoOres.neo_ores), SpellDamageLevel.class, 3);
	public static final SpellItem spell_damageLv4 = new SpellItem(new BasicData(Reference.MOD_ID, "damage4", 4, SpellItemType.FIRE, 1, 1.6f), "damage_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_damageLv3, 0, 5, new ResourceLocation(Reference.MOD_ID, "damage"), NeoOres.neo_ores), SpellDamageLevel.class, 4);
	public static final SpellItem spell_damageLv5 = new SpellItem(new BasicData(Reference.MOD_ID, "damage5", 5, SpellItemType.FIRE, 1, 1.6f), "damage_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_damageLv4, 0, 6, new ResourceLocation(Reference.MOD_ID, "damage"), NeoOres.neo_ores), SpellDamageLevel.class, 5);
	public static final SpellItem spell_damageLv6 = new SpellItem(new BasicData(Reference.MOD_ID, "damage6", 6, SpellItemType.FIRE, 1, 1.6f), "damage_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_damageLv5, 0, 7, new ResourceLocation(Reference.MOD_ID, "damage"), NeoOres.neo_ores), SpellDamageLevel.class, 6);
	public static final SpellItem spell_damageLv7 = new SpellItem(new BasicData(Reference.MOD_ID, "damage7", 7, SpellItemType.FIRE, 1, 1.6f), "damage_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_damageLv6, 0, 8, new ResourceLocation(Reference.MOD_ID, "damage"), NeoOres.neo_ores), SpellDamageLevel.class, 7);
	public static final SpellItem spell_damageLv8 = new SpellItem(new BasicData(Reference.MOD_ID, "damage8", 8, SpellItemType.FIRE, 1, 1.6f), "damage_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_damageLv7, 0, 9, new ResourceLocation(Reference.MOD_ID, "damage"), NeoOres.neo_ores), SpellDamageLevel.class, 8);
	public static final SpellItem spell_damageLv9 = new SpellItem(new BasicData(Reference.MOD_ID, "damage9", 9, SpellItemType.FIRE, 1, 1.6f), "damage_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_damageLv8, 0, 10, new ResourceLocation(Reference.MOD_ID, "damage"), NeoOres.neo_ores), SpellDamageLevel.class, 9);
	public static final SpellItem spell_damageLv10 = new SpellItem(new BasicData(Reference.MOD_ID, "damage10", 10, SpellItemType.FIRE, 1, 1.6f), "damage_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_damageLv9, 0, 11, new ResourceLocation(Reference.MOD_ID, "damage"), NeoOres.neo_ores), SpellDamageLevel.class, 10);
	public static final SpellItem spell_damageLv11 = new SpellItem(new BasicData(Reference.MOD_ID, "damage11", 11, SpellItemType.FIRE, 1, 1.6f), "damage_level",
			new MageKnowledgeTableData(NeoOresSpells.spell_damageLv10, 0, 12, new ResourceLocation(Reference.MOD_ID, "damage"), NeoOres.neo_ores), SpellDamageLevel.class, 11);
	public static final SpellItem spell_bullet = new SpellItem(new BasicData(Reference.MOD_ID, "spell_bullet", 4, SpellItemType.EARTH, 0, 5), "spell_bullet",
			new MageKnowledgeTableData(NeoOresSpells.spell_earth_damage, 1, 1, new ResourceLocation(Reference.MOD_ID, "spell_bullet"), NeoOres.neo_ores), SpellBullet.class);
	public static final SpellItem spell_uncollidable = new SpellItem(new BasicData(Reference.MOD_ID, "uncollidable", 4, SpellItemType.AIR, 2, 1), "uncollidable",
			new MageKnowledgeTableData(NeoOresSpells.spell_bullet, 2, 1, new ResourceLocation(Reference.MOD_ID, "uncollidable"), NeoOres.neo_ores), SpellUncollidable.class);
	public static final SpellItem spell_pull_item = new SpellItem(new BasicData(Reference.MOD_ID, "pull_item", 1, SpellItemType.AIR, 1, 1), "pull_item",
			new MageKnowledgeTableData(NeoOresSpells.spell_uncollidable, 3, 1, new ResourceLocation(Reference.MOD_ID, "pull_item"), NeoOres.neo_ores), SpellPullItem.class);
	public static final SpellItem spell_push_item = new SpellItem(new BasicData(Reference.MOD_ID, "push_item", 1, SpellItemType.AIR, 1, 1), "push_item",
			new MageKnowledgeTableData(NeoOresSpells.spell_pull_item, 4, 1, new ResourceLocation(Reference.MOD_ID, "push_item"), NeoOres.neo_ores), SpellPushItem.class);
	public static final SpellItem spell_pipe_item = new SpellItem(new BasicData(Reference.MOD_ID, "pipe_item", 2, SpellItemType.AIR, 2, 1), "pipe_item",
			new MageKnowledgeTableData(NeoOresSpells.spell_push_item, 5, 1, new ResourceLocation(Reference.MOD_ID, "pipe_item"), NeoOres.neo_ores), SpellPipeItem.class);
	public static final SpellItem spell_speed1 = new SpellItem(new BasicData(Reference.MOD_ID, "spell_speed1", 4, SpellItemType.AIR, 2, 2), "speed",
			new MageKnowledgeTableData(NeoOresSpells.spell_uncollidable, 3, 2, new ResourceLocation(Reference.MOD_ID, "speed"), NeoOres.neo_ores), SpellSpeed.class, 1);
	public static final SpellItem spell_speed2 = new SpellItem(new BasicData(Reference.MOD_ID, "spell_speed2", 5, SpellItemType.AIR, 2, 2), "speed",
			new MageKnowledgeTableData(NeoOresSpells.spell_speed1, 4, 2, new ResourceLocation(Reference.MOD_ID, "speed"), NeoOres.neo_ores), SpellSpeed.class, 2);
	public static final SpellItem spell_speed3 = new SpellItem(new BasicData(Reference.MOD_ID, "spell_speed3", 6, SpellItemType.AIR, 2, 2), "speed",
			new MageKnowledgeTableData(NeoOresSpells.spell_speed2, 5, 3, new ResourceLocation(Reference.MOD_ID, "speed"), NeoOres.neo_ores), SpellSpeed.class, 3);
	public static final SpellItem spell_speed4 = new SpellItem(new BasicData(Reference.MOD_ID, "spell_speed4", 7, SpellItemType.AIR, 2, 2), "speed",
			new MageKnowledgeTableData(NeoOresSpells.spell_speed3, 6, 3, new ResourceLocation(Reference.MOD_ID, "speed"), NeoOres.neo_ores), SpellSpeed.class, 4);
	public static final SpellItem spell_continuation1 = new SpellItem(new BasicData(Reference.MOD_ID, "spell_continuation1", 4, SpellItemType.FIRE, 2, 2), "continuation",
			new MageKnowledgeTableData(NeoOresSpells.spell_bullet, 1, 2, new ResourceLocation(Reference.MOD_ID, "continuation"), NeoOres.neo_ores), SpellContinuation.class, 1);
	public static final SpellItem spell_continuation2 = new SpellItem(new BasicData(Reference.MOD_ID, "spell_continuation2", 5, SpellItemType.FIRE, 2, 2), "continuation",
			new MageKnowledgeTableData(NeoOresSpells.spell_continuation1, 2, 3, new ResourceLocation(Reference.MOD_ID, "continuation"), NeoOres.neo_ores), SpellContinuation.class, 2);
	public static final SpellItem spell_continuation3 = new SpellItem(new BasicData(Reference.MOD_ID, "spell_continuation3", 6, SpellItemType.FIRE, 2, 2), "continuation",
			new MageKnowledgeTableData(NeoOresSpells.spell_continuation2, 2, 4, new ResourceLocation(Reference.MOD_ID, "continuation"), NeoOres.neo_ores), SpellContinuation.class, 3);
	public static final SpellItem spell_continuation4 = new SpellItem(new BasicData(Reference.MOD_ID, "spell_continuation4", 7, SpellItemType.FIRE, 2, 2), "continuation",
			new MageKnowledgeTableData(NeoOresSpells.spell_continuation3, 3, 5, new ResourceLocation(Reference.MOD_ID, "continuation"), NeoOres.neo_ores), SpellContinuation.class, 4);
	public static final SpellItem spell_noGravity = new SpellItem(new BasicData(Reference.MOD_ID, "spell_noGravity", 8, SpellItemType.AIR, 100, 1), "no_gravity",
			new MageKnowledgeTableData(NeoOresSpells.spell_speed4, 7, 4, new ResourceLocation(Reference.MOD_ID, "no_gravity"), NeoOres.neo_ores), SpellNoGravity.class);
	public static final SpellItem spell_noAnyResistance = new SpellItem(new BasicData(Reference.MOD_ID, "spell_noAnyResistance", 8, SpellItemType.AIR, 100, 1), "no_resistance",
			new MageKnowledgeTableData(NeoOresSpells.spell_continuation4, 3, 6, new ResourceLocation(Reference.MOD_ID, "no_resistance"), NeoOres.neo_ores), SpellNoAnyResistance.class);
	public static final SpellItem spell_range1 = new SpellItem(new BasicData(Reference.MOD_ID, "range1", 2, SpellItemType.EARTH, 1, 9), "range",
			new MageKnowledgeTableData(NeoOresSpells.spell_place, 1, -2, new ResourceLocation(Reference.MOD_ID, "range"), NeoOres.neo_ores), SpellRange.class, 1);
	public static final SpellItem spell_range2 = new SpellItem(new BasicData(Reference.MOD_ID, "range2", 5, SpellItemType.EARTH, 1, 25), "range",
			new MageKnowledgeTableData(NeoOresSpells.spell_range1, 1, -3, new ResourceLocation(Reference.MOD_ID, "range"), NeoOres.neo_ores), SpellRange.class, 2);
	public static final SpellItem spell_range3 = new SpellItem(new BasicData(Reference.MOD_ID, "range3", 8, SpellItemType.EARTH, 1, 49), "range",
			new MageKnowledgeTableData(NeoOresSpells.spell_range2, 1, -4, new ResourceLocation(Reference.MOD_ID, "range"), NeoOres.neo_ores), SpellRange.class, 3);
	public static final SpellItem spell_range4 = new SpellItem(new BasicData(Reference.MOD_ID, "range4", 11, SpellItemType.EARTH, 1, 81), "range",
			new MageKnowledgeTableData(NeoOresSpells.spell_range3, 1, -5, new ResourceLocation(Reference.MOD_ID, "range"), NeoOres.neo_ores), SpellRange.class, 4);
	public static final SpellItem spell_no_inertia = new SpellItem(new BasicData(Reference.MOD_ID, "no_inertia", 1, SpellItemType.EARTH, 10, 1), "no_inertia",
			new MageKnowledgeTableData(NeoOresSpells.spell_bullet, 2, 2, new ResourceLocation(Reference.MOD_ID, "no_inertia"), NeoOres.neo_ores), SpellNoInertia.class);
	public static final SpellItem spell_reach1 = new SpellItem(new BasicData(Reference.MOD_ID, "reach1", 2, SpellItemType.AIR, 1, 2.0f), "reach",
			new MageKnowledgeTableData(NeoOresSpells.spell_place, 2, -1, new ResourceLocation(Reference.MOD_ID, "reach"), NeoOres.neo_ores), SpellReach.class, 1);
	public static final SpellItem spell_reach2 = new SpellItem(new BasicData(Reference.MOD_ID, "reach2", 4, SpellItemType.AIR, 1, 2.0f), "reach",
			new MageKnowledgeTableData(NeoOresSpells.spell_reach1, 3, -1, new ResourceLocation(Reference.MOD_ID, "reach"), NeoOres.neo_ores), SpellReach.class, 2);
	public static final SpellItem spell_reach3 = new SpellItem(new BasicData(Reference.MOD_ID, "reach3", 6, SpellItemType.AIR, 1, 2.0f), "reach",
			new MageKnowledgeTableData(NeoOresSpells.spell_reach2, 4, -1, new ResourceLocation(Reference.MOD_ID, "reach"), NeoOres.neo_ores), SpellReach.class, 3);
	public static final SpellItem spell_reach4 = new SpellItem(new BasicData(Reference.MOD_ID, "reach4", 8, SpellItemType.AIR, 1, 2.0f), "reach",
			new MageKnowledgeTableData(NeoOresSpells.spell_reach3, 5, -1, new ResourceLocation(Reference.MOD_ID, "reach"), NeoOres.neo_ores), SpellReach.class, 4);
	public static final SpellItem spell_life_ended = new SpellItem(new BasicData(Reference.MOD_ID, "life_ended", 10, SpellItemType.WATER, 10, 2.0f), "life_ended",
			new MageKnowledgeTableData(NeoOresSpells.spell_continuation1, 1, 3, new ResourceLocation(Reference.MOD_ID, "life_ended"), NeoOres.neo_ores), SpellVanished.class);
	public static final SpellItem spell_continuation_down1 = new SpellItem(new BasicData(Reference.MOD_ID, "continuation_down1", 2, SpellItemType.FIRE, 2, 1), "continuation_down",
			new MageKnowledgeTableData(NeoOresSpells.spell_life_ended, 1, 4, new ResourceLocation(Reference.MOD_ID, "continuation_down"), NeoOres.neo_ores), SpellContinuationDown.class, 1);
	public static final SpellItem spell_continuation_down2 = new SpellItem(new BasicData(Reference.MOD_ID, "continuation_down2", 4, SpellItemType.FIRE, 2, 1), "continuation_down",
			new MageKnowledgeTableData(NeoOresSpells.spell_continuation_down1, 2, 5, new ResourceLocation(Reference.MOD_ID, "continuation_down"), NeoOres.neo_ores), SpellContinuationDown.class, 2);
	public static final SpellItem spell_continuation_down3 = new SpellItem(new BasicData(Reference.MOD_ID, "continuation_down3", 6, SpellItemType.FIRE, 2, 1), "continuation_down",
			new MageKnowledgeTableData(NeoOresSpells.spell_continuation_down2, 2, 6, new ResourceLocation(Reference.MOD_ID, "continuation_down"), NeoOres.neo_ores), SpellContinuationDown.class, 3);
	public static final SpellItem spell_continuation_down4 = new SpellItem(new BasicData(Reference.MOD_ID, "continuation_down4", 8, SpellItemType.FIRE, 2, 1), "continuation_down",
			new MageKnowledgeTableData(NeoOresSpells.spell_continuation_down3, 3, 7, new ResourceLocation(Reference.MOD_ID, "continuation_down"), NeoOres.neo_ores), SpellContinuationDown.class, 4);
	public static final SpellItem spell_offset_up = new SpellItem(new BasicData(Reference.MOD_ID, "offset_up", 4, SpellItemType.EARTH, 10, 1), "offset_up",
			new MageKnowledgeTableData(NeoOresSpells.spell_self, -2, 1, new ResourceLocation(Reference.MOD_ID, "offset_up"), NeoOres.neo_ores), SpellOffsetUp.class);
	public static final SpellItem spell_offset_down = new SpellItem(new BasicData(Reference.MOD_ID, "offset_down", 4, SpellItemType.EARTH, 10, 1), "offset_down",
			new MageKnowledgeTableData(NeoOresSpells.spell_offset_up, -3, 2, new ResourceLocation(Reference.MOD_ID, "offset_down"), NeoOres.neo_ores), SpellOffsetDown.class);
	public static final SpellItem spell_worn_tick = new SpellItem(new BasicData(Reference.MOD_ID, "worn_tick", 5, SpellItemType.WATER, 0, 7), "worn_tick",
			new MageKnowledgeTableData(NeoOresSpells.spell_self, -1, 2, new ResourceLocation(Reference.MOD_ID, "worn_tick"), NeoOres.neo_ores), SpellWornTick.class);
	public static final SpellItem spell_light = new SpellItem(new BasicData(Reference.MOD_ID, "light", 1, SpellItemType.EARTH, 10, 1), "light",
			new MageKnowledgeTableData(NeoOresSpells.spell_place, 2, -2, new ResourceLocation(Reference.MOD_ID, "light"), NeoOres.neo_ores), SpellLight.class);
	public static final SpellItem spell_plantable = new SpellItem(new BasicData(Reference.MOD_ID, "plantable", 2, SpellItemType.EARTH, 2, 2.0f), "plantable",
			new MageKnowledgeTableData(NeoOresSpells.spell_light, 2, -3, new ResourceLocation(Reference.MOD_ID, "plantable"), NeoOres.neo_ores), SpellPlantable.class);
	public static final SpellItem spell_blacklist = new SpellItem(new BasicData(Reference.MOD_ID, "blacklist", 2, SpellItemType.AIR, 10, 1), "blacklist",
			new MageKnowledgeTableData(NeoOresSpells.spell_pipe_item, 6, 1, new ResourceLocation(Reference.MOD_ID, "blacklist"), NeoOres.neo_ores), SpellFilterBlackList.class);
	public static final SpellItem spell_whitelist = new SpellItem(new BasicData(Reference.MOD_ID, "whitelist", 2, SpellItemType.AIR, 10, 1), "whitelist",
			new MageKnowledgeTableData(NeoOresSpells.spell_blacklist, 7, 1, new ResourceLocation(Reference.MOD_ID, "whitelist"), NeoOres.neo_ores), SpellFilterWhiteList.class);
	public static final SpellItem spell_break_block = new SpellItem(new BasicData(Reference.MOD_ID, "break_block", 2, SpellItemType.EARTH, 5, 1), "break_block",
			new MageKnowledgeTableData(NeoOresSpells.spell_worn_tick, -1, 3, new ResourceLocation(Reference.MOD_ID, "break_block"), NeoOres.neo_ores), SpellBreaking.class);
	public static final SpellItem spell_attacking = new SpellItem(new BasicData(Reference.MOD_ID, "attacking", 2, SpellItemType.EARTH, 5, 1), "attacking",
			new MageKnowledgeTableData(NeoOresSpells.spell_break_block, -2, 4, new ResourceLocation(Reference.MOD_ID, "damage_given"), NeoOres.neo_ores), SpellAttacking.class);
	public static final SpellItem spell_jumped = new SpellItem(new BasicData(Reference.MOD_ID, "jumped", 4, SpellItemType.AIR, 10, 1), "jumped",
			new MageKnowledgeTableData(NeoOresSpells.spell_attacking, -2, 5, new ResourceLocation(Reference.MOD_ID, "jumped"), NeoOres.neo_ores), SpellJumped.class);
	public static final SpellItem spell_sneak = new SpellItem(new BasicData(Reference.MOD_ID, "sneak", 4, SpellItemType.AIR, 10, 1), "sneak",
			new MageKnowledgeTableData(NeoOresSpells.spell_jumped, -3, 6, new ResourceLocation(Reference.MOD_ID, "sneak"), NeoOres.neo_ores), SpellSneaking.class);
	public static final SpellItem spell_fall = new SpellItem(new BasicData(Reference.MOD_ID, "fall", 6, SpellItemType.AIR, 20, 1), "fall",
			new MageKnowledgeTableData(NeoOresSpells.spell_sneak, -3, 7, new ResourceLocation(Reference.MOD_ID, "fall"), NeoOres.neo_ores), SpellFallen.class);
	public static final SpellItem spell_death = new SpellItem(new BasicData(Reference.MOD_ID, "death", 6, SpellItemType.WATER, 20, 1), "death",
			new MageKnowledgeTableData(NeoOresSpells.spell_fall, -4, 8, new ResourceLocation(Reference.MOD_ID, "death"), NeoOres.neo_ores), SpellDead.class);
	public static final SpellItem spell_damaged = new SpellItem(new BasicData(Reference.MOD_ID, "damaged", 6, SpellItemType.WATER, 20, 1), "damaged",
			new MageKnowledgeTableData(NeoOresSpells.spell_death, -4, 9, new ResourceLocation(Reference.MOD_ID, "environmental_damage"), NeoOres.neo_ores), SpellDamaged.class);
	public static final SpellItem spell_attacked = new SpellItem(new BasicData(Reference.MOD_ID, "attacked", 4, SpellItemType.EARTH, 10, 1), "attacked",
			new MageKnowledgeTableData(NeoOresSpells.spell_damaged, -5, 10, new ResourceLocation(Reference.MOD_ID, "damage_taken"), NeoOres.neo_ores), SpellAttacked.class);
	public static final SpellItem spell_duration1 = new SpellItem(new BasicData(Reference.MOD_ID, "duration1", 1, SpellItemType.FIRE, 0, 2), "duration",
			new MageKnowledgeTableData(NeoOresSpells.spell_self, -2, 2, new ResourceLocation(Reference.MOD_ID, "duration"), NeoOres.neo_ores), SpellDuration.class, 1);
	public static final SpellItem spell_duration2 = new SpellItem(new BasicData(Reference.MOD_ID, "duration2", 2, SpellItemType.FIRE, 0, 2), "duration",
			new MageKnowledgeTableData(NeoOresSpells.spell_duration1, -3, 3, new ResourceLocation(Reference.MOD_ID, "duration"), NeoOres.neo_ores), SpellDuration.class, 2);
	public static final SpellItem spell_duration3 = new SpellItem(new BasicData(Reference.MOD_ID, "duration3", 3, SpellItemType.FIRE, 0, 2), "duration",
			new MageKnowledgeTableData(NeoOresSpells.spell_duration2, -4, 4, new ResourceLocation(Reference.MOD_ID, "duration"), NeoOres.neo_ores), SpellDuration.class, 3);
	public static final SpellItem spell_duration4 = new SpellItem(new BasicData(Reference.MOD_ID, "duration4", 4, SpellItemType.FIRE, 0, 2), "duration",
			new MageKnowledgeTableData(NeoOresSpells.spell_duration3, -5, 5, new ResourceLocation(Reference.MOD_ID, "duration"), NeoOres.neo_ores), SpellDuration.class, 4);
	public static final SpellItem spell_duration5 = new SpellItem(new BasicData(Reference.MOD_ID, "duration5", 5, SpellItemType.FIRE, 0, 2), "duration",
			new MageKnowledgeTableData(NeoOresSpells.spell_duration4, -6, 6, new ResourceLocation(Reference.MOD_ID, "duration"), NeoOres.neo_ores), SpellDuration.class, 5);
	public static final SpellItem spell_duration6 = new SpellItem(new BasicData(Reference.MOD_ID, "duration6", 6, SpellItemType.FIRE, 0, 2), "duration",
			new MageKnowledgeTableData(NeoOresSpells.spell_duration5, -7, 7, new ResourceLocation(Reference.MOD_ID, "duration"), NeoOres.neo_ores), SpellDuration.class, 6);
	public static final SpellItem spell_duration7 = new SpellItem(new BasicData(Reference.MOD_ID, "duration7", 7, SpellItemType.FIRE, 0, 2), "duration",
			new MageKnowledgeTableData(NeoOresSpells.spell_duration6, -8, 8, new ResourceLocation(Reference.MOD_ID, "duration"), NeoOres.neo_ores), SpellDuration.class, 7);
	public static final SpellItem spell_duration8 = new SpellItem(new BasicData(Reference.MOD_ID, "duration8", 8, SpellItemType.FIRE, 0, 2), "duration",
			new MageKnowledgeTableData(NeoOresSpells.spell_duration7, -9, 9, new ResourceLocation(Reference.MOD_ID, "duration"), NeoOres.neo_ores), SpellDuration.class, 8);
	public static final SpellItem spell_duration9 = new SpellItem(new BasicData(Reference.MOD_ID, "duration9", 9, SpellItemType.FIRE, 0, 2), "duration",
			new MageKnowledgeTableData(NeoOresSpells.spell_duration8, -10, 10, new ResourceLocation(Reference.MOD_ID, "duration"), NeoOres.neo_ores), SpellDuration.class, 9);
	public static final SpellItem spell_duration10 = new SpellItem(new BasicData(Reference.MOD_ID, "duration10", 10, SpellItemType.FIRE, 0, 2), "duration",
			new MageKnowledgeTableData(NeoOresSpells.spell_duration9, -11, 11, new ResourceLocation(Reference.MOD_ID, "duration"), NeoOres.neo_ores), SpellDuration.class, 10);
	public static final SpellItem spell_duration11 = new SpellItem(new BasicData(Reference.MOD_ID, "duration11", 11, SpellItemType.FIRE, 0, 2), "duration",
			new MageKnowledgeTableData(NeoOresSpells.spell_duration10, -12, 12, new ResourceLocation(Reference.MOD_ID, "duration"), NeoOres.neo_ores), SpellDuration.class, 11);
	public static final SpellItem spell_amplify1 = new SpellItem(new BasicData(Reference.MOD_ID, "amplify1", 3, SpellItemType.WATER, 0, 8), "amplify",
			new MageKnowledgeTableData(NeoOresSpells.spell_duration2, -4, 3, new ResourceLocation(Reference.MOD_ID, "amplify"), NeoOres.neo_ores), SpellAmplify.class, 1);
	public static final SpellItem spell_amplify2 = new SpellItem(new BasicData(Reference.MOD_ID, "amplify2", 6, SpellItemType.WATER, 0, 8), "amplify",
			new MageKnowledgeTableData(NeoOresSpells.spell_amplify1, -5, 3, new ResourceLocation(Reference.MOD_ID, "amplify"), NeoOres.neo_ores), SpellAmplify.class, 2);
	public static final SpellItem spell_amplify3 = new SpellItem(new BasicData(Reference.MOD_ID, "amplify3", 9, SpellItemType.WATER, 0, 8), "amplify",
			new MageKnowledgeTableData(NeoOresSpells.spell_amplify2, -6, 3, new ResourceLocation(Reference.MOD_ID, "amplify"), NeoOres.neo_ores), SpellAmplify.class, 3);
	public static final SpellItem spell_heal = new SpellItem(new BasicData(Reference.MOD_ID, "heal", 1, SpellItemType.WATER, 10, 1), "heal",
			new MageKnowledgeTableData(NeoOresSpells.spell_offset_up, -3, 1, new ResourceLocation(Reference.MOD_ID, "heal"), NeoOres.neo_ores), SpellHeal.class);
	public static final SpellItem spell_life_tap = new SpellItem(new BasicData(Reference.MOD_ID, "life_tap", 1, SpellItemType.WATER, 0, 1), "life_tap",
			new MageKnowledgeTableData(NeoOresSpells.spell_heal, -4, 2, new ResourceLocation(Reference.MOD_ID, "life_tap"), NeoOres.neo_ores), SpellLifeTap.class);
	public static final SpellItem spell_sunny = new SpellItem(new BasicData(Reference.MOD_ID, "sunny", 8, SpellItemType.AIR, 1000, 1), "sunny",
			new MageKnowledgeTableData(NeoOresSpells.spell_light, 3, -2, new ResourceLocation(Reference.MOD_ID, "sunny"), NeoOres.neo_ores), SpellSunny.class);
	public static final SpellItem spell_rain = new SpellItem(new BasicData(Reference.MOD_ID, "rain", 8, SpellItemType.AIR, 1000, 1), "rain",
			new MageKnowledgeTableData(NeoOresSpells.spell_sunny, 4, -2, new ResourceLocation(Reference.MOD_ID, "rain"), NeoOres.neo_ores), SpellRain.class);
	public static final SpellItem spell_thunder = new SpellItem(new BasicData(Reference.MOD_ID, "thunder", 8, SpellItemType.AIR, 1000, 1), "thunder",
			new MageKnowledgeTableData(NeoOresSpells.spell_rain, 5, -2, new ResourceLocation(Reference.MOD_ID, "thunder"), NeoOres.neo_ores), SpellThunder.class);
	public static final SpellItem spell_lightning = new SpellItem(new BasicData(Reference.MOD_ID, "lightning", 4, SpellItemType.EARTH, 1000, 1), "lightning",
			new MageKnowledgeTableData(NeoOresSpells.spell_thunder, 6, -3, new ResourceLocation(Reference.MOD_ID, "lightning_bolt"), NeoOres.neo_ores), SpellLightningBolt.class);
	public static final SpellItem spell_grow = new SpellItem(new BasicData(Reference.MOD_ID, "grow", 1, SpellItemType.EARTH, 50, 1), "grow",
			new MageKnowledgeTableData(NeoOresSpells.spell_silk, -2, -1, new ResourceLocation(Reference.MOD_ID, "grow"), NeoOres.neo_ores), SpellGrow.class);
	public static final SpellItem spell_summon = new SpellItem(new BasicData(Reference.MOD_ID, "summon", 5, SpellItemType.EARTH, 100, 10), "summon",
			new MageKnowledgeTableData(NeoOresSpells.spell_grow, -3, -2, new ResourceLocation(Reference.MOD_ID, "summon"), NeoOres.neo_ores), SpellSummon.class);
	public static final SpellItem spell_nbt_applying = new SpellItem(new BasicData(Reference.MOD_ID, "nbt_apply", 11, SpellItemType.WATER, 1, 10000), "nbt_apply",
			new MageKnowledgeTableData(NeoOresSpells.spell_summon, -4, -2, new ResourceLocation(Reference.MOD_ID, "nbt_apply"), NeoOres.neo_ores), SpellCanApplyNBT.class);
	public static final SpellItem spell_discombine = new SpellItem(new BasicData(Reference.MOD_ID, "discombine", 1, SpellItemType.EARTH, 100, 1), "discombine",
			new MageKnowledgeTableData(NeoOresSpells.spell_silk, -2, -2, new ResourceLocation(Reference.MOD_ID, "discombine"), NeoOres.neo_ores), SpellDiscombine.class);
	public static final SpellItem spell_blink = new SpellItem(new BasicData(Reference.MOD_ID, "blink", 3, SpellItemType.AIR, 20, 1), "blink",
			new MageKnowledgeTableData(NeoOresSpells.spell_no_inertia, 3, 3, new ResourceLocation(Reference.MOD_ID, "blink"), NeoOres.neo_ores), SpellBlink.class);
	public static final SpellItem spell_teleport = new SpellItem(new BasicData(Reference.MOD_ID, "teleport", 6, SpellItemType.FIRE, 50, 1), "teleport",
			new MageKnowledgeTableData(NeoOresSpells.spell_blink, 4, 4, new ResourceLocation(Reference.MOD_ID, "teleport"), NeoOres.neo_ores), SpellTeleport.class);
	public static final SpellItem spell_translocate = new SpellItem(new BasicData(Reference.MOD_ID, "translocate", 9, SpellItemType.FIRE, 1000, 1), "translocate",
			new MageKnowledgeTableData(NeoOresSpells.spell_teleport, 5, 5, new ResourceLocation(Reference.MOD_ID, "translocate"), NeoOres.neo_ores), SpellTranslocate.class);
	public static final SpellItem spell_dimension_over = new SpellItem(new BasicData(Reference.MOD_ID, "dimension_over", 11, SpellItemType.EARTH, 1, 10), "dimension_over",
			new MageKnowledgeTableData(NeoOresSpells.spell_translocate, 6, 6, new ResourceLocation(Reference.MOD_ID, "dimension_over"), NeoOres.neo_ores), SpellDimensionOver.class);
	public static final SpellItem spell_placeable = new SpellItem(new BasicData(Reference.MOD_ID, "placeable", 6, SpellItemType.FIRE, 0, 10.0F), "placeable",
			new MageKnowledgeTableData(NeoOresSpells.spell_duration1, -2, 3, new ResourceLocation(Reference.MOD_ID, "placeable"), NeoOres.neo_ores), SpellPlaceable.class);
	public static final SpellItem spell_disarm = new SpellItem(new BasicData(Reference.MOD_ID, "disarm", 4, SpellItemType.WATER, 30, 1), "disarm",
			new MageKnowledgeTableData(NeoOresSpells.spell_amplify1, -5, 2, new ResourceLocation(Reference.MOD_ID, "disarm"), NeoOres.neo_ores), SpellDisarm.class);
	public static final SpellItem spell_day = new SpellItem(new BasicData(Reference.MOD_ID, "day", 11, SpellItemType.FIRE, 10000, 1), "day",
			new MageKnowledgeTableData(NeoOresSpells.spell_translocate, 5, 6, new ResourceLocation(Reference.MOD_ID, "day"), NeoOres.neo_ores), SpellDay.class);
	public static final SpellItem spell_night = new SpellItem(new BasicData(Reference.MOD_ID, "night", 11, SpellItemType.FIRE, 10000, 1), "night",
			new MageKnowledgeTableData(NeoOresSpells.spell_translocate, 6, 5, new ResourceLocation(Reference.MOD_ID, "night"), NeoOres.neo_ores), SpellNight.class);
	
	public static final List<SpellItem> registry = Arrays.asList(spell_touch, spell_dig, spell_support_liquid, spell_composition, spell_earth_damage, spell_harvestLv1, spell_harvestLv2,
			spell_harvestLv3, spell_harvestLv4, spell_harvestLv5, spell_harvestLv6, spell_harvestLv7, spell_harvestLv8, spell_harvestLv9, spell_harvestLv10, spell_harvestLv11, spell_luck1,
			spell_luck2, spell_luck3, spell_luck4, spell_luck5, spell_luck6, spell_luck7, spell_luck8, spell_luck9, spell_luck10, spell_silk, spell_tier1, spell_tier2, spell_tier3, spell_tier4,
			spell_tier5, spell_tier6, spell_tier7, spell_tier8, spell_tier9, spell_tier10, spell_tier11, spell_bullet, spell_speed1, spell_speed2, spell_speed3, spell_speed4, spell_continuation1,
			spell_continuation2, spell_continuation3, spell_continuation4, spell_noGravity, spell_noAnyResistance, spell_gather, spell_range1, spell_range2, spell_range3, spell_range4,
			spell_damageLv1, spell_damageLv2, spell_damageLv3, spell_damageLv4, spell_damageLv5, spell_damageLv6, spell_damageLv7, spell_damageLv8, spell_damageLv9, spell_damageLv10, spell_damageLv11,
			spell_summon, spell_nbt_applying, spell_ore_gen, spell_no_inertia, spell_pull_item, spell_uncollidable, spell_push_item, spell_pipe_item, spell_reach1, spell_reach2, spell_reach3,
			spell_reach4, spell_life_ended, spell_continuation_down1, spell_continuation_down2, spell_continuation_down3, spell_continuation_down4, spell_place, spell_offset_up, spell_offset_down,
			spell_worn_tick, spell_damaged, spell_plantable, spell_blacklist, spell_whitelist, spell_self, spell_attacked, spell_attacking, spell_dimension_over, spell_discombine, spell_placeable,
			spell_blink, spell_teleport, spell_translocate, spell_light, spell_disarm, spell_sneak, spell_break_block, spell_fall, spell_death, spell_jumped, spell_amplify1, spell_amplify2,
			spell_amplify3, spell_duration1, spell_duration2, spell_duration3, spell_duration4, spell_duration5, spell_duration6, spell_duration7, spell_duration8, spell_duration9, spell_duration10,
			spell_duration11, spell_heal, spell_life_tap, spell_sunny, spell_rain, spell_thunder, spell_lightning, spell_grow, spell_day, spell_night);
}
