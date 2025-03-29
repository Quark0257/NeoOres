package neo_ores.spell.conditional;

import neo_ores.api.spell.Spell.SpellConditional;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SpellDamaged extends SpellConditional {
	@Override
	public boolean checkRunnableAndRun(Event event, World world, EntityLivingBase runner, ItemStack stack, NBTTagCompound spells, long mana)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
