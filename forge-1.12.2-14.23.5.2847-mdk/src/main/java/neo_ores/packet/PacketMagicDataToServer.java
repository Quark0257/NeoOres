package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.main.NeoOresData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/*
 * PacketManaDataToServer pmds = new PacketManaDataToServer(mc.player.getEntityId());
 * NeoOres.PACKET.sendToServer((IMessage)pmds);
 */

//Not Using
public class PacketMagicDataToServer implements IMessage
{
	private NBTTagCompound nbt;

	public PacketMagicDataToServer(NBTTagCompound entitydata)
	{
		this.nbt = entitydata;
	}

	public PacketMagicDataToServer()
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

	public static class Handler implements IMessageHandler<PacketMagicDataToServer, IMessage>
	{
		@Override
		public IMessage onMessage(PacketMagicDataToServer message, final MessageContext ctx)
		{
			WorldServer worldServer = (WorldServer) (ctx.getServerHandler()).player.world;
			EntityPlayerMP player = ctx.getServerHandler().player;
			worldServer.addScheduledTask(new Runnable()
			{
				public void run()
				{	
					if(message.nbt.hasKey("packetRequest") && message.nbt.getBoolean("packetRequest"))
						NeoOresData.instance.getPMD(player).markSending();
					else 
						NeoOresData.instance.getPMD(player).readFromNBT(message.nbt);
				}
			});
			return null;
		}
	}
}
