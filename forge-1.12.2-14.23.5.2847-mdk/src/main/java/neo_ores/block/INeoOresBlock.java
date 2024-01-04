package neo_ores.block;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface INeoOresBlock
{
	public ModelResourceLocation getModel(int meta);
	
	//0~15 available
	public int getMaxMeta();
	
	public Item getItemBlock(Block block);
	
	public String getUnlocalizedName(ItemStack stack);
	
	@SuppressWarnings("rawtypes")
	public default IProperty[] setNoRenderProperties() {
		return null;
	}
}
