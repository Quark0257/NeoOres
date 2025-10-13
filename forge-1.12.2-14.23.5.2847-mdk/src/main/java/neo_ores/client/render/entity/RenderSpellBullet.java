package neo_ores.client.render.entity;

import neo_ores.client.particle.TexturedParticle;
import neo_ores.entity.EntitySpellBullet;
import neo_ores.event.NeoOresClientEvents;
import neo_ores.main.NeoOres;
import neo_ores.util.SpellUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderSpellBullet extends Render<EntitySpellBullet>
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
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((float) (this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
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
		
		if (entity.getStack().isEmpty())
		{
			return;
		}
		
		for (int k = 0; k < 4; ++k)
		{
			double px = entity.posX - entity.motionX * ((double) k + 0.7) / 4.0D;
			double py = entity.posY - entity.motionY * ((double) k + 0.7) / 4.0D;
			double pz = entity.posZ - entity.motionZ * ((double) k + 0.7) / 4.0D;
			double dx = 0.0;
			double dy = 0.0;
			double dz = 0.0;
			final double m = 0.3;
			for (int i = 0; i < 4; i++)
			{
				dx = m * entity.world.rand.nextDouble() - 0.5 * m;
				dy = m * entity.world.rand.nextDouble() - 0.5 * m;
				dz = m * entity.world.rand.nextDouble() - 0.5 * m;

				NeoOresClientEvents.getInstance().addParticle(new TexturedParticle(px + dx, py + dy, pz + dz, 0.0, 0.0, 0.0, 
						Math.min(6 + entity.world.rand.nextInt(4), Math.max(0, entity.life - 1)), 1.0F, NeoOres.PARTICLE_MAGIC)
						.setColor(SpellUtils.getColor(entity.getStack()), 1.0F));
			}
		}
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
