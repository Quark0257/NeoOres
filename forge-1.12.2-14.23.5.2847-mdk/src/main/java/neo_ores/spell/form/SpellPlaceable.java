package neo_ores.spell.form;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.spell.Spell;
import neo_ores.api.spell.SpellItem;
import neo_ores.api.spell.Spell.SpellFormNotEntity;
import neo_ores.entity.EntitySpellPlaceable;
import neo_ores.spell.SpellItemInterfaces.HasChanceLiquid;
import neo_ores.spell.SpellItemInterfaces.HasContinuation;
import neo_ores.spell.SpellItemInterfaces.HasContinuationDown;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.spell.SpellItemInterfaces.HasReach;
import neo_ores.spell.SpellItemInterfaces.HasUncollidable;
import neo_ores.spell.SpellItemInterfaces.HasVanished;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.ServerUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class SpellPlaceable extends SpellFormNotEntity implements HasRange, HasContinuation, HasUncollidable, HasContinuationDown, HasReach, HasChanceLiquid, HasVanished
{
	private int size = 0;
	private int continuation = 0;
	private boolean uncollidable = false;
	private int continuationDown = 0;
	private int reachM = 0;
	private boolean liquid = false;
	private boolean vanished = false;

	@Override
	public boolean needConditional()
	{
		return false;
	}

	@Override
	public void onSpellRunningServer(World world, EntityLivingBase runner, ItemStack stack, RayTraceResult result, NBTTagCompound spells)
	{
		RayTraceResult traceresult = result;
		if (traceresult == null)
		{
			double reach = 3.0;
			if (runner instanceof EntityPlayer)
			{
				reach = ((EntityPlayer) runner).getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
			}
			reach *= ((double) this.reachM / 3.0 + 1.0D);
			traceresult = SpellUtils.rayTrace(world, runner, reach, this.liquid, !this.uncollidable, true);
		}

		if (traceresult == null)
		{
			return;
		}

		if (traceresult.typeOfHit == Type.ENTITY && traceresult.entityHit != null)
		{
			traceresult = RayTraceUtils.getSimpleResult(traceresult.entityHit.getPosition(), EnumFacing.DOWN);
		}

		if (traceresult.typeOfHit != Type.BLOCK)
		{
			return;
		}

		int maxLife = 200 * ((int) Math.pow(2, this.continuation)) / (this.continuationDown + 1);
		EntitySpellPlaceable entity = new EntitySpellPlaceable(world, runner, maxLife, spells, stack, this.uncollidable, this.size, this.vanished);
		BlockPos entitySpawn = traceresult.getBlockPos();
		if (world.getBlockState(entitySpawn).getBlock() != Blocks.AIR)
		{
			if (traceresult.sideHit == EnumFacing.DOWN)
			{
				entitySpawn = entitySpawn.add(0, -entity.height, 0);
			}
			else
			{
				entitySpawn = entitySpawn.add(traceresult.sideHit.getDirectionVec());
			}
		}
		entity.setPositionAndRotation(entitySpawn.getX() + 0.5D, entitySpawn.getY(), entitySpawn.getZ() + 0.5D, 0.0F, 0.0F);
		ServerUtils.sendSoundToClient(world, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_BLAZE_SHOOT, runner instanceof EntityPlayer ? SoundCategory.PLAYERS : SoundCategory.HOSTILE,
				1.0F, 1.0F);
		world.spawnEntity(entity);
	}

	@Override
	public void setRange(int value)
	{
		this.size = value;
	}

	@Override
	public void setContinuationDown(int value)
	{
		this.continuationDown = value;
	}

	@Override
	public void setUncollidable()
	{
		this.uncollidable = true;
	}

	@Override
	public void setContinuation(int value)
	{
		this.continuation = value;
	}

	@Override
	public void setReach(int value)
	{
		this.reachM = value;
	}

	@Override
	public void setSupport()
	{
		this.liquid = true;
	}

	public static void runSpell(World world, EntityLivingBase runner, ItemStack stack, RayTraceResult result, NBTTagCompound spells)
	{
		List<Spell> corrections = new ArrayList<Spell>();
		List<Spell> effects = new ArrayList<Spell>();

		for (SpellItem spell : SpellUtils.getListFromItemStackNBT(spells))
		{
			Spell sc = spell.getSpellClass();
			if (sc instanceof Spell.SpellCorrection)
			{
				corrections.add(sc);
			}
			else if (sc instanceof Spell.SpellEffect)
			{
				effects.add(sc);
			}
		}

		if (result != null && result.typeOfHit != Type.MISS)
		{
			for (Spell effect : effects)
			{
				Spell.SpellEffect spell = (Spell.SpellEffect) effect;
				for (Spell correction : corrections)
				{
					((Spell.SpellCorrection) correction).onCorrection(spell);
				}
				spell.onEffectRunToOther(world, runner, result, stack);
			}
		}
	}

	@Override
	public void setVanished()
	{
		this.vanished = true;
	}
}
