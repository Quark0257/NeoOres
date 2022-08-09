package neo_ores.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public interface INeoOresBlock
{
	public ModelResourceLocation getModel(int meta);
	
	//0~15 available
	public int getMaxMeta();
	
	public Item getItemBlock(Block block);
}
