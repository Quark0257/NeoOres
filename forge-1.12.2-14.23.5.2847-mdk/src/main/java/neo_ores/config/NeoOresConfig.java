package neo_ores.config;

import neo_ores.main.Reference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.Config.RequiresWorldRestart;
import net.minecraftforge.common.config.Config.SlidingOption;
import net.minecraftforge.common.config.Config.Type;

@Config(modid=Reference.MOD_ID,name="neo_ores",type=Type.INSTANCE)
public class NeoOresConfig 
{
	@LangKey("config.category.dimensions")
	public static DimID dim = new DimID();

	public static class DimID
	{
		@LangKey("config.dimensions.the_earth")
		@RequiresMcRestart
		public int dimearth = 253;
		
		@LangKey("config.dimensions.the_water")
		@RequiresMcRestart
		public int dimwater = 255;
		
		@LangKey("config.dimensions.the_air")
		@RequiresMcRestart
		public int dimair= 252;
		
		@LangKey("config.dimensions.the_fire")
		@RequiresMcRestart
		public int dimfire = 254;
	}
	
	@LangKey("config.category.ore_gen")
	public static OreGen ore_gen = new OreGen();
	
	public static class OreGen
	{
		@LangKey("config.category.ore_all")
		@RequiresWorldRestart
		public ForAll all = new ForAll();
		
		@LangKey("config.category.aerite_ore")
		@RequiresWorldRestart
		public ForAeriteOre aerite = new ForAeriteOre();
		
		@LangKey("config.category.drenite_ore")
		@RequiresWorldRestart
		public ForDreniteOre drenite = new ForDreniteOre();
		
		@LangKey("config.category.forcite_ore")
		@RequiresWorldRestart
		public ForForciteOre forcite = new ForForciteOre();
		
		@LangKey("config.category.flamite_ore")
		@RequiresWorldRestart
		public ForFlamiteOre flamite = new ForFlamiteOre();
		
		@LangKey("config.category.guardite_ore")
		@RequiresWorldRestart
		public ForGuarditeOre guardite = new ForGuarditeOre();
		
		@LangKey("config.category.landite_ore")
		@RequiresWorldRestart
		public ForLanditeOre landite = new ForLanditeOre();
		
		@LangKey("config.category.marlite_ore")
		@RequiresWorldRestart
		public ForMarliteOre marlite = new ForMarliteOre();
		
		@LangKey("config.category.sanitite_ore")
		@RequiresWorldRestart
		public ForSanititeOre sanitite = new ForSanititeOre();
		
		@LangKey("config.category.coal_ore")
		@RequiresWorldRestart
		public ForCoalOre coal = new ForCoalOre();
		
		@LangKey("config.category.diamond_ore")
		@RequiresWorldRestart
		public ForDiamondOre diamond = new ForDiamondOre();
		
		@LangKey("config.category.emerald_ore")
		@RequiresWorldRestart
		public ForEmeraldOre emerald = new ForEmeraldOre();
		
		@LangKey("config.category.gold_ore")
		@RequiresWorldRestart
		public ForGoldOre gold = new ForGoldOre();
		
		@LangKey("config.category.iron_ore")
		@RequiresWorldRestart
		public ForIronOre iron = new ForIronOre();
		
		@LangKey("config.category.lapis_ore")
		@RequiresWorldRestart
		public ForLapisOre lapis = new ForLapisOre();
		
		@LangKey("config.category.quartz_ore")
		@RequiresWorldRestart
		public ForQuartzOre quartz = new ForQuartzOre();
		
		@LangKey("config.category.redstone_ore")
		@RequiresWorldRestart
		public ForRedstoneOre redstone = new ForRedstoneOre();
		
