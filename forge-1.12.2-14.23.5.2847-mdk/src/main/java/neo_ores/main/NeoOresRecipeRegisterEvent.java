package neo_ores.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import neo_ores.api.RecipeOreStack;
import neo_ores.api.recipe.ManaCompositionRecipe;
import neo_ores.api.recipe.ManaCraftingRecipe;
import neo_ores.api.recipe.OreWeightRecipe;
import neo_ores.api.recipe.SpellRecipe;
import neo_ores.api.spell.SpellItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class NeoOresRecipeRegisterEvent
{
	private List<ManaCraftingRecipeManager> manacraftingrecipes = new ArrayList<ManaCraftingRecipeManager>();
	private List<ManaCompositionRecipeManager> manacompositionrecipes = new ArrayList<ManaCompositionRecipeManager>();

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void registerSpellItems(final RegistryEvent.Register<SpellItem> event)
	{
		for (SpellItem spell : NeoOresSpells.registry)
		{
			event.getRegistry().register(spell);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void registerSpellRecipes(final RegistryEvent.Register<SpellRecipe> event)
	{
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_dig, new RecipeOreStack(new ItemStack(Items.IRON_SHOVEL), 1), new RecipeOreStack(new ItemStack(Items.IRON_AXE), 1),
				new RecipeOreStack(new ItemStack(Items.WOODEN_PICKAXE), 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_touch, new RecipeOreStack("stickWood", 1), new RecipeOreStack("feather", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_support_liquid, new RecipeOreStack(new ItemStack(Items.BUCKET), 1), new RecipeOreStack(new ItemStack(Items.SHEARS), 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_composition, new RecipeOreStack("enhancedPedestalAll", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_earth_damage, new RecipeOreStack(new ItemStack(Items.WOODEN_SWORD), 4)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv1, new RecipeOreStack(new ItemStack(Items.STONE_PICKAXE), 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv2, new RecipeOreStack(new ItemStack(Items.IRON_PICKAXE), 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv3, new RecipeOreStack(new ItemStack(Items.DIAMOND_PICKAXE), 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv4, new RecipeOreStack("obsidian", 16)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv5, new RecipeOreStack(new ItemStack(NeoOresItems.gnomite_pickaxe), 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv6, new RecipeOreStack(new ItemStack(NeoOresItems.undite_pickaxe), 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv7, new RecipeOreStack(new ItemStack(NeoOresItems.sylphite_pickaxe), 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv8, new RecipeOreStack(new ItemStack(NeoOresItems.sylphite_pickaxe), 1), new RecipeOreStack("gemDiamond", 64)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv9, new RecipeOreStack(new ItemStack(NeoOresItems.salamite_pickaxe), 1), new RecipeOreStack("gemDiamond", 128)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv10, new RecipeOreStack(new ItemStack(NeoOresItems.salamite_pickaxe), 1), new RecipeOreStack("gemDiamond", 256)));
		event.getRegistry()
				.register(new SpellRecipe(NeoOresSpells.spell_harvestLv11, new RecipeOreStack(new ItemStack(NeoOresItems.gnomite_paxel), 1),
						new RecipeOreStack(new ItemStack(NeoOresItems.sylphite_paxel), 1), new RecipeOreStack(new ItemStack(NeoOresItems.undite_paxel), 1),
						new RecipeOreStack(new ItemStack(NeoOresItems.salamite_paxel), 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck1, new RecipeOreStack("gemLapis", 8), new RecipeOreStack("gemDiamond", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck2, new RecipeOreStack("gemLapis", 16), new RecipeOreStack("gemDiamond", 2)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck3, new RecipeOreStack("gemLapis", 32), new RecipeOreStack("gemDiamond", 3)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck4, new RecipeOreStack("gemLapis", 64), new RecipeOreStack("gemDiamond", 4)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck5, new RecipeOreStack("gemLapis", 128), new RecipeOreStack("gemDiamond", 5)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck6, new RecipeOreStack("gemLapis", 256), new RecipeOreStack("gemDiamond", 6)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck7, new RecipeOreStack("gemLapis", 512), new RecipeOreStack("gemDiamond", 7)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck8, new RecipeOreStack("gemLapis", 1024), new RecipeOreStack("gemDiamond", 8)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck9, new RecipeOreStack("gemLapis", 2048), new RecipeOreStack("gemDiamond", 9)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck10, new RecipeOreStack("gemLapis", 4096), new RecipeOreStack("gemDiamond", 10)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_silk, new RecipeOreStack("gemEmerald", 10), new RecipeOreStack("wool", 1), new RecipeOreStack("feather", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier1, new RecipeOreStack("blockEarthEssence", 1), new RecipeOreStack("blockWaterEssence", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier2, new RecipeOreStack("blockFireEssence", 1), new RecipeOreStack("blockAirEssence", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier3, new RecipeOreStack("gemDiamond", 1), new RecipeOreStack("gemEmerald", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_bullet, new RecipeOreStack(new ItemStack(Items.SNOWBALL), 1), new RecipeOreStack(new ItemStack(Items.ARROW), 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_speed1, new RecipeOreStack("ingotGold", 4)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_speed2, new RecipeOreStack("ingotGold", 8)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_speed3, new RecipeOreStack("ingotGold", 16)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_speed4, new RecipeOreStack("ingotGold", 32)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_continuation1, new RecipeOreStack("enderpearl", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_continuation2, new RecipeOreStack("enderpearl", 2)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_continuation3, new RecipeOreStack("enderpearl", 4)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_continuation4, new RecipeOreStack("enderpearl", 8)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_noGravity, new RecipeOreStack("enderpearl", 16)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_noAnyResistance, new RecipeOreStack("ingotGold", 64), new RecipeOreStack(new ItemStack(Items.ARROW), 4)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier4, new RecipeOreStack("ingotLandite", 1), new RecipeOreStack("ingotGuardite", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier5, new RecipeOreStack("ingotGnomite", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier6, new RecipeOreStack("ingotMarlite", 1), new RecipeOreStack("gemSanitite", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier7, new RecipeOreStack("gemUndite", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier8, new RecipeOreStack("gemDrenite", 1), new RecipeOreStack("gemAerite", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier9, new RecipeOreStack("gemSylphite", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier10, new RecipeOreStack("gemFlamite", 1), new RecipeOreStack("gemForcite", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier11, new RecipeOreStack("gemSalamite", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_range1, new RecipeOreStack("dustGlowstone", 16), new RecipeOreStack("string", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_gather, new RecipeOreStack(new ItemStack(Blocks.HOPPER), 1), new RecipeOreStack(new ItemStack(Items.WATER_BUCKET), 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_range2, new RecipeOreStack("dustGlowstone", 64), new RecipeOreStack("string", 2)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_range3, new RecipeOreStack("dustGlowstone", 256), new RecipeOreStack("string", 4)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_range4, new RecipeOreStack("dustGlowstone", 1024), new RecipeOreStack("string", 8)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv1, new RecipeOreStack(new ItemStack(Items.SPIDER_EYE), 4), new RecipeOreStack("gemQuartz", 4)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv2, new RecipeOreStack(new ItemStack(Items.SPIDER_EYE), 8), new RecipeOreStack("gemQuartz", 8)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv3, new RecipeOreStack(new ItemStack(Items.SPIDER_EYE), 16), new RecipeOreStack("gemQuartz", 16)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv4, new RecipeOreStack(new ItemStack(Items.SPIDER_EYE), 32), new RecipeOreStack("gemQuartz", 32)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv5, new RecipeOreStack(new ItemStack(Items.SPIDER_EYE), 64), new RecipeOreStack("gemQuartz", 64)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv6, new RecipeOreStack(new ItemStack(Items.SPIDER_EYE), 128), new RecipeOreStack("gemQuartz", 128)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv7, new RecipeOreStack(new ItemStack(Items.SPIDER_EYE), 256), new RecipeOreStack("gemQuartz", 256)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv8, new RecipeOreStack(new ItemStack(Items.SPIDER_EYE), 1024), new RecipeOreStack("gemQuartz", 1024)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv9, new RecipeOreStack(new ItemStack(Items.SPIDER_EYE), 2048), new RecipeOreStack("gemQuartz", 2048)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv10, new RecipeOreStack(new ItemStack(Items.SPIDER_EYE), 4096), new RecipeOreStack("gemQuartz", 4096)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv11, new RecipeOreStack(new ItemStack(Items.SPIDER_EYE), 8192), new RecipeOreStack("gemQuartz", 8192)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_summon, new RecipeOreStack("mobBottle", 1), new RecipeOreStack(new ItemStack(Items.NETHER_STAR), 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_nbt_applying, new RecipeOreStack(new ItemStack(Items.NETHER_STAR), 64)));
		event.getRegistry()
				.register(new SpellRecipe(NeoOresSpells.spell_ore_gen, new RecipeOreStack(new ItemStack(Items.NETHER_STAR), 64), new RecipeOreStack(new ItemStack(NeoOresItems.sylphite), 16),
						new RecipeOreStack(new ItemStack(NeoOresItems.gnomite_ingot), 16), new RecipeOreStack(new ItemStack(NeoOresItems.salamite), 16),
						new RecipeOreStack(new ItemStack(NeoOresItems.undite), 16)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_no_inertia, new RecipeOreStack(new ItemStack(Items.ENDER_EYE), 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_collidable_filter, new RecipeOreStack("enderpearl", 1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_pull_item, new RecipeOreStack(new ItemStack(Blocks.PISTON), 1)));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void registerManaCraftingRecipe(final RegistryEvent.Register<ManaCraftingRecipe> event)
	{
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_axe), 10000, "RR", "TR", "T ", 'R', "gemUndite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_pickaxe), 10000, "RRR", " T ", " T ", 'R', "gemUndite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_sword), 10000, "R", "R", "T", 'R', "gemUndite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_hoe), 10000, "RR", "T ", "T ", 'R', "gemUndite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_paxel), 10000, "ASP", "LTH", " T ", 'A', new ItemStack(NeoOresItems.undite_axe), 'H', new ItemStack(NeoOresItems.undite_hoe), 'P',
				new ItemStack(NeoOresItems.undite_pickaxe), 'L', new ItemStack(NeoOresItems.undite_shovel), 'S', new ItemStack(NeoOresItems.undite_sword), 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_shovel), 10000, "R", "T", "T", 'R', "gemUndite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_axe), 10000, "RR", "TR", "T ", 'R', "ingotGnomite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_pickaxe), 10000, "RRR", " T ", " T ", 'R', "ingotGnomite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_sword), 10000, "R", "R", "T", 'R', "ingotGnomite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_hoe), 10000, "RR", "T ", "T ", 'R', "ingotGnomite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_paxel), 10000, "ASP", "LTH", " T ", 'A', new ItemStack(NeoOresItems.gnomite_axe), 'H', new ItemStack(NeoOresItems.gnomite_hoe),
				'P', new ItemStack(NeoOresItems.gnomite_pickaxe), 'L', new ItemStack(NeoOresItems.gnomite_shovel), 'S', new ItemStack(NeoOresItems.gnomite_sword), 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_shovel), 10000, "R", "T", "T", 'R', "ingotGnomite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_axe), 10000, "RR", "TR", "T ", 'R', "gemSalamite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_pickaxe), 10000, "RRR", " T ", " T ", 'R', "gemSalamite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_sword), 10000, "R", "R", "T", 'R', "gemSalamite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_hoe), 10000, "RR", "T ", "T ", 'R', "gemSalamite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_paxel), 10000, "ASP", "LTH", " T ", 'A', new ItemStack(NeoOresItems.salamite_axe), 'H', new ItemStack(NeoOresItems.salamite_hoe),
				'P', new ItemStack(NeoOresItems.salamite_pickaxe), 'L', new ItemStack(NeoOresItems.salamite_shovel), 'S', new ItemStack(NeoOresItems.salamite_sword), 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_shovel), 10000, "R", "T", "T", 'R', "gemSalamite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_axe), 10000, "RR", "TR", "T ", 'R', "gemSylphite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_pickaxe), 10000, "RRR", " T ", " T ", 'R', "gemSylphite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_sword), 10000, "R", "R", "T", 'R', "gemSylphite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_hoe), 10000, "RR", "T ", "T ", 'R', "gemSylphite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_paxel), 10000, "ASP", "LTH", " T ", 'A', new ItemStack(NeoOresItems.sylphite_axe), 'H', new ItemStack(NeoOresItems.sylphite_hoe),
				'P', new ItemStack(NeoOresItems.sylphite_pickaxe), 'L', new ItemStack(NeoOresItems.sylphite_shovel), 'S', new ItemStack(NeoOresItems.sylphite_sword), 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_shovel), 10000, "R", "T", "T", 'R', "gemSylphite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.essence, 1, 0), 5, " H ", "HXH", " H ", 'H', "gravel", 'X', "nuggetIron");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.essence, 1, 1), 5, " H ", "HXH", " H ", 'H', PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), 'X',
				"nuggetIron");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.essence, 1, 2), 5, " H ", "HXH", " H ", 'H', new ItemStack(Blocks.MAGMA), 'X', "nuggetIron");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.essence, 1, 3), 5, " H ", "HXH", " H ", 'H', "feather", 'X', "nuggetIron");
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.pedestal, 9), 20, "ICI", " I ", 'C', "chestWood", 'I', "ingotIron");
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.pedestal_water), 5, "W", "P", 'W', new ItemStack(Items.WATER_BUCKET), 'P', new ItemStack(NeoOresBlocks.pedestal));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 0), 20, " P ", "PCP", " P ", 'C', "chestWood", 'P', new ItemStack(NeoOresBlocks.pedestal));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 1), 20, " P ", "PCP", " P ", 'C', "chestWood", 'P', new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 0));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 2), 20, " P ", "PCP", " P ", 'C', "chestWood", 'P', new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 1));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 3), 20, " P ", "PCP", " P ", 'C', "chestWood", 'P', new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 2));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 4), 20, " P ", "PCP", " P ", 'C', "chestWood", 'P', new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 3));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 5), 20, " P ", "PCP", " P ", 'C', "chestWood", 'P', new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 4));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 6), 20, " P ", "PCP", " P ", 'C', "chestWood", 'P', new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 5));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 7), 20, " P ", "PCP", " P ", 'C', "chestWood", 'P', new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 6));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.mage_knowledge_table), 100, " F ", "AXW", " E ", 'F', new ItemStack(NeoOresItems.essence, 1, 2), 'A',
				new ItemStack(NeoOresItems.essence, 1, 3), 'E', new ItemStack(NeoOresItems.essence, 1, 0), 'W', new ItemStack(NeoOresItems.essence, 1, 1), 'X', new ItemStack(Blocks.ENCHANTING_TABLE));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.spell_recipe_creation_table), 100, "F", "B", "M", 'F', "feather", 'B', "dyeBlack", 'M', new ItemStack(NeoOresBlocks.mana_workbench));
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.mana_wrench), 10, "I", "T", "T", 'I', "nuggetIron", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.spell_sheet), 10, "E", "P", 'E', "essenceAll", 'P', "paper");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.recipe_sheet), 10, "E", "P", 'E', "dyeBlack", 'P', "paper");
		this.addManaCraftingRecipe(new ItemStack(Blocks.QUARTZ_ORE, 4), 10, "DND", "NDN", "DND", 'D', "stoneDiorite", 'N', "netherrack");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_helmet), 10000, "PPP", "P P", 'P', "ingotGnomite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_chestplate), 10000, "P P", "PPP", "PPP", 'P', "ingotGnomite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_leggings), 10000, "PPP", "P P", "P P", 'P', "ingotGnomite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_boots), 10000, "P P", "P P", 'P', "ingotGnomite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_helmet), 10000, "PPP", "P P", 'P', "gemSalamite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_chestplate), 10000, "P P", "PPP", "PPP", 'P', "gemSalamite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_leggings), 10000, "PPP", "P P", "P P", 'P', "gemSalamite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_boots), 10000, "P P", "P P", 'P', "gemSalamite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_helmet), 10000, "PPP", "P P", 'P', "gemSylphite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_chestplate), 10000, "P P", "PPP", "PPP", 'P', "gemSylphite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_leggings), 10000, "PPP", "P P", "P P", 'P', "gemSylphite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_boots), 10000, "P P", "P P", 'P', "gemSylphite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_helmet), 10000, "PPP", "P P", 'P', "gemUndite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_chestplate), 10000, "P P", "PPP", "PPP", 'P', "gemUndite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_leggings), 10000, "PPP", "P P", "P P", 'P', "gemUndite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_boots), 10000, "P P", "P P", 'P', "gemUndite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.mob_bottle), 100, "A", "D", "B", 'A', "gemAerite", 'D', "dyeBlack", 'B', new ItemStack(Items.GLASS_BOTTLE));
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.mob_bottle_master), 100, "D", "B", 'D', new ItemStack(Blocks.DRAGON_EGG), 'B', new ItemStack(NeoOresItems.mob_bottle));

		int n = 0;
		for (ManaCraftingRecipeManager mcrm : manacraftingrecipes)
		{
			event.getRegistry().register(new ManaCraftingRecipe(mcrm.getResult(), mcrm.getMana(), mcrm.getObjects()).setRegistryName(Reference.MOD_ID, "manaCrafting." + n));
			n++;
		}
	}

	public static void registerFromJson(FMLPreInitializationEvent event)
	{
		File configFile = new File(event.getModConfigurationDirectory(), "neo_ores.json");
		if (!configFile.exists())
		{
			try
			{
				JsonWriter writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8));
				JsonObject configDefault = new JsonObject();
				JsonArray spell_ore_gen = new JsonArray();

				JsonObject stone = new JsonObject();
				stone.addProperty("id", "minecraft:stone");
				stone.addProperty("metadata", 0);
				// OreDic Only
				JsonArray acceptModsStoneOre = new JsonArray();
				acceptModsStoneOre.add("minecraft");
				acceptModsStoneOre.add("thermalfoundation");
				acceptModsStoneOre.add("nuclearcraft");
				acceptModsStoneOre.add("ic2");
				acceptModsStoneOre.add("mekanism");
				acceptModsStoneOre.add("roots");
				acceptModsStoneOre.add("thaumcraft");
				acceptModsStoneOre.add("actuallyaddition");
				acceptModsStoneOre.add("biomesoplenty");
				acceptModsStoneOre.add("appliedenergistics2");
				acceptModsStoneOre.add("immersiveengineering");
				acceptModsStoneOre.add("astralsorcery");
				acceptModsStoneOre.add("draconicevolution");
				acceptModsStoneOre.add("embers");
				acceptModsStoneOre.add("rftools");
				acceptModsStoneOre.add("forestry");
				acceptModsStoneOre.add("projectred-exploration");
				acceptModsStoneOre.add("metallurgy");
				acceptModsStoneOre.add("iceandfire");

				JsonObject oreIronStone = new JsonObject();
				oreIronStone.addProperty("registry_name", "oredic_ore_iron_stone");
				oreIronStone.addProperty("id", "ore:oreIron");
				oreIronStone.addProperty("weight", 11520);
				oreIronStone.add("replace_block", stone);
				oreIronStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreIronStone);

				JsonObject oreGoldStone = new JsonObject();
				oreGoldStone.addProperty("registry_name", "oredic_ore_gold_stone");
				oreGoldStone.addProperty("id", "ore:oreGold");
				oreGoldStone.addProperty("weight", 576);
				oreGoldStone.add("replace_block", stone);
				oreGoldStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreGoldStone);

				JsonObject oreDiamondStone = new JsonObject();
				oreDiamondStone.addProperty("registry_name", "oredic_ore_diamond_stone");
				oreDiamondStone.addProperty("id", "ore:oreDiamond");
				oreDiamondStone.addProperty("weight", 128);
				oreDiamondStone.add("replace_block", stone);
				oreDiamondStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreDiamondStone);

				JsonObject oreEmeraldStone = new JsonObject();
				oreEmeraldStone.addProperty("registry_name", "oredic_ore_emerald_stone");
				oreEmeraldStone.addProperty("id", "ore:oreEmerald");
				oreEmeraldStone.addProperty("weight", 112);
				oreEmeraldStone.add("replace_block", stone);
				oreEmeraldStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreEmeraldStone);

				JsonObject oreRedstoneStone = new JsonObject();
				oreRedstoneStone.addProperty("registry_name", "oredic_ore_redstone_stone");
				oreRedstoneStone.addProperty("id", "ore:oreRedstone");
				oreRedstoneStone.addProperty("weight", 1024);
				oreRedstoneStone.add("replace_block", stone);
				oreRedstoneStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreRedstoneStone);

				JsonObject oreLapisStone = new JsonObject();
				oreLapisStone.addProperty("registry_name", "oredic_ore_lapis_stone");
				oreLapisStone.addProperty("id", "ore:oreLapis");
				oreLapisStone.addProperty("weight", 224);
				oreLapisStone.add("replace_block", stone);
				oreLapisStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreLapisStone);

				JsonObject oreCoalStone = new JsonObject();
				oreCoalStone.addProperty("registry_name", "oredic_ore_coal_stone");
				oreCoalStone.addProperty("id", "ore:oreCoal");
				oreCoalStone.addProperty("weight", 43520);
				oreCoalStone.add("replace_block", stone);
				oreCoalStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreCoalStone);

				configDefault.add("spell_ore_gen", spell_ore_gen);

				Gson gson = new GsonBuilder().serializeNulls().create();
				writer.setIndent("  ");
				gson.toJson(configDefault, writer);
				writer.close();
			}
			catch (Exception e)
			{
				FMLLog.log.error("Unable to create {} - skipping", configFile);
			}
		}
		JsonParser parser = new JsonParser();
		JsonElement config;
		try
		{
			try (Reader reader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8))
			{
				config = parser.parse(reader);
			}

			if (config.isJsonObject())
			{
				JsonObject jo = config.getAsJsonObject();
				for (JsonElement genE : jo.get("spell_ore_gen").getAsJsonArray())
				{
					try
					{
						JsonObject gen = genE.getAsJsonObject();
						GameRegistry.findRegistry(OreWeightRecipe.class)
								.register(new OreWeightRecipe(gen).setRegistryName(new ResourceLocation(Reference.MOD_ID, gen.get("registry_name").getAsString())));
					}
					catch (Exception e)
					{
						FMLLog.log.error("Unable to register");
					}
				}
			}
		}
		catch (Exception e)
		{
			FMLLog.log.error("Unable to parse {} - skipping", configFile);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerOreWeightRecipes(RegistryEvent.Register<OreWeightRecipe> event)
	{
		// event.getR
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
		event.getRegistry()
				.register(new ShapedRecipes(Reference.MOD_ID, 3, 3,
						NonNullList.from(Ingredient.EMPTY, Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItems(Items.NETHER_STAR),
								Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItem(Item.getItemFromBlock(Blocks.DRAGON_EGG)), Ingredient.fromItems(Items.NETHER_STAR),
								Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItems(Items.NETHER_STAR)),
						NeoOres.addName(NeoOres.addEnchantment(NeoOres.addRegacy(new ItemStack(Items.STICK)), Enchantments.KNOCKBACK, 100), "homerunbat")).setRegistryName(Reference.MOD_ID,
								"homerunbat"));
		event.getRegistry().register(
				new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.essence, 1, 1), "HHH", "HXH", "HHH", 'H', Items.POTIONITEM, 'X', "ingotIron")
						.setRegistryName(Reference.MOD_ID, "water_essence"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.essence, 1, 2), "HHH", "HXH", "HHH", 'H', Blocks.MAGMA, 'X', "ingotIron")
						.setRegistryName(Reference.MOD_ID, "fire_essence"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.essence, 1, 0), "CDS", "GXG", "SDC", 'C', Items.CLAY_BALL, 'D',
				"dirt", 'S', "sand", 'G', "gravel", 'X', "ingotIron").setRegistryName(Reference.MOD_ID, "earth_essence"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.essence, 1, 3), "HHH", "HXH", "HHH", 'H', "feather", 'X', "ingotIron")
						.setRegistryName(Reference.MOD_ID, "air_essence"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.mana_workbench), " F ", "AXW", " E ", 'F',
						new ItemStack(NeoOresItems.essence, 1, 2), 'A', new ItemStack(NeoOresItems.essence, 1, 3), 'E', new ItemStack(NeoOresItems.essence, 1, 0), 'W',
						new ItemStack(NeoOresItems.essence, 1, 1), 'X', "workbench").setRegistryName(Reference.MOD_ID, "mana_workbench"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 8), "P", "H", 'P',
				new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 0), 'H', new ItemStack(Blocks.HOPPER)).setRegistryName(Reference.MOD_ID, "EPhopped1"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 9), "P", "H", 'P',
				new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 1), 'H', new ItemStack(Blocks.HOPPER)).setRegistryName(Reference.MOD_ID, "EPhopped2"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 10), "P", "H", 'P',
				new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 2), 'H', new ItemStack(Blocks.HOPPER)).setRegistryName(Reference.MOD_ID, "EPhopped3"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 11), "P", "H", 'P',
				new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 3), 'H', new ItemStack(Blocks.HOPPER)).setRegistryName(Reference.MOD_ID, "EPhopped4"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 12), "P", "H", 'P',
				new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 4), 'H', new ItemStack(Blocks.HOPPER)).setRegistryName(Reference.MOD_ID, "EPhopped5"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 13), "P", "H", 'P',
				new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 5), 'H', new ItemStack(Blocks.HOPPER)).setRegistryName(Reference.MOD_ID, "EPhopped6"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 14), "P", "H", 'P',
				new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 6), 'H', new ItemStack(Blocks.HOPPER)).setRegistryName(Reference.MOD_ID, "EPhopped7"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 15), "P", "H", 'P',
				new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, 7), 'H', new ItemStack(Blocks.HOPPER)).setRegistryName(Reference.MOD_ID, "EPhopped8"));
		event.getRegistry().register(
				new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.air_essence_block), "XX", "XX", 'X', new ItemStack(NeoOresItems.essence, 1, 3))
						.setRegistryName(Reference.MOD_ID, "air_comp"));
		event.getRegistry().register(
				new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.earth_essence_block), "XX", "XX", 'X', new ItemStack(NeoOresItems.essence, 1, 0))
						.setRegistryName(Reference.MOD_ID, "earth_comp"));
		event.getRegistry().register(
				new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.fire_essence_block), "XX", "XX", 'X', new ItemStack(NeoOresItems.essence, 1, 2))
						.setRegistryName(Reference.MOD_ID, "fire_comp"));
		event.getRegistry().register(
				new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.water_essence_block), "XX", "XX", 'X', new ItemStack(NeoOresItems.essence, 1, 1))
						.setRegistryName(Reference.MOD_ID, "water_comp"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.essence, 4, 3), "X", 'X', new ItemStack(NeoOresBlocks.air_essence_block))
						.setRegistryName(Reference.MOD_ID, "air_decomp"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.essence, 4, 0), "X", 'X', new ItemStack(NeoOresBlocks.earth_essence_block))
						.setRegistryName(Reference.MOD_ID, "earth_decomp"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.essence, 4, 2), "X", 'X', new ItemStack(NeoOresBlocks.fire_essence_block))
						.setRegistryName(Reference.MOD_ID, "fire_decomp"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.essence, 4, 1), "X", 'X', new ItemStack(NeoOresBlocks.water_essence_block))
						.setRegistryName(Reference.MOD_ID, "water_decomp"));

		/*
		 * GameRegistry.addSmelting(new ItemStack(NeoOres.guardite_ore), new
		 * ItemStack(NeoOres.guardite_ingot), 16.0F); GameRegistry.addSmelting(new
		 * ItemStack(NeoOres.landite_ore), new ItemStack(NeoOres.landite_ingot), 16.0F);
		 * GameRegistry.addSmelting(new ItemStack(NeoOres.marlite_ore), new
		 * ItemStack(NeoOres.marlite_ingot), 16.0F);
		 */

		addOreDictSmelting("oreGuardite", new ItemStack(NeoOresItems.guardite_ingot), 16.0F);
		addOreDictSmelting("oreLandite", new ItemStack(NeoOresItems.landite_ingot), 16.0F);
		addOreDictSmelting("oreMarlite", new ItemStack(NeoOresItems.marlite_ingot), 16.0F);
		addOreDictSmelting("oreSanitite", new ItemStack(NeoOresItems.sanitite), 16.0F);
		addOreDictSmelting("oreFlamite", new ItemStack(NeoOresItems.flamite), 16.0F);
		addOreDictSmelting("oreForcite", new ItemStack(NeoOresItems.forcite), 16.0F);
		addOreDictSmelting("oreDrenite", new ItemStack(NeoOresItems.drenite), 16.0F);
		addOreDictSmelting("oreAerite", new ItemStack(NeoOresItems.aerite), 16.0F);
		addOreDictSmelting("oreCoal", new ItemStack(Items.COAL), 0.1F);
		addOreDictSmelting("oreDiamond", new ItemStack(Items.DIAMOND), 1.0F);
		addOreDictSmelting("oreEmerald", new ItemStack(Items.EMERALD), 1.0F);
		addOreDictSmelting("oreGold", new ItemStack(Items.GOLD_INGOT), 1.0F);
		addOreDictSmelting("oreIron", new ItemStack(Items.IRON_INGOT), 0.7F);
		addOreDictSmelting("oreLapis", new ItemStack(Items.DYE, 1, 4), 0.2F);
		addOreDictSmelting("oreRedstone", new ItemStack(Items.REDSTONE), 0.7F);
		addOreDictSmelting("oreQuartz", new ItemStack(Items.QUARTZ), 0.2F);

		BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD), "essenceAll",
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), NeoOres.mana_regen));
		BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), NeoOres.mana_regen), "dustRedstone",
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), NeoOres.long_mana_regen));
		BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), NeoOres.mana_regen), "dustGlowstone",
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), NeoOres.strong_mana_regen));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void registerManaCompositionRecipe(final RegistryEvent.Register<ManaCompositionRecipe> event)
	{
		this.addManaCompositionRecipe(0, new ItemStack(Items.FEATHER, 5), Arrays.asList(new RecipeOreStack("gemQuartz", 5), new RecipeOreStack("enderpearl", 1)));
		this.addManaCompositionRecipe(2, new ItemStack(Items.ENDER_PEARL, 1), Arrays.asList(new RecipeOreStack("ingotIron", 10)));
		this.addManaCompositionRecipe(4, new ItemStack(NeoOresItems.gnomite_ingot, 1), Arrays.asList(new RecipeOreStack("ingotGuardite", 8), new RecipeOreStack("ingotLandite", 8)));
		this.addManaCompositionRecipe(6, new ItemStack(NeoOresItems.undite, 1), Arrays.asList(new RecipeOreStack("ingotMarlite", 8), new RecipeOreStack("gemSanitite", 8)));
		this.addManaCompositionRecipe(8, new ItemStack(NeoOresItems.sylphite, 1), Arrays.asList(new RecipeOreStack("gemDrenite", 8), new RecipeOreStack("gemAerite", 8)));
		this.addManaCompositionRecipe(10, new ItemStack(NeoOresItems.salamite, 1), Arrays.asList(new RecipeOreStack("gemFlamite", 8), new RecipeOreStack("gemForcite", 8)));

		int n = 0;
		for (ManaCompositionRecipeManager mcrm : manacompositionrecipes)
		{
			event.getRegistry().register(new ManaCompositionRecipe(mcrm.getTier(), mcrm.getResult(), mcrm.getObjects()).setRegistryName(Reference.MOD_ID, "" + n));
			n++;
		}
	}

	private void addManaCraftingRecipe(ItemStack result, int mana, Object... objects)
	{
		manacraftingrecipes.add(new ManaCraftingRecipeManager(result, mana, objects));
	}

	private void addManaCompositionRecipe(int tier, ItemStack result, List<RecipeOreStack> objects)
	{
		manacompositionrecipes.add(new ManaCompositionRecipeManager(tier, result, objects));
	}

	protected static class ManaCraftingRecipeManager
	{
		private final ItemStack stack;
		private final int value;
		private final Object[] list;

		public ManaCraftingRecipeManager(ItemStack result, int mana, Object... objects)
		{
			stack = result;
			value = mana;
			list = objects;
		}

		public ItemStack getResult()
		{
			return stack;
		}

		public int getMana()
		{
			return value;
		}

		public Object[] getObjects()
		{
			return list;
		}
	}

	protected static class ManaCompositionRecipeManager
	{
		private final ItemStack stack;
		private final int value;
		private final List<RecipeOreStack> list;

		public ManaCompositionRecipeManager(int tier, ItemStack result, List<RecipeOreStack> objects)
		{
			stack = result;
			value = tier;
			list = objects;
		}

		public ItemStack getResult()
		{
			return stack;
		}

		public int getTier()
		{
			return value;
		}

		public List<RecipeOreStack> getObjects()
		{
			return list;
		}
	}

	@SubscribeEvent
	public void onConfigChangedEvent(OnConfigChangedEvent event)
	{
		if (event.getModID().equals(Reference.MOD_ID))
		{
			ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
		}
	}

	public static void addOreDictSmelting(String oredict, ItemStack result, float exp)
	{
		for (ItemStack stack : OreDictionary.getOres(oredict))
		{
			GameRegistry.addSmelting(stack, result, exp);
		}
	}
}
