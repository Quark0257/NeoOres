package neo_ores.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import neo_ores.api.SpellUtils;
import neo_ores.api.spell.Spell;
import neo_ores.api.spell.SpellItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeEventFactory;

public class EntitySpellBullet extends EntityThrowable
{
	protected List<SpellItem> spells = new ArrayList<SpellItem>();
	private int life;
	private int ticksInGround;
	private int ticksInAir;
	private String throwerName;
	private ItemStack stack = ItemStack.EMPTY;
	private boolean throughWater;
	private boolean isUpdatingDefault;
	private int ignoreTime;
	private boolean supportLiquid;

	public EntitySpellBullet(World worldIn) 
	{
		super(worldIn);
	}

    public EntitySpellBullet(World worldIn, double x, double y, double z)
    {
        super(worldIn,x,y,z);
    }
    
    public EntitySpellBullet(World worldIn, EntityLivingBase shooter,boolean nogravity,boolean noResistance,int life,NBTTagCompound spells,boolean supportLiquid,ItemStack handItem)
    {
        super(worldIn,shooter);
        this.spells = SpellUtils.getListFromItemStackNBT(spells);
        this.motionX = 0;
        this.motionZ = 0;
        this.motionY = 0;
        this.life = life;
        this.supportLiquid = supportLiquid;
        this.throughWater = noResistance;
        this.setNoGravity(nogravity);
        this.stack = handItem;
    }
    
    public void shoot(EntityLivingBase entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity)
    {
        float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
        float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
        float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
        this.shoot((double)f, (double)f1, (double)f2, velocity, 0.0F);
        this.motionX += entityThrower.motionX;
        this.motionZ += entityThrower.motionZ;

        if (!entityThrower.onGround)
        {
            this.motionY += entityThrower.motionY;
        }
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy)
    {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double)f;
        y = y / (double)f;
        z = z / (double)f;
        x = x * (double)velocity;
        y = y * (double)velocity;
        z = z * (double)velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.ticksInGround = 0;
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
    	
    	this.isUpdatingDefault = true;
    	super.onUpdate();
    	this.isUpdatingDefault = false;    	
    	
    	this.motionX = lastMotionX;
    	this.motionY = lastMotionY;
    	this.motionZ = lastMotionZ;
    	
    	Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d1,this.supportLiquid,!this.supportLiquid,false);
        vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
    	
    	Entity entity = null;
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D));
        double d0 = 0.0D;
        boolean flag = false;

        for (int i = 0; i < list.size(); ++i)
        {
            Entity entity1 = list.get(i);

            if (entity1.canBeCollidedWith())
            {
                if (entity1 == this.ignoreEntity)
                {
                    flag = true;
                }
                else if (this.thrower != null && this.ticksExisted < 2 && this.ignoreEntity == null)
                {
                    this.ignoreEntity = entity1;
                    flag = true;
                }
                else
                {
                    flag = false;
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
                    RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

                    if (raytraceresult1 != null)
                    {
                        double d1 = vec3d.squareDistanceTo(raytraceresult1.hitVec);

                        if (d1 < d0 || d0 == 0.0D)
                        {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }
        }

        if (this.ignoreEntity != null)
        {
            if (flag)
            {
                this.ignoreTime = 2;
            }
            else if (this.ignoreTime-- <= 0)
            {
                this.ignoreEntity = null;
            }
        }

        if (entity != null)
        {
            raytraceresult = new RayTraceResult(entity);
        }

        if (raytraceresult != null)
        {
            if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK && this.world.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.PORTAL)
            {
                this.setPortal(raytraceresult.getBlockPos());
            }
            else if (!ForgeEventFactory.onProjectileImpact(this, raytraceresult))
            {
                this.onImpact(raytraceresult);
            }
        }
    	
    	float f1 = (throughWater) ? 1.0F : 0.99F;
    	float f2 = this.getGravityVelocity();
    	
    	if (!this.throughWater && this.isInWater())
        {
            for (int j = 0; j < 4; ++j)
            {
                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
            }

            f1 = 0.8F;
        }
    	
    	this.motionX *= (double)f1;
        this.motionY *= (double)f1;
        this.motionZ *= (double)f1;
    	
        --this.life;
        if(life <= 0)
        {
        	this.setDead();
        }
    	
    	if (!this.hasNoGravity())
        {
            this.motionY -= (double)f2;
        }
    }
    

	protected void onImpact(RayTraceResult result) 
	{
		if(this.isUpdatingDefault) return;

		List<SpellItem> notformlist = new ArrayList<SpellItem>();
		List<SpellItem> formlist = new ArrayList<SpellItem>();
		if(this.spells != null && !this.spells.isEmpty())
		{
			for(SpellItem spell : this.spells)
			{
				if(spell.getSpellClass() instanceof Spell.SpellForm)
				{
					formlist.add(spell);
				}
				else
				{
					notformlist.add(spell);
				}
			}
			
			for(SpellItem formspell : formlist)
			{
				Spell form = formspell.getSpellClass();
				for(SpellItem notformspell : notformlist)
				{
					if(notformspell.getSpellClass() instanceof Spell.SpellCorrection)
					{
						((Spell.SpellCorrection)notformspell.getSpellClass()).onCorrection(form);
					}
				}
				
				if(form instanceof Spell.SpellForm)
				{
					((Spell.SpellForm)form).onSpellRunning(this.world, this.getThrower(),this.getStack(),result,SpellUtils.getItemStackNBTFromList(notformlist, new NBTTagCompound()));
				}
			}
		}
		
		this.world.setEntityState(this, (byte)3);
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
        this.throughWater = compound.getBoolean("throughWater");
        this.supportLiquid = compound.getBoolean("supportLiquid");
        this.spells = SpellUtils.getListFromItemStackNBT(compound);
        
        this.stack = new ItemStack(compound.getCompoundTag("stack"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) 
	{
		super.writeEntityToNBT(compound);
        compound.setInteger("life", this.life);
        compound.setBoolean("throughWater",this.throughWater);
        compound.setBoolean("supportLiquid",this.supportLiquid);
        compound = SpellUtils.getItemStackNBTFromList(spells, compound);
        NBTTagCompound stack = this.stack.writeToNBT(new NBTTagCompound());

        compound.setTag("stack", stack);
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
                    Entity entity = ((WorldServer)this.world).getEntityFromUuid(UUID.fromString(this.throwerName));

                    if (entity instanceof EntityLivingBase)
                    {
                        this.thrower = (EntityLivingBase)entity;
                    }
                }
                catch (Throwable var2)
                {
                    this.thrower = null;
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
}
