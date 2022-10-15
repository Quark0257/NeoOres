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

    protected void generateLightBrightnessTable()
    {
        float f = 0.3F;

        for (int i = 0; i <= 15; ++i)
        {
            float f1 = 1.0F - (float)i / 15.0F;
            this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
        }
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
