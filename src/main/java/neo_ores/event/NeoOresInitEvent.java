package neo_ores.event;

import neo_ores.items.ItemBlockDimension;
import neo_ores.items.ItemBlockNeoOre;
import neo_ores.items.ItemManaBlock;
import neo_ores.main.NeoOres;
import neo_ores.main.Reference;
import neo_ores.tileentity.NeoPortalRenderer;
import neo_ores.tileentity.TileEntityManaFurnace;
import neo_ores.tileentity.TileEntityNeoPortal;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class NeoOresInitEvent 
{
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerColored(ColorHandlerEvent.Item event)
	{
		IItemColor color = new IItemColor()
		{
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) 
			{
				if(stack.getItem() == NeoOres.air_essence_core)
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
				else if(stack.getItem() == NeoOres.earth_essence_core)
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
				else if(stack.getItem() == NeoOres.fire_essence_core)
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
				else if(stack.getItem() == NeoOres.water_essence_core)
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
		event.getItemColors().registerItemColorHandler(color, NeoOres.air_essence_core);
		event.getItemColors().registerItemColorHandler(color, NeoOres.earth_essence_core);
		event.getItemColors().registerItemColorHandler(color, NeoOres.fire_essence_core);
		event.getItemColors().registerItemColorHandler(color, NeoOres.water_essence_core);
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
	
	@SuppressWarnings("deprecation")
	@SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) 
	{
        event.getRegistry().register(NeoOres.undite_block);
        event.getRegistry().register(NeoOres.mana_block);
        event.getRegistry().register(NeoOres.mana_workbench);
		event.getRegistry().register(NeoOres.mana_furnace);
		event.getRegistry().register(NeoOres.lit_mana_furnace);
		event.getRegistry().register(NeoOres.earth_portal);
		event.getRegistry().register(NeoOres.water_portal);
		event.getRegistry().register(NeoOres.air_portal);
		event.getRegistry().register(NeoOres.fire_portal);
		event.getRegistry().register(NeoOres.empty_portal);
		event.getRegistry().register(NeoOres.empty_portal_water);
		
		event.getRegistry().register(NeoOres.custom_lit_redstone_ore);
		event.getRegistry().register(NeoOres.custom_redstone_ore);
		event.getRegistry().register(NeoOres.custom_coal_ore);
		event.getRegistry().register(NeoOres.custom_diamond_ore);
		event.getRegistry().register(NeoOres.custom_emerald_ore);
		event.getRegistry().register(NeoOres.custom_gold_ore);
		event.getRegistry().register(NeoOres.custom_iron_ore);
		event.getRegistry().register(NeoOres.custom_lapis_ore);
		event.getRegistry().register(NeoOres.custom_quartz_ore);
		event.getRegistry().register(NeoOres.aerite_ore);
		event.getRegistry().register(NeoOres.drenite_ore);
		event.getRegistry().register(NeoOres.flamite_ore);
		event.getRegistry().register(NeoOres.forcite_ore);
		event.getRegistry().register(NeoOres.guardite_ore);
		event.getRegistry().register(NeoOres.landite_ore);
		event.getRegistry().register(NeoOres.marlite_ore);
		event.getRegistry().register(NeoOres.sanitite_ore);
		
		event.getRegistry().register(NeoOres.dim_stone);
		
		event.getRegistry().register(NeoOres.study_table);
		
		GameRegistry.registerTileEntity(TileEntityManaFurnace.class, "mana_furnace");
		GameRegistry.registerTileEntity(TileEntityNeoPortal.class, "neo_portal");
    }
	
	@SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) 
	{		
		event.getRegistry().register(new ItemBlock(NeoOres.undite_block).setRegistryName(Reference.MOD_ID, "undite_block"));
		event.getRegistry().register(new ItemManaBlock(NeoOres.mana_block).setRegistryName(Reference.MOD_ID, "mana_block"));
		event.getRegistry().register(new ItemBlock(NeoOres.mana_workbench).setRegistryName(Reference.MOD_ID, "mana_workbench"));
		event.getRegistry().register(new ItemBlock(NeoOres.mana_furnace).setRegistryName(Reference.MOD_ID, "mana_furnace"));
		event.getRegistry().register(new ItemBlock(NeoOres.lit_mana_furnace).setRegistryName(Reference.MOD_ID, "lit_mana_furnace"));
		event.getRegistry().register(new ItemBlock(NeoOres.earth_portal).setRegistryName(Reference.MOD_ID, "earth_portal"));
		event.getRegistry().register(new ItemBlock(NeoOres.water_portal).setRegistryName(Reference.MOD_ID, "water_portal"));
		event.getRegistry().register(new ItemBlock(NeoOres.air_portal).setRegistryName(Reference.MOD_ID, "air_portal"));
		event.getRegistry().register(new ItemBlock(NeoOres.fire_portal).setRegistryName(Reference.MOD_ID, "fire_portal"));
		event.getRegistry().register(new ItemBlock(NeoOres.empty_portal).setRegistryName(Reference.MOD_ID, "pedestal"));
		event.getRegistry().register(new ItemBlock(NeoOres.empty_portal_water).setRegistryName(Reference.MOD_ID, "pedestal_water"));
		event.getRegistry().register(new ItemBlock(NeoOres.study_table).setRegistryName(Reference.MOD_ID, "study_table"));
		
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.custom_lit_redstone_ore).setRegistryName(NeoOres.custom_lit_redstone_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.custom_redstone_ore).setRegistryName(NeoOres.custom_redstone_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.custom_coal_ore).setRegistryName(NeoOres.custom_coal_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.custom_diamond_ore).setRegistryName(NeoOres.custom_diamond_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.custom_emerald_ore).setRegistryName(NeoOres.custom_emerald_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.custom_gold_ore).setRegistryName(NeoOres.custom_gold_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.custom_iron_ore).setRegistryName(NeoOres.custom_iron_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.custom_lapis_ore).setRegistryName(NeoOres.custom_lapis_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.custom_quartz_ore).setRegistryName(NeoOres.custom_quartz_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.aerite_ore).setRegistryName(NeoOres.aerite_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.drenite_ore).setRegistryName(NeoOres.drenite_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.flamite_ore).setRegistryName(NeoOres.flamite_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.forcite_ore).setRegistryName(NeoOres.forcite_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.guardite_ore).setRegistryName(NeoOres.guardite_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.landite_ore).setRegistryName(NeoOres.landite_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.marlite_ore).setRegistryName(NeoOres.marlite_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockNeoOre(NeoOres.sanitite_ore).setRegistryName(NeoOres.sanitite_ore.getRegistryName()));
		event.getRegistry().register(new ItemBlockDimension(NeoOres.dim_stone));
		
        event.getRegistry().register(NeoOres.undite);
        event.getRegistry().register(NeoOres.gnomite_ingot);
        event.getRegistry().register(NeoOres.salamite);
        event.getRegistry().register(NeoOres.sylphite);
        event.getRegistry().register(NeoOres.mana_ingot);
        event.getRegistry().register(NeoOres.essence);
        event.getRegistry().register(NeoOres.air_essence_core);
        event.getRegistry().register(NeoOres.earth_essence_core);
        event.getRegistry().register(NeoOres.fire_essence_core);
        event.getRegistry().register(NeoOres.water_essence_core);
        event.getRegistry().register(NeoOres.sanitite);
        event.getRegistry().register(NeoOres.marlite_ingot);
        event.getRegistry().register(NeoOres.aerite);
        event.getRegistry().register(NeoOres.drenite);
        event.getRegistry().register(NeoOres.guardite_ingot);
        event.getRegistry().register(NeoOres.landite_ingot);
        event.getRegistry().register(NeoOres.flamite);
        event.getRegistry().register(NeoOres.forcite);
        event.getRegistry().register(NeoOres.spell);
        
        event.getRegistry().register(NeoOres.undite_axe);
        event.getRegistry().register(NeoOres.undite_hoe);
        event.getRegistry().register(NeoOres.undite_pickaxe);
        event.getRegistry().register(NeoOres.undite_shovel);
        event.getRegistry().register(NeoOres.undite_sword);
        event.getRegistry().register(NeoOres.undite_paxel);
        event.getRegistry().register(NeoOres.salamite_axe);
        event.getRegistry().register(NeoOres.salamite_hoe);
        event.getRegistry().register(NeoOres.salamite_pickaxe);
        event.getRegistry().register(NeoOres.salamite_shovel);
        event.getRegistry().register(NeoOres.salamite_sword);
        event.getRegistry().register(NeoOres.salamite_paxel);
        event.getRegistry().register(NeoOres.gnomite_axe);
        event.getRegistry().register(NeoOres.gnomite_hoe);
        event.getRegistry().register(NeoOres.gnomite_pickaxe);
        event.getRegistry().register(NeoOres.gnomite_shovel);
        event.getRegistry().register(NeoOres.gnomite_sword);
        event.getRegistry().register(NeoOres.gnomite_paxel);
        event.getRegistry().register(NeoOres.sylphite_axe);
        event.getRegistry().register(NeoOres.sylphite_hoe);
        event.getRegistry().register(NeoOres.sylphite_pickaxe);
        event.getRegistry().register(NeoOres.sylphite_shovel);
        event.getRegistry().register(NeoOres.sylphite_sword);
        event.getRegistry().register(NeoOres.sylphite_paxel);
        event.getRegistry().register(NeoOres.creative_axe);
        event.getRegistry().register(NeoOres.creative_hoe);
        event.getRegistry().register(NeoOres.creative_pickaxe);
        event.getRegistry().register(NeoOres.creative_shovel);
        event.getRegistry().register(NeoOres.creative_sword);
        event.getRegistry().register(NeoOres.creative_paxel);
        
        event.getRegistry().register(NeoOres.undite_helmet);
        event.getRegistry().register(NeoOres.undite_chestplate);
        event.getRegistry().register(NeoOres.undite_leggings);
        event.getRegistry().register(NeoOres.undite_boots);
        event.getRegistry().register(NeoOres.salamite_helmet);
        event.getRegistry().register(NeoOres.salamite_chestplate);
        event.getRegistry().register(NeoOres.salamite_leggings);
        event.getRegistry().register(NeoOres.salamite_boots);
        event.getRegistry().register(NeoOres.sylphite_helmet);
        event.getRegistry().register(NeoOres.sylphite_chestplate);
        event.getRegistry().register(NeoOres.sylphite_leggings);
        event.getRegistry().register(NeoOres.sylphite_boots);
        event.getRegistry().register(NeoOres.gnomite_helmet);
        event.getRegistry().register(NeoOres.gnomite_chestplate);
        event.getRegistry().register(NeoOres.gnomite_leggings);
        event.getRegistry().register(NeoOres.gnomite_boots);
        event.getRegistry().register(NeoOres.creative_helmet);
        event.getRegistry().register(NeoOres.creative_chestplate);
        event.getRegistry().register(NeoOres.creative_leggings);
        event.getRegistry().register(NeoOres.creative_boots);
	}
	
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) 
    {
    	OBJLoader.INSTANCE.addDomain(Reference.MOD_ID);
    	
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.undite_block), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "undite_block"), "inventory"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.mana_block), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "mana_block"), "inventory"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.mana_workbench), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "mana_workbench"), "inventory"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.mana_furnace), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "mana_furnace"), "inventory"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.lit_mana_furnace), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "lit_mana_furnace"), "inventory"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.earth_portal), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "earth_portal"), "inventory"));	
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.water_portal), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "water_portal"), "inventory"));	
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.air_portal), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "air_portal"), "inventory"));	
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.fire_portal), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "fire_portal"), "inventory"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.empty_portal), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "pedestal"), "inventory"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.empty_portal_water), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "pedestal_water"), "inventory"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.aerite_ore), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "aerite_ore"), "inventory"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.drenite_ore), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "drenite_ore"), "inventory"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.flamite_ore), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "flamite_ore"), "inventory"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.forcite_ore), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "forcite_ore"), "inventory"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.guardite_ore), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "guardite_ore"), "inventory"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.landite_ore), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "landite_ore"), "inventory"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.marlite_ore), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "marlite_ore"), "inventory"));
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.sanitite_ore), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "sanitite_ore"), "inventory"));
    	for(int meta = 0;meta < 4;meta++)
    	{
    		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.custom_lit_redstone_ore), meta, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, DimensionName.getFromMeta(meta).getName() + "_custom_redstone_ore"), "inventory"));
    		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.custom_redstone_ore), meta, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, DimensionName.getFromMeta(meta).getName() + "_custom_redstone_ore"), "inventory"));
    		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.custom_coal_ore), meta, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, DimensionName.getFromMeta(meta).getName() + "_custom_coal_ore"), "inventory"));
    		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.custom_diamond_ore), meta, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, DimensionName.getFromMeta(meta).getName() + "_custom_diamond_ore"), "inventory"));
    		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.custom_emerald_ore), meta, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, DimensionName.getFromMeta(meta).getName() + "_custom_emerald_ore"), "inventory"));
    		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.custom_gold_ore), meta, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, DimensionName.getFromMeta(meta).getName() + "_custom_gold_ore"), "inventory"));
    		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.custom_iron_ore), meta, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, DimensionName.getFromMeta(meta).getName() + "_custom_iron_ore"), "inventory"));
    		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.custom_lapis_ore), meta, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, DimensionName.getFromMeta(meta).getName() + "_custom_lapis_ore"), "inventory"));
    		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.custom_quartz_ore), meta, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, DimensionName.getFromMeta(meta).getName() + "_custom_quartz_ore"), "inventory"));
    		
    		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.dim_stone), meta, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, DimensionName.getFromMeta(meta).getName() + "_dim_stone"), "inventory"));
    	}
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(NeoOres.study_table), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "study_table"), "inventory"));
    	
        ModelLoader.setCustomModelResourceLocation(NeoOres.undite, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "undite"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.gnomite_ingot, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "gnomite_ingot"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.salamite, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "salamite"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.sylphite, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "sylphite"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.mana_ingot, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "mana_ingot"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.essence, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "earth_essence.obj"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.essence, 1, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "water_essence.obj"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.essence, 2, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "fire_essence.obj"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.essence, 3, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "air_essence.obj"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.essence, 4, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "mana_essence.obj"), "inventory"));
        for(int i = 0;i < 11;i++) ModelLoader.setCustomModelResourceLocation(NeoOres.air_essence_core, i, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "essence_core"), "inventory"));
        for(int i = 0;i < 11;i++) ModelLoader.setCustomModelResourceLocation(NeoOres.earth_essence_core, i, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "essence_core"), "inventory"));
        for(int i = 0;i < 11;i++) ModelLoader.setCustomModelResourceLocation(NeoOres.fire_essence_core, i, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "essence_core"), "inventory"));
        for(int i = 0;i < 11;i++) ModelLoader.setCustomModelResourceLocation(NeoOres.water_essence_core, i, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "essence_core"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.sanitite, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "sanitite"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.marlite_ingot, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "marlite_ingot"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.aerite, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "aerite"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.drenite, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "drenite"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.guardite_ingot, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "guardite_ingot"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.landite_ingot, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "landite_ingot"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.flamite, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "flamite"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.forcite, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "forcite"), "inventory"));
        for(int i = 0;i < 4;i++) ModelLoader.setCustomModelResourceLocation(NeoOres.spell, i, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "spell." + i), "inventory"));
        
        ModelLoader.setCustomModelResourceLocation(NeoOres.undite_axe, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "undite_axe"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.undite_hoe, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "undite_hoe"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.undite_pickaxe, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "undite_pickaxe"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.undite_shovel, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "undite_shovel"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.undite_sword, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "undite_sword"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.undite_paxel, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "undite_paxel"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.salamite_axe, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "salamite_axe"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.salamite_hoe, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "salamite_hoe"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.salamite_pickaxe, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "salamite_pickaxe"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.salamite_shovel, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "salamite_shovel"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.salamite_sword, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "salamite_sword"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.salamite_paxel, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "salamite_paxel"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.gnomite_axe, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "gnomite_axe"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.gnomite_hoe, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "gnomite_hoe"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.gnomite_pickaxe, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "gnomite_pickaxe"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.gnomite_shovel, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "gnomite_shovel"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.gnomite_sword, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "gnomite_sword"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.gnomite_paxel, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "gnomite_paxel"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.sylphite_axe, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "sylphite_axe"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.sylphite_hoe, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "sylphite_hoe"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.sylphite_pickaxe, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "sylphite_pickaxe"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.sylphite_shovel, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "sylphite_shovel"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.sylphite_sword, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "sylphite_sword"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.sylphite_paxel, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "sylphite_paxel"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.creative_axe, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "creative_axe"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.creative_hoe, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "creative_hoe"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.creative_pickaxe, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "creative_pickaxe"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.creative_shovel, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "creative_shovel"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.creative_sword, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "creative_sword"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.creative_paxel, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "creative_paxel"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.undite_helmet, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "undite_helmet"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.undite_chestplate, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "undite_chestplate"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.undite_leggings, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "undite_leggings"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.undite_boots, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "undite_boots"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.creative_helmet, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "creative_helmet"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.creative_chestplate, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "creative_chestplate"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.creative_leggings, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "creative_leggings"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.creative_boots, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "creative_boots"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.sylphite_helmet, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "sylphite_helmet"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.sylphite_chestplate, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "sylphite_chestplate"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.sylphite_leggings, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "sylphite_leggings"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.sylphite_boots, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "sylphite_boots"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.salamite_helmet, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "salamite_helmet"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.salamite_chestplate, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "salamite_chestplate"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.salamite_leggings, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "salamite_leggings"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.salamite_boots, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "salamite_boots"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.gnomite_helmet, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "gnomite_helmet"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.gnomite_chestplate, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "gnomite_chestplate"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.gnomite_leggings, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "gnomite_leggings"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(NeoOres.gnomite_boots, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "gnomite_boots"), "inventory"));

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNeoPortal.class,new NeoPortalRenderer());
    }
}
