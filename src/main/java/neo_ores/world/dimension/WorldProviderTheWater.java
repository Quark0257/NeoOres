package neo_ores.world.dimension;

import neo_ores.main.NeoOres;
import net.minecraft.init.Biomes;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderTheWater extends WorldProvider
{
	@Override
	public DimensionType getDimensionType() 
	{
		return NeoOres.THE_WATER;
	}
	
    public void init()
    {
        this.biomeProvider = new BiomeProviderSingle(Biomes.MESA);
        this.hasSkyLight = false;
    }

    public IChunkGenerator createChunkGenerator()
    {
        return new ChunkGeneratorTheWater(this.world, this.world.getWorldInfo().isMapFeaturesEnabled(), this.world.getSeed());
    }

    public boolean isSurfaceWorld()
    {
        return false;
    }

    public boolean canCoordinateBeSpawn(int x, int z)
    {
        return false;
    }

    public float calculateCelestialAngle(long worldTime, float partialTicks)
    {
        return 0.0F;
    }

    public boolean canRespawnHere()
    {
        return false;
    }
    
    public double getHorizon()
    {
    	return 256.0D;
    }
}
