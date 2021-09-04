package neo_ores.spell;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public interface ISpell 
{
	public SpellType getSpellItemType();
	
	//On CORRECTION... if(spell instanceof T) spell.pram etc.
	public default void onCorrection(ISpell spell)
	{
		return;
	}
	
	//On FORM
	public default boolean isCreateSpellEntity()
	{
		return false;
	}
	
	public default boolean isConditional()
	{
		return false;
	}
	
	//On FORM(if Not SELF, return runner = null,if SELF, return result null)
	public default void onSpellRunning(World world, EntityLivingBase runner,@Nullable ItemStack stack,@Nullable RayTraceResult result ,NBTTagCompound spells)
	{
		return;
	}
	
	//On EFFECT(if SELF,(runner != null && result == null))
	public default void onEffectRun(World world, EntityLivingBase runner,RayTraceResult result)
	{
		return;
	}
	
	public static interface ISpellForm extends ISpell
	{
		public default SpellType getSpellItemType() 
		{
			return SpellType.FORM;
		}
		
		public boolean isCreateSpellEntity();
		
		public void onSpellRunning(World world, EntityLivingBase runner,@Nullable ItemStack stack, @Nullable RayTraceResult result,NBTTagCompound spells);
		
	}
	
	public static interface ISpellCorrection extends ISpell
	{
		public default SpellType getSpellItemType() 
		{
			return SpellType.CORRECTION;
		}
		
		public int getLevel();
		
		public void onCorrection(ISpell spell);
	}
	
	public static interface ISpellEffect extends ISpell
	{
		public default SpellType getSpellItemType() 
		{
			return SpellType.EFFECT;
		}
		
		public void onEffectRun(World world, EntityLivingBase runner,RayTraceResult result);
		
	}
	
	public static interface ISpellFormNotEntity extends ISpellForm
	{
		public default boolean isCreateSpellEntity()
		{
			return false;
		}
	}
	
	public static interface ISpellFormSpellEntity extends ISpellForm
	{
		public default boolean isCreateSpellEntity()
		{
			return true;
		}
	}
	
	public static interface ISpellConditional extends ISpell
	{
		public default SpellType getSpellItemType() 
		{
			return SpellType.CONDITIONAL;
		}
	}
}
