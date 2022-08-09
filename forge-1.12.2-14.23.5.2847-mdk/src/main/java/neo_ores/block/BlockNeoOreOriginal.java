package neo_ores.block;

import neo_ores.item.ItemBlockNeoOre;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class BlockNeoOreOriginal extends BlockNeoOre 
{
	public BlockNeoOreOriginal(String registername, int harvestLevel, DimensionName dimension, float light,
			boolean dropSelf, Item dropItem, int damage, int min, int max, int dropExpMin, int dropExpMax) {
		super(registername, harvestLevel, dimension, light, dropSelf, dropItem, damage, min, max, dropExpMin, dropExpMax);
	}
	
	public ModelResourceLocation getModel(int meta)
	{
		return new ModelResourceLocation(this.getRegistryName(), "inventory");
	}
	
	//0~15 available
	public int getMaxMeta()
	{
		return 0;
	}
	
	public Item getItemBlock(Block block)
	{
		return new ItemBlockNeoOre(block).setRegistryName(block.getRegistryName());
	}
}
