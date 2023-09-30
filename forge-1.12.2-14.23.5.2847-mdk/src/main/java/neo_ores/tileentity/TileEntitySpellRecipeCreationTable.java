package neo_ores.tileentity;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.spell.SpellItem;
import neo_ores.inventory.ContainerSpellRecipeCreationTable;
import neo_ores.main.NeoOres;
import neo_ores.packet.PacketSRCTToClient;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;

public class TileEntitySpellRecipeCreationTable extends TileEntityLockable implements ITickable, IInventory
{   
    
    private List<SpellItem> selectedSpells = new ArrayList<SpellItem>();
    private NonNullList<ItemStack> srctItemStacks = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);

    public String srctSearch = "";

    public int getSizeInventory()
    {
        return this.srctItemStacks.size();
    }

    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.srctItemStacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    public ItemStack getStackInSlot(int index)
    {
        return this.srctItemStacks.get(index);
    }

    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.srctItemStacks, index, count);
    }

    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.srctItemStacks, index);
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemstack = this.srctItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.srctItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            this.markDirty();
        }
    }

    public String getName()
    {
        return "container.spell_recipe_creation_table";
    }

    public boolean hasCustomName()
    {
        return false;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.selectedSpells = SpellUtils.getListFromNBT(compound.getCompoundTag("selectedSpells"));
        this.srctItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.srctItemStacks);

        this.srctSearch = compound.getString("search");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setTag("selectedSpells", SpellUtils.getNBTFromList(this.selectedSpells));
        ItemStackHelper.saveAllItems(compound, this.srctItemStacks);

        compound.setString("CustomName", this.srctSearch);

        return compound;
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void update()
    {
        boolean flag1 = false;

        if (flag1)
        {
            this.markDirty();
        }
        
        NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("x", this.getPos().getX());
		nbt.setInteger("y", this.getPos().getY());
		nbt.setInteger("z", this.getPos().getZ());
		nbt.setTag("recipeSpells", SpellUtils.getNBTFromList(selectedSpells));
		
		PacketSRCTToClient psrcts = new PacketSRCTToClient(nbt);
		NeoOres.PACKET.sendToAll(psrcts);
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public void openInventory(EntityPlayer player) 
    {
    
    }

    public void closeInventory(EntityPlayer player) {}

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        if(index == 0)
        {
        	
        }
        return false;
    }

    public int[] getSlotsForFace(EnumFacing side)
    {
    	return new int[] {0};
    }

    public String getGuiID()
    {
        return "neo_ores:spell_recipe_creation_table";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerSpellRecipeCreationTable(playerInventory, this);
    }

    public int getField(int id)
    {
    	return 0;
    }

    public void setField(int id, int value)
    {
    }

    public int getFieldCount()
    {
        return 0;
    }

    public void clear()
    {
        this.srctItemStacks.clear();
    }
    
    public List<SpellItem> getSpellItems()
    {
    	return this.selectedSpells;
    }
    
    public void setSpellItems(List<SpellItem> list)
    {
    	this.selectedSpells = list;
    }
    
    public void addSpellItem(SpellItem item)
    {
    	if(!this.selectedSpells.contains(item)) this.selectedSpells.add(item);
    }
    
    public void removeSpellItem(SpellItem item)
    {
    	this.selectedSpells.remove(item);
    }
}
