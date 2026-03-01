package neo_ores.entity;

import neo_ores.client.particle.TexturedParticle;
import neo_ores.event.NeoOresClientEvents;
import neo_ores.main.NeoOres;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityThruster extends Entity
{
	private static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityThruster.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> BOOSTED_ENTITY_ID = EntityDataManager.<Integer>createKey(EntityThruster.class, DataSerializers.VARINT);
	private int age;
	private int life;
	private EntityLivingBase boostedEntity;
	private int color;

	public EntityThruster(World worldIn)
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);
	}

	protected void entityInit()
	{
		this.dataManager.register(BOOSTED_ENTITY_ID, Integer.valueOf(0));
		this.dataManager.register(COLOR, Integer.valueOf(0xFFFFFF));
	}

	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRender3d(double x, double y, double z)
	{
		return false;
	}

	protected EntityThruster(World worldIn, double x, double y, double z, int color, int amplifier)
	{
		super(worldIn);
		this.age = 0;
		this.setSize(0.25F, 0.25F);
		this.setPosition(x, y, z);
		this.dataManager.set(COLOR, color);
		this.color = color;
		this.motionX = this.rand.nextGaussian() * 0.001D;
		this.motionZ = this.rand.nextGaussian() * 0.001D;
		this.motionY = 0.05D;
		this.life = (int) (5.0 * Math.pow(2.0, amplifier));
	}

	public EntityThruster(World world, int color, int amplifier, EntityLivingBase target)
	{
		this(world, target.posX, target.posY, target.posZ, color, amplifier);
		this.dataManager.set(BOOSTED_ENTITY_ID, Integer.valueOf(target.getEntityId()));
		this.boostedEntity = target;
	}

	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z)
	{
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			float f = MathHelper.sqrt(x * x + z * z);
			this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
			this.rotationPitch = (float) (MathHelper.atan2(y, (double) f) * (180D / Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}
	}

	public void onUpdate()
	{
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		super.onUpdate();

		if (this.boostedEntity == null)
		{
			Entity entity = this.world.getEntityByID(((Integer) this.dataManager.get(BOOSTED_ENTITY_ID)).intValue());

			if (entity instanceof EntityLivingBase)
			{
				this.boostedEntity = (EntityLivingBase) entity;
			}
		}

		if (this.boostedEntity != null)
		{
			Vec3d vec3d = this.boostedEntity.getLookVec();
			double d0 = 1.5D;
			double d1 = 0.1D;
			this.boostedEntity.motionX += vec3d.x * d1 + (vec3d.x * d0 - this.boostedEntity.motionX) * 0.5D;
			this.boostedEntity.motionY += vec3d.y * d1 + (vec3d.y * d0 - this.boostedEntity.motionY) * 0.5D;
			this.boostedEntity.motionZ += vec3d.z * d1 + (vec3d.z * d0 - this.boostedEntity.motionZ) * 0.5D;

			this.setPosition(this.boostedEntity.posX, this.boostedEntity.posY, this.boostedEntity.posZ);
			this.motionX = this.boostedEntity.motionX;
			this.motionY = this.boostedEntity.motionY;
			this.motionZ = this.boostedEntity.motionZ;
		}

		float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

		for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
		{
			;
		}

		while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
		{
			this.prevRotationPitch += 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw < -180.0F)
		{
			this.prevRotationYaw -= 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
		{
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

		if (this.age == 0 && !this.isSilent())
		{
			this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_FIREWORK_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
		}

		++this.age;

		if (this.world.isRemote && this.age % 2 < 2)
		{
			for (int i = 0; i < 8; i++)
			{
				NeoOresClientEvents.getInstance().addParticle(new TexturedParticle(this.posX, this.posY - 0.3D, this.posZ, this.rand.nextGaussian() * 0.05D, -this.motionY * 0.5D,
						this.rand.nextGaussian() * 0.05D, 5, (float) (5.0 * this.rand.nextGaussian()), NeoOres.PARTICLE_MAGIC).setColor(this.dataManager.get(COLOR), 1.0F));
			}
		}

		if (!this.world.isRemote && this.age > this.life)
		{
			this.setDead();
		}
	}

	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("age", this.age);
		compound.setInteger("life", this.life);
		compound.setInteger("color", this.dataManager.get(COLOR));
	}

	public void readEntityFromNBT(NBTTagCompound compound)
	{
		this.age = compound.getInteger("age");
		this.life = compound.getInteger("life");
		this.color = compound.getInteger("color");
		this.dataManager.set(COLOR, this.color);
	}

	public boolean canBeAttackedWithItem()
	{
		return false;
	}
}
