package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.main.NeoOres;
import neo_ores.util.SpellUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketParticleTypeToClient implements IMessage
{
	private NBTTagCompound nbt;

	public PacketParticleTypeToClient()
	{
	}

	public PacketParticleTypeToClient(NBTTagCompound nbt)
	{
		this.nbt = nbt;
	}

	public PacketParticleTypeToClient(Vec3d target, double maxRadius, double accel, int minCount, int maxCount, float minParticleVolume, float maxParticleVolume, int color, int dimension, int type)
	{
		this.nbt = new NBTTagCompound();
		NBTTagCompound targetTag = new NBTTagCompound();
		targetTag.setDouble("x", target.x);
		targetTag.setDouble("y", target.y);
		targetTag.setDouble("z", target.z);
		this.nbt.setTag("target", targetTag);
		this.nbt.setDouble("maxRadius", maxRadius);
		this.nbt.setInteger("minCount", minCount);
		this.nbt.setInteger("maxCount", maxCount);
		this.nbt.setFloat("minParticleVolume", minParticleVolume);
		this.nbt.setFloat("maxParticleVolume", maxParticleVolume);
		this.nbt.setInteger("color", color);
		this.nbt.setInteger("dim", dimension);
		this.nbt.setInteger("type", type);
		this.nbt.setDouble("accel", accel);
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

	public static class Handler implements IMessageHandler<PacketParticleTypeToClient, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketParticleTypeToClient message, MessageContext ctx)
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
					NBTTagCompound targetTag = message.nbt.getCompoundTag("target");
					Vec3d target = new Vec3d(targetTag.getDouble("x"), targetTag.getDouble("y"), targetTag.getDouble("z"));
					int type = message.nbt.getInteger("type");
					if (type == 0)
					{
						SpellUtils.displayParticleTypeB(world, target, message.nbt.getDouble("maxRadius"), message.nbt.getInteger("minCount"), message.nbt.getInteger("maxCount"),
								message.nbt.getFloat("minParticleVolume"), message.nbt.getFloat("maxParticleVolume"), message.nbt.getInteger("color"));
					}
					else if (type == 1) 
					{
						SpellUtils.displayParticleTypeC(world, target, message.nbt.getDouble("maxRadius"), message.nbt.getDouble("accel"), message.nbt.getInteger("minCount"), message.nbt.getInteger("maxCount"),
								message.nbt.getFloat("minParticleVolume"), message.nbt.getFloat("maxParticleVolume"), message.nbt.getInteger("color"));
					}
				}
			});
			return null;
		}
	}
}
