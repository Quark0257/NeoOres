package neo_ores.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import neo_ores.api.StackUtils;
import neo_ores.tileentity.DetectorWrapper;
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
		result = new ArrayList<>(result.stream().sorted(Comparator.comparingInt(new ToIntFunction<AbstractTileEntityPedestal>()
		{
			@Override
			public int applyAsInt(AbstractTileEntityPedestal arg0)
			{
				return -arg0.getPriority();
			}
		})).collect(Collectors.toList()));
		return result;
	}

	public static List<IItemHandler> getPedestalsItems(List<BlockPos> list, World world, EnumFacing facing)
	{
		List<IItemHandler> result = new ArrayList<>();
		for (AbstractTileEntityPedestal te : getPedestals(list, world))
		{
			IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
			if (handler != null)
			{
				result.add(handler);
			}
		}
		return result;
	}

	public static List<ItemStack> getItemList(List<IItemHandler> handlers)
	{
		Map<NBTTagCompound, Integer> itemList = new HashMap<>();
		List<IItemHandler> detectors = new ArrayList<>();
		for (IItemHandler handler : handlers)
		{
			if (handler instanceof DetectorWrapper)
			{
				detectors.add(handler);
				continue;
			}
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
		for (IItemHandler handler : detectors)
		{
			for (int i = 0; i < handler.getSlots(); i++)
			{
				ItemStack stack = handler.getStackInSlot(i).copy();
				if (stack.isEmpty())
				{
					continue;
				}
				NBTTagCompound nbt = StackUtils.getNBT(stack, true);
				if (!itemList.containsKey(nbt))
				{
					ItemStack copiedStack = StackUtils.getItem(nbt);
					copiedStack.setCount(1);
					nbt = StackUtils.getNBT(addRequestableTag(copiedStack), true);
					itemList.put(nbt, 1);
				}
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

	public static ItemStack addRequestableTag(ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setBoolean("requestable", true);
		return stack;
	}

	public static boolean hasRequestableTag(ItemStack stack)
	{
		if (stack.hasTagCompound())
		{
			if (stack.getTagCompound().hasKey("requestable"))
			{
				return stack.getTagCompound().getBoolean("requestable");
			}
		}
		return false;
	}

	public static ItemStack removeRequestableTag(ItemStack stack)
	{
		if (stack.hasTagCompound())
		{
			if (stack.getTagCompound().hasKey("requestable"))
			{
				stack.getTagCompound().removeTag("requestable");
				if (stack.getTagCompound().hasNoTags())
				{
					stack.setTagCompound(null);
				}
			}
		}
		return stack;
	}
}
