package neo_ores.api;

import javax.annotation.Nullable;

import neo_ores.item.IItemNeoTool;
import neo_ores.world.dimension.DimensionHelper.ToolType;
import net.minecraft.item.ItemStack;

public class TierUtils
{
	public static final RingArray<ToolType> tiers = new RingArray<ToolType>(ToolType.AIR, ToolType.EARTH, ToolType.WATER, ToolType.FIRE);

	public final ItemStack stack;

	public TierUtils(ItemStack target)
	{
		stack = target;
	}

	@Nullable
	private IItemNeoTool get()
	{
		if (stack.getItem() instanceof IItemNeoTool)
		{
			return (IItemNeoTool) stack.getItem();
		}
		return null;
	}

	public int[] getTier()
	{
		if (get() == null)
		{
			return new int[] { 0, 0, 0, 0 };
		}

		return get().getTierList(stack);
	}

	public void setTier(int air, int earth, int fire, int water)
	{
		if (get() != null)
		{
			get().setTierList(stack, air, earth, fire, water);
		}
	}

	public int getAir()
	{
		return getTier()[0];
	}

	public int getEarth()
	{
		return getTier()[1];
	}

	public int getFire()
	{
		return getTier()[2];
	}

	public int getWater()
	{
		return getTier()[3];
	}

	public int get(ToolType type)
	{
		switch (type)
		{
			case AIR:
			{
				return getAir();
			}
			case EARTH:
			{
				return getEarth();
			}
			case FIRE:
			{
				return getFire();
			}
			case WATER:
			{
				return getWater();
			}
			default:
				return 0;
		}
	}

	public void set(ToolType type, int value)
	{
		switch (type)
		{
			case AIR:
			{
				setAir(value);
				return;
			}
			case EARTH:
			{
				setEarth(value);
				return;
			}
			case FIRE:
			{
				setFire(value);
				return;
			}
			case WATER:
			{
				setWater(value);
				return;
			}
			default:
				return;
		}
	}

	public void setAir(int air)
	{
		setTier(air, getEarth(), getFire(), getWater());
	}

	public void setEarth(int earth)
	{
		setTier(getAir(), earth, getFire(), getWater());
	}

	public void setFire(int fire)
	{
		setTier(getAir(), getEarth(), fire, getWater());
	}

	public void setWater(int water)
	{
		setTier(getAir(), getEarth(), getFire(), water);
	}
}
