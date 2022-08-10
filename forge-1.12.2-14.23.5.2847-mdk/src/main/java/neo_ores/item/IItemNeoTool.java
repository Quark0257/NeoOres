package neo_ores.item;

import java.util.List;

import neo_ores.api.TierUtils;
import neo_ores.world.dimension.DimensionHelper.ToolType;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public interface IItemNeoTool
{
	public Item setToolType(ToolType name);
	
	public ToolType getToolType();
	
	//[air,earth,fire,water]
	public default int[] getTierList(ItemStack stack)
	{
		if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("tiers", 10))
		{
			return new int[] {0,0,0,0};
		}
		
		NBTTagCompound tiers = stack.getTagCompound().getCompoundTag("tiers");
		return new int[] {tiers.getInteger("air"),tiers.getInteger("earth"),tiers.getInteger("fire"),tiers.getInteger("water")};
	}
	
	//[air,earth,fire,water]
	public default void setTierList(ItemStack stack,int... data)
	{
		if(data.length != 4) return;
		
		if(!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		
		NBTTagCompound tiers = new NBTTagCompound();
		tiers.setInteger("air", data[0]);
		tiers.setInteger("earth", data[1]);
		tiers.setInteger("fire", data[2]);
		tiers.setInteger("water", data[3]);
		
		stack.getTagCompound().setTag("tiers", tiers);
	}
	
	public default void addTierInfo(ItemStack stack, World world, List<String> list, ITooltipFlag flag)
	{
		if(stack.getItem() instanceof IItemNeoTool)
		{
			TierUtils utils = new TierUtils(stack);
			if(utils.getAir() == 11 && utils.getEarth() == 11 && utils.getFire() == 11 && utils.getWater() == 11)
			{
				list.add(TextFormatting.WHITE + I18n.format("tooltip.space").trim());
				return;
			}

			if(utils.getAir() != 0)
			{
				list.add(TextFormatting.AQUA + I18n.format("tooltip.air").trim() + " " + I18n.format("tier." + utils.getAir()).trim());
			}
			
			if(utils.getEarth() != 0)
			{
				list.add(TextFormatting.GREEN + I18n.format("tooltip.earth").trim() + " " + I18n.format("tier." + utils.getEarth()).trim());
			}
			
			if(utils.getFire() != 0)
			{
				list.add(TextFormatting.GOLD + I18n.format("tooltip.fire").trim() + " " + I18n.format("tier." + utils.getFire()).trim());
			}
			
			if(utils.getWater() != 0)
			{
				list.add(TextFormatting.DARK_PURPLE + I18n.format("tooltip.water").trim() + " " + I18n.format("tier." + utils.getWater()).trim());
			}
		}
	}
}
