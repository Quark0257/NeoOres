package neo_ores.spell.form;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.spell.Spell;
import neo_ores.api.spell.SpellItem;
import neo_ores.api.spell.Spell.SpellFormNotEntity;
import neo_ores.spell.SpellItemInterfaces.HasChanceLiquid;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class SpellTouch extends SpellFormNotEntity implements HasChanceLiquid
{
	private boolean liquid = false;
	
	@Override
	public void onSpellRunning(World world, EntityLivingBase runner, ItemStack stack,RayTraceResult result, NBTTagCompound spells) 
	{
		List<SpellItem> corrections = new ArrayList<SpellItem>();
		List<SpellItem> effects = new ArrayList<SpellItem>();
		
		for(SpellItem spell : SpellUtils.getListInitialized(SpellUtils.getListFromItemStackNBT(spells)))
		{
			if(spell.getSpellClass() instanceof Spell.SpellCorrection)
			{
				corrections.add(spell);
			}
			else if(spell.getSpellClass() instanceof Spell.SpellEffect)
			{
				effects.add(spell);
			}
		}
		
		if(result != null && result.typeOfHit == Type.ENTITY)
		{
			for(SpellItem effect : effects)
			{
				Spell.SpellEffect spell = (Spell.SpellEffect)effect.getSpellClass();
				for(SpellItem correction : corrections)
				{
					((Spell.SpellCorrection)correction.getSpellClass()).onCorrection(spell);
				}
				spell.onEffectRunToSelfAndOther(world, runner, result,stack);
				spell.onEffectRunToOther(world, result,stack);
			}
		}
		else
		{
			RayTraceResult blockresult = result;
			if(blockresult == null && runner instanceof EntityPlayer)
			{
				blockresult = SpellUtils.rayTrace(world, (EntityPlayer)runner, this.liquid);
			}
			
			if(blockresult != null)
			{
				for(SpellItem effect : effects)
				{
					Spell.SpellEffect spell = (Spell.SpellEffect)effect.getSpellClass();
					for(SpellItem correction : corrections)
					{
						((Spell.SpellCorrection)correction.getSpellClass()).onCorrection(spell);
					}
					spell.onEffectRunToSelfAndOther(world, runner, blockresult,stack);
					spell.onEffectRunToOther(world, blockresult,stack);
				}
			}
		}
	}

	@Override
	public void setSupport() 
	{
		this.liquid = true;
	}

	@Override
	public boolean needConditional() 
	{
		return false;
	}

	@Override
	public void initialize() {
		liquid = false;
	}
}
