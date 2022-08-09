package neo_ores.api.spell;

import java.lang.reflect.ParameterizedType;

import javax.annotation.Nullable;

import neo_ores.spell.SpellItemInterfaces.ICorrectingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public abstract class Spell
{
	/*
	 * Before spell executes, this runs once.
	 */
	public abstract void initialize();
	
	public static abstract class SpellForm extends Spell
	{		
		public abstract boolean needPrimaryForm();
		
		public abstract boolean needConditional();
		
		public abstract void onSpellRunning(World world, @Nullable EntityLivingBase runner,ItemStack stack, @Nullable RayTraceResult result,NBTTagCompound spells);
		
	}
	
	public static abstract class SpellCorrection extends Spell
	{
		protected final int level;
		
		public SpellCorrection(int level)
		{
			this.level= level;
		}
		
		public abstract void onCorrection(Spell spell);
		
		public int getLevel()
		{
			return this.level;
		}
		
		public void initialize() {}
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
				if(type.isInstance(spell))
				{
					this.onApply((T)spell);
				}
			}
			catch(ClassCastException e)
			{
				System.out.println("Spelling has an unexpected error (a class cast error) and this spell item was skipped!");
				return;
			}
		}
		
		protected abstract void onApply(T spell);
	}
	
	public static abstract class SpellEffect extends Spell
	{		
		public abstract void onEffectRunToSelf(World world, EntityLivingBase runner, ItemStack stack);
		
		public abstract void onEffectRunToOther(World world, RayTraceResult result, ItemStack stack);
		
		public abstract void onEffectRunToSelfAndOther(World world, EntityLivingBase runner,RayTraceResult result, ItemStack stack);
		
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
		public abstract boolean isSpellRunnable(World world, @Nullable EntityLivingBase runner,ItemStack stack, @Nullable RayTraceResult result,NBTTagCompound spells);
		
		public void initialize() {}
	}
}
