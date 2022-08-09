package neo_ores.client.render.entity;

import neo_ores.entity.EntitySpellBullet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderSpellBullet  extends Render<EntitySpellBullet> 
{
	private RenderItem render;
	
	protected RenderSpellBullet(RenderManager renderManagerIn) 
	{
		super(renderManagerIn);
		this.render = Minecraft.getMinecraft().getRenderItem();
	}
	
	public void doRender(EntitySpellBullet entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        this.render.renderItem(entity.getStack(), ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);

        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntitySpellBullet entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

	public static class RenderSpellBulletFactory implements IRenderFactory<EntitySpellBullet>
	{
		
		@Override
		public Render<? super EntitySpellBullet> createRenderFor(RenderManager manager) 
		{
			return new RenderSpellBullet(manager);
		}
	}
}
