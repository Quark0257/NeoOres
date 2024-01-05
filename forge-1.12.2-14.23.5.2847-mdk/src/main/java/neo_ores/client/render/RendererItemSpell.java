package neo_ores.client.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.common.model.TRSRTransformation;

public class RendererItemSpell extends PerspectiveMapWrapper
{
	private final ModelResourceLocation inInv;
	private final ModelResourceLocation outInv;

	public RendererItemSpell(ModelResourceLocation inInventory, ModelResourceLocation in3d)
	{
		super(new RendererItemSpell.Model(), ImmutableMap.copyOf(new HashMap<TransformType, TRSRTransformation>()));
		inInv = inInventory;
		outInv = in3d;
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
	{
		ModelManager manager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager();

		IBakedModel model;
		if (cameraTransformType != TransformType.GUI && cameraTransformType != TransformType.GROUND
				&& cameraTransformType != TransformType.FIXED)
			model = manager.getModel(outInv);
		else
			model = manager.getModel(inInv);

		return Pair.of(model, TRSRTransformation.identity().getMatrix());
	}

	public static class Model implements IBakedModel
	{

		public Model()
		{
		}

		@Override
		public ItemCameraTransforms getItemCameraTransforms()
		{
			return ItemCameraTransforms.DEFAULT; // The requirement for this is a bug
		}

		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
		{
			return new ArrayList<BakedQuad>();
		}

		@Override
		public ItemOverrideList getOverrides()
		{
			return new ItemOverrideList(new ArrayList<ItemOverride>());
		}

		@Override
		public boolean isAmbientOcclusion()
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isGui3d()
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isBuiltInRenderer()
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public TextureAtlasSprite getParticleTexture()
		{
			// TODO Auto-generated method stub
			return null;
		}
	}
}
