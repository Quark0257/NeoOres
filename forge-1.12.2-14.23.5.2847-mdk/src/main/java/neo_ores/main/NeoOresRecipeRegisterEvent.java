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
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_axe), 4000, "RR", "TR", "T ", 'R', "gemUndite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_pickaxe), 4000, "RRR", " T ", " T ", 'R', "gemUndite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_sword), 4000, "R", "R", "T", 'R', "gemUndite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_hoe), 4000, "RR", "T ", "T ", 'R', "gemUndite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_paxel), 4000, "ASP", "LTH", " T ", 'A', new ItemStack(NeoOresItems.undite_axe), 'H', new ItemStack(NeoOresItems.undite_hoe), 'P',
				new ItemStack(NeoOresItems.undite_pickaxe), 'L', new ItemStack(NeoOresItems.undite_shovel), 'S', new ItemStack(NeoOresItems.undite_sword), 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_shovel), 4000, "R", "T", "T", 'R', "gemUndite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_axe), 1000, "RR", "TR", "T ", 'R', "ingotGnomite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_pickaxe), 1000, "RRR", " T ", " T ", 'R', "ingotGnomite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_sword), 1000, "R", "R", "T", 'R', "ingotGnomite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_hoe), 1000, "RR", "T ", "T ", 'R', "ingotGnomite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_paxel), 1000, "ASP", "LTH", " T ", 'A', new ItemStack(NeoOresItems.gnomite_axe), 'H', new ItemStack(NeoOresItems.gnomite_hoe),
				'P', new ItemStack(NeoOresItems.gnomite_pickaxe), 'L', new ItemStack(NeoOresItems.gnomite_shovel), 'S', new ItemStack(NeoOresItems.gnomite_sword), 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_shovel), 1000, "R", "T", "T", 'R', "ingotGnomite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_axe), 64000, "RR", "TR", "T ", 'R', "gemSalamite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_pickaxe), 64000, "RRR", " T ", " T ", 'R', "gemSalamite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_sword), 64000, "R", "R", "T", 'R', "gemSalamite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_hoe), 64000, "RR", "T ", "T ", 'R', "gemSalamite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_paxel), 64000, "ASP", "LTH", " T ", 'A', new ItemStack(NeoOresItems.salamite_axe), 'H', new ItemStack(NeoOresItems.salamite_hoe),
				'P', new ItemStack(NeoOresItems.salamite_pickaxe), 'L', new ItemStack(NeoOresItems.salamite_shovel), 'S', new ItemStack(NeoOresItems.salamite_sword), 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_shovel), 64000, "R", "T", "T", 'R', "gemSalamite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_axe), 16000, "RR", "TR", "T ", 'R', "gemSylphite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_pickaxe), 16000, "RRR", " T ", " T ", 'R', "gemSylphite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_sword), 16000, "R", "R", "T", 'R', "gemSylphite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_hoe), 16000, "RR", "T ", "T ", 'R', "gemSylphite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_paxel), 16000, "ASP", "LTH", " T ", 'A', new ItemStack(NeoOresItems.sylphite_axe), 'H', new ItemStack(NeoOresItems.sylphite_hoe),
				'P', new ItemStack(NeoOresItems.sylphite_pickaxe), 'L', new ItemStack(NeoOresItems.sylphite_shovel), 'S', new ItemStack(NeoOresItems.sylphite_sword), 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_shovel), 16000, "R", "T", "T", 'R', "gemSylphite", 'T', "stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.creative_paxel), 256000, "ASP", "LTH", " T ", 'A', new ItemStack(NeoOresItems.creative_axe), 'H',
				new ItemStack(NeoOresItems.creative_hoe), 'P', new ItemStack(NeoOresItems.creative_pickaxe), 'L', new ItemStack(NeoOresItems.creative_shovel), 'S',
				new ItemStack(NeoOresItems.creative_sword), 'T', "stickWood");
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
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_helmet), 1000, "PPP", "P P", 'P', "ingotGnomite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_chestplate), 1000, "P P", "PPP", "PPP", 'P', "ingotGnomite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_leggings), 1000, "PPP", "P P", "P P", 'P', "ingotGnomite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_boots), 1000, "P P", "P P", 'P', "ingotGnomite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_helmet), 64000, "PPP", "P P", 'P', "gemSalamite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_chestplate), 64000, "P P", "PPP", "PPP", 'P', "gemSalamite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_leggings), 64000, "PPP", "P P", "P P", 'P', "gemSalamite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_boots), 64000, "P P", "P P", 'P', "gemSalamite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_helmet), 16000, "PPP", "P P", 'P', "gemSylphite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_chestplate), 16000, "P P", "PPP", "PPP", 'P', "gemSylphite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_leggings), 16000, "PPP", "P P", "P P", 'P', "gemSylphite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_boots), 16000, "P P", "P P", 'P', "gemSylphite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_helmet), 4000, "PPP", "P P", 'P', "gemUndite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_chestplate), 4000, "P P", "PPP", "PPP", 'P', "gemUndite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_leggings), 4000, "PPP", "P P", "P P", 'P', "gemUndite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_boots), 4000, "P P", "P P", 'P', "gemUndite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.mob_bottle), 1000, "A", "D", "B", 'A', "gemAerite", 'D', "dyeBlack", 'B', new ItemStack(Items.GLASS_BOTTLE));
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.mob_bottle_master), 10000, "D", "B", 'D', new ItemStack(Blocks.DRAGON_EGG), 'B', new ItemStack(NeoOresItems.mob_bottle));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.mana_furnace), 256000, " F ", "AXW", " E ", 'F', new ItemStack(NeoOresItems.essence, 1, 2), 'A',
				new ItemStack(NeoOresItems.essence, 1, 3), 'E', new ItemStack(NeoOresItems.essence, 1, 0), 'W', new ItemStack(NeoOresItems.essence, 1, 1), 'X',
				new ItemStack(NeoOresBlocks.mana_block));
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.earth_essence_core, 1, 0), 100, " E ", "GXG", " E ", 'E', new ItemStack(NeoOresItems.essence, 1, 0), 'G', "ingotGnomite", 'X',
				"netherStar");
		for (int i = 0; i < 10; i++)
			this.addManaCraftingRecipe(new ItemStack(NeoOresItems.earth_essence_core, 1, i + 1), 100, " E ", "CXC", " E ", 'E', new ItemStack(NeoOresItems.essence, 1, 0), 'C',
					new ItemStack(NeoOresItems.earth_essence_core, 1, i), 'X', "netherStar");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.water_essence_core, 1, 0), 100, " E ", "GXG", " E ", 'E', new ItemStack(NeoOresItems.essence, 1, 1), 'G', "gemUndite", 'X', "netherStar");
		for (int i = 0; i < 10; i++)
			this.addManaCraftingRecipe(new ItemStack(NeoOresItems.water_essence_core, 1, i + 1), 100, " E ", "CXC", " E ", 'E', new ItemStack(NeoOresItems.essence, 1, 1), 'C',
					new ItemStack(NeoOresItems.water_essence_core, 1, i), 'X', "netherStar");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.air_essence_core, 1, 0), 100, " E ", "GXG", " E ", 'E', new ItemStack(NeoOresItems.essence, 1, 3), 'G', "gemSylphite", 'X', "netherStar");
		for (int i = 0; i < 10; i++)
			this.addManaCraftingRecipe(new ItemStack(NeoOresItems.air_essence_core, 1, i + 1), 100, " E ", "CXC", " E ", 'E', new ItemStack(NeoOresItems.essence, 1, 3), 'C',
					new ItemStack(NeoOresItems.air_essence_core, 1, i), 'X', "netherStar");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.fire_essence_core, 1, 0), 100, " E ", "GXG", " E ", 'E', new ItemStack(NeoOresItems.essence, 1, 2), 'G', "gemSalamite", 'X',
				"netherStar");
		for (int i = 0; i < 10; i++)
			this.addManaCraftingRecipe(new ItemStack(NeoOresItems.fire_essence_core, 1, i + 1), 100, " E ", "CXC", " E ", 'E', new ItemStack(NeoOresItems.essence, 1, 2), 'C',
					new ItemStack(NeoOresItems.fire_essence_core, 1, i), 'X', "netherStar");

		int n = 0;
		for (ManaCraftingRecipeManager mcrm : manacraftingrecipes)
		{
			event.getRegistry().register(new ManaCraftingRecipe(mcrm.getResult(), mcrm.getMana(), mcrm.getObjects()).setRegistryName(Reference.MOD_ID, "manaCrafting." + n));
			n++;
		}
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
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.essence, 1, 1), "HHH", "HXH", "HHH", 'H',
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), 'X', "ingotIron").setRegistryName(Reference.MOD_ID, "water_essence"));
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
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.dim_planks, 4, 0), "X", 'X', new ItemStack(NeoOresBlocks.dim_log, 1, 0))
						.setRegistryName(Reference.MOD_ID, "earth_planks"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.dim_planks, 4, 1), "X", 'X', new ItemStack(NeoOresBlocks.dim_log, 1, 1))
						.setRegistryName(Reference.MOD_ID, "water_planks"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.dim_planks, 4, 2), "X", 'X', new ItemStack(NeoOresBlocks.dim_log, 1, 2))
						.setRegistryName(Reference.MOD_ID, "air_planks"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.dim_planks, 4, 3), "X", 'X', new ItemStack(NeoOresBlocks.dim_log, 1, 3))
						.setRegistryName(Reference.MOD_ID, "fire_planks"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.sylphite_block, 1), "XXX", "XXX", "XXX", 'X',
				new ItemStack(NeoOresItems.sylphite, 1, 0)).setRegistryName(Reference.MOD_ID, "sylphite_comp"));
		event.getRegistry().register(
				new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.undite_block, 1), "XXX", "XXX", "XXX", 'X', new ItemStack(NeoOresItems.undite, 1, 0))
						.setRegistryName(Reference.MOD_ID, "undite_comp"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.gnomite_block, 1), "XXX", "XXX", "XXX", 'X',
				new ItemStack(NeoOresItems.gnomite_ingot, 1, 0)).setRegistryName(Reference.MOD_ID, "gnomite_comp"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.salamite_block, 1), "XXX", "XXX", "XXX", 'X',
				new ItemStack(NeoOresItems.salamite, 1, 0)).setRegistryName(Reference.MOD_ID, "salamite_comp"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.sanitite_block, 1), "XXX", "XXX", "XXX", 'X',
				new ItemStack(NeoOresItems.sanitite, 1, 0)).setRegistryName(Reference.MOD_ID, "sanitite_comp"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.marlite_block, 1), "XXX", "XXX", "XXX", 'X',
				new ItemStack(NeoOresItems.marlite_ingot, 1, 0)).setRegistryName(Reference.MOD_ID, "marlite_comp"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.landite_block, 1), "XXX", "XXX", "XXX", 'X',
				new ItemStack(NeoOresItems.landite_ingot, 1, 0)).setRegistryName(Reference.MOD_ID, "landite_comp"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.guardite_block, 1), "XXX", "XXX", "XXX", 'X',
				new ItemStack(NeoOresItems.guardite_ingot, 1, 0)).setRegistryName(Reference.MOD_ID, "guardite_comp"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.forcite_block, 1), "XXX", "XXX", "XXX", 'X',
				new ItemStack(NeoOresItems.forcite, 1, 0)).setRegistryName(Reference.MOD_ID, "forcite_comp"));
		event.getRegistry().register(
				new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.aerite_block, 1), "XXX", "XXX", "XXX", 'X', new ItemStack(NeoOresItems.aerite, 1, 0))
						.setRegistryName(Reference.MOD_ID, "aerite_comp"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.flamite_block, 1), "XXX", "XXX", "XXX", 'X',
				new ItemStack(NeoOresItems.flamite, 1, 0)).setRegistryName(Reference.MOD_ID, "flamite_comp"));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.drenite_block, 1), "XXX", "XXX", "XXX", 'X',
				new ItemStack(NeoOresItems.drenite, 1, 0)).setRegistryName(Reference.MOD_ID, "drenite_comp"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.sylphite, 9), "X", 'X', new ItemStack(NeoOresBlocks.sylphite_block, 1, 0))
						.setRegistryName(Reference.MOD_ID, "sylphite_decomp"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.undite, 9), "X", 'X', new ItemStack(NeoOresBlocks.undite_block, 1, 0))
						.setRegistryName(Reference.MOD_ID, "undite_decomp"));
		event.getRegistry().register(
				new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.gnomite_ingot, 9), "X", 'X', new ItemStack(NeoOresBlocks.gnomite_block, 1, 0))
						.setRegistryName(Reference.MOD_ID, "gnomite_decomp"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.salamite, 9), "X", 'X', new ItemStack(NeoOresBlocks.salamite_block, 1, 0))
						.setRegistryName(Reference.MOD_ID, "salamite_decomp"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.sanitite, 9), "X", 'X', new ItemStack(NeoOresBlocks.sanitite_block, 1, 0))
						.setRegistryName(Reference.MOD_ID, "sanitite_decomp"));
		event.getRegistry().register(
				new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.marlite_ingot, 9), "X", 'X', new ItemStack(NeoOresBlocks.marlite_block, 1, 0))
						.setRegistryName(Reference.MOD_ID, "marlite_decomp"));
		event.getRegistry().register(
				new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.landite_ingot, 9), "X", 'X', new ItemStack(NeoOresBlocks.landite_block, 1, 0))
						.setRegistryName(Reference.MOD_ID, "landite_decomp"));
		event.getRegistry().register(
				new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.guardite_ingot, 9), "X", 'X', new ItemStack(NeoOresBlocks.guardite_block, 1, 0))
						.setRegistryName(Reference.MOD_ID, "guardite_decomp"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.forcite, 9), "X", 'X', new ItemStack(NeoOresBlocks.forcite_block, 1, 0))
						.setRegistryName(Reference.MOD_ID, "forcite_decomp"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.aerite, 9), "X", 'X', new ItemStack(NeoOresBlocks.aerite_block, 1, 0))
						.setRegistryName(Reference.MOD_ID, "aerite_decomp"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.flamite, 9), "X", 'X', new ItemStack(NeoOresBlocks.flamite_block, 1, 0))
						.setRegistryName(Reference.MOD_ID, "flamite_decomp"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.drenite, 9), "X", 'X', new ItemStack(NeoOresBlocks.drenite_block, 1, 0))
						.setRegistryName(Reference.MOD_ID, "drenite_decomp"));

		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresBlocks.mana_block, 1), "XXX", "XXX", "XXX", 'X',
				new ItemStack(NeoOresItems.mana_ingot, 1, 0)).setRegistryName(Reference.MOD_ID, "mana_comp"));
		event.getRegistry()
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID, "recipes"), new ItemStack(NeoOresItems.mana_ingot, 9), "X", 'X', new ItemStack(NeoOresBlocks.mana_block, 1, 0))
						.setRegistryName(Reference.MOD_ID, "mana_decomp"));

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
		this.addManaCompositionRecipe(0, new ItemStack(NeoOresBlocks.dim_brick, 1, 0), Arrays.asList(new RecipeOreStack("urystone", 4)));
		this.addManaCompositionRecipe(0, new ItemStack(NeoOresBlocks.dim_brick, 1, 1), Arrays.asList(new RecipeOreStack("gabrystone", 4)));
		this.addManaCompositionRecipe(0, new ItemStack(NeoOresBlocks.dim_brick, 1, 2), Arrays.asList(new RecipeOreStack("raphastone", 4)));
		this.addManaCompositionRecipe(0, new ItemStack(NeoOresBlocks.dim_brick, 1, 3), Arrays.asList(new RecipeOreStack("michastone", 4)));
		this.addManaCompositionRecipe(11, new ItemStack(NeoOresItems.mana_ingot, 64),
				Arrays.asList(new RecipeOreStack("netherStar", 1), new RecipeOreStack(new ItemStack(NeoOresItems.essence, 1, 0), 1), new RecipeOreStack(new ItemStack(NeoOresItems.essence, 1, 1), 1),
						new RecipeOreStack(new ItemStack(NeoOresItems.essence, 1, 2), 1), new RecipeOreStack(new ItemStack(NeoOresItems.essence, 1, 3), 1)));

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

				JsonArray acceptModsDimStoneOre = new JsonArray();
				acceptModsDimStoneOre.add("neo_ores");

				JsonObject urystone = new JsonObject();
				urystone.addProperty("id", "neo_ores:dim_stone");
				urystone.addProperty("metadata", 0);

				JsonObject oreIronUryStone = new JsonObject();
				oreIronUryStone.addProperty("registry_name", "oredic_ore_iron_urystone");
				oreIronUryStone.addProperty("id", "neo_ores:custom_iron_ore");
				oreIronUryStone.addProperty("metadata", 0);
				oreIronUryStone.addProperty("weight", 40680);
				oreIronUryStone.add("replace_block", urystone);
				oreIronUryStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreIronUryStone);

				JsonObject oreGoldUryStone = new JsonObject();
				oreGoldUryStone.addProperty("registry_name", "oredic_ore_gold_urystone");
				oreGoldUryStone.addProperty("id", "neo_ores:custom_gold_ore");
				oreGoldUryStone.addProperty("metadata", 0);
				oreGoldUryStone.addProperty("weight", 4608);
				oreGoldUryStone.add("replace_block", urystone);
				oreGoldUryStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreGoldUryStone);

				JsonObject oreDiamondUryStone = new JsonObject();
				oreDiamondUryStone.addProperty("registry_name", "oredic_ore_diamond_urystone");
				oreDiamondUryStone.addProperty("id", "neo_ores:custom_diamond_ore");
				oreDiamondUryStone.addProperty("metadata", 0);
				oreDiamondUryStone.addProperty("weight", 2048);
				oreDiamondUryStone.add("replace_block", urystone);
				oreDiamondUryStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreDiamondUryStone);

				JsonObject oreEmeraldUryStone = new JsonObject();
				oreEmeraldUryStone.addProperty("registry_name", "oredic_ore_emerald_urystone");
				oreEmeraldUryStone.addProperty("id", "neo_ores:custom_emerald_ore");
				oreEmeraldUryStone.addProperty("metadata", 0);
				oreEmeraldUryStone.addProperty("weight", 1152);
				oreEmeraldUryStone.add("replace_block", urystone);
				oreEmeraldUryStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreEmeraldUryStone);

				JsonObject oreRedstoneUryStone = new JsonObject();
				oreRedstoneUryStone.addProperty("registry_name", "oredic_ore_redstone_urystone");
				oreRedstoneUryStone.addProperty("id", "neo_ores:custom_redstone_ore");
				oreRedstoneUryStone.addProperty("metadata", 0);
				oreRedstoneUryStone.addProperty("weight", 16384);
				oreRedstoneUryStone.add("replace_block", urystone);
				oreRedstoneUryStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreRedstoneUryStone);

				JsonObject oreLapisUryStone = new JsonObject();
				oreLapisUryStone.addProperty("registry_name", "oredic_ore_lapis_urystone");
				oreLapisUryStone.addProperty("id", "neo_ores:custom_lapis_ore");
				oreLapisUryStone.addProperty("metadata", 0);
				oreLapisUryStone.addProperty("weight", 3584);
				oreLapisUryStone.add("replace_block", urystone);
				oreLapisUryStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreLapisUryStone);

				JsonObject oreQuartzUryStone = new JsonObject();
				oreQuartzUryStone.addProperty("registry_name", "oredic_ore_quartz_urystone");
				oreQuartzUryStone.addProperty("id", "neo_ores:custom_quartz_ore");
				oreQuartzUryStone.addProperty("metadata", 0);
				oreQuartzUryStone.addProperty("weight", 43068);
				oreQuartzUryStone.add("replace_block", urystone);
				oreQuartzUryStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreQuartzUryStone);

				JsonObject oreCoalUryStone = new JsonObject();
				oreCoalUryStone.addProperty("registry_name", "oredic_ore_coal_urystone");
				oreCoalUryStone.addProperty("id", "neo_ores:custom_coal_ore");
				oreCoalUryStone.addProperty("metadata", 0);
				oreCoalUryStone.addProperty("weight", 87040);
				oreCoalUryStone.add("replace_block", urystone);
				oreCoalUryStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreCoalUryStone);

				JsonObject oreGuarditeUryStone = new JsonObject();
				oreGuarditeUryStone.addProperty("registry_name", "oredic_ore_guardite_urystone");
				oreGuarditeUryStone.addProperty("id", "neo_ores:guardite_ore");
				oreGuarditeUryStone.addProperty("weight", 5280);
				oreGuarditeUryStone.add("replace_block", urystone);
				oreGuarditeUryStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreGuarditeUryStone);

				JsonObject oreLanditeUryStone = new JsonObject();
				oreLanditeUryStone.addProperty("registry_name", "oredic_ore_landite_urystone");
				oreLanditeUryStone.addProperty("id", "neo_ores:landite_ore");
				oreLanditeUryStone.addProperty("weight", 5280);
				oreLanditeUryStone.add("replace_block", urystone);
				oreLanditeUryStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreLanditeUryStone);

				JsonObject gabrystone = new JsonObject();
				gabrystone.addProperty("id", "neo_ores:dim_stone");
				gabrystone.addProperty("metadata", 1);

				JsonObject oreIronGabryStone = new JsonObject();
				oreIronGabryStone.addProperty("registry_name", "oredic_ore_iron_gabrystone");
				oreIronGabryStone.addProperty("id", "neo_ores:custom_iron_ore");
				oreIronGabryStone.addProperty("metadata", 1);
				oreIronGabryStone.addProperty("weight", 40680);
				oreIronGabryStone.add("replace_block", gabrystone);
				oreIronGabryStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreIronGabryStone);

				JsonObject oreGoldGabryStone = new JsonObject();
				oreGoldGabryStone.addProperty("registry_name", "oredic_ore_gold_gabrystone");
				oreGoldGabryStone.addProperty("id", "neo_ores:custom_gold_ore");
				oreGoldGabryStone.addProperty("metadata", 1);
				oreGoldGabryStone.addProperty("weight", 4068);
				oreGoldGabryStone.add("replace_block", gabrystone);
				oreGoldGabryStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreGoldGabryStone);

				JsonObject oreDiamondGabryStone = new JsonObject();
				oreDiamondGabryStone.addProperty("registry_name", "oredic_ore_diamond_gabrystone");
				oreDiamondGabryStone.addProperty("id", "neo_ores:custom_diamond_ore");
				oreDiamondGabryStone.addProperty("metadata", 1);
				oreDiamondGabryStone.addProperty("weight", 2048);
				oreDiamondGabryStone.add("replace_block", gabrystone);
				oreDiamondGabryStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreDiamondGabryStone);

				JsonObject oreEmeraldGabryStone = new JsonObject();
				oreEmeraldGabryStone.addProperty("registry_name", "oredic_ore_emerald_gabrystone");
				oreEmeraldGabryStone.addProperty("id", "neo_ores:custom_emerald_ore");
				oreEmeraldGabryStone.addProperty("metadata", 1);
				oreEmeraldGabryStone.addProperty("weight", 1152);
				oreEmeraldGabryStone.add("replace_block", gabrystone);
				oreEmeraldGabryStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreEmeraldGabryStone);

				JsonObject oreRedstoneGabryStone = new JsonObject();
				oreRedstoneGabryStone.addProperty("registry_name", "oredic_ore_redstone_gabrystone");
				oreRedstoneGabryStone.addProperty("id", "neo_ores:custom_redstone_ore");
				oreRedstoneGabryStone.addProperty("metadata", 1);
				oreRedstoneGabryStone.addProperty("weight", 16384);
				oreRedstoneGabryStone.add("replace_block", gabrystone);
				oreRedstoneGabryStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreRedstoneGabryStone);

				JsonObject oreLapisGabryStone = new JsonObject();
				oreLapisGabryStone.addProperty("registry_name", "oredic_ore_lapis_gabrystone");
				oreLapisGabryStone.addProperty("id", "neo_ores:custom_lapis_ore");
				oreLapisGabryStone.addProperty("metadata", 1);
				oreLapisGabryStone.addProperty("weight", 3584);
				oreLapisGabryStone.add("replace_block", gabrystone);
				oreLapisGabryStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreLapisGabryStone);

				JsonObject oreQuartzGabryStone = new JsonObject();
				oreQuartzGabryStone.addProperty("registry_name", "oredic_ore_quartz_gabrystone");
				oreQuartzGabryStone.addProperty("id", "neo_ores:custom_quartz_ore");
				oreQuartzGabryStone.addProperty("metadata", 1);
				oreQuartzGabryStone.addProperty("weight", 43068);
				oreQuartzGabryStone.add("replace_block", gabrystone);
				oreQuartzGabryStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreQuartzGabryStone);

				JsonObject oreCoalGabryStone = new JsonObject();
				oreCoalGabryStone.addProperty("registry_name", "oredic_ore_coal_gabrystone");
				oreCoalGabryStone.addProperty("id", "neo_ores:custom_coal_ore");
				oreCoalGabryStone.addProperty("metadata", 1);
				oreCoalGabryStone.addProperty("weight", 87040);
				oreCoalGabryStone.add("replace_block", gabrystone);
				oreCoalGabryStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreCoalGabryStone);

				JsonObject oreMarliteGabryStone = new JsonObject();
				oreMarliteGabryStone.addProperty("registry_name", "oredic_ore_marlite_gabrystone");
				oreMarliteGabryStone.addProperty("id", "neo_ores:marlite_ore");
				oreMarliteGabryStone.addProperty("weight", 2400);
				oreMarliteGabryStone.add("replace_block", gabrystone);
				oreMarliteGabryStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreMarliteGabryStone);

				JsonObject oreSanititeGabryStone = new JsonObject();
				oreSanititeGabryStone.addProperty("registry_name", "oredic_ore_sanitite_gabrystone");
				oreSanititeGabryStone.addProperty("id", "neo_ores:sanitite_ore");
				oreSanititeGabryStone.addProperty("weight", 2400);
				oreSanititeGabryStone.add("replace_block", gabrystone);
				oreSanititeGabryStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreSanititeGabryStone);

				JsonObject raphastone = new JsonObject();
				raphastone.addProperty("id", "neo_ores:dim_stone");
				raphastone.addProperty("metadata", 2);

				JsonObject oreIronRaphaStone = new JsonObject();
				oreIronRaphaStone.addProperty("registry_name", "oredic_ore_iron_raphastone");
				oreIronRaphaStone.addProperty("id", "neo_ores:custom_iron_ore");
				oreIronRaphaStone.addProperty("metadata", 2);
				oreIronRaphaStone.addProperty("weight", 40680);
				oreIronRaphaStone.add("replace_block", raphastone);
				oreIronRaphaStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreIronRaphaStone);

				JsonObject oreGoldRaphaStone = new JsonObject();
				oreGoldRaphaStone.addProperty("registry_name", "oredic_ore_gold_raphastone");
				oreGoldRaphaStone.addProperty("id", "neo_ores:custom_gold_ore");
				oreGoldRaphaStone.addProperty("metadata", 2);
				oreGoldRaphaStone.addProperty("weight", 4608);
				oreGoldRaphaStone.add("replace_block", raphastone);
				oreGoldRaphaStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreGoldRaphaStone);

				JsonObject oreDiamondRaphaStone = new JsonObject();
				oreDiamondRaphaStone.addProperty("registry_name", "oredic_ore_diamond_raphastone");
				oreDiamondRaphaStone.addProperty("id", "neo_ores:custom_diamond_ore");
				oreDiamondRaphaStone.addProperty("metadata", 2);
				oreDiamondRaphaStone.addProperty("weight", 2048);
				oreDiamondRaphaStone.add("replace_block", raphastone);
				oreDiamondRaphaStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreDiamondRaphaStone);

				JsonObject oreEmeraldRaphaStone = new JsonObject();
				oreEmeraldRaphaStone.addProperty("registry_name", "oredic_ore_emerald_raphastone");
				oreEmeraldRaphaStone.addProperty("id", "neo_ores:custom_emerald_ore");
				oreEmeraldRaphaStone.addProperty("metadata", 2);
				oreEmeraldRaphaStone.addProperty("weight", 1152);
				oreEmeraldRaphaStone.add("replace_block", raphastone);
				oreEmeraldRaphaStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreEmeraldRaphaStone);

				JsonObject oreRedstoneRaphaStone = new JsonObject();
				oreRedstoneRaphaStone.addProperty("registry_name", "oredic_ore_redstone_raphastone");
				oreRedstoneRaphaStone.addProperty("id", "neo_ores:custom_redstone_ore");
				oreRedstoneRaphaStone.addProperty("metadata", 2);
				oreRedstoneRaphaStone.addProperty("weight", 16384);
				oreRedstoneRaphaStone.add("replace_block", raphastone);
				oreRedstoneRaphaStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreRedstoneRaphaStone);

				JsonObject oreLapisRaphaStone = new JsonObject();
				oreLapisRaphaStone.addProperty("registry_name", "oredic_ore_lapis_raphastone");
				oreLapisRaphaStone.addProperty("id", "neo_ores:custom_lapis_ore");
				oreLapisRaphaStone.addProperty("metadata", 2);
				oreLapisRaphaStone.addProperty("weight", 3584);
				oreLapisRaphaStone.add("replace_block", raphastone);
				oreLapisRaphaStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreLapisRaphaStone);

				JsonObject oreQuartzRaphaStone = new JsonObject();
				oreQuartzRaphaStone.addProperty("registry_name", "oredic_ore_quartz_raphastone");
				oreQuartzRaphaStone.addProperty("id", "neo_ores:custom_quartz_ore");
				oreQuartzRaphaStone.addProperty("metadata", 2);
				oreQuartzRaphaStone.addProperty("weight", 43068);
				oreQuartzRaphaStone.add("replace_block", raphastone);
				oreQuartzRaphaStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreQuartzRaphaStone);

				JsonObject oreCoalRaphaStone = new JsonObject();
				oreCoalRaphaStone.addProperty("registry_name", "oredic_ore_coal_raphastone");
				oreCoalRaphaStone.addProperty("id", "neo_ores:custom_coal_ore");
				oreCoalRaphaStone.addProperty("metadata", 2);
				oreCoalRaphaStone.addProperty("weight", 87040);
				oreCoalRaphaStone.add("replace_block", raphastone);
				oreCoalRaphaStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreCoalRaphaStone);

				JsonObject oreAeriteRaphaStone = new JsonObject();
				oreAeriteRaphaStone.addProperty("registry_name", "oredic_ore_aerite_raphastone");
				oreAeriteRaphaStone.addProperty("id", "neo_ores:aerite_ore");
				oreAeriteRaphaStone.addProperty("weight", 1440);
				oreAeriteRaphaStone.add("replace_block", raphastone);
				oreAeriteRaphaStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreAeriteRaphaStone);

				JsonObject oreDreniteRaphaStone = new JsonObject();
				oreDreniteRaphaStone.addProperty("registry_name", "oredic_ore_drenite_raphastone");
				oreDreniteRaphaStone.addProperty("id", "neo_ores:drenite_ore");
				oreDreniteRaphaStone.addProperty("weight", 1440);
				oreDreniteRaphaStone.add("replace_block", raphastone);
				oreDreniteRaphaStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreDreniteRaphaStone);

				JsonObject michastone = new JsonObject();
				michastone.addProperty("id", "neo_ores:dim_stone");
				michastone.addProperty("metadata", 3);

				JsonObject oreIronMichaStone = new JsonObject();
				oreIronMichaStone.addProperty("registry_name", "oredic_ore_iron_michastone");
				oreIronMichaStone.addProperty("id", "neo_ores:custom_iron_ore");
				oreIronMichaStone.addProperty("metadata", 3);
				oreIronMichaStone.addProperty("weight", 40680);
				oreIronMichaStone.add("replace_block", michastone);
				oreIronMichaStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreIronMichaStone);

				JsonObject oreGoldMichaStone = new JsonObject();
				oreGoldMichaStone.addProperty("registry_name", "oredic_ore_gold_michastone");
				oreGoldMichaStone.addProperty("id", "neo_ores:custom_gold_ore");
				oreGoldMichaStone.addProperty("metadata", 3);
				oreGoldMichaStone.addProperty("weight", 4608);
				oreGoldMichaStone.add("replace_block", michastone);
				oreGoldMichaStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreGoldMichaStone);

				JsonObject oreDiamondMichaStone = new JsonObject();
				oreDiamondMichaStone.addProperty("registry_name", "oredic_ore_diamond_michastone");
				oreDiamondMichaStone.addProperty("id", "neo_ores:custom_diamond_ore");
				oreDiamondMichaStone.addProperty("metadata", 3);
				oreDiamondMichaStone.addProperty("weight", 2048);
				oreDiamondMichaStone.add("replace_block", michastone);
				oreDiamondMichaStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreDiamondMichaStone);

				JsonObject oreEmeraldMichaStone = new JsonObject();
				oreEmeraldMichaStone.addProperty("registry_name", "oredic_ore_emerald_michastone");
				oreEmeraldMichaStone.addProperty("id", "neo_ores:custom_emerald_ore");
				oreEmeraldMichaStone.addProperty("metadata", 3);
				oreEmeraldMichaStone.addProperty("weight", 1152);
				oreEmeraldMichaStone.add("replace_block", michastone);
				oreEmeraldMichaStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreEmeraldMichaStone);

				JsonObject oreRedstoneMichaStone = new JsonObject();
				oreRedstoneMichaStone.addProperty("registry_name", "oredic_ore_redstone_michastone");
				oreRedstoneMichaStone.addProperty("id", "neo_ores:custom_redstone_ore");
				oreRedstoneMichaStone.addProperty("metadata", 3);
				oreRedstoneMichaStone.addProperty("weight", 16384);
				oreRedstoneMichaStone.add("replace_block", michastone);
				oreRedstoneMichaStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreRedstoneMichaStone);

				JsonObject oreLapisMichaStone = new JsonObject();
				oreLapisMichaStone.addProperty("registry_name", "oredic_ore_lapis_michastone");
				oreLapisMichaStone.addProperty("id", "neo_ores:custom_lapis_ore");
				oreLapisMichaStone.addProperty("metadata", 3);
				oreLapisMichaStone.addProperty("weight", 3584);
				oreLapisMichaStone.add("replace_block", michastone);
				oreLapisMichaStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreLapisMichaStone);

				JsonObject oreQuartzMichaStone = new JsonObject();
				oreQuartzMichaStone.addProperty("registry_name", "oredic_ore_quartz_michastone");
				oreQuartzMichaStone.addProperty("id", "neo_ores:custom_quartz_ore");
				oreQuartzMichaStone.addProperty("metadata", 3);
				oreQuartzMichaStone.addProperty("weight", 43068);
				oreQuartzMichaStone.add("replace_block", michastone);
				oreQuartzMichaStone.add("acceptMods", acceptModsDimStoneOre);
				spell_ore_gen.add(oreQuartzMichaStone);

				JsonObject oreCoalMichaStone = new JsonObject();
				oreCoalMichaStone.addProperty("registry_name", "oredic_ore_coal_michastone");
				oreCoalMichaStone.addProperty("id", "neo_ores:custom_coal_ore");
				oreCoalMichaStone.addProperty("metadata", 3);
				oreCoalMichaStone.addProperty("weight", 87040);
				oreCoalMichaStone.add("replace_block", michastone);
				oreCoalMichaStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreCoalMichaStone);

				JsonObject oreForciteMichaStone = new JsonObject();
				oreForciteMichaStone.addProperty("registry_name", "oredic_ore_forcite_michastone");
				oreForciteMichaStone.addProperty("id", "neo_ores:forcite_ore");
				oreForciteMichaStone.addProperty("weight", 960);
				oreForciteMichaStone.add("replace_block", michastone);
				oreForciteMichaStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreForciteMichaStone);

				JsonObject oreFlamiteMichaStone = new JsonObject();
				oreFlamiteMichaStone.addProperty("registry_name", "oredic_ore_flamite_michastone");
				oreFlamiteMichaStone.addProperty("id", "neo_ores:flamite_ore");
				oreFlamiteMichaStone.addProperty("weight", 960);
				oreFlamiteMichaStone.add("replace_block", michastone);
				oreFlamiteMichaStone.add("acceptMods", acceptModsStoneOre);
				spell_ore_gen.add(oreFlamiteMichaStone);

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
}
