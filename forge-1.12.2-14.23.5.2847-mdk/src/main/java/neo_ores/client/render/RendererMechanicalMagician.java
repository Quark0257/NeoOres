package neo_ores.client.render;

import neo_ores.client.model.ModelMechanicalMagician;
import neo_ores.tileentity.TileEntityMechanicalMagician;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RendererMechanicalMagician extends TileEntitySpecialRenderer<TileEntityMechanicalMagician>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation("neo_ores:textures/entity/mechanical_magician.png");
	private final ModelMechanicalMagician mmm = new ModelMechanicalMagician();

	public void render(TileEntityMechanicalMagician te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F); // radius 1.5
		this.bindTexture(TEXTURE);

		GlStateManager.enableCull();
		float angleY = (te.getDirection().y - 90) * (float) Math.PI / 180.0F;
		float angleX = (-te.getDirection().x) * (float) Math.PI / 180.0F;
		this.mmm.render(0.0F, 0.0F, 0.0F, angleY, angleX, 0.0F, 0.0725F);
		GlStateManager.popMatrix();
	}
}
