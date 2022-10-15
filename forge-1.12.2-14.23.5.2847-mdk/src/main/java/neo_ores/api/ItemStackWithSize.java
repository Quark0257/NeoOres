package neo_ores.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

public class  ItemStackWithSize
{
    private final ItemStack stack;
    private ItemStack mediate;
    private int size;
    public static final ItemStackWithSize EMPTY = new ItemStackWithSize(ItemStack.EMPTY,0);

    public ItemStackWithSize(ItemStack itemstack, int size)
    {
    	stack = (itemstack == null) ? ItemStack.EMPTY : itemstack;
    	stack.setCount(1);
        this.size = size;
        ItemStack stack = itemstack.copy();
        stack.setCount(size);
        this.setMediate(stack);
    }

    public ItemStack getStack()
    {
    	this.updateStack();
        return this.isEmpty() ? ItemStack.EMPTY : stack;
    }

    public int getSize()
    {
    	this.updateStack();
        return (size < 0) ? 0 : size;
    }
    
    public void addSize(int count)
    {
    	this.setSize((size + count < 0 || size + count > Integer.MAX_VALUE) ? 0 : (size + count));
    }
    
    public void setSize(int count) 
    {
    	this.updateStack();
    	this.size = count;
    	this.mediate.setCount(count);
    }

    public List<ItemStack> asList(int clampsize)
    {
    	this.updateStack();
    	int csize = this.size;
        List<ItemStack> list = new ArrayList<ItemStack>();
        for(int i = 0;i < csize / clampsize;i++)
        {
            ItemStack copied = this.stack.copy();
            copied.setCount(clampsize);
            list.add(copied);
        }

        if(csize % clampsize > 0)
        {
            ItemStack copied = this.stack.copy();
            copied.setCount(csize % clampsize);
            list.add(copied);
        }
        return list;
    }

    public boolean addStack(ItemStack stack)
    {
        if(this.compareWith(stack))
        {
            this.addSize(stack.getCount());
            return true;
        }
        return false;
    }
    
    @SuppressWarnings("static-access")
	public boolean compareWith(ItemStack stack)
    {
    	if(stack.getItem() == this.stack.getItem() && stack.getItemDamage() == this.stack.getItemDamage() &&  this.compareNBTWith(stack, getStack()))
        {
            return true;
        }
        return false;
    }
    
    public static boolean compareNBTWith(ItemStack stack1,ItemStack stack2)
    {
    	return (!stack1.hasTagCompound() && stack2.hasTagCompound() || stack1.hasTagCompound() && !stack2.hasTagCompound()) ? false : ((!stack1.hasTagCompound() && !stack2.hasTagCompound()) ? true : stack1.getTagCompound().equals(stack2.getTagCompound()));
    }
    
    public boolean isEmpty()
    {
    	this.updateStack();
    	return this.stack.isEmpty() || this.size <= 0;
    }
    
    public static void setToNBT(NonNullList<ItemStackWithSize> list,NBTTagCompound nbt)
    {
    	NBTTagList taglist = new NBTTagList();
    	for(ItemStackWithSize stackWS : list)
    	{
    		stackWS.updateStack();
    		NBTTagCompound item = new NBTTagCompound();
    		item.setString("id", stackWS.getStack().getItem().getRegistryName().toString());
    		item.setInteger("damage", stackWS.getStack().getItemDamage());
    		item.setInteger("size",stackWS.getSize());
    		if(stackWS.getStack().hasTagCompound()) item.setTag("tag", stackWS.getStack().getTagCompound());
    		taglist.appendTag(item);
    	}
    	nbt.setTag("ItemsWithSize", taglist);
    }
    
    public static void getFromNBT(NonNullList<ItemStackWithSize> list,NBTTagCompound nbt)
    {
    	NBTTagCompound copied = nbt.copy();
    	if(copied.hasKey("ItemsWithSize", 9))
    	{
    		NBTTagList taglist = copied.getTagList("ItemsWithSize",10);
    		for(int i = 0;i < taglist.tagCount();i++)
    		{
    			NBTTagCompound item = taglist.getCompoundTagAt(i);
    			Item d = Item.getByNameOrId(item.getString("id"));
    			if(d != null)
    			{
    				ItemStack stack = new ItemStack(d,1,item.getInteger("damage"));
    				if(item.hasKey("tag", 10)) stack.setTagCompound(item.getCompoundTag("tag"));
    				list.set(i,new ItemStackWithSize(stack,item.getInteger("size")));
    			}
    		}
    	}
    }
    
    public String toString()
    {
    	return this.getStack().toString() + ": x" + this.getSize();
    }
    
    public ItemStackWithSize copy()
    {
    	return new ItemStackWithSize(this.stack.copy(),this.size);
    }
    
    public ItemStackWithSize split(int amount)
    {
        int i = Math.min(amount, this.size);
        ItemStackWithSize itemstack = this.copy();
        itemstack.setSize(i);
        this.addSize(-i);
        return itemstack;
    }
    
    private void setMediate(ItemStack stack)
    {
    	this.mediate = stack;
    }
    
    public ItemStack getMediate()
    {
    	return this.mediate;
    }
    
    public void updateStack()
    {
    	this.size = this.mediate.getCount();
    }
}
