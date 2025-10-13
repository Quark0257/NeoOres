package neo_ores.tileentity;

import javax.annotation.Nonnull;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class EnhancedPedestalWrapper extends SidedInvWrapper
{
	public EnhancedPedestalWrapper(ISidedInventory inv, EnumFacing side)
	{
		super(inv, side);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EnhancedPedestalWrapper that = (EnhancedPedestalWrapper) o;

		return inv.equals(that.inv) && side == that.side;
	}

	@Override
	@Nonnull
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
	{
		if (stack.isEmpty())
			return ItemStack.EMPTY;

		int slot1 = getSlot(inv, slot, side);

		if (slot1 == -1)
			return stack;

		ItemStack stackInSlot = inv.getStackInSlot(slot1);

		int m;
		if (!stackInSlot.isEmpty())
		{
			if (stackInSlot.getCount() >= getSlotLimit(slot))
				return stack;

			if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot))
				return stack;

			if (!inv.canInsertItem(slot1, stack, side) || !inv.isItemValidForSlot(slot1, stack))
				return stack;

			m = getSlotLimit(slot) - stackInSlot.getCount();

			if (stack.getCount() <= m)
			{
				if (!simulate)
				{
					ItemStack copy = stack.copy();
					copy.grow(stackInSlot.getCount());
					setInventorySlotContents(slot1, copy);
				}

				return ItemStack.EMPTY;
			}
			else
			{
				// copy the stack to not modify the original one
				stack = stack.copy();
				if (!simulate)
				{
					ItemStack copy = stack.splitStack(m);
					copy.grow(stackInSlot.getCount());
					setInventorySlotContents(slot1, copy);
					return stack;
				}
				else
				{
					stack.shrink(m);
					return stack;
				}
			}
		}
		else
		{
			if (!inv.canInsertItem(slot1, stack, side) || !inv.isItemValidForSlot(slot1, stack))
				return stack;

			m = getSlotLimit(slot);
			if (m < stack.getCount())
			{
				// copy the stack to not modify the original one
				stack = stack.copy();
				if (!simulate)
				{
					setInventorySlotContents(slot1, stack.splitStack(m));
					return stack;
				}
				else
				{
					stack.shrink(m);
					return stack;
				}
			}
			else
			{
				if (!simulate)
					setInventorySlotContents(slot1, stack);
				return ItemStack.EMPTY;
			}
		}
	}

	private void setInventorySlotContents(int slot, ItemStack stack)
	{
		inv.markDirty(); // Notify vanilla of updates, We change the handler to be responsible for this
							// instead of the caller. So mimic vanilla behavior
		inv.setInventorySlotContents(slot, stack);
	}
}
