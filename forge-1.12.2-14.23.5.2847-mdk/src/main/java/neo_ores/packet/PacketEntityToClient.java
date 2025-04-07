package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.main.NeoOres;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketEntityToClient implements IMessage
{
	private NBTTagCompound nbt;

	public PacketEntityToClient()
	{
	}

	public PacketEntityToClient(NBTTagCompound nbt)
	{
		this.nbt = nbt;
	}

	public PacketEntityToClient(Entity entity)
	{
		this.nbt = new NBTTagCompound();
		this.nbt.setTag("entityData", entity.serializeNBT());
		this.nbt.setString("id", EntityList.getKey(entity).toString());
		this.nbt.setInteger("dim", entity.world.provider.getDimension());
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

	public static class Handler implements IMessageHandler<PacketEntityToClient, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketEntityToClient message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				public void run()
				{
					World world = NeoOres.proxy.getClientWorld();
					if (world == null)
						return;
					NBTTagCompound nbt = message.nbt;
					int dim = nbt.getInteger("dim");
					if (dim != world.provider.getDimension()) {
						return;
					}
					if (nbt.hasKey("entityData") && nbt.hasKey("id")) {
						try
						{
							NBTTagCompound compound = nbt.getCompoundTag("entityData");
							Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(nbt.getString("id")), world);
							entity.deserializeNBT(compound);
							world.spawnEntity(entity);
						}
						catch (IllegalArgumentException | SecurityException e)
						{
							FMLLog.log.warn("Couldn't spawn entity on client side");
						}
					}
				}
			});
			return null;
		}
	}
}
