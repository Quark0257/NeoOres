package neo_ores.packet;

import java.util.List;

import io.netty.buffer.ByteBuf;
import neo_ores.main.NeoOres;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketBiomeChangeToClient implements IMessage
{
	private NBTTagCompound nbt;

	public PacketBiomeChangeToClient()
	{
	}

	public PacketBiomeChangeToClient(NBTTagCompound nbt)
	{
		this.nbt = nbt;
	}

	public PacketBiomeChangeToClient(List<BlockPos> positions, byte biomeId, int dimension)
	{
		this.nbt = new NBTTagCompound();
		NBTTagList tagList = new NBTTagList();
		for (BlockPos pos : positions)
		{
			tagList.appendTag(new NBTTagIntArray(new int[] { pos.getX(), pos.getY(), pos.getZ() }));
		}
		this.nbt.setTag("positions", tagList);
		this.nbt.setByte("id", biomeId);
		this.nbt.setInteger("dim", dimension);
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

	public static class Handler implements IMessageHandler<PacketBiomeChangeToClient, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketBiomeChangeToClient message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				public void run()
				{
					World world = NeoOres.proxy.getClientWorld();
					if (world == null)
						return;
					int dim = message.nbt.getInteger("dim");
					if (dim != world.provider.getDimension())
					{
						return;
					}
					NBTTagList targetTag = message.nbt.getTagList("positions", 11);
					byte biomeId = message.nbt.getByte("id");
					for (int i = 0; i < targetTag.tagCount(); i++) 
					{
						int[] array = targetTag.getIntArrayAt(i);
						BlockPos pos = new BlockPos(array[0], array[1], array[2]);
						Chunk chunk = world.getChunkFromBlockCoords(pos);
						int x = pos.getX() & 15;
				        int z = pos.getZ() & 15;
						chunk.getBiomeArray()[z << 4 | x] = biomeId;
						world.markBlockRangeForRenderUpdate(new BlockPos(array[0], 0, array[2]), new BlockPos(array[0], 255, array[2]));
					}
				}
			});
			return null;
		}
	}
}
