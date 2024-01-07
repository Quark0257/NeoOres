package neo_ores.world.gen.structures;

import java.util.Random;

import net.minecraft.util.ResourceLocation;

public class StructureMobSpawnerUtils
{
	public static final ResourceLocation[] resources = new ResourceLocation[] { new ResourceLocation("husk"), new ResourceLocation("vindication_illager"), new ResourceLocation("vex"),
			new ResourceLocation("spider"), new ResourceLocation("cave_spider"), new ResourceLocation("magma_cube"), new ResourceLocation("silverfish"),
			new ResourceLocation("guardian"), new ResourceLocation("zombie"), };

	public static ResourceLocation getMobId(Random rand)
	{
		return resources[rand.nextInt(resources.length)];
	}
}
