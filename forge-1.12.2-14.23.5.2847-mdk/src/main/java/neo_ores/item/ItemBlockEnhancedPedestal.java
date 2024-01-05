package neo_ores.item;

import neo_ores.block.BlockEnhancedPedestal;
import neo_ores.block.properties.PedestalTiers;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockEnhancedPedestal extends ItemBlock
{
	public ItemBlockEnhancedPedestal(Block block)
	{
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	public int getMetadata(int damage)
	{
		return damage;
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return ((BlockEnhancedPedestal) this.getBlock()).getUnlocalizedName(stack);
	}

	public String getItemStackDisplayName(ItemStack stack)
	{
		int meta = PedestalTiers.getFromMeta(stack.getItemDamage()).getMeta();
		return I18n.format("hoppered." + (meta / 8)) + I18n.format(this.getUnlocalizedName(stack) + ".name").trim() + I18n.format("size.") + (meta % 8 + 1) + I18n.format(")");
	}
}
