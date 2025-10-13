package neo_ores.inventory;

import neo_ores.pi.InventoryPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ContainerPedestalInterface extends ContainerPIBase
{
	public final InventoryPI basicInventory = new InventoryPI("tmp", true, 60);
	public NonNullList<ItemStack> itemList = NonNullList.<ItemStack>create();
	
	@SideOnly(Side.CLIENT)
	static class PISlot extends Slot
	{
		public PISlot(IInventory inventory, int index, int x, int y)
		{
			super(inventory, index, x, y);
		}

		public boolean canTakeStack(EntityPlayer playerIn)
		{
			return !this.getHasStack();
		}
	}

	public ContainerPedestalInterface(InventoryPlayer playerInventory, boolean localWorld, EntityPlayer playerIn)
	{
		super(playerInventory, localWorld, playerIn);

		for (int i = 0; i < 5; ++i)
		{
			for (int j = 0; j < 12; ++j)
			{
				this.addSlotToContainer(new PISlot(basicInventory, i * 12 + j, 13 + j * 18, 24 + i * 18));
			}
		}
	}
	
	public void scrollTo(float pos)
    {
        int i = (this.itemList.size() + 12 - 1) / 12 - 5;
        int j = (int)((double)(pos * (float)i) + 0.5D);

        if (j < 0)
        {
            j = 0;
        }
        
        for (int k = 0; k < 5; ++k)
        {
            for (int l = 0; l < 12; ++l)
            {
                int i1 = l + (k + j) * 12;

                if (i1 >= 0 && i1 < this.itemList.size())
                {
                    basicInventory.setInventorySlotContents(l + k * 12, this.itemList.get(i1));
                }
                else
                {
                    basicInventory.setInventorySlotContents(l + k * 12, ItemStack.EMPTY);
                }
            }
        }
    }

    public boolean canScroll()
    {
        return this.itemList.size() > 45;
    }
    
    public boolean canMergeSlot(ItemStack stack, Slot slotIn)
    {
        return slotIn.inventory instanceof InventoryPlayer;
    }

    public boolean canDragIntoSlot(Slot slotIn)
    {
        return slotIn.inventory instanceof InventoryPlayer;
    }
}
