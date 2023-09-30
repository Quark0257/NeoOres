package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.main.NeoOres;
import neo_ores.tileentity.TileEntitySpellRecipeCreationTable;
import neo_ores.util.SpellUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSRCTToClient  implements IMessage 
{
	private NBTTagCompound nbt;
	
	public PacketSRCTToClient(NBTTagCompound data) 
	{
	    this.nbt = data;
	}
	
	public PacketSRCTToClient() {}
	
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
	
	public static class Handler implements IMessageHandler<PacketSRCTToClient, IMessage>
	{
		@Override
		public IMessage onMessage(PacketSRCTToClient message, final MessageContext ctx) 
		{
		    Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
	            public void run() 
	            {
	            	World world = NeoOres.proxy.getClientWorld();
	            	int x = message.nbt.getInteger("x");
	            	int y = message.nbt.getInteger("y");
	            	int z = message.nbt.getInteger("z");
	            	TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
	            	if(te instanceof TileEntitySpellRecipeCreationTable)
	            	{
	            		TileEntitySpellRecipeCreationTable tesrct = ((TileEntitySpellRecipeCreationTable)te);
	            		tesrct.srctSearch = message.nbt.getString("search");
	            		tesrct.setSpellItems(SpellUtils.getListFromNBT(message.nbt.getCompoundTag("recipeSpells")));
	            	}
	            }
	        });
			return null;
		}		
	}
}
