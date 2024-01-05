package neo_ores.client.particle;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParticleMagic1 extends Particle
{
	private float textureSize = 4.0F;
	private final TextureAtlasSprite[] textures;
	private int index;

	public ParticleMagic1(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int color, int time, float size,
			TextureAtlasSprite... texture)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		this.motionX = xSpeedIn;
		this.motionY = ySpeedIn;
		this.motionZ = zSpeedIn;
		this.posX = xCoordIn;
		this.posY = yCoordIn;
		this.posZ = zCoordIn;
		this.particleBlue = (int) (color % (256 * 256)) / 256.0F;
		this.particleRed = (int) (color / (256 * 256)) / 256.0F;
		this.particleGreen = (int) ((color / 256) % 256) / 256.0F;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.particleMaxAge = time;
		this.particleAge = 0;
		this.setSize(size, size);
		this.particleScale *= this.rand.nextFloat() * 0.6F + 0.5F;
		this.textures = texture;
		this.index = 0;
		this.setParticleTexture(this.textures[this.index]);
	}

	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{

		this.setParticleTexture(this.textures[this.particleAge % this.textures.length]);

		GL11.glColor4d(1.0D, 1.0D, 1.0D, 0.5D);

		float f = (float) this.particleTextureIndexX / 16.0F;
		float f1 = f + 0.0624375F;
		float f2 = (float) this.particleTextureIndexY / 16.0F;
		float f3 = f2 + 0.0624375F;
		float f4 = 0.1F * this.particleScale;

		if (this.particleTexture != null)
		{
			f = this.particleTexture.getMinU();
			f1 = this.particleTexture.getMaxU();
			f2 = this.particleTexture.getMinV();
			f3 = this.particleTexture.getMaxV();
		}

		float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
		float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
		float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
		int i = 240;// this.getBrightnessForRender(partialTicks);
		int j = i >> 16 & 65535;
		int k = i & 65535;
		Vec3d[] avec3d = new Vec3d[] { new Vec3d((double) (-rotationX * f4 - rotationXY * f4), (double) (-rotationZ * f4), (double) (-rotationYZ * f4 - rotationXZ * f4)),
				new Vec3d((double) (-rotationX * f4 + rotationXY * f4), (double) (rotationZ * f4), (double) (-rotationYZ * f4 + rotationXZ * f4)),
				new Vec3d((double) (rotationX * f4 + rotationXY * f4), (double) (rotationZ * f4), (double) (rotationYZ * f4 + rotationXZ * f4)),
				new Vec3d((double) (rotationX * f4 - rotationXY * f4), (double) (-rotationZ * f4), (double) (rotationYZ * f4 - rotationXZ * f4)) };

		if (this.particleAngle != 0.0F)
		{
			float f8 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
			float f9 = MathHelper.cos(f8 * 0.5F);
			float f10 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.x;
			float f11 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.y;
			float f12 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.z;
			Vec3d vec3d = new Vec3d((double) f10, (double) f11, (double) f12);

			for (int l = 0; l < 4; ++l)
			{
				avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double) (f9 * f9) - vec3d.dotProduct(vec3d)))
						.add(vec3d.crossProduct(avec3d[l]).scale((double) (2.0F * f9)));
			}
		}

		GlStateManager.depthMask(false);

		buffer.pos((double) f5 + avec3d[0].x / this.textureSize, (double) f6 + avec3d[0].y / this.textureSize, (double) f7 + avec3d[0].z / this.textureSize).tex((double) f1, (double) f3)
				.color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[1].x / this.textureSize, (double) f6 + avec3d[1].y / this.textureSize, (double) f7 + avec3d[1].z / this.textureSize).tex((double) f1, (double) f2)
				.color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[2].x / this.textureSize, (double) f6 + avec3d[2].y / this.textureSize, (double) f7 + avec3d[2].z / this.textureSize).tex((double) f, (double) f2)
				.color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[3].x / this.textureSize, (double) f6 + avec3d[3].y / this.textureSize, (double) f7 + avec3d[3].z / this.textureSize).tex((double) f, (double) f3)
				.color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
	}

	public int getFXLayer()
	{
		return 1;
	}

	public void onUpdate()
	{
		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;

		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setExpired();
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
	}
}
