package neo_ores.tileentity;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class DetectorWrapper extends SidedInvWrapper
{
	private final TileEntityPedestalNetworkDetector detector;
	
	public DetectorWrapper(TileEntityPedestalNetworkDetector inv, EnumFacing side)
	{
		super(inv, side);
		this.detector = inv;
	}

	@Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (amount == 0)
            return ItemStack.EMPTY;

        int slot1 = getSlot(inv, slot, side);

        if (slot1 == -1)
            return ItemStack.EMPTY;

        ItemStack stackInSlot = inv.getStackInSlot(slot1);

        if (stackInSlot.isEmpty())
            return ItemStack.EMPTY;
        if (!simulate) 
        {
        	this.detector.setOn();
        }
        return super.extractItem(slot1, amount, simulate);
    }
}
