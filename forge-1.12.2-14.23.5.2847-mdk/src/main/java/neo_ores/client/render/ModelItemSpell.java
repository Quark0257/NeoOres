package neo_ores.client.render;

import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelItemSpell implements IModel
{
	private final ModelResourceLocation inInv;
	private final ModelResourceLocation outInv;

	public ModelItemSpell(ModelResourceLocation inInv, ModelResourceLocation outInv)
	{
		this.inInv = inInv;
		this.outInv = outInv;
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		return new RendererItemSpell(this.inInv, this.outInv);
	}

	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return ImmutableList.of();
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return ImmutableList.of();
	}

	@Override
	public IModelState getDefaultState()
	{
		return TRSRTransformation.identity();
	}
}
