package neo_ores.inventory;

import neo_ores.items.ItemNeoArmor;
import neo_ores.items.ItemNeoAxe;
import neo_ores.items.ItemNeoHoe;
import neo_ores.items.ItemNeoPickaxe;
import neo_ores.items.ItemNeoSpade;
import neo_ores.items.ItemNeoSword;
import neo_ores.items.ItemNeoTool;
import neo_ores.main.NeoOres;
import neo_ores.tileentity.TileEntityManaFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerManaFurnace extends Container 
{
	private final TileEntityManaFurnace tileManaFurnace;
    private int cookTime;
    private int totalCookTime;
    private int furnaceBurnTime;
    private int currentItemBurnTime;

    public ContainerManaFurnace(InventoryPlayer playerInventory, TileEntityManaFurnace manaFurnace)
    {
        this.tileManaFurnace = manaFurnace;

        this.addSlotToContainer(new SlotAirEssenceCore(manaFurnace, 0, 38, 35));
        this.addSlotToContainer(new SlotFireEssenceCore(manaFurnace, 1, 56, 17));
        this.addSlotToContainer(new SlotWaterEssenceCore(manaFurnace, 2, 74, 35));
        this.addSlotToContainer(new SlotEarthEssenceCore(manaFurnace, 3, 56, 53));
        this.addSlotToContainer(new Slot(manaFurnace, 4, 56, 35) 
        {
        	public boolean isItemValid(ItemStack stack)
        	{
        		return stack.getItem() instanceof ItemNeoTool || stack.getItem() instanceof ItemNeoArmor;
        	}
        });
        this.addSlotToContainer(new SlotManaFurnaceFuel(manaFurnace, 5, 92, 53));
        this.addSlotToContainer(new SlotManaFurnaceOutput(playerInventory.player, manaFurnace, 6, 134, 35));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileManaFurnace);
    }
    
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (this.cookTime != this.tileManaFurnace.getField(2))
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tileManaFurnace.getField(2));
            }

            if (this.furnaceBurnTime != this.tileManaFurnace.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tileManaFurnace.getField(0));
            }

            if (this.currentItemBurnTime != this.tileManaFurnace.getField(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tileManaFurnace.getField(1));
            }

            if (this.totalCookTime != this.tileManaFurnace.getField(3))
            {
                icontainerlistener.sendWindowProperty(this, 3, this.tileManaFurnace.getField(3));
            }
        }

        this.cookTime = this.tileManaFurnace.getField(2);
        this.furnaceBurnTime = this.tileManaFurnace.getField(0);
        this.currentItemBurnTime = this.tileManaFurnace.getField(1);
        this.totalCookTime = this.tileManaFurnace.getField(3);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.tileManaFurnace.setField(id, data);
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileManaFurnace.isUsableByPlayer(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 6)
            {
                if (!this.mergeItemStack(itemstack1, 7, 43, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index != 1 && index != 0 && index != 2 && index != 3 && index != 4 && index != 5)
            {
                if (itemstack1.getItem() instanceof ItemNeoAxe || itemstack1.getItem() instanceof ItemNeoHoe || itemstack1.getItem() instanceof ItemNeoPickaxe || itemstack1.getItem() instanceof ItemNeoSpade || itemstack1.getItem() instanceof ItemNeoSword || itemstack1.getItem() instanceof ItemNeoArmor)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 5, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (TileEntityManaFurnace.isItemFuel(itemstack1))
                {
                    if (!this.canMergeSlot(itemstack1, this.inventorySlots.get(5)))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(itemstack1.getItem() == NeoOres.air_essence_core)
                {
                	if(!this.canMergeSlot(itemstack1, this.inventorySlots.get(0)))
                	{
                		return ItemStack.EMPTY;
                	}
                }
                else if(itemstack1.getItem() == NeoOres.fire_essence_core)
                {
                	if(!this.canMergeSlot(itemstack1, this.inventorySlots.get(1)))
                	{
                		return ItemStack.EMPTY;
                	}
                }
                else if(itemstack1.getItem() == NeoOres.water_essence_core)
                {
                	if(!this.canMergeSlot(itemstack1, this.inventorySlots.get(2)))
                	{
                		return ItemStack.EMPTY;
                	}
                }
                else if(itemstack1.getItem() == NeoOres.earth_essence_core)
                {
                	if(!this.canMergeSlot(itemstack1, this.inventorySlots.get(3)))
                	{
                		return ItemStack.EMPTY;
                	}
                }
                else if (index >= 7 && index < 34)
                {
                    if (!this.mergeItemStack(itemstack1, 34, 43, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 34 && index < 43 && !this.mergeItemStack(itemstack1, 7, 34, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 7, 43, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}
