package neo_ores.event;

import neo_ores.main.Reference;
import neo_ores.util.NeoOresServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class NeoOresWorldEvents
{
	@SubscribeEvent
	public static void onWorldSaved(WorldEvent.Save event) 
	{
		NeoOresServer.onWorldSaved(event);
	}
	
	@SubscribeEvent
	public static void onServerTick(ServerTickEvent event) 
	{
		if(!NeoOresServer.isLoaded())
			return;
		if(event.phase == Phase.START) 
		{
			NeoOresServer.instance.update();
		}
	}
}
