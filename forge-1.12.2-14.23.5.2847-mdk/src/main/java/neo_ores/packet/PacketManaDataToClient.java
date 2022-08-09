package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.main.NeoOres;
import neo_ores.main.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketManaDataToClient implements IMessage
{
	private NBTTagCompound nbt;

	public PacketManaDataToClient() {}
	
	public PacketManaDataToClient(NBTTagCompound nbt) 
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
	
	public static class Handler implements IMessageHandler<PacketManaDataToClient, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketManaDataToClient message, MessageContext ctx) 
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
	            public void run() 
	            {
	            	World world = NeoOres.proxy.getClientWorld();
	              	if (world == null) return; 
	              	int playerid = message.nbt.getInteger("playerID");
	              	Entity player = world.getEntityByID(playerid);	              	
	              	if (player != null && player instanceof EntityPlayerSP) 
	              	{
	              		NBTTagCompound nbt = player.getEntityData();
	              		
	              		if(!message.nbt.hasKey(Reference.MOD_ID, 10)) return;
	              		nbt.setTag(Reference.MOD_ID, (NBTBase)message.nbt.getCompoundTag(Reference.MOD_ID).copy());
	              	} 
	            }
	        });
			return null;
		}
	}
}
