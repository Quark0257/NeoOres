package neo_ores.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleNoGravity extends Particle
{
	public ParticleNoGravity(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int color, int time, float size,
			TextureAtlasSprite texture)
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
		this.setParticleTexture(texture);
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
