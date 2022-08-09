package neo_ores.item;

import neo_ores.block.BlockEnhancedPedestal;
import neo_ores.block.properties.PedestalTiers;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class ItemBlockEnhancedPedestal extends ItemBlock {
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
        return ((BlockEnhancedPedestal)this.getBlock()).getUnlocalizedName(stack);
    }

	public String getItemStackDisplayName(ItemStack stack)
    {
    	int meta = PedestalTiers.getFromMeta(stack.getItemDamage()).getMeta();
        return I18n.translateToLocal(this.getUnlocalizedName(stack) + ".name").trim() + I18n.translateToLocal("size." ) + (meta % 8 + 1) + I18n.translateToLocal("hoppered." + (meta / 8));
    }
}
