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

public class PacketParticleTypeBToClient implements IMessage
{
	private NBTTagCompound nbt;

	public PacketParticleTypeBToClient()
	{
	}

	public PacketParticleTypeBToClient(NBTTagCompound nbt)
	{
		this.nbt = nbt;
	}

	public PacketParticleTypeBToClient(Vec3d target, double maxRadius, int minCount, int maxCount, float minParticleVolume, float maxParticleVolume, int color, int dimension)
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

	public static class Handler implements IMessageHandler<PacketParticleTypeBToClient, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketParticleTypeBToClient message, MessageContext ctx)
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
					SpellUtils.displayParticleTypeB(world, target, message.nbt.getDouble("maxRadius"), message.nbt.getInteger("minCount"), message.nbt.getInteger("maxCount"),
							message.nbt.getFloat("minParticleVolume"), message.nbt.getFloat("maxParticleVolume"), message.nbt.getInteger("color"));
				}
			});
			return null;
		}
	}
}
