package neo_ores.util;

import java.util.UUID;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import neo_ores.main.NeoOres;
import neo_ores.packet.PacketMagicDataToServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerMagicDataClient extends PlayerMagicData
{
	private boolean changed = false;

	public PlayerMagicDataClient()
	{
		super(false);
	}

	public void sync()
	{
		this.changed = false;
	}

	public boolean isChanged()
	{
		return this.changed;
	}

	public void sendToOtherSide(@Nullable UUID uuid)
	{
		this.changed = true;
		NBTTagCompound send = this.writeToNBT(new NBTTagCompound());
		send.setBoolean("isChanged", this.changed);
		PacketMagicDataToServer pmds = new PacketMagicDataToServer(send);
		NeoOres.PACKET.sendToServer((IMessage) pmds);
	}

	public void sendPacketRequest()
	{
		this.changed = false;
		FMLLog.log.log(Level.INFO, "[NeoOres] Requests sync with PMD");
		NBTTagCompound send = new NBTTagCompound();
		send.setBoolean("packetRequest", true);
		PacketMagicDataToServer pmds = new PacketMagicDataToServer(send);
		NeoOres.PACKET.sendToServer((IMessage) pmds);
	}
}
