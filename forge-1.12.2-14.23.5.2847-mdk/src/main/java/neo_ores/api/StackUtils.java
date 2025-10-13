package neo_ores.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

public class StackUtils
{
	public static List<ItemStack> asList(ItemStack stack, int clampsize)
	{
		int csize = stack.getCount();
		List<ItemStack> list = new ArrayList<ItemStack>();
		for (int i = 0; i < csize / clampsize; i++)
		{
			ItemStack copied = stack.copy();
			copied.setCount(clampsize);
			list.add(copied);
		}

		if (csize % clampsize > 0)
		{
			ItemStack copied = stack.copy();
			copied.setCount(csize % clampsize);
			list.add(copied);
		}
		return list;
	}

	public static boolean compareWith(ItemStack stackA, ItemStack stackB)
	{
		if (stackB.getItem() == stackA.getItem() && stackB.getItemDamage() == stackA.getItemDamage() && compareNBTWith(stackB, stackA))
		{
			return true;
		}
		return false;
	}

	public static boolean compareNBTWith(ItemStack stack1, ItemStack stack2)
	{
		return (!stack1.hasTagCompound() && stack2.hasTagCompound() || stack1.hasTagCompound() && !stack2.hasTagCompound()) ? false
				: ((!stack1.hasTagCompound() && !stack2.hasTagCompound()) ? true : stack1.getTagCompound().equals(stack2.getTagCompound()));
	}

	public static NBTTagCompound saveAllItems(NBTTagCompound tag, NonNullList<ItemStack> list)
	{
		return saveAllItems(tag, list, true);
	}

	public static NBTTagCompound saveAllItems(NBTTagCompound tag, NonNullList<ItemStack> list, boolean saveEmpty)
	{
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < list.size(); ++i)
		{
			ItemStack itemstack = list.get(i);

			if (!itemstack.isEmpty())
			{
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setInteger("Slot", i);
				itemstack.writeToNBT(nbttagcompound);
				nbttagcompound.removeTag("Count");
				nbttagcompound.setInteger("Count", itemstack.getCount());
				nbttaglist.appendTag(nbttagcompound);
			}
		}

		if (!nbttaglist.hasNoTags() || saveEmpty)
		{
			tag.setTag("Items", nbttaglist);
		}

		return tag;
	}

	public static void loadAllItems(NBTTagCompound tag, NonNullList<ItemStack> list)
	{
		NBTTagList nbttaglist = tag.getTagList("Items", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound.getInteger("Slot");

			if (j >= 0 && j < list.size())
			{
				ItemStack result = new ItemStack(nbttagcompound);
				result.setCount(nbttagcompound.getInteger("Count"));
				list.set(j, result);
			}
		}
	}

	public static NBTTagCompound getNBT(ItemStack stack, boolean removeSize)
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		stack.writeToNBT(nbttagcompound);
		nbttagcompound.removeTag("Count");
		if (!removeSize)
		{
			nbttagcompound.setInteger("Count", stack.getCount());
		}
		return nbttagcompound;
	}
	
	public static ItemStack getItem(NBTTagCompound stack)
	{
		ItemStack result = new ItemStack(stack);
		result.setCount(stack.getInteger("Count"));
		return result;
	}
	
	public static NBTTagCompound convertItemsToNBT(NBTTagCompound tag, List<ItemStack> list)
	{
		NBTTagList nbttaglist = new NBTTagList();

		for (ItemStack stack : list)
		{
			if (!stack.isEmpty())
			{
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				stack.writeToNBT(nbttagcompound);
				nbttagcompound.removeTag("Count");
				nbttagcompound.setInteger("Count", stack.getCount());
				nbttaglist.appendTag(nbttagcompound);
			}
		}

		tag.setTag("Items", nbttaglist);

		return tag;
	}
	
	public static List<ItemStack> convertNBTToItems(NBTTagCompound tag)
	{
		List<ItemStack> list = new ArrayList<>();
		NBTTagList nbttaglist = tag.getTagList("Items", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			ItemStack result = new ItemStack(nbttagcompound);
			result.setCount(nbttagcompound.getInteger("Count"));
			list.add(result);
		}
		return list;
	}
}
