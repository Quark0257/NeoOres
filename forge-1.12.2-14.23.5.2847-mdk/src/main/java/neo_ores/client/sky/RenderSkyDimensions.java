package neo_ores.client.sky;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;

public class RenderSkyDimensions extends IRenderHandler
{
	private static final ResourceLocation FORCEFIELD_TEXTURES = new ResourceLocation("textures/misc/forcefield.png");
	private static final ResourceLocation MOON_PHASES_TEXTURES = new ResourceLocation("textures/environment/moon_phases.png");
	private static final ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");

	private int starGLCallList = -1;
	private int glSkyList = -1;
	private int glSkyList2 = -1;
	private TextureManager renderEngine;
	private VertexFormat vertexBufferFormat;
	private final boolean isStarColored;

	public RenderSkyDimensions(boolean starColored)
	{
		this.isStarColored = starColored;
	}

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc)
	{
		this.renderEngine = mc.getTextureManager();
		this.renderEngine.bindTexture(FORCEFIELD_TEXTURES);
		this.vertexBufferFormat = new VertexFormat();
		this.vertexBufferFormat.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));

		GlStateManager.disableTexture2D();
		Vec3d vec3d = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
		float f = (float) vec3d.x;
		float f1 = (float) vec3d.y;
		float f2 = (float) vec3d.z;

		GlStateManager.color(f, f1, f2);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.depthMask(false);
		GlStateManager.enableFog();
		GlStateManager.color(f, f1, f2);

		GlStateManager.callList(this.glSkyList);

		GlStateManager.disableFog();
		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderHelper.disableStandardItemLighting();
		float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);

		if (afloat != null)
		{
			GlStateManager.disableTexture2D();
			GlStateManager.shadeModel(7425);
			GlStateManager.pushMatrix();
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
			float f6 = afloat[0];
			float f7 = afloat[1];
			float f8 = afloat[2];

			bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
			bufferbuilder.pos(0.0D, 100.0D, 0.0D).color(f6, f7, f8, afloat[3]).endVertex();

			for (int j2 = 0; j2 <= 16; ++j2)
			{
				float f21 = (float) j2 * ((float) Math.PI * 2F) / 16.0F;
				float f12 = MathHelper.sin(f21);
				float f13 = MathHelper.cos(f21);
				bufferbuilder.pos((double) (f12 * 120.0F), (double) (f13 * 120.0F), (double) (-f13 * 40.0F * afloat[3])).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
			}

			tessellator.draw();
			GlStateManager.popMatrix();
			GlStateManager.shadeModel(7424);
		}

		GlStateManager.enableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.pushMatrix();
		float f16 = world.provider.isSurfaceWorld() ? 1.0F - world.getRainStrength(partialTicks) : 1.0F;
		GlStateManager.color(1.0F, 1.0F, 1.0F, f16);
		GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);

		if (world.provider.isSurfaceWorld())
		{
			float f17 = 30.0F;
			this.renderEngine.bindTexture(SUN_TEXTURES);
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
			bufferbuilder.pos((double) (-f17), 100.0D, (double) (-f17)).tex(0.0D, 0.0D).endVertex();
			bufferbuilder.pos((double) f17, 100.0D, (double) (-f17)).tex(1.0D, 0.0D).endVertex();
			bufferbuilder.pos((double) f17, 100.0D, (double) f17).tex(1.0D, 1.0D).endVertex();
			bufferbuilder.pos((double) (-f17), 100.0D, (double) f17).tex(0.0D, 1.0D).endVertex();
			tessellator.draw();

			f17 = 20.0F;
			this.renderEngine.bindTexture(MOON_PHASES_TEXTURES);
			int k1 = world.getMoonPhase();
			int i2 = k1 % 4;
			int k2 = k1 / 4 % 2;
			float f22 = (float) (i2 + 0) / 4.0F;
			float f23 = (float) (k2 + 0) / 2.0F;
			float f24 = (float) (i2 + 1) / 4.0F;
			float f14 = (float) (k2 + 1) / 2.0F;
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
			bufferbuilder.pos((double) (-f17), -100.0D, (double) f17).tex((double) f24, (double) f14).endVertex();
			bufferbuilder.pos((double) f17, -100.0D, (double) f17).tex((double) f22, (double) f14).endVertex();
			bufferbuilder.pos((double) f17, -100.0D, (double) (-f17)).tex((double) f22, (double) f23).endVertex();
			bufferbuilder.pos((double) (-f17), -100.0D, (double) (-f17)).tex((double) f24, (double) f23).endVertex();
			tessellator.draw();
		}

		GlStateManager.disableTexture2D();

		this.generateStars(world, mc);
		float f15 = world.getStarBrightness(partialTicks) * f16;

		if (f15 > 0.0F)
		{
			GlStateManager.color(f15, f15, f15, f15);

			GlStateManager.callList(this.starGLCallList);
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableFog();
		GlStateManager.popMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.color(0.0F, 0.0F, 0.0F);
		double d3 = mc.player.getPositionEyes(partialTicks).y - world.getHorizon();

		if (d3 < 0.0D)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 12.0F, 0.0F);

			GlStateManager.callList(this.glSkyList2);

			GlStateManager.popMatrix();
			float f19 = -((float) (d3 + 65.0D));
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
			bufferbuilder.pos(-1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			tessellator.draw();
		}

		if (world.provider.isSkyColored())
		{
			GlStateManager.color(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
		}
		else
		{
			GlStateManager.color(f, f1, f2);
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, -((float) (d3 - 16.0D)), 0.0F);
		GlStateManager.callList(this.glSkyList2);
		GlStateManager.popMatrix();
		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
	}

	private void generateStars(WorldClient world, Minecraft mc)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		if (this.starGLCallList >= 0)
		{
			GLAllocation.deleteDisplayLists(this.starGLCallList);
			this.starGLCallList = -1;
		}

		this.starGLCallList = GLAllocation.generateDisplayLists(1);
		GlStateManager.pushMatrix();
		GlStateManager.glNewList(this.starGLCallList, 4864);
		this.renderStars(bufferbuilder, world, mc);
		tessellator.draw();
		GlStateManager.glEndList();
		GlStateManager.popMatrix();
	}

	private void renderStars(BufferBuilder bufferBuilderIn, WorldClient world, Minecraft mc)
	{
		Random random = new Random(10842L);
		bufferBuilderIn.begin(7, DefaultVertexFormats.POSITION_COLOR);

		for (int i = 0; i < 2150; ++i)
		{
			double d0 = (double) (random.nextFloat() * 2.0F - 1.0F);
			double d1 = (double) (random.nextFloat() * 2.0F - 1.0F);
			double d2 = (double) (random.nextFloat() * 2.0F - 1.0F);
			double d3 = (double) (0.15F + random.nextFloat() * 0.1F);
			double d4 = d0 * d0 + d1 * d1 + d2 * d2;

			if (d4 < 1.0D && d4 > 0.01D)
			{
				d4 = 1.0D / Math.sqrt(d4);
				d0 = d0 * d4;
				d1 = d1 * d4;
				d2 = d2 * d4;
				double d5 = d0 * 100.0D;
				double d6 = d1 * 100.0D;
				double d7 = d2 * 100.0D;
				double d8 = Math.atan2(d0, d2);
				double d9 = Math.sin(d8);
				double d10 = Math.cos(d8);
				double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
				double d12 = Math.sin(d11);
				double d13 = Math.cos(d11);
				double d14 = random.nextDouble() * Math.PI * 2.0D;
				double d15 = Math.sin(d14);
				double d16 = Math.cos(d14);

				for (int j = 0; j < 4; ++j)
				{
					double d18 = (double) ((j & 2) - 1) * d3;
					double d19 = (double) ((j + 1 & 2) - 1) * d3;
					double d21 = d18 * d16 - d19 * d15;
					double d22 = d19 * d16 + d18 * d15;
					double d23 = d21 * d12 + 0.0D * d13;
					double d24 = 0.0D * d12 - d21 * d13;
					double d25 = d24 * d9 - d22 * d10;
					double d26 = d22 * d9 + d24 * d10;

					if (this.isStarColored)
					{
						float red;
						float green;
						float blue;

						double seed = random.nextDouble();
						float temp = (float) (4.0D * (seed - 0.5D) * (seed - 0.5D) * (seed - 0.5D) + 0.5D);

						float f0 = 0.08F;
						float f1 = 0.30F;
						float f2 = 0.50F;
						float f3 = 0.75F;

						if (temp < f0)
						{
							red = 1.0F;
							blue = 0.0F;
							green = 0.0F;
						}
						else if (temp < f1)
						{
							red = 1.0F;
							blue = 0.0F;
							green = (temp - f0) / (f1 - f0);
						}
						else if (temp < f2)
						{
							red = 1.0F;
							green = 1.0F;
							blue = (temp - f1) / (f2 - f1);
						}
						else if (temp < f3)
						{
							red = 1.0F;
							green = 1.0F;
							blue = 1.0F;
						}
						else
						{
							red = ((1.00F - temp) / (1.0F - f3)) * 0.75F + 0.25F;
							green = 0.5F + red * 0.5F;
							blue = 1.0F;
						}

						bufferBuilderIn.pos(d5 + d25, d6 + d23, d7 + d26).color(red, green, blue, 1.0F).endVertex();
					}
					else
					{
						bufferBuilderIn.pos(d5 + d25, d6 + d23, d7 + d26).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
					}
				}
			}
		}
	}
}
