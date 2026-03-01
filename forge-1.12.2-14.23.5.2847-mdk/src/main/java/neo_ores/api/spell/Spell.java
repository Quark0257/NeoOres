package neo_ores.api.spell;

import java.lang.reflect.ParameterizedType;

import javax.annotation.Nullable;

import neo_ores.api.FakePlayerMechanicalMagician;
import neo_ores.main.NeoOres;
import neo_ores.spell.SpellItemInterfaces.ICorrectingBase;
import neo_ores.util.RayTraceUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class Spell
{
	public boolean canEditBlocksBySpells(EntityLivingBase runner, ItemStack stack, World world, @Nullable BlockPos pos, @Nullable EnumFacing face) 
	{
		if (runner instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) runner;
			if (runner.isPotionActive(NeoOres.antigriefing)) 
			{
				return false;
			}
			
			if (!player.capabilities.allowEdit) 
			{
				if (pos != null) 
				{
					player.canPlayerEdit(pos, face, stack);
				}
				else 
				{
					return false;
				}
			}
			
			return true;
		}
		else 
		{
			return ForgeEventFactory.getMobGriefingEvent(world, runner);
		}
	}
	
	public static abstract class SpellForm extends Spell
	{	
		/**
		 * If this is true, it's means this spell need primary form such as Touch or
		 * Self
		 * 
		 * @return
		 */
		public abstract boolean needPrimaryForm();

		/**
		 * If this is true, it's means this spell is passive.
		 * 
		 * @return
		 */
		public abstract boolean needConditional();

		public abstract void onSpellRunningServer(World world, EntityLivingBase runner, ItemStack stack, @Nullable RayTraceResult result, NBTTagCompound spells);

		public void onSpellRunning(World world, EntityLivingBase runner, ItemStack stack, @Nullable RayTraceResult result, NBTTagCompound spells)
		{
			if (!world.isRemote)
			{
				if (runner == null || (runner instanceof FakePlayer && !(runner instanceof FakePlayerMechanicalMagician)))
				{
					return;
				}
				this.onSpellRunningServer(world, runner, stack, result, spells);
			}
		}
	}

	public static abstract class SpellCorrection extends Spell
	{
		protected final int level;

		public SpellCorrection(int level)
		{
			this.level = level;
		}

		public abstract void onCorrection(Spell spell);

		public int getLevel()
		{
			return this.level;
		}

		public void initialize()
		{
		}
	}

	public static abstract class SpellCorrectionSingle<T extends ICorrectingBase> extends SpellCorrection
	{
		public SpellCorrectionSingle(int level)
		{
			super(level);
		}

		@SuppressWarnings("unchecked")
		public void onCorrection(Spell spell)
		{
			try
			{
				Class<T> type = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
				if (type.isInstance(spell))
				{
					this.onApply((T) spell);
				}
			}
			catch (ClassCastException e)
			{
				FMLLog.log.error("Spelling has an unexpected error (a class cast error) and this spell item was skipped!");
				return;
			}
		}

		protected abstract void onApply(T spell);
	}

	public static abstract class SpellEffect extends Spell
	{
		@Nullable
		public abstract RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack);
		
		public RayTraceResult getResultBlockFromEntity(World world, RayTraceResult result, ItemStack stack, boolean offsetUp, boolean offsetDown) 
		{
			if (result != null && result.typeOfHit == Type.ENTITY && result.entityHit instanceof EntityLivingBase) 
			{
				RayTraceResult result2 = this.getResultAsRunningToSelf(world, (EntityLivingBase)result.entityHit, stack);
				if (result2 != null && result2.typeOfHit == RayTraceResult.Type.BLOCK) 
				{
					EnumFacing face = offsetUp ? EnumFacing.UP : EnumFacing.DOWN;
					BlockPos pos = result2.getBlockPos();
					if (offsetDown) 
					{
						pos = pos.add(EnumFacing.DOWN.getDirectionVec());
					}
					if (offsetUp) 
					{
						pos = pos.add(EnumFacing.UP.getDirectionVec());
					}
					return RayTraceUtils.getSimpleResult(pos, face);
				}
				return result;
			}
			return result;
		}

		public void onEffectRunToSelf(World world, EntityLivingBase runner, ItemStack stack)
		{
			RayTraceResult result = this.getResultAsRunningToSelf(world, runner, stack);
			if (result != null)
			{
				this.onEffectRunToOther(world, runner, result, stack);
			}
		}

		public abstract void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack);
	}

	public static abstract class SpellFormNotEntity extends SpellForm
	{
		public boolean needPrimaryForm()
		{
			return false;
		}
	}

	public static abstract class SpellFormSpellEntity extends SpellForm
	{
		public boolean needPrimaryForm()
		{
			return true;
		}
	}

	public static abstract class SpellConditional extends Spell
	{
		public abstract boolean checkRunnableAndRun(Event event, World world, @Nullable EntityLivingBase runner, ItemStack stack, NBTTagCompound spells, long mana);

		public void initialize()
		{
		}
	}
}
