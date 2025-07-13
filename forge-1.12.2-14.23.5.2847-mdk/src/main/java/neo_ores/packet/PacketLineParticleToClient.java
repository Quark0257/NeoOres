package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.client.particle.TexturedParticle;
import neo_ores.event.NeoOresClientEvents;
import neo_ores.main.NeoOres;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketLineParticleToClient implements IMessage
{
	private NBTTagCompound nbt;

	public PacketLineParticleToClient()
	{
	}

	public PacketLineParticleToClient(NBTTagCompound nbt)
	{
		this.nbt = nbt;
	}

	public PacketLineParticleToClient(Vec3d start, Vec3d vel, int color, int dimension)
	{
		this.nbt = new NBTTagCompound();
		NBTTagCompound targetTag = new NBTTagCompound();
		targetTag.setDouble("x", start.x);
		targetTag.setDouble("y", start.y);
		targetTag.setDouble("z", start.z);
		this.nbt.setTag("start", targetTag);
		NBTTagCompound sizeTag = new NBTTagCompound();
		sizeTag.setDouble("x", vel.x);
		sizeTag.setDouble("y", vel.y);
		sizeTag.setDouble("z", vel.z);
		this.nbt.setTag("vel", sizeTag);
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

	public static class Handler implements IMessageHandler<PacketLineParticleToClient, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketLineParticleToClient message, MessageContext ctx)
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
					Vec3d start = new Vec3d(targetTag.getDouble("x"), targetTag.getDouble("y"), targetTag.getDouble("z"));
					NBTTagCompound sizeTag = message.nbt.getCompoundTag("vel");
					Vec3d vel = new Vec3d(sizeTag.getDouble("x"), sizeTag.getDouble("y"), sizeTag.getDouble("z"));
					int color = message.nbt.getInteger("color");
					double multiplier = 4.0D;
					int sizeR = (int) (vel.lengthVector() * multiplier);
					vel = vel.normalize();
					for (int k = 0; k < sizeR; k++)
					{
						double dx = vel.x * (double)k / multiplier;
						double dy = vel.y * (double)k / multiplier;
						double dz = vel.z * (double)k / multiplier;
						NeoOresClientEvents.getInstance().addParticle(new TexturedParticle(start.x + dx, start.y + dy, start.z + dz, 0.0, 0.0, 0.0,
								12 + world.rand.nextInt(8), 1.0F, NeoOres.PARTICLE_MAGIC).setColor(color, 1.0F));
					}
				}
			});
			return null;
		}
	}
}
