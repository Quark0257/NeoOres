package neo_ores.tileentity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import neo_ores.api.RecipeOreStack;
import neo_ores.api.StackUtils;
import neo_ores.api.Structure;
import neo_ores.api.StructureUtils;
import neo_ores.api.spell.SpellItem;
import neo_ores.item.ISpellWritable;
import neo_ores.item.IPostscriptDataIntoSpell;
import neo_ores.item.ISpellRecipeWritable;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresItems;
import neo_ores.main.Reference;
import neo_ores.packet.PacketItemsToClient;
import neo_ores.util.CompareStateAlter;
import neo_ores.util.SpellUtils;
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
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TileEntityPedestal extends AbstractTileEntityPedestal implements ISidedInventory
{
	private ItemStack stack = ItemStack.EMPTY;
	public int tickCount;
	private boolean isMultiblock = false;
	private boolean isCreating = false;
	private int phase = 0;
	private int requiredSize;
	private NBTTagCompound additionalData = new NBTTagCompound();
	private NBTTagList desc = new NBTTagList();

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
		this.additionalData = compound.getCompoundTag("additionalData");
		this.desc = compound.getTagList("desc", 10);
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
		compound.setTag("additionalData", this.additionalData);
		compound.setTag("desc", this.desc);
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
		return !stack.isEmpty() && count > 0 ? stack.splitStack(count) : ItemStack.EMPTY;
	}

	public ItemStack removeStackFromSlot(int index)
	{
		ItemStack stackcopy = this.stack.copy();
		this.stack = ItemStack.EMPTY;
		return stackcopy;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		if (this.compareWith(stack, this.stack))
		{
			ItemStack stack2 = this.stack.copy();
			this.stack.setCount(stack2.getCount() + stack.getCount());
		}
		else if (this.stack.isEmpty())
		{
			this.stack = stack;
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
			PacketItemsToClient pic = new PacketItemsToClient(packet);
			NeoOres.PACKET.sendToAll(pic);
		}

		super.update();
		this.isMultiblock = this.multiBlock();

		if (!this.getWorld().isRemote)
		{
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
					if (this.phase < recipeFromList.size())
					{
						if (!recipeFromList.get(this.phase).getListTogether().isEmpty())
						{
							List<ItemStack> list = recipeFromList.get(this.phase).getListTogether();
							this.getEP().setDisplay(list.get((this.tickCount / 20) % list.size()));
						}
						loop0: for (int index = 0; index < this.getEP().getSizeInventory(); index++)
						{
							for (ItemStack stack : this.getEP().getItems().get(index).asList(64))
							{
								if (recipeFromList.get(this.phase).compareWith(stack))
								{
									this.getEP().decrStackSize(index, 1);
									this.requiredSize++;
									if (stack.getItem() instanceof IPostscriptDataIntoSpell)
									{
										this.additionalData = ((IPostscriptDataIntoSpell) stack.getItem()).postscript(stack, this.world, this.additionalData);
										this.desc = ((IPostscriptDataIntoSpell) stack.getItem()).addFormattedDesc(stack, this.world, this.desc);
									}
									break loop0;
								}
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
						this.getEP().setDisplay(new ItemStack(NeoOresItems.spell_sheet));
						for (int index = 0; index < this.getEP().getSizeInventory(); index++)
						{
							if (this.getEP().getItems().get(index).getStack().getItem() instanceof ISpellWritable)
							{
								ItemStack stack = this.getEP().getItems().get(index).getStack().copy();
								ItemStack stack1 = ((ISpellWritable) stack.getItem()).writeActiveSpells(recipeIn, stack);
								stack1.getTagCompound().setTag("additionalData", this.additionalData);
								stack1.getTagCompound().setTag("desc", this.desc);
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

	}

	public boolean multiBlock()
	{
		if (!this.getWorld().isRemote && this.offset < -0.4375)
		{
			if (this.getWorld() instanceof WorldServer)
			{
				WorldServer server = (WorldServer) this.getWorld();
				Structure str = new Structure(server, new ResourceLocation(Reference.MOD_ID, "alter/alter")).setPosition(this.getPos().add(-4, -5, -4));
				CompareStateAlter csa = new CompareStateAlter(str);
				return StructureUtils.isMatch(this.getWorld(), str, csa);
			}
		}
		return false;
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

	public void spellCreation(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			if (!this.isMultiblock)
			{
				ITextComponent itextcomponent = new TextComponentTranslation("chat.noMultiblock");
				playerIn.sendStatusMessage(itextcomponent, true);
			}
			else if (!this.isRecipeIn())
			{
				ITextComponent itextcomponent = new TextComponentTranslation("chat.noRecipe");
				playerIn.sendStatusMessage(itextcomponent, true);
			}
			else
			{
				this.phase = 0;
				this.isCreating = true;
				this.requiredSize = 0;
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
}
