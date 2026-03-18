package neo_ores.client.render;

import neo_ores.client.model.ModelManaBlock;
import neo_ores.tileentity.TileEntityManaBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RendererManaBlock extends TileEntitySpecialRenderer<TileEntityManaBlock>
{
	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("minecraft:textures/misc/enchanted_item_glint.png");
	private static final ResourceLocation TEXTURE = new ResourceLocation("neo_ores:textures/entity/model_mana_block.png");
	private final ModelManaBlock mmb = new ModelManaBlock();
	private final ModelManaBlock mmb2 = new ModelManaBlock();
	private float scale = 0.0625F;

	public void render(TileEntityManaBlock te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		this.bindTexture(TEXTURE);
		GlStateManager.enableCull();
		this.mmb.render(scale);
		renderEffect(te, partialTicks);

		GlStateManager.popMatrix();
	}

	private void renderEffect(TileEntityManaBlock te, float partialTicks)
	{
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		GlStateManager.enableFog();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
		this.bindTexture(RES_ITEM_GLINT);
		GlStateManager.disableLighting();
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.pushMatrix();
		float texScale = 0.5F;
		GlStateManager.scale(texScale, texScale, texScale);
		float f = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / texScale;
		GlStateManager.translate(f, 0.0F, 0.0F);
		GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.color(0.00390625F * 128F, 0.00390625F * 64F, 0.00390625F * 204F);
		this.mmb2.textureHeight = 64;
		this.mmb2.render(scale);

		GlStateManager.matrixMode(5890);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.pushMatrix();
		GlStateManager.scale(texScale, texScale, texScale);
		float f1 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / texScale;
		GlStateManager.translate(-f1, 0.0F, 0.0F);
		GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.color(0.00390625F * 128F, 0.00390625F * 64F, 0.00390625F * 204F);
		this.mmb2.textureHeight = 64;
		this.mmb2.render(scale);

		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();
		GlStateManager.matrixMode(5890);

		GlStateManager.popMatrix();

		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}
}
