package neo_ores.world.dimension;

import net.minecraft.util.IStringSerializable;

public class DimensionHelper 
{	
	public static enum DimensionName implements IStringSerializable
	{
		WATER("water",1),
		EARTH("earth",0),
		AIR("air",2),
		FIRE("fire",3);
		
		private String name;
		private int meta;
		
		private DimensionName(String name,int id)
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
