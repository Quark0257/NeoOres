package neo_ores.mana;

import neo_ores.main.NeoOres;
import neo_ores.main.Reference;
import neo_ores.mana.packet.PacketManaDataToServer;
import neo_ores.spell.SpellUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerManaDataClient
{
	private final NBTTagCompound nbt;
	private final NBTTagCompound neo_ores;
	private final NBTTagCompound data;
	
	public PlayerManaDataClient(EntityPlayerSP player) 
	{
		NBTTagCompound neo_ores = new NBTTagCompound();
		NBTTagCompound nbt = new NBTTagCompound();
		this.data = player.getEntityData().copy();
		
		if(this.data.hasKey(Reference.MOD_ID,10))
		{
			neo_ores = this.data.getCompoundTag(Reference.MOD_ID).copy();
			if(neo_ores.hasKey(SpellUtils.NBTTagUtils.MAGIC,10))
			{
				nbt = neo_ores.getCompoundTag(SpellUtils.NBTTagUtils.MAGIC).copy();
			}
		}
		
		this.nbt = nbt;
		this.neo_ores = neo_ores;
	}
	
	public long getMaxMXP()
	{
		return this.nbt.getLong(SpellUtils.NBTTagUtils.MAX_MXP);
	}
	
	public long getMana()
	{
		return this.nbt.getLong(SpellUtils.NBTTagUtils.MANA);
	}
	
	public long getLevel()
	{
		return this.nbt.getLong(SpellUtils.NBTTagUtils.LEVEL);
	}
	
	public long getMXP()
	{
		return this.nbt.getLong(SpellUtils.NBTTagUtils.MXP);
	}
	
	public long getMaxMana()
	{
		return this.nbt.getLong(SpellUtils.NBTTagUtils.MAX_MANA);
	}
	
	public long getMagicPoint()
	{
		return this.nbt.getLong(SpellUtils.NBTTagUtils.MAGIC_POINT);
	}
	
	public void setMXP(long value)
	{
		if(value < 0) value = 0;
		
		this.nbt.setLong(SpellUtils.NBTTagUtils.MXP, value);
		this.neo_ores.setTag(SpellUtils.NBTTagUtils.MAGIC, this.nbt.copy());
		this.data.setTag(Reference.MOD_ID, this.neo_ores.copy());
		PacketManaDataToServer pmds = new PacketManaDataToServer(this.data.copy());
		NeoOres.PACKET.sendToServer((IMessage)pmds);
	}
	
	public void setMagicPoint(long value)
	{
		if(value < 0) value = 0;
		
		this.nbt.setLong(SpellUtils.NBTTagUtils.MAGIC_POINT, value);
		this.neo_ores.setTag(SpellUtils.NBTTagUtils.MAGIC, this.nbt.copy());
		this.data.setTag(Reference.MOD_ID, this.neo_ores.copy());
		PacketManaDataToServer pmds = new PacketManaDataToServer(this.data.copy());
		NeoOres.PACKET.sendToServer((IMessage)pmds);
	}
	
	public void setMaxMana(long value)
	{
		if(value < 0) value = 0;
		
		this.nbt.setLong(SpellUtils.NBTTagUtils.MAX_MANA, value);
		this.neo_ores.setTag(SpellUtils.NBTTagUtils.MAGIC, this.nbt.copy());
		this.data.setTag(Reference.MOD_ID, this.neo_ores.copy());
		PacketManaDataToServer pmds = new PacketManaDataToServer(this.data.copy());
		NeoOres.PACKET.sendToServer((IMessage)pmds);
	}
	
	public void setLevel(long value)
	{
		if(value < 0) value = 0;
		
		this.nbt.setLong(SpellUtils.NBTTagUtils.LEVEL, value);
		this.neo_ores.setTag(SpellUtils.NBTTagUtils.MAGIC, this.nbt.copy());
		this.data.setTag(Reference.MOD_ID, this.neo_ores.copy());
		PacketManaDataToServer pmds = new PacketManaDataToServer(this.data.copy());
		NeoOres.PACKET.sendToServer((IMessage)pmds);
	}
	
	public void setMana(long value)
	{
		if(value < 0) value = 0;
		
		if(value > this.getMaxMana()) value = this.getMaxMana();
		
		this.nbt.setLong(SpellUtils.NBTTagUtils.MANA, value);
		this.neo_ores.setTag(SpellUtils.NBTTagUtils.MAGIC, this.nbt.copy());
		this.data.setTag(Reference.MOD_ID, this.neo_ores.copy());
		PacketManaDataToServer pmds = new PacketManaDataToServer(this.data.copy());
		NeoOres.PACKET.sendToServer((IMessage)pmds);
	}
	
	public void addMana(long value)
	{
		if(value + (float)this.getMana() > Long.MAX_VALUE) this.setMana(Long.MAX_VALUE);
		else if(value + this.getMana() >= 0) this.setMana(value + this.getMana());
		else this.setMana(0);
	}
	
	public void addLevel(long value)
	{
		if(value + this.getLevel() > Long.MAX_VALUE) this.setLevel(Long.MAX_VALUE);
		else if(value + this.getLevel() >= 0) this.setLevel(value + this.getLevel());
		else this.setLevel(0);
	}
	
	public void addMXP(long value)
	{
		if(value + this.getMXP() > Long.MAX_VALUE) this.setMXP(Long.MAX_VALUE);
		else if(value + this.getMXP() >= 0) this.setMXP(value + this.getMXP());
		else this.setMXP(0);
	}
	
	public void addMaxMana(long value)
	{
		if(value + this.getMaxMana() > Long.MAX_VALUE) this.setMaxMana(Long.MAX_VALUE);
		else if(value + this.getMaxMana() >= 0) this.setMaxMana(value + this.getMaxMana());
		else this.setMaxMana(0);
	}
	
	public void addMagicPoint(long value)
	{
		if(value + this.getMagicPoint() > Long.MAX_VALUE) this.setMagicPoint(Long.MAX_VALUE);
		else if(value + this.getMagicPoint() >= 0) this.setMagicPoint(value + this.getMagicPoint());
		else this.setMagicPoint(0);
	}
}
