package neo_ores.proxy;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class CommonProxy
{
	public World getClientWorld()
	{
		return null;
	}

	public void setCustomStateModel(Block block)
	{
	}
	
	public void setCustomStateModel(Fluid fluid) 
	{
	}
	
	public void init() {}
}
