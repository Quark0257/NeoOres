package neo_ores.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import neo_ores.api.LargeItemStack;
import neo_ores.block.properties.PedestalTiers;
import neo_ores.item.ItemBlockEnhancedPedestal;
import neo_ores.main.NeoOresItems;
import neo_ores.tileentity.TileEntityEnhancedPedestal;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEnhancedPedestal extends NeoOresBlock implements ITileEntityProvider
{
	protected static final AxisAlignedBB AABB_BOUNDING = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
	protected static final AxisAlignedBB AABB_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 0.125D);
	protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 0.8125D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 0.8125D, 1.0D);

	public BlockEnhancedPedestal()
	{
		super(Material.ANVIL);
		this.setSoundType(SoundType.METAL);
		this.setHardness(5.0F);
		this.setHarvestLevel("pickaxe", 0);
		this.setResistance(Float.MAX_VALUE);
	}

	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
	{
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BOTTOM);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB_BOUNDING;
	}

	public boolean isFullCube(IBlockState iblockstate)
	{
		return false;
	}

	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityEnhancedPedestal)
		{
			TileEntityEnhancedPedestal.dropInventoryItems(worldIn, pos, (TileEntityEnhancedPedestal) tileentity);
		}

		super.breakBlock(worldIn, pos, state);
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
			return true;
		ItemStack itemstack = playerIn.getHeldItem(hand);

		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityEnhancedPedestal)
		{
			TileEntityEnhancedPedestal teep = (TileEntityEnhancedPedestal) tileentity;
			if (itemstack.isEmpty())
			{
				return false;
			}
			else
			{
				Item item = itemstack.getItem();
				if (!playerIn.isSneaking() && item == NeoOresItems.mana_wrench)
				{
					teep.addSlot(1);
					return true;
				}
				else if (playerIn.isSneaking() && item == NeoOresItems.mana_wrench)
				{
					teep.addSlot(-1);
					return true;
				}
				else if (item == NeoOresItems.spell)
				{
					return false;
				}
				else if (!playerIn.isSneaking())
				{
					playerIn.setHeldItem(hand, teep.addItemStackToInventory(itemstack));
					return true;
				}
			}
		}

		return false;
	}

	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player)
	{
		if (world.isRemote)
			return;
		@SuppressWarnings("deprecation")
		RayTraceResult result = ForgeHooks.rayTraceEyes(player, ((EntityPlayerMP) player).interactionManager.getBlockReachDistance() + 1.0D);
		if (result == null || result.typeOfHit != Type.BLOCK)
			return;
		TileEntity tileentity = world.getTileEntity(pos);

		if (tileentity instanceof TileEntityEnhancedPedestal)
		{
			TileEntityEnhancedPedestal teep = (TileEntityEnhancedPedestal) tileentity;
			int slot = teep.getSlot();
			LargeItemStack isws = teep.getItems().get(slot);
			ItemStack stack = isws.getStack().copy();

			if (player.isSneaking())
			{
				if (isws.getSize() > stack.getMaxStackSize())
				{
					stack.setCount(stack.getMaxStackSize());
					this.addStackToPlayer(player, stack.copy());
					teep.decrStackSize(slot, stack.getMaxStackSize());
				}
				else
				{
					stack.setCount(isws.getSize());
					this.addStackToPlayer(player, stack.copy());
					teep.removeStackFromSlot(slot);
				}
			}
			else
			{
				if (isws.getSize() > 1)
				{
					stack.setCount(1);
					this.addStackToPlayer(player, stack.copy());
					teep.decrStackSize(slot, 1);
				}
				else
				{
					stack.setCount(1);
					this.addStackToPlayer(player, stack.copy());
					teep.removeStackFromSlot(slot);
				}
			}
		}
	}

	public int getTier(int meta)
	{
		return this.getStateFromMeta(meta).getValue(TIER).getTier();
	}

	public boolean canSuck(int meta)
	{
		return this.getStateFromMeta(meta).getValue(TIER).canSuck();
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		TileEntityEnhancedPedestal teep = new TileEntityEnhancedPedestal();
		teep.setSize(this.getTier(meta));
		teep.setSlot(0);
		teep.setDisplay(ItemStack.EMPTY);
		teep.setSuckable(this.canSuck(meta));
		return teep;
	}

	// Property
	public static final PropertyEnum<PedestalTiers> TIER = PropertyEnum.create("tier", PedestalTiers.class);

	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(this);
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(worldIn.getBlockState(pos)));
	}

	protected ItemStack getSilkTouchDrop(IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(state));
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return this.getSilkTouchDrop(state).getMetadata();
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(TIER).getMeta();
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(TIER, PedestalTiers.getFromMeta(meta));
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)));
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> stacks)
	{
		for (PedestalTiers name : PedestalTiers.values())
		{
			stacks.add(new ItemStack(Item.getItemFromBlock(this), 1, name.getMeta()));
		}
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { TIER });
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return this.getUnlocalizedName();
	}

	private void addStackToPlayer(EntityPlayer entityplayer, ItemStack itemstack)
	{
		if (!itemstack.isEmpty() && entityplayer.isServerWorld())
		{
			boolean flag = entityplayer.inventory.addItemStackToInventory(itemstack);

			if (flag)
			{
				entityplayer.world.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
						((entityplayer.getRNG().nextFloat() - entityplayer.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
				entityplayer.inventoryContainer.detectAndSendChanges();
			}

			if (flag && itemstack.isEmpty())
			{
			}
			else
			{
				EntityItem entityitem = entityplayer.dropItem(itemstack, false);

				if (entityitem != null)
				{
					entityitem.setNoPickupDelay();
					entityitem.setOwner(entityplayer.getName());
				}
			}
		}
	}

	// 0~15 available
	public int getMaxMeta()
	{
		return 15;
	}

	public Item getItemBlock(Block block)
	{
		return new ItemBlockEnhancedPedestal(block).setRegistryName(block.getRegistryName());
	}
}
