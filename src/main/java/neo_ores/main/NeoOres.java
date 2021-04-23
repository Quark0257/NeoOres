package neo_ores.main;

import java.util.Arrays;
import java.util.Random;

import morph.avaritia.init.ModItems;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.ExtremeShapedRecipe;
import neo_ores.blocks.BlockDimension;
import neo_ores.blocks.BlockNeoOre;
import neo_ores.blocks.EmptyPortal;
import neo_ores.blocks.ManaBlock;
import neo_ores.blocks.ManaFurnace;
import neo_ores.blocks.ManaWorkbench;
import neo_ores.blocks.NeoOresPortal;
import neo_ores.blocks.StudyTable;
import neo_ores.blocks.UnditeBlock;
import neo_ores.capability.CapabilitySpellCreatingTable;
import neo_ores.command.CommandNeoOres;
import neo_ores.config.NeoOresConfig;
import neo_ores.creativetab.NeoOresTab;
import neo_ores.enchantments.EnchantmentOffensive;
import neo_ores.enchantments.EnchantmentSoulBound;
import neo_ores.event.NeoOresInitEvent;
import neo_ores.event.NeoOresInitEventAfterItems;
import neo_ores.event.NeoPlayerEvent;
import neo_ores.gui.GuiHandler;
import neo_ores.gui.StudyTabs;
import neo_ores.items.ItemEffected;
import neo_ores.items.ItemEssence;
import neo_ores.items.ItemEssenceCoreAir;
import neo_ores.items.ItemEssenceCoreEarth;
import neo_ores.items.ItemEssenceCoreFire;
import neo_ores.items.ItemEssenceCoreWater;
import neo_ores.items.ItemNeoArmor;
import neo_ores.items.ItemNeoAxe;
import neo_ores.items.ItemNeoHoe;
import neo_ores.items.ItemNeoPaxel;
import neo_ores.items.ItemNeoPickaxe;
import neo_ores.items.ItemNeoSpade;
import neo_ores.items.ItemNeoSword;
import neo_ores.items.ItemSpell;
import neo_ores.mana.TierCalc;
import neo_ores.mana.packet.PacketManaDataToClient;
import neo_ores.mana.packet.PacketManaDataToServer;
import neo_ores.proxy.CommonProxy;
import neo_ores.registry.ManaCraftingRecipeRegister;
import neo_ores.registry.SpellItemRegister;
import neo_ores.spell.SpellItem;
import neo_ores.spell.Spells;
import neo_ores.spell.Spells.SpellComposition;
import neo_ores.spell.SpellItemType;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import neo_ores.world.dimension.DimensionHelper.ToolType;
import neo_ores.world.dimension.WorldProviderTheFire;
import neo_ores.world.dimension.WorldProviderTheEarth;
import neo_ores.world.dimension.WorldProviderTheAir;
import neo_ores.world.dimension.WorldProviderTheWater;
import neo_ores.world.gen.NeoOresOreGen;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, acceptedMinecraftVersions=Reference.ACCEPTED_MINECRAFT_VERSIONS, 
dependencies="required-after:baubles@[1.5.2,);after:avaritia@[3.3.0,);after:jei@[4.16.1.301,);")
public class NeoOres 
{
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		NeoOresInfoCore.registerInfo(meta);
		MinecraftForge.EVENT_BUS.register(new NeoOresInitEvent());
		MinecraftForge.EVENT_BUS.register(new SpellItemRegister());
		MinecraftForge.EVENT_BUS.register(new ManaCraftingRecipeRegister());
		MinecraftForge.EVENT_BUS.register(new NeoOresInitEventAfterItems());
		MinecraftForge.EVENT_BUS.register(new NeoPlayerEvent());
		DimensionManager.registerDimension(THE_WATER.getId(), THE_WATER);
		DimensionManager.registerDimension(THE_EARTH.getId(), THE_EARTH);
		DimensionManager.registerDimension(THE_FIRE.getId(), THE_FIRE);
		DimensionManager.registerDimension(THE_AIR.getId(), THE_AIR);
		GameRegistry.registerWorldGenerator(new NeoOresOreGen(), 0);
		
