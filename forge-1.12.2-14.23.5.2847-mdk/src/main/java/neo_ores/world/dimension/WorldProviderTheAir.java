package neo_ores.world.dimension;

import neo_ores.client.sky.RenderSkyDimensions;
import neo_ores.main.NeoOres;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderTheAir extends WorldProvider
{
	@Override
	public DimensionType getDimensionType() 
	{
		return NeoOres.THE_AIR;
	}
	
	public void init()
    {
        this.biomeProvider = new BiomeProviderSingle(NeoOres.air);
        this.hasSkyLight = true;
        this.setSkyRenderer(new RenderSkyDimensions(false));
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public MusicTicker.MusicType getMusicType()
    {
        return NeoOres.sylphied;
    }

    public IChunkGenerator createChunkGenerator()
    {
        return new ChunkGeneratorTheAir(this.world, this.world.getWorldInfo().isMapFeaturesEnabled(), this.world.getSeed());
    }

    public boolean isSurfaceWorld()
    {
        return true;
    }

    public boolean canCoordinateBeSpawn(int x, int z)
    {
        return false;
    }

    public boolean canRespawnHere()
    {
        return true;
    }
    
    public double getHorizon()
    {
    	return -64.0D;
    }
    
    public boolean shouldMapSpin(String entity, double x, double z, double rotation)
    {
    	return false;
    }
}
