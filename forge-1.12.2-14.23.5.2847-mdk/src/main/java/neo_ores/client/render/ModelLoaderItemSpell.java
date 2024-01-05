package neo_ores.client.render;

import java.io.IOException;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class ModelLoaderItemSpell implements ICustomModelLoader
{
	private final ModelResourceLocation inInv;
	private final ModelResourceLocation outInv;
	private final ModelResourceLocation key;

	public ModelLoaderItemSpell(ModelResourceLocation inInv, ModelResourceLocation outInv, ModelResourceLocation key)
	{
		this.key = key;
		this.inInv = inInv;
		this.outInv = outInv;
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return (new ResourceLocation(key.getResourceDomain(), "models/item/" + key.getResourcePath())).toString()
				.equals(modelLocation.toString());
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws IOException
	{
		return new ModelItemSpell(inInv, outInv);
	}

	public void registerModel(Item item, int meta)
	{
		ModelLoaderRegistry.registerLoader(new ModelLoaderItemSpell(inInv, outInv, key));
		ModelLoader.setCustomModelResourceLocation(item, meta, key);
		ModelBakery.registerItemVariants(item, inInv, outInv);
	}
}
