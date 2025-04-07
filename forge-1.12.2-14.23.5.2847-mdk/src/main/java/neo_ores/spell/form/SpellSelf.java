package neo_ores.spell.form;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.spell.Spell;
import neo_ores.api.spell.SpellItem;
import neo_ores.api.spell.Spell.SpellFormNotEntity;
import neo_ores.spell.SpellItemInterfaces.HasOffsetDown;
import neo_ores.spell.SpellItemInterfaces.HasOffsetUp;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class SpellSelf extends SpellFormNotEntity implements HasOffsetDown, HasOffsetUp
{
	private boolean offsetDown = false;
	private boolean offsetUp = false;
	
	@Override
	public boolean needConditional()
	{
		return false;
	}

	@Override
	public void onSpellRunningServer(World world, EntityLivingBase runner, ItemStack stack, RayTraceResult result, NBTTagCompound spells)
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
		
		for (Spell effect : effects)
		{
			Spell.SpellEffect spell = (Spell.SpellEffect) effect;
			for (Spell correction : corrections)
			{
				((Spell.SpellCorrection) correction).onCorrection(spell);
			}
			if (this.offsetDown || this.offsetUp) {
				RayTraceResult spellTrace = spell.getResultAsRunningToSelf(world, runner, stack);
				if (spellTrace != null && spellTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
					EnumFacing face = this.offsetUp ? EnumFacing.UP : EnumFacing.DOWN;
					BlockPos pos = spellTrace.getBlockPos();
					if (this.offsetDown) {
						pos = pos.add(EnumFacing.DOWN.getDirectionVec());
					}
					if (this.offsetUp) {
						pos = pos.add(EnumFacing.UP.getDirectionVec());
					}
					spellTrace = RayTraceUtils.getSimpleResult(pos, face);
					spell.onEffectRunToOther(world, runner, spellTrace, stack);
					continue;
				}
			}
			spell.onEffectRunToSelf(world, runner, stack);
		}
	}

	@Override
	public void setOffsetUp()
	{
		this.offsetUp = true;
	}

	@Override
	public void setOffsetDown()
	{
		this.offsetDown = true;
	}

}
