package neo_ores.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class GuiSpellCreatingTable extends GuiContainer implements IContainerListener
{

	public GuiSpellCreatingTable(Container inventorySlotsIn) 
	{
		super(inventorySlotsIn);
	}

	@Override
	public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) 
	{
		
	}

	@Override
	public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) 
	{
		
	}

	@Override
	public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) 
	{
		
	}

	@Override
	public void sendAllWindowProperties(Container containerIn, IInventory inventory) 
	{
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		
	}	
}
