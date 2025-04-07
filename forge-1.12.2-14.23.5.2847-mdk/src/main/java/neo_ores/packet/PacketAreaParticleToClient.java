package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.client.particle.ParticleMagic;
import neo_ores.event.NeoOresRegisterEvents;
import neo_ores.main.NeoOres;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketAreaParticleToClient implements IMessage
{
	private NBTTagCompound nbt;

	public PacketAreaParticleToClient()
	{
	}

	public PacketAreaParticleToClient(NBTTagCompound nbt)
	{
		this.nbt = nbt;
	}

	public PacketAreaParticleToClient(Vec3d start, Vec3d size, int color, int dimension)
	{
		this.nbt = new NBTTagCompound();
		NBTTagCompound targetTag = new NBTTagCompound();
		targetTag.setDouble("x", start.x);
		targetTag.setDouble("y", start.y);
		targetTag.setDouble("z", start.z);
		this.nbt.setTag("start", targetTag);
		NBTTagCompound sizeTag = new NBTTagCompound();
		sizeTag.setDouble("x", size.x);
		sizeTag.setDouble("y", size.y);
		sizeTag.setDouble("z", size.z);
		this.nbt.setTag("size", sizeTag);
		this.nbt.setInteger("color", color);
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

	public static class Handler implements IMessageHandler<PacketAreaParticleToClient, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketAreaParticleToClient message, MessageContext ctx)
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
					NBTTagCompound targetTag = message.nbt.getCompoundTag("start");
					Vec3d target = new Vec3d(targetTag.getDouble("x"), targetTag.getDouble("y"), targetTag.getDouble("z"));
					NBTTagCompound sizeTag = message.nbt.getCompoundTag("size");
					Vec3d size = new Vec3d(sizeTag.getDouble("x"), sizeTag.getDouble("y"), sizeTag.getDouble("z"));
					int color = message.nbt.getInteger("color");
					double y = target.y + 0.5 * size.y;
					double multiplier = 2.0D;
					double dMulti = 1.0 / multiplier;
					int maxX = (int)(size.x * multiplier);
					int maxZ = (int)(size.z * multiplier);
					for (int i = 0; i < maxX; i++)
					{
						for (int j = 0; j < maxZ; j++)
						{
							double baseX = (double)i / multiplier;
							double baseZ = (double)j / multiplier;
							double dx = baseX + dMulti * (world.rand.nextDouble());
							double dy = 0.5 * size.y * (world.rand.nextDouble() - 0.5D);
							double dz = baseZ + dMulti * (world.rand.nextDouble());
							ParticleMagic png = new ParticleMagic(world, target.x + dx, y + dy, target.z + dz, 0.0, 0.0, 0.0, color, 12 + world.rand.nextInt(8),
									0.3F + 0.02f * world.rand.nextFloat(), NeoOresRegisterEvents.particle0);
							Minecraft.getMinecraft().effectRenderer.addEffect(png);
						}
					}
				}
			});
			return null;
		}
	}
}
