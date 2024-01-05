package neo_ores.tileentity;

import neo_ores.api.FakePlayerMechanicalMagician;
import neo_ores.api.MathUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.WorldServer;

public class TileEntityMechanicalMagician extends TileEntity implements ITickable, ISidedInventory
{
	private FakePlayerMechanicalMagician player = null;
	public boolean redstone;
	public boolean activated;
	private BlockPos destination;

	public TileEntityMechanicalMagician()
	{

	}

	public void setPlayer(WorldServer world, BlockPos pos, EnumFacing direction)
	{
		this.player = new FakePlayerMechanicalMagician(world, pos, direction);
		this.destination = pos.add(direction.getDirectionVec());
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		this.player.readEntityFromNBT(compound.getCompoundTag("player"));
		int[] desti = compound.getIntArray("destination");
		if (desti.length == 3)
		{
			this.destination = new BlockPos(desti[0], desti[1], desti[2]);
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		NBTTagCompound playerTag = new NBTTagCompound();
		this.player.writeEntityToNBT(playerTag);
		compound.setTag("player", playerTag);
		int[] desti = new int[3];
		desti[0] = this.destination.getX();
		desti[1] = this.destination.getY();
		desti[2] = this.destination.getZ();
		compound.setIntArray("destination", desti);

		return super.writeToNBT(compound);
	}

	public void setDestination(BlockPos destination)
	{
		this.destination = destination;
		Vec2f direction = MathUtils.getYawPitch(destination.getX() - this.getPos().getX(), destination.getY() - this.getPos().getY(), destination.getZ() - this.getPos().getZ());
		this.player.rotationPitch = direction.y;
		this.player.rotationYaw = direction.x;
	}

	@Override
	public int getSizeInventory()
	{
		return 14;
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
		if (this.redstone && !this.activated)
		{
			EntityLivingBase target = null;
			double d = 0;
			for (EntityLivingBase elb : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.destination.getX(), this.destination.getY(), this.destination.getZ(),
					this.destination.getX() + 1.0D, this.destination.getY() + 1.0D, this.destination.getZ() + 1.0D)))
			{
				double x = elb.posX - this.getPos().getX() - 0.5D;
				double y = elb.posX - this.getPos().getY() - 0.5D;
				double z = elb.posX - this.getPos().getZ() - 0.5D;
				double d0 = x * x + y * y + z * z;
				if (d <= 0)
					d = d0;
				else
				{
					if (d > d0)
					{
						d = d0;
						target = elb;
					}
				}
			}

			SpellUtils.run(null, this.player, this.player.getHeldItemMainhand(), target);

			this.activated = true;
		}

		if (!this.redstone)
		{
			this.activated = false;
		}
	}
}
