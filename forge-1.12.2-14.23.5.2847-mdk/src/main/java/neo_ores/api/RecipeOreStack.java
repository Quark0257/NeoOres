package neo_ores.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeOreStack
{
	private final Object object;
	private final int size;

	public RecipeOreStack(Object object, int size)
	{
		this.object = object;
		this.size = size;
	}

	public boolean isItemStack()
	{
		return (this.object instanceof ItemStack);
	}

	public int getSize()
	{
		return size;
	}

	public boolean isEmpty()
	{
		if (this.size <= 0)
			return false;
		if (this.isItemStack())
			return ((ItemStack) this.object).isEmpty();
		return object == null || !(object instanceof String);
	}

	public ItemStack getStack()
	{
		return this.isItemStack() ? ((ItemStack) this.object) : ItemStack.EMPTY;
	}

	public boolean isOreDic()
	{
		return !this.isItemStack() && !this.isEmpty();
	}

	public String getOreDic()
	{
		return (String) object;
	}

	public List<ItemStack> getListFromOreDic()
	{
		List<ItemStack> list = new ArrayList<ItemStack>();
		if (this.isOreDic())
		{
			list.addAll(OreDictionary.getOres((String) this.object));
		}
		return list;
	}

	public List<ItemStack> getListTogether()
	{
		List<ItemStack> list = new ArrayList<ItemStack>();
		if (this.isItemStack())
			list.add(getStack());
		else if (this.isOreDic())
			list.addAll(getListFromOreDic());
		return list;
	}

	public boolean compareStackWith(ItemStack stack)
	{
		return this.isItemStack() ? compareWith(stack, this.getStack()) : false;
	}

	public boolean compareOreDicWith(ItemStack stack)
	{
		if (this.isOreDic())
		{
			for (ItemStack stack1 : this.getListFromOreDic())
			{
				if (compareWith(stack, stack1))
					return true;
			}
		}
		return false;
	}

	public boolean compareWith(ItemStack stack)
	{
		if (this.isOreDic())
			return this.compareOreDicWith(stack);
		else if (this.isItemStack())
			return this.compareStackWith(stack);
		return false;
	}

	/**
	 * @param stack1 means {@code target}
	 * @param stack2 means {@code recipe}
	 */
	public static boolean compareWith(ItemStack stack1, ItemStack stack2)
	{
		if (stack1.getItem() == stack2.getItem() && stack1.getItemDamage() == stack2.getItemDamage() && compareNBTWith(stack1, stack2))
		{
			return true;
		}
		return false;
	}

	/**
	 * @param stack1 means {@code target}
	 * @param stack2 means {@code recipe}
	 */
	public static boolean compareNBTWith(ItemStack stack1, ItemStack stack2)
	{
		return (!stack1.hasTagCompound() && stack2.hasTagCompound()) ? false
				: ((!stack1.hasTagCompound() && !stack2.hasTagCompound() || stack1.hasTagCompound() && !stack2.hasTagCompound()) ? true : stack1.getTagCompound().equals(stack2.getTagCompound()));
	}

	public String toString()
	{
		return this.getStack().toString() + ": x" + this.getSize();
	}
}
