package neo_ores.api;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryUtils 
{
	public static boolean addInventoryfromInventorySlot(int targetindex, IInventory target, IInventory distination)
	{
		if(targetindex < target.getSizeInventory())
		{
			ItemStack targetstack = target.getStackInSlot(targetindex).copy();
			int targetcount = targetstack.getCount();
			int size = distination.getSizeInventory();
			for(int i = 0;i < size;i++)
			{
				ItemStack distinationstack = distination.getStackInSlot(i).copy();
				if(distination.getInventoryStackLimit() <= distination.getStackInSlot(i).getCount() || distination.getStackInSlot(i).getMaxStackSize() <= distination.getStackInSlot(i).getCount() || !distination.isItemValidForSlot(i,targetstack))
					continue;
				if(StackUtils.compareWith(distination.getStackInSlot(i),targetstack))
				{
					int count = targetstack.getCount() + distination.getStackInSlot(i).getCount();
					int min = Math.min(distination.getInventoryStackLimit(), distination.getStackInSlot(i).getMaxStackSize());
					if(count <= min)
					{
						target.removeStackFromSlot(targetindex);
						targetstack = ItemStack.EMPTY;
						distinationstack.setCount(count);
						distination.setInventorySlotContents(i, distinationstack);
					}
					else
					{
						int quantity = min - distination.getStackInSlot(i).getCount();
						target.decrStackSize(targetindex, quantity);
						targetstack.shrink(quantity);
						distinationstack.setCount(min);
						distination.setInventorySlotContents(i, distinationstack);
					}
				}
				
				if(targetstack.isEmpty()) return true;
			}
			
			for(int i = 0;i < size;i++)
			{
				ItemStack distinationstack = targetstack.copy();
				if(distination.getInventoryStackLimit() <= distination.getStackInSlot(i).getCount() || distination.getStackInSlot(i).getMaxStackSize() <= distination.getStackInSlot(i).getCount() || !distination.isItemValidForSlot(i,targetstack))
					continue;
				if(distination.getStackInSlot(i).isEmpty())
				{
					int count = targetstack.getCount();
					int min = Math.min(distination.getInventoryStackLimit(), distination.getStackInSlot(i).getMaxStackSize());
					if(count <= min)
					{
						target.removeStackFromSlot(targetindex);
						targetstack = ItemStack.EMPTY;
						distinationstack.setCount(count);
						distination.setInventorySlotContents(i, distinationstack);
					}
					else
					{
						target.decrStackSize(targetindex, min);
						targetstack.shrink(min);
						distinationstack.setCount(min);
						distination.setInventorySlotContents(i, distinationstack);
					}
				}
				
				if(targetstack.isEmpty()) return true;
			}
			return targetcount != targetstack.getCount();
		}
		return false;
	}
	
	public static boolean addInventoryfromStack(ItemStack stack, IInventory distination)
	{
		ItemStack targetstack = stack.copy();
		int targetcount = targetstack.getCount();
		int size = distination.getSizeInventory();
		for(int i = 0;i < size;i++)
		{
			ItemStack distinationstack = distination.getStackInSlot(i).copy();
			if(distination.getInventoryStackLimit() <= distination.getStackInSlot(i).getCount() || distination.getStackInSlot(i).getMaxStackSize() <= distination.getStackInSlot(i).getCount() || !distination.isItemValidForSlot(i,targetstack))
				continue;
			if(StackUtils.compareWith(distination.getStackInSlot(i),targetstack))
			{
				int count = targetstack.getCount() + distination.getStackInSlot(i).getCount();
				int min = Math.min(distination.getInventoryStackLimit(), distination.getStackInSlot(i).getMaxStackSize());
				if(count <= min)
				{
					stack = ItemStack.EMPTY;
					targetstack = ItemStack.EMPTY;
					distinationstack.setCount(count);
					distination.setInventorySlotContents(i, distinationstack);
				}
				else
				{
					int quantity = min - distination.getStackInSlot(i).getCount();
					stack.shrink(quantity);
					targetstack.shrink(quantity);
					distinationstack.setCount(min);
					distination.setInventorySlotContents(i, distinationstack);
				}
			}
			
			if(targetstack.isEmpty()) return true;
		}
		
		for(int i = 0;i < size;i++)
		{
			ItemStack distinationstack = targetstack.copy();
			if(distination.getInventoryStackLimit() <= distination.getStackInSlot(i).getCount() || distination.getStackInSlot(i).getMaxStackSize() <= distination.getStackInSlot(i).getCount() || !distination.isItemValidForSlot(i,targetstack))
				continue;
			if(distination.getStackInSlot(i).isEmpty())
			{
				int count = targetstack.getCount();
				int min = Math.min(distination.getInventoryStackLimit(), distination.getStackInSlot(i).getMaxStackSize());
				if(count <= min)
				{
					stack = ItemStack.EMPTY;
					targetstack = ItemStack.EMPTY;
					distinationstack.setCount(count);
					distination.setInventorySlotContents(i, distinationstack);
				}
				else
				{
					stack.shrink(min);
					targetstack.shrink(min);
					distinationstack.setCount(min);
					distination.setInventorySlotContents(i, distinationstack);
				}
			}
			
			if(targetstack.isEmpty()) return true;
		}
		return targetcount != targetstack.getCount();
	}
}
