package neo_ores.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import neo_ores.api.spell.SpellItem;
import neo_ores.config.NeoOresConfig;
import neo_ores.main.NeoOres;
import neo_ores.packet.PacketMagicDataToClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerMagicData
{
	private boolean needSaving;
	private boolean clientChanged;
	protected boolean needSending;
	private final float mxpBarCap = 1.007F;
	protected static final Random random = new Random();
	protected boolean runMXP;
	protected final boolean isFake;
	protected long mana;
	protected float maginificantMaxMana;
	protected long additionMaxMana;
	protected long magicpoint;
	protected long maxMana;
	protected float mxp_bar;
	protected long level;
	protected long mxp;
	protected final Map<String, Boolean> studies;

	public PlayerMagicData(boolean isFake)
	{
		this.isFake = isFake;
		this.needSaving = false;
		this.needSending = false;
		this.additionMaxMana = 0L;
		this.maginificantMaxMana = 0.0F;
		this.mana = 0L;
		this.magicpoint = 0L;
		this.maxMana = this.isFake ? (long)NeoOresConfig.magic.liquid_mana_convert_rate : 0L;
		this.level = 0L;
		this.mxp_bar = 0.0F;
		this.mxp = 0L;
		this.clientChanged = false;
		this.studies = new HashMap<String, Boolean>();
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasKey("magicData"))
		{
			NBTTagCompound magic = nbt.getCompoundTag("magicData");
			this.additionMaxMana = magic.getLong("additionMaxMana");
			this.maginificantMaxMana = magic.getFloat("maginificantMaxMana");
			this.mana = magic.getLong("mana");
			this.magicpoint = magic.getLong("magicpoint");
			this.maxMana = magic.getLong("maxMana");
			this.level = magic.getLong("level");
			this.mxp_bar = magic.getFloat("mxp_bar");
			this.mxp = magic.getLong("mxp");
		}

		if (nbt.hasKey("studyData"))
		{
			NBTTagCompound study = nbt.getCompoundTag("studyData");
			for (String modid : study.getKeySet())
			{
				NBTTagList list = study.getTagList(modid, 8);
				for (int i = 0; i < list.tagCount(); i++)
				{
					String id = ((NBTTagString) list.get(i)).getString();
					this.studies.put(modid + "@" + id, true);
				}
			}
		}

		this.clientChanged = nbt.getBoolean("isChanged");

		this.markSending();
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		NBTTagCompound magic = new NBTTagCompound();
		magic.setLong("additionMaxMana", this.additionMaxMana);
		magic.setFloat("maginificantMaxMana", this.maginificantMaxMana);
		magic.setLong("mana", this.mana);
		magic.setLong("magicpoint", this.magicpoint);
		magic.setLong("maxMana", this.maxMana);
		magic.setLong("level", this.level);
		magic.setFloat("mxp_bar", this.mxp_bar);
		magic.setLong("mxp", this.mxp);
		nbt.setTag("magicData", magic);
		NBTTagCompound study = new NBTTagCompound();
		for (Map.Entry<String, List<String>> entry : this.alreadyGottenList().entrySet())
		{
			if (!entry.getValue().isEmpty())
			{
				NBTTagList list = new NBTTagList();
				for (String id : entry.getValue())
				{
					// studyData.getTagList(entry.getKey(), 8);
					list.appendTag(new NBTTagString(id));
				}
				study.setTag(entry.getKey(), list);
			}
		}
		nbt.setTag("studyData", study);
		return nbt;
	}

	public void setMana(long value)
	{
		if (value > this.getMaxMana())
			this.mana = this.getMaxMana();
		else if (value >= 0L)
			this.mana = value;
		if (!this.runMXP)
		{
			// System.out.println("markSending");
			this.markDirty();
			this.markSending();
		}
	}

	public void setLevel(long value)
	{
		if (value >= 0L)
			this.level = value;
		if (!this.runMXP)
		{
			this.markDirty();
			this.markSending();
		}
	}

	public void setMaxManaMag(float magnificant)
	{
		this.maginificantMaxMana = magnificant;
	}

	public void setMaxManaAdd(long addtion)
	{
		this.additionMaxMana = addtion;
	}

	public float getMaxManaMag()
	{
		return this.maginificantMaxMana;
	}

	public long getMaxManaAdd()
	{
		return this.additionMaxMana;
	}

	public void setMXP(long value)
	{
		if (this.isFake)
		{
			this.mxp = value;
			return;
		}

		if (this.level <= 0L)
			return;
		this.runMXP = true;
		long gotLevel = this.getLevel();
		float gotMXPBar = this.getMXPBar();
		long gotMaxMana = this.getTrueMaxMana();
		long level = 0;
		long maxMana = 0;
		long magicpoint = 0;
		long mxp = value;
		double all_required_magic_point = 0.0D;
		for (SpellItem spell : SpellUtils.registry)
		{
			all_required_magic_point += spell.getTier();
		}
		while (mxp > (long) (16.0F + gotMXPBar))
		{
			if ((16.0F + gotMXPBar) > Long.MAX_VALUE)
			{
				return;
			}

			mxp -= (long) (16.0F + gotMXPBar);
			level++;

			double pro = 10.0D;
			double a = 5.5D / pro / all_required_magic_point;
			double b = 5.0D;
			double at = Math.atan(b);
			double compares = (Math.atan(a * (gotLevel + level) - b) + at) / (at + Math.PI * 0.5D);
			if ((gotLevel + level) % pro == 0 && random.nextDouble() > compares)
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
			else if (gotLevel + level > 3000L)
			{
				gotMXPBar = (long) ((gotLevel + level) * 270.0F - 572442.63F);
			}
			else if (gotLevel + level > 2000L)
			{
				gotMXPBar = (long) ((gotLevel + level) * ((gotLevel + level) * ((gotLevel + level) * 0.00001F)) - 32442.63F);
			}
			else if (gotLevel + level > 1000L)
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
		if (level > 0L)
		{
			this.setMana(this.getMaxMana());
		}
		this.mxp = mxp;

		this.markSending();
		this.markDirty();
		this.runMXP = false;
	}

	public void setMagicPoint(long value)
	{
		if (value >= 0L)
			this.magicpoint = value;
		if (!this.runMXP)
		{
			this.markDirty();
			this.markSending();
		}
	}

	public void setTrueMaxMana(long value)
	{
		if (value >= 0L)
			this.maxMana = value;
		if (!this.runMXP)
		{
			this.markDirty();
			this.markSending();
		}
	}

	private void setMXPBar(float f)
	{
		this.mxp_bar = f;
	}

	private float getMXPBar()
	{
		return this.getLevel() > 0 ? this.mxp_bar : this.mxpBarCap;
	}

	public long getMaxMXP()
	{
		return (16.0F + this.getMXPBar()) <= Long.MAX_VALUE ? (long) (16.0F + this.getMXPBar()) : Long.MAX_VALUE;
	}

	public long getMana()
	{
		return this.mana;
	}

	public long getLevel()
	{
		return this.level;
	}

	public long getMXP()
	{
		return this.mxp;
	}

	public long getTrueMaxMana()
	{
		return this.maxMana;
	}

	public long getMaxMana()
	{
		long value = (long) ((this.getTrueMaxMana() + this.getMaxManaAdd()) * (1 + this.getMaxManaMag()));
		return value < Long.MAX_VALUE ? value : Long.MAX_VALUE;
	}

	public long getMagicPoint()
	{
		return this.magicpoint;
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

	public boolean didGet(String modid, String id)
	{
		return this.studies.containsKey(modid + "@" + id) ? this.studies.get(modid + "@" + id) : false;
	}

	public boolean canGet(String pmodid, String parent, String modid, String id)
	{
		if (parent.equals(id) && pmodid.equals(modid))
		{
			return !didGet(modid, id);
		}
		else if (didGet(pmodid, parent))
		{
			return !didGet(modid, id);
		}
		else
		{
			return false;
		}
	}

	public boolean canGetRoot(String modid, String id)
	{
		return this.canGet(modid, id, modid, id);
	}

	public void set(String id, String i)
	{
		this.studies.put(id + "@" + i, true);
		this.markSending();
		this.markDirty();
	}

	public void remove(String id, String i)
	{
		this.studies.put(id + "@" + i, false);
		this.markSending();
		this.markDirty();
	}

	public Map<String, List<String>> alreadyGottenList()
	{
		NBTTagCompound studyData = new NBTTagCompound();
		Map<String, List<String>> list = new HashMap<String, List<String>>();
		for (Map.Entry<String, List<String>> entry : SpellUtils.getAll().entrySet())
		{
			List<String> ids = new ArrayList<String>();
			for (String id : entry.getValue())
			{
				if (didGet(entry.getKey(), id))
				{
					ids.add(id);
				}
			}

			if (!ids.isEmpty())
			{
				list.put(entry.getKey(), ids);
				studyData.setTag(entry.getKey(), new NBTTagList());
				for (String id : ids)
				{
					studyData.getTagList(entry.getKey(), 8).appendTag(new NBTTagString(id));
				}
			}
		}

		return list;
	}

	public boolean isDirty()
	{
		return this.needSaving;
	}

	public void markDirty()
	{
		this.needSaving = true;
	}

	public void markSending()
	{
		this.needSending = true;
	}

	public void sendToOtherSide(UUID uuid)
	{
		if (this.needSending)
		{
			NBTTagCompound send = this.writeToNBT(new NBTTagCompound());
			send.setString("uuid", uuid.toString());
			send.setBoolean("isClientChanged", this.clientChanged);
			PacketMagicDataToClient pmdc = new PacketMagicDataToClient(send);
			NeoOres.PACKET.sendToAll((IMessage) pmdc);

			this.clientChanged = false;
		}
		this.needSending = false;
	}
}
