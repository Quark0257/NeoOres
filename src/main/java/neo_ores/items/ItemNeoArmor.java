package neo_ores.items;

import java.util.Arrays;
import java.util.List;

import neo_ores.main.NeoOres;
import neo_ores.mana.PlayerManaDataServer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class ItemNeoArmor extends ItemArmor
{

	public ItemNeoArmor(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) 
	{
		super(materialIn, renderIndexIn, equipmentSlotIn);
		if(materialIn == NeoOres.armorCreative)
		{
			this.setMaxDamage(Integer.MAX_VALUE);
		}
	}
	
	public void onArmorTick(World world,EntityPlayer player,ItemStack stack)
	{
		if(!world.isRemote && player.ticksExisted % 200 == 0)
		{
			PlayerManaDataServer pmds = new PlayerManaDataServer((EntityPlayerMP)player);
			if(pmds.getMana() > 0)
			{
				pmds.addMana(-1L);
				stack.damageItem(-1, player);
			}
		}
	}
	
	public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag)
	{
		super.addInformation(itemStack, world, list, flag);
		if(itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("tiers", 9))
		{
			NBTTagList NBTList = itemStack.getTagCompound().getTagList("tiers", 10);
			if(NBTList != null && NBTList.getCompoundTagAt(0) != null)
			{				
				NBTTagCompound nbt = NBTList.getCompoundTagAt(0);
				if(nbt.hasKey("air") && nbt.hasKey("earth") && nbt.hasKey("fire") && nbt.hasKey("water"))
				{
					boolean tipflag = false;
					
					if(nbt.getInteger("water") == 11 && nbt.getInteger("fire") == 11 && nbt.getInteger("earth") == 11 && nbt.getInteger("air") == 11)
					{
						for(int i = 0;i < list.size();i++)
						{
							List<String> spList = Arrays.asList(list.get(i).split(" "));
							for(int j = 0;j < spList.size();j++)
							{
								if(spList.get(j).equals(TextFormatting.DARK_PURPLE + I18n.translateToLocal("armortip.water").trim()))
								{
									list.remove(i);
								}
								
								if(spList.get(j).equals(TextFormatting.GOLD + I18n.translateToLocal("armortip.fire").trim()))
								{
									list.remove(i);
								}
								
								if(spList.get(j).equals(TextFormatting.GREEN + I18n.translateToLocal("armortip.earth").trim()))
								{
									list.remove(i);
								}
								
								if(spList.get(j).equals(TextFormatting.AQUA + I18n.translateToLocal("armortip.air").trim()))
								{
									list.remove(i);
								}
							}
						}
						
						list.add(TextFormatting.WHITE + I18n.translateToLocal("armortip.space").trim());
					}
					else
					{
						if(nbt.getInteger("water") > 0)
						{
							for(int i = 0;i < list.size();i++)
							{
								List<String> spList = Arrays.asList(list.get(i).split(" "));
								for(int j = 0;j < spList.size();j++)
								{
									if(spList.get(j).equals(TextFormatting.DARK_PURPLE + I18n.translateToLocal("armortip.water").trim()))
									{
										list.remove(i);
										list.add(i, TextFormatting.DARK_PURPLE + I18n.translateToLocal("armortip.water").trim() + " " + I18n.translateToLocal("tier." + nbt.getInteger("water")).trim());
										tipflag = true;
										break;
									}
									if(tipflag) break;
								}
							}			
							if(!tipflag) list.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("armortip.water").trim() + " " + I18n.translateToLocal("tier." + nbt.getInteger("water")).trim());
							else tipflag = false;
						}
						
						if(nbt.getInteger("fire") > 0)
						{
							for(int i = 0;i < list.size();i++)
							{
								List<String> spList = Arrays.asList(list.get(i).split(" "));
								for(int j = 0;j < spList.size();j++)
								{
									if(spList.get(j).equals(TextFormatting.GOLD + I18n.translateToLocal("armortip.fire").trim()))
									{
										list.remove(i);
										list.add(i, TextFormatting.GOLD + I18n.translateToLocal("armortip.fire").trim() + " " + I18n.translateToLocal("tier." + nbt.getInteger("fire")).trim());
										tipflag = true;
										break;
									}
									if(tipflag) break;
								}
							}			
							if(!tipflag) list.add(TextFormatting.GOLD + I18n.translateToLocal("armortip.fire").trim() + " " + I18n.translateToLocal("tier." + nbt.getInteger("fire")).trim());
							else tipflag = false;
						}
						
						if(nbt.getInteger("earth") > 0)
						{
							for(int i = 0;i < list.size();i++)
							{
								List<String> spList = Arrays.asList(list.get(i).split(" "));
								for(int j = 0;j < spList.size();j++)
								{
									if(spList.get(j).equals(TextFormatting.GREEN + I18n.translateToLocal("armortip.earth").trim()))
									{
										list.remove(i);
										list.add(i, TextFormatting.GREEN + I18n.translateToLocal("armortip.earth").trim() + " " + I18n.translateToLocal("tier." + nbt.getInteger("earth")).trim());
										tipflag = true;
										break;
									}
									if(tipflag) break;
								}
							}			
							if(!tipflag) list.add(TextFormatting.GREEN + I18n.translateToLocal("armortip.earth").trim() + " " + I18n.translateToLocal("tier." + nbt.getInteger("earth")).trim());
							else tipflag = false;
						}
						
						if(nbt.getInteger("air") > 0)
						{
							for(int i = 0;i < list.size();i++)
							{
								List<String> spList = Arrays.asList(list.get(i).split(" "));
								for(int j = 0;j < spList.size();j++)
								{
									if(spList.get(j).equals(TextFormatting.AQUA + I18n.translateToLocal("armortip.air").trim()))
									{
										list.remove(i);
										list.add(i, TextFormatting.AQUA + I18n.translateToLocal("armortip.air").trim() + " " + I18n.translateToLocal("tier." + nbt.getInteger("air")).trim());
										tipflag = true;
										break;
									}
									if(tipflag) break;
								}
							}			
							if(!tipflag) list.add(TextFormatting.AQUA + I18n.translateToLocal("armortip.air").trim() + " " + I18n.translateToLocal("tier." + nbt.getInteger("air")).trim());
						}
					}
				}
			}	
		}
	}
}
