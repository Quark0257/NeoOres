package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresRegisterEvent;
import neo_ores.util.SpellUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketParticleToClient implements IMessage
{
	private NBTTagCompound nbt;

	public PacketParticleToClient()
	{
	}

	public PacketParticleToClient(NBTTagCompound nbt)
	{
		this.nbt = nbt;
	}

	public PacketParticleToClient(Vec3d target, Vec3d size, TextureAtlasSprite[] texture, int color, int particleVolume)
	{
		this.nbt = new NBTTagCompound();
		NBTTagCompound targetTag = new NBTTagCompound();
		targetTag.setDouble("x", target.x);
		targetTag.setDouble("y", target.y);
		targetTag.setDouble("z", target.z);
		this.nbt.setTag("target", targetTag);
		NBTTagCompound sizeTag = new NBTTagCompound();
		sizeTag.setDouble("x", size.x);
		sizeTag.setDouble("y", size.y);
		sizeTag.setDouble("z", size.z);
		this.nbt.setTag("size", sizeTag);
		this.nbt.setInteger("particleVolume", particleVolume);
		this.nbt.setInteger("color", color);
		if (texture == NeoOresRegisterEvent.particle0)
			this.nbt.setString("type", "a_0");
		else
			this.nbt.setString("type", "-1");
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

	public static class Handler implements IMessageHandler<PacketParticleToClient, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketParticleToClient message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				public void run()
				{
					World world = NeoOres.proxy.getClientWorld();
					if (world == null)
						return;
					String type = message.nbt.getString("type");
					if (type.equals("-1"))
						return;
					if (type.equals("a_0"))
					{
						NBTTagCompound targetTag = message.nbt.getCompoundTag("target");
						Vec3d target = new Vec3d(targetTag.getDouble("x"), targetTag.getDouble("y"), targetTag.getDouble("z"));
						NBTTagCompound sizeTag = message.nbt.getCompoundTag("size");
						Vec3d size = new Vec3d(sizeTag.getDouble("x"), sizeTag.getDouble("y"), sizeTag.getDouble("z"));
						SpellUtils.displayParticleTypeA(world, target, size, NeoOresRegisterEvent.particle0, message.nbt.getInteger("color"), message.nbt.getInteger("particleVolume"));
					}
				}
			});
			return null;
		}
	}
}
