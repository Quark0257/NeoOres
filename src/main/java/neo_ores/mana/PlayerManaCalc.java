package neo_ores.mana;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerManaCalc 
{
	public PlayerManaCalc() {}
	
	public int getMP(EntityPlayer player) 
	{
		if(!player.world.isRemote && player.hasItemInSlot(EntityEquipmentSlot.HEAD));
		{
			ItemStack headItem = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			if(headItem.getTagCompound() != null && headItem.getTagCompound().hasKey("neo_ores", 9))
			{
				NBTTagList headNBTList = headItem.getTagCompound().getTagList("neo_ores", 10);
				if(headNBTList != null && headNBTList.getCompoundTagAt(0) != null)
				{
					NBTTagCompound nbt = headNBTList.getCompoundTagAt(0);
					if(nbt.hasKey("ML") && nbt.hasKey("maxMP") && nbt.hasKey("MP") && nbt.hasKey("MXP") && nbt.hasKey("isSoulBound"))
					{
						return nbt.getInteger("MP");
					}
				}
			}
		}			
		return 0;
	}
	
	public void setMP(EntityPlayer player,int value)
	{
		if(!player.world.isRemote && player.hasItemInSlot(EntityEquipmentSlot.HEAD));
		{
			ItemStack headItem = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			if(headItem.getTagCompound() != null && headItem.getTagCompound().hasKey("neo_ores", 9))
			{
				NBTTagList headNBTList = headItem.getTagCompound().getTagList("neo_ores", 10);
				if(headNBTList != null && headNBTList.getCompoundTagAt(0) != null)
				{
					NBTTagCompound nbt = headNBTList.getCompoundTagAt(0);
					if(nbt.hasKey("ML") && nbt.hasKey("maxMP") && nbt.hasKey("MP") && nbt.hasKey("MXP") && nbt.hasKey("isSoulBound"))
					{
						int ML = nbt.getInteger("ML");
						int maxMP = nbt.getInteger("maxMP");
						int MP = nbt.getInteger("MP");
						int MXP = nbt.getInteger("MXP");
						
						MP = value;
						if(MP < 0)
						{
							MP = 0;
						}
						
						if(MP > maxMP)
						{
							MP = maxMP;
						}
						
						//setNBT
						NBTTagCompound itemNBT = new NBTTagCompound();
						itemNBT.setInteger("ML", ML);
						itemNBT.setInteger("maxMP", maxMP);
						itemNBT.setInteger("MP", MP);
						itemNBT.setInteger("MXP", MXP);
						itemNBT.setInteger("isSoulBound", nbt.getInteger("isSoulBound"));
						headNBTList.set(0, itemNBT);
					}
				}
			}
		}
	}
	public void addMP(EntityPlayer player, int value)
	{
		this.setMP(player, this.getMP(player) + value);
	}
	
	public int getMaxMP(EntityPlayer player) 
	{
		if(!player.world.isRemote && player.hasItemInSlot(EntityEquipmentSlot.HEAD));
		{
			ItemStack headItem = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			if(headItem.getTagCompound() != null && headItem.getTagCompound().hasKey("neo_ores", 9))
			{
				NBTTagList headNBTList = headItem.getTagCompound().getTagList("neo_ores", 10);
				if(headNBTList != null && headNBTList.getCompoundTagAt(0) != null)
				{
					NBTTagCompound nbt = headNBTList.getCompoundTagAt(0);
					if(nbt.hasKey("ML") && nbt.hasKey("maxMP") && nbt.hasKey("MP") && nbt.hasKey("MXP") && nbt.hasKey("isSoulBound"))
					{
						return nbt.getInteger("maxMP");
					}
				}
			}
		}			
		return 0;
	}
	
	public void setMaxMP(EntityPlayer player,int value)
	{
		if(!player.world.isRemote && player.hasItemInSlot(EntityEquipmentSlot.HEAD));
		{
			ItemStack headItem = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			if(headItem.getTagCompound() != null && headItem.getTagCompound().hasKey("neo_ores", 9))
			{
				NBTTagList headNBTList = headItem.getTagCompound().getTagList("neo_ores", 10);
				if(headNBTList != null && headNBTList.getCompoundTagAt(0) != null)
				{
					NBTTagCompound nbt = headNBTList.getCompoundTagAt(0);
					if(nbt.hasKey("ML") && nbt.hasKey("maxMP") && nbt.hasKey("MP") && nbt.hasKey("MXP") && nbt.hasKey("isSoulBound"))
					{
						int ML = nbt.getInteger("ML");
						int maxMP = nbt.getInteger("maxMP");
						int MP = nbt.getInteger("MP");
						int MXP = nbt.getInteger("MXP");
						
						maxMP = value;
						if(maxMP < 0)
						{
							maxMP = 0;
						}
						
						if(MP > maxMP)
						{
							MP = maxMP;
						}
						
						//setNBT
						NBTTagCompound itemNBT = new NBTTagCompound();
						itemNBT.setInteger("ML", ML);
						itemNBT.setInteger("maxMP", maxMP);
						itemNBT.setInteger("MP", MP);
						itemNBT.setInteger("MXP", MXP);
						itemNBT.setInteger("isSoulBound", nbt.getInteger("isSoulBound"));
						headNBTList.set(0, itemNBT);
					}
				}
			}
		}
	}
	public void addMaxMP(EntityPlayer player, int value)
	{
		this.setMaxMP(player, this.getMaxMP(player) + value);
	}
	
	public void addMXP(EntityPlayer player,int value)
	{
		if(!player.world.isRemote && player.hasItemInSlot(EntityEquipmentSlot.HEAD));
		{
			ItemStack headItem = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			if(headItem.getTagCompound() != null && headItem.getTagCompound().hasKey("neo_ores", 9))
			{
				NBTTagList headNBTList = headItem.getTagCompound().getTagList("neo_ores", 10);
				if(headNBTList != null && headNBTList.getCompoundTagAt(0) != null)
				{
					NBTTagCompound nbt = headNBTList.getCompoundTagAt(0);
					if(nbt.hasKey("ML") && nbt.hasKey("maxMP") && nbt.hasKey("MP") && nbt.hasKey("MXP") && nbt.hasKey("isSoulBound"))
					{
						int ML = nbt.getInteger("ML");
						int maxMP = nbt.getInteger("maxMP");
						int MP = nbt.getInteger("MP");
						int MXP = nbt.getInteger("MXP");
						
						MXP += value;
						if(MXP < 0)
						{
							MXP = 0;
						}
						
						//setNBT
						NBTTagCompound itemNBT = new NBTTagCompound();
						itemNBT.setInteger("ML", ML);
						itemNBT.setInteger("maxMP", maxMP);
						itemNBT.setInteger("MP", MP);
						itemNBT.setInteger("MXP", MXP);
						itemNBT.setInteger("isSoulBound", nbt.getInteger("isSoulBound"));
						headNBTList.set(0, itemNBT);
					}
				}
			}
		}
	}
}