		PACKET.registerMessage(PacketManaDataToClient.Handler.class, PacketManaDataToClient.class, 0, Side.CLIENT);
		PACKET.registerMessage(PacketManaDataToServer.Handler.class, PacketManaDataToServer.class, 1, Side.SERVER);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) 
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	}
	
	@EventHandler
	public void postInit(final FMLPostInitializationEvent event) 
	{
		if(Loader.isModLoaded("avaritia"))
		{
			NBTTagCompound nbt = new NBTTagCompound();

			if(!nbt.hasKey("tiers", 9))
			{
				nbt.setTag("tiers", new NBTTagList());
			}
			NBTTagList NBTList = nbt.getTagList("tiers", 10);
			if(NBTList != null && NBTList.getCompoundTagAt(0) != null)
			{
				NBTTagCompound itemNBT = new NBTTagCompound();
				itemNBT.setInteger("air", 10);
				itemNBT.setInteger("earth", 10);
				itemNBT.setInteger("fire", 10);
				itemNBT.setInteger("water", 10);

				NBTList.appendTag(itemNBT);
			}
			
			ItemStack helmet = new ItemStack(NeoOres.creative_helmet);
			helmet.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_helmet"), new ExtremeShapedRecipe(helmet,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOres.mana_block),'E',new ItemStack(NeoOres.earth_essence_core,1,10),'A',new ItemStack(NeoOres.air_essence_core,1,10),'F',new ItemStack(NeoOres.fire_essence_core,1,10),'W',new ItemStack(NeoOres.water_essence_core,1,10),'X',new ItemStack(ModItems.infinity_helmet))));
			ItemStack chestplate = new ItemStack(NeoOres.creative_chestplate);
			chestplate.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_chestplate"), new ExtremeShapedRecipe(chestplate,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOres.mana_block),'E',new ItemStack(NeoOres.earth_essence_core,1,10),'A',new ItemStack(NeoOres.air_essence_core,1,10),'F',new ItemStack(NeoOres.fire_essence_core,1,10),'W',new ItemStack(NeoOres.water_essence_core,1,10),'X',new ItemStack(ModItems.infinity_chestplate))));
			ItemStack leggings = new ItemStack(NeoOres.creative_leggings);
			leggings.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_leggings"), new ExtremeShapedRecipe(leggings,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOres.mana_block),'E',new ItemStack(NeoOres.earth_essence_core,1,10),'A',new ItemStack(NeoOres.air_essence_core,1,10),'F',new ItemStack(NeoOres.fire_essence_core,1,10),'W',new ItemStack(NeoOres.water_essence_core,1,10),'X',new ItemStack(ModItems.infinity_pants))));
			ItemStack boots = new ItemStack(NeoOres.creative_boots);
			boots.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_boots"), new ExtremeShapedRecipe(boots,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOres.mana_block),'E',new ItemStack(NeoOres.earth_essence_core,1,10),'A',new ItemStack(NeoOres.air_essence_core,1,10),'F',new ItemStack(NeoOres.fire_essence_core,1,10),'W',new ItemStack(NeoOres.water_essence_core,1,10),'X',new ItemStack(ModItems.infinity_boots))));
			ItemStack axe = new ItemStack(NeoOres.creative_axe);
			axe.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_axe"), new ExtremeShapedRecipe(axe,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOres.mana_block),'E',new ItemStack(NeoOres.earth_essence_core,1,10),'A',new ItemStack(NeoOres.air_essence_core,1,10),'F',new ItemStack(NeoOres.fire_essence_core,1,10),'W',new ItemStack(NeoOres.water_essence_core,1,10),'X',new ItemStack(ModItems.infinity_axe))));
			ItemStack hoe = new ItemStack(NeoOres.creative_hoe);
			hoe.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_hoe"), new ExtremeShapedRecipe(hoe,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOres.mana_block),'E',new ItemStack(NeoOres.earth_essence_core,1,10),'A',new ItemStack(NeoOres.air_essence_core,1,10),'F',new ItemStack(NeoOres.fire_essence_core,1,10),'W',new ItemStack(NeoOres.water_essence_core,1,10),'X',new ItemStack(ModItems.infinity_hoe))));
			ItemStack pickaxe = new ItemStack(NeoOres.creative_pickaxe);
			pickaxe.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_pickaxe"), new ExtremeShapedRecipe(pickaxe,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOres.mana_block),'E',new ItemStack(NeoOres.earth_essence_core,1,10),'A',new ItemStack(NeoOres.air_essence_core,1,10),'F',new ItemStack(NeoOres.fire_essence_core,1,10),'W',new ItemStack(NeoOres.water_essence_core,1,10),'X',new ItemStack(ModItems.infinity_pickaxe))));
			ItemStack shovel = new ItemStack(NeoOres.creative_shovel);
			shovel.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_shovel"), new ExtremeShapedRecipe(shovel,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOres.mana_block),'E',new ItemStack(NeoOres.earth_essence_core,1,10),'A',new ItemStack(NeoOres.air_essence_core,1,10),'F',new ItemStack(NeoOres.fire_essence_core,1,10),'W',new ItemStack(NeoOres.water_essence_core,1,10),'X',new ItemStack(ModItems.infinity_shovel))));
			ItemStack sword = new ItemStack(NeoOres.creative_sword);
			sword.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_sword"), new ExtremeShapedRecipe(sword,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOres.mana_block),'E',new ItemStack(NeoOres.earth_essence_core,1,10),'A',new ItemStack(NeoOres.air_essence_core,1,10),'F',new ItemStack(NeoOres.fire_essence_core,1,9),'W',new ItemStack(NeoOres.water_essence_core,1,9),'X',new ItemStack(ModItems.infinity_sword))));
			
		}
	}
	
	@EventHandler
	public void serverInit(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandNeoOres());
	}
	
	@SidedProxy(clientSide = "neo_ores.proxy.ClientProxy", serverSide = "neo_ores.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	public static TierCalc tierCalc = new TierCalc();
	
	@Instance(Reference.MOD_ID)
	public static NeoOres instance;
	
	@Metadata(Reference.MOD_ID)
	private static ModMetadata meta;
	
	public static final String LEGACY = "legacy";
	public static final Random RANDOM = new Random();
	
	public static ItemStack addRegacy(ItemStack stack)
	{
		ItemStack item = stack;
		NBTTagCompound nbt = new NBTTagCompound();
		if(item.hasTagCompound())
		{
			nbt = item.getTagCompound();
		}
		
		nbt.setBoolean(LEGACY, true);
		item.setTagCompound(nbt);
		return item;
	}
	
	public static ItemStack addEnchantment(ItemStack stack,Enchantment ench,int level)
	{
		stack.addEnchantment(ench, level);
		return stack;
	}
	
	public static ItemStack addName(ItemStack stack,String displayname)
	{
		stack.setTranslatableName(displayname);
		return stack;
	}
	
	public static final int guiIDManaWorkbench = 0;
	public static final int guiIDManaFurnace = 1;
	public static final int guiIDStudyTable = 2;
	
	public static final SimpleNetworkWrapper PACKET = NetworkRegistry.INSTANCE.newSimpleChannel("neo_ores".toLowerCase());
	
	public static final StudyTabs neo_ores = new StudyTabs("neo_ores",new ResourceLocation(Reference.MOD_ID,""));
	public static final CreativeTabs neo_ores_tab = new NeoOresTab("neo_ores_tab");
	
	@CapabilityInject(CapabilitySpellCreatingTable.class)
    public static final Capability<CapabilitySpellCreatingTable> SPELL_CAP = null;
	
	public static final DamageSource WATER = new DamageSource("neo_ores.water").setDamageIsAbsolute().setDamageBypassesArmor();
	public static final DamageSource FIRE = new DamageSource("neo_ores.fire").setDamageIsAbsolute().setDamageBypassesArmor();
	public static final DamageSource EARTH = new DamageSource("neo_ores.earth").setDamageIsAbsolute().setDamageBypassesArmor();
	public static final DamageSource AIR = new DamageSource("neo_ores.air").setDamageIsAbsolute().setDamageBypassesArmor();
	public static final DamageSource CERATIVE = new DamageSource("neo_ores.creative").setDamageIsAbsolute().setDamageBypassesArmor().setDamageAllowedInCreativeMode();
	
	public static EntityDamageSource setDamageByEntity(DamageSource source,Entity entity)
	{
		return (EntityDamageSource) new EntityDamageSource(source.getDamageType(),entity).setDamageBypassesArmor().setDamageIsAbsolute();
	}
	
	public static final DimensionType THE_WATER = DimensionType.register("The Gabry", "dim_water", NeoOresConfig.dim.dimwater, WorldProviderTheWater.class, false);
	public static final DimensionType THE_EARTH = DimensionType.register("The Ury", "dim_earth", NeoOresConfig.dim.dimearth, WorldProviderTheEarth.class, false);
	public static final DimensionType THE_FIRE = DimensionType.register("The Micha", "dim_fire", NeoOresConfig.dim.dimfire, WorldProviderTheFire.class, false);
	public static final DimensionType THE_AIR = DimensionType.register("The Rapha", "dim_air", NeoOresConfig.dim.dimair, WorldProviderTheAir.class, false);	
		
	public static final Item undite = new Item()
				.setRegistryName(Reference.MOD_ID, "undite")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("undite");
	public static final Item gnomite_ingot = new Item()
				.setRegistryName(Reference.MOD_ID, "gnomite_ingot")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("gnomite_ingot");
	public static final Item salamite = new Item()
				.setRegistryName(Reference.MOD_ID, "salamite")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("salamite");
		public static final Item sylphite = new Item()
				.setRegistryName(Reference.MOD_ID, "sylphite")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("sylphite");
		public static final Item mana_ingot = new ItemEffected()
				.setRegistryName(Reference.MOD_ID, "mana_ingot")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("mana_ingot");
		public static final Item essence = new ItemEssence()
				.setRegistryName(Reference.MOD_ID, "essence")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("essence");
		public static final Item air_essence_core = new ItemEssenceCoreAir()
				.setRegistryName(Reference.MOD_ID, "air_essence_core")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("air_essence_core");
		public static final Item earth_essence_core = new ItemEssenceCoreEarth()
				.setRegistryName(Reference.MOD_ID, "earth_essence_core")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("earth_essence_core");
		public static final Item fire_essence_core = new ItemEssenceCoreFire()
				.setRegistryName(Reference.MOD_ID, "fire_essence_core")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("fire_essence_core");
		public static final Item water_essence_core = new ItemEssenceCoreWater()
				.setRegistryName(Reference.MOD_ID, "water_essence_core")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("water_essence_core");
		public static final Item sanitite = new Item()
				.setRegistryName(Reference.MOD_ID, "sanitite")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("sanitite");
		public static final Item marlite_ingot = new Item()
				.setRegistryName(Reference.MOD_ID, "marlite_ingot")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("marlite_ingot");
		public static final Item aerite = new Item()
				.setRegistryName(Reference.MOD_ID, "aerite")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("aerite");
		public static final Item drenite = new Item()
				.setRegistryName(Reference.MOD_ID, "drenite")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("drenite");
		public static final Item guardite_ingot = new Item()
				.setRegistryName(Reference.MOD_ID, "guardite_ingot")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("guardite_ingot");
		public static final Item landite_ingot = new Item()
				.setRegistryName(Reference.MOD_ID, "landite_ingot")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("landite_ingot");
		public static final Item forcite = new Item()
				.setRegistryName(Reference.MOD_ID, "forcite")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("forcite");
		public static final Item flamite = new Item()
				.setRegistryName(Reference.MOD_ID, "flamite")
				.setCreativeTab(NeoOres.neo_ores_tab)
				.setUnlocalizedName("flamite");
		public static final Item spell = new ItemSpell()
				.setRegistryName(Reference.MOD_ID, "spell")
				.setUnlocalizedName("spell");
	
	public static final ToolMaterial toolUndite = EnumHelper.addToolMaterial("tool_undite", 12, 2601, 14.0F, 4.1F, 10).setRepairItem(new ItemStack(NeoOres.undite));
	public static final ToolMaterial toolSalamite = EnumHelper.addToolMaterial("tool_salamite", 16, 1820, 9.8F, 12.0F, 7).setRepairItem(new ItemStack(NeoOres.salamite));
	public static final ToolMaterial toolSylphite = EnumHelper.addToolMaterial("tool_sylphite", 14, 1274, 6.9F, 8.4F, 20).setRepairItem(new ItemStack(NeoOres.sylphite));
	public static final ToolMaterial toolGnomite = EnumHelper.addToolMaterial("tool_gnomite", 10, 892, 20.0F, 5.9F, 14).setRepairItem(new ItemStack(NeoOres.gnomite_ingot));
	public static final ToolMaterial toolCreative = EnumHelper.addToolMaterial("tool_creative", Integer.MAX_VALUE, Integer.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, (int)Byte.MAX_VALUE).setRepairItem(new ItemStack(NeoOres.mana_block));
	
	public static final ArmorMaterial armorUndite = EnumHelper.addArmorMaterial("armor_undite", "neo_ores:undite", 29, new int[] {5,8,10,6}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 4.0F).setRepairItem(new ItemStack(NeoOres.undite));
	public static final ArmorMaterial armorSalamite = EnumHelper.addArmorMaterial("armor_salamite", "neo_ores:salamite", 23, new int[] {3,7,9,4}, 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.2F).setRepairItem(new ItemStack(NeoOres.salamite));
	public static final ArmorMaterial armorSylphite = EnumHelper.addArmorMaterial("armor_sylphite", "neo_ores:sylphite", 45, new int[] {2,6,7,3}, 16, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.6F).setRepairItem(new ItemStack(NeoOres.sylphite));
	public static final ArmorMaterial armorGnomite = EnumHelper.addArmorMaterial("armor_gnomite", "neo_ores:gnomite", 36, new int[] {6,11,13,6}, 13, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F).setRepairItem(new ItemStack(NeoOres.gnomite_ingot));
	public static final ArmorMaterial armorCreative = EnumHelper.addArmorMaterial("armor_creative", "neo_ores:creative", Integer.MAX_VALUE, new int[] {Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE}, (int)Byte.MAX_VALUE, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, Float.MAX_VALUE).setRepairItem(new ItemStack(NeoOres.mana_block));
	
	public static final Item undite_axe = new ItemNeoAxe(toolUndite)
			.setToolType(ToolType.WATER)
			.setRegistryName(Reference.MOD_ID, "undite_axe")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("undite_axe");
	public static final Item undite_hoe = new ItemNeoHoe(toolUndite)
			.setToolType(ToolType.WATER)
			.setRegistryName(Reference.MOD_ID, "undite_hoe")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("undite_hoe");
	public static final Item undite_paxel = new ItemNeoPaxel(toolUndite)
			.setToolType(ToolType.WATER)
			.setRegistryName(Reference.MOD_ID, "undite_paxel")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("undite_paxel");
	public static final Item undite_pickaxe = new ItemNeoPickaxe(toolUndite)
			.setToolType(ToolType.WATER)
			.setRegistryName(Reference.MOD_ID, "undite_pickaxe")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("undite_pickaxe");
	public static final Item undite_shovel = new ItemNeoSpade(toolUndite)
			.setToolType(ToolType.WATER)
			.setRegistryName(Reference.MOD_ID, "undite_shovel")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("undite_shovel");
	public static final Item undite_sword = new ItemNeoSword(toolUndite)
			.setToolType(ToolType.WATER)
			.setRegistryName(Reference.MOD_ID, "undite_sword")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("undite_sword");
	public static final Item salamite_axe = new ItemNeoAxe(toolSalamite)
			.setToolType(ToolType.FIRE)
			.setRegistryName(Reference.MOD_ID, "salamite_axe")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("salamite_axe");
	public static final Item salamite_hoe = new ItemNeoHoe(toolSalamite)
			.setToolType(ToolType.FIRE)
			.setRegistryName(Reference.MOD_ID, "salamite_hoe")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("salamite_hoe");
	public static final Item salamite_paxel = new ItemNeoPaxel(toolSalamite)
			.setToolType(ToolType.WATER)
			.setRegistryName(Reference.MOD_ID, "salamite_paxel")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("salamite_paxel");
	public static final Item salamite_pickaxe = new ItemNeoPickaxe(toolSalamite)
			.setToolType(ToolType.FIRE)
			.setRegistryName(Reference.MOD_ID, "salamite_pickaxe")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("salamite_pickaxe");
	public static final Item salamite_shovel = new ItemNeoSpade(toolSalamite)
			.setToolType(ToolType.FIRE)
			.setRegistryName(Reference.MOD_ID, "salamite_shovel")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("salamite_shovel");
	public static final Item salamite_sword = new ItemNeoSword(toolSalamite)
			.setToolType(ToolType.FIRE)
			.setRegistryName(Reference.MOD_ID, "salamite_sword")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("salamite_sword");
	public static final Item gnomite_axe = new ItemNeoAxe(toolGnomite)
			.setToolType(ToolType.EARTH)
			.setRegistryName(Reference.MOD_ID, "gnomite_axe")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("gnomite_axe");
	public static final Item gnomite_hoe = new ItemNeoHoe(toolGnomite)
			.setToolType(ToolType.EARTH)
			.setRegistryName(Reference.MOD_ID, "gnomite_hoe")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("gnomite_hoe");
	public static final Item gnomite_paxel = new ItemNeoPaxel(toolGnomite)
			.setToolType(ToolType.EARTH)
			.setRegistryName(Reference.MOD_ID, "gnomite_paxel")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("gnomite_paxel");
	public static final Item gnomite_pickaxe = new ItemNeoPickaxe(toolGnomite)
			.setToolType(ToolType.EARTH)
			.setRegistryName(Reference.MOD_ID, "gnomite_pickaxe")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("gnomite_pickaxe");
	public static final Item gnomite_shovel = new ItemNeoSpade(toolGnomite)
			.setToolType(ToolType.EARTH)
			.setRegistryName(Reference.MOD_ID, "gnomite_shovel")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("gnomite_shovel");
	public static final Item gnomite_sword = new ItemNeoSword(toolGnomite)
			.setToolType(ToolType.EARTH)
			.setRegistryName(Reference.MOD_ID, "gnomite_sword")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("gnomite_sword");
	public static final Item sylphite_axe = new ItemNeoAxe(toolSylphite)
			.setToolType(ToolType.AIR)
			.setRegistryName(Reference.MOD_ID, "sylphite_axe")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("sylphite_axe");
	public static final Item sylphite_hoe = new ItemNeoHoe(toolSylphite)
			.setToolType(ToolType.AIR)
			.setRegistryName(Reference.MOD_ID, "sylphite_hoe")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("sylphite_hoe");
	public static final Item sylphite_paxel = new ItemNeoPaxel(toolSylphite)
			.setToolType(ToolType.AIR)
			.setRegistryName(Reference.MOD_ID, "sylphite_paxel")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("sylphite_paxel");
	public static final Item sylphite_pickaxe = new ItemNeoPickaxe(toolSylphite)
			.setToolType(ToolType.AIR)
			.setRegistryName(Reference.MOD_ID, "sylphite_pickaxe")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("sylphite_pickaxe");
	public static final Item sylphite_shovel = new ItemNeoSpade(toolSylphite)
			.setToolType(ToolType.AIR)
			.setRegistryName(Reference.MOD_ID, "sylphite_shovel")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("sylphite_shovel");
	public static final Item sylphite_sword = new ItemNeoSword(toolSylphite)
			.setToolType(ToolType.AIR)
			.setRegistryName(Reference.MOD_ID, "sylphite_sword")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("sylphite_sword");
	public static final Item creative_axe = new ItemNeoAxe(toolCreative)
			.setToolType(ToolType.CREATIVE)
			.setRegistryName(Reference.MOD_ID, "creative_axe")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("creative_axe");
	public static final Item creative_hoe = new ItemNeoHoe(toolCreative)
			.setToolType(ToolType.CREATIVE)
			.setRegistryName(Reference.MOD_ID, "creative_hoe")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("creative_hoe");
	public static final Item creative_paxel = new ItemNeoPaxel(toolCreative)
			.setToolType(ToolType.CREATIVE)
			.setRegistryName(Reference.MOD_ID, "creative_paxel")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("creative_paxel");
	public static final Item creative_pickaxe = new ItemNeoPickaxe(toolCreative)
			.setToolType(ToolType.CREATIVE)
			.setRegistryName(Reference.MOD_ID, "creative_pickaxe")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("creative_pickaxe");
	public static final Item creative_shovel = new ItemNeoSpade(toolCreative)
			.setToolType(ToolType.CREATIVE)
			.setRegistryName(Reference.MOD_ID, "creative_shovel")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("creative_shovel");
	public static final Item creative_sword = new ItemNeoSword(toolCreative)
			.setToolType(ToolType.CREATIVE)
			.setRegistryName(Reference.MOD_ID, "creative_sword")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("creative_sword");
	
	public static final Item undite_helmet = new ItemNeoArmor(NeoOres.armorUndite, 3, EntityEquipmentSlot.HEAD)
			.setRegistryName(Reference.MOD_ID, "undite_helmet")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("undite_helmet");
	public static final Item undite_chestplate = new ItemNeoArmor(NeoOres.armorUndite, 3, EntityEquipmentSlot.CHEST)
			.setRegistryName(Reference.MOD_ID, "undite_chestplate")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("undite_chestplate");
	public static final Item undite_leggings = new ItemNeoArmor(NeoOres.armorUndite, 3, EntityEquipmentSlot.LEGS)
			.setRegistryName(Reference.MOD_ID, "undite_leggings")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("undite_leggings");
	public static final Item undite_boots = new ItemNeoArmor(NeoOres.armorUndite, 3, EntityEquipmentSlot.FEET)
			.setRegistryName(Reference.MOD_ID, "undite_boots")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("undite_boots");
	public static final Item salamite_helmet = new ItemNeoArmor(NeoOres.armorSalamite, 3, EntityEquipmentSlot.HEAD)
			.setRegistryName(Reference.MOD_ID, "salamite_helmet")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("salamite_helmet");
	public static final Item salamite_chestplate = new ItemNeoArmor(NeoOres.armorSalamite, 3, EntityEquipmentSlot.CHEST)
			.setRegistryName(Reference.MOD_ID, "salamite_chestplate")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("salamite_chestplate");
	public static final Item salamite_leggings = new ItemNeoArmor(NeoOres.armorSalamite, 3, EntityEquipmentSlot.LEGS)
			.setRegistryName(Reference.MOD_ID, "salamite_leggings")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("salamite_leggings");
	public static final Item salamite_boots = new ItemNeoArmor(NeoOres.armorSalamite, 3, EntityEquipmentSlot.FEET)
			.setRegistryName(Reference.MOD_ID, "salamite_boots")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("salamite_boots");
	public static final Item gnomite_helmet = new ItemNeoArmor(NeoOres.armorGnomite, 3, EntityEquipmentSlot.HEAD)
			.setRegistryName(Reference.MOD_ID, "gnomite_helmet")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("gnomite_helmet");
	public static final Item gnomite_chestplate = new ItemNeoArmor(NeoOres.armorGnomite, 3, EntityEquipmentSlot.CHEST)
			.setRegistryName(Reference.MOD_ID, "gnomite_chestplate")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("gnomite_chestplate");
	public static final Item gnomite_leggings = new ItemNeoArmor(NeoOres.armorGnomite, 3, EntityEquipmentSlot.LEGS)
			.setRegistryName(Reference.MOD_ID, "gnomite_leggings")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("gnomite_leggings");
	public static final Item gnomite_boots = new ItemNeoArmor(NeoOres.armorGnomite, 3, EntityEquipmentSlot.FEET)
			.setRegistryName(Reference.MOD_ID, "gnomite_boots")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("gnomite_boots");
	public static final Item sylphite_helmet = new ItemNeoArmor(NeoOres.armorSylphite, 3, EntityEquipmentSlot.HEAD)
			.setRegistryName(Reference.MOD_ID, "sylphite_helmet")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("sylphite_helmet");
	public static final Item sylphite_chestplate = new ItemNeoArmor(NeoOres.armorSylphite, 3, EntityEquipmentSlot.CHEST)
			.setRegistryName(Reference.MOD_ID, "sylphite_chestplate")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("sylphite_chestplate");
	public static final Item sylphite_leggings = new ItemNeoArmor(NeoOres.armorSylphite, 3, EntityEquipmentSlot.LEGS)
			.setRegistryName(Reference.MOD_ID, "sylphite_leggings")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("sylphite_leggings");
	public static final Item sylphite_boots = new ItemNeoArmor(NeoOres.armorSylphite, 3, EntityEquipmentSlot.FEET)
			.setRegistryName(Reference.MOD_ID, "sylphite_boots")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("sylphite_boots");
	public static final Item creative_helmet = new ItemNeoArmor(NeoOres.armorCreative, 3, EntityEquipmentSlot.HEAD)
			.setRegistryName(Reference.MOD_ID, "creative_helmet")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("creative_helmet");
	public static final Item creative_chestplate = new ItemNeoArmor(NeoOres.armorCreative, 3, EntityEquipmentSlot.CHEST)
			.setRegistryName(Reference.MOD_ID, "creative_chestplate")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("creative_chestplate");
	public static final Item creative_leggings = new ItemNeoArmor(NeoOres.armorCreative, 3, EntityEquipmentSlot.LEGS)
			.setRegistryName(Reference.MOD_ID, "creative_leggings")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("creative_leggings");
	public static final Item creative_boots = new ItemNeoArmor(NeoOres.armorCreative, 3, EntityEquipmentSlot.FEET)
			.setRegistryName(Reference.MOD_ID, "creative_boots")
			.setCreativeTab(NeoOres.neo_ores_tab)
			.setUnlocalizedName("creative_boots");
	
	public static final Block mana_workbench = new ManaWorkbench()
			.setRegistryName(Reference.MOD_ID,"mana_workbench")
			.setUnlocalizedName("mana_workbench")
			.setCreativeTab(NeoOres.neo_ores_tab);	
	public static final Block mana_furnace = new ManaFurnace(false)
			.setRegistryName(Reference.MOD_ID,"mana_furnace")
			.setUnlocalizedName("mana_furnace")
			.setCreativeTab(NeoOres.neo_ores_tab);	
	public static final Block study_table = new StudyTable()
			.setRegistryName(Reference.MOD_ID,"study_table")
			.setUnlocalizedName("study_table")
			.setCreativeTab(NeoOres.neo_ores_tab);	
	public static final Block lit_mana_furnace = new ManaFurnace(true)
			.setRegistryName(Reference.MOD_ID,"lit_mana_furnace")
			.setUnlocalizedName("mana_furnace")
			.setCreativeTab(null);
	public static final Block mana_block = new ManaBlock()
			.setRegistryName(Reference.MOD_ID,"mana_block")
			.setUnlocalizedName("mana_block")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block empty_portal = new EmptyPortal(false)
			.setRegistryName(Reference.MOD_ID,"empty_portal")
			.setUnlocalizedName("empty_portal")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block empty_portal_water = new EmptyPortal(true)
			.setRegistryName(Reference.MOD_ID,"portal_with_water")
			.setUnlocalizedName("portal_with_water")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block earth_portal = new NeoOresPortal(0)
			.setRegistryName(Reference.MOD_ID,"earth_portal")
			.setUnlocalizedName("earth_portal")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block water_portal = new NeoOresPortal(1)
			.setRegistryName(Reference.MOD_ID,"water_portal")
			.setUnlocalizedName("water_portal")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block air_portal = new NeoOresPortal(2)
			.setRegistryName(Reference.MOD_ID,"air_portal")
			.setUnlocalizedName("air_portal")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block fire_portal = new NeoOresPortal(3)
			.setRegistryName(Reference.MOD_ID,"fire_portal")
			.setUnlocalizedName("fire_portal")
			.setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block undite_block = new UnditeBlock()
			.setRegistryName(Reference.MOD_ID,"undite_block")
			.setUnlocalizedName("undite_block")
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
	
	public static final Block landite_ore = new BlockNeoOre("landite_ore",3,DimensionName.EARTH, 0.0F, true, null,0, 1, 1, 0, 0).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block marlite_ore = new BlockNeoOre("marlite_ore",4,DimensionName.WATER, 0.0F, true, null,0, 1, 1, 0, 0).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block drenite_ore = new BlockNeoOre("drenite_ore",5,DimensionName.AIR, 0.0F, false, NeoOres.drenite,0, 1, 1, 6, 13).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block forcite_ore = new BlockNeoOre("forcite_ore",6,DimensionName.FIRE, 0.0F, false, NeoOres.forcite,0, 1, 1, 7, 15).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block guardite_ore = new BlockNeoOre("guardite_ore",7,DimensionName.EARTH, 0.0F, true, null,0, 1, 1, 0, 0).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block sanitite_ore = new BlockNeoOre("sanitite_ore",8,DimensionName.WATER, 0.0F, false, NeoOres.sanitite,0, 1, 1, 9, 19).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block aerite_ore = new BlockNeoOre("aerite_ore",9,DimensionName.AIR, 0.0F, false, NeoOres.aerite,0, 1, 1, 10, 21).setCreativeTab(NeoOres.neo_ores_tab);
	public static final Block flamite_ore = new BlockNeoOre("flamite_ore",10,DimensionName.FIRE, 0.0F, false, NeoOres.flamite,0, 1, 1, 11, 23).setCreativeTab(NeoOres.neo_ores_tab);
	
	public static final Block dim_stone = new BlockDimension("dim_stone", Material.ROCK, 1.5F, 10.0F, "pickaxe", 0, 0.0F, SoundType.STONE).setCreativeTab(neo_ores_tab);
	
	public static final Enchantment offensive = new EnchantmentOffensive();
	public static final Enchantment soulbound = new EnchantmentSoulBound();
	
	public static final SpellItem spell_touch = new SpellItem(Reference.MOD_ID,"touch",1,SpellItemType.EARTH,4,1,Arrays.asList(new ItemStack(Blocks.WOOL)),"touch",null,0,0,new ResourceLocation(Reference.MOD_ID, "touch"),neo_ores,new Spells.Touch());
	public static final SpellItem spell_dig = new SpellItem(Reference.MOD_ID,"dig",1,SpellItemType.EARTH,1,1,Arrays.asList(new ItemStack(Items.IRON_SHOVEL)),"dig",NeoOres.spell_touch,0,1,new ResourceLocation(Reference.MOD_ID, "dig"),neo_ores ,new Spells.SpellDig());
	public static final SpellItem spell_support_liquid = new SpellItem(Reference.MOD_ID,"support_liquid",4,SpellItemType.WATER,1,3,Arrays.asList(new ItemStack(Items.BUCKET),new ItemStack(NeoOres.undite)),"support_liquid",NeoOres.spell_dig,0,2,new ResourceLocation(Reference.MOD_ID,"support_liquid"),neo_ores,new Spells.SupportLiquid());
	
	public static final SpellItem spell_harvestLv1 = new SpellItem(Reference.MOD_ID,"harvest1",1,SpellItemType.EARTH,1,3,Arrays.asList(new ItemStack(Items.BUCKET),new ItemStack(NeoOres.undite)),"harvest_level",NeoOres.spell_dig,1,2,new ResourceLocation(Reference.MOD_ID,"harvest_level"),neo_ores,new Spells.HarvestLevel(1));
	public static final SpellItem spell_harvestLv2 = new SpellItem(Reference.MOD_ID,"harvest2",2,SpellItemType.EARTH,1,3,Arrays.asList(new ItemStack(Items.BUCKET),new ItemStack(NeoOres.undite)),"harvest_level",NeoOres.spell_harvestLv1,1,3,new ResourceLocation(Reference.MOD_ID,"harvest_level"),neo_ores,new Spells.HarvestLevel(2));
	public static final SpellItem spell_harvestLv3 = new SpellItem(Reference.MOD_ID,"harvest3",3,SpellItemType.EARTH,1,3,Arrays.asList(new ItemStack(Items.BUCKET),new ItemStack(NeoOres.undite)),"harvest_level",NeoOres.spell_harvestLv2,2,4,new ResourceLocation(Reference.MOD_ID,"harvest_level"),neo_ores,new Spells.HarvestLevel(3));
	public static final SpellItem spell_harvestLv4 = new SpellItem(Reference.MOD_ID,"harvest4",4,SpellItemType.EARTH,1,3,Arrays.asList(new ItemStack(Items.BUCKET),new ItemStack(NeoOres.undite)),"harvest_level",NeoOres.spell_harvestLv3,2,4,new ResourceLocation(Reference.MOD_ID,"harvest_level"),neo_ores,new Spells.HarvestLevel(4));
	public static final SpellItem spell_harvestLv5 = new SpellItem(Reference.MOD_ID,"harvest5",5,SpellItemType.EARTH,1,3,Arrays.asList(new ItemStack(Items.BUCKET),new ItemStack(NeoOres.undite)),"harvest_level",NeoOres.spell_harvestLv4,2,4,new ResourceLocation(Reference.MOD_ID,"harvest_level"),neo_ores,new Spells.HarvestLevel(5));
	public static final SpellItem spell_harvestLv6 = new SpellItem(Reference.MOD_ID,"harvest6",6,SpellItemType.EARTH,1,3,Arrays.asList(new ItemStack(Items.BUCKET),new ItemStack(NeoOres.undite)),"harvest_level",NeoOres.spell_harvestLv5,2,4,new ResourceLocation(Reference.MOD_ID,"harvest_level"),neo_ores,new Spells.HarvestLevel(6));
	public static final SpellItem spell_harvestLv7 = new SpellItem(Reference.MOD_ID,"harvest7",7,SpellItemType.EARTH,1,3,Arrays.asList(new ItemStack(Items.BUCKET),new ItemStack(NeoOres.undite)),"harvest_level",NeoOres.spell_harvestLv6,2,4,new ResourceLocation(Reference.MOD_ID,"harvest_level"),neo_ores,new Spells.HarvestLevel(7));
	public static final SpellItem spell_harvestLv8 = new SpellItem(Reference.MOD_ID,"harvest8",8,SpellItemType.EARTH,1,3,Arrays.asList(new ItemStack(Items.BUCKET),new ItemStack(NeoOres.undite)),"harvest_level",NeoOres.spell_harvestLv7,2,4,new ResourceLocation(Reference.MOD_ID,"harvest_level"),neo_ores,new Spells.HarvestLevel(8));
	public static final SpellItem spell_harvestLv9 = new SpellItem(Reference.MOD_ID,"harvest9",9,SpellItemType.EARTH,1,3,Arrays.asList(new ItemStack(Items.BUCKET),new ItemStack(NeoOres.undite)),"harvest_level",NeoOres.spell_harvestLv8,2,4,new ResourceLocation(Reference.MOD_ID,"harvest_level"),neo_ores,new Spells.HarvestLevel(9));
	public static final SpellItem spell_harvestLv10 = new SpellItem(Reference.MOD_ID,"harvest10",10,SpellItemType.EARTH,1,3,Arrays.asList(new ItemStack(Items.BUCKET),new ItemStack(NeoOres.undite)),"harvest_level",NeoOres.spell_harvestLv9,2,4,new ResourceLocation(Reference.MOD_ID,"harvest_level"),neo_ores,new Spells.HarvestLevel(10));
	public static final SpellItem spell_harvestLv11 = new SpellItem(Reference.MOD_ID,"harvest11",11,SpellItemType.EARTH,1,3,Arrays.asList(new ItemStack(Items.BUCKET),new ItemStack(NeoOres.undite)),"harvest_level",NeoOres.spell_harvestLv10,2,4,new ResourceLocation(Reference.MOD_ID,"harvest_level"),neo_ores,new Spells.HarvestLevel(11));
	
	public static final SpellItem spell_composition = new SpellItem(Reference.MOD_ID,"composiotion",1,SpellItemType.EARTH,1,3,Arrays.asList(new ItemStack(Items.BUCKET),new ItemStack(NeoOres.undite)),"composition",NeoOres.spell_dig,1,1,new ResourceLocation(Reference.MOD_ID,"composition"),neo_ores,new SpellComposition());
	public static final SpellItem spell_earth_damage = new SpellItem(Reference.MOD_ID,"earth_damage",1,SpellItemType.EARTH,1,1,Arrays.asList(new ItemStack(Items.IRON_SHOVEL)),"earth_damage",NeoOres.spell_dig,-1,1,new ResourceLocation(Reference.MOD_ID, "earth_damage"),neo_ores,new Spells.SpellEarthDamage());
}