		public class ForAll
		{
			@LangKey("config.ore_gen.air_min_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int air_min_height = 0;
			
			@LangKey("config.ore_gen.air_max_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int air_max_height = 127;
			
			@LangKey("config.ore_gen.earth_min_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int earth_min_height = 0;
			
			@LangKey("config.ore_gen.earth_max_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int earth_max_height = 255;
			
			@LangKey("config.ore_gen.fire_min_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int fire_min_height = 0;
			
			@LangKey("config.ore_gen.fire_max_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int fire_max_height = 127;
			
			@LangKey("config.ore_gen.water_max_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int water_min_height = 0;
			
			@LangKey("config.ore_gen.water_max_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int water_max_height = 255;	
		}
		
		public class ForAeriteOre
		{
			@LangKey("config.ore_gen.size")
			@RangeInt(min=3,max=50)
			@SlidingOption
			public int size = 5;
			
			@LangKey("config.ore_gen.tries")
			@RangeInt(min=0,max=40)
			@SlidingOption
			public int tries = 9;
			
			@LangKey("config.ore_gen.center_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int min_height = 0;
			
			@LangKey("config.ore_gen.range")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int max_height = 255;
		}
		
		public class ForDreniteOre
		{
			@LangKey("config.ore_gen.size")
			@RangeInt(min=3,max=50)
			@SlidingOption
			public int size = 5;
			
			@LangKey("config.ore_gen.tries")
			@RangeInt(min=0,max=40)
			@SlidingOption
			public int tries = 9;
			
			@LangKey("config.ore_gen.center_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int min_height = 0;
			
			@LangKey("config.ore_gen.range")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int max_height = 255;
		}
		
		public class ForForciteOre
		{
			@LangKey("config.ore_gen.size")
			@RangeInt(min=3,max=50)
			@SlidingOption
			public int size = 5;
			
			@LangKey("config.ore_gen.tries")
			@RangeInt(min=0,max=40)
			@SlidingOption
			public int tries = 6;
			
			@LangKey("config.ore_gen.center_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int min_height = 0;
			
			@LangKey("config.ore_gen.range")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int max_height = 255;
		}
		
		public class ForFlamiteOre
		{
			@LangKey("config.ore_gen.size")
			@RangeInt(min=3,max=50)
			@SlidingOption
			public int size = 5;
			
			@LangKey("config.ore_gen.tries")
			@RangeInt(min=0,max=40)
			@SlidingOption
			public int tries = 6;
			
			@LangKey("config.ore_gen.center_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int min_height = 0;
			
			@LangKey("config.ore_gen.range")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int max_height = 255;
		}
		
		public class ForGuarditeOre
		{
			@LangKey("config.ore_gen.size")
			@RangeInt(min=3,max=50)
			@SlidingOption
			public int size = 5;
			
			@LangKey("config.ore_gen.tries")
			@RangeInt(min=0,max=40)
			@SlidingOption
			public int tries = 33;
			
			@LangKey("config.ore_gen.center_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int min_height = 0;
			
			@LangKey("config.ore_gen.range")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int max_height = 255;
		}
		
		public class ForLanditeOre
		{
			@LangKey("config.ore_gen.size")
			@RangeInt(min=3,max=50)
			@SlidingOption
			public int size = 5;
			
			@LangKey("config.ore_gen.tries")
			@RangeInt(min=0,max=40)
			@SlidingOption
			public int tries = 33;
			
			@LangKey("config.ore_gen.center_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int min_height = 0;
			
			@LangKey("config.ore_gen.range")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int max_height = 255;
		}
		
		public class ForMarliteOre
		{
			@LangKey("config.ore_gen.size")
			@RangeInt(min=3,max=50)
			@SlidingOption
			public int size = 5;
			
			@LangKey("config.ore_gen.tries")
			@RangeInt(min=0,max=40)
			@SlidingOption
			public int tries = 15;
			
			@LangKey("config.ore_gen.center_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int min_height = 0;
			
			@LangKey("config.ore_gen.range")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int max_height = 255;
		}
		
		public class ForSanititeOre
		{
			@LangKey("config.ore_gen.size")
			@RangeInt(min=3,max=50)
			@SlidingOption
			public int size = 5;
			
			@LangKey("config.ore_gen.tries")
			@RangeInt(min=0,max=40)
			@SlidingOption
			public int tries = 15;
			
			@LangKey("config.ore_gen.center_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int min_height = 0;
			
			@LangKey("config.ore_gen.range")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int max_height = 255;
		}
		
		public class ForCoalOre
		{
			@LangKey("config.ore_gen.size")
			@RangeInt(min=3,max=50)
			@SlidingOption
			public int size = 17;
			
			@LangKey("config.ore_gen.tries")
			@RangeInt(min=0,max=40)
			@SlidingOption
			public int tries = 20;
			
			@LangKey("config.ore_gen.center_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int min_height = 0;
			
			@LangKey("config.ore_gen.range")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int max_height = 255;
		}
		
		public class ForDiamondOre
		{
			@LangKey("config.ore_gen.size")
			@RangeInt(min=3,max=50)
			@SlidingOption
			public int size = 8;
			
			@LangKey("config.ore_gen.tries")
			@RangeInt(min=0,max=40)
			@SlidingOption
			public int tries = 1;
			
			@LangKey("config.ore_gen.center_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int min_height = 0;
			
			@LangKey("config.ore_gen.range")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int max_height = 255;
		}
		
		public class ForEmeraldOre
		{
			@LangKey("config.ore_gen.size")
			@RangeInt(min=3,max=50)
			@SlidingOption
			public int size = 3;
			
			@LangKey("config.ore_gen.tries")
			@RangeInt(min=0,max=40)
			@SlidingOption
			public int tries = 12;
			
			@LangKey("config.ore_gen.center_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int min_height = 0;
			
			@LangKey("config.ore_gen.range")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int max_height = 255;
		}
		
		public class ForGoldOre
		{
			@LangKey("config.ore_gen.size")
			@RangeInt(min=3,max=50)
			@SlidingOption
			public int size = 9;
			
			@LangKey("config.ore_gen.tries")
			@RangeInt(min=0,max=40)
			@SlidingOption
			public int tries = 2;
			
			@LangKey("config.ore_gen.center_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int min_height = 0;
			
			@LangKey("config.ore_gen.range")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int max_height = 255;
		}
		
		public class ForIronOre
		{
			@LangKey("config.ore_gen.size")
			@RangeInt(min=3,max=50)
			@SlidingOption
			public int size = 9;
			
			@LangKey("config.ore_gen.tries")
			@RangeInt(min=0,max=40)
			@SlidingOption
			public int tries = 20;
			
			@LangKey("config.ore_gen.center_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int min_height = 0;
			
			@LangKey("config.ore_gen.range")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int max_height = 255;
		}
		
		public class ForLapisOre
		{
			@LangKey("config.ore_gen.size")
			@RangeInt(min=3,max=50)
			@SlidingOption
			public int size = 7;
			
			@LangKey("config.ore_gen.tries")
			@RangeInt(min=0,max=40)
			@SlidingOption
			public int tries = 2;
			
			@LangKey("config.ore_gen.center_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int min_height = 0;
			
			@LangKey("config.ore_gen.range")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int max_height = 255;
		}
		
		public class ForQuartzOre
		{
			@LangKey("config.ore_gen.size")
			@RangeInt(min=3,max=50)
			@SlidingOption
			public int size = 14;
			
			@LangKey("config.ore_gen.tries")
			@RangeInt(min=0,max=40)
			@SlidingOption
			public int tries = 12;
			
			@LangKey("config.ore_gen.center_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int min_height = 0;
			
			@LangKey("config.ore_gen.range")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int max_height = 255;
		}
		
		public class ForRedstoneOre
		{
			@LangKey("config.ore_gen.size")
			@RangeInt(min=3,max=50)
			@SlidingOption
			public int size = 8;
			
			@LangKey("config.ore_gen.tries")
			@RangeInt(min=0,max=40)
			@SlidingOption
			public int tries = 8;
			
			@LangKey("config.ore_gen.center_height")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int min_height = 0;
			
			@LangKey("config.ore_gen.range")
			@RangeInt(min=0,max=255)
			@SlidingOption
			public int max_height = 255;
		}
	}
	
	@LangKey("config.category.magic")
	public static Magic magic = new Magic();
	
	public static class Magic {
		@LangKey("config.magic.repeatable")
		@RequiresMcRestart
		public boolean repeatable = false;
		
		@LangKey("config.magic.init_magic_point")
		@RequiresMcRestart
		public int init_magic_point = 10;
		
		@LangKey("config.magic.init_max_mana")
		@RequiresMcRestart
		public int init_max_mana = 100;
		
		@LangKey("config.magic.init_mana")
		@RequiresMcRestart
		public int init_mana = 50;
		
		@LangKey("config.magic.init_mxp")
		@RequiresMcRestart
		public int init_mxp = 0;
		
		@LangKey("config.magic.init_level")
		@RequiresMcRestart
		public int init_level = 1;
	}
	
	@LangKey("config.category.miscellaneous")
	public static Misc miscellaneous = new Misc();
	
	public static class Misc
	{		
		@LangKey("config.misc.mkt.back")
		@RequiresWorldRestart
		public String mkt_back = "neo_ores:textures/blocks/michastone.png";
	}
}
