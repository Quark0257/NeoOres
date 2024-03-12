package neo_ores.tileentity;

import java.util.Arrays;

import neo_ores.api.FakePlayerMechanicalMagician;
import neo_ores.api.MathUtils;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresSpells;
import neo_ores.packet.PacketDestinationToClient;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.WorldServer;

public class TileEntityMechanicalMagician extends TileEntity implements ITickable, ISidedInventory
{
	private FakePlayerMechanicalMagician player = null;
	private boolean redstone = false;
	private boolean activated = false;
	private BlockPos destination = BlockPos.ORIGIN;
	private double reach = 4.0;
	private long mxp = 0L;
	private NonNullList<ItemStack> mmItemStacks = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);

	public TileEntityMechanicalMagician()
	{
		
	}

	public void setPlayer(WorldServer world, BlockPos destination)
	{
		this.player = new FakePlayerMechanicalMagician(world, this.getPos(), destination.subtract(this.getPos()));
		this.destination = destination;
		
		this.markDirty();
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		//if(this.player != null) this.player.readEntityFromNBT(compound.getCompoundTag("player"));
		compound.setLong("mxp", this.mxp);
		int[] desti = compound.getIntArray("destination");
		if (desti.length == 3)
		{
			this.destination = new BlockPos(desti[0], desti[1], desti[2]);
		}
		this.activated = compound.getBoolean("activated");
		this.redstone = compound.getBoolean("redstone");
		this.reach = compound.getDouble("reach");
		this.mmItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.mmItemStacks);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		//NBTTagCompound playerTag = new NBTTagCompound();
		//(this.player != null) this.player.writeEntityToNBT(playerTag);
		//compound.setTag("player", playerTag);
		compound.setLong("mxp", this.mxp);
		int[] desti = new int[3];
		desti[0] = this.destination.getX();
		desti[1] = this.destination.getY();
		desti[2] = this.destination.getZ();
		compound.setIntArray("destination", desti);
		compound.setBoolean("activated", this.activated);
		compound.setBoolean("redstone", this.redstone);
		compound.setDouble("reach", this.reach);
		ItemStackHelper.saveAllItems(compound, this.mmItemStacks);

		return compound;
	}

	public void setDestination(BlockPos destination)
	{
		this.destination = destination;
		Vec2f direction = MathUtils.getYawPitch(destination.getX() - this.getPos().getX(), destination.getY() - this.getPos().getY(), destination.getZ() - this.getPos().getZ());
		if(this.player != null) {
			this.player.rotationPitch = direction.y;
			this.player.rotationYaw = direction.x;
		}
		
		this.markDirty();
	}

	@Override
	public int getSizeInventory()
	{
		return this.mmItemStacks.size();
	}

	@Override
	public boolean isEmpty()
	{
		for (ItemStack itemstack : this.mmItemStacks)
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
		return this.mmItemStacks.get(index);
	}

	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(this.mmItemStacks, index, count);
	}

	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.mmItemStacks, index);
	}

	public void setInventorySlotContents(int index, ItemStack stack)
	{
		ItemStack itemstack = this.mmItemStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.mmItemStacks.set(index, stack);

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


	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		if (this.world.getTileEntity(this.pos) != this)
		{
			return false;
		}
		else
		{
			return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
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
		return true;
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
		this.mmItemStacks.clear();
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return new int[] { 0 };
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
	
	public void setRedstone(boolean flag) {
		this.redstone = flag;
		this.markDirty();
	}

	@Override
	public void update()
	{
		if(this.player == null && !this.getWorld().isRemote) {
			this.setPlayer((WorldServer)this.getWorld(), destination);
		}
		
		Vec2f direction = this.getDirection();
		if(this.player != null) {
			this.player.setMagicXp(this.mxp);
			this.player.rotationPitch = direction.y;
			this.player.rotationYaw = direction.x;
		}
		
		if (this.redstone && !this.activated)
		{
			SpellUtils.run(Arrays.asList(NeoOresSpells.spell_touch, NeoOresSpells.spell_dig), this.getWorld(), this.player, this.player.getHeldItemMainhand(), null);

			this.activated = true;
			
			this.markDirty();
		}

		if (!this.redstone)
		{
			this.activated = false;
		}
		
		if(this.player != null) {
			this.mxp = this.player.getMagicXp();
		}
		
		if(!this.getWorld().isRemote) {
			PacketDestinationToClient pdc = new PacketDestinationToClient(this.getPos(),this.destination);
			NeoOres.PACKET.sendToAll(pdc);
		}
	}
	
	public Vec2f getDirection() {
		return MathUtils.getYawPitch(destination.getX() - this.getPos().getX(), destination.getY() - this.getPos().getY(), destination.getZ() - this.getPos().getZ());
	}
}
