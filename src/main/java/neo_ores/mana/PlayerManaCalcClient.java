package neo_ores.mana;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerManaCalcClient 
{
	public PlayerManaCalcClient() {}
	
	public int getMP(EntityPlayer player) 
	{
		if(player.hasItemInSlot(EntityEquipmentSlot.HEAD));
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
	
	public int getMaxMP(EntityPlayer player) 
	{
		if(player.hasItemInSlot(EntityEquipmentSlot.HEAD));
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
	
	public int getMXP(EntityPlayer player)
	{
		if(player.hasItemInSlot(EntityEquipmentSlot.HEAD));
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
						return nbt.getInteger("MXP");
					}
				}
			}
		}
		return 0;
	}
	
	public int getML(EntityPlayer player) 
	{
		if(player.hasItemInSlot(EntityEquipmentSlot.HEAD));
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
						return nbt.getInteger("ML");
					}
				}
			}
		}			
		return 0;
	}
}
