package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import neo_ores.main.NeoOresData;
import neo_ores.pi.PICommand;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPISyncToServer implements IMessage
{
	private NBTTagCompound nbt;

	public PacketPISyncToServer(NBTTagCompound nbtTagCompound)
	{
		this.nbt = nbtTagCompound.copy();
		PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
		ByteBufUtils.writeTag(buffer, this.nbt);
		if (buffer.writerIndex() > 32767) 
		{
			throw new IllegalArgumentException("Payload may not be larger than 32767 bytes");
		}
	}

	public PacketPISyncToServer()
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

	public static class Handler implements IMessageHandler<PacketPISyncToServer, IMessage>
	{
		@Override
		public IMessage onMessage(PacketPISyncToServer message, final MessageContext ctx)
		{
			EntityPlayerMP mp = (ctx.getServerHandler()).player;
			PICommand command = new PICommand();
			command.readFromNBT(message.nbt);
			NeoOresData.instance.getCurrentTargetPedestals(mp).setCommand(command);
			return null;
		}
	}
}

