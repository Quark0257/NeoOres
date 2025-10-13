package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncConstantDataToServer implements IMessage
{
	private NBTTagCompound nbt;

	public PacketSyncConstantDataToServer(NBTTagCompound compound)
	{
		this.nbt = compound.copy();
	}

	public PacketSyncConstantDataToServer()
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
	
	public static class Handler implements IMessageHandler<PacketSyncConstantDataToServer, IMessage>
	{
		@Override
		public IMessage onMessage(PacketSyncConstantDataToServer message, final MessageContext ctx)
		{
			WorldServer worldServer = (WorldServer) (ctx.getServerHandler()).player.world;
			worldServer.addScheduledTask(new Runnable()
			{
				public void run()
				{
					NBTTagCompound compound = NeoOresData.instance.getConstantValue(message.nbt);
					if (compound != null) 
					{
						NeoOres.PACKET.sendTo(new PacketSyncConstantDataToClient(compound), ctx.getServerHandler().player);
					}
				}
			});
			return null;
		}
	}
}
