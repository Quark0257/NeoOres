package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.main.NeoOres;
import neo_ores.tileentity.TileEntityMechanicalMagician;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketDestinationToClient implements IMessage
{
	private NBTTagCompound nbt;

	public PacketDestinationToClient()
	{
	}

	public PacketDestinationToClient(NBTTagCompound nbt)
	{
		this.nbt = nbt;
	}
	
	public PacketDestinationToClient(BlockPos pos, BlockPos destination)
	{
		this.nbt = new NBTTagCompound();
		NBTTagCompound nbtPos = new NBTTagCompound();
		nbtPos.setInteger("x", pos.getX());
		nbtPos.setInteger("y", pos.getY());
		nbtPos.setInteger("z", pos.getZ());
		NBTTagCompound nbtDes = new NBTTagCompound();
		nbtDes.setInteger("x", destination.getX());
		nbtDes.setInteger("y", destination.getY());
		nbtDes.setInteger("z", destination.getZ());
		this.nbt.setTag("pos", nbtPos);
		this.nbt.setTag("destination", nbtDes);
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

	public static class Handler implements IMessageHandler<PacketDestinationToClient, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketDestinationToClient message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				public void run()
				{
					World world = NeoOres.proxy.getClientWorld();
					if (world == null)
						return;
					NBTTagCompound pos = message.nbt.getCompoundTag("pos");
					NBTTagCompound destination = message.nbt.getCompoundTag("destination");
					TileEntity te = world.getTileEntity(new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z")));
					if (te instanceof TileEntityMechanicalMagician)
					{
						TileEntityMechanicalMagician temm = (TileEntityMechanicalMagician) te;
						temm.setDestination(new BlockPos(destination.getInteger("x"), destination.getInteger("y"), destination.getInteger("z")));
					}
				}
			});
			return null;
		}
	}
}
