package neo_ores.world.dimension;

import neo_ores.main.NeoOres;
import neo_ores.proxy.ClientProxy;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderTheWater extends WorldProvider
{
	@Override
	public DimensionType getDimensionType()
	{
		return NeoOres.THE_WATER;
	}

	public void init()
	{
		this.biomeProvider = new BiomeProviderSingle(NeoOres.water);
		this.hasSkyLight = false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public MusicTicker.MusicType getMusicType()
	{
		return ClientProxy.undine;
	}

	protected void generateLightBrightnessTable()
	{
		float f = 0.05F;

		for (int i = 0; i <= 15; ++i)
		{
			float f1 = 1.0F - (float) i / 15.0F;
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

	public boolean canRespawnHere()
	{
		return false;
	}

	public double getHorizon()
	{
		return 256.0D;
	}

	public float calculateCelestialAngle(long worldTime, float partialTicks)
	{
		return 0.0F;
	}

	public boolean shouldMapSpin(String entity, double x, double z, double rotation)
	{
		return false;
	}
}
