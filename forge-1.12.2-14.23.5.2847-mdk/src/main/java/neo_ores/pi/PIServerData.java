package neo_ores.pi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import neo_ores.api.StackUtils;
import neo_ores.inventory.ContainerPIBase;
import neo_ores.main.NeoOres;
import neo_ores.packet.PacketPISyncToClient;
import neo_ores.util.PIUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.items.IItemHandler;

public class PIServerData
{
	private final List<BlockPos> posList;
	private int dim;
	private EnumFacing facing;
	private PICommand command;

	public PIServerData()
	{
		this.posList = new ArrayList<>();
		this.dim = 0;
	}

	public void setCommand(PICommand command)
	{
		this.command = command;
	}

	
	// TODO Request in Server side
	public void update(UUID uuid, EntityPlayerMP player, MinecraftServer server)
	{
		NBTTagCompound compound = new NBTTagCompound();
		if (this.command != null)
		{
			if (!DimensionManager.isWorldQueuedToUnload(this.dim) && DimensionManager.getWorld(this.dim) != null) 
			{
				if (player.openContainer != null && player.openContainer instanceof ContainerPIBase)
				{
					WorldServer world = DimensionManager.getWorld(this.dim);
					List<IItemHandler> handlers = PIUtils.getPedestalsItems(this.posList, world, this.facing);
					this.command.execute(handlers);
					compound = this.command.syncToClient(compound);
					if (this.command.getPickup()) 
					{
						player.inventory.setItemStack(this.command.getResult().copy());
					}
					else if (this.command.getTransfer()) 
					{
						int slot = this.command.getSlot();
						if (0 <= slot && slot < player.openContainer.inventorySlots.size())
						{
							player.openContainer.putStackInSlot(slot, this.command.getResult().copy());
							player.openContainer.detectAndSendChanges();
						}
						else 
						{
							ItemStack tempStack = this.command.getResult().copy();
							player.inventory.addItemStackToInventory(tempStack);
							player.inventoryContainer.detectAndSendChanges();
							if (!tempStack.isEmpty()) 
							{
								player.dropItem(tempStack, false);
							}
						}
					}
					else if (this.command.getThrow() && !this.command.getResult().isEmpty()) 
					{
						player.dropItem(this.command.getResult(), false);
					}
					List<ItemStack> list = PIUtils.getItemList(handlers);
					compound = StackUtils.convertItemsToNBT(compound, list);
					NeoOres.PACKET.sendTo(new PacketPISyncToClient(compound), player);
				}
			}

			this.command = null;
		}
	}

	public void setDim(int dim)
	{
		this.dim = dim;
	}

	public void setFace(EnumFacing face)
	{
		this.facing = face;
	}

	public void setPosList(List<BlockPos> pos)
	{
		this.posList.clear();
		this.posList.addAll(pos);
	}

	public List<BlockPos> getPosList()
	{
		return this.posList;
	}

	public int getDim()
	{
		return this.dim;
	}

	public EnumFacing getFace()
	{
		return this.facing;
	}
}
