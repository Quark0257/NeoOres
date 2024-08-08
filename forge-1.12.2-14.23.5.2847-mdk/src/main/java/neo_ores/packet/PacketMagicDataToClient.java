package neo_ores.packet;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresData;
import neo_ores.util.PlayerMagicDataClient;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketMagicDataToClient implements IMessage
{
	private NBTTagCompound nbt;

	public PacketMagicDataToClient()
	{
	}

	public PacketMagicDataToClient(NBTTagCompound nbt)
	{
		this.nbt = nbt;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, nbt);
	}

	public static class Handler implements IMessageHandler<PacketMagicDataToClient, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketMagicDataToClient message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				public void run()
				{
					World world = NeoOres.proxy.getClientWorld();
					if (world == null)
						return;
					UUID uuid = UUID.fromString(message.nbt.getString("uuid"));
					PlayerMagicDataClient pmdc = NeoOresData.getPMDC(uuid);
					if (pmdc.isChanged() && message.nbt.hasKey("isClientChanged") && message.nbt.getBoolean("isClientChanged"))
					{
						pmdc.readFromNBT(message.nbt);
						pmdc.sync();
					}
					else if (!pmdc.isChanged())
						pmdc.readFromNBT(message.nbt);
				}
			});
			return null;
		}
	}
}
