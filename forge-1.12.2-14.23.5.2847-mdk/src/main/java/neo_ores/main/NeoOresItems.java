package neo_ores.main;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import neo_ores.item.INeoOresItem;
import neo_ores.item.ItemEffected;
import neo_ores.item.ItemEssence;
import neo_ores.item.ItemEssenceCoreAir;
import neo_ores.item.ItemEssenceCoreEarth;
import neo_ores.item.ItemEssenceCoreFire;
import neo_ores.item.ItemEssenceCoreWater;
import neo_ores.item.ItemManaWrench;
import neo_ores.item.ItemMobBottle;
import neo_ores.item.ItemNeoArmor;
import neo_ores.item.ItemNeoAxe;
import neo_ores.item.ItemNeoHoe;
import neo_ores.item.ItemNeoPaxel;
import neo_ores.item.ItemNeoPickaxe;
import neo_ores.item.ItemNeoSpade;
import neo_ores.item.ItemNeoSword;
import neo_ores.item.ItemRecipeSheet;
import neo_ores.item.ItemSpell;
import neo_ores.item.ItemSpellSheet;
import neo_ores.item.ItemTotem;
import neo_ores.item.ItemUpgrade;
import neo_ores.tileentity.TileEntityMechanicalMagician;
import neo_ores.world.dimension.DimensionHelper.ToolType;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NeoOresItems
{
	public static final ToolMaterial toolUndite = EnumHelper.addToolMaterial("tool_undite", 12, 2601, 14.0F, 4.1F, 10).setRepairItem(new ItemStack(NeoOresItems.undite));
	public static final ToolMaterial toolSalamite = EnumHelper.addToolMaterial("tool_salamite", 16, 1820, 9.8F, 12.0F, 7).setRepairItem(new ItemStack(NeoOresItems.salamite));
	public static final ToolMaterial toolSylphite = EnumHelper.addToolMaterial("tool_sylphite", 14, 1274, 6.9F, 8.4F, 20).setRepairItem(new ItemStack(NeoOresItems.sylphite));
	public static final ToolMaterial toolGnomite = EnumHelper.addToolMaterial("tool_gnomite", 10, 892, 20.0F, 5.9F, 14).setRepairItem(new ItemStack(NeoOresItems.gnomite_ingot));
	public static final ToolMaterial toolCreative = EnumHelper.addToolMaterial("tool_creative", Integer.MAX_VALUE, Integer.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, (int) Byte.MAX_VALUE)
			.setRepairItem(new ItemStack(NeoOresBlocks.mana_block));

	public static final ArmorMaterial armorUndite = EnumHelper.addArmorMaterial("armor_undite", "neo_ores:undite", 29, new int[] { 5, 8, 10, 6 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 4.0F)
			.setRepairItem(new ItemStack(NeoOresItems.undite));
	public static final ArmorMaterial armorSalamite = EnumHelper.addArmorMaterial("armor_salamite", "neo_ores:salamite", 23, new int[] { 3, 7, 9, 4 }, 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.2F)
			.setRepairItem(new ItemStack(NeoOresItems.salamite));
	public static final ArmorMaterial armorSylphite = EnumHelper.addArmorMaterial("armor_sylphite", "neo_ores:sylphite", 45, new int[] { 2, 6, 7, 3 }, 16, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.6F)
			.setRepairItem(new ItemStack(NeoOresItems.sylphite));
	public static final ArmorMaterial armorGnomite = EnumHelper.addArmorMaterial("armor_gnomite", "neo_ores:gnomite", 36, new int[] { 6, 11, 13, 6 }, 13, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F)
			.setRepairItem(new ItemStack(NeoOresItems.gnomite_ingot));
	public static final ArmorMaterial armorCreative = EnumHelper.addArmorMaterial("armor_creative", "neo_ores:creative", Integer.MAX_VALUE,
			new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE }, (int) Byte.MAX_VALUE, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, Float.MAX_VALUE)
			.setRepairItem(new ItemStack(NeoOresBlocks.mana_block));

	public static final Item undite = new INeoOresItem.Impl().setRegistryName(Reference.MOD_ID, "undite").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("undite");
	public static final Item gnomite_ingot = new INeoOresItem.Impl().setRegistryName(Reference.MOD_ID, "gnomite_ingot").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("gnomite_ingot");
	public static final Item salamite = new INeoOresItem.Impl().setRegistryName(Reference.MOD_ID, "salamite").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("salamite");
	public static final Item sylphite = new INeoOresItem.Impl().setRegistryName(Reference.MOD_ID, "sylphite").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("sylphite");
	public static final Item mana_ingot = new ItemEffected().setRegistryName(Reference.MOD_ID, "mana_ingot").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("mana_ingot");
	public static final Item essence = new ItemEssence().setRegistryName(Reference.MOD_ID, "essence").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("essence");
	public static final Item air_essence_core = new ItemEssenceCoreAir().setRegistryName(Reference.MOD_ID, "air_essence_core").setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("air_essence_core");
	public static final Item earth_essence_core = new ItemEssenceCoreEarth().setRegistryName(Reference.MOD_ID, "earth_essence_core").setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("earth_essence_core");
	public static final Item fire_essence_core = new ItemEssenceCoreFire().setRegistryName(Reference.MOD_ID, "fire_essence_core").setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("fire_essence_core");
	public static final Item water_essence_core = new ItemEssenceCoreWater().setRegistryName(Reference.MOD_ID, "water_essence_core").setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("water_essence_core");
	public static final Item sanitite = new INeoOresItem.Impl().setRegistryName(Reference.MOD_ID, "sanitite").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("sanitite");
	public static final Item marlite_ingot = new INeoOresItem.Impl().setRegistryName(Reference.MOD_ID, "marlite_ingot").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("marlite_ingot");
	public static final Item aerite = new INeoOresItem.Impl().setRegistryName(Reference.MOD_ID, "aerite").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("aerite");
	public static final Item drenite = new INeoOresItem.Impl().setRegistryName(Reference.MOD_ID, "drenite").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("drenite");
	public static final Item guardite_ingot = new INeoOresItem.Impl().setRegistryName(Reference.MOD_ID, "guardite_ingot").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("guardite_ingot");
	public static final Item landite_ingot = new INeoOresItem.Impl().setRegistryName(Reference.MOD_ID, "landite_ingot").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("landite_ingot");
	public static final Item forcite = new INeoOresItem.Impl().setRegistryName(Reference.MOD_ID, "forcite").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("forcite");
	public static final Item flamite = new INeoOresItem.Impl().setRegistryName(Reference.MOD_ID, "flamite").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("flamite");
	public static final Item spell = new ItemSpell() // Not INeoOresItem
			.setRegistryName(Reference.MOD_ID, "spell").setUnlocalizedName("spell");
	public static final Item mana_wrench = new ItemManaWrench().setRegistryName(Reference.MOD_ID, "mana_wrench").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("mana_wrench");
	public static final Item recipe_sheet = new ItemRecipeSheet().setRegistryName(Reference.MOD_ID, "recipe_sheet").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("recipe_sheet");
	public static final Item spell_sheet = new ItemSpellSheet().setRegistryName(Reference.MOD_ID, "spell_sheet").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("spell_sheet");

	public static final Item mob_bottle = new ItemMobBottle(false).setRegistryName(Reference.MOD_ID, "mob_bottle").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("mob_bottle");

	public static final Item mob_bottle_master = new ItemMobBottle(true).setRegistryName(Reference.MOD_ID, "mob_bottle_boss").setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("mob_bottle_boss");
	public static final Item undite_axe = new ItemNeoAxe(NeoOresItems.toolUndite).setToolType(ToolType.WATER).setRegistryName(Reference.MOD_ID, "undite_axe").setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("undite_axe");
	public static final Item undite_hoe = new ItemNeoHoe(NeoOresItems.toolUndite).setToolType(ToolType.WATER).setRegistryName(Reference.MOD_ID, "undite_hoe").setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("undite_hoe");
	public static final Item undite_paxel = new ItemNeoPaxel(NeoOresItems.toolUndite).setToolType(ToolType.WATER).setRegistryName(Reference.MOD_ID, "undite_paxel").setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("undite_paxel");
	public static final Item undite_pickaxe = new ItemNeoPickaxe(NeoOresItems.toolUndite).setToolType(ToolType.WATER).setRegistryName(Reference.MOD_ID, "undite_pickaxe")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("undite_pickaxe");
	public static final Item undite_shovel = new ItemNeoSpade(NeoOresItems.toolUndite).setToolType(ToolType.WATER).setRegistryName(Reference.MOD_ID, "undite_shovel")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("undite_shovel");
	public static final Item undite_sword = new ItemNeoSword(NeoOresItems.toolUndite).setToolType(ToolType.WATER).setRegistryName(Reference.MOD_ID, "undite_sword").setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("undite_sword");
	public static final Item salamite_axe = new ItemNeoAxe(NeoOresItems.toolSalamite).setToolType(ToolType.FIRE).setRegistryName(Reference.MOD_ID, "salamite_axe").setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("salamite_axe");
	public static final Item salamite_hoe = new ItemNeoHoe(NeoOresItems.toolSalamite).setToolType(ToolType.FIRE).setRegistryName(Reference.MOD_ID, "salamite_hoe").setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("salamite_hoe");
	public static final Item salamite_paxel = new ItemNeoPaxel(NeoOresItems.toolSalamite).setToolType(ToolType.FIRE).setRegistryName(Reference.MOD_ID, "salamite_paxel")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("salamite_paxel");
	public static final Item salamite_pickaxe = new ItemNeoPickaxe(NeoOresItems.toolSalamite).setToolType(ToolType.FIRE).setRegistryName(Reference.MOD_ID, "salamite_pickaxe")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("salamite_pickaxe");
	public static final Item salamite_shovel = new ItemNeoSpade(NeoOresItems.toolSalamite).setToolType(ToolType.FIRE).setRegistryName(Reference.MOD_ID, "salamite_shovel")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("salamite_shovel");
	public static final Item salamite_sword = new ItemNeoSword(NeoOresItems.toolSalamite).setToolType(ToolType.FIRE).setRegistryName(Reference.MOD_ID, "salamite_sword")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("salamite_sword");
	public static final Item gnomite_axe = new ItemNeoAxe(NeoOresItems.toolGnomite).setToolType(ToolType.EARTH).setRegistryName(Reference.MOD_ID, "gnomite_axe").setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("gnomite_axe");
	public static final Item gnomite_hoe = new ItemNeoHoe(NeoOresItems.toolGnomite).setToolType(ToolType.EARTH).setRegistryName(Reference.MOD_ID, "gnomite_hoe").setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("gnomite_hoe");
	public static final Item gnomite_paxel = new ItemNeoPaxel(NeoOresItems.toolGnomite).setToolType(ToolType.EARTH).setRegistryName(Reference.MOD_ID, "gnomite_paxel")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("gnomite_paxel");
	public static final Item gnomite_pickaxe = new ItemNeoPickaxe(NeoOresItems.toolGnomite).setToolType(ToolType.EARTH).setRegistryName(Reference.MOD_ID, "gnomite_pickaxe")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("gnomite_pickaxe");
	public static final Item gnomite_shovel = new ItemNeoSpade(NeoOresItems.toolGnomite).setToolType(ToolType.EARTH).setRegistryName(Reference.MOD_ID, "gnomite_shovel")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("gnomite_shovel");
	public static final Item gnomite_sword = new ItemNeoSword(NeoOresItems.toolGnomite).setToolType(ToolType.EARTH).setRegistryName(Reference.MOD_ID, "gnomite_sword")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("gnomite_sword");
	public static final Item sylphite_axe = new ItemNeoAxe(NeoOresItems.toolSylphite).setToolType(ToolType.AIR).setRegistryName(Reference.MOD_ID, "sylphite_axe").setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("sylphite_axe");
	public static final Item sylphite_hoe = new ItemNeoHoe(NeoOresItems.toolSylphite).setToolType(ToolType.AIR).setRegistryName(Reference.MOD_ID, "sylphite_hoe").setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("sylphite_hoe");
	public static final Item sylphite_paxel = new ItemNeoPaxel(NeoOresItems.toolSylphite).setToolType(ToolType.AIR).setRegistryName(Reference.MOD_ID, "sylphite_paxel")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("sylphite_paxel");
	public static final Item sylphite_pickaxe = new ItemNeoPickaxe(NeoOresItems.toolSylphite).setToolType(ToolType.AIR).setRegistryName(Reference.MOD_ID, "sylphite_pickaxe")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("sylphite_pickaxe");
	public static final Item sylphite_shovel = new ItemNeoSpade(NeoOresItems.toolSylphite).setToolType(ToolType.AIR).setRegistryName(Reference.MOD_ID, "sylphite_shovel")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("sylphite_shovel");
	public static final Item sylphite_sword = new ItemNeoSword(NeoOresItems.toolSylphite).setToolType(ToolType.AIR).setRegistryName(Reference.MOD_ID, "sylphite_sword")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("sylphite_sword");
	public static final Item creative_axe = new ItemNeoAxe(NeoOresItems.toolCreative).setToolType(ToolType.CREATIVE).setRegistryName(Reference.MOD_ID, "creative_axe")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("creative_axe");
	public static final Item creative_hoe = new ItemNeoHoe(NeoOresItems.toolCreative).setToolType(ToolType.CREATIVE).setRegistryName(Reference.MOD_ID, "creative_hoe")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("creative_hoe");
	public static final Item creative_paxel = new ItemNeoPaxel(NeoOresItems.toolCreative).setToolType(ToolType.CREATIVE).setRegistryName(Reference.MOD_ID, "creative_paxel")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("creative_paxel");
	public static final Item creative_pickaxe = new ItemNeoPickaxe(NeoOresItems.toolCreative).setToolType(ToolType.CREATIVE).setRegistryName(Reference.MOD_ID, "creative_pickaxe")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("creative_pickaxe");
	public static final Item creative_shovel = new ItemNeoSpade(NeoOresItems.toolCreative).setToolType(ToolType.CREATIVE).setRegistryName(Reference.MOD_ID, "creative_shovel")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("creative_shovel");
	public static final Item creative_sword = new ItemNeoSword(NeoOresItems.toolCreative).setToolType(ToolType.CREATIVE).setRegistryName(Reference.MOD_ID, "creative_sword")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("creative_sword");

	public static final Item undite_helmet = new ItemNeoArmor(NeoOresItems.armorUndite, 3, EntityEquipmentSlot.HEAD).setToolType(ToolType.WATER).setRegistryName(Reference.MOD_ID, "undite_helmet")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("undite_helmet");
	public static final Item undite_chestplate = new ItemNeoArmor(NeoOresItems.armorUndite, 3, EntityEquipmentSlot.CHEST).setToolType(ToolType.WATER)
			.setRegistryName(Reference.MOD_ID, "undite_chestplate").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("undite_chestplate");
	public static final Item undite_leggings = new ItemNeoArmor(NeoOresItems.armorUndite, 3, EntityEquipmentSlot.LEGS).setToolType(ToolType.WATER).setRegistryName(Reference.MOD_ID, "undite_leggings")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("undite_leggings");
	public static final Item undite_boots = new ItemNeoArmor(NeoOresItems.armorUndite, 3, EntityEquipmentSlot.FEET).setToolType(ToolType.WATER).setRegistryName(Reference.MOD_ID, "undite_boots")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("undite_boots");
	public static final Item salamite_helmet = new ItemNeoArmor(NeoOresItems.armorSalamite, 3, EntityEquipmentSlot.HEAD).setToolType(ToolType.FIRE).setRegistryName(Reference.MOD_ID, "salamite_helmet")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("salamite_helmet");
	public static final Item salamite_chestplate = new ItemNeoArmor(NeoOresItems.armorSalamite, 3, EntityEquipmentSlot.CHEST).setToolType(ToolType.FIRE)
			.setRegistryName(Reference.MOD_ID, "salamite_chestplate").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("salamite_chestplate");
	public static final Item salamite_leggings = new ItemNeoArmor(NeoOresItems.armorSalamite, 3, EntityEquipmentSlot.LEGS).setToolType(ToolType.FIRE)
			.setRegistryName(Reference.MOD_ID, "salamite_leggings").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("salamite_leggings");
	public static final Item salamite_boots = new ItemNeoArmor(NeoOresItems.armorSalamite, 3, EntityEquipmentSlot.FEET).setToolType(ToolType.FIRE).setRegistryName(Reference.MOD_ID, "salamite_boots")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("salamite_boots");
	public static final Item gnomite_helmet = new ItemNeoArmor(NeoOresItems.armorGnomite, 3, EntityEquipmentSlot.HEAD).setToolType(ToolType.EARTH).setRegistryName(Reference.MOD_ID, "gnomite_helmet")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("gnomite_helmet");
	public static final Item gnomite_chestplate = new ItemNeoArmor(NeoOresItems.armorGnomite, 3, EntityEquipmentSlot.CHEST).setToolType(ToolType.EARTH)
			.setRegistryName(Reference.MOD_ID, "gnomite_chestplate").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("gnomite_chestplate");
	public static final Item gnomite_leggings = new ItemNeoArmor(NeoOresItems.armorGnomite, 3, EntityEquipmentSlot.LEGS).setToolType(ToolType.EARTH)
			.setRegistryName(Reference.MOD_ID, "gnomite_leggings").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("gnomite_leggings");
	public static final Item gnomite_boots = new ItemNeoArmor(NeoOresItems.armorGnomite, 3, EntityEquipmentSlot.FEET).setToolType(ToolType.EARTH).setRegistryName(Reference.MOD_ID, "gnomite_boots")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("gnomite_boots");
	public static final Item sylphite_helmet = new ItemNeoArmor(NeoOresItems.armorSylphite, 3, EntityEquipmentSlot.HEAD).setToolType(ToolType.AIR).setRegistryName(Reference.MOD_ID, "sylphite_helmet")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("sylphite_helmet");
	public static final Item sylphite_chestplate = new ItemNeoArmor(NeoOresItems.armorSylphite, 3, EntityEquipmentSlot.CHEST).setToolType(ToolType.AIR)
			.setRegistryName(Reference.MOD_ID, "sylphite_chestplate").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("sylphite_chestplate");
	public static final Item sylphite_leggings = new ItemNeoArmor(NeoOresItems.armorSylphite, 3, EntityEquipmentSlot.LEGS).setToolType(ToolType.AIR)
			.setRegistryName(Reference.MOD_ID, "sylphite_leggings").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("sylphite_leggings");
	public static final Item sylphite_boots = new ItemNeoArmor(NeoOresItems.armorSylphite, 3, EntityEquipmentSlot.FEET).setToolType(ToolType.AIR).setRegistryName(Reference.MOD_ID, "sylphite_boots")
			.setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("sylphite_boots");
	public static final Item creative_helmet = new ItemNeoArmor(NeoOresItems.armorCreative, 3, EntityEquipmentSlot.HEAD).setToolType(ToolType.CREATIVE)
			.setRegistryName(Reference.MOD_ID, "creative_helmet").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("creative_helmet");
	public static final Item creative_chestplate = new ItemNeoArmor(NeoOresItems.armorCreative, 3, EntityEquipmentSlot.CHEST).setToolType(ToolType.CREATIVE)
			.setRegistryName(Reference.MOD_ID, "creative_chestplate").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("creative_chestplate");
	public static final Item creative_leggings = new ItemNeoArmor(NeoOresItems.armorCreative, 3, EntityEquipmentSlot.LEGS).setToolType(ToolType.CREATIVE)
			.setRegistryName(Reference.MOD_ID, "creative_leggings").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("creative_leggings");
	public static final Item creative_boots = new ItemNeoArmor(NeoOresItems.armorCreative, 3, EntityEquipmentSlot.FEET).setToolType(ToolType.CREATIVE)
			.setRegistryName(Reference.MOD_ID, "creative_boots").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("creative_boots");

	public static final Item totem = new ItemTotem(64).setRegistryName(Reference.MOD_ID, "totem_basic").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("totem_basic");
	public static final Item totem_independent = new ItemTotem(256) {
		public boolean needsPlayer(ItemStack stack)
		{
			return false;
		}
	}.setRegistryName(Reference.MOD_ID, "totem_independent").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("totem_independent");
	public static final Item totem_creative = new ItemTotem(0)
	{
		public boolean needsPlayer(ItemStack stack)
		{
			return false;
		}
		
		public boolean isCreative(ItemStack stack)
		{
			return true;
		}
		
		@SideOnly(Side.CLIENT)
		public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
		{
			if(stack.getTagCompound() == null || !stack.getTagCompound().getBoolean("Unbreakable"))
			{
				tooltip.add(TextFormatting.BLUE + I18n.format("item.unbreakable"));
			}
		}
	}.setRegistryName(Reference.MOD_ID, "totem_creative").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("totem_creative");
	public static final Item upgrade_void = new ItemUpgrade()
	{
		public void upgrade(TileEntityMechanicalMagician temm)
		{
			temm.voidExp = true;
		}
	}.setRegistryName(Reference.MOD_ID, "upgrade_void").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("upgrade_void");
	public static final Item upgrade_xp = new ItemUpgrade()
	{
		public void upgrade(TileEntityMechanicalMagician temm)
		{
			temm.makeExp = true;
		}
	}.setRegistryName(Reference.MOD_ID, "upgrade_xp").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("upgrade_xp");
	public static final Item upgrade_fluid = new ItemUpgrade()
	{
		public void upgrade(TileEntityMechanicalMagician temm)
		{
			temm.useLiquidMana = true;
		}
	}.setRegistryName(Reference.MOD_ID, "upgrade_fluid").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("upgrade_fluid");
	public static final Item upgrade_consume = new ItemUpgrade()
	{
		public void upgrade(TileEntityMechanicalMagician temm)
		{
			temm.noConsumeMana = 10L;
		}
	}.setRegistryName(Reference.MOD_ID, "upgrade_consume").setCreativeTab(NeoOres.neo_ores_tab).setUnlocalizedName("upgrade_consume");

	public static final List<Item> registry = Arrays.asList(undite, gnomite_ingot, salamite, sylphite, mana_ingot, essence, air_essence_core, earth_essence_core, fire_essence_core, water_essence_core,
			aerite, drenite, flamite, forcite, guardite_ingot, landite_ingot, marlite_ingot, sanitite, spell, mana_wrench, recipe_sheet, spell_sheet, mob_bottle, mob_bottle_master, undite_axe,
			undite_hoe, undite_paxel, undite_pickaxe, undite_shovel, undite_sword, undite_helmet, undite_chestplate, undite_leggings, undite_boots, gnomite_axe, gnomite_hoe, gnomite_paxel,
			gnomite_pickaxe, gnomite_shovel, gnomite_sword, gnomite_helmet, gnomite_chestplate, gnomite_leggings, gnomite_boots, salamite_axe, salamite_hoe, salamite_paxel, salamite_pickaxe,
			salamite_shovel, salamite_sword, salamite_helmet, salamite_chestplate, salamite_leggings, salamite_boots, sylphite_axe, sylphite_hoe, sylphite_paxel, sylphite_pickaxe, sylphite_shovel,
			sylphite_sword, sylphite_helmet, sylphite_chestplate, sylphite_leggings, sylphite_boots, creative_axe, creative_hoe, creative_paxel, creative_pickaxe, creative_shovel, creative_sword,
			creative_helmet, creative_chestplate, creative_leggings, creative_boots, totem, totem_independent, totem_creative, upgrade_void, upgrade_xp, upgrade_fluid, upgrade_consume);
}
