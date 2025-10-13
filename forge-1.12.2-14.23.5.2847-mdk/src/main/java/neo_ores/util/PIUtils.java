package neo_ores.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import neo_ores.api.StackUtils;
import neo_ores.tileentity.AbstractTileEntityPedestal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class PIUtils
{
	public static List<AbstractTileEntityPedestal> getPedestals(List<BlockPos> list, World world)
	{
		List<AbstractTileEntityPedestal> result = new ArrayList<>();
		for (BlockPos pos : list)
		{
			if (world.isAreaLoaded(pos, pos))
			{
				TileEntity te = world.getTileEntity(pos);
				if (te != null && te instanceof AbstractTileEntityPedestal)
				{
					result.add((AbstractTileEntityPedestal) te);
				}
			}
		}
		return result;
	}

	public static List<IItemHandler> getPedestalsItems(List<BlockPos> list, World world, EnumFacing facing)
	{
		List<IItemHandler> result = new ArrayList<>();
		for (BlockPos pos : list)
		{
			if (world.isAreaLoaded(pos, pos))
			{
				TileEntity te = world.getTileEntity(pos);
				if (te != null && te instanceof AbstractTileEntityPedestal)
				{
					IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
					if (handler != null)
					{
						result.add(handler);
					}
				}
			}
		}
		return result;
	}

	public static List<ItemStack> getItemList(List<IItemHandler> handlers)
	{
		Map<NBTTagCompound, Integer> itemList = new HashMap<>();
		for (IItemHandler handler : handlers)
		{
			for (int i = 0; i < handler.getSlots(); i++)
			{
				ItemStack stack = handler.getStackInSlot(i);
				if (stack.isEmpty())
				{
					continue;
				}
				NBTTagCompound nbt = StackUtils.getNBT(stack, true);
				if (!itemList.containsKey(nbt))
				{
					itemList.put(nbt, 0);
				}
				itemList.put(nbt, itemList.get(nbt) + stack.getCount());
			}
		}
		List<ItemStack> result = new ArrayList<>();
		for (NBTTagCompound key : itemList.keySet())
		{
			ItemStack stack = StackUtils.getItem(key);
			stack.setCount(itemList.get(key));
			result.add(stack);
		}
		return result;
	}
}
