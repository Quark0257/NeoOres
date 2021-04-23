package neo_ores.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import neo_ores.spell.ISpell;
import neo_ores.spell.SpellItem;
import neo_ores.spell.SpellType;
import neo_ores.spell.SpellUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeEventFactory;

public class SpellBullet extends EntityThrowable
{
	private List<SpellItem> spells;
	private int life;
	private EntityLivingBase shootingEntity;
	private int ticksInGround;
	private int xTile;
	private int yTile;
	private int zTile;
	private int ticksInAir;
	private Block inTile;
	private int ignoreTime;
	private String throwerName;
	private boolean isSupportLiquid;

	public SpellBullet(World worldIn) 
	{
		super(worldIn);
	}
	
	public SpellBullet(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    public SpellBullet(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }
    
    public SpellBullet(World worldIn, EntityLivingBase shooter,double x, double y, double z,float pitch,float yaw, float speed, boolean gravity,int life,boolean isSupportLiquid ,NBTTagCompound spells)
    {
        super(worldIn, x, y, z);
        this.shootingEntity = shooter;
        this.spells = SpellUtils.getListFromItemStackNBT(spells);
        double pitchR = Math.toRadians((double)pitch);
        double yawR = Math.toRadians((double)yaw);
        this.motionX = -Math.sin(yawR) * Math.cos(pitchR);
        this.motionZ = Math.sin(yawR) * Math.cos(pitchR);
        this.motionY = Math.sin(pitchR); 
        this.life = life;
        this.isSupportLiquid = isSupportLiquid;
        this.setNoGravity(!gravity);
    }
    
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {}
    
    protected float getGravityVelocity()
    {
        return 0.03F * 100.0F / (float)life;
    }

    public void onUpdate()
    {
    	if (this.world.isRemote || (this.shootingEntity == null || !this.shootingEntity.isDead) && this.world.isBlockLoaded(new BlockPos(this)))
        {
    		this.lastTickPosX = this.posX;
            this.lastTickPosY = this.posY;
            this.lastTickPosZ = this.posZ;
            
    		if (!this.world.isRemote)
            {
                this.setFlag(6, this.isGlowing());
            }
            this.onEntityUpdate();
            
            if (this.throwableShake > 0)
            {
                --this.throwableShake;
            }

            if (this.inGround)
            {
                if (this.world.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile)
                {
                    ++this.ticksInGround;

                    if (this.ticksInGround == 1200)
                    {
                        this.setDead();
                    }

                    return;
                }

                this.inGround = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
            else
            {
                ++this.ticksInAir;
            }
            
            if(this.hasNoGravity())
            {
            	--this.life;
                if(life <= 0)
                {
                	this.setDead();
                }
            }
            
            //On Impact
            Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d1,this.isSupportLiquid);
            vec3d = new Vec3d(this.posX, this.posY, this.posZ);
            vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (raytraceresult != null)
            {
                vec3d1 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
            }

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
            
            //EntityPosition
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

            for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
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
            float f2 = this.getGravityVelocity();
            
            if (!this.hasNoGravity())
            {
                this.motionY -= (double)f2;
            }

            this.setPosition(this.posX, this.posY, this.posZ);
        }
    	else
    	{
    		this.setDead();
    	}
    }
    
	@Override
	protected void onImpact(RayTraceResult result) 
	{
		List<SpellItem> notformlist = new ArrayList<SpellItem>();
		List<SpellItem> formlist = new ArrayList<SpellItem>();
		for(SpellItem spell : this.spells)
		{
			if(spell.getSpellClass().getSpellItemType() == SpellType.FORM)
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
			ISpell form = formspell.getSpellClass();
			for(SpellItem notformspell : notformlist)
			{
				if(notformspell.getSpellClass().getSpellItemType() == SpellType.CORRECTION)
				{
					notformspell.getSpellClass().onCorrection(form);
				}
			}
			
			form.onSpellRunning(this.world, this.getThrower(),null,result,SpellUtils.getItemStackNBTFromList(notformlist, new NBTTagCompound()));
		}
		
		this.setDead();
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) 
	{
		this.xTile = compound.getInteger("xTile");
        this.yTile = compound.getInteger("yTile");
        this.zTile = compound.getInteger("zTile");
        this.life = compound.getInteger("life");
        this.isSupportLiquid = compound.getBoolean("isSupportedLiquid");
        this.spells = SpellUtils.getListFromItemStackNBT(compound);

        if (compound.hasKey("inTile", 8))
        {
            this.inTile = Block.getBlockFromName(compound.getString("inTile"));
        }
        else
        {
            this.inTile = Block.getBlockById(compound.getByte("inTile") & 255);
        }

        this.throwableShake = compound.getByte("shake") & 255;
        this.inGround = compound.getByte("inGround") == 1;
        this.thrower = null;
        this.throwerName = compound.getString("ownerName");

        if (this.throwerName != null && this.throwerName.isEmpty())
        {
            this.throwerName = null;
        }

        this.thrower = this.getThrower();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) 
	{
		compound.setInteger("xTile", this.xTile);
        compound.setInteger("yTile", this.yTile);
        compound.setInteger("zTile", this.zTile);
        compound.setInteger("life", this.life);
        compound.setBoolean("isSupportedLiquid",this.isSupportLiquid);
        compound = SpellUtils.getItemStackNBTFromList(spells, compound);
        ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(this.inTile);
        compound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        compound.setByte("shake", (byte)this.throwableShake);
        compound.setByte("inGround", (byte)(this.inGround ? 1 : 0));

        if ((this.throwerName == null || this.throwerName.isEmpty()) && this.thrower instanceof EntityPlayer)
        {
            this.throwerName = this.thrower.getName();
        }

        compound.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
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
