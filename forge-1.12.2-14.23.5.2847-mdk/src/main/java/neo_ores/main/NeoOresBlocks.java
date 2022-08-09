package neo_ores.main;

import java.util.Arrays;
import java.util.List;

import neo_ores.block.BlockAerite;
import neo_ores.block.BlockAirEssence;
import neo_ores.block.BlockDimension;
import neo_ores.block.BlockDrenite;
import neo_ores.block.BlockEarthEssence;
import neo_ores.block.BlockEnhancedPedestal;
import neo_ores.block.BlockFireEssence;
import neo_ores.block.BlockFlamite;
import neo_ores.block.BlockForcite;
import neo_ores.block.BlockGnomite;
import neo_ores.block.BlockGuardite;
import neo_ores.block.BlockLandite;
import neo_ores.block.BlockMageKnowledgeTable;
import neo_ores.block.BlockMana;
import neo_ores.block.BlockManaFurnace;
import neo_ores.block.BlockManaWorkbench;
import neo_ores.block.BlockMarlite;
import neo_ores.block.BlockNeoOre;
import neo_ores.block.BlockNeoOreOriginal;
import neo_ores.block.BlockNeoOresPortal;
import neo_ores.block.BlockPedestal;
import neo_ores.block.BlockSalamite;
import neo_ores.block.BlockSanitite;
import neo_ores.block.BlockSpellRecipeCreationTable;
import neo_ores.block.BlockSylphite;
import neo_ores.block.BlockUndite;
import neo_ores.block.BlockWaterEssence;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;

