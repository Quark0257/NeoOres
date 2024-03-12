package neo_ores.main;

import neo_ores.client.render.RendererPedestal;
import neo_ores.client.render.entity.RenderSpellBullet;
import neo_ores.entity.EntitySpellBullet;
import neo_ores.item.INeoOresItem;
import neo_ores.item.ItemSpell;
import neo_ores.api.ColorUtils;
import neo_ores.block.BlockDimension;
import neo_ores.block.INeoOresBlock;
import neo_ores.client.color.BlockColorNeoOres;
import neo_ores.client.color.ItemColorNeoOres;
import neo_ores.client.render.ModelLoaderItemSpell;
import neo_ores.client.render.RendererMechanicalMagician;
import neo_ores.client.render.RendererNeoPortal;
import neo_ores.client.render.RendererMageKnowledgeTable;
import neo_ores.tileentity.TileEntityEnhancedPedestal;
import neo_ores.tileentity.TileEntityManaFurnace;
import neo_ores.tileentity.TileEntityMechanicalMagician;
import neo_ores.tileentity.TileEntityNeoPortal;
import neo_ores.tileentity.TileEntityPedestal;
import neo_ores.tileentity.TileEntitySpellRecipeCreationTable;
import neo_ores.util.SpellUtils;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import neo_ores.tileentity.TileEntityMageKnowledgeTable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.Biome;
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
public class NeoOresRegisterEvent
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
	public void registerColoredItem(ColorHandlerEvent.Item event)
	{
		IItemColor essenceColor = new IItemColor()
		{
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex)
			{
				int color_code = -1;
				if (stack.getItem() == NeoOresItems.air_essence_core)
				{
					color_code = DimensionName.AIR.getColor();
				}
				else if (stack.getItem() == NeoOresItems.earth_essence_core)
				{
					color_code = DimensionName.EARTH.getColor();
				}
				else if (stack.getItem() == NeoOresItems.fire_essence_core)
				{
					color_code = DimensionName.FIRE.getColor();
				}
				else if (stack.getItem() == NeoOresItems.water_essence_core)
				{
					color_code = DimensionName.WATER.getColor();
				}

				return ColorUtils.getColorWithWhite(color_code, 1.0 / 11.0 * (double) (stack.getMetadata() + 1));
			}
		};
		event.getItemColors().registerItemColorHandler(essenceColor, NeoOresItems.air_essence_core);
		event.getItemColors().registerItemColorHandler(essenceColor, NeoOresItems.earth_essence_core);
		event.getItemColors().registerItemColorHandler(essenceColor, NeoOresItems.fire_essence_core);
		event.getItemColors().registerItemColorHandler(essenceColor, NeoOresItems.water_essence_core);

		IItemColor spellColor = new IItemColor()
		{
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex)
			{
				if (stack.getItem() instanceof ItemSpell)
				{
					return SpellUtils.getColor(stack);
				}
				return 0xFFFFFF;
			}
		};
		event.getItemColors().registerItemColorHandler(spellColor, NeoOresItems.spell);

		IItemColor logColor = new IItemColor()
		{
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex)
			{
				return ColorUtils.getColorWithWhite(DimensionName.getFromMeta(stack.getMetadata() % 4).getColor(), 0.75);
			}
		};
		event.getItemColors().registerItemColorHandler(logColor, Item.getItemFromBlock(NeoOresBlocks.dim_log));
		event.getItemColors().registerItemColorHandler(logColor, Item.getItemFromBlock(NeoOresBlocks.dim_planks));
		
		IItemColor leavesDim = new IItemColor()
		{
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex)
			{
				return ColorUtils.getColorWithWhite(ColorUtils.makeColor((double) DimensionName.getFromMeta(stack.getMetadata() % 4).getColorIndex()).getColor(), 0.75);
			}
		};
		event.getItemColors().registerItemColorHandler(leavesDim, Item.getItemFromBlock(NeoOresBlocks.dim_leaves));

		IItemColor CDleavesDim = new IItemColor()
		{
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex)
			{
				return ColorUtils.getColorWithWhite(ColorUtils.makeColor((double) DimensionName.getFromMeta(stack.getMetadata() % 4).getColorIndex() - 0.33).getColor(), 0.75);
			}
		};
		event.getItemColors().registerItemColorHandler(CDleavesDim, Item.getItemFromBlock(NeoOresBlocks.corroded_dim_leaves));

		IItemColor CIleavesDim = new IItemColor()
		{
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex)
			{
				return ColorUtils.getColorWithWhite(ColorUtils.makeColor((double) DimensionName.getFromMeta(stack.getMetadata() % 4).getColorIndex() + 0.33).getColor(), 0.75);
			}
		};
		event.getItemColors().registerItemColorHandler(CIleavesDim, Item.getItemFromBlock(NeoOresBlocks.corroding_dim_leaves));

		for (int i = 0; i < 12; i++)
		{
			IItemColor saplingColor = new ItemColorNeoOres(i);
			event.getItemColors().registerItemColorHandler(saplingColor, Item.getItemFromBlock(NeoOresBlocks.color_saplings.get(i)));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerColoredBlock(ColorHandlerEvent.Block event)
	{
		IBlockColor logColor = new IBlockColor()
		{
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
			{
				if (!(worldIn != null && pos != null))
					return -1;
				if (worldIn.getBlockState(pos).getPropertyKeys().contains(BlockDimension.DIM))
				{
					return ColorUtils.getColorWithWhite(worldIn.getBlockState(pos).getValue(BlockDimension.DIM).getColor(), 0.75);
				}
				return -1;
			}
		};
		event.getBlockColors().registerBlockColorHandler(logColor, NeoOresBlocks.dim_log);
		event.getBlockColors().registerBlockColorHandler(logColor, NeoOresBlocks.dim_planks);

		for (int i = 0; i < 12; i++)
		{
			IBlockColor saplingColor = new BlockColorNeoOres(i);
			event.getBlockColors().registerBlockColorHandler(saplingColor, NeoOresBlocks.color_saplings.get(i));
		}

		IBlockColor leavesDim = new IBlockColor()
		{
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
			{
				return ColorUtils.getColorWithWhite(ColorUtils.makeColor((double) state.getValue(BlockDimension.DIM).getColorIndex()).getColor(), 0.75);
			}
		};
		event.getBlockColors().registerBlockColorHandler(leavesDim, NeoOresBlocks.dim_leaves);
		IBlockColor leavesCDDim = new IBlockColor()
		{
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
			{
				return ColorUtils.getColorWithWhite(ColorUtils.makeColor((double) state.getValue(BlockDimension.DIM).getColorIndex() - 0.33).getColor(), 0.75);
			}
		};
		event.getBlockColors().registerBlockColorHandler(leavesCDDim, NeoOresBlocks.corroded_dim_leaves);
		IBlockColor leavesCIDim = new IBlockColor()
		{
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
			{
				return ColorUtils.getColorWithWhite(ColorUtils.makeColor((double) state.getValue(BlockDimension.DIM).getColorIndex() + 0.33).getColor(), 0.75);
			}
		};
		event.getBlockColors().registerBlockColorHandler(leavesCIDim, NeoOresBlocks.corroding_dim_leaves);
	}

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

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event)
	{
		for (Block block : NeoOresBlocks.registry)
		{
			event.getRegistry().register(block);
			if (block instanceof INeoOresBlock)
			{
				NeoOres.proxy.setCustomStateModel(block);
			}
		}

		GameRegistry.registerTileEntity(TileEntityManaFurnace.class, new ResourceLocation(Reference.MOD_ID, "mana_furnace"));
		GameRegistry.registerTileEntity(TileEntityNeoPortal.class, new ResourceLocation(Reference.MOD_ID, "neo_portal"));
		GameRegistry.registerTileEntity(TileEntityMageKnowledgeTable.class, new ResourceLocation(Reference.MOD_ID, "mage_knowledge_table"));
		GameRegistry.registerTileEntity(TileEntityEnhancedPedestal.class, new ResourceLocation(Reference.MOD_ID, "enhanced_pedestal"));
		GameRegistry.registerTileEntity(TileEntityPedestal.class, new ResourceLocation(Reference.MOD_ID, "pedestal"));
		GameRegistry.registerTileEntity(TileEntitySpellRecipeCreationTable.class, new ResourceLocation(Reference.MOD_ID, "spell_recipe_creation_table"));
		GameRegistry.registerTileEntity(TileEntityMechanicalMagician.class, new ResourceLocation(Reference.MOD_ID, "mechanical_magician"));
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event)
	{
		for (Block block : NeoOresBlocks.registry)
		{
			if (block instanceof INeoOresBlock)
			{
				event.getRegistry().register(((INeoOresBlock) block).getItemBlock(block));
			}
		}

		for (Item item : NeoOresItems.registry)
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
		for (int i = 0; i < 16; i++)
			OreDictionary.registerOre("enhancedPedestalAll", new ItemStack(NeoOresBlocks.enhanced_pedestal, 1, i));
		OreDictionary.registerOre("pedestal", NeoOresBlocks.pedestal);
		OreDictionary.registerOre("pedestalWatered", NeoOresBlocks.pedestal_water);
		for (int i = 0; i < 5; i++)
			OreDictionary.registerOre("essenceAll", new ItemStack(NeoOresItems.essence, 1, i));
		OreDictionary.registerOre("mobBottle", NeoOresItems.mob_bottle);
		OreDictionary.registerOre("mobBottle", NeoOresItems.mob_bottle_master);
		for (int i = 0; i < 4; i++)
		{
			OreDictionary.registerOre("oreQuartz", new ItemStack(NeoOresBlocks.custom_quartz_ore, 1, i));
			OreDictionary.registerOre("oreCoal", new ItemStack(NeoOresBlocks.custom_coal_ore, 1, i));
			OreDictionary.registerOre("oreRedstone", new ItemStack(NeoOresBlocks.custom_redstone_ore, 1, i));
			OreDictionary.registerOre("oreEmerald", new ItemStack(NeoOresBlocks.custom_emerald_ore, 1, i));
			OreDictionary.registerOre("oreDiamond", new ItemStack(NeoOresBlocks.custom_diamond_ore, 1, i));
			OreDictionary.registerOre("oreLapis", new ItemStack(NeoOresBlocks.custom_lapis_ore, 1, i));
			OreDictionary.registerOre("oreIron", new ItemStack(NeoOresBlocks.custom_iron_ore, 1, i));
			OreDictionary.registerOre("oreGold", new ItemStack(NeoOresBlocks.custom_gold_ore, 1, i));
			OreDictionary.registerOre("stoneDimension", new ItemStack(NeoOresBlocks.dim_stone, 1, i));
			OreDictionary.registerOre("logWood", new ItemStack(NeoOresBlocks.dim_log, 1, i));
			OreDictionary.registerOre("plankWood", new ItemStack(NeoOresBlocks.dim_planks, 1, i));
			OreDictionary.registerOre("treeLeaves", new ItemStack(NeoOresBlocks.dim_leaves, 1, i));
			OreDictionary.registerOre("treeLeaves", new ItemStack(NeoOresBlocks.corroded_dim_leaves, 1, i));
			OreDictionary.registerOre("treeLeaves", new ItemStack(NeoOresBlocks.corroding_dim_leaves, 1, i));
		}
		for (Block sapling : NeoOresBlocks.color_saplings) {
			OreDictionary.registerOre("treeSapling", new ItemStack(sapling, 1, 0));
		}
		OreDictionary.registerOre("urystone", new ItemStack(NeoOresBlocks.dim_stone, 1, 0));
		OreDictionary.registerOre("gabrystone", new ItemStack(NeoOresBlocks.dim_stone, 1, 1));
		OreDictionary.registerOre("raphastone", new ItemStack(NeoOresBlocks.dim_stone, 1, 2));
		OreDictionary.registerOre("michastone", new ItemStack(NeoOresBlocks.dim_stone, 1, 3));
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
		OreDictionary.registerOre("gemUndite", NeoOresItems.undite);
		OreDictionary.registerOre("gemSylphite", NeoOresItems.sylphite);
		OreDictionary.registerOre("ingotGnomite", NeoOresItems.gnomite_ingot);
		OreDictionary.registerOre("gemSalamite", NeoOresItems.salamite);
		OreDictionary.registerOre("blockAerite", NeoOresBlocks.aerite_block);
		OreDictionary.registerOre("blockDrenite", NeoOresBlocks.drenite_block);
		OreDictionary.registerOre("blockForcite", NeoOresBlocks.forcite_block);
		OreDictionary.registerOre("blockFlamite", NeoOresBlocks.flamite_block);
		OreDictionary.registerOre("blockGuardite", NeoOresBlocks.guardite_block);
		OreDictionary.registerOre("blockLandite", NeoOresBlocks.landite_block);
		OreDictionary.registerOre("blockMarlite", NeoOresBlocks.marlite_block);
		OreDictionary.registerOre("blockSanitite", NeoOresBlocks.sanitite_block);
		OreDictionary.registerOre("blockUndite", NeoOresBlocks.undite_block);
		OreDictionary.registerOre("blockSylphite", NeoOresBlocks.sylphite_block);
		OreDictionary.registerOre("blockGnomite", NeoOresBlocks.gnomite_block);
		OreDictionary.registerOre("blockSalamite", NeoOresBlocks.salamite_block);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event)
	{
		OBJLoader.INSTANCE.addDomain(Reference.MOD_ID);

		int metadata;
		for (Block block1 : NeoOresBlocks.registry)
		{
			if (block1 instanceof INeoOresBlock)
			{
				INeoOresBlock block = (INeoOresBlock) block1;
				for (metadata = 0; metadata <= block.getMaxMeta(); metadata++)
				{
					ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block1), metadata, block.getModel(metadata));
				}
			}
		}

		for (Item item : NeoOresItems.registry)
		{
			if (item instanceof INeoOresItem)
			{
				INeoOresItem neo_item = (INeoOresItem) item;
				for (metadata = 0; metadata <= neo_item.getMaxMeta(); metadata++)
				{
					ModelLoader.setCustomModelResourceLocation(item, metadata, neo_item.getModel(item, metadata));
				}
			}
		}

		for (int i = 0; i < 4; i++)
			(new ModelLoaderItemSpell(new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "spell." + i), "inventory"),
					new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "spell" + i + ".obj"), "inventory"),
					new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "spelltype" + i), "inventory"))).registerModel(NeoOresItems.spell, i);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNeoPortal.class, new RendererNeoPortal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMageKnowledgeTable.class, new RendererMageKnowledgeTable());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnhancedPedestal.class, new RendererPedestal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPedestal.class, new RendererPedestal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMechanicalMagician.class, new RendererMechanicalMagician());
	}

	@SideOnly(Side.CLIENT)
	public static void registerRendering()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellBullet.class, new RenderSpellBullet.RenderSpellBulletFactory());
	}

	public static void registerEntity(Object mod)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "spell_bullet"), EntitySpellBullet.class, "Spell Bullet", 0, mod, 1, 1, true);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void textureHook(TextureStitchEvent.Pre event)
	{
		// event.getMap().updateAnimations();
		earth0 = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/earth0"));
		water0 = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/water0"));
		air0 = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/air0"));
		fire0 = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/fire0"));
		particle1 = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/particle1"));
		particle2 = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/particle2"));
		particle3 = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/particle3"));
		particle4 = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/particle4"));
		particle1_animated = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/particle1_animated"));
		particle0 = new TextureAtlasSprite[] { null, null, null, null, null, null, null, null };
		for (int meta = 0; meta < 8; meta++)
		{
			particle0[meta] = event.getMap().registerSprite(new ResourceLocation("neo_ores:particles/particle_animated" + meta));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerSounds(RegistryEvent.Register<SoundEvent> event)
	{
		event.getRegistry().register(NeoOres.MUSIC_AIR);
		event.getRegistry().register(NeoOres.MUSIC_FIRE);
		event.getRegistry().register(NeoOres.MUSIC_EARTH);
		event.getRegistry().register(NeoOres.MUSIC_WATER);
	}

	@SubscribeEvent
	public void registerBiomes(RegistryEvent.Register<Biome> event)
	{
		event.getRegistry().register(NeoOres.earth);
		event.getRegistry().register(NeoOres.air);
		event.getRegistry().register(NeoOres.fire);
		event.getRegistry().register(NeoOres.water);
	}
}
