package neo_ores.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import io.netty.buffer.ByteBuf;
import neo_ores.api.FakePlayerMechanicalMagician;
import neo_ores.api.spell.SpellItem;
import neo_ores.main.NeoOresData;
import neo_ores.spell.form.SpellPlaceable;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntitySpellPlaceable extends EntityThrowable implements IEntityAdditionalSpawnData
{
	protected List<SpellItem> spells = new ArrayList<SpellItem>();
	private int life;
	private int ticksInGround;
	private int ticksInAir;
	private String throwerName;
	private ItemStack stack = ItemStack.EMPTY;
	private boolean notCollided;
	private String shooterName = "";
	private int range;
	private boolean vanished = false;

	private static final Predicate<Entity> PROJECTILE_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE);

	public EntitySpellPlaceable(World worldIn)
	{
		super(worldIn);
		this.setSize(0.5F, 0.5F);
	}

	public EntitySpellPlaceable(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntitySpellPlaceable(World worldIn, EntityLivingBase shooter, int life, NBTTagCompound spells, ItemStack handItem, boolean applyNotCollidedFilter, int range, boolean vanish)
	{
		super(worldIn, shooter);
		this.spells = SpellUtils.getListFromItemStackNBT(spells);
		this.motionX = 0;
		this.motionZ = 0;
		this.motionY = 0;
		this.life = life;
		this.setNoGravity(true);
		this.stack = handItem.copy();
		this.shooterName = getShooterName(shooter);
		this.notCollided = applyNotCollidedFilter;
		this.range = range;
		this.vanished = vanish;
		this.setSize(this.range);
	}

	private static String getShooterName(Entity entityShooter)
	{
		return entityShooter.getUniqueID().toString();
	}

	public void shoot(double x, double y, double z, float velocity, float inaccuracy)
	{
	}

	protected float getGravityVelocity()
	{
		return 0.03F;
	}

	public void onUpdate()
	{
		double lastMotionX = this.motionX;
		double lastMotionY = this.motionY;
		double lastMotionZ = this.motionZ;

		if (!this.world.isRemote)
		{
			boolean fakePlayer = this.getThrower() == null && this.shooterName.equals(FakePlayerMechanicalMagician.UUID_STR);
			boolean normalEntity = this.getThrower() != null && !(this.getThrower() instanceof EntityPlayerMP) && !this.getThrower().isEntityAlive();
			boolean playerEntity = this.getThrower() != null && this.getThrower() instanceof EntityPlayerMP;
			if (fakePlayer || normalEntity)
			{
				this.setDead();
			}

			if (playerEntity)
			{
				EntityPlayerMP player = (EntityPlayerMP) this.getThrower();
				if (player instanceof FakePlayerMechanicalMagician)
				{
					FakePlayerMechanicalMagician fake = (FakePlayerMechanicalMagician) player;
					if (!fake.isEntityAlive())
					{
						this.setDead();
					}
				}
				else if (!(player instanceof FakePlayer))
				{
					if (!player.isEntityAlive() && NeoOresData.instance.getPSD(player).isLoggedIn())
					{
						this.setDead();
					}
				}
			}
		}

		super.onUpdate();

		this.motionX = lastMotionX;
		this.motionY = lastMotionY;
		this.motionZ = lastMotionZ;

		List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), Predicates.and(PROJECTILE_TARGETS, new Predicate<Entity>()
		{
			public boolean apply(@Nullable Entity entity)
			{
				if (entity == null)
				{
					return false;
				}
				if (entity instanceof EntitySpellPlaceable)
				{
					return false;
				}
				String name = getShooterName(entity);
				return notCollided ? true : entity.canBeCollidedWith() && !name.equals(shooterName);
			}
		}));

		List<Entity> sorted = list.stream().sorted(new Comparator<Entity>()
		{
			@Override
			public int compare(Entity o1, Entity o2)
			{
				boolean flag = getPositionVector().subtract(o1.getPositionVector()).lengthSquared() < getPositionVector().subtract(o2.getPositionVector()).lengthSquared();
				return flag ? -1 : 1;
			}
		}).collect(Collectors.toList());

		int maxEntity = this.range * 2 + 1;

		int count;
		for (count = 0; count < Math.min(sorted.size(), maxEntity); count++)
		{
			this.onCollided(sorted.get(count));
		}

		float f1 = 0.99F;
		float f2 = this.getGravityVelocity();

		if (this.isInWater())
		{
			f1 = 0.8F;
		}

		this.motionX *= (double) f1;
		this.motionY *= (double) f1;
		this.motionZ *= (double) f1;

		--this.life;
		if (this.life <= 0)
		{
			if (this.vanished)
			{
				EnumFacing vanishedFace = EnumFacing.getFacingFromVector((float) this.motionX, (float) this.motionY, (float) this.motionZ).getOpposite();
				SpellPlaceable.runSpell(this.world, this.getThrower(), this.getStack(), RayTraceUtils.getSimpleResult(this.posX, this.posY, this.posZ, vanishedFace),
						SpellUtils.getItemStackNBTFromList(this.spells, new NBTTagCompound()));
			}
			this.setDead();
		}

		if (!this.hasNoGravity())
		{
			this.motionY -= (double) f2;
		}
	}

	protected void onImpact(RayTraceResult result)
	{
	}

	protected void onCollided(Entity entity)
	{
		if (!this.world.isRemote)
		{
			if (entity instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer) entity;

				if (this.thrower instanceof EntityPlayer && !((EntityPlayer) this.thrower).canAttackPlayer(entityplayer))
				{
					return;
				}
			}

			if (this.getThrower() == null)
			{
				return;
			}

			if (this.spells != null && !this.spells.isEmpty())
			{
				SpellPlaceable.runSpell(this.world, this.getThrower(), this.getStack(), RayTraceUtils.getSimpleResult(entity), SpellUtils.getItemStackNBTFromList(this.spells, new NBTTagCompound()));
			}
		}

		this.world.setEntityState(this, (byte) 3);
		this.setDead();
	}

	public ItemStack getStack()
	{
		return this.stack;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.life = compound.getInteger("life");
		this.notCollided = compound.getBoolean("notCollided");
		this.spells = SpellUtils.getListFromItemStackNBT(compound);
		this.shooterName = compound.getString("shooterName");
		this.stack = new ItemStack(compound.getCompoundTag("stack"));
		this.range = compound.getInteger("range");
		this.vanished = compound.getBoolean("vanished");
		this.setSize(this.range);
	}

	private void setSize(int range)
	{
		float base = 0.5F;
		float width = base * (2 * range + 1);
		this.setSize(width, base);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("life", this.life);
		compound = SpellUtils.getItemStackNBTFromList(this.spells, compound, false);
		NBTTagCompound stack = this.stack.writeToNBT(new NBTTagCompound());
		compound.setTag("stack", stack);
		compound.setString("shooterName", this.shooterName);
		compound.setBoolean("notCollided", this.notCollided);
		compound.setInteger("range", this.range);
		compound.setBoolean("vanished", this.vanished);
		super.writeEntityToNBT(compound);
	}

	@Nullable
	public EntityLivingBase getThrower()
	{
		if (this.thrower == null && this.throwerName != null && !this.throwerName.isEmpty())
		{
			this.thrower = this.world.getPlayerEntityByName(this.throwerName);

			if (this.thrower == null && this.world instanceof WorldServer)
			{
				try
				{
					Entity entity = ((WorldServer) this.world).getEntityFromUuid(UUID.fromString(this.throwerName));

					if (entity instanceof EntityLivingBase)
					{
						this.thrower = (EntityLivingBase) entity;
					}
				}
				catch (Throwable var2)
				{
					this.thrower = null;
				}
			}
		}

		if (this.thrower == null)
		{
			if (this.shooterName != null && !this.shooterName.isEmpty())
			{
				for (EntityPlayer player : this.world.playerEntities)
				{
					if (player instanceof EntityPlayerMP && EntityPlayer.getUUID(((EntityPlayerMP) player).getGameProfile()).equals(UUID.fromString(this.shooterName)))
					{
						this.thrower = player;
						break;
					}
				}

				if (this.thrower == null && this.world instanceof WorldServer)
				{
					Entity entity = ((WorldServer) this.world).getEntityFromUuid(UUID.fromString(this.shooterName));

					if (entity != null && entity instanceof EntityLivingBase)
					{
						this.thrower = (EntityLivingBase) entity;
					}
				}
			}
		}

		return this.thrower;
	}

	public int getTicksInAir()
	{
		return this.ticksInAir;
	}

	public int getTicksInGround()
	{
		return this.ticksInGround;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		NBTTagCompound nbt = this.serializeNBT();
		ByteBufUtils.writeTag(buffer, nbt);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		NBTTagCompound nbt = ByteBufUtils.readTag(additionalData);
		this.deserializeNBT(nbt);
	}
}
