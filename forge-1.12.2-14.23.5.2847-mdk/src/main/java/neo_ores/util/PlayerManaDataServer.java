package neo_ores.util;

import java.util.Random;

import neo_ores.api.IMagicExperienceContainer;
import neo_ores.api.spell.SpellItem;
import neo_ores.data.PlayerDataStorage;
import neo_ores.main.NeoOres;
import neo_ores.main.Reference;
import neo_ores.packet.PacketManaDataToClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerManaDataServer
{
	public final float mxpBarCap = 1.007F;
	private final PlayerDataStorage eds;
	private final Random random = new Random();
	private final EntityPlayerMP playermp;
	private boolean runMXP;

	public PlayerManaDataServer(EntityPlayerMP player)
	{
		this.playermp = player;
		this.eds = new PlayerDataStorage(player);
		runMXP = false;
	}

	public void setMana(long value)
	{
		if (value > this.getMaxMana())
			this.eds.setValue("mana", this.getMaxMana());
		else if (value >= 0)
			this.eds.setValue("mana", value);
		if (!runMXP)
			this.sendToClient();
	}

	public void setLevel(long value)
	{
		if (value >= 0)
			this.eds.setValue("level", value);
		if (!runMXP)
			this.sendToClient();
	}

	public void setMaxManaMag(float magnificant)
	{
		this.eds.setValue("maginificantMaxMana", magnificant);
	}

	public void setMaxManaAdd(long addtion)
	{
		this.eds.setValue("additionMaxMana", addtion);
	}

	public float getMaxManaMag()
	{
		return this.eds.getValue("maginificantMaxMana");
	}

	public long getMaxManaAdd()
	{
		return (long) this.eds.getValue("additionMaxMana");
	}

	public void setMXP(long value)
	{
		runMXP = true;
		long gotLevel = this.getLevel();
		float gotMXPBar = this.getMXPBar();
		long gotMaxMana = this.getTrueMaxMana();
		long level = 0;
		long maxMana = 0;
		long magicpoint = 0;
		long mxp = value;
		while (mxp > (long) (16.0F + gotMXPBar))
		{
			if ((16.0F + gotMXPBar) > Long.MAX_VALUE)
			{
				return;
			}

			mxp -= (long) (16.0F + gotMXPBar);
			level++;

			double all_required_magic_point = 0.0D;
			for (SpellItem spell : SpellUtils.registry)
			{
				all_required_magic_point += spell.getTier();
			}

			double pro = 10.0D;
			double a = 5.5D / pro / all_required_magic_point;
			double b = 5.0D;
			double compares = (Math.atan(a * (gotLevel + level) - b) + Math.atan(b)) / (Math.atan(b) + Math.PI / 2);
			if ((gotLevel + level) % pro == 0 && this.random.nextDouble() > compares)
			{
				magicpoint++;
			}

			if (gotMaxMana + maxMana + ((gotLevel + level) * ((gotLevel + level) * 0.000007F) + 1.0F) > Long.MAX_VALUE)
			{
				maxMana = Long.MAX_VALUE - gotMaxMana;
			}
			else
			{
				maxMana += (long) ((gotLevel + level) * ((gotLevel + level) * 0.000007F) + 1.0F);
			}

			if ((gotLevel + level) * 270.0F - 572442.63F > Long.MAX_VALUE)
			{
				gotMXPBar = Long.MAX_VALUE;
			}
			else if (gotLevel + level > 3000)
			{
				gotMXPBar = (long) ((gotLevel + level) * 270.0F - 572442.63F);
			}
			else if (gotLevel + level > 2000)
			{
				gotMXPBar = (long) ((gotLevel + level) * ((gotLevel + level) * ((gotLevel + level) * 0.00001F)) - 32442.63F);
			}
			else if (gotLevel + level > 1000)
			{
				gotMXPBar = (long) ((gotLevel + level) * ((gotLevel + level) * ((gotLevel + level) * ((gotLevel + level) * ((gotLevel + level) * 0.0000000000015F)))) - 442.63F);
			}
			else
			{
				gotMXPBar *= this.mxpBarCap;
			}
		}

		this.setMXPBar(gotMXPBar);
		this.addLevel(level);
		this.addMagicPoint(magicpoint);
		this.addTrueMaxMana(maxMana);
		if (level > 0)
		{
			this.setMana(this.getMaxMana());
		}
		this.eds.setValue("mxp", mxp);

		this.sendToClient();
		runMXP = false;
	}

	public void setMagicPoint(long value)
	{
		if (value >= 0)
			eds.setValue("magicpoint", value);
		if (!runMXP)
			this.sendToClient();
	}

	public void setTrueMaxMana(long value)
	{
		if (value >= 0)
			this.eds.setValue("maxMana", value);
		if (!runMXP)
			this.sendToClient();
	}

	private void setMXPBar(float f)
	{
		this.eds.setValue("mxp_bar", f);
	}

	private float getMXPBar()
	{
		return this.getLevel() > 0 ? this.eds.getValue("mxp_bar") : 1.007F;
	}

	public long getMaxMXP()
	{
		return (16.0F + this.getMXPBar()) <= Long.MAX_VALUE ? (long) (16.0F + this.getMXPBar()) : Long.MAX_VALUE;
	}

	public long getMana()
	{
		return this.eds.getValue("mana") < Long.MAX_VALUE ? (long) this.eds.getValue("mana") : Long.MAX_VALUE;
	}

	public long getLevel()
	{
		return this.eds.getValue("level") < Long.MAX_VALUE ? (long) this.eds.getValue("level") : Long.MAX_VALUE;
	}

	public long getMXP()
	{
		return this.eds.getValue("mxp") < Long.MAX_VALUE ? (long) this.eds.getValue("mxp") : Long.MAX_VALUE;
	}

	public long getTrueMaxMana()
	{
		return this.eds.getValue("maxMana") < Long.MAX_VALUE ? (long) this.eds.getValue("maxMana") : Long.MAX_VALUE;
	}

	public long getMaxMana()
	{
		long value = (long) ((this.getTrueMaxMana() + this.getMaxManaAdd()) * (1 + this.getMaxManaMag()));
		return value < Long.MAX_VALUE ? value : Long.MAX_VALUE;
	}

	public long getMagicPoint()
	{
		return this.eds.getValue("magicpoint") < Long.MAX_VALUE ? (long) this.eds.getValue("magicpoint") : Long.MAX_VALUE;
	}

	public void addMana(long value)
	{
		if (value > Long.MAX_VALUE - (float) this.getMana())
			this.setMana(Long.MAX_VALUE);
		else if (value + this.getMana() >= 0)
			this.setMana(value + this.getMana());
		else
			this.setMana(0);
	}

	public void addLevel(long value)
	{
		if (value > Long.MAX_VALUE - this.getLevel())
			this.setLevel(Long.MAX_VALUE);
		else if (value + this.getLevel() >= 0)
			this.setLevel(value + this.getLevel());
		else
			this.setLevel(0);
	}

	public void addMXP(long value)
	{
		if (this.playermp instanceof FakePlayer)
		{
			if (this.playermp instanceof IMagicExperienceContainer)
			{
				((IMagicExperienceContainer) this.playermp).addMagicXp(value);
			}
			return;
		}
		if (value > Long.MAX_VALUE - this.getMXP())
			this.setMXP(Long.MAX_VALUE);
		else if (value + this.getMXP() >= 0)
			this.setMXP(value + this.getMXP());
		else
			this.setMXP(0);
	}

	public void addTrueMaxMana(long value)
	{
		if (value > Long.MAX_VALUE - this.getTrueMaxMana())
			this.setTrueMaxMana(Long.MAX_VALUE);
		else if (value + this.getTrueMaxMana() >= 0)
			this.setTrueMaxMana(value + this.getTrueMaxMana());
		else
			this.setTrueMaxMana(0);
	}

	public void addMagicPoint(long value)
	{
		if (value > Long.MAX_VALUE - this.getMagicPoint())
			this.setMagicPoint(Long.MAX_VALUE);
		else if (value + this.getMagicPoint() >= 0)
			this.setMagicPoint(value + this.getMagicPoint());
		else
			this.setMagicPoint(0);
	}

	public void sendToClient()
	{
		StudyItemManagerServer sim = new StudyItemManagerServer(playermp);

		NBTTagCompound packetNBT = new NBTTagCompound();
		NBTTagCompound neo_ores = new NBTTagCompound();
		NBTTagCompound magicData = new NBTTagCompound();
		NBTTagCompound studyData = sim.alreadyGottenList().copy();

		magicData.setLong(SpellUtils.NBTTagUtils.LEVEL, this.getLevel());
		magicData.setLong(SpellUtils.NBTTagUtils.MXP, this.getMXP());
		magicData.setLong(SpellUtils.NBTTagUtils.MANA, this.getMana());
		magicData.setLong(SpellUtils.NBTTagUtils.MAX_MANA, this.getMaxMana());
		magicData.setLong(SpellUtils.NBTTagUtils.MAX_MXP, this.getMaxMXP());
		magicData.setLong(SpellUtils.NBTTagUtils.MAGIC_POINT, this.getMagicPoint());

		neo_ores.setTag(SpellUtils.NBTTagUtils.MAGIC, (NBTBase) magicData);
		neo_ores.setTag(SpellUtils.NBTTagUtils.STUDY, (NBTBase) studyData);

		packetNBT.setInteger("playerID", playermp.getEntityId());
		packetNBT.setTag(Reference.MOD_ID, (NBTBase) neo_ores);

		PacketManaDataToClient pmdc = new PacketManaDataToClient(packetNBT);
		NeoOres.PACKET.sendToAll((IMessage) pmdc);
	}
}
