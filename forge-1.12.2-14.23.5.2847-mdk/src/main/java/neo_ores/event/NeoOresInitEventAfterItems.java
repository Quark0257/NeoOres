package neo_ores.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import neo_ores.api.ItemStackWithSizeForRecipe;
import neo_ores.api.recipe.ManaCompositionRecipe;
import neo_ores.api.recipe.ManaCraftingRecipe;
import neo_ores.api.recipe.SpellRecipe;
import neo_ores.api.spell.SpellItem;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.NeoOresItems;
import neo_ores.main.NeoOresSpells;
import neo_ores.main.Reference;
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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class NeoOresInitEventAfterItems 
{	
	private List<ManaCraftingRecipeManager> manacraftingrecipes = new ArrayList<ManaCraftingRecipeManager>();
	private List<ManaCompositionRecipeManager> manacompositionrecipes = new ArrayList<ManaCompositionRecipeManager>();
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void registerSpellItems(final RegistryEvent.Register<SpellItem> event)
	{
		for(SpellItem spell : NeoOresSpells.registry)
		{
			event.getRegistry().register(spell);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void registerSpellRecipes(final RegistryEvent.Register<SpellRecipe> event)
	{
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_dig,new ItemStackWithSizeForRecipe(new ItemStack(Items.IRON_SHOVEL),1),new ItemStackWithSizeForRecipe(new ItemStack(Items.IRON_AXE),1),new ItemStackWithSizeForRecipe(new ItemStack(Items.WOODEN_PICKAXE),1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_touch,new ItemStackWithSizeForRecipe("stickWood",1),new ItemStackWithSizeForRecipe("feather",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_support_liquid,new ItemStackWithSizeForRecipe(new ItemStack(Items.BUCKET),1),new ItemStackWithSizeForRecipe(new ItemStack(Items.SHEARS),1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_composition,new ItemStackWithSizeForRecipe("enhancedPedestalAll",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_earth_damage,new ItemStackWithSizeForRecipe(new ItemStack(Items.WOODEN_SWORD),4)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv1,new ItemStackWithSizeForRecipe(new ItemStack(Items.STONE_PICKAXE),1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv2,new ItemStackWithSizeForRecipe(new ItemStack(Items.IRON_PICKAXE),1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv3,new ItemStackWithSizeForRecipe(new ItemStack(Items.DIAMOND_PICKAXE),1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv4,new ItemStackWithSizeForRecipe("obsidian",16)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv5,new ItemStackWithSizeForRecipe(new ItemStack(NeoOresItems.gnomite_pickaxe),1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv6,new ItemStackWithSizeForRecipe(new ItemStack(NeoOresItems.undite_pickaxe),1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv7,new ItemStackWithSizeForRecipe(new ItemStack(NeoOresItems.sylphite_pickaxe),1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv8,new ItemStackWithSizeForRecipe(new ItemStack(NeoOresItems.sylphite_pickaxe),1),new ItemStackWithSizeForRecipe("gemDiamond",64)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv9,new ItemStackWithSizeForRecipe(new ItemStack(NeoOresItems.salamite_pickaxe),1),new ItemStackWithSizeForRecipe("gemDiamond",128)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv10,new ItemStackWithSizeForRecipe(new ItemStack(NeoOresItems.salamite_pickaxe),1),new ItemStackWithSizeForRecipe("gemDiamond",256)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_harvestLv11,new ItemStackWithSizeForRecipe(new ItemStack(NeoOresItems.gnomite_paxel),1),
				new ItemStackWithSizeForRecipe(new ItemStack(NeoOresItems.sylphite_paxel),1),new ItemStackWithSizeForRecipe(new ItemStack(NeoOresItems.undite_paxel),1),new ItemStackWithSizeForRecipe(new ItemStack(NeoOresItems.salamite_paxel),1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck1,new ItemStackWithSizeForRecipe("gemLapis",8),new ItemStackWithSizeForRecipe("gemDiamond",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck2,new ItemStackWithSizeForRecipe("gemLapis",16),new ItemStackWithSizeForRecipe("gemDiamond",2)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck3,new ItemStackWithSizeForRecipe("gemLapis",32),new ItemStackWithSizeForRecipe("gemDiamond",3)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck4,new ItemStackWithSizeForRecipe("gemLapis",64),new ItemStackWithSizeForRecipe("gemDiamond",4)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck5,new ItemStackWithSizeForRecipe("gemLapis",128),new ItemStackWithSizeForRecipe("gemDiamond",5)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck6,new ItemStackWithSizeForRecipe("gemLapis",256),new ItemStackWithSizeForRecipe("gemDiamond",6)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck7,new ItemStackWithSizeForRecipe("gemLapis",512),new ItemStackWithSizeForRecipe("gemDiamond",7)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck8,new ItemStackWithSizeForRecipe("gemLapis",1024),new ItemStackWithSizeForRecipe("gemDiamond",8)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck9,new ItemStackWithSizeForRecipe("gemLapis",2048),new ItemStackWithSizeForRecipe("gemDiamond",9)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_luck10,new ItemStackWithSizeForRecipe("gemLapis",4096),new ItemStackWithSizeForRecipe("gemDiamond",10)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_silk,new ItemStackWithSizeForRecipe("gemEmerald",10),new ItemStackWithSizeForRecipe("wool",1),new ItemStackWithSizeForRecipe("feather",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier1,new ItemStackWithSizeForRecipe("blockEarthEssence",1),new ItemStackWithSizeForRecipe("blockWaterEssence",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier2,new ItemStackWithSizeForRecipe("blockFireEssence",1),new ItemStackWithSizeForRecipe("blockAirEssence",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier3,new ItemStackWithSizeForRecipe("gemDiamond",1),new ItemStackWithSizeForRecipe("gemEmerald",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_bullet,new ItemStackWithSizeForRecipe(new ItemStack(Items.SNOWBALL),1),new ItemStackWithSizeForRecipe(new ItemStack(Items.ARROW),1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_speed1,new ItemStackWithSizeForRecipe("ingotGold",4)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_speed2,new ItemStackWithSizeForRecipe("ingotGold",8)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_speed3,new ItemStackWithSizeForRecipe("ingotGold",16)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_speed4,new ItemStackWithSizeForRecipe("ingotGold",32)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_continuation1,new ItemStackWithSizeForRecipe("enderpearl",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_continuation2,new ItemStackWithSizeForRecipe("enderpearl",2)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_continuation3,new ItemStackWithSizeForRecipe("enderpearl",4)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_continuation4,new ItemStackWithSizeForRecipe("enderpearl",8)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_noGravity,new ItemStackWithSizeForRecipe("enderpearl",16)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_noAnyResistance,new ItemStackWithSizeForRecipe("ingotGold",64),new ItemStackWithSizeForRecipe(new ItemStack(Items.ARROW),4)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier4,new ItemStackWithSizeForRecipe("ingotLandite",1),new ItemStackWithSizeForRecipe("ingotGuardite",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier5,new ItemStackWithSizeForRecipe("ingotGnomite",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier6,new ItemStackWithSizeForRecipe("ingotMarlite",1),new ItemStackWithSizeForRecipe("gemSanitite",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier7,new ItemStackWithSizeForRecipe("gemUndite",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier8,new ItemStackWithSizeForRecipe("gemDrenite",1),new ItemStackWithSizeForRecipe("gemAerite",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier9,new ItemStackWithSizeForRecipe("gemSylphite",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier10,new ItemStackWithSizeForRecipe("gemFlamite",1),new ItemStackWithSizeForRecipe("gemForcite",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_tier11,new ItemStackWithSizeForRecipe("gemSalamite",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_range1,new ItemStackWithSizeForRecipe("dustGlowstone",16),new ItemStackWithSizeForRecipe("string",1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_gather,new ItemStackWithSizeForRecipe(new ItemStack(Blocks.HOPPER),1),new ItemStackWithSizeForRecipe(new ItemStack(Items.WATER_BUCKET),1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_range2,new ItemStackWithSizeForRecipe("dustGlowstone",64),new ItemStackWithSizeForRecipe("string",2)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_range3,new ItemStackWithSizeForRecipe("dustGlowstone",256),new ItemStackWithSizeForRecipe("string",4)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_range4,new ItemStackWithSizeForRecipe("dustGlowstone",1024),new ItemStackWithSizeForRecipe("string",8)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv1,new ItemStackWithSizeForRecipe(new ItemStack(Items.SPIDER_EYE),4),new ItemStackWithSizeForRecipe("gemQuartz",4)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv2,new ItemStackWithSizeForRecipe(new ItemStack(Items.SPIDER_EYE),8),new ItemStackWithSizeForRecipe("gemQuartz",8)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv3,new ItemStackWithSizeForRecipe(new ItemStack(Items.SPIDER_EYE),16),new ItemStackWithSizeForRecipe("gemQuartz",16)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv4,new ItemStackWithSizeForRecipe(new ItemStack(Items.SPIDER_EYE),32),new ItemStackWithSizeForRecipe("gemQuartz",32)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv5,new ItemStackWithSizeForRecipe(new ItemStack(Items.SPIDER_EYE),64),new ItemStackWithSizeForRecipe("gemQuartz",64)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv6,new ItemStackWithSizeForRecipe(new ItemStack(Items.SPIDER_EYE),128),new ItemStackWithSizeForRecipe("gemQuartz",128)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv7,new ItemStackWithSizeForRecipe(new ItemStack(Items.SPIDER_EYE),256),new ItemStackWithSizeForRecipe("gemQuartz",256)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv8,new ItemStackWithSizeForRecipe(new ItemStack(Items.SPIDER_EYE),1024),new ItemStackWithSizeForRecipe("gemQuartz",1024)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv9,new ItemStackWithSizeForRecipe(new ItemStack(Items.SPIDER_EYE),2048),new ItemStackWithSizeForRecipe("gemQuartz",2048)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv10,new ItemStackWithSizeForRecipe(new ItemStack(Items.SPIDER_EYE),4096),new ItemStackWithSizeForRecipe("gemQuartz",4096)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_damageLv11,new ItemStackWithSizeForRecipe(new ItemStack(Items.SPIDER_EYE),8192),new ItemStackWithSizeForRecipe("gemQuartz",8192)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_summon, new ItemStackWithSizeForRecipe("mobBottle",1),new ItemStackWithSizeForRecipe(new ItemStack(Items.NETHER_STAR),1)));
		event.getRegistry().register(new SpellRecipe(NeoOresSpells.spell_nbt_applying, new ItemStackWithSizeForRecipe(new ItemStack(Items.NETHER_STAR),64)));
	}

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void registerManaCraftingRecipe(final RegistryEvent.Register<ManaCraftingRecipe> event)
	{
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_axe), 10000, "RR", "TR","T ",'R', "gemUndite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_pickaxe), 10000, "RRR", " T "," T ",'R', "gemUndite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_sword), 10000, "R", "R","T",'R', "gemUndite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_hoe), 10000, "RR", "T ","T ",'R', "gemUndite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_paxel), 10000, "ASP", "LTH"," T ",'A', new ItemStack(NeoOresItems.undite_axe),'H', new ItemStack(NeoOresItems.undite_hoe),'P', new ItemStack(NeoOresItems.undite_pickaxe),'L', new ItemStack(NeoOresItems.undite_shovel),'S', new ItemStack(NeoOresItems.undite_sword), 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_shovel), 10000, "R", "T","T",'R', "gemUndite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_axe), 10000, "RR", "TR","T ",'R', "ingotGnomite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_pickaxe), 10000, "RRR", " T "," T ",'R', "ingotGnomite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_sword), 10000, "R", "R","T",'R', "ingotGnomite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_hoe), 10000, "RR", "T ","T ",'R', "ingotGnomite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_paxel), 10000, "ASP", "LTH"," T ",'A', new ItemStack(NeoOresItems.gnomite_axe),'H', new ItemStack(NeoOresItems.gnomite_hoe),'P', new ItemStack(NeoOresItems.gnomite_pickaxe),'L', new ItemStack(NeoOresItems.gnomite_shovel),'S', new ItemStack(NeoOresItems.gnomite_sword), 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_shovel), 10000, "R", "T","T",'R', "ingotGnomite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_axe), 10000, "RR", "TR","T ",'R', "gemSalamite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_pickaxe), 10000, "RRR", " T "," T ",'R', "gemSalamite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_sword), 10000, "R", "R","T",'R', "gemSalamite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_hoe), 10000, "RR", "T ","T ",'R', "gemSalamite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_paxel), 10000, "ASP", "LTH"," T ",'A', new ItemStack(NeoOresItems.salamite_axe),'H', new ItemStack(NeoOresItems.salamite_hoe),'P', new ItemStack(NeoOresItems.salamite_pickaxe),'L', new ItemStack(NeoOresItems.salamite_shovel),'S', new ItemStack(NeoOresItems.salamite_sword), 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_shovel), 10000, "R", "T","T",'R', "gemSalamite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_axe), 10000, "RR", "TR","T ",'R', "gemSylphite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_pickaxe), 10000, "RRR", " T "," T ",'R',"gemSylphite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_sword), 10000, "R", "R","T",'R', "gemSylphite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_hoe), 10000, "RR", "T ","T ",'R', "gemSylphite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_paxel), 10000, "ASP", "LTH"," T ",'A', new ItemStack(NeoOresItems.sylphite_axe),'H', new ItemStack(NeoOresItems.sylphite_hoe),'P', new ItemStack(NeoOresItems.sylphite_pickaxe),'L', new ItemStack(NeoOresItems.sylphite_shovel),'S', new ItemStack(NeoOresItems.sylphite_sword), 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_shovel), 10000, "R", "T","T",'R',"gemSylphite", 'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(Blocks.LOG), 20, "RR", "RR",'R', "plankWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.essence,1,0),5," H ","HXH"," H ",'H',"gravel",'X',"nuggetIron");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.essence,1,1),5," H ","HXH"," H ",'H',PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER),'X',"nuggetIron");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.essence,1,2),5," H ","HXH"," H ",'H',new ItemStack(Blocks.MAGMA),'X',"nuggetIron");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.essence,1,3),5," H ","HXH"," H ",'H',"feather",'X',"nuggetIron");
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.pedestal,9), 20,"ICI"," I ",'C',"chestWood",'I',"ingotIron");
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.pedestal_water), 5,"W","P",'W',new ItemStack(Items.WATER_BUCKET),'P',new ItemStack(NeoOresBlocks.pedestal));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.enhanced_pedestal,1,0), 20,"PPP","P P","PPP",'P',new ItemStack(NeoOresBlocks.pedestal));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.enhanced_pedestal,1,1), 20," P ","PCP"," P ",'C',"chestWood",'P',new ItemStack(NeoOresBlocks.enhanced_pedestal,1,0));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.enhanced_pedestal,1,2), 20," P ","PCP"," P ",'C',"chestWood",'P',new ItemStack(NeoOresBlocks.enhanced_pedestal,1,1));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.enhanced_pedestal,1,3), 20," P ","PCP"," P ",'C',"chestWood",'P',new ItemStack(NeoOresBlocks.enhanced_pedestal,1,2));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.enhanced_pedestal,1,4), 20," P ","PCP"," P ",'C',"chestWood",'P',new ItemStack(NeoOresBlocks.enhanced_pedestal,1,3));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.enhanced_pedestal,1,5), 20," P ","PCP"," P ",'C',"chestWood",'P',new ItemStack(NeoOresBlocks.enhanced_pedestal,1,4));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.enhanced_pedestal,1,6), 20," P ","PCP"," P ",'C',"chestWood",'P',new ItemStack(NeoOresBlocks.enhanced_pedestal,1,5));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.enhanced_pedestal,1,7), 20," P ","PCP"," P ",'C',"chestWood",'P',new ItemStack(NeoOresBlocks.enhanced_pedestal,1,6));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.mage_knowledge_table),100," F ","AXW"," E ",'F',new ItemStack(NeoOresItems.essence,1,2),'A',new ItemStack(NeoOresItems.essence,1,3),'E',new ItemStack(NeoOresItems.essence,1,0),'W',new ItemStack(NeoOresItems.essence,1,1),'X',new ItemStack(Blocks.ENCHANTING_TABLE));
		this.addManaCraftingRecipe(new ItemStack(NeoOresBlocks.spell_recipe_creation_table), 100, "F","B","M",'F',"feather",'B',"dyeBlack",'M',new ItemStack(NeoOresBlocks.mana_workbench));
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.mana_wrench), 10, "I","T","T",'I',"nuggetIron",'T',"stickWood");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.spell_sheet), 10, "E","P",'E',"essenceAll",'P',"paper");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.recipe_sheet), 10, "E","P",'E',"dyeBlack",'P',"paper");
		this.addManaCraftingRecipe(new ItemStack(Blocks.QUARTZ_ORE,4), 10, "DND","NDN","DND",'D',"stoneDiorite",'N',"netherrack");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_helmet), 10000, "PPP","P P",'P',"ingotGnomite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_chestplate), 10000, "P P","PPP","PPP",'P',"ingotGnomite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_leggings), 10000, "PPP","P P","P P",'P',"ingotGnomite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.gnomite_boots), 10000, "P P","P P",'P',"ingotGnomite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_helmet), 10000, "PPP","P P",'P',"gemSalamite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_chestplate), 10000, "P P","PPP","PPP",'P',"gemSalamite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_leggings), 10000, "PPP","P P","P P",'P',"gemSalamite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.salamite_boots), 10000, "P P","P P",'P',"gemSalamite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_helmet), 10000, "PPP","P P",'P',"gemSylphite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_chestplate), 10000, "P P","PPP","PPP",'P',"gemSylphite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_leggings), 10000, "PPP","P P","P P",'P',"gemSylphite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.sylphite_boots), 10000, "P P","P P",'P',"gemSylphite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_helmet), 10000, "PPP","P P",'P',"gemUndite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_chestplate), 10000, "P P","PPP","PPP",'P',"gemUndite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_leggings), 10000, "PPP","P P","P P",'P',"gemUndite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.undite_boots), 10000, "P P","P P",'P',"gemUndite");
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.mob_bottle), 100, "A","D","B",'A',"gemAerite",'D',"dyeBlack",'B',new ItemStack(Items.GLASS_BOTTLE));
		this.addManaCraftingRecipe(new ItemStack(NeoOresItems.mob_bottle_master), 100, "D","B",'D',new ItemStack(Blocks.DRAGON_EGG),'B',new ItemStack(NeoOresItems.mob_bottle));
		
		int n = 0;
		for(ManaCraftingRecipeManager mcrm : manacraftingrecipes)
		{
			event.getRegistry().register(new ManaCraftingRecipe(mcrm.getResult(),mcrm.getMana(),mcrm.getObjects()).setRegistryName(Reference.MOD_ID,"manaCrafting." + n));
			n++;
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
        event.getRegistry().register(new ShapedRecipes(Reference.MOD_ID, 3, 3, NonNullList.from(Ingredient.EMPTY, Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItem(Item.getItemFromBlock(Blocks.DRAGON_EGG)), Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItems(Items.NETHER_STAR),Ingredient.fromItems(Items.NETHER_STAR)),NeoOres.addName(NeoOres.addEnchantment(NeoOres.addRegacy(new ItemStack(Items.STICK)),Enchantments.KNOCKBACK,100),"homerunbat")).setRegistryName(Reference.MOD_ID,"homerunbat"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresItems.essence,1,1),"HHH","HXH","HHH",'H',Items.POTIONITEM,'X',"ingotIron").setRegistryName(Reference.MOD_ID, "water_essence"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresItems.essence,1,2),"HHH","HXH","HHH",'H',Blocks.MAGMA,'X',"ingotIron").setRegistryName(Reference.MOD_ID, "fire_essence"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresItems.essence,1,0),"CDS","GXG","SDC",'C',Items.CLAY_BALL,'D',"dirt",'S',"sand",'G',"gravel",'X',"ingotIron").setRegistryName(Reference.MOD_ID, "earth_essence"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresItems.essence,1,3),"HHH","HXH","HHH",'H',"feather",'X',"ingotIron").setRegistryName(Reference.MOD_ID, "air_essence"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresBlocks.mana_workbench)," F ","AXW"," E ",'F',new ItemStack(NeoOresItems.essence,1,2),'A',new ItemStack(NeoOresItems.essence,1,3),'E',new ItemStack(NeoOresItems.essence,1,0),'W',new ItemStack(NeoOresItems.essence,1,1),'X',"workbench").setRegistryName(Reference.MOD_ID, "mana_workbench"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresBlocks.enhanced_pedestal,1,8),"P","H",'P',new ItemStack(NeoOresBlocks.enhanced_pedestal,1,0),'H',new ItemStack(Blocks.HOPPER)).setRegistryName(Reference.MOD_ID, "EPhopped1"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresBlocks.enhanced_pedestal,1,9),"P","H",'P',new ItemStack(NeoOresBlocks.enhanced_pedestal,1,1),'H',new ItemStack(Blocks.HOPPER)).setRegistryName(Reference.MOD_ID, "EPhopped2"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresBlocks.enhanced_pedestal,1,10),"P","H",'P',new ItemStack(NeoOresBlocks.enhanced_pedestal,1,2),'H',new ItemStack(Blocks.HOPPER)).setRegistryName(Reference.MOD_ID, "EPhopped3"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresBlocks.enhanced_pedestal,1,11),"P","H",'P',new ItemStack(NeoOresBlocks.enhanced_pedestal,1,3),'H',new ItemStack(Blocks.HOPPER)).setRegistryName(Reference.MOD_ID, "EPhopped4"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresBlocks.enhanced_pedestal,1,12),"P","H",'P',new ItemStack(NeoOresBlocks.enhanced_pedestal,1,4),'H',new ItemStack(Blocks.HOPPER)).setRegistryName(Reference.MOD_ID, "EPhopped5"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresBlocks.enhanced_pedestal,1,13),"P","H",'P',new ItemStack(NeoOresBlocks.enhanced_pedestal,1,5),'H',new ItemStack(Blocks.HOPPER)).setRegistryName(Reference.MOD_ID, "EPhopped6"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresBlocks.enhanced_pedestal,1,14),"P","H",'P',new ItemStack(NeoOresBlocks.enhanced_pedestal,1,6),'H',new ItemStack(Blocks.HOPPER)).setRegistryName(Reference.MOD_ID, "EPhopped7"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresBlocks.enhanced_pedestal,1,15),"P","H",'P',new ItemStack(NeoOresBlocks.enhanced_pedestal,1,7),'H',new ItemStack(Blocks.HOPPER)).setRegistryName(Reference.MOD_ID, "EPhopped8"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresBlocks.air_essence_block),"XX","XX",'X',new ItemStack(NeoOresItems.essence,1,3)).setRegistryName(Reference.MOD_ID, "air_comp"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresBlocks.earth_essence_block),"XX","XX",'X',new ItemStack(NeoOresItems.essence,1,0)).setRegistryName(Reference.MOD_ID, "earth_comp"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresBlocks.fire_essence_block),"XX","XX",'X',new ItemStack(NeoOresItems.essence,1,2)).setRegistryName(Reference.MOD_ID, "fire_comp"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresBlocks.water_essence_block),"XX","XX",'X',new ItemStack(NeoOresItems.essence,1,1)).setRegistryName(Reference.MOD_ID, "water_comp"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresItems.essence,4,3),"XX","XX",'X',new ItemStack(NeoOresBlocks.air_essence_block)).setRegistryName(Reference.MOD_ID, "air_decomp"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresItems.essence,4,0),"XX","XX",'X',new ItemStack(NeoOresBlocks.earth_essence_block)).setRegistryName(Reference.MOD_ID, "earth_decomp"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresItems.essence,4,2),"XX","XX",'X',new ItemStack(NeoOresBlocks.fire_essence_block)).setRegistryName(Reference.MOD_ID, "fire_decomp"));
        event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"recipes"),new ItemStack(NeoOresItems.essence,4,1),"XX","XX",'X',new ItemStack(NeoOresBlocks.water_essence_block)).setRegistryName(Reference.MOD_ID, "water_decomp"));
        
        /*
        GameRegistry.addSmelting(new ItemStack(NeoOres.guardite_ore), new ItemStack(NeoOres.guardite_ingot), 16.0F);
        GameRegistry.addSmelting(new ItemStack(NeoOres.landite_ore), new ItemStack(NeoOres.landite_ingot), 16.0F);
        GameRegistry.addSmelting(new ItemStack(NeoOres.marlite_ore), new ItemStack(NeoOres.marlite_ingot), 16.0F);
        */
        
        addOreDictSmelting("oreGuardite",new ItemStack(NeoOresItems.guardite_ingot), 16.0F);
        addOreDictSmelting("oreLandite",new ItemStack(NeoOresItems.landite_ingot), 16.0F);
        addOreDictSmelting("oreMarlite",new ItemStack(NeoOresItems.marlite_ingot), 16.0F);
        addOreDictSmelting("oreSanitite",new ItemStack(NeoOresItems.sanitite), 16.0F);
        addOreDictSmelting("oreFlamite",new ItemStack(NeoOresItems.flamite), 16.0F);
        addOreDictSmelting("oreForcite",new ItemStack(NeoOresItems.forcite), 16.0F);
        addOreDictSmelting("oreDrenite",new ItemStack(NeoOresItems.drenite), 16.0F);
        addOreDictSmelting("oreAerite",new ItemStack(NeoOresItems.aerite), 16.0F);
        addOreDictSmelting("oreCoal",new ItemStack(Items.COAL), 0.1F);
        addOreDictSmelting("oreDiamond",new ItemStack(Items.DIAMOND), 1.0F);
        addOreDictSmelting("oreEmerald",new ItemStack(Items.EMERALD), 1.0F);
        addOreDictSmelting("oreGold",new ItemStack(Items.GOLD_INGOT), 1.0F);
        addOreDictSmelting("oreIron",new ItemStack(Items.IRON_INGOT), 0.7F);
        addOreDictSmelting("oreLapis",new ItemStack(Items.DYE,1,4), 0.2F);
        addOreDictSmelting("oreRedstone",new ItemStack(Items.REDSTONE), 0.7F);
        addOreDictSmelting("oreQuartz",new ItemStack(Items.QUARTZ), 0.2F);
        
        BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD), "essenceAll", PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), NeoOres.mana_regen));
        BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), NeoOres.mana_regen), "dustRedstone", PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), NeoOres.long_mana_regen));
        BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), NeoOres.mana_regen), "dustGlowstone", PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), NeoOres.strong_mana_regen));
    }
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void registerManaCompositionRecipe(final RegistryEvent.Register<ManaCompositionRecipe> event)
	{
		this.addManaCompositionRecipe(0,new ItemStack(Items.FEATHER,5),Arrays.asList(new ItemStackWithSizeForRecipe("gemQuartz",5),new ItemStackWithSizeForRecipe("enderpearl",1)));
		this.addManaCompositionRecipe(2,new ItemStack(Items.ENDER_PEARL,1),Arrays.asList(new ItemStackWithSizeForRecipe("ingotIron",10)));
		this.addManaCompositionRecipe(4,new ItemStack(NeoOresItems.gnomite_ingot,1),Arrays.asList(new ItemStackWithSizeForRecipe("ingotGuardite",8),new ItemStackWithSizeForRecipe("ingotLandite",8)));
		this.addManaCompositionRecipe(6,new ItemStack(NeoOresItems.undite,1),Arrays.asList(new ItemStackWithSizeForRecipe("ingotMarlite",8),new ItemStackWithSizeForRecipe("gemSanitite",8)));
		this.addManaCompositionRecipe(8,new ItemStack(NeoOresItems.sylphite,1),Arrays.asList(new ItemStackWithSizeForRecipe("gemDrenite",8),new ItemStackWithSizeForRecipe("gemAerite",8)));
		this.addManaCompositionRecipe(10,new ItemStack(NeoOresItems.salamite,1),Arrays.asList(new ItemStackWithSizeForRecipe("gemFlamite",8),new ItemStackWithSizeForRecipe("gemForcite",8)));
		
		int n = 0;
		for(ManaCompositionRecipeManager mcrm : manacompositionrecipes)
		{
			event.getRegistry().register(new ManaCompositionRecipe(mcrm.getTier(),mcrm.getResult(),mcrm.getObjects()).setRegistryName(Reference.MOD_ID, "" + n));
			n++;
		}
	}
	
	private void addManaCraftingRecipe(ItemStack result,int mana,Object... objects)
	{
		manacraftingrecipes.add(new ManaCraftingRecipeManager(result,mana,objects));
	}
	
	private void addManaCompositionRecipe(int tier,ItemStack result,List<ItemStackWithSizeForRecipe> objects)
	{
		manacompositionrecipes.add(new ManaCompositionRecipeManager(tier,result,objects));
	}
	
	protected static class ManaCraftingRecipeManager
	{
		private final ItemStack stack;
		private final int value;
		private final Object[] list;
		public ManaCraftingRecipeManager(ItemStack result,int mana,Object... objects)
		{
			stack = result;
			value = mana;
			list = objects;
		}
		
		public ItemStack getResult() {
			return stack;
		}
		
		public int getMana() {
			return value;
		}
		
		public Object[] getObjects() {
			return list;
		}
	}
	
	protected static class ManaCompositionRecipeManager
	{
		private final ItemStack stack;
		private final int value;
		private final List<ItemStackWithSizeForRecipe> list;
		public ManaCompositionRecipeManager(int tier,ItemStack result,List<ItemStackWithSizeForRecipe> objects)
		{
			stack = result;
			value = tier;
			list = objects;
		}
		
		public ItemStack getResult() {
			return stack;
		}
		
		public int getTier() {
			return value;
		}
		
		public List<ItemStackWithSizeForRecipe> getObjects() {
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
	
	public static void addOreDictSmelting(String oredict,ItemStack result,float exp)
	{
		for(ItemStack stack : OreDictionary.getOres(oredict))
		{
			GameRegistry.addSmelting(stack, result, exp);
		}
	}  
}
