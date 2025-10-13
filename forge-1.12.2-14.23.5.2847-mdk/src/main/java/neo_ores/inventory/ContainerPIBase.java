package neo_ores.inventory;

import javax.annotation.Nullable;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.container.SlotBauble;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerPIBase extends Container
{
	private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
	public boolean isLocalWorld;
	protected final EntityPlayer player;
	private final IBaublesItemHandler baubles;

	public ContainerPIBase(InventoryPlayer playerInventory, boolean localWorld, EntityPlayer playerIn)
	{
		this.isLocalWorld = localWorld;
		this.player = playerIn;
		this.baubles = BaublesApi.getBaublesHandler(playerIn);

		for (int l = 0; l < 3; ++l)
		{
			for (int j1 = 0; j1 < 9; ++j1)
			{
				this.addSlotToContainer(new Slot(playerInventory, j1 + (l + 1) * 9, 49 + j1 * 18, 126 + l * 18));
			}
		}

		for (int i1 = 0; i1 < 9; ++i1)
		{
			this.addSlotToContainer(new Slot(playerInventory, i1, 49 + i1 * 18, 184));
		}
		
		for (int k = 0; k < 4; ++k)
        {
            final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[k];
            this.addSlotToContainer(new Slot(playerInventory, 36 + (3 - k), 211, 126 + k * 18)
            {
                public int getSlotStackLimit()
                {
                    return 1;
                }

                public boolean isItemValid(ItemStack stack)
                {
                    return stack.getItem().isValidArmor(stack, entityequipmentslot, player);
                }

                public boolean canTakeStack(EntityPlayer playerIn)
                {
                    ItemStack itemstack = this.getStack();
                    return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(playerIn);
                }
                @Nullable
                @SideOnly(Side.CLIENT)
                public String getSlotTexture()
                {
                    return ItemArmor.EMPTY_SLOT_NAMES[entityequipmentslot.getIndex()];
                }
            });
        }
		
		this.addSlotToContainer(new Slot(playerInventory, 40, 31, 180)
        {
            @Nullable
            @SideOnly(Side.CLIENT)
            public String getSlotTexture()
            {
                return "minecraft:items/empty_armor_slot_shield";
            }
        });
		
		for (int k = 0; k < 7; k++) 
		{
			this.addSlotToContainer(new SlotBauble(playerIn, this.baubles, k, 13 + (k / 4) * 18, 126 + (k % 4) * 18));
		}
	}

	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index >= 0 && index < 27)
			{
				if (!this.mergeItemStack(itemstack1, 27, 36, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (index >= 27 && index < 36)
			{
				if (!this.mergeItemStack(itemstack1, 0, 27, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, 36, false))
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
		}

		return itemstack;
	}

	public boolean canMergeSlot(ItemStack stack, Slot slotIn)
	{
		return super.canMergeSlot(stack, slotIn);
	}
}
