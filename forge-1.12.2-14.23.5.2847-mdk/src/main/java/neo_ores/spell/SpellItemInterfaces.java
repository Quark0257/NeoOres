package neo_ores.spell;

public class SpellItemInterfaces
{
	public static interface ICorrectingBase
	{
	}

	public static interface HasRange extends ICorrectingBase
	{
		public void setRange(int value);
	}

	public static interface HasSpeed extends ICorrectingBase
	{
		public void setSpeed(int value);
	}

	public static interface HasLuck extends ICorrectingBase
	{
		public void setLuck(int value);
	}

	public static interface HasHarvestLevel extends ICorrectingBase
	{
		public void setHarvestLevel(int value);
	}

	public static interface HasHealLevel extends ICorrectingBase
	{
		public void setHealLevel(int value);
	}

	public static interface HasDamageLevel extends ICorrectingBase
	{
		public void setDamageLevel(int value);
	}

	public static interface HasChanceLiquid extends ICorrectingBase
	{
		public void setSupport();
	}

	public static interface HasSilk extends ICorrectingBase
	{
		public void setSilkTouch();
	}

	public static interface HasGather extends ICorrectingBase
	{
		public void setCanGather();
	}

	public static interface HasTier extends ICorrectingBase
	{
		public void setTier(int value);
	}

	public static interface HasNoGravity extends ICorrectingBase
	{
		public void setNoGravity();
	}

	public static interface HasNoAnyResistance extends ICorrectingBase
	{
		public void setNoAnyResistance();
	}

	public static interface HasContinuation extends ICorrectingBase
	{
		public void setContinuation(int value);
	}

	public static interface HasCanApplyNBT extends ICorrectingBase
	{
		public void setCanApplyNBT();
	}

	public static interface HasNoInertia extends ICorrectingBase
	{
		public void setNoInertia();
	}

	public static interface HasCollidableFilter extends ICorrectingBase
	{
		public void setCollidableFilter();
	}
}