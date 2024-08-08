package neo_ores.api;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.util.FakePlayer;

public class InventoryUtils
{
	public static boolean addInventoryfromInventorySlot(int targetindex, IInventory target, IInventory distination)
	{
		if (targetindex < target.getSizeInventory())
		{
			ItemStack targetstack = target.getStackInSlot(targetindex).copy();
			if (targetstack.isEmpty())
				return false;
			int targetcount = targetstack.getCount();
			int size = distination.getSizeInventory();
			for (int i = 0; i < size; i++)
			{
				ItemStack distinationstack = distination.getStackInSlot(i).copy();
				if (distination.getInventoryStackLimit() <= distination.getStackInSlot(i).getCount())
					continue;
				if (distination.getStackInSlot(i).getMaxStackSize() <= distination.getStackInSlot(i).getCount())
					continue;
				if (!distination.isItemValidForSlot(i, targetstack))
					continue;
				if (distination instanceof InventoryPlayer)
				{
					if (((InventoryPlayer) distination).mainInventory.size() <= i)
						continue;
				}
				if (StackUtils.compareWith(distination.getStackInSlot(i), targetstack))
				{
					int count = targetstack.getCount() + distination.getStackInSlot(i).getCount();
					int min = Math.min(distination.getInventoryStackLimit(), distination.getStackInSlot(i).getMaxStackSize());
					if (count <= min)
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

				if (targetstack.isEmpty())
					return true;
			}

			for (int i = 0; i < size; i++)
			{
				ItemStack distinationstack = targetstack.copy();
				if (distination.getInventoryStackLimit() <= distination.getStackInSlot(i).getCount() || distination.getStackInSlot(i).getMaxStackSize() <= distination.getStackInSlot(i).getCount()
						|| !distination.isItemValidForSlot(i, targetstack))
					continue;
				if (distination instanceof InventoryPlayer)
				{
					if (((InventoryPlayer) distination).mainInventory.size() <= i)
						continue;
				}
				if (distination.getStackInSlot(i).isEmpty())
				{
					int count = targetstack.getCount();
					int min = Math.min(distination.getInventoryStackLimit(), distination.getStackInSlot(i).getMaxStackSize());
					if (count <= min)
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

				if (targetstack.isEmpty())
					return true;
			}
			return targetcount != targetstack.getCount();
		}
		return false;
	}

	public static ItemStack addInventoryfromStack(ItemStack stack, IInventory distination)
	{
		if (stack.isEmpty())
			return ItemStack.EMPTY;
		ItemStack targetstack = stack.copy();
		int size = distination.getSizeInventory();
		for (int i = 0; i < size; i++)
		{
			ItemStack distinationstack = distination.getStackInSlot(i).copy();
			if (distination.getInventoryStackLimit() <= distination.getStackInSlot(i).getCount() || distination.getStackInSlot(i).getMaxStackSize() <= distination.getStackInSlot(i).getCount()
					|| !distination.isItemValidForSlot(i, targetstack))
				continue;
			if (distination instanceof InventoryPlayer)
			{
				if (((InventoryPlayer) distination).mainInventory.size() <= i)
					continue;
			}
			if (StackUtils.compareWith(distination.getStackInSlot(i), targetstack))
			{
				int count = targetstack.getCount() + distination.getStackInSlot(i).getCount();
				int min = Math.min(distination.getInventoryStackLimit(), distination.getStackInSlot(i).getMaxStackSize());
				if (count <= min)
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

			if (targetstack.isEmpty())
				return stack;
		}

		for (int i = 0; i < size; i++)
		{
			ItemStack distinationstack = targetstack.copy();
			if (distination.getInventoryStackLimit() <= distination.getStackInSlot(i).getCount() || distination.getStackInSlot(i).getMaxStackSize() <= distination.getStackInSlot(i).getCount()
					|| !distination.isItemValidForSlot(i, targetstack))
				continue;
			if (distination instanceof InventoryPlayer)
			{
				if (((InventoryPlayer) distination).mainInventory.size() <= i)
					continue;
			}
			if (distination.getStackInSlot(i).isEmpty())
			{
				int count = targetstack.getCount();
				int min = Math.min(distination.getInventoryStackLimit(), distination.getStackInSlot(i).getMaxStackSize());
				if (count <= min)
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

			if (targetstack.isEmpty())
				return stack;
		}
		return stack;
	}

	public static void addStackToPlayer(EntityPlayer entityplayer, ItemStack itemstack)
	{
		if (!itemstack.isEmpty() && entityplayer.isServerWorld())
		{
			boolean flag = (entityplayer instanceof FakePlayer) ? false : entityplayer.inventory.addItemStackToInventory(itemstack);

			if (flag)
			{
				entityplayer.world.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
						((entityplayer.getRNG().nextFloat() - entityplayer.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
				entityplayer.inventoryContainer.detectAndSendChanges();
			}

			if (flag && itemstack.isEmpty())
			{
			}
			else
			{
				EntityItem entityitem = entityplayer.dropItem(itemstack, false);

				if (entityitem != null)
				{
					entityitem.setNoPickupDelay();
					entityitem.setOwner(entityplayer.getName());
				}
			}
		}
	}

	public static IInventory getPlayerInventory(EntityPlayer player)
	{
		if (player instanceof FakePlayer && player instanceof HasInventory)
			return ((HasInventory) player).getInventory();
		return player.inventory;
	}
}
