package neo_ores.block.properties;

import net.minecraft.util.IStringSerializable;

public enum PedestalTiers implements IStringSerializable
{
	TIER1(1, false), TIER2(2, false), TIER3(3, false), TIER4(4, false), TIER5(5, false), TIER6(6, false), TIER7(7, false), TIER8(8, false), TIER1_s(1, true), TIER2_s(2, true), TIER3_s(3, true),
	TIER4_s(4, true), TIER5_s(5, true), TIER6_s(6, true), TIER7_s(7, true), TIER8_s(8, true);

	private int meta;
	private int tier;
	private boolean canSuck;

	PedestalTiers(int tier, boolean canSuck)
	{
		this.tier = tier;
		this.canSuck = canSuck;
		this.meta = canSuck ? tier + 7 : tier - 1;
	}

	public int getTier()
	{
		return tier;
	}

	public boolean canSuck()
	{
		return canSuck;
	}

	public int getMeta()
	{
		return this.meta;
	}

	public static PedestalTiers getFromMeta(int meta)
	{
		for (int i = 0; i < PedestalTiers.values().length; i++)
		{
			if (meta == PedestalTiers.values()[i].meta)
			{
				return PedestalTiers.values()[i];
			}
		}

		return PedestalTiers.TIER1;
	}

	@Override
	public String getName()
	{
		return String.valueOf(meta);
	}
}
