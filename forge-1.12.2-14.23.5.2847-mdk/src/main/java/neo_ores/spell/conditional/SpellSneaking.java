package neo_ores.spell.conditional;

import neo_ores.event.SneakEvent;
import neo_ores.util.RayTraceUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SpellSneaking extends SpellConditionalBase
{
	@Override
	public RayTraceResult getTarget(Event event, World world, EntityLivingBase runner, ItemStack stack, NBTTagCompound spells)
	{
		if (event instanceof SneakEvent)
		{
			if (!event.isCanceled())
			{
				return RayTraceUtils.getMissResult();
			}
		}
		return null;
	}
}
