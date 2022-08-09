package neo_ores.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TierCalc 
{
    public TierCalc() {}
	
	public int getWaterTier(ItemStack itemStack)
	{
		if(itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("tiers", 9))
		{
			NBTTagList NBTList = itemStack.getTagCompound().getTagList("tiers", 10);
			if(NBTList != null && NBTList.getCompoundTagAt(0) != null)
			{
				NBTTagCompound nbt = NBTList.getCompoundTagAt(0);
				if(nbt.hasKey("air") && nbt.hasKey("earth") && nbt.hasKey("fire") && nbt.hasKey("water"))
				{
					return nbt.getInteger("water");
				}
			}
		}
		return 0;
	}
	
	public void setWaterTier(ItemStack itemStack, int value)
	{
		if(itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("tiers", 9))
		{
			NBTTagList NBTList = itemStack.getTagCompound().getTagList("tiers", 10);
			if(NBTList != null && NBTList.getCompoundTagAt(0) != null)
			{
				NBTTagCompound nbt = NBTList.getCompoundTagAt(0);
				if(nbt.hasKey("air") && nbt.hasKey("earth") && nbt.hasKey("fire") && nbt.hasKey("water"))
				{
					int air = nbt.getInteger("air");
					int earth = nbt.getInteger("earth");
					int fire = nbt.getInteger("fire");
					int water = nbt.getInteger("water");
					
					water = value;
					
					NBTTagCompound itemNBT = new NBTTagCompound();
					itemNBT.setInteger("air", air);
					itemNBT.setInteger("earth", earth);
					itemNBT.setInteger("fire", fire);
					itemNBT.setInteger("water", water);
					NBTList.set(0, itemNBT);
				}
			}
		}
		else
		{
			if(itemStack.getTagCompound() == null)
			{
				itemStack.setTagCompound(new NBTTagCompound());
			}

			if(!itemStack.getTagCompound().hasKey("tiers", 9))
			{
				itemStack.getTagCompound().setTag("tiers", new NBTTagList());
			}
			NBTTagList NBTList = itemStack.getTagCompound().getTagList("tiers", 10);
			if(NBTList != null && NBTList.getCompoundTagAt(0) != null)
			{
				NBTTagCompound nbt = NBTList.getCompoundTagAt(0);
				if(nbt.hasKey("air") && nbt.hasKey("earth") && nbt.hasKey("fire") && nbt.hasKey("water"))
				{
					return;
				}
				else
				{				
					NBTTagCompound itemNBT = new NBTTagCompound();
					itemNBT.setInteger("air", 0);
					itemNBT.setInteger("earth", 0);
					itemNBT.setInteger("fire", 0);
					itemNBT.setInteger("water", value);

					NBTList.appendTag(itemNBT);
				}
			}
		}
	}
	
	public int getFireTier(ItemStack itemStack)
	{
		if(itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("tiers", 9))
		{
			NBTTagList NBTList = itemStack.getTagCompound().getTagList("tiers", 10);
			if(NBTList != null && NBTList.getCompoundTagAt(0) != null)
			{
				NBTTagCompound nbt = NBTList.getCompoundTagAt(0);
				if(nbt.hasKey("air") && nbt.hasKey("earth") && nbt.hasKey("fire") && nbt.hasKey("water"))
				{
					return nbt.getInteger("fire");
				}
			}
		}
		return 0;
	}
	
	public void setFireTier(ItemStack itemStack, int value)
	{
		if(itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("tiers", 9))
		{
			NBTTagList NBTList = itemStack.getTagCompound().getTagList("tiers", 10);
			if(NBTList != null && NBTList.getCompoundTagAt(0) != null)
			{
				NBTTagCompound nbt = NBTList.getCompoundTagAt(0);
				if(nbt.hasKey("air") && nbt.hasKey("earth") && nbt.hasKey("fire") && nbt.hasKey("water"))
				{
					int air = nbt.getInteger("air");
					int earth = nbt.getInteger("earth");
					int fire = nbt.getInteger("fire");
					int water = nbt.getInteger("water");
					
					fire = value;
					
					NBTTagCompound itemNBT = new NBTTagCompound();
					itemNBT.setInteger("air", air);
					itemNBT.setInteger("earth", earth);
					itemNBT.setInteger("fire", fire);
					itemNBT.setInteger("water", water);
					NBTList.set(0, itemNBT);
				}
			}
		}
		else
		{
			if(itemStack.getTagCompound() == null)
			{
				itemStack.setTagCompound(new NBTTagCompound());
			}

			if(!itemStack.getTagCompound().hasKey("tiers", 9))
			{
				itemStack.getTagCompound().setTag("tiers", new NBTTagList());
			}
			NBTTagList NBTList = itemStack.getTagCompound().getTagList("tiers", 10);
			if(NBTList != null && NBTList.getCompoundTagAt(0) != null)
			{
				NBTTagCompound nbt = NBTList.getCompoundTagAt(0);
				if(nbt.hasKey("air") && nbt.hasKey("earth") && nbt.hasKey("fire") && nbt.hasKey("water"))
				{
					return;
				}
				else
				{				
					NBTTagCompound itemNBT = new NBTTagCompound();
					itemNBT.setInteger("air", 0);
					itemNBT.setInteger("earth", 0);
					itemNBT.setInteger("fire", value);
					itemNBT.setInteger("water", 0);

					NBTList.appendTag(itemNBT);
				}
			}
		}
	}
	
	public int getEarthTier(ItemStack itemStack)
	{
		if(itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("tiers", 9))
		{
			NBTTagList NBTList = itemStack.getTagCompound().getTagList("tiers", 10);
			if(NBTList != null && NBTList.getCompoundTagAt(0) != null)
			{
				NBTTagCompound nbt = NBTList.getCompoundTagAt(0);
				if(nbt.hasKey("air") && nbt.hasKey("earth") && nbt.hasKey("fire") && nbt.hasKey("water"))
				{
					return nbt.getInteger("earth");
				}
			}
		}
		return 0;
	}
	
	public void setEarthTier(ItemStack itemStack, int value)
	{
		if(itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("tiers", 9))
		{
			NBTTagList NBTList = itemStack.getTagCompound().getTagList("tiers", 10);
			if(NBTList != null && NBTList.getCompoundTagAt(0) != null)
			{
				NBTTagCompound nbt = NBTList.getCompoundTagAt(0);
				if(nbt.hasKey("air") && nbt.hasKey("earth") && nbt.hasKey("fire") && nbt.hasKey("water"))
				{
					int air = nbt.getInteger("air");
					int earth = nbt.getInteger("earth");
					int fire = nbt.getInteger("fire");
					int water = nbt.getInteger("water");
					
					earth = value;
					
					NBTTagCompound itemNBT = new NBTTagCompound();
					itemNBT.setInteger("air", air);
					itemNBT.setInteger("earth", earth);
					itemNBT.setInteger("fire", fire);
					itemNBT.setInteger("water", water);
					NBTList.set(0, itemNBT);
				}
			}
		}
		else
		{
			if(itemStack.getTagCompound() == null)
			{
				itemStack.setTagCompound(new NBTTagCompound());
			}

			if(!itemStack.getTagCompound().hasKey("tiers", 9))
			{
				itemStack.getTagCompound().setTag("tiers", new NBTTagList());
			}
			NBTTagList NBTList = itemStack.getTagCompound().getTagList("tiers", 10);
			if(NBTList != null && NBTList.getCompoundTagAt(0) != null)
			{
				NBTTagCompound nbt = NBTList.getCompoundTagAt(0);
				if(nbt.hasKey("air") && nbt.hasKey("earth") && nbt.hasKey("fire") && nbt.hasKey("water"))
				{
					return;
				}
				else
				{				
					NBTTagCompound itemNBT = new NBTTagCompound();
					itemNBT.setInteger("air", 0);
					itemNBT.setInteger("earth",value);
					itemNBT.setInteger("fire", 0);
					itemNBT.setInteger("water", 0);

					NBTList.appendTag(itemNBT);
				}
			}
		}
	}
	
	public int getAirTier(ItemStack itemStack)
	{
		if(itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("tiers", 9))
		{
			NBTTagList NBTList = itemStack.getTagCompound().getTagList("tiers", 10);
			if(NBTList != null && NBTList.getCompoundTagAt(0) != null)
			{
				NBTTagCompound nbt = NBTList.getCompoundTagAt(0);
				if(nbt.hasKey("air") && nbt.hasKey("earth") && nbt.hasKey("fire") && nbt.hasKey("water"))
				{
					return nbt.getInteger("air");
				}
			}
		}
		return 0;
	}
	
	public void setAirTier(ItemStack itemStack, int value)
	{
		if(itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("tiers", 9))
		{
			NBTTagList NBTList = itemStack.getTagCompound().getTagList("tiers", 10);
			if(NBTList != null && NBTList.getCompoundTagAt(0) != null)
			{
				NBTTagCompound nbt = NBTList.getCompoundTagAt(0);
				if(nbt.hasKey("air") && nbt.hasKey("earth") && nbt.hasKey("fire") && nbt.hasKey("water"))
				{
					int air = nbt.getInteger("air");
					int earth = nbt.getInteger("earth");
					int fire = nbt.getInteger("fire");
					int water = nbt.getInteger("water");
					
					air = value;
					
					NBTTagCompound itemNBT = new NBTTagCompound();
					itemNBT.setInteger("air", air);
					itemNBT.setInteger("earth", earth);
					itemNBT.setInteger("fire", fire);
					itemNBT.setInteger("water", water);
					NBTList.set(0, itemNBT);
				}
			}
		}
		else
		{
			if(itemStack.getTagCompound() == null)
			{
				itemStack.setTagCompound(new NBTTagCompound());
			}

			if(!itemStack.getTagCompound().hasKey("tiers", 9))
			{
				itemStack.getTagCompound().setTag("tiers", new NBTTagList());
			}
			NBTTagList NBTList = itemStack.getTagCompound().getTagList("tiers", 10);
			if(NBTList != null && NBTList.getCompoundTagAt(0) != null)
			{
				NBTTagCompound nbt = NBTList.getCompoundTagAt(0);
				if(nbt.hasKey("air") && nbt.hasKey("earth") && nbt.hasKey("fire") && nbt.hasKey("water"))
				{
					return;
				}
				else
				{				
					NBTTagCompound itemNBT = new NBTTagCompound();
					itemNBT.setInteger("air", value);
					itemNBT.setInteger("earth", 0);
					itemNBT.setInteger("fire", 0);
					itemNBT.setInteger("water", 0);

					NBTList.appendTag(itemNBT);
				}
			}
		}
	}
}
