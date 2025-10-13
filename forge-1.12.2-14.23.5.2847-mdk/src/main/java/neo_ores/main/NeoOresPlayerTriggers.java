package neo_ores.main;

import java.util.Arrays;
import java.util.List;

import neo_ores.api.IDialogReward;
import neo_ores.api.IPlayerRunnable;
import neo_ores.api.PlayerTrigger;
import neo_ores.config.NeoOresConfig;
import neo_ores.util.NumberUtils;
import neo_ores.util.PlayerMagicDataClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class NeoOresPlayerTriggers
{
	public static class RewardMxp implements IDialogReward
	{
		private final long amount;

		public RewardMxp(long value)
		{
			this.amount = value;
		}

		@Override
		public String getDesc()
		{
			return "reward.mxp";
		}

		@Override
		public void takeRewardClient(EntityPlayer player)
		{
			PlayerMagicDataClient pmdc = NeoOresData.getPMDC(EntityPlayer.getUUID(player.getGameProfile()));
			pmdc.addMXP(this.amount);
			pmdc.sendToOtherSide(null);
		}

		public Object[] getFormats()
		{
			return new Object[] { NumberUtils.getPrefixedNumber(this.amount, 3) };
		}
	}

	public static class RewardMagicPoint implements IDialogReward
	{
		private final long amount;

		public RewardMagicPoint(long value)
		{
			this.amount = value;
		}

		@Override
		public String getDesc()
		{
			return "reward.magic_point";
		}

		@Override
		public void takeRewardClient(EntityPlayer player)
		{
			PlayerMagicDataClient pmdc = NeoOresData.getPMDC(EntityPlayer.getUUID(player.getGameProfile()));
			pmdc.addMagicPoint(this.amount);
			pmdc.sendToOtherSide(null);
		}

		public Object[] getFormats()
		{
			return new Object[] { NumberUtils.getPrefixedNumber(this.amount, 3) };
		}
	}

	public static final PlayerTrigger earthDimVisit = new PlayerTrigger("earth_dim_visit", EarthDimVisit.class, Arrays.asList(new RewardMxp(1000L)),
			new ResourceLocation(Reference.MOD_ID, "earth_dim_visit"));
	public static final PlayerTrigger fireDimVisit = new PlayerTrigger("fire_dim_visit", FireDimVisit.class, Arrays.asList(new RewardMxp(1000000L)),
			new ResourceLocation(Reference.MOD_ID, "fire_dim_visit"));
	public static final PlayerTrigger airDimVisit = new PlayerTrigger("air_dim_visit", AirDimVisit.class, Arrays.asList(new RewardMxp(100000L)),
			new ResourceLocation(Reference.MOD_ID, "air_dim_visit"));
	public static final PlayerTrigger waterDimVisit = new PlayerTrigger("water_dim_visit", WaterDimVisit.class, Arrays.asList(new RewardMxp(10000L)),
			new ResourceLocation(Reference.MOD_ID, "water_dim_visit"));

	public static class EarthDimVisit implements IPlayerRunnable
	{
		@Override
		public void run(EntityPlayer target)
		{
			// TODO
		}

		@Override
		public boolean isRunnable(EntityPlayer target)
		{
			if (!target.world.isRemote)
			{
				return target.world.provider.getDimension() == NeoOresConfig.dim.dimearth;
			}
			return false;
		}

	}
	
	public static class WaterDimVisit implements IPlayerRunnable
	{
		@Override
		public void run(EntityPlayer target)
		{
			// TODO
		}

		@Override
		public boolean isRunnable(EntityPlayer target)
		{
			if (!target.world.isRemote)
			{
				return target.world.provider.getDimension() == NeoOresConfig.dim.dimwater;
			}
			return false;
		}

	}
	
	public static class AirDimVisit implements IPlayerRunnable
	{
		@Override
		public void run(EntityPlayer target)
		{
			// TODO
		}

		@Override
		public boolean isRunnable(EntityPlayer target)
		{
			if (!target.world.isRemote)
			{
				return target.world.provider.getDimension() == NeoOresConfig.dim.dimair;
			}
			return false;
		}

	}
	
	public static class FireDimVisit implements IPlayerRunnable
	{
		@Override
		public void run(EntityPlayer target)
		{
			// TODO
		}

		@Override
		public boolean isRunnable(EntityPlayer target)
		{
			if (!target.world.isRemote)
			{
				return target.world.provider.getDimension() == NeoOresConfig.dim.dimfire;
			}
			return false;
		}

	}

	public static final List<PlayerTrigger> registry = Arrays.asList(earthDimVisit, fireDimVisit, airDimVisit, waterDimVisit);
}
