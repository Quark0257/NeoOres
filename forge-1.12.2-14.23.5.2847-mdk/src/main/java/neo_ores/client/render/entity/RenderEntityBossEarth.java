package neo_ores.client.render.entity;

import codechicken.lib.math.MathHelper;
import neo_ores.api.IFunction;
import neo_ores.entity.boss.EntityBossEarth;
import neo_ores.main.NeoOresItems;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderEntityBossEarth extends RenderItemLiving<EntityBossEarth>
{
	public RenderEntityBossEarth(RenderManager rendermanagerIn)
	{
		super(rendermanagerIn, new ModelBase()
		{
		}, 0.35F);
	}

	public void doRender(EntityBossEarth entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		ItemStack stack = new ItemStack(NeoOresItems.essence, 1, 0);
		float yaw = -this.interpolateRotation(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
		final boolean flag = entity.isTransparentView();
		this.renderItemWithGlFunc(entity, stack, flag, new IFunction<Void>()
		{
			@Override
			public Void function(Void t)
			{
				GlStateManager.translate(x, y + 1.2, z);
				GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(-15.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.scale(1.6, 1.6, 1.6);
				return null;
			}
		});
		float f = this.handleRotationFloat(entity, partialTicks) * (float) Math.PI * -0.1F;
		double r = 0.4;
		for (int i = 0; i < 6; i++)
		{
			double d = Math.sin(f / (6.0D + 0.1D * (i - 2.5)));
			final double dy = d * Math.cos(i * MathHelper.pi);
			final double theta = (double) i * MathHelper.pi / 3.0D;
			this.renderItemWithGlFunc(entity, stack, flag, new IFunction<Void>()
			{
				@Override
				public Void function(Void t)
				{
					GlStateManager.translate(x, y + 1.0 + 0.16 * dy, z);
					GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
					GlStateManager.translate(r * Math.cos(theta), 0.0D, r * Math.sin(theta));
					GlStateManager.rotate(-(float)theta * 57.29578F - 45.0F, 0.0F, 1.0F, 0.0F);
					GlStateManager.scale(0.4, 2.5, 0.4);
					GlStateManager.rotate(55.0F, 1.0F, 0.0F, 0.0F);
					GlStateManager.rotate(-15.0F, 0.0F, 1.0F, 0.0F);

					return null;
				}
			});
		}

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBossEarth entity)
	{
		return null;
	}

	public static class RenderEntityBossEarthFactory implements IRenderFactory<EntityBossEarth>
	{
		@Override
		public Render<? super EntityBossEarth> createRenderFor(RenderManager manager)
		{
			return new RenderEntityBossEarth(manager);
		}
	}
}
