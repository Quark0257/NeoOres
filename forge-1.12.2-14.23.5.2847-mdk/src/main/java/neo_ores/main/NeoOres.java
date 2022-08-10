package neo_ores.main;

import neo_ores.api.spell.KnowledgeTab;
import neo_ores.client.gui.GuiHandler;
import neo_ores.command.CommandNeoOres;
import neo_ores.config.NeoOresConfig;
import neo_ores.creativetab.NeoOresTab;
import neo_ores.enchantments.EnchantmentOffensive;
import neo_ores.enchantments.EnchantmentSoulBound;
import neo_ores.event.NeoOresInitEvent;
import neo_ores.event.NeoOresInitEventAfterItems;
import neo_ores.event.NeoPlayerEvent;
import neo_ores.packet.PacketItemsToClient;
import neo_ores.packet.PacketManaDataToClient;
import neo_ores.packet.PacketManaDataToServer;
import neo_ores.packet.PacketSRCTToClient;
import neo_ores.packet.PacketSRCTToServer;
import neo_ores.potion.PotionAntiKnockback;
import neo_ores.potion.PotionFreeze;
import neo_ores.potion.PotionGravity;
import neo_ores.potion.PotionManaBoost;
import neo_ores.potion.PotionManaRegeneration;
import neo_ores.potion.PotionManaWeakness;
import neo_ores.potion.PotionShield;
import neo_ores.potion.PotionUndying;
import neo_ores.proxy.CommonProxy;
import neo_ores.world.dimension.WorldProviderTheFire;
import neo_ores.world.dimension.WorldProviderTheEarth;
import neo_ores.world.dimension.WorldProviderTheAir;
import neo_ores.world.dimension.WorldProviderTheWater;
import neo_ores.world.gen.NeoOresOreGen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
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

import java.util.Random;

import morph.avaritia.init.ModItems;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.ExtremeShapedRecipe;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, acceptedMinecraftVersions=Reference.ACCEPTED_MINECRAFT_VERSIONS, 
dependencies="required-after:baubles@[1.5.2,);after:avaritia@[3.3.0,);after:jei;")
public class NeoOres 
{
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		NeoOresInfoCore.registerInfo(meta);
		MinecraftForge.EVENT_BUS.register(new NeoOresInitEvent());
		MinecraftForge.EVENT_BUS.register(new NeoOresInitEventAfterItems());
		MinecraftForge.EVENT_BUS.register(new NeoPlayerEvent());
		DimensionManager.registerDimension(THE_WATER.getId(), THE_WATER);
		DimensionManager.registerDimension(THE_EARTH.getId(), THE_EARTH);
		DimensionManager.registerDimension(THE_FIRE.getId(), THE_FIRE);
		DimensionManager.registerDimension(THE_AIR.getId(), THE_AIR);
		GameRegistry.registerWorldGenerator(new NeoOresOreGen(), 0);
		
		PACKET.registerMessage(PacketManaDataToClient.Handler.class, PacketManaDataToClient.class, 0, Side.CLIENT);
		PACKET.registerMessage(PacketManaDataToServer.Handler.class, PacketManaDataToServer.class, 1, Side.SERVER);
		PACKET.registerMessage(PacketItemsToClient.Handler.class, PacketItemsToClient.class, 2, Side.CLIENT);
		PACKET.registerMessage(PacketSRCTToServer.Handler.class, PacketSRCTToServer.class, 3, Side.SERVER);
		PACKET.registerMessage(PacketSRCTToClient.Handler.class, PacketSRCTToClient.class, 4, Side.CLIENT);
		
