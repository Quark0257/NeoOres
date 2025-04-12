package neo_ores.spell.effect;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.main.NeoOresData;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.RayTraceUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public class SpellRain extends SpellEffect
{
	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		return RayTraceUtils.getMissResult();
	}

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (world.isRemote)
		{
			return;
		}

		MinecraftServer server = world.getMinecraftServer();
		if (server != null)
		{
			WorldInfo worldinfo = server.getWorld(0).getWorldInfo();
			int i = (300 + world.rand.nextInt(600)) * 20;
			worldinfo.setCleanWeatherTime(0);
			worldinfo.setRainTime(i);
			worldinfo.setThunderTime(i);
			worldinfo.setRaining(true);
			worldinfo.setThundering(false);

			if (runner instanceof EntityPlayerMP)
			{
				PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
				pmds.addMXP(1L);
			}
		}
	}
}
