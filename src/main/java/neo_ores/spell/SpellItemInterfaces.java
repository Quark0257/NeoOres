package neo_ores.spell;

public class SpellItemInterfaces 
{
	public static interface HasRange
	{
		public void setRange(int value);
	}
	
	public static interface HasSpeed
	{
		public void setSpeed(int value);
	}
	
	public static interface HasLuck
	{
		public void setLuck(int value);
	}
	
	public static interface HasHarvestLevel
	{
		public void setHavestLevel(int value);
	}
	
	public static interface HasHealLevel
	{
		public void setHealLevel(int value);
	}
	
	public static interface HasDamageLevel
	{
		public void setDamageLevel(int value);
	}
	
	public static interface HasChanceLiquid
	{		
		public void setSupport();
	}
	
	public static interface HasSilk
	{
		public void setSilkTouch();
	}
	
	public static interface HasGather
	{
		public void setCanGather();
	}
	
	public static interface HasTier
	{
		public void setTier(int value);
	}
}