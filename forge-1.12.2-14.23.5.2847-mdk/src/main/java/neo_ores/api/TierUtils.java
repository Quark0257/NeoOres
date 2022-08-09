package neo_ores.api;

import javax.annotation.Nullable;

import neo_ores.item.IItemNeoTool;
import net.minecraft.item.ItemStack;

public class TierUtils 
{
	public final ItemStack stack;
	
	public TierUtils(ItemStack target)
	{
		stack = target;
	}
	
	@Nullable
	private IItemNeoTool get()
	{
		if(stack.getItem() instanceof IItemNeoTool)
		{
			return (IItemNeoTool)stack.getItem();
		}
		return null;
	}
	
	public int[] getTier()
	{
		if(get() == null)
		{
			return new int[] {0,0,0,0};
		}
		
		return get().getTierList(stack);
	}
	
	public void setTier(int air,int earth,int fire,int water)
	{
		if(get() != null)
		{
			get().setTierList(stack, air,earth,fire,water);
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
	
	public void setAir(int air)
	{
		setTier(air,getEarth(),getFire(),getWater());
	}
	
	public void setEarth(int earth)
	{
		setTier(getAir(),earth,getFire(),getWater());
	}
	
	public void setFire(int fire)
	{
		setTier(getAir(),getEarth(),fire,getWater());
	}
	
	public void setWater(int water)
	{
		setTier(getAir(),getEarth(),getFire(),water);
	}
}
