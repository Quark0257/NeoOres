package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.api.PlayerManaDataServer;
import neo_ores.api.SpellUtils;
import neo_ores.api.StudyItemManagerServer;
import neo_ores.main.Reference;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/*
 * PacketManaDataToServer pmds = new PacketManaDataToServer(mc.player.getEntityId());
 * NeoOres.PACKET.sendToServer((IMessage)pmds);
 */

//Not Using
public class PacketManaDataToServer implements IMessage 
{
	private NBTTagCompound nbt;
	
	public PacketManaDataToServer(NBTTagCompound entitydata) 
	{
	    this.nbt = entitydata;
	}
	
	public PacketManaDataToServer() {}
	
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
	
	public static class Handler implements IMessageHandler<PacketManaDataToServer, IMessage>
	{
		@Override
		public IMessage onMessage(PacketManaDataToServer message, final MessageContext ctx) 
		{
			WorldServer worldServer = (WorldServer)(ctx.getServerHandler()).player.world;
			EntityPlayerMP player = ctx.getServerHandler().player;
		    worldServer.addScheduledTask(new Runnable() 
			{
				public void run() 
	            {
	            	if(message.nbt.hasKey(Reference.MOD_ID,10) && message.nbt.getCompoundTag(Reference.MOD_ID).hasKey(SpellUtils.NBTTagUtils.MAGIC, 10)
	            			&& message.nbt.getCompoundTag(Reference.MOD_ID).hasKey(SpellUtils.NBTTagUtils.STUDY, 10))
	            	{
	            		PlayerManaDataServer pmds = new PlayerManaDataServer(player);
	            		StudyItemManagerServer sims = new StudyItemManagerServer(player);
	            		
	            		NBTTagCompound magic = message.nbt.getCompoundTag(Reference.MOD_ID).getCompoundTag(SpellUtils.NBTTagUtils.MAGIC).copy();
	            		NBTTagCompound study = message.nbt.getCompoundTag(Reference.MOD_ID).getCompoundTag(SpellUtils.NBTTagUtils.STUDY).copy();
	            		
	            		//pmds.setLevel(magic.getLong(SpellUtils.NBTTagUtils.LEVEL));
	            		//pmds.setMana(magic.getLong(SpellUtils.NBTTagUtils.MANA));
	            		//pmds.setMXP(magic.getLong(SpellUtils.NBTTagUtils.MXP));
	            		pmds.setMagicPoint(magic.getLong(SpellUtils.NBTTagUtils.MAGIC_POINT));
	            		//pmds.setMaxMana(magic.getLong(SpellUtils.NBTTagUtils.MAX_MANA));
	            		
	            		for(String modid : study.getKeySet())
	            		{
	            			if(study.hasKey(modid, 9))
	            			{
	            				for(int i = 0;i < study.getTagList(modid, 8).tagCount();i++)
	            				{
	            					sims.set(modid, study.getTagList(modid, 8).getStringTagAt(i));
	            				}
	            			}
	            		}
	            	}
	            }
	        });
			return null;
		}		
	}
}
