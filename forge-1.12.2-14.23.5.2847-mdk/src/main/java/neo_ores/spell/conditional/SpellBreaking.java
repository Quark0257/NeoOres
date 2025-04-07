package neo_ores.spell.conditional;

import neo_ores.util.RayTraceUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SpellBreaking extends SpellConditionalBase
{
	@Override
	public RayTraceResult getTarget(Event event, World world, EntityLivingBase runner, ItemStack stack, NBTTagCompound spells)
	{
		if (event instanceof BreakEvent)
		{
			BreakEvent be = (BreakEvent) event;
			if (!event.isCanceled())
			{
				return RayTraceUtils.getSimpleResult(be.getPos(), null);
			}
		}
		return null;
	}
}
