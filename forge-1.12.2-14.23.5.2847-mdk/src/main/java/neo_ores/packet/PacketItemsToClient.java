package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.main.NeoOres;
import neo_ores.tileentity.AbstractTileEntityPedestal;
import neo_ores.tileentity.TileEntityPedestalNetworkDetector;
import neo_ores.tileentity.TileEntityEnhancedPedestal;
import neo_ores.tileentity.TileEntityPedestal;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketItemsToClient implements IMessage
{
	private NBTTagCompound nbt;

	public PacketItemsToClient()
	{
	}

	public PacketItemsToClient(NBTTagCompound nbt)
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

	public static class Handler implements IMessageHandler<PacketItemsToClient, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketItemsToClient message, MessageContext ctx)
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
					TileEntity te = world.getTileEntity(new BlockPos(message.nbt.getInteger("x"), message.nbt.getInteger("y"), message.nbt.getInteger("z")));
					if (te instanceof AbstractTileEntityPedestal)
					{
						AbstractTileEntityPedestal teep = (AbstractTileEntityPedestal) te;
						teep.offset = message.nbt.getDouble("offset");
						teep.setDisplay(new ItemStack(message.nbt.getCompoundTag("display")));
						if (message.nbt.hasKey("slotsize") && teep instanceof TileEntityEnhancedPedestal)
						{
							((TileEntityEnhancedPedestal) teep).slotsize = message.nbt.getInteger("slotsize");
						}

						if (message.nbt.hasKey("multiblock") && te instanceof TileEntityPedestal)
						{
							((TileEntityPedestal) te).setClient(message.nbt.getBoolean("multiblock"), message.nbt.getInteger("phase"), message.nbt.getInteger("maxPhase"),
									message.nbt.getBoolean("isCreating"));
						}
						
						if (message.nbt.hasKey("count") && message.nbt.hasKey("inputPower") && teep instanceof TileEntityPedestalNetworkDetector) 
						{
							((TileEntityPedestalNetworkDetector) teep).setClient(message.nbt.getInteger("count"), message.nbt.getBoolean("inputPower"));
						}
					}
				}
			});
			return null;
		}
	}
}
