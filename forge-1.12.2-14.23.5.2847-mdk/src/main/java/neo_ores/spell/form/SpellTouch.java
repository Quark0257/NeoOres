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
	public void onSpellRunning(World world, EntityLivingBase runner, ItemStack stack, RayTraceResult result, NBTTagCompound spells)
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

		if (result != null && result.typeOfHit == Type.ENTITY)
		{
			for (Spell effect : effects)
			{
				Spell.SpellEffect spell = (Spell.SpellEffect) effect;
				for (Spell correction : corrections)
				{
					((Spell.SpellCorrection) correction).onCorrection(spell);
				}
				spell.onEffectRunToSelfAndOther(world, runner, result, stack);
				spell.onEffectRunToOther(world, result, stack);
			}
		}
		else
		{
			RayTraceResult blockresult = result;
			if (blockresult == null && runner instanceof EntityPlayer)
			{
				blockresult = SpellUtils.rayTrace(world, (EntityPlayer) runner, this.liquid);
			}

			if (blockresult != null)
			{
				for (Spell effect : effects)
				{
					Spell.SpellEffect spell = (Spell.SpellEffect) effect;
					for (Spell correction : corrections)
					{
						((Spell.SpellCorrection) correction).onCorrection(spell);
					}
					spell.onEffectRunToSelfAndOther(world, runner, blockresult, stack);
					spell.onEffectRunToOther(world, blockresult, stack);
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
}