public class NeoOresBlocks 
{
	public static final Block mana_workbench = new BlockManaWorkbench()
			.setRegistryName(Reference.MOD_ID,"mana_workbench")
			.setUnlocalizedName("mana_workbench")
			.setCreativeTab(NeoOres.neo_ores_tab);	
	public static final Block mana_furnace = new BlockManaFurnace(false)
			.setRegistryName(Reference.MOD_ID,"mana_furnace")
			.setUnlocalizedName("mana_furnace")
			.setCreativeTab(NeoOres.neo_ores_tab);	
	public static final Block mage_knowledge_table = new BlockMageKnowledgeTable()
			.setRegistryName(Reference.MOD_ID,"mage_knowledge_table")
			.setUnlocalizedName("mage_knowledge_table")
			.setCreativeTab(NeoOres.neo_ores_tab);	
	public static final Block lit_mana_furnace = new BlockManaFurnace(true)
			.setRegistryName(Reference.MOD_ID,"lit_mana_furnace")
			.setUnlocalizedName("mana_furnace")
			.setCreativeTab(null);
	public static final Block spell_recipe_creation_table = new BlockSpellRecipeCreationTable()
			.setRegistryName(Reference.MOD_ID,"spell_recipe_creation_table")
			.setUnlocalizedName("spell_recipe_creation_table")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block mana_block = new BlockMana()
			.setRegistryName(Reference.MOD_ID,"mana_block")
			.setUnlocalizedName("mana_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block pedestal = new BlockPedestal(false)
			.setRegistryName(Reference.MOD_ID,"pedestal")
			.setUnlocalizedName("pedestal")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block pedestal_water = new BlockPedestal(true)
			.setRegistryName(Reference.MOD_ID,"pedestal_water")
			.setUnlocalizedName("pedestal_water")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block enhanced_pedestal = new BlockEnhancedPedestal()
			.setRegistryName(Reference.MOD_ID,"enhanced_pedestal")
			.setUnlocalizedName("enhanced_pedestal")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block earth_portal = new BlockNeoOresPortal(0)
			.setRegistryName(Reference.MOD_ID,"earth_portal")
			.setUnlocalizedName("earth_portal")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block water_portal = new BlockNeoOresPortal(1)
			.setRegistryName(Reference.MOD_ID,"water_portal")
			.setUnlocalizedName("water_portal")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block air_portal = new BlockNeoOresPortal(2)
			.setRegistryName(Reference.MOD_ID,"air_portal")
			.setUnlocalizedName("air_portal")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block fire_portal = new BlockNeoOresPortal(3)
			.setRegistryName(Reference.MOD_ID,"fire_portal")
			.setUnlocalizedName("fire_portal")
			.setCreativeTab(NeoOres.neo_ores_tab);

	public static final Block aerite_block = new BlockAerite()
			.setRegistryName(Reference.MOD_ID,"aerite_block")
			.setUnlocalizedName("aerite_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block drenite_block = new BlockDrenite()
			.setRegistryName(Reference.MOD_ID,"drenite_block")
			.setUnlocalizedName("drenite_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block flamite_block = new BlockFlamite()
			.setRegistryName(Reference.MOD_ID,"flamite_block")
			.setUnlocalizedName("flamite_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block forcite_block = new BlockForcite()
			.setRegistryName(Reference.MOD_ID,"forcite_block")
			.setUnlocalizedName("forcite_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block gnomite_block = new BlockGnomite()
			.setRegistryName(Reference.MOD_ID,"gnomite_block")
			.setUnlocalizedName("gnomite_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block guardite_block = new BlockGuardite()
			.setRegistryName(Reference.MOD_ID,"guardite_block")
			.setUnlocalizedName("guardite_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block landite_block = new BlockLandite()
			.setRegistryName(Reference.MOD_ID,"landite_block")
			.setUnlocalizedName("landite_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block marlite_block = new BlockMarlite()
			.setRegistryName(Reference.MOD_ID,"marlite_block")
			.setUnlocalizedName("marlite_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block salamite_block = new BlockSalamite()
			.setRegistryName(Reference.MOD_ID,"salamite_block")
			.setUnlocalizedName("salamite_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block sanitite_block = new BlockSanitite()
			.setRegistryName(Reference.MOD_ID,"sanitite_block")
			.setUnlocalizedName("sanitite_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block sylphite_block = new BlockSylphite()
			.setRegistryName(Reference.MOD_ID,"sylphite_block")
			.setUnlocalizedName("sylphite_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block undite_block = new BlockUndite()
			.setRegistryName(Reference.MOD_ID,"undite_block")
			.setUnlocalizedName("undite_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block air_essence_block = new BlockAirEssence()
			.setRegistryName(Reference.MOD_ID,"air_essence_block")
			.setUnlocalizedName("air_essence_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block earth_essence_block = new BlockEarthEssence()
			.setRegistryName(Reference.MOD_ID,"earth_essence_block")
			.setUnlocalizedName("earth_essence_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block fire_essence_block = new BlockFireEssence()
			.setRegistryName(Reference.MOD_ID,"fire_essence_block")
			.setUnlocalizedName("fire_essence_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block water_essence_block = new BlockWaterEssence()
			.setRegistryName(Reference.MOD_ID,"water_essence_block")
			.setUnlocalizedName("water_essence_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block custom_lit_redstone_ore = new BlockNeoOre("custom_lit_redstone_ore",2,null, 0.625F, false, Items.REDSTONE,0, 4, 5, 1, 5).setCreativeTab(null);
	public static final Block custom_redstone_ore = new BlockNeoOre("custom_redstone_ore",2,null, 0.0F, false, Items.REDSTONE,0, 4, 5, 1, 5).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block custom_coal_ore = new BlockNeoOre("custom_coal_ore",0,null, 0.0F, false, Items.COAL,0, 1, 1, 0, 2).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block custom_diamond_ore = new BlockNeoOre("custom_diamond_ore",2,null, 0.0F, false, Items.DIAMOND,0, 1, 1, 3, 7).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block custom_emerald_ore = new BlockNeoOre("custom_emerald_ore",2,null, 0.0F, false, Items.EMERALD,0, 1, 1, 3, 7).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block custom_gold_ore = new BlockNeoOre("custom_gold_ore",2,null, 0.0F, true, null,0, 1, 1, 0, 0).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block custom_iron_ore = new BlockNeoOre("custom_iron_ore",1,null, 0.0F, true, null,0, 1, 1, 0, 0).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block custom_lapis_ore = new BlockNeoOre("custom_lapis_ore",1,null, 0.0F, false, Items.DYE,EnumDyeColor.BLUE.getDyeDamage(), 4, 8, 2, 5).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block custom_quartz_ore = new BlockNeoOre("custom_quartz_ore",0,null, 0.0F, false, Items.QUARTZ,0, 1, 1, 2, 5).setCreativeTab(NeoOres.neo_ores_tab);
	
	public static final Block landite_ore = new BlockNeoOreOriginal("landite_ore",3,DimensionName.EARTH, 0.0F, true, null,0, 1, 1, 0, 0).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block marlite_ore = new BlockNeoOreOriginal("marlite_ore",5,DimensionName.WATER, 0.0F, true, null,0, 1, 1, 0, 0).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block drenite_ore = new BlockNeoOreOriginal("drenite_ore",7,DimensionName.AIR, 0.0F, false, NeoOresItems.drenite,0, 1, 1, 6, 13).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block forcite_ore = new BlockNeoOreOriginal("forcite_ore",9,DimensionName.FIRE, 0.0F, false, NeoOresItems.forcite,0, 1, 1, 7, 15).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block guardite_ore = new BlockNeoOreOriginal("guardite_ore",3,DimensionName.EARTH, 0.0F, true, null,0, 1, 1, 0, 0).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block sanitite_ore = new BlockNeoOreOriginal("sanitite_ore",5,DimensionName.WATER, 0.0F, false, NeoOresItems.sanitite,0, 1, 1, 9, 19).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block aerite_ore = new BlockNeoOreOriginal("aerite_ore",7,DimensionName.AIR, 0.0F, false, NeoOresItems.aerite,0, 1, 1, 10, 21).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block flamite_ore = new BlockNeoOreOriginal("flamite_ore",9,DimensionName.FIRE, 0.0F, false, NeoOresItems.flamite,0, 1, 1, 11, 23).setCreativeTab(NeoOres.neo_ores_tab);
	
	public static final Block dim_stone = new BlockDimension("dim_stone", Material.ROCK, 1.5F, 10.0F, "pickaxe", 0, 0.0F, SoundType.STONE).setCreativeTab(NeoOres.neo_ores_tab);
	
	public static final List<Block> registry = Arrays.asList(
			mana_workbench,
			mana_furnace,
			mage_knowledge_table,
			lit_mana_furnace,
			spell_recipe_creation_table,
			mana_block,
			pedestal,
			pedestal_water,
			enhanced_pedestal,
			air_portal,
			earth_portal,
			fire_portal,
			water_portal,
			aerite_block,
			drenite_block,
			forcite_block,
			flamite_block,
			gnomite_block,
			guardite_block,
			landite_block,
			marlite_block,
			sanitite_block,
			salamite_block,
			sylphite_block,
			undite_block,
			air_essence_block,
			earth_essence_block,
			fire_essence_block,
			water_essence_block,
			custom_redstone_ore,
			custom_lit_redstone_ore,
			custom_coal_ore,
			custom_diamond_ore,
			custom_emerald_ore,
			custom_gold_ore,
			custom_iron_ore,
			custom_lapis_ore,
			custom_quartz_ore,
			landite_ore,
			marlite_ore,
			drenite_ore,
			forcite_ore,
			guardite_ore,
			sanitite_ore,
			aerite_ore,
			flamite_ore,
			dim_stone
			);	
}
