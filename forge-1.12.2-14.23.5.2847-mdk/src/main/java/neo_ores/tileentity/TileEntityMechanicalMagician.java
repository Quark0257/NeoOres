package neo_ores.tileentity;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import neo_ores.api.FakePlayerMechanicalMagician;
import neo_ores.api.MathUtils;
import neo_ores.api.spell.SpellItem;
import neo_ores.config.NeoOresConfig;
import neo_ores.inventory.ContainerMechanicalMagician;
import neo_ores.item.IItemTotem;
import neo_ores.item.IItemUpgrade;
import neo_ores.item.ItemSpell;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.NeoOresData;
import neo_ores.packet.PacketDestinationToClient;
import neo_ores.util.ManaTank;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileEntityMechanicalMagician extends TileEntityLockable implements ITickable, ISidedInventory
{
	public static final int TANK_CAP = 40000;
	private FakePlayerMechanicalMagician player = null;
	private NBTTagCompound playerNBT = null;
	private boolean redstone = false;
	private boolean activated = false;
	private BlockPos destination = BlockPos.ORIGIN;
	private static final Random rand = new Random();
	private double reach = 4.0;
	private String mmCustomName;
	private int mxp = 0;
	public long noConsumeMana = 0L;
	public boolean makeExp = false;
	/**
	 *  if this boolean is true, no work makeExp
	 */
	public boolean useLiquidMana = false;
	private ManaTank tank;
	public boolean voidExp = false;

	public boolean isUseLiquidManaGui = false;
	public int liquidAmount = 0;
	private NonNullList<ItemStack> mmItemStacks = NonNullList.<ItemStack>withSize(14, ItemStack.EMPTY);

	public TileEntityMechanicalMagician()
	{
		this.tank = new ManaTank(TANK_CAP);
		this.markDirty();
	}

	public void setPlayer(WorldServer world, BlockPos destination)
	{
		this.player = new FakePlayerMechanicalMagician(world, this.getPos(), destination.subtract(this.getPos()), this);
		this.destination = destination;

		this.markDirty();
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.mxp = compound.getInteger("mxp");
		this.tank.readFromNBT(compound);
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
		if (compound.hasKey("CustomName", 8))
		{
			this.mmCustomName = compound.getString("CustomName");
		}

		if (compound.hasKey("player"))
		{
			this.playerNBT = compound.getCompoundTag("player");
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("mxp", this.mxp);
		this.tank.writeToNBT(compound);
		int[] desti = new int[3];
		desti[0] = this.destination.getX();
		desti[1] = this.destination.getY();
		desti[2] = this.destination.getZ();
		compound.setIntArray("destination", desti);
		compound.setBoolean("activated", this.activated);
		compound.setBoolean("redstone", this.redstone);
		compound.setDouble("reach", this.reach);
		ItemStackHelper.saveAllItems(compound, this.mmItemStacks);

		if (this.hasCustomName())
		{
			compound.setString("CustomName", this.mmCustomName);
		}

		if (this.player != null)
		{
			NBTTagCompound player = new NBTTagCompound();
			this.player.writeEntityToNBT(player);
			compound.setTag("player", player);
		}

		return compound;
	}

	public void setDestination(BlockPos destination)
	{
		this.destination = destination;
		Vec2f direction = MathUtils.getYawPitch(destination.getX() - this.getPos().getX(), destination.getY() - this.getPos().getY(), destination.getZ() - this.getPos().getZ());
		if (this.player != null)
		{
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
		this.mmItemStacks.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit())
		{
			stack.setCount(this.getInventoryStackLimit());
		}
	}

	public String getName()
	{
		return this.hasCustomName() ? this.mmCustomName : "container.mechanical_magician";
	}

	public boolean hasCustomName()
	{
		return this.mmCustomName != null && !this.mmCustomName.isEmpty();
	}

	public void setCustomInventoryName(String name)
	{
		this.mmCustomName = name;
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
		return !(0 <= index && index <= 4) || (0 == index && stack.getItem() instanceof ItemSpell) || (1 == index && stack.getItem() instanceof IItemTotem)
				|| (2 <= index && index <= 4 && stack.getItem() instanceof IItemUpgrade);
	}

	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public int getField(int id)
	{
		if (id == 0)
		{
			return this.liquidAmount;
		}
		else if (id == 1)
		{
			return this.isUseLiquidManaGui ? 1 : 0;
		}
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
		if (id == 0)
		{
			this.liquidAmount = value;
		}
		else if (id == 1)
		{
			this.isUseLiquidManaGui = value == 1;
		}
	}

	@Override
	public int getFieldCount()
	{
		return 2;
	}

	@Override
	public void clear()
	{
		this.mmItemStacks.clear();
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		int[] is = new int[9];
		for (int i = 5; i < 14; i++)
		{
			is[i - 5] = i;
		}
		return is;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return !(0 <= index && index <= 4);
	}

	public void setRedstone(boolean flag)
	{
		this.redstone = flag;
		this.markDirty();
	}

	@Override
	public void update()
	{
		this.upgradeInit();
		this.upgradeApply();
		if (!this.world.isRemote)
		{
			this.isUseLiquidManaGui = this.useLiquidMana;
			this.liquidAmount = this.tank.getFluidAmount();
		}

		if (this.player == null && !this.getWorld().isRemote)
		{
			this.setPlayer((WorldServer) this.getWorld(), this.destination);
			if (this.playerNBT != null)
			{
				this.player.readEntityFromNBT(this.playerNBT);
			}
		}

		Vec2f direction = this.getDirection();
		if (this.player != null)
		{
			this.player.rotationPitch = direction.y;
			this.player.rotationYaw = direction.x;
			PlayerMagicData pmd = NeoOresData.instance.getPMD(this.player);
			if(pmd.getMana() == pmd.getMaxMana() && this.tank.fill(new FluidStack(NeoOresBlocks.fluid_mana, 1), false) == 1) 
			{
				pmd.setMana(0L);
				this.tank.fill(new FluidStack(NeoOresBlocks.fluid_mana, 1), true);
			}
		}

		if (this.redstone && !this.activated)
		{
			ItemStack itemspell = this.getStackInSlot(0);
			if (itemspell.getTagCompound() != null && itemspell.getTagCompound().hasKey(SpellUtils.NBTTagUtils.SPELL))
			{
				PlayerMagicData pmd = null;
				int value = rand.nextInt(Integer.MAX_VALUE);
				List<SpellItem> rawSpellList = SpellUtils.getListFromItemStackNBT(itemspell.getTagCompound().copy());
				ItemStack totem = this.getStackInSlot(1);
				boolean flag = totem.getItem() instanceof IItemTotem && (this.mxp <= value);
				long manaConsume = SpellUtils.getMPConsume(rawSpellList);
				if (flag)
				{
					IItemTotem itemtotem = (IItemTotem) this.getStackInSlot(1).getItem();
					if (itemtotem.needsPlayer(totem))
					{
						if (!itemtotem.hasPlayerUUID(totem))
							flag = false;
						else
						{
							UUID uuid = UUID.fromString(itemtotem.getPlayerUUID(totem));
							EntityPlayer player = this.world.getPlayerEntityByUUID(uuid);
							if (player != null && !(player instanceof FakePlayer) && player instanceof EntityPlayerMP)
							{
								if (!this.useLiquidMana && !player.isCreative())
								{
									pmd = NeoOresData.instance.getPMD((EntityPlayerMP) player);
									if (manaConsume > pmd.getMana())
										flag = false;
									else
										pmd.addMana(-manaConsume);
								}
							}
							else
								flag = false;
						}
					}
					else if (!itemtotem.isCreative(totem) && !this.useLiquidMana)
					{
						flag = false;
					}

					if (!itemtotem.isCreative(totem) && this.useLiquidMana)
					{
						long consume = (manaConsume <= this.noConsumeMana) ? 0L : (long) Math.max(((double) manaConsume) / NeoOresConfig.magic.liquid_mana_convert_rate, 1.0D);
						if (consume > (long) this.tank.getFluidAmount())
							flag = false;
						else
							this.tank.drain((int) consume, true);
					}
				}

				if (flag)
				{
					SpellUtils.run(rawSpellList, this.getWorld(), this.player, this.getStackInSlot(0), null);
					if (this.voidExp)
					{
						NeoOresData.instance.getPMD(this.player).setMXP(0L);
					}

					if (pmd != null && this.makeExp)
					{
						pmd.addMXP(NeoOresData.instance.getPMD(this.player).getMXP()); // TODO
						NeoOresData.instance.getPMD(this.player).setMXP(0L);
					}
				}
			}
			this.activated = true;

			this.markDirty();
		}

		if (!this.redstone)
		{
			this.activated = false;
		}

		if (this.player != null)
		{
			int addition = (int) Math.min(NeoOresData.instance.getPMD(this.player).getMXP(), Integer.MAX_VALUE - this.mxp);
			this.mxp += addition;
			NeoOresData.instance.getPMD(this.player).addMXP(-addition);
		}

		if (!this.getWorld().isRemote)
		{
			PacketDestinationToClient pdc = new PacketDestinationToClient(this.getPos(), this.destination);
			NeoOres.PACKET.sendToAll(pdc);
		}
	}

	public void upgradeInit()
	{
		this.useLiquidMana = false;
		this.makeExp = false;
		this.voidExp = false;
		this.noConsumeMana = 0L;
	}

	public void upgradeApply()
	{
		for (int i = 2; i < 5; i++)
		{
			ItemStack upgrade = this.getStackInSlot(i);
			if (upgrade.getItem() instanceof IItemUpgrade)
			{
				IItemUpgrade ug = (IItemUpgrade) upgrade.getItem();
				ug.upgrade(this, upgrade);
			}
		}
	}

	public Vec2f getDirection()
	{
		return MathUtils.getYawPitch(this.destination.getX() - this.getPos().getX(), this.destination.getY() - this.getPos().getY(), this.destination.getZ() - this.getPos().getZ());
	}

	public String getGuiID()
	{
		return "neo_ores:mechanical_magician";
	}

	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerMechanicalMagician(playerInventory, this);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (!this.hasCapability(capability, facing))
			return null;
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T) this.tank;
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return (this.useLiquidMana && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) || super.hasCapability(capability, facing);
	}

	public ManaTank getTank()
	{
		return this.useLiquidMana ? this.tank : new ManaTank(0);
	}
}
