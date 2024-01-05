package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.main.NeoOres;
import neo_ores.tileentity.AbstractTileEntityPedestal;
import neo_ores.tileentity.TileEntityEnhancedPedestal;
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
					}
				}
			});
			return null;
		}
	}
}
