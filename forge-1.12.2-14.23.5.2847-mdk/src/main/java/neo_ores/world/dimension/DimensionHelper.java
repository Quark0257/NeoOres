package neo_ores.world.dimension;

import java.util.Arrays;
import java.util.List;

import net.minecraft.util.IStringSerializable;

public class DimensionHelper 
{	
	public static final List<Double> colors = Arrays.asList(-0.33,0.0,0.33,0.67,1.0,1.33,1.67,2.0,2.33,2.67,3.0,3.33);
	
	public static enum DimensionName implements IStringSerializable
	{
		/*
		WATER("water",1,0x8700FF),
		EARTH("earth",0,0xB5FF00),
		AIR("air",2,0x00FFCE),
		FIRE("fire",3,0xFF5200);
		*/
		WATER("water",1,0x8700FF,1),
		EARTH("earth",0,0x78FF00,3),
		AIR("air",2,0x00FFBE,0),
		FIRE("fire",3,0xFF5000,2);
		
		private String name;
		private int meta;
		private int color;
		private int colorIndex;
		
		private DimensionName(String name,int id, int color, int colorIndex)
		{
			this.name = name;
			this.meta = id;
			this.color = color;
			this.colorIndex = colorIndex;
		}
		
		public int getMeta()
		{
			return this.meta;
		}
		
		public int getColorIndex() {
			return this.colorIndex;
		}
		
		public int getColor() {
			return this.color;
		}
		
		public static DimensionName getFromMeta(int meta)
		{
			for(int i = 0;i < DimensionName.values().length;i++)
			{
				if(meta == DimensionName.values()[i].meta)
				{
					return DimensionName.values()[i];
				}
			}
			
			return DimensionName.EARTH;
		}
		
		public static DimensionName getFromColor(int colorIndex)
		{
			for(int i = 0;i < DimensionName.values().length;i++)
			{
				if(colorIndex == DimensionName.values()[i].colorIndex)
				{
					return DimensionName.values()[i];
				}
			}
			
			return DimensionName.EARTH;
		}
		
		@Override
		public String getName() 
		{
			return name;
		}
	}
	
	public static enum ToolType implements IStringSerializable
	{
		WATER("water",1),
		EARTH("earth",0),
		AIR("air",2),
		FIRE("fire",3),
		CREATIVE("creative",4);
		
		private String name;
		private int meta;
		
		private ToolType(String name,int id)
		{
			this.name = name;
			this.meta = id;
		}
		
		public int getMeta()
		{
			return this.meta;
		}
		
		public static DimensionName getFromMeta(int meta)
		{
			for(int i = 0;i < DimensionName.values().length;i++)
			{
				if(meta == DimensionName.values()[i].meta)
				{
					return DimensionName.values()[i];
				}
			}
			
			return DimensionName.WATER;
		}
		
		@Override
		public String getName() 
		{
			return name;
		}
	}
}
