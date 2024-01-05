package neo_ores.spell.form;

import neo_ores.api.spell.Spell.SpellFormSpellEntity;
import neo_ores.entity.EntitySpellBullet;
import neo_ores.spell.SpellItemInterfaces.HasChanceLiquid;
import neo_ores.spell.SpellItemInterfaces.HasCollidableFilter;
import neo_ores.spell.SpellItemInterfaces.HasContinuation;
import neo_ores.spell.SpellItemInterfaces.HasNoAnyResistance;
import neo_ores.spell.SpellItemInterfaces.HasNoGravity;
import neo_ores.spell.SpellItemInterfaces.HasNoInertia;
import neo_ores.spell.SpellItemInterfaces.HasSpeed;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class SpellBullet extends SpellFormSpellEntity implements HasChanceLiquid, HasSpeed, HasContinuation, HasNoGravity, HasNoAnyResistance, HasNoInertia, HasCollidableFilter
{
	private boolean liquid = false;
	private boolean noGravity = false;
	private boolean noResistance = false;
	private int continuation = 0;
	private int speed = 0;
	private boolean noInertia = false;
	private boolean canCollided = false;

	@Override
	public void onSpellRunning(World world, EntityLivingBase runner, ItemStack stack, RayTraceResult result, NBTTagCompound spells)
	{
		EntitySpellBullet entity = new EntitySpellBullet(world, runner, this.noGravity, this.noResistance, 20 * (this.continuation + 1), spells, this.liquid, stack, canCollided);
		entity.shoot(runner, runner.rotationPitch, runner.rotationYaw, 0.0F, 0.5F * (this.speed + 2), !this.noInertia);
		world.playSound(runner.posX, runner.posY, runner.posZ, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F, true);
		world.spawnEntity(entity);
	}

	@Override
	public boolean needConditional()
	{
		return false;
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
	public void setSpeed(int value)
	{
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

	@Override
	public void setNoInertia()
	{
		this.noInertia = true;
	}

	@Override
	public void setCollidableFilter()
	{
		this.canCollided = true;
	}
}
