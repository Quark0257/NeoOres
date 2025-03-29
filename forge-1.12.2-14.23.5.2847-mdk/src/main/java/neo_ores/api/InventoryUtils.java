package neo_ores.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class InventoryUtils
{
	public static boolean addInventoryfromInventorySlot(int targetindex, IInventory target, IInventory distination, @Nullable EnumFacing facingTarget, @Nullable EnumFacing facingDist)
	{
		if (targetindex < target.getSizeInventory())
		{
			ItemStack targetstack = target.getStackInSlot(targetindex).copy();
			if (target instanceof ISidedInventory && facingTarget != null)
			{
				if (!((ISidedInventory) target).canExtractItem(targetindex, targetstack, facingTarget)
						|| !Arrays.stream(((ISidedInventory) target).getSlotsForFace(facingTarget)).boxed().collect(Collectors.toList()).contains(targetindex))
				{
					return false;
				}
			}
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
				if (distination instanceof ISidedInventory && facingDist != null)
				{
					if (!((ISidedInventory) distination).canInsertItem(i, targetstack, facingDist)
							|| !Arrays.stream(((ISidedInventory) distination).getSlotsForFace(facingDist)).boxed().collect(Collectors.toList()).contains(i))
					{
						continue;
					}
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
				if (distination instanceof ISidedInventory && facingDist != null)
				{
					if (!((ISidedInventory) distination).canInsertItem(i, targetstack, facingDist)
							|| !Arrays.stream(((ISidedInventory) distination).getSlotsForFace(facingDist)).boxed().collect(Collectors.toList()).contains(i))
					{
						continue;
					}
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

	public static ItemStack addInventoryfromStack(ItemStack stack, IInventory distination, @Nullable EnumFacing facing)
	{
		if (stack.isEmpty())
			return ItemStack.EMPTY;
		ItemStack targetstack = stack.copy();
		int size = distination.getSizeInventory();
		// for slots are not empty
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
			if (distination instanceof ISidedInventory && facing != null)
			{
				if (!((ISidedInventory) distination).canInsertItem(i, targetstack, facing)
						|| !Arrays.stream(((ISidedInventory) distination).getSlotsForFace(facing)).boxed().collect(Collectors.toList()).contains(i))
				{
					continue;
				}
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
		// for slots are empty
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
			if (distination instanceof ISidedInventory && facing != null)
			{
				if (!((ISidedInventory) distination).canInsertItem(i, targetstack, facing)
						|| !Arrays.stream(((ISidedInventory) distination).getSlotsForFace(facing)).boxed().collect(Collectors.toList()).contains(i))
				{
					continue;
				}
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

	public static Map<Integer, ItemStack> getInventoryStackList(IInventory target, boolean exceptHoldItem, @Nullable EnumFacing facing)
	{
		Map<Integer, ItemStack> map = new HashMap<Integer, ItemStack>();
		int size = target.getSizeInventory();
		for (int i = 0; i < size; i++)
		{
			if (target instanceof InventoryPlayer)
			{
				if (((InventoryPlayer) target).mainInventory.size() <= i || (exceptHoldItem && ((InventoryPlayer) target).currentItem == i))
					continue;
			}
			if (target instanceof ISidedInventory && facing != null)
			{
				if (!Arrays.stream(((ISidedInventory) target).getSlotsForFace(facing)).boxed().collect(Collectors.toList()).contains(i))
				{
					continue;
				}
			}
			map.put(i, target.getStackInSlot(i));
		}
		return map;
	}

	public static FluidStack addFluidToInventory(FluidStack stack, IInventory inventory, @Nullable EnumFacing facing)
	{
		Map<Integer, ItemStack> map = getInventoryStackList(inventory, false, facing);
		for (int key : map.keySet()) {
			ItemStack itemStack = map.get(key);
			if (itemStack.getItem() == Items.BUCKET) {
				if (itemStack.getCount() == 1) {
					itemStack.shrink(1);
					if (stack.amount >= 1000) {
						FluidStack newFluidStack = stack.copy();
						newFluidStack.amount = 1000;
						stack.amount -= 1000;
						ItemStack newStack = FluidUtil.getFilledBucket(newFluidStack);
						addInventoryfromStack(newStack, inventory, null);
					}
				} else {
					if (stack.amount >= 1000) {
						FluidStack newFluidStack = stack.copy();
						newFluidStack.amount = 1000;
						ItemStack newStack = FluidUtil.getFilledBucket(newFluidStack);
						if (addInventoryfromStack(newStack, inventory, null).isEmpty()) {
							itemStack.shrink(1);
							stack.amount -= 1000;
						}
					}
				}
			}
		}
		return stack;
	}
	
	public static Map<Integer, FluidStack> getFluidFromInventory(IInventory inventory, boolean exceptHoldItem, @Nullable EnumFacing facing)
	{
		Map<Integer, FluidStack> map = new HashMap<Integer, FluidStack>();
		int size = inventory.getSizeInventory();
		for (int i = 0; i < size; i++) {
			if (inventory instanceof InventoryPlayer)
			{
				if (((InventoryPlayer) inventory).mainInventory.size() <= i || (exceptHoldItem && ((InventoryPlayer) inventory).currentItem == i))
					continue;
			}
			if (inventory instanceof ISidedInventory && facing != null)
			{
				if (!Arrays.stream(((ISidedInventory) inventory).getSlotsForFace(facing)).boxed().collect(Collectors.toList()).contains(i))
				{
					continue;
				}
			}
			
			ItemStack stack = inventory.getStackInSlot(i).copy();
			if (stack.getItem() == ForgeModContainer.getInstance().universalBucket) {
				FluidStack fluid = FluidUtil.getFluidContained(stack);
				if (fluid != null && fluid.getFluid() != null) {
					map.put(i, fluid);
				}
			}
		}
		
		return map;
	}
	
	public static boolean addFluidToInventoryFromTank(IFluidHandler handler, IInventory inventory, @Nullable EnumFacing facing) {
		FluidStack stack = handler.drain(1000, false).copy();
		if (stack.amount >= 0) {
			stack = addFluidToInventory(stack, inventory, facing);
			int d = handler.drain(1000, false).amount - stack.amount;
			handler.drain(d, true);
			return d > 0;
		}
		return false;
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
