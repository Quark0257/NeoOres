package neo_ores.entity.boss;

import java.util.List;

import neo_ores.entity.ai.EntityAIHurtByTargetNoSight;
import neo_ores.entity.ai.EntityAIMeleeNoPath;
import neo_ores.entity.ai.EntityAISelfSpelling;
import neo_ores.entity.ai.EntityAISpreadSpelling;
import neo_ores.entity.ai.IArrangeBlocks;
import neo_ores.entity.ai.IAttackNoPath;
import neo_ores.entity.ai.ISpellingSelf;
import neo_ores.entity.ai.ISpellingSpread;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public abstract class AbstractNeoOresBoss extends EntityCreature implements IBoundingPos, IRangedAttackMob, IArrangeBlocks, IAttackNoPath, ISpellingSpread, ISpellingSelf
{
	private static final DataParameter<Integer> SPAWN_TIME = EntityDataManager.<Integer>createKey(AbstractNeoOresBoss.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> PEACE = EntityDataManager.<Boolean>createKey(AbstractNeoOresBoss.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> SPELL = EntityDataManager.<Boolean>createKey(AbstractNeoOresBoss.class, DataSerializers.BOOLEAN);
	protected final BossInfoServer bossInfo;

	protected Vec3d boundingPos;
	protected boolean prevPeace;
	private boolean needPlace;

	public AbstractNeoOresBoss(World worldIn)
	{
		super(worldIn);
		this.boundingPos = Vec3d.ZERO;
		this.setHealth(this.getMaxHealth());
		this.prevPeace = false;
		this.bossInfo = this.getBossInfo();
		this.needPlace = false;
		this.isImmuneToFire = true;
	}
	
	public boolean canBreatheUnderwater() 
	{
		return true;
	}

	protected boolean canDespawn()
	{
		return false;
	}
	
	public boolean allowSpread() 
	{
		return !this.isPeace();
	}
	
	public boolean allowSelf() 
	{
		return !this.isPeace();
	}

	protected abstract BossInfoServer getBossInfo();

	protected abstract boolean isWaterType();

	protected abstract SpellData getProximitySpell();

	protected abstract SpellData getTargetHideEntitySpell();

	protected abstract boolean isTargetHideTargetEntity();

	protected abstract boolean isMelee();

	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(SPAWN_TIME, Integer.valueOf(0));
		this.dataManager.register(PEACE, Boolean.valueOf(false));
		this.dataManager.register(SPELL, Boolean.valueOf(false));
	}

	protected void initEntityAI()
	{
		this.tasks.addTask(0, new AIDoNothing());
		if (this.isWaterType())
		{

		}
		else
		{
			this.tasks.addTask(1, new EntityAISwimming(this));
			this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
		}
		this.tasks.addTask(7, new EntityAILookIdle(this));
		if (this.isMelee())
		{
			this.tasks.addTask(2, new EntityAIMeleeNoPath<>(this, 1.0D, !this.isTargetHideTargetEntity()));
		}
		else
		{
			this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 20, 32.0F));
		}
		this.tasks.addTask(3, new EntityAISpreadSpelling<>(this, 600, 200, 40, 6, 32.0));
		this.tasks.addTask(3, new EntityAISelfSpelling<>(this, 600, 200, 40));
		this.targetTasks.addTask(1, new EntityAIHurtByTargetNoSight(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
	}

	public int getSpawnTime()
	{
		return ((Integer) this.dataManager.get(SPAWN_TIME)).intValue();
	}

	public void setSpawnTime(int time)
	{
		this.dataManager.set(SPAWN_TIME, Integer.valueOf(time));
	}

	public boolean isPeace()
	{
		return ((Boolean) this.dataManager.get(PEACE)).booleanValue();
	}

	public void setPeace(boolean flag)
	{
		this.dataManager.set(PEACE, Boolean.valueOf(flag));
	}
	
	@Override
	public boolean isSpelling()
	{
		return ((Boolean) this.dataManager.get(SPELL)).booleanValue();
	}
	
	@Override
	public void setSpelling(boolean flag) 
	{
		this.dataManager.set(SPELL, Boolean.valueOf(flag));
	}
	
	public abstract double peaceModeDist();

	public boolean isTransparentView()
	{
		final int spawnTime = this.getSpawnTime();
		return spawnTime > 0 && (spawnTime > 100 || (spawnTime / 10) % 2 == 0) || this.isPeace() || !this.isEntityAlive();
	}

	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setDouble("boundingPosX", this.boundingPos.x);
		compound.setDouble("boundingPosY", this.boundingPos.y);
		compound.setDouble("boundingPosZ", this.boundingPos.z);
		compound.setBoolean("isPeace", this.isPeace());
		compound.setInteger("spawnTime", this.getSpawnTime());
		compound.setBoolean("spelling", this.isSpelling());
	}

	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		double x = compound.getDouble("boundingPosX");
		double y = compound.getDouble("boundingPosY");
		double z = compound.getDouble("boundingPosZ");
		this.boundingPos = new Vec3d(x, y, z);
		this.setPeace(compound.getBoolean("isPeace"));
		this.setSpawnTime(compound.getInteger("spawnTime"));
		this.setSpelling(compound.getBoolean("spelling"));
		if (this.hasCustomName())
		{
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	public void setCustomNameTag(String name)
	{
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	public void setBound(Vec3d pos)
	{
		this.boundingPos = pos;
		this.setSpawnTime(200);
		this.setHealth(1.0F);
		this.bossInfo.setPercent(0.0F);
	}

	public SoundCategory getSoundCategory()
	{
		return SoundCategory.HOSTILE;
	}

	public void onLivingUpdate()
	{
		this.updateArmSwingProgress();
		float f = this.getBrightness();

		if (f > 0.5F)
		{
			this.idleTime += 2;
		}

		super.onLivingUpdate();
	}

	public void onUpdate()
	{
		super.onUpdate();

		if (!this.world.isRemote && this.needPeaceMode())
		{
			this.setPeace(true);
		}

		if (this.isPeace() && !this.world.isRemote)
		{
			this.setLocationAndAngles(this.boundingPos.x, this.boundingPos.y, this.boundingPos.z, 0.0F, 0.0F);
			if (!this.prevPeace)
			{
				// TODO
			}
		}

		if (this.ticksExisted % 10 == 0)
		{
			this.heal(0.01F * this.getMaxHealth());
		}

		this.prevPeace = this.isPeace();
		
		if (this.onGround) 
		{
			this.needPlace = false;
		}
		else if (this.needPlace) 
		{
			BlockPos pos = this.getPosition().add(EnumFacing.DOWN.getDirectionVec());
			IBlockState state = this.world.getBlockState(pos);
			if (state.getBlock().isAir(state, this.world, pos)) 
			{
				this.getPlaceSpell().run(this, RayTraceUtils.getSimpleResult(pos.add(EnumFacing.DOWN.getDirectionVec()), EnumFacing.DOWN));
				this.needPlace = false;
			}
		}
	}

	protected boolean needPeaceMode()
	{
		return this.world.getDifficulty() == EnumDifficulty.PEACEFUL || this.getPositionVector().subtract(this.boundingPos).lengthSquared() > this.peaceModeDist() * this.peaceModeDist();
	}

	protected void checkPeaceMode()
	{
		if (!this.needPeaceMode())
		{
			this.setPeace(false);
		}
	}

	protected void updateAITasks()
	{
		if (this.getSpawnTime() > 0)
		{
			int timer = this.getSpawnTime() - 1;

			if (timer <= 0)
			{
				this.world.newExplosion(this, this.posX, this.posY + (double) this.getEyeHeight(), this.posZ, 4.0F, false, false);
				this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITHER_SPAWN, this.getSoundCategory(), 1.0F, 1.0F, true);
			}

			this.setSpawnTime(timer);

			this.heal(this.getMaxHealth() * 0.01F);
		}
		else if (this.isPeace())
		{
		}
		else
		{
			super.updateAITasks();
		}

		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
	}

	public void addTrackingPlayer(EntityPlayerMP player)
	{
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	public void removeTrackingPlayer(EntityPlayerMP player)
	{
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	protected SoundEvent getSwimSound()
	{
		return SoundEvents.ENTITY_HOSTILE_SWIM;
	}

	protected SoundEvent getSplashSound()
	{
		return SoundEvents.ENTITY_HOSTILE_SPLASH;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.ENTITY_HOSTILE_HURT;
	}

	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_HOSTILE_DEATH;
	}

	protected SoundEvent getFallSound(int heightIn)
	{
		return heightIn > 4 ? SoundEvents.ENTITY_HOSTILE_BIG_FALL : SoundEvents.ENTITY_HOSTILE_SMALL_FALL;
	}

	public boolean attackEntityAsMob(Entity entityIn)
	{
		if (this.isMelee()) 
		{
			this.getProximitySpell().run(this, new RayTraceResult(entityIn));
			return true;
		}
		return super.attackEntityAsMob(entityIn);
	}

	public float getBlockPathWeight(BlockPos pos)
	{
		return 0.5F - this.world.getLightBrightness(pos);
	}

	protected boolean isValidLightLevel()
	{
		BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

		if (this.world.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32))
		{
			return false;
		}
		else
		{
			int i = this.world.getLightFromNeighbors(blockpos);

			if (this.world.isThundering())
			{
				int j = this.world.getSkylightSubtracted();
				this.world.setSkylightSubtracted(10);
				i = this.world.getLightFromNeighbors(blockpos);
				this.world.setSkylightSubtracted(j);
			}

			return i <= this.rand.nextInt(8);
		}
	}

	public boolean getCanSpawnHere()
	{
		return super.getCanSpawnHere();
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
	}
	
	public boolean isEntityInvulnerable(DamageSource source)
    {
        return source == DamageSource.IN_WALL || super.isEntityInvulnerable(source);
    }

	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isEntityInvulnerable(source) && source != DamageSource.OUT_OF_WORLD)
		{
			return false;
		}
		
		if (this.isNoAI() && source != DamageSource.OUT_OF_WORLD)
		{
			this.checkPeaceMode();
			return false;
		}
		return super.attackEntityFrom(source, amount);
	}

	protected boolean isNoAI()
	{
		return this.getSpawnTime() > 0 || this.isPeace();
	}

	protected boolean canDropLoot()
	{
		return true;
	}

	public boolean isNonBoss()
	{
		return false;
	}

	protected boolean canBeRidden(Entity entityIn)
	{
		return false;
	}

	public void fall(float distance, float damageMultiplier)
	{
		// super.fall(distance, damageMultiplier);
	}

	class AIDoNothing extends EntityAIBase
	{
		public AIDoNothing()
		{
			this.setMutexBits(7);
		}

		public boolean shouldExecute()
		{
			return AbstractNeoOresBoss.this.isNoAI();
		}
	}
	
	public abstract SpellData getBreakSpell();
	
	public abstract SpellData getPlaceSpell();

	@Override
	public void arrangeBlocks(List<BlockPos> blocks, boolean targetUpward)
	{
		if (targetUpward)
		{
			this.jump();
			this.needPlace = true;
		}
		
		for (BlockPos pos : blocks) 
		{
			IBlockState state = this.world.getBlockState(pos);
			if (!state.getBlock().isAir(state, this.world, pos)) 
			{
				this.getBreakSpell().run(this, RayTraceUtils.getSimpleResult(pos, null));
			}
			pos = pos.add(EnumFacing.UP.getDirectionVec());
			state = this.world.getBlockState(pos);
			if (!state.getBlock().isAir(state, this.world, pos)) 
			{
				this.getBreakSpell().run(this, RayTraceUtils.getSimpleResult(pos, null));
			}
		}
	}
	
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
	{
		this.getProximitySpell().run(this, null);
	}
	
	@Override
	public void attackNoPath(EntityLivingBase target)
	{
		this.getTargetHideEntitySpell().run(this, RayTraceUtils.getSimpleResult(target));
	}
}
