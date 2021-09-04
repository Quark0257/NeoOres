package neo_ores.inventory;

import neo_ores.mana.PlayerManaCalc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotManaFurnaceOutput extends Slot
{
	PlayerManaCalc manaCalc = new PlayerManaCalc();
	
    public SlotManaFurnaceOutput(EntityPlayer player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition)
    {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

    public ItemStack decrStackSize(int amount)
    {
        return super.decrStackSize(amount);
    }

    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
    {
    	manaCalc.addMXP(thePlayer, 50);
        super.onTake(thePlayer, stack);
        return stack;
    }
}
