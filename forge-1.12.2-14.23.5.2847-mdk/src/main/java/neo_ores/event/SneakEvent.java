package neo_ores.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;

public class SneakEvent extends LivingEvent
{
	public SneakEvent(EntityLivingBase entity)
	{
		super(entity);
	}
}
