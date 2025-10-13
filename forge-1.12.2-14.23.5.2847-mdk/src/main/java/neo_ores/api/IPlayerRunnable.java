package neo_ores.api;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerRunnable
{
	public void run(EntityPlayer target);
	
	public boolean isRunnable(EntityPlayer target);
	
	public default UUID getKey() 
	{
		return UUID.randomUUID();
	}
}
