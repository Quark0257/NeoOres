package neo_ores.inventory;

import java.util.ArrayList;
import java.util.List;

import neo_ores.main.NeoOres;
import neo_ores.mana.PlayerManaDataServer;
import neo_ores.recipes.ManaCraftingRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerManaWorkbench extends Container
{
    private final IInventory outputSlot;
    private final IInventory inputSlots;
    private final World world;
    private final BlockPos selfPosition;
    public long cost;
    public ManaCraftingRecipes recipe = new ManaCraftingRecipes();
    @SuppressWarnings("unused")
	private final EntityPlayer player;

    @SideOnly(Side.CLIENT)
    public ContainerManaWorkbench(InventoryPlayer playerInventory, World worldIn, EntityPlayer player)
    {
        this(playerInventory, worldIn, BlockPos.ORIGIN, player);
    }

    public ContainerManaWorkbench(InventoryPlayer playerInventory, final World worldIn, final BlockPos blockPosIn, EntityPlayer player)
    {
        this.outputSlot = new InventoryCraftResult()
        {
        	public void markDirty()
            {
                super.markDirty();
                ContainerManaWorkbench.this.onCraftMatrixChanged(ContainerManaWorkbench.this.inputSlots);
            }
        };
        this.inputSlots = new InventoryBasic("ManaWorkbench", true, 9)
        {
        	public int getInventoryStackLimit()
        	{
        		return 64;
        	}
            public void markDirty()
            {
                super.markDirty();
                ContainerManaWorkbench.this.onCraftMatrixChanged(this);
            }
        };
        this.selfPosition = blockPosIn;
        this.world = worldIn;
        this.player = player;
        this.addSlotToContainer(new Slot(this.inputSlots, 0, 40, 18));
        this.addSlotToContainer(new Slot(this.inputSlots, 1, 58, 18));
        this.addSlotToContainer(new Slot(this.inputSlots, 2, 76, 18));
        this.addSlotToContainer(new Slot(this.inputSlots, 3, 40, 36));
        this.addSlotToContainer(new Slot(this.inputSlots, 4, 58, 36));
        this.addSlotToContainer(new Slot(this.inputSlots, 5, 76, 36));
        this.addSlotToContainer(new Slot(this.inputSlots, 6, 40, 54));
        this.addSlotToContainer(new Slot(this.inputSlots, 7, 58, 54));
        this.addSlotToContainer(new Slot(this.inputSlots, 8, 76, 54));
        
        this.addSlotToContainer(new Slot(this.outputSlot, 9, 138, 43)
        {
            public boolean isItemValid(ItemStack stack)
            {
                return false;
            }

            public boolean canTakeStack(EntityPlayer playerIn)
            {
            	if(!player.world.isRemote)
            	{
            		EntityPlayerMP playermp = (EntityPlayerMP)playerIn;
            		PlayerManaDataServer pmd = new PlayerManaDataServer(playermp);
                	return (playerIn.capabilities.isCreativeMode || pmd.getMana() >= ContainerManaWorkbench.this.cost) && ContainerManaWorkbench.this.cost > 0 && this.getHasStack();
            	}
            	else
            	{
            		return (playerIn.capabilities.isCreativeMode || playerIn.getEntityData().getInteger("neo_ores_mana") >= ContainerManaWorkbench.this.cost) && ContainerManaWorkbench.this.cost > 0 && this.getHasStack();
            	}
            }
            
            public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
            {
                if (!thePlayer.capabilities.isCreativeMode && !thePlayer.world.isRemote)
                {
                	EntityPlayerMP playermp = (EntityPlayerMP)thePlayer;
                	PlayerManaDataServer pmd = new PlayerManaDataServer(playermp);
                	pmd.addMana(-ContainerManaWorkbench.this.cost);
                }
                
                for(int i = 0;i < 9;i++)
                {
                	if(ContainerManaWorkbench.this.inputSlots.getStackInSlot(i).getCount() == 1)
                	{
                		ContainerManaWorkbench.this.inputSlots.setInventorySlotContents(i, ItemStack.EMPTY);
                	}
                	else if(!ContainerManaWorkbench.this.inputSlots.getStackInSlot(i).isEmpty())
                	{
                		ContainerManaWorkbench.this.inputSlots.getStackInSlot(i).shrink(1);
                	}
                }
                
                return stack;
            }
        });

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

	public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        super.onCraftMatrixChanged(inventoryIn);

        if (inventoryIn == this.inputSlots)
        {
            this.updateManaCraftOutput();
        }
    }

    public void updateManaCraftOutput()
    {
    	List<ItemStack[]> items = new ArrayList<ItemStack[]>();
        items.add(new ItemStack[] {this.inputSlots.getStackInSlot(0),this.inputSlots.getStackInSlot(1),this.inputSlots.getStackInSlot(2)});
        items.add(new ItemStack[] {this.inputSlots.getStackInSlot(3),this.inputSlots.getStackInSlot(4),this.inputSlots.getStackInSlot(5)});
        items.add(new ItemStack[] {this.inputSlots.getStackInSlot(6),this.inputSlots.getStackInSlot(7),this.inputSlots.getStackInSlot(8)});
        
        this.cost = (Long)recipe.getResult(items)[1];
        ItemStack outputItem = (ItemStack)recipe.getResult(items)[0];
        
        if(outputItem.isEmpty())
        {
        	this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
        	this.cost = 0;
        }
        else
        {
        	this.outputSlot.setInventorySlotContents(0, outputItem);
            this.detectAndSendChanges();
        }
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
    }

    /*
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        if (id == 0)
        {
            this.cost = data;
        }
    }
    */

    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        if (!this.world.isRemote)
        {
            this.clearContainer(playerIn, this.world, this.inputSlots);
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        if (this.world.getBlockState(this.selfPosition).getBlock() != NeoOres.mana_workbench)
        {
            return false;
        }
        else
        {
            return playerIn.getDistanceSq((double)this.selfPosition.getX() + 0.5D, (double)this.selfPosition.getY() + 0.5D, (double)this.selfPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {	
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 9)
            {
            	this.onCraftMatrixChanged(this.inputSlots);
            	if(playerIn instanceof EntityPlayerMP)
            	{
            		EntityPlayerMP playermp = (EntityPlayerMP)playerIn;
            		PlayerManaDataServer pmd = new PlayerManaDataServer(playermp);
                	if((!playerIn.capabilities.isCreativeMode && pmd.getMana() < ContainerManaWorkbench.this.cost) || ContainerManaWorkbench.this.cost <= 0)
                	{
                		return ItemStack.EMPTY;
                	}
            	}
            	
                itemstack1.getItem().onCreated(itemstack1, this.world, playerIn);
                
                if (!this.mergeItemStack(itemstack1, 10, 46, true))
                {
                    return ItemStack.EMPTY;
                }
                
                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index >= 10 && index < 37)
            {
                if (!this.mergeItemStack(itemstack1, 37, 46, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 37 && index < 46)
            {
                if (!this.mergeItemStack(itemstack1, 10, 37, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 10, 46, false))
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

            ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);

            if (index == 9)
            {
                playerIn.dropItem(itemstack2, false);
            }
        }

        return itemstack;
    }
}
