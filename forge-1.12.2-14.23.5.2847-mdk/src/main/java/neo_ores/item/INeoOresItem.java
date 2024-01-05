package neo_ores.item;

import neo_ores.main.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public interface INeoOresItem
{
	public default ModelResourceLocation getModel(Item item, int meta)
	{
		return new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, item.getRegistryName().getResourcePath()), "inventory");
	}

	public default int getMaxMeta()
	{
		return 0;
	}

	public static class Impl extends Item implements INeoOresItem
	{
	}
}
