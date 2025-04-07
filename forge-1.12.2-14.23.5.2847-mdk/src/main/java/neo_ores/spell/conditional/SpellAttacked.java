package neo_ores.spell.conditional;

import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SpellAttacked extends SpellConditionalBase
{
	@Override
	public RayTraceResult getTarget(Event event, World world, EntityLivingBase runner, ItemStack stack, NBTTagCompound spells)
	{
		if (event instanceof LivingHurtEvent)
		{
			if (SpellUtils.canRunPassiveSpell(((LivingHurtEvent) event).getSource()) && ((LivingHurtEvent) event).getEntity() == runner && !event.isCanceled())
			{
				return RayTraceUtils.getSimpleResult(((LivingHurtEvent) event).getSource().getTrueSource());
			}
		}
		return null;
	}
}
