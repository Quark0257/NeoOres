package neo_ores.spell.form;

import neo_ores.api.spell.Spell.SpellFormSpellEntity;
import neo_ores.entity.EntitySpellBullet;
import neo_ores.spell.SpellItemInterfaces.HasChanceLiquid;
import neo_ores.spell.SpellItemInterfaces.HasContinuation;
import neo_ores.spell.SpellItemInterfaces.HasNoAnyResistance;
import neo_ores.spell.SpellItemInterfaces.HasNoGravity;
import neo_ores.spell.SpellItemInterfaces.HasSpeed;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class SpellBullet  extends SpellFormSpellEntity implements HasChanceLiquid,HasSpeed,HasContinuation,HasNoGravity,HasNoAnyResistance
{
	private boolean liquid;
	private boolean noGravity;
	private boolean noResistance;
	private int continuation;
	private int speed;
	
	@Override
	public void onSpellRunning(World world, EntityLivingBase runner, ItemStack stack,RayTraceResult result, NBTTagCompound spells) 
	{
		EntitySpellBullet entity = new EntitySpellBullet(world, runner, this.noGravity,this.noResistance, 20 * (this.continuation + 1),spells,this.liquid, stack);
        entity.shoot(runner, runner.rotationPitch, runner.rotationYaw, 0.0F, 0.5F * (this.speed + 1));
        world.playSound(runner.posX, runner.posY, runner.posZ, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F, true);
        world.spawnEntity(entity);
	}

	@Override
	public boolean needConditional() 
	{
		return false;
	}

	@Override
	public void initialize() 
	{
		this.liquid = false;
		this.continuation = 0;
		this.noGravity= false;
		this.noResistance = false;
		this.speed = 0;
	}

	@Override
	public void setSupport() 
	{
		this.liquid = true;
	}

	@Override
	public void setContinuation(int value) 
	{
		this.continuation = value;
	}

	@Override
	public void setSpeed(int value) {
		this.speed = value;
	}

	@Override
	public void setNoAnyResistance() 
	{
		this.noResistance = true;
	}

	@Override
	public void setNoGravity() 
	{
		this.noGravity = true;
	}
}
