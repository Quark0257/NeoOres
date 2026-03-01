package neo_ores.inventory;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerArmorInvWrapper;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.items.wrapper.PlayerOffhandInvWrapper;

public class WrapperPlayerInventory implements IInventory, ICapabilityProvider
{
	private final InventoryPlayer playerInv;
	
	public WrapperPlayerInventory(InventoryPlayer playerInv) 
	{
		this.playerInv = playerInv;
	}

	@Override
	public String getName()
	{
		return this.playerInv.getName();
	}

	@Override
	public boolean hasCustomName()
	{
		return this.playerInv.hasCustomName();
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return this.playerInv.getDisplayName();
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		IItemHandler handler = new PlayerInvWrapper(this.playerInv);
		if (!this.hasCapability(capability, facing))
			return null;
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) handler;
		return null;
	}
	
	public IItemHandler getMainCap() 
	{
		return new PlayerMainInvWrapper(this.playerInv);
	}
	
	public IItemHandler getArmorCap() 
	{
		return new PlayerArmorInvWrapper(this.playerInv);
	}
	
	public IItemHandler getOffhandCap() 
	{
		return new PlayerOffhandInvWrapper(this.playerInv);
	}

	@Override
	public int getSizeInventory()
	{
		return this.playerInv.getSizeInventory();
	}

	@Override
	public boolean isEmpty()
	{
		return this.playerInv.isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return this.playerInv.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return this.playerInv.decrStackSize(index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return this.playerInv.removeStackFromSlot(index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.playerInv.setInventorySlotContents(index, stack);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return this.playerInv.getInventoryStackLimit();
	}

	@Override
	public void markDirty()
	{
		this.playerInv.markDirty();
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return this.playerInv.isUsableByPlayer(player);
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
		this.playerInv.openInventory(player);
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		this.playerInv.closeInventory(player);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return this.playerInv.isItemValidForSlot(index, stack);
	}

	@Override
	public int getField(int id)
	{
		return this.playerInv.getField(id);
	}

	@Override
	public void setField(int id, int value)
	{
		this.playerInv.setField(id, value);
	}

	@Override
	public int getFieldCount()
	{
		return this.playerInv.getFieldCount();
	}

	@Override
	public void clear()
	{
		this.playerInv.clear();
	}

}
