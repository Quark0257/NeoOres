package neo_ores.packet;

import java.util.List;

import io.netty.buffer.ByteBuf;
import neo_ores.main.NeoOres;
import neo_ores.tileentity.TileEntityEnhancedPedestal;
import neo_ores.util.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPedestalsSyncToClient implements IMessage
{
	private NBTTagCompound nbt;

	public PacketPedestalsSyncToClient(World world, List<BlockPos> pos)
	{
		NBTTagCompound data = new NBTTagCompound();
		data.setTag("tileEntities", WorldUtils.getTileEntitiesData(WorldUtils.<TileEntityEnhancedPedestal>getTileEntities(world, pos)));
		data.setInteger("dim", world.provider.getDimension());
		this.nbt = data;
	}

	public PacketPedestalsSyncToClient()
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
	
	public static class Handler implements IMessageHandler<PacketPedestalsSyncToClient, IMessage>
	{
		@Override
		public IMessage onMessage(PacketPedestalsSyncToClient message, final MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				public void run()
				{
					World world = NeoOres.proxy.getClientWorld();
					if (world == null)
						return;
					int dim = message.nbt.getInteger("dim");
					if (dim != world.provider.getDimension()) {
						return;
					}
					NBTTagList list = message.nbt.getTagList("tileEntities", 10);
					WorldUtils.setTileEntitiesData(world, list);
				}
			});
			return null;
		}
	}
}
