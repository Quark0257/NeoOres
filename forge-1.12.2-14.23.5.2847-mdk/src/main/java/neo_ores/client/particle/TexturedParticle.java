package neo_ores.client.particle;

import java.util.UUID;

import neo_ores.api.IFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class TexturedParticle
{
	private boolean expired;
	public double prevPosX;
	public double prevPosY;
	public double prevPosZ;
	public double posX;
	public double posY;
	public double posZ;
	public double motionX;
	public double motionY;
	public double motionZ;
	public int particleAge;
	public int particleMaxAge;
	public final ResourceLocation[] textures;
	public float particleSize;
	public float textureSize;
	public float particleRed;
	public float particleGreen;
	public float particleBlue;
	public float particleAlpha;
	private UUID uuid;
	private BlockPos pos;
	private IFunction<TexturedParticle> updateFunc;

	public TexturedParticle(double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int time, float size, ResourceLocation... list)
	{
		this.expired = false;
		this.posX = xCoordIn;
		this.posY = yCoordIn;
		this.posZ = zCoordIn;
		this.motionX = xSpeedIn;
		this.motionY = ySpeedIn;
		this.motionZ = zSpeedIn;
		this.particleAge = 0;
		this.particleMaxAge = time;
		this.textures = list;
		this.particleSize = size * Minecraft.getMinecraft().world.rand.nextFloat() * 0.6F + 0.5F;
		this.textureSize = 4.0F;
		this.particleAlpha = 1.0F;
		this.particleRed = 1.0F;
		this.particleBlue = 1.0F;
		this.particleGreen = 1.0F;
		this.uuid = UUID.randomUUID();
		this.pos = BlockPos.ORIGIN;
		this.updateFunc = null;
	}

	public TexturedParticle setUpdateFunc(IFunction<TexturedParticle> func)
	{
		this.updateFunc = func;
		return this;
	}

	public UUID getUUID()
	{
		return this.uuid;
	}

	public BlockPos getPos()
	{
		return this.pos;
	}

	public TexturedParticle setUUID(UUID uuid)
	{
		this.uuid = uuid;
		return this;
	}

	public TexturedParticle setPos(BlockPos pos)
	{
		this.pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
		return this;
	}

	public void render(float partialTicks)
	{
		Entity view = Minecraft.getMinecraft().getRenderViewEntity();
		if (view == null)
		{
			view = Minecraft.getMinecraft().player;
		}
		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
		GlStateManager.enableCull();
		GlStateManager.depthMask(false);
		GlStateManager.color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
		double interpX = this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks;
		double interpY = this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks;
		double interpZ = this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks;
		float rotX = ActiveRenderInfo.getRotationX();
		float rotZ = ActiveRenderInfo.getRotationZ();
		float rotXY = ActiveRenderInfo.getRotationXY();
		float rotYZ = ActiveRenderInfo.getRotationYZ();
		float rotXZ = ActiveRenderInfo.getRotationXZ();
		Minecraft.getMinecraft().getTextureManager().bindTexture(this.textures[this.particleAge % this.textures.length]);
		float f = 0.0F;
		float f1 = f + 1.0F;
		float f2 = 0.0F;
		float f3 = f2 + 1.0F;
		float f4 = 0.1F * this.particleSize;
		double entityX = view.lastTickPosX + (view.posX - view.lastTickPosX) * partialTicks;
		double entityY = view.lastTickPosY + (view.posY - view.lastTickPosY) * partialTicks;
		double entityZ = view.lastTickPosZ + (view.posZ - view.lastTickPosZ) * partialTicks;
		double posX = interpX - entityX;
		double posY = interpY - entityY;
		double posZ = interpZ - entityZ;
		Vec3d[] avec3d = new Vec3d[] { new Vec3d(-rotX * f4 - rotYZ * f4, -rotXZ * f4, -rotZ * f4 - rotXY * f4), new Vec3d(-rotX * f4 + rotYZ * f4, rotXZ * f4, -rotZ * f4 + rotXY * f4),
				new Vec3d(rotX * f4 + rotYZ * f4, rotXZ * f4, rotZ * f4 + rotXY * f4), new Vec3d(rotX * f4 - rotYZ * f4, -rotXZ * f4, rotZ * f4 - rotXY * f4) };
		for (int l = 0; l < 4; ++l)
		{
			avec3d[l] = avec3d[l].scale(1.0 / this.textureSize);
		}
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder buffer = tess.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(posX + avec3d[0].x, posY + avec3d[0].y, posZ + avec3d[0].z).tex((double) f, (double) f3).endVertex();
		buffer.pos(posX + avec3d[1].x, posY + avec3d[1].y, posZ + avec3d[1].z).tex((double) f1, (double) f3).endVertex();
		buffer.pos(posX + avec3d[2].x, posY + avec3d[2].y, posZ + avec3d[2].z).tex((double) f1, (double) f2).endVertex();
		buffer.pos(posX + avec3d[3].x, posY + avec3d[3].y, posZ + avec3d[3].z).tex((double) f, (double) f2).endVertex();
		tess.draw();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
	}

	public TexturedParticle setColor(int color, float alpha)
	{
		this.particleBlue = (int) (color % 256) / 256.0F;
		this.particleRed = (int) (color / (256 * 256)) / 256.0F;
		this.particleGreen = (int) ((color / 256) % 256) / 256.0F;
		this.particleAlpha = alpha;
		return this;
	}

	public void update()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.updateFunc != null)
		{
			this.updateFunc.function(this);
		}
		else
		{
			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
		}
		
		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setExpired();
		}
	}

	public boolean isExpired()
	{
		return this.expired;
	}

	public void setExpired()
	{
		this.expired = true;
	}
}
