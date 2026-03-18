package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.api.PlayerTrigger;
import neo_ores.client.gui.GuiQuestToast;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class PacketPlayerTriggerComplete implements IMessage
{
	private NBTTagCompound nbt;

	public PacketPlayerTriggerComplete(ResourceLocation location)
	{
		this.nbt = new NBTTagCompound();
		this.nbt.setString("domain", location.getResourceDomain());
		this.nbt.setString("path", location.getResourcePath());
	}

	public PacketPlayerTriggerComplete()
	{
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

	public static class Handler implements IMessageHandler<PacketPlayerTriggerComplete, IMessage>
	{
		@Override
		public IMessage onMessage(PacketPlayerTriggerComplete message, final MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				public void run()
				{
					ResourceLocation location = new ResourceLocation(message.nbt.getString("domain"), message.nbt.getString("path"));
					PlayerTrigger trigger = GameRegistry.findRegistry(PlayerTrigger.class).getValue(location);
					if (trigger != null) 
					{
						Minecraft.getMinecraft().getToastGui().add(new GuiQuestToast(trigger));
					}
				}
			});
			return null;
		}
	}
}
