package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.main.NeoOresData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncConstantDataToClient implements IMessage
{
	private NBTTagCompound nbt;

	public PacketSyncConstantDataToClient(NBTTagCompound data)
	{
		this.nbt = data.copy();
	}

	public PacketSyncConstantDataToClient()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, this.nbt);
	}

	public static class Handler implements IMessageHandler<PacketSyncConstantDataToClient, IMessage>
	{
		@Override
		public IMessage onMessage(PacketSyncConstantDataToClient message, final MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				public void run()
				{
					NeoOresData.setConstantValue(message.nbt);
				}
			});
			return null;
		}
	}
}
