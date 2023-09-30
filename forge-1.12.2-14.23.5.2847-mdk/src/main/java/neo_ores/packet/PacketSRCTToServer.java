package neo_ores.packet;

import io.netty.buffer.ByteBuf;
import neo_ores.tileentity.TileEntitySpellRecipeCreationTable;
import neo_ores.util.SpellUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSRCTToServer  implements IMessage 
{
	private NBTTagCompound nbt;
	
	public PacketSRCTToServer(NBTTagCompound data) 
	{
	    this.nbt = data;
	}
	
	public PacketSRCTToServer() {}
	
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
	
	public static class Handler implements IMessageHandler<PacketSRCTToServer, IMessage>
	{
		@Override
		public IMessage onMessage(PacketSRCTToServer message, final MessageContext ctx) 
		{
			WorldServer worldServer = (WorldServer)(ctx.getServerHandler()).player.world;
		    worldServer.addScheduledTask(new Runnable() 
			{
				public void run() 
	            {
	            	int x = message.nbt.getInteger("x");
	            	int y = message.nbt.getInteger("y");
	            	int z = message.nbt.getInteger("z");
	            	TileEntity te = worldServer.getTileEntity(new BlockPos(x,y,z));
	            	if(te instanceof TileEntitySpellRecipeCreationTable)
	            	{
	            		TileEntitySpellRecipeCreationTable tesrct = ((TileEntitySpellRecipeCreationTable)te);
	            		tesrct.srctSearch = message.nbt.getString("search");
	            		tesrct.setSpellItems(SpellUtils.getListFromNBT(message.nbt.getCompoundTag("recipeSpells")));
	            		ItemStack sentStack = new ItemStack(message.nbt.getCompoundTag("recipeItem"));
	            		if(tesrct.getStackInSlot(0).getItem() == sentStack.getItem() && tesrct.getStackInSlot(0).getMetadata() == sentStack.getMetadata())tesrct.setInventorySlotContents(0, sentStack);
	            	}
	            }
	        });
			return null;
		}		
	}
}
