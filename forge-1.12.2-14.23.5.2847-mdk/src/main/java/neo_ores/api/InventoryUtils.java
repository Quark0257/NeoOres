package neo_ores.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import neo_ores.inventory.WrapperPlayerInventory;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class InventoryUtils
{	
	public static boolean addInventoryFromInventorySlot(IInventory target, IInventory destination, @Nullable EnumFacing facingTarget, @Nullable EnumFacing facingDest) 
	{
		return InventoryUtils.addInventoryFromInventorySlot(target, destination, facingTarget, facingDest, new Predicate<ItemStack>() {
			@Override
			public boolean apply(ItemStack input)
			{
				return true;
			}});
	}
	
	public static boolean addInventoryFromInventorySlot(IInventory target, IInventory destination, @Nullable EnumFacing facingTarget, @Nullable EnumFacing facingDest, @Nonnull Predicate<ItemStack> filter) 
	{
		if (target instanceof ICapabilityProvider && destination instanceof ICapabilityProvider) 
		{
			IItemHandler targetHandler = ((ICapabilityProvider) target).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facingTarget);
			IItemHandler destHandler = ((ICapabilityProvider) destination).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facingDest);
			if (targetHandler != null && destHandler != null) 
			{
				for (int targetSlot = 0; targetSlot < targetHandler.getSlots(); targetSlot++) 
				{
					ItemStack stack = targetHandler.getStackInSlot(targetSlot).copy();
					if (!stack.isEmpty() && filter.apply(stack)) 
					{
						for (int destSlot = 0; destSlot < destHandler.getSlots(); destSlot++) 
						{
							if (destHandler.isItemValid(destSlot, stack) && !destHandler.getStackInSlot(destSlot).isEmpty()) 
							{
								ItemStack remainStack = destHandler.insertItem(destSlot, stack, true);
								int amount = stack.getCount() - remainStack.getCount();
								ItemStack simulatedStack = targetHandler.extractItem(targetSlot, amount, true);
								amount = Math.min(amount, simulatedStack.getCount());
								if (amount > 0) 
								{
									ItemStack extractedStack = targetHandler.extractItem(targetSlot, amount, false);
									destHandler.insertItem(destSlot, extractedStack, false);
									return true;
								}
							}
						}
						for (int destSlot = 0; destSlot < destHandler.getSlots(); destSlot++) 
						{
							if (destHandler.isItemValid(destSlot, stack) && destHandler.getStackInSlot(destSlot).isEmpty()) 
							{
								ItemStack remainStack = destHandler.insertItem(destSlot, stack, true);
								int amount = stack.getCount() - remainStack.getCount();
								ItemStack simulatedStack = targetHandler.extractItem(targetSlot, amount, true);
								amount = Math.min(amount, simulatedStack.getCount());
								if (amount > 0) 
								{
									ItemStack extractedStack = targetHandler.extractItem(targetSlot, amount, false);
									destHandler.insertItem(destSlot, extractedStack, false);
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public static ItemStack addInventoryFromStack(ItemStack stack, IInventory destination, @Nullable EnumFacing facing)
	{
		if (stack.isEmpty())
			return ItemStack.EMPTY;
		IItemHandler destHandler = ((ICapabilityProvider) destination).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
		if (destHandler == null)
			return stack;
		for (int destSlot = 0; destSlot < destHandler.getSlots(); destSlot++) 
		{
			if (destHandler.isItemValid(destSlot, stack) && !destHandler.getStackInSlot(destSlot).isEmpty()) 
			{
				ItemStack remainStack = destHandler.insertItem(destSlot, stack, true);
				int amount = stack.getCount() - remainStack.getCount();
				if (amount > 0) 
				{
					return destHandler.insertItem(destSlot, stack, false);
				}
			}
		}
		for (int destSlot = 0; destSlot < destHandler.getSlots(); destSlot++) 
		{
			if (destHandler.isItemValid(destSlot, stack) && destHandler.getStackInSlot(destSlot).isEmpty()) 
			{
				ItemStack remainStack = destHandler.insertItem(destSlot, stack, true);
				int amount = stack.getCount() - remainStack.getCount();
				if (amount > 0) 
				{
					return destHandler.insertItem(destSlot, stack, false);
				}
			}
		}
		return stack;
	}
	
	/*
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
			if (distination instanceof ICapabilityProvider) 
			{
				ICapabilityProvider cap = (ICapabilityProvider) distination;
				IItemHandler handler = cap.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facingDist);
				if (handler != null) 
				{
					for (int i = 0; i < handler.getSlots(); i++) 
					{
						if (!targetstack.isEmpty() && StackUtils.compareWith(handler.getStackInSlot(i), targetstack)) 
						{
							targetstack = handler.insertItem(i, targetstack, false);
						}
					}
					for (int i = 0; i < handler.getSlots(); i++) 
					{
						targetstack = handler.insertItem(i, targetstack, false);
					}
					if (targetstack.getCount() != targetcount) 
					{
						target.decrStackSize(targetindex, targetcount - targetstack.getCount());
					}
					return targetstack.getCount() != targetcount;
				}
			}
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
		if (distination instanceof ICapabilityProvider) 
		{
			ICapabilityProvider cap = (ICapabilityProvider) distination;
			if (cap.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) 
			{
				IItemHandler handler = cap.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
				if (handler != null) 
				{
					for (int i = 0; i < handler.getSlots(); i++) 
					{
						if (!stack.isEmpty() && StackUtils.compareWith(stack, handler.getStackInSlot(i))) 
						{
							stack = handler.insertItem(i, stack, false);
						}
					}
					for (int i = 0; i < handler.getSlots(); i++) 
					{
						stack = handler.insertItem(i, stack, false);
					}
					return stack;
				}
			}
		}
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
	*/

	public static IItemHandler getInventoryStackList(IInventory target, @Nullable EnumFacing facing)
	{
		if (target instanceof ICapabilityProvider) 
		{
			return ((ICapabilityProvider) target).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
		}
		return null;
	}

	public static FluidStack addFluidToInventory(FluidStack stack, IInventory inventory, @Nullable EnumFacing facing)
	{
		IItemHandler handler = null;
		if (inventory instanceof ICapabilityProvider) 
		{
			handler = ((ICapabilityProvider) inventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
		}
		
		if (handler == null) 
		{
			return stack;
		}
		
		for (int slot = 0; slot < handler.getSlots(); slot++)
		{
			ItemStack itemStack = handler.getStackInSlot(slot);
			if (itemStack.getItem() == Items.BUCKET)
			{
				if (itemStack.getCount() == 1)
				{
					itemStack.shrink(1);
					if (stack.amount >= 1000)
					{
						FluidStack newFluidStack = stack.copy();
						newFluidStack.amount = 1000;
						stack.amount -= 1000;
						ItemStack newStack = FluidUtil.getFilledBucket(newFluidStack);
						addInventoryFromStack(newStack, inventory, null);
					}
				}
				else
				{
					if (stack.amount >= 1000)
					{
						FluidStack newFluidStack = stack.copy();
						newFluidStack.amount = 1000;
						ItemStack newStack = FluidUtil.getFilledBucket(newFluidStack);
						if (addInventoryFromStack(newStack, inventory, null).isEmpty())
						{
							itemStack.shrink(1);
							stack.amount -= 1000;
						}
					}
				}
			}
		}
		return stack;
	}
	
	public static boolean outputFluidFromInventory(IInventory target, @Nullable EnumFacing targetFacing, @Nullable IFluidHandler destFluidHandler, BlockPos destPos, 
			EnumFacing destFacing, IFunction<BlockPos> function, Predicate<Fluid> filter, World world, EntityPlayer player) 
	{
		IItemHandler handler = null;
		if (target instanceof ICapabilityProvider) 
		{
			handler = ((ICapabilityProvider) target).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, targetFacing);
		}
		
		if (handler == null) 
		{
			return false;
		}
		
		if (destFluidHandler != null)
		{
			for (int slot = 0; slot < handler.getSlots(); slot++)
			{
				ItemStack stack = handler.getStackInSlot(slot);
				if (SpellUtils.isFluidContainer(stack))
				{
					FluidStack fluid = FluidUtil.getFluidContained(stack);
					if (fluid != null && fluid.getFluid() != null)
					{
						if (!filter.apply(fluid.getFluid()))
						{
							continue;
						}
						int willFill = destFluidHandler.fill(fluid, false);
						IFluidHandler stackHandler = FluidUtil.getFluidHandler(stack);
						FluidStack fluidStack = stackHandler.drain(fluid, false);
						if (fluidStack == null) 
						{
							continue;
						}
						int amount = Math.min(fluidStack.amount, willFill);
						if (amount > 0)
						{
							FluidStack drained = stackHandler.drain(new FluidStack(fluidStack.getFluid(), amount), true);
							destFluidHandler.fill(drained, true);
							return true;
						}
					}
				}
			}
		}
		else
		{
			for (int slot = 0; slot < handler.getSlots(); slot++)
			{
				ItemStack stack = handler.getStackInSlot(slot);
				if (SpellUtils.isFluidContainer(stack))
				{
					FluidStack fluid = FluidUtil.getFluidContained(stack);
					if (fluid != null && fluid.getFluid() != null)
					{
						if (!filter.apply(fluid.getFluid()))
						{
							continue;
						}
						IFluidHandler stackHandler = FluidUtil.getFluidHandler(stack);
						int amount = stackHandler.drain(fluid, false).amount;
						if (amount >= 1000) 
						{
							BlockPos nextPos = destPos.add(destFacing.getDirectionVec());
							FluidStack willDrain = new FluidStack(fluid.getFluid(), 1000);
							if (FluidUtil.tryPlaceFluid(player, world, nextPos, stack, willDrain) != FluidActionResult.FAILURE) 
							{
								stackHandler.drain(willDrain, true);
								function.function(nextPos);
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	/*
	public static Map<Integer, FluidStack> getFluidFromInventory(IInventory inventory, boolean exceptHoldItem, @Nullable EnumFacing facing)
	{
		Map<Integer, FluidStack> map = new HashMap<Integer, FluidStack>();
		IItemHandler handler = null;
		if (inventory instanceof WrapperPlayerInventory) 
		{
			handler = ((WrapperPlayerInventory) inventory).getMainCap();
		} 
		else if (inventory instanceof ICapabilityProvider) 
		{
			handler = ((ICapabilityProvider) inventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
		}
		
		int size = inventory.getSizeInventory();
		for (int i = 0; i < size; i++)
		{
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
			if (SpellUtils.isFluidContainer(stack))
			{
				FluidStack fluid = FluidUtil.getFluidContained(stack);
				if (fluid != null && fluid.getFluid() != null)
				{
					map.put(i, fluid);
				}
			}
		}

		return map;
	}

	public static Map<Integer, FluidStack> getFluidFromInventory2(IInventory inventory, boolean exceptHoldItem, @Nullable EnumFacing facing)
	{
		Map<Integer, FluidStack> map = new HashMap<Integer, FluidStack>();
		int size = inventory.getSizeInventory();
		for (int i = 0; i < size; i++)
		{
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
			if (SpellUtils.isFluidContainer(stack))
			{
				FluidStack fluid = FluidUtil.getFluidContained(stack);
				if (fluid != null && fluid.getFluid() != null)
				{
					map.put(i, fluid);
				}
			}
		}

		return map;
	}
	*/

	public static boolean addFluidToInventoryFromTank(IFluidHandler handler, IInventory inventory, @Nullable EnumFacing facing, FluidStack target)
	{
		FluidStack stack = handler.drain(target, false).copy();
		if (stack != null && stack.amount == target.amount)
		{
			stack = addFluidToInventory(stack, inventory, facing);
			handler.drain(target, true);
			return true;
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
		return new WrapperPlayerInventory(player.inventory);
	}
}