		if(event.getSide().isClient())
		{
			NeoOresInitEvent.registerRendering();
		}
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) 
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		
		NeoOresInitEvent.registerEntity(this);
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
				itemNBT.setInteger("air", 11);
				itemNBT.setInteger("earth", 11);
				itemNBT.setInteger("fire", 11);
				itemNBT.setInteger("water", 11);

				NBTList.appendTag(itemNBT);
			}
			
			ItemStack helmet = new ItemStack(NeoOresItems.creative_helmet);
			helmet.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_helmet"), new ExtremeShapedRecipe(helmet,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOresBlocks.mana_block),'E',new ItemStack(NeoOresItems.earth_essence_core,1,10),'A',new ItemStack(NeoOresItems.air_essence_core,1,10),'F',new ItemStack(NeoOresItems.fire_essence_core,1,10),'W',new ItemStack(NeoOresItems.water_essence_core,1,10),'X',new ItemStack(ModItems.infinity_helmet))));
			ItemStack chestplate = new ItemStack(NeoOresItems.creative_chestplate);
			chestplate.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_chestplate"), new ExtremeShapedRecipe(chestplate,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOresBlocks.mana_block),'E',new ItemStack(NeoOresItems.earth_essence_core,1,10),'A',new ItemStack(NeoOresItems.air_essence_core,1,10),'F',new ItemStack(NeoOresItems.fire_essence_core,1,10),'W',new ItemStack(NeoOresItems.water_essence_core,1,10),'X',new ItemStack(ModItems.infinity_chestplate))));
			ItemStack leggings = new ItemStack(NeoOresItems.creative_leggings);
			leggings.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_leggings"), new ExtremeShapedRecipe(leggings,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOresBlocks.mana_block),'E',new ItemStack(NeoOresItems.earth_essence_core,1,10),'A',new ItemStack(NeoOresItems.air_essence_core,1,10),'F',new ItemStack(NeoOresItems.fire_essence_core,1,10),'W',new ItemStack(NeoOresItems.water_essence_core,1,10),'X',new ItemStack(ModItems.infinity_pants))));
			ItemStack boots = new ItemStack(NeoOresItems.creative_boots);
			boots.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_boots"), new ExtremeShapedRecipe(boots,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOresBlocks.mana_block),'E',new ItemStack(NeoOresItems.earth_essence_core,1,10),'A',new ItemStack(NeoOresItems.air_essence_core,1,10),'F',new ItemStack(NeoOresItems.fire_essence_core,1,10),'W',new ItemStack(NeoOresItems.water_essence_core,1,10),'X',new ItemStack(ModItems.infinity_boots))));
			ItemStack axe = new ItemStack(NeoOresItems.creative_axe);
			axe.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_axe"), new ExtremeShapedRecipe(axe,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOresBlocks.mana_block),'E',new ItemStack(NeoOresItems.earth_essence_core,1,10),'A',new ItemStack(NeoOresItems.air_essence_core,1,10),'F',new ItemStack(NeoOresItems.fire_essence_core,1,10),'W',new ItemStack(NeoOresItems.water_essence_core,1,10),'X',new ItemStack(ModItems.infinity_axe))));
			ItemStack hoe = new ItemStack(NeoOresItems.creative_hoe);
			hoe.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_hoe"), new ExtremeShapedRecipe(hoe,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOresBlocks.mana_block),'E',new ItemStack(NeoOresItems.earth_essence_core,1,10),'A',new ItemStack(NeoOresItems.air_essence_core,1,10),'F',new ItemStack(NeoOresItems.fire_essence_core,1,10),'W',new ItemStack(NeoOresItems.water_essence_core,1,10),'X',new ItemStack(ModItems.infinity_hoe))));
			ItemStack pickaxe = new ItemStack(NeoOresItems.creative_pickaxe);
			pickaxe.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_pickaxe"), new ExtremeShapedRecipe(pickaxe,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOresBlocks.mana_block),'E',new ItemStack(NeoOresItems.earth_essence_core,1,10),'A',new ItemStack(NeoOresItems.air_essence_core,1,10),'F',new ItemStack(NeoOresItems.fire_essence_core,1,10),'W',new ItemStack(NeoOresItems.water_essence_core,1,10),'X',new ItemStack(ModItems.infinity_pickaxe))));
			ItemStack shovel = new ItemStack(NeoOresItems.creative_shovel);
			shovel.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_shovel"), new ExtremeShapedRecipe(shovel,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOresBlocks.mana_block),'E',new ItemStack(NeoOresItems.earth_essence_core,1,10),'A',new ItemStack(NeoOresItems.air_essence_core,1,10),'F',new ItemStack(NeoOresItems.fire_essence_core,1,10),'W',new ItemStack(NeoOresItems.water_essence_core,1,10),'X',new ItemStack(ModItems.infinity_shovel))));
			ItemStack sword = new ItemStack(NeoOresItems.creative_sword);
			sword.setTagCompound(nbt);
			AvaritiaRecipeManager.EXTREME_RECIPES.put(new ResourceLocation(Reference.MOD_ID,"creative_sword"), new ExtremeShapedRecipe(sword,CraftingHelper.parseShaped("MEEEEEEEM","WMEEEEEMA","WWMEEEMAA","WWWMEMAAA","WWWWXAAAA","WWWMFMAAA","WWMFFFMAA","WMFFFFFMA","MFFFFFFFM",
					'M',new ItemStack(NeoOresBlocks.mana_block),'E',new ItemStack(NeoOresItems.earth_essence_core,1,10),'A',new ItemStack(NeoOresItems.air_essence_core,1,10),'F',new ItemStack(NeoOresItems.fire_essence_core,1,9),'W',new ItemStack(NeoOresItems.water_essence_core,1,9),'X',new ItemStack(ModItems.infinity_sword))));
			
		}
	}
	
	@EventHandler
	public void serverInit(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandNeoOres());
	}
	
	@SidedProxy(clientSide = "neo_ores.proxy.ClientProxy", serverSide = "neo_ores.proxy.CommonProxy")
	public static CommonProxy proxy;
	
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
	public static final int guiIDSRCT = 3;
	
	public static final SimpleNetworkWrapper PACKET = NetworkRegistry.INSTANCE.newSimpleChannel("neo_ores".toLowerCase());
	
	public static final KnowledgeTab neo_ores = new KnowledgeTab("itemGroup.neo_ores_tab");

	public static final CreativeTabs neo_ores_tab = new NeoOresTab("neo_ores_tab");

	public static final DamageSource WATER = new DamageSource("neo_ores.water").setDamageBypassesArmor().setDamageIsAbsolute();
	public static final DamageSource FIRE = new DamageSource("neo_ores.fire").setDamageBypassesArmor().setDamageIsAbsolute();
	public static final DamageSource EARTH = new DamageSource("neo_ores.earth").setDamageBypassesArmor().setDamageIsAbsolute();
	public static final DamageSource AIR = new DamageSource("neo_ores.air").setDamageBypassesArmor().setDamageIsAbsolute();
	public static final DamageSource Payment = new DamageSource("neo_ores.payment").setDamageBypassesArmor().setDamageIsAbsolute();
	public static final DamageSource CERATIVE = new DamageSource("neo_ores.creative").setDamageIsAbsolute().setDamageBypassesArmor().setDamageAllowedInCreativeMode();
	
	public static EntityDamageSource setDamageByEntity(DamageSource source,Entity entity)
	{
		EntityDamageSource eds = new EntityDamageSource(source.getDamageType(),entity);
		if(source.isDamageAbsolute()) eds.setDamageIsAbsolute();
		if(source.isUnblockable()) eds.setDamageBypassesArmor();
		return eds;
	}
	
	public static final DimensionType THE_WATER = DimensionType.register("The Gabry", "dim_water", NeoOresConfig.dim.dimwater, WorldProviderTheWater.class, false);
	public static final DimensionType THE_EARTH = DimensionType.register("The Ury", "dim_earth", NeoOresConfig.dim.dimearth, WorldProviderTheEarth.class, false);
	public static final DimensionType THE_FIRE = DimensionType.register("The Micha", "dim_fire", NeoOresConfig.dim.dimfire, WorldProviderTheFire.class, false);
	public static final DimensionType THE_AIR = DimensionType.register("The Rapha", "dim_air", NeoOresConfig.dim.dimair, WorldProviderTheAir.class, false);	
	
	public static final Enchantment offensive = new EnchantmentOffensive();
	public static final Enchantment soulbound = new EnchantmentSoulBound();
	
	public static final Potion mana_boost = new PotionManaBoost("neo_ores.effect.mana_boost").setIconIndex(0,0).setRegistryName(Reference.MOD_ID, "mana_boost").setBeneficial();
	public static final Potion mana_weakness = new PotionManaWeakness("neo_ores.effect.mana_weakness").setIconIndex(2,0).setRegistryName(Reference.MOD_ID, "mana_weakness");
	public static final Potion mana_regeneration = new PotionManaRegeneration("neo_ores.effect.mana_regeneration").setIconIndex(1,0).setRegistryName(Reference.MOD_ID, "mana_regeneration").setBeneficial();
	public static final Potion gravity = new PotionGravity("neo_ores.effect.gravity").setIconIndex(4,0).setRegistryName(Reference.MOD_ID, "gravity");
	public static final Potion freeze = new PotionFreeze("neo_ores.effect.freeze").setIconIndex(3,0).setRegistryName(Reference.MOD_ID, "freeze");
	public static final Potion undying = new PotionUndying("neo_ores.effect.undying").setIconIndex(6,0).setRegistryName(Reference.MOD_ID, "undying").setBeneficial();
	public static final Potion shield = new PotionShield("neo_ores.effect.shield").setIconIndex(5,0).setRegistryName(Reference.MOD_ID, "shield").setBeneficial();
	public static final Potion antiknockback = new PotionAntiKnockback("neo_ores.effect.antiknockback").setIconIndex(7,0).setRegistryName(Reference.MOD_ID, "antiknockback").setBeneficial();
	
	public static final PotionType mana_regen = new PotionType("mana_regen",new PotionEffect(NeoOres.mana_regeneration,3600)).setRegistryName(Reference.MOD_ID, "mana_regen");
	public static final PotionType strong_mana_regen = new PotionType("mana_regen",new PotionEffect(NeoOres.mana_regeneration,1800,1)).setRegistryName(Reference.MOD_ID, "strong_mana_regen");
	public static final PotionType long_mana_regen = new PotionType("mana_regen",new PotionEffect(NeoOres.mana_regeneration,9600)).setRegistryName(Reference.MOD_ID, "long_mana_regen");
	

}
