package neo_ores.packet;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import neo_ores.entity.EntitySpellBullet;
import neo_ores.main.NeoOres;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketEntityToClient implements IMessage
{
	private NBTTagCompound nbt;
	@SuppressWarnings("serial")
	public static final Map<String, Class<? extends Entity>> MAP_ENTITY_CLASS = new HashMap<String, Class<? extends Entity>>() {
		{
			put("EntitySpellBullet", EntitySpellBullet.class);
		}
	};

	public PacketEntityToClient()
	{
	}

	public PacketEntityToClient(NBTTagCompound nbt)
	{
		this.nbt = nbt;
	}

	public PacketEntityToClient(Entity entity, String name)
	{
		this.nbt = new NBTTagCompound();
		this.nbt.setTag("entityData", entity.serializeNBT());
		this.nbt.setString("name", name);
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
					if (nbt.hasKey("entityData") && nbt.hasKey("name")) {
						String name = nbt.getString("name");
						if (!MAP_ENTITY_CLASS.containsKey(name)) {
							return;
						}
						try
						{
							Entity entity = MAP_ENTITY_CLASS.get(name).getConstructor(World.class).newInstance(world);
							NBTTagCompound compound = nbt.getCompoundTag("entityData");
							MAP_ENTITY_CLASS.get(name).cast(entity).deserializeNBT(compound);
							world.spawnEntity(entity);
						}
						catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
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
