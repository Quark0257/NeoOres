package neo_ores.tileentity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import neo_ores.api.ItemStackWithSize;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresItems;
import neo_ores.packet.PacketItemsToClient;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityEnhancedPedestal extends AbstractTileEntityPedestal implements ISidedInventory
{
	public int slotsize;
	private NonNullList<ItemStackWithSize> item_list = NonNullList.withSize(this.slotsize, ItemStackWithSize.EMPTY);
	private int selectedSlot;
	private boolean canSuck;
	public int tickCount;

	public TileEntityEnhancedPedestal()
	{
		
	}
	
	public void setSize(int slotsize)
	{
		this.slotsize = 2 * (int)Math.pow(2.0D,(double)((0 < slotsize && slotsize <= 8) ? slotsize : 1));
		this.item_list = NonNullList.withSize(this.slotsize, ItemStackWithSize.EMPTY);
	}
	
	public void setSuckable(boolean canSuck)
	{
		this.canSuck = canSuck;
	}
	
	public void setSlot(int slot)
	{
		this.selectedSlot = slot;
	}
	
	public int getSlot()
	{
		return this.selectedSlot;
	}
	
	public void addSlot(int slot)
	{
		int apply = this.getSlot();
		if(slot >= 0)
		{
			for(int n = 0;n < slot;n++)
			{
				for(int i = 1;i < this.getSizeInventory();i++)
				{
					if(!this.getItems().get((apply + i >= this.getSizeInventory()) ? apply + i - this.getSizeInventory() : apply + i).isEmpty())
					{
						apply = (apply + i >= this.getSizeInventory()) ? apply + i - this.getSizeInventory() : apply + i;
						break;
					}
				}
			}
		}
		else
		{
			for(int n = 0;n < (-slot);n++)
			{
				for(int i = 1;i < this.getSizeInventory();i++)
				{
					if(!this.getItems().get((apply - i < 0) ? this.getSizeInventory() + apply - i : apply - i).isEmpty())
					{
						apply = (apply - i < 0) ? this.getSizeInventory() + apply - i : apply - i;
						break;
					}
				}
			}
		}
		
		this.setSlot(apply);
	}
	
	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        
        this.slotsize = compound.getInteger("slotSize");
        this.selectedSlot = compound.getInteger("selectedSlot");
        this.canSuck = compound.getBoolean("canSuck");
        
        if(compound.hasKey("display", 10))
        {
        	this.display = new ItemStack(compound.getCompoundTag("display"));
        }
        
        this.item_list = NonNullList.withSize(this.getSizeInventory(), ItemStackWithSize.EMPTY);
        ItemStackWithSize.getFromNBT(this.item_list,compound);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        
        ItemStackWithSize.setToNBT(this.item_list, compound);
        
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound = display.writeToNBT(nbttagcompound);
        compound.setTag("display", nbttagcompound);
        compound.setInteger("slotSize",this.slotsize);
        compound.setInteger("selectedSlot", this.selectedSlot);
        compound.setBoolean("canSuck",this.canSuck);
        
        return compound;
    }

    @Override
    public boolean isEmpty() 
    {
    	boolean flag = true;
    	for(int i = 0;i < this.item_list.size();i++)
    	{
    		flag = this.item_list.get(i).isEmpty();
    		if(!flag) break;
    	}
        return flag;
    }
    
    public ItemStack getStackInSlot(int index)
    {
    	//System.out.println(index);
    	return this.item_list.get(index).getMediate();
    }
    
    public ItemStack decrStackSize(int index, int count) 
    {
    	ItemStack stack1 = !this.item_list.get(index).isEmpty() && count > 0 ? this.item_list.get(index).getMediate().splitStack(count) : ItemStack.EMPTY;
    	
    	if (!stack1.isEmpty())
        {
            this.markDirty();
        }
    	
    	return stack1;
    }

    public ItemStack removeStackFromSlot(int index) 
    {
    	ItemStack stack = this.item_list.get(index).getMediate().copy();
    	this.item_list.set(index, ItemStackWithSize.EMPTY);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) 
    {
    	if(this.item_list.get(index).isEmpty()) this.item_list.set(index,new ItemStackWithSize(stack,(this.getInventoryStackLimit() < stack.getCount()) ? this.getInventoryStackLimit() : stack.getCount()));
    	else 
    	{
    		ItemStack stack1 = stack.copy();
    		if(this.getInventoryStackLimit() < this.item_list.get(index).getSize() + stack.getCount())
    		{
    			stack1.setCount(this.getInventoryStackLimit() - this.item_list.get(index).getSize() - stack.getCount());
    			stack.setCount(stack.getCount() - this.getInventoryStackLimit() + this.item_list.get(index).getSize());
    		}
    		this.item_list.get(index).addStack(stack1);
    	}
    	
    	this.markDirty();
    }
    
    public ItemStack addItemStackToInventory(ItemStack stack) 
    {
    	ItemStack stack1 = stack.copy();
    	ItemStack stack2 = stack.copy();
    	if(this.isFull()) return stack;
    	int n = this.getSizeInventory();
    	for(int i = 0;i < n;i++)
    	{
    		if(this.item_list.get(i).compareWith(stack))
    		{
    			if(this.item_list.get(i).getSize() + stack2.getCount() > this.getInventoryStackLimit())
    			{
    				stack1.setCount(this.getInventoryStackLimit() - this.item_list.get(i).getSize());
    				stack2.setCount(stack2.getCount() - stack1.getCount());
    				this.setInventorySlotContents(i, stack1);
    			}
    			else
    			{
    				this.setInventorySlotContents(i, stack2);
    				return ItemStack.EMPTY;
    			}
    		}
    	}
    	
    	ItemStack stack3 = stack2;
    	
    	for(int i = 0;i < n;i++)
    	{
    		if(this.item_list.get(i).isEmpty())
    		{
				if(stack3.getCount() > this.getInventoryStackLimit())
				{
					stack2.setCount(this.getInventoryStackLimit());
    				stack3.setCount(stack3.getCount() - stack2.getCount());
    				this.setInventorySlotContents(i, stack2);
				}
				else
    			{
    				this.setInventorySlotContents(i, stack3);
    				return ItemStack.EMPTY;
    			}
    		}
    	}
    	return stack3;
    }
    
    public boolean isFull()
    {
    	if(item_list.isEmpty()) return true;
    	for(ItemStackWithSize stack : item_list)
    	{
    		if(stack.isEmpty()) return false;
    		else if(stack.getSize() < this.getInventoryStackLimit()) return false;
    	}
    	return true;
    }

    @Override
    public int getInventoryStackLimit() 
    {
        return (int)Math.pow(slotsize / 2,3) * 64;
    }
    
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) 
    {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return this.item_list.get(index).compareWith(stack) || this.item_list.get(index).isEmpty();
    }

    @Override
    public int getField(int id) 
    {
        return 0;
    }

    @Override
    public void setField(int id, int value) 
    {

    }

    @Override
    public int getFieldCount() 
    {
        return 0;
    }

    @Override
    public void clear() 
    {
    	for(int i = 0;i < this.item_list.size();i++)
    	{
    		this.item_list.set(i, ItemStackWithSize.EMPTY);
    	}
    }

    @Override
    public String getName() 
    {
        return "container.enhance_pedestal";
    }

    @Override
    public boolean hasCustomName() 
    {
        return false;
    }

	@Override
	public void update() 
	{
		//System.out.println(this.display);
		if(!this.getWorld().isRemote)
		{
			for(int i = 0;i < this.getSizeInventory();i++)
			{
				if(!this.item_list.get(i).isEmpty())
				{
					for(int j = i + 1;j < this.getSizeInventory();j++)
					{
						if(!this.item_list.get(i).isEmpty())
						{
							if(this.item_list.get(i).compareWith(this.item_list.get(j).getStack()))
							{
								if(this.getInventoryStackLimit() < this.item_list.get(i).getSize() + this.item_list.get(j).getSize())
								{
									this.item_list.get(j).setSize(this.getInventoryStackLimit() - (this.item_list.get(i).getSize() + this.item_list.get(j).getSize()));
									this.item_list.get(i).setSize(this.getInventoryStackLimit());
								}
								else
								{
									this.item_list.get(i).addSize(this.item_list.get(j).getSize());
									this.item_list.set(j,ItemStackWithSize.EMPTY);
								}
							}
						}
					}
				}
			}
		}	
		
		if(this.canSuck && !this.getWorld().isRemote)
		{
			List<EntityItem> list = TileEntityHopper.getCaptureItems(this.getWorld(), (double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 1.0625D, (double)this.pos.getZ() + 0.5D);
			for(EntityItem ei : list)
			{
				if(ei.getItem().getItem() != NeoOresItems.mana_wrench)
				{	
					ei.setItem(this.addItemStackToInventory(ei.getItem()));
					if(ei.getItem().isEmpty()) ei.setDead();
				}
			}
		}
		
		if(!this.getWorld().isRemote) 
		{
			NBTTagCompound packet = new NBTTagCompound();
			packet.setInteger("x", this.pos.getX());
			packet.setInteger("y", this.pos.getY());
			packet.setInteger("z", this.pos.getZ());
			packet.setDouble("offset", offset);
			packet.setInteger("slotsize", this.slotsize);
			ItemStack stack = (this.getDisplay().isEmpty()) ? this.getStackInSlot(this.getSlot()).copy() : this.getDisplay();
			stack.setCount(1);
			NBTTagCompound nbttagcompound = new NBTTagCompound();
	        nbttagcompound = stack.writeToNBT(nbttagcompound);
			packet.setTag("display", nbttagcompound);
			PacketItemsToClient pic = new PacketItemsToClient(packet);
			NeoOres.PACKET.sendToAll(pic);
		}
		
		super.update();
		
		if(!this.getWorld().isRemote && this.getItems().get(this.getSlot()).isEmpty())
		{
			for(int i = 0;i < this.getSizeInventory();i++)
			{
				if(!this.getItems().get(i).isEmpty())
				{
					this.setSlot(i);
					break;
				}
			}
		}
	}

	@Override
	public int getSizeInventory() 
	{
		return this.slotsize;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) 
	{
		if(side != EnumFacing.UP)
		{
			List<Integer> list = new ArrayList<Integer>();
			int size = this.getSizeInventory();
			for(int i = 0;i < size;i++)
			{
				list.add(i);
			}
			return ArrayUtils.toPrimitive(list.toArray(new Integer[] {}));
		}
		return new int[] {};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) 
	{
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) 
	{
		return true;
	}
	
	public NonNullList<ItemStackWithSize> getItems()
	{
		return this.item_list;
	}
	
	public static void dropInventoryItems(World worldIn, BlockPos pos, TileEntityEnhancedPedestal tileentity) 
	{
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
		for (ItemStackWithSize stackWS : tileentity.getItems())
        {
			if(!stackWS.isEmpty())
			{
				for(ItemStack stack : stackWS.asList(stackWS.getStack().getMaxStackSize()))
	            {
	            	InventoryHelper.spawnItemStack(worldIn, x, y, z, stack);
	            }
			}
        }
	}

	@Override
	public boolean canExtract(int index, ItemStack stack, EnumFacing direction) {
		return direction != EnumFacing.UP;
	}

	@Override
	public boolean canInsert(int index, ItemStack stack, EnumFacing direction) {
		return direction != EnumFacing.UP && this.isItemValidForSlot(index, stack);
	}
}
