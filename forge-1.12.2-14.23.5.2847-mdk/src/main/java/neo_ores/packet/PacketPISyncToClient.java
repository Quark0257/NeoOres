package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.event.NeoOresClientEvents;
import neo_ores.main.NeoOres;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPISyncToClient implements IMessage
{
	private NBTTagCompound nbt;

	public PacketPISyncToClient(NBTTagCompound compound)
	{
		this.nbt = compound.copy();
	}

	public PacketPISyncToClient()
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
	
	public static class Handler implements IMessageHandler<PacketPISyncToClient, IMessage>
	{
		@Override
		public IMessage onMessage(PacketPISyncToClient message, final MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				public void run()
				{
					World world = NeoOres.proxy.getClientWorld();
					if (world == null)
						return;
					NeoOresClientEvents.getInstance().getPIClientData().readFromNBT(message.nbt);
				}
			});
			return null;
		}
	}
}
