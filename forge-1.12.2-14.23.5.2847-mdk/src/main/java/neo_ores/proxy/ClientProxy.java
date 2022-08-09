package neo_ores.proxy;

import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ClientProxy extends CommonProxy
{
	public World getClientWorld() 
	{
	    return (World)(FMLClientHandler.instance().getClient()).world;
	}
}
