package neo_ores.spell.form;

import neo_ores.api.spell.Spell.SpellForm;
import neo_ores.main.NeoOresData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class SpellWornTick extends SpellForm implements IPassiveSpell
{
	private int slot = -1;
	private long mana = 0L;
	
	@Override
	public boolean needPrimaryForm()
	{
		return true;
	}

	@Override
	public boolean needConditional()
	{
		return true;
	}

	@Override
	public void onSpellRunningServer(World world, EntityLivingBase runner, ItemStack stack, RayTraceResult result, NBTTagCompound spells)
	{
		NeoOresData.instance.addPassiveSpell(runner, this.slot, stack, spells, this.mana);
	}

	@Override
	public void setSlot(int slot)
	{
		this.slot = slot;
	}

	@Override
	public int getSlot()
	{
		return this.slot;
	}

	@Override
	public void setMana(long mana)
	{
		this.mana = mana;
	}

	@Override
	public long getMana()
	{
		return this.mana;
	}
}
