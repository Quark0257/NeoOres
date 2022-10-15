package neo_ores.tileentity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.FakePlayer;

public class TileEntityMechanicalMagician extends TileEntity implements ITickable, ISidedInventory
{
	public final FakePlayer player;
	public boolean redstone;
	public boolean activated;
	
	public TileEntityMechanicalMagician(FakePlayer player)
	{
		this.player = player;
	}
	
	@Override
	public int getSizeInventory() 
	{
		return 0;
	}

	@Override
	public boolean isEmpty() 
	{
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) 
	{
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) 
	{
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) 
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) 
	{
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 0;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) 
	{
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) 
	{
	}

	@Override
	public void closeInventory(EntityPlayer player) 
	{
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) 
	{
		return false;
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
	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public boolean hasCustomName() 
	{
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) 
	{
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) 
	{
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) 
	{
		return false;
	}

	@Override
	public void update() 
	{
		if(this.redstone && !this.activated)
		{
			if(redstone)
			{
				this.player.getHeldItemMainhand().useItemRightClick(world, player, EnumHand.MAIN_HAND);
			}
			else
			{
				double d = 0;
				EntityLivingBase target = null;
				for(EntityLivingBase elb : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.getPos().getX(),this.getPos().getY(),this.getPos().getZ(),this.getPos().getX() + 1.0D,this.getPos().getY() + 1.0D,this.getPos().getZ() + 1.0D)))
				{
					double x = elb.posX - this.getPos().getX() - 0.5D;
					double y = elb.posX - this.getPos().getY() - 0.5D;
					double z = elb.posX - this.getPos().getZ() - 0.5D;
					double d0 = x * x + y * y + z * z;
					if(d <= 0) d = d0;
					else
					{
						if(d > d0)
						{
							d = d0;
							elb = target;
						}
					}
				}
				this.player.getHeldItemMainhand().interactWithEntity(player,target, EnumHand.MAIN_HAND);
			}
			this.activated = true;
		}
		
		if(!this.redstone)
		{
			this.activated = false;
		}
	}
}
