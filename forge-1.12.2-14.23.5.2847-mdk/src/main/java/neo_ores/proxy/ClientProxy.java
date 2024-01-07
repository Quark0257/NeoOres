package neo_ores.proxy;

import neo_ores.block.INeoOresBlock;
import neo_ores.main.NeoOres;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.world.World;
import net.minecraftforge.client.EnumHelperClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ClientProxy extends CommonProxy
{
	public static MusicTicker.MusicType sylphied;
	public static MusicTicker.MusicType gnome;
	public static MusicTicker.MusicType salamandra;
	public static MusicTicker.MusicType undine;
	
	public World getClientWorld()
	{
		return (World) (FMLClientHandler.instance().getClient()).world;
	}

	@SuppressWarnings("rawtypes")
	public void setCustomStateModel(Block block)
	{
		if (block instanceof INeoOresBlock)
		{
			IProperty[] props = ((INeoOresBlock) block).setNoRenderProperties();
			if (props != null)
			{
				StateMap map = (new StateMap.Builder()).ignore(props).build();
				ModelLoader.setCustomStateMapper(block, map);
			}
		}
	}
	
	public void init() {
		sylphied = EnumHelperClient.addMusicType("Sylphied", NeoOres.MUSIC_AIR, 3600, 12000);
		gnome = EnumHelperClient.addMusicType("Gnome", NeoOres.MUSIC_EARTH, 3600, 12000);
		salamandra = EnumHelperClient.addMusicType("Salamandra", NeoOres.MUSIC_FIRE, 3600, 12000);
		undine = EnumHelperClient.addMusicType("Undine", NeoOres.MUSIC_WATER, 3600, 12000);
	}
}
