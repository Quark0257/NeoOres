package neo_ores.event;

import neo_ores.client.render.RendererPedestal;
import neo_ores.client.render.entity.RenderSpellBullet;
import neo_ores.entity.EntitySpellBullet;
import neo_ores.item.INeoOresItem;
import neo_ores.block.INeoOresBlock;
import neo_ores.client.render.ModelLoaderItemSpell;
import neo_ores.client.render.RendererNeoPortal;
import neo_ores.client.render.RendererMageKnowledgeTable;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.NeoOresItems;
import neo_ores.main.Reference;
import neo_ores.tileentity.TileEntityEnhancedPedestal;
import neo_ores.tileentity.TileEntityManaFurnace;
import neo_ores.tileentity.TileEntityNeoPortal;
import neo_ores.tileentity.TileEntityPedestal;
import neo_ores.tileentity.TileEntitySpellRecipeCreationTable;
import neo_ores.tileentity.TileEntityMageKnowledgeTable;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class NeoOresInitEvent 
{
	public static TextureAtlasSprite earth0;
	public static TextureAtlasSprite water0;
	public static TextureAtlasSprite air0;
	public static TextureAtlasSprite fire0;
	public static TextureAtlasSprite particle1;
	public static TextureAtlasSprite particle2;
	public static TextureAtlasSprite particle3;
	public static TextureAtlasSprite particle4;
	public static TextureAtlasSprite particle1_animated;
	public static TextureAtlasSprite[] particle0;
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerColored(ColorHandlerEvent.Item event)
	{
		IItemColor color = new IItemColor()
		{
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) 
			{
				if(stack.getItem() == NeoOresItems.air_essence_core)
				{
					int color_code = 0xFFFFFF;
					switch(stack.getMetadata()) 
					{
						case 0: color_code = 0xE8FFFB; break;
						case 1: color_code = 0xD1FFF6; break;
						case 2: color_code = 0xB9FFF2; break;
						case 3: color_code = 0xA2FFED; break;
						case 4: color_code = 0x8BFFE9; break;
						case 5: color_code = 0x73FFE4; break;
						case 6: color_code = 0x5CFFE0; break;
						case 7: color_code = 0x45FFDB; break;
						case 8: color_code = 0x2EFFD7; break;
						case 9: color_code = 0x17FFD2; break;
						case 10: color_code = 0x00FFCE; break;
					}
					return color_code;
				}
				else if(stack.getItem() == NeoOresItems.earth_essence_core)
				{
					int color_code = 0xFFFFFF;
					switch(stack.getMetadata()) 
					{
						case 0: color_code = 0xF8FFE8; break;
						case 1: color_code = 0xF1FFD1; break;
						case 2: color_code = 0xEAFFB9; break;
						case 3: color_code = 0xE4FFA2; break;
						case 4: color_code = 0xDDFF8B; break;
						case 5: color_code = 0xD6FF73; break;
						case 6: color_code = 0xD0FF5C; break;
						case 7: color_code = 0xC9FF45; break;
						case 8: color_code = 0xC3FF2E; break;
						case 9: color_code = 0xBCFF17; break;
						case 10: color_code = 0xB5FF00; break;
					}
					return color_code;
				}
				else if(stack.getItem() == NeoOresItems.fire_essence_core)
				{
					int color_code = 0xFFFFFF;
					switch(stack.getMetadata()) 
					{
						case 0: color_code = 0xFFEFE8; break;
						case 1: color_code = 0xFFDFD1; break;
						case 2: color_code = 0xFFD0B9; break;
						case 3: color_code = 0xFFC0A2; break;
						case 4: color_code = 0xFFB08B; break;
						case 5: color_code = 0xFFA173; break;
						case 6: color_code = 0xFF915C; break;
						case 7: color_code = 0xFF8145; break;
						case 8: color_code = 0xFF722E; break;
						case 9: color_code = 0xFF6217; break;
						case 10: color_code = 0xFF5200; break;
					}
					return color_code;
				}
				else if(stack.getItem() == NeoOresItems.water_essence_core)
				{
					int color_code = 0xFFFFFF;
					switch(stack.getMetadata()) 
					{
						case 0: color_code = 0xF4E8FF; break;
						case 1: color_code = 0xE9D1FF; break;
						case 2: color_code = 0xDEB9FF; break;
						case 3: color_code = 0xD3A2FF; break;
						case 4: color_code = 0xC88BFF; break;
						case 5: color_code = 0xBE73FF; break;
						case 6: color_code = 0xB35CFF; break;
						case 7: color_code = 0xA845FF; break;
						case 8: color_code = 0x9D2EFF; break;
						case 9: color_code = 0x9217FF; break;
						case 10: color_code = 0x8700FF; break;
					}
					return color_code;
				}
				return 0xFFFFFF;
			}
		};
		event.getItemColors().registerItemColorHandler(color, NeoOresItems.air_essence_core);
		event.getItemColors().registerItemColorHandler(color, NeoOresItems.earth_essence_core);
		event.getItemColors().registerItemColorHandler(color, NeoOresItems.fire_essence_core);
		event.getItemColors().registerItemColorHandler(color, NeoOresItems.water_essence_core);
	}
	/*
	@SubscribeEvent
    public static void onAttachTileEntityCapabilities(AttachCapabilitiesEvent<TileEntity> event) 
	{
        if (event.getObject() instanceof TileEntitySpellCreatingTable) 
        {
        	event.addCapability(new ResourceLocation(Reference.MOD_ID, "spells"), new CapabilitySpellCreatingTable());
        }
    }
    */
	
	@SubscribeEvent
    public void registerEnchantments(RegistryEvent.Register<Enchantment> event)
    {
		event.getRegistry().register(NeoOres.offensive);
		event.getRegistry().register(NeoOres.soulbound);
    }
	
	@SubscribeEvent
    public void registerPotionEffects(RegistryEvent.Register<Potion> event)
    {
		event.getRegistry().register(NeoOres.mana_regeneration);
		event.getRegistry().register(NeoOres.mana_weakness);
		event.getRegistry().register(NeoOres.mana_boost);
		event.getRegistry().register(NeoOres.antiknockback);
		event.getRegistry().register(NeoOres.freeze);
		event.getRegistry().register(NeoOres.gravity);
		event.getRegistry().register(NeoOres.shield);
		event.getRegistry().register(NeoOres.undying);
    }
	
	@SubscribeEvent
    public void registerPotions(RegistryEvent.Register<PotionType> event)
    {
		event.getRegistry().register(NeoOres.mana_regen);
		event.getRegistry().register(NeoOres.long_mana_regen);
		event.getRegistry().register(NeoOres.strong_mana_regen);
    }
	
	@SuppressWarnings("deprecation")
	@SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) 
	{
		for(Block block : NeoOresBlocks.registry)
		{
			event.getRegistry().register(block);
		}
		
		GameRegistry.registerTileEntity(TileEntityManaFurnace.class, "mana_furnace");
		GameRegistry.registerTileEntity(TileEntityNeoPortal.class, "neo_portal");
		GameRegistry.registerTileEntity(TileEntityMageKnowledgeTable.class, "mage_knowledge_table");
		GameRegistry.registerTileEntity(TileEntityEnhancedPedestal.class, "enhanced_pedestal");
		GameRegistry.registerTileEntity(TileEntityPedestal.class, "pedestal");
		GameRegistry.registerTileEntity(TileEntitySpellRecipeCreationTable.class, "spell_recipe_creation_table");
    }
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) 
	{	
		for(Block block : NeoOresBlocks.registry)
		{
			if(block instanceof INeoOresBlock)
			{
				event.getRegistry().register(((INeoOresBlock)block).getItemBlock(block));
			}
		}
		
		for(Item item : NeoOresItems.registry)
		{
			event.getRegistry().register(item);
		}

        registerOreDict();
	}
	
	public static void registerOreDict()
	{	
		OreDictionary.registerOre("blockAirEssence", NeoOresBlocks.air_essence_block);
		OreDictionary.registerOre("blockEarthEssence", NeoOresBlocks.earth_essence_block);
		OreDictionary.registerOre("blockFireEssence", NeoOresBlocks.fire_essence_block);
		OreDictionary.registerOre("blockWaterEssence", NeoOresBlocks.water_essence_block);
		for(int i = 0;i<16;i++) OreDictionary.registerOre("enhancedPedestalAll", new ItemStack(NeoOresBlocks.enhanced_pedestal,1,i));
		OreDictionary.registerOre("pedestal",NeoOresBlocks.pedestal);
		OreDictionary.registerOre("pedestalWatered", NeoOresBlocks.pedestal_water);
		for(int i = 0;i<5;i++) OreDictionary.registerOre("essenceAll", new ItemStack(NeoOresItems.essence,1,i));
		OreDictionary.registerOre("gemUndite", NeoOresItems.undite);
		OreDictionary.registerOre("gemSylphite", NeoOresItems.sylphite);
		OreDictionary.registerOre("ingotGnomite", NeoOresItems.gnomite_ingot);
		OreDictionary.registerOre("gemSalamite", NeoOresItems.salamite);
		OreDictionary.registerOre("mobBottle",NeoOresItems.mob_bottle );
		OreDictionary.registerOre("mobBottle",NeoOresItems.mob_bottle_master );
		for(int i = 0;i < 4;i++)
		{
			OreDictionary.registerOre("oreQuartz", new ItemStack(NeoOresBlocks.custom_quartz_ore,1,i));
			OreDictionary.registerOre("oreCoal", new ItemStack(NeoOresBlocks.custom_coal_ore,1,i));
			OreDictionary.registerOre("oreRedstone", new ItemStack(NeoOresBlocks.custom_redstone_ore,1,i));
			OreDictionary.registerOre("oreEmerald", new ItemStack(NeoOresBlocks.custom_emerald_ore,1,i));
			OreDictionary.registerOre("oreDiamond", new ItemStack(NeoOresBlocks.custom_diamond_ore,1,i));
			OreDictionary.registerOre("oreLapis", new ItemStack(NeoOresBlocks.custom_lapis_ore,1,i));
			OreDictionary.registerOre("oreIron", new ItemStack(NeoOresBlocks.custom_iron_ore,1,i));
			OreDictionary.registerOre("oreGold", new ItemStack(NeoOresBlocks.custom_gold_ore,1,i));
		}
		OreDictionary.registerOre("oreAerite", NeoOresBlocks.aerite_ore);
		OreDictionary.registerOre("oreDrenite", NeoOresBlocks.drenite_ore);
		OreDictionary.registerOre("oreForcite", NeoOresBlocks.forcite_ore);
		OreDictionary.registerOre("oreFlamite", NeoOresBlocks.flamite_ore);
		OreDictionary.registerOre("oreGuardite", NeoOresBlocks.guardite_ore);
		OreDictionary.registerOre("oreLandite", NeoOresBlocks.landite_ore);
		OreDictionary.registerOre("oreMarlite", NeoOresBlocks.marlite_ore);
		OreDictionary.registerOre("oreSanitite", NeoOresBlocks.sanitite_ore);
		OreDictionary.registerOre("gemAerite", NeoOresItems.aerite);
		OreDictionary.registerOre("gemDrenite", NeoOresItems.drenite);
		OreDictionary.registerOre("gemForcite", NeoOresItems.forcite);
		OreDictionary.registerOre("gemFlamite", NeoOresItems.flamite);
		OreDictionary.registerOre("ingotGuardite", NeoOresItems.guardite_ingot);
		OreDictionary.registerOre("ingotLandite", NeoOresItems.landite_ingot);
		OreDictionary.registerOre("ingotMarlite", NeoOresItems.marlite_ingot);
		OreDictionary.registerOre("gemSanitite", NeoOresItems.sanitite);
	}
	
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) 
    {
    	OBJLoader.INSTANCE.addDomain(Reference.MOD_ID);
    	
    	int metadata;
    	for(Block block1 : NeoOresBlocks.registry)
		{
    		if(block1 instanceof INeoOresBlock)
    		{
    			INeoOresBlock block = (INeoOresBlock)block1;
    			for(metadata = 0;metadata <= block.getMaxMeta();metadata++)
    			{
        			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block1), metadata, block.getModel(metadata));
        		}
    		}
		}
    	
    	for(Item item : NeoOresItems.registry)
    	{
    		if(item instanceof INeoOresItem)
    		{
    			INeoOresItem neo_item = (INeoOresItem)item;
    			for(metadata = 0;metadata <= neo_item.getMaxMeta();metadata++)
    			{
    				ModelLoader.setCustomModelResourceLocation(item, metadata, neo_item.getModel(item, metadata));
    			}
    		}
    	}

        for(int i = 0;i < 4;i++) (new ModelLoaderItemSpell(new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "spell." + i), "inventory"),
        		new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "spell"+ i +".obj"),"inventory"),new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "spelltype" + i),"inventory"))).registerModel(NeoOresItems.spell, i);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNeoPortal.class,new RendererNeoPortal());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMageKnowledgeTable.class,new RendererMageKnowledgeTable());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnhancedPedestal.class,new RendererPedestal());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPedestal.class,new RendererPedestal());
    }
    
    @SideOnly(Side.CLIENT)
    public static void registerRendering()
    {
    	RenderingRegistry.registerEntityRenderingHandler(EntitySpellBullet.class,new RenderSpellBullet.RenderSpellBulletFactory());
    }
    
    public static void registerEntity(Object mod)
    {
    	EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID,"spell_bullet"), EntitySpellBullet.class, "Spell Bullet", 0,mod, 1, 1, true);
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void textureHook(TextureStitchEvent.Pre event) 
    {
    	//event.getMap().updateAnimations();
    	earth0 = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/earth0"));
    	water0 = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/water0"));
    	air0 = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/air0"));
    	fire0 = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/fire0"));
    	particle1 = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/particle1"));
    	particle2 = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/particle2"));
    	particle3 = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/particle3"));
    	particle4 = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/particle4"));
    	particle1_animated = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/particle1_animated"));
    	particle0 = new TextureAtlasSprite[] {null,null,null,null,null,null,null,null};
    	for(int meta = 0;meta < 8;meta++)
    	{
    		particle0[meta] = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/particle_animated" + meta));
    	}
    }
}
