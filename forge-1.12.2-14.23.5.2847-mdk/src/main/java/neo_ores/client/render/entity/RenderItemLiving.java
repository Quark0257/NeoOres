package neo_ores.client.render.entity;

import neo_ores.api.IFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

public abstract class RenderItemLiving<T extends EntityLiving> extends RenderLiving<T>
{	
	public RenderItemLiving(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn)
	{
		super(rendermanagerIn, modelbaseIn, shadowsizeIn);
	}

	public void renderItemWithGlFunc(T parentEntity, ItemStack stack, boolean allowBlendSeparate, IFunction<Void> glFunc)
	{
		RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		this.renderManager.renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);

		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		RenderHelper.enableStandardItemLighting();
		if (allowBlendSeparate)
		{
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		}
		else
		{
			GlStateManager.blendFunc(SourceFactor.ONE, DestFactor.ZERO);
		}
		GlStateManager.pushMatrix();
		IBakedModel ibakedmodel = itemRender.getItemModelWithOverrides(stack, parentEntity.world, (EntityLivingBase) null);
		glFunc.function(null);
		boolean flag1 = ibakedmodel.isGui3d();

		if (this.renderOutlines)
		{
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(parentEntity));
		}

		if (flag1)
		{
			GlStateManager.pushMatrix();

			IBakedModel transformedModel = ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
			itemRender.renderItem(stack, transformedModel);
			GlStateManager.popMatrix();
		}
		else
		{
			GlStateManager.pushMatrix();

			IBakedModel transformedModel = ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
			itemRender.renderItem(stack, transformedModel);
			GlStateManager.popMatrix();
			GlStateManager.translate(0.0F, 0.0F, 0.09375F);
		}

		if (this.renderOutlines)
		{
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		this.renderManager.renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
	}
}
