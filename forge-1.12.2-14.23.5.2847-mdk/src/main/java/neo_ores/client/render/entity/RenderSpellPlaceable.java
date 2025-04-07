package neo_ores.client.render.entity;

import neo_ores.client.particle.ParticleMagic;
import neo_ores.entity.EntitySpellPlaceable;
import neo_ores.event.NeoOresRegisterEvents;
import neo_ores.util.SpellUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderSpellPlaceable extends Render<EntitySpellPlaceable>
{
	private RenderItem render;

	protected RenderSpellPlaceable(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
		this.render = Minecraft.getMinecraft().getRenderItem();
	}

	public void doRender(EntitySpellPlaceable entity, double x, double y, double z, float entityYaw, float partialTicks)
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

		this.render.renderItem(ItemStack.EMPTY, ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);

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

		AxisAlignedBB aabb = entity.getEntityBoundingBox();
		Vec3d min = new Vec3d(aabb.minX, aabb.minY, aabb.minZ);
		Vec3d max = new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ);
		Vec3d size = max.subtract(min);
		int color = SpellUtils.getColor(entity.getStack());
		double py = min.y + 0.5 * size.y;
		double multiplier = 2.0D;
		double dMulti = 1.0 / multiplier;
		int maxX = (int) (size.x * multiplier);
		int maxZ = (int) (size.z * multiplier);
		for (int i = 0; i < maxX; i++)
		{
			for (int j = 0; j < maxZ; j++)
			{
				double baseX = (double) i / multiplier;
				double baseZ = (double) j / multiplier;
				double dx = baseX + dMulti * (entity.world.rand.nextDouble());
				double dy = 0.5 * size.y * (entity.world.rand.nextDouble() - 0.5D);
				double dz = baseZ + dMulti * (entity.world.rand.nextDouble());
				ParticleMagic png = new ParticleMagic(entity.world, min.x + dx, py + dy, min.z + dz, 0.0, 0.0, 0.0, color, 12 + entity.world.rand.nextInt(8),
						0.3F + 0.02f * entity.world.rand.nextFloat(), NeoOresRegisterEvents.particle0);
				Minecraft.getMinecraft().effectRenderer.addEffect(png);
			}
		}
	}

	protected ResourceLocation getEntityTexture(EntitySpellPlaceable entity)
	{
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

	public static class RenderSpellPlaceableFactory implements IRenderFactory<EntitySpellPlaceable>
	{
		@Override
		public Render<? super EntitySpellPlaceable> createRenderFor(RenderManager manager)
		{
			return new RenderSpellPlaceable(manager);
		}
	}
}
