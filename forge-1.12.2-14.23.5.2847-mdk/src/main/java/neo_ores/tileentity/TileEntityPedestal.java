package neo_ores.tileentity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

import neo_ores.api.RecipeOreStack;
import neo_ores.api.RecipeOreStackWildCard;
import neo_ores.api.RecipeOreStackWildCardPostScript;
import neo_ores.api.StackUtils;
import neo_ores.api.Structure;
import neo_ores.api.StructureUtils;
import neo_ores.api.spell.SpellItem;
import neo_ores.client.particle.TexturedParticle;
import neo_ores.event.NeoOresClientEvents;
import neo_ores.item.ISpellWritable;
import neo_ores.item.IPostscriptDataIntoSpell;
import neo_ores.item.ISpellRecipeWritable;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresItems;
import neo_ores.main.Reference;
import neo_ores.packet.PacketItemsToClient;
import neo_ores.util.CompareStateAlter;
import neo_ores.util.SpellUtils;
import net.jafama.FastMath;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class TileEntityPedestal extends AbstractTileEntityPedestal implements ISidedInventory
{
	private ItemStack stack = ItemStack.EMPTY;
	public int tickCount;
	private boolean isMultiblock = false;
	private boolean isCreating = false;
	private int phase = 0;
	private int maxPhase = 0;
	private int requiredSize;
	private NBTTagCompound additionalData = new NBTTagCompound();
	private NBTTagList desc = new NBTTagList();
	private ItemStack writingItem = ItemStack.EMPTY;
	private String alterType = "";
	private static final String[] ALTERS = new String[] { "alter_tier2", "alter_tier2_1", "alter_tier3", "alter_tier3_1", "alter_tier4" };

	@SideOnly(Side.CLIENT)
	public void setClient(boolean multiblock, int phase, int maxPhase, boolean isCreating)
	{
		this.isMultiblock = multiblock;
		this.phase = phase;
		this.maxPhase = maxPhase;
		this.isCreating = isCreating;
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		if (compound.hasKey("display", 10))
		{
			this.display = new ItemStack(compound.getCompoundTag("display"));
		}

		this.stack = new ItemStack(compound.getCompoundTag("stack"));
		this.offset = compound.getDouble("offset");
		this.phase = compound.getInteger("phase");
		this.requiredSize = compound.getInteger("required");
		this.isCreating = compound.getBoolean("isCreating");
		this.additionalData = compound.getCompoundTag(SpellUtils.NBTTagUtils.ADDITIONAL);
		this.desc = compound.getTagList(SpellUtils.NBTTagUtils.SPELL_DESC, 10);
		this.writingItem = new ItemStack(compound.getCompoundTag("writingItem"));
		this.alterType = compound.getString("alterType");
		this.maxPhase = compound.getInteger("maxPhase");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{

		NBTTagCompound nbttagcompound = stack.writeToNBT(new NBTTagCompound());
		compound.setTag("stack", nbttagcompound);

		nbttagcompound = display.writeToNBT(new NBTTagCompound());
		compound.setTag("display", nbttagcompound);
		compound.setDouble("offset", this.offset);
		compound.setInteger("phase", this.phase);
		compound.setInteger("required", this.requiredSize);
		compound.setBoolean("isCreating", this.isCreating);
		compound.setTag(SpellUtils.NBTTagUtils.ADDITIONAL, this.additionalData);
		compound.setTag(SpellUtils.NBTTagUtils.SPELL_DESC, this.desc);
		compound.setTag("writingItem", this.writingItem.writeToNBT(new NBTTagCompound()));
		compound.setString("alterType", alterType);
		compound.setInteger("maxPhase", maxPhase);
		super.writeToNBT(compound);
		return compound;
	}

	@Override
	public boolean isEmpty()
	{
		return stack.isEmpty();
	}

	public ItemStack getStackInSlot(int index)
	{
		return stack;
	}

	public ItemStack decrStackSize(int index, int count)
	{
		return !this.stack.isEmpty() && count > 0 ? this.stack.splitStack(count) : ItemStack.EMPTY;
	}

	public ItemStack removeStackFromSlot(int index)
	{
		ItemStack prev = this.stack;
		this.stack = ItemStack.EMPTY;
		return prev;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.stack = stack;

		if (stack.getCount() > this.getInventoryStackLimit())
		{
			stack.setCount(this.getInventoryStackLimit());
		}
	}

	public void setContents(ItemStack stack)
	{
		this.stack = stack;
	}

	public ItemStack addItemStackToInventory(ItemStack stack)
	{
		if (this.compareWith(stack, this.stack))
		{
			ItemStack stack1 = stack.copy();
			ItemStack stack2 = stack.copy();
			if (this.stack.getMaxStackSize() < this.stack.getCount() + stack.getCount())
			{
				stack1.setCount(this.stack.getMaxStackSize() - this.stack.getCount());
				stack2.setCount(this.stack.getCount() + stack.getCount() - this.stack.getMaxStackSize());
			}
			else
			{
				stack2 = ItemStack.EMPTY;
			}
			this.setInventorySlotContents(0, stack1);
			return stack2;
		}
		else if (this.stack.isEmpty())
		{
			this.setInventorySlotContents(0, stack);
			return ItemStack.EMPTY;
		}
		return stack;
	}

	public boolean isFull()
	{
		return (!this.stack.isEmpty() && this.stack.getCount() == this.stack.getMaxStackSize());
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
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
		return this.compareWith(stack, this.stack) || this.stack.isEmpty();
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
		this.stack = ItemStack.EMPTY;
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
		if (!this.getWorld().isRemote)
		{
			NBTTagCompound packet = new NBTTagCompound();
			packet.setInteger("x", this.pos.getX());
			packet.setInteger("y", this.pos.getY());
			packet.setInteger("z", this.pos.getZ());
			packet.setDouble("offset", offset);
			ItemStack stack = (this.getDisplay().isEmpty()) ? this.stack : this.getDisplay();
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound = stack.writeToNBT(nbttagcompound);
			packet.setTag("display", nbttagcompound);
			packet.setInteger("dim", this.world.provider.getDimension());
			packet.setBoolean("multiblock", this.isMultiblock);
			packet.setInteger("maxPhase", maxPhase);
			packet.setInteger("phase", phase);
			packet.setBoolean("isCreating", isCreating);
			PacketItemsToClient pic = new PacketItemsToClient(packet);
			NeoOres.PACKET.sendToAll(pic);
		}

		super.update();

		if (!this.getWorld().isRemote)
		{
			this.isMultiblock = this.multiBlock();
			boolean flag = false;
			boolean flag2 = true;
			if (this.isMultiblock && this.isCreating)
			{
				List<SpellItem> recipeIn = this.getRecipeIn();
				if (recipeIn.isEmpty())
					flag = true;
				if (this.getEP() == null)
					flag = true;
				if (!flag)
				{
					List<RecipeOreStack> recipeFromList = SpellUtils.getRecipeFromList(recipeIn);
					this.maxPhase = recipeFromList.size();
					if (this.phase < recipeFromList.size())
					{
						if (!recipeFromList.get(this.phase).getListTogether().isEmpty())
						{
							List<ItemStack> list = recipeFromList.get(this.phase).getListTogether();
							this.getEP().setDisplay(list.get((this.tickCount / 20) % list.size()));
						}
						loop0: for (int index = 0; index < this.getEP().getSizeInventory(); index++)
						{
							ItemStack stack = this.getEP().getItems().get(index);
							if (recipeFromList.get(this.phase).compareWith(stack))
							{
								if (recipeFromList.get(this.phase) instanceof RecipeOreStackWildCardPostScript) 
								{
									IPostscriptDataIntoSpell post = (RecipeOreStackWildCardPostScript) recipeFromList.get(this.phase);
									this.additionalData = post.postscript(stack, this.world, this.additionalData);
									this.desc = post.addFormattedDesc(stack, this.world, this.desc);
								}
								else if (recipeFromList.get(this.phase) instanceof RecipeOreStackWildCard)
								{
									if (stack.getItem() != NeoOresItems.spell_sheet)
									{
										this.writingItem = stack.copy();
										this.writingItem.setCount(1);
									}
									else
									{
										this.writingItem = new ItemStack(NeoOresItems.spell);
									}
								}
								else if (stack.getItem() instanceof IPostscriptDataIntoSpell)
								{
									this.additionalData = ((IPostscriptDataIntoSpell) stack.getItem()).postscript(stack, this.world, this.additionalData);
									this.desc = ((IPostscriptDataIntoSpell) stack.getItem()).addFormattedDesc(stack, this.world, this.desc);
								}

								this.getEP().decrStackSize(index, 1);
								this.requiredSize++;
								break loop0;
							}
						}
						if (requiredSize >= recipeFromList.get(this.phase).getSize())
						{
							this.phase++;
							this.requiredSize = 0;
							this.getWorld().playSound(null, (double) this.getPos().getX() + 0.5, (double) this.getPos().getY() - 3.5, (double) this.getPos().getZ() + 0.5,
									SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, this.getWorld().rand.nextFloat() * 0.1F + 0.9F);
						}
					}
					else if (this.phase == recipeFromList.size())
					{
						if (this.writingItem.isEmpty())
						{
							this.getEP().setDisplay(new ItemStack(NeoOresItems.spell_sheet));
							for (int index = 0; index < this.getEP().getSizeInventory(); index++)
							{
								if (this.getEP().getItems().get(index).getItem() instanceof ISpellWritable)
								{
									ItemStack stack = this.getEP().getItems().get(index).copy();
									ItemStack stack1 = ((ISpellWritable) stack.getItem()).writeActiveSpells(recipeIn, stack);
									stack1.getTagCompound().setTag(SpellUtils.NBTTagUtils.ADDITIONAL, this.additionalData);
									stack1.getTagCompound().setTag(SpellUtils.NBTTagUtils.SPELL_DESC, this.desc);
									InventoryHelper.spawnItemStack(this.getWorld(), this.getPos().getX(), this.getPos().getY() - 1, this.getPos().getZ(), stack1);
									this.getEP().decrStackSize(index, 1);
									this.additionalData = new NBTTagCompound();
									this.desc = new NBTTagList();
									flag = true;
									this.getWorld().playSound(null, (double) this.getPos().getX() + 0.5, (double) this.getPos().getY() - 3.5, (double) this.getPos().getZ() + 0.5,
											SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1.0F, 0.5F);
									break;
								}
							}
						}
						else
						{
							ISpellWritable.writeNBT(recipeIn, this.writingItem);
							this.writingItem.getTagCompound().setTag(SpellUtils.NBTTagUtils.ADDITIONAL, this.additionalData);
							this.writingItem.getTagCompound().setTag(SpellUtils.NBTTagUtils.SPELL_DESC, this.desc);
							InventoryHelper.spawnItemStack(this.getWorld(), this.getPos().getX(), this.getPos().getY() - 1, this.getPos().getZ(), this.writingItem);
							this.additionalData = new NBTTagCompound();
							this.desc = new NBTTagList();
							flag = true;
							this.getWorld().playSound(null, (double) this.getPos().getX() + 0.5, (double) this.getPos().getY() - 3.5, (double) this.getPos().getZ() + 0.5,
									SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1.0F, 0.5F);
						}
					}
					else
					{
						flag = true;
					}
				}

				if (!flag)
				{
					flag2 = false;
					((WorldServer) this.getWorld()).spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, true, this.getPos().getX() + 0.5D, this.getPos().getY() - 2.5D, this.getPos().getZ() + 0.5D, 2,
							0, -0.5, 0, 1, new int[0]);
				}
			}

			if (flag2)
			{
				if (this.getEP() != null)
				{
					this.getEP().setDisplay(ItemStack.EMPTY);
				}
			}

			if (flag)
			{
				this.isCreating = false;
			}
		}
		else
		{
			boolean flag = this.isMultiblock();
			if (flag)
			{
				double x = this.getPos().getX() + 0.5D;
				double y = this.getPos().getY() + 0.5D;
				double z = this.getPos().getZ() + 0.5D;
				Random random = this.getWorld().rand;
				for (int i = 0; i < 1; i++)
				{
					double r = 1.0 * random.nextDouble();
					double theta = 2.0 * Math.PI * random.nextDouble();
					double v = 3.0D * random.nextDouble() + 1.0D;
					Vec3d start = new Vec3d(x + r * FastMath.cos(theta), y - 4.5D + (4.0D - v) * random.nextDouble(), z + r * FastMath.sin(theta));
					Vec3d velocity = new Vec3d(0.0, v, 0.0);
					for (int j = 0; j < 4; j++)
					{
						int d = (int) (60.0D / (random.nextDouble() + 0.5D));
						NeoOresClientEvents.getInstance().addParticle(
								new TexturedParticle(start.x, start.y, start.z, velocity.x / d, velocity.y / d, velocity.z / d, d, 6.0F * random.nextFloat() + 1.0F, NeoOres.PARTICLE_MAGIC)
										.setColor(this.getColor(), 1.0F));
					}
				}
			}
		}
	}

	private int getColor()
	{
		if (this.isCreating && this.maxPhase != 0)
		{
			double rate = 1.25 * Math.PI * (double) this.phase / (double) this.maxPhase;
			double red = FastMath.min(FastMath.max(255.0 * FastMath.cos(rate), 0), 255) + FastMath.min(FastMath.max(-255.0 * FastMath.sin(rate), 0), 255);
			double green = FastMath.min(FastMath.max(255.0 * FastMath.sin(rate), 0), 255);
			double blue = FastMath.min(FastMath.max(-255.0 * FastMath.cos(rate), 0), 255);
			return 256 * 256 * (int) red + 256 * (int) green + (int) blue;
		}
		return 0xDDDDDD;
	}

	public boolean multiBlock()
	{
		if (!this.getWorld().isRemote && this.offset < -0.4375)
		{
			if (this.getWorld() instanceof WorldServer)
			{
				WorldServer server = (WorldServer) this.getWorld();
				if (!Arrays.asList(ALTERS).contains(this.alterType))
				{
					return false;
				}
				Structure str = new Structure(server, new ResourceLocation(Reference.MOD_ID, "alter/" + this.alterType)).setPosition(this.getPos().add(-4, -5, -4));
				CompareStateAlter csa = new CompareStateAlter(str);
				return StructureUtils.isMatch(this.getWorld(), str, csa);
			}
		}
		return false;
	}

	public boolean isMultiblock()
	{
		return this.isMultiblock;
	}

	private TileEntityEnhancedPedestal getEP()
	{
		TileEntity te = this.getWorld().getTileEntity(new BlockPos(this.getPos().getX(), this.getPos().getY() - 5, this.getPos().getZ()));
		if (te instanceof TileEntityEnhancedPedestal)
		{
			return (TileEntityEnhancedPedestal) te;
		}
		return null;
	}

	@Override
	public int getSizeInventory()
	{
		return 1;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		if (this.offset < -0.4375 && side != EnumFacing.DOWN || this.offset >= -0.4375 && side != EnumFacing.UP)
		{
			List<Integer> list = new ArrayList<Integer>();
			int size = this.getSizeInventory();
			for (int i = 0; i < size; i++)
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
		return canInsert(index, itemStackIn, direction);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return canExtract(index, stack, direction);
	}

	public static void dropInventoryItems(World worldIn, BlockPos pos, TileEntityPedestal tileentity)
	{
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
		if (!tileentity.getStackInSlot(0).isEmpty())
		{
			InventoryHelper.spawnItemStack(worldIn, x, y, z, tileentity.getStackInSlot(0));
		}
	}

	@Override
	public boolean canExtract(int index, ItemStack stack, EnumFacing direction)
	{
		return (direction != EnumFacing.UP && this.offset >= -0.4375) || (direction != EnumFacing.DOWN && this.offset < -0.4375);
	}

	@Override
	public boolean canInsert(int index, ItemStack stack, EnumFacing direction)
	{
		if (this.offset < -0.4375)
		{
			return direction != EnumFacing.DOWN && this.isItemValidForSlot(index, stack);
		}
		return direction != EnumFacing.UP && this.isItemValidForSlot(index, stack);
	}

	private boolean compareWith(ItemStack stack1, ItemStack stack2)
	{
		return stack1.getItem() == stack2.getItem() && stack1.getItemDamage() == stack2.getItemDamage() && StackUtils.compareNBTWith(stack1, stack2);
	}

	public void spellCreation(World worldIn, BlockPos pos, IBlockState state, @Nullable EntityPlayer playerIn)
	{
		if (!world.isRemote && this.offset < -0.4375)
		{
			if (!isMultiblock)
			{
				this.checkMultiBlockType();
			}
			if (!this.isMultiblock)
			{
				ITextComponent itextcomponent = new TextComponentTranslation("chat.noMultiblock");
				if (playerIn != null)
					playerIn.sendStatusMessage(itextcomponent, true);
			}
			else if (!this.isRecipeIn())
			{
				ITextComponent itextcomponent = new TextComponentTranslation("chat.noRecipe");
				if (playerIn != null)
					playerIn.sendStatusMessage(itextcomponent, true);
			}
			else if (!this.isCreating)
			{
				if (SpellUtils.getMPConsume(getRecipeIn()) <= getMaxMana())
				{
					this.phase = 0;
					this.isCreating = true;
					this.requiredSize = 0;
					this.writingItem = ItemStack.EMPTY;
				}
				else
				{
					ITextComponent itextcomponent = new TextComponentTranslation("chat.lackTier");
					if (playerIn != null)
						playerIn.sendStatusMessage(itextcomponent, true);
				}
			}
		}
	}

	private long getMaxMana()
	{
		if (this.alterType.contains("alter_tier2"))
		{
			return 500L;
		}
		else if (this.alterType.contains("alter_tier3"))
		{
			return 50000L;
		}
		else if (this.alterType.contains("alter_tier4"))
		{
			return Long.MAX_VALUE;
		}

		return 0L;
	}

	private void checkMultiBlockType()
	{
		if (!this.getWorld().isRemote && this.offset < -0.4375)
		{
			if (this.getWorld() instanceof WorldServer)
			{
				WorldServer server = (WorldServer) this.getWorld();
				for (String type : ALTERS)
				{
					Structure str = new Structure(server, new ResourceLocation(Reference.MOD_ID, "alter/" + type)).setPosition(this.getPos().add(-4, -5, -4));
					CompareStateAlter csa = new CompareStateAlter(str);
					if (StructureUtils.isMatch(this.getWorld(), str, csa))
					{
						this.alterType = type;
						this.isMultiblock = true;
						break;
					}
				}
			}
		}
	}

	private boolean isRecipeIn()
	{
		return this.getStackInSlot(0).getItem() instanceof ISpellRecipeWritable && ((ISpellRecipeWritable) this.getStackInSlot(0).getItem()).hasRecipe(this.getStackInSlot(0));
	}

	private List<SpellItem> getRecipeIn()
	{
		if (this.isRecipeIn())
			return ((ISpellRecipeWritable) this.getStackInSlot(0).getItem()).readRecipeSpells(this.getStackInSlot(0));
		return new ArrayList<SpellItem>();
	}
	

	IItemHandler handlerTop = new SidedInvWrapper(this, EnumFacing.UP);
	IItemHandler handlerBottom = new SidedInvWrapper(this, EnumFacing.DOWN);
	IItemHandler handlerWest = new SidedInvWrapper(this, EnumFacing.WEST);
	IItemHandler handlerEast = new SidedInvWrapper(this, EnumFacing.EAST);
	IItemHandler handlerSouth = new SidedInvWrapper(this, EnumFacing.SOUTH);
	IItemHandler handlerNorth = new SidedInvWrapper(this, EnumFacing.NORTH);
	
	@Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }
	
	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (!this.hasCapability(capability, facing))
			return null;
		if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			if (facing == EnumFacing.DOWN)
				return (T) handlerBottom;
			else if (facing == EnumFacing.UP)
				return (T) handlerTop;
			else if (facing == EnumFacing.WEST)
				return (T) handlerWest;
			else if (facing == EnumFacing.EAST)
				return (T) handlerEast;
			else if (facing == EnumFacing.SOUTH)
				return (T) handlerSouth;
			else if (facing == EnumFacing.NORTH)
				return (T) handlerNorth;
		return super.getCapability(capability, facing);
	}
}
