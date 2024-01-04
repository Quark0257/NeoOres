package neo_ores.proxy;

import neo_ores.block.INeoOresBlock;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ClientProxy extends CommonProxy
{
	public World getClientWorld() 
	{
	    return (World)(FMLClientHandler.instance().getClient()).world;
	}
	
	@SuppressWarnings("rawtypes")
	public void setCustomStateModel(Block block) {
		if(block instanceof INeoOresBlock) {
			IProperty[] props = ((INeoOresBlock)block).setNoRenderProperties();
			if(props != null) {
				StateMap map = (new StateMap.Builder()).ignore(props).build();
				ModelLoader.setCustomStateMapper(block, map);
			}
		}
	}
}
