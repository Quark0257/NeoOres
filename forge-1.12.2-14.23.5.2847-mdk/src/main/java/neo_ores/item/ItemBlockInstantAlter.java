package neo_ores.item;

import java.util.List;

import neo_ores.block.INeoOresBlock;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemBlockInstantAlter extends ItemBlockNeoOres
{
	public <T extends Block & INeoOresBlock> ItemBlockInstantAlter(T block)
	{
		super(block);
	}
	
	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag)
	{
		super.addInformation(stack, world, list, flag);
		list.add(TextFormatting.BLUE + I18n.format("creativeItem").trim());
		list.add(TextFormatting.BLUE + I18n.format("instant_alter.range").trim());
		list.add(TextFormatting.BLUE + I18n.format("instant_alter.tooltip").trim());
	}
}
