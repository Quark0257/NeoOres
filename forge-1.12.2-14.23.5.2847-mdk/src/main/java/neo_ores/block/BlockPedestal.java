package neo_ores.block;

import java.util.List;
import javax.annotation.Nullable;

import neo_ores.main.NeoOresItems;
import neo_ores.tileentity.TileEntityPedestal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPedestal extends NeoOresBlock implements ITileEntityProvider
{
	private boolean watered;
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.VERTICAL);

	protected static final AxisAlignedBB AABB_BOUNDING = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
	protected static final AxisAlignedBB AABB_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 0.125D);
	protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 0.8125D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 0.8125D, 1.0D);

	protected static final AxisAlignedBB AABB_BOUNDING2 = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D);
	protected static final AxisAlignedBB AABB_BOUNDING3 = new AxisAlignedBB(0.0D, 0.375D, 0.0D, 1.0D, 1.0D, 1.0D);

	public BlockPedestal(boolean water)
	{
		super(Material.ANVIL);
		this.setSoundType(SoundType.METAL);
		this.setHardness(5.0F);
		this.setHarvestLevel("pickaxe", 0);
		this.setResistance(Float.MAX_VALUE);
		this.watered = water;
	}

	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
	{
		if (watered)
		{
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BOTTOM);
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST);
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH);
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST);
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH);
		}
		else if (state.getValue(FACING) == EnumFacing.UP)
		{
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BOUNDING3);
		}
		else
		{
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BOUNDING2);
		}
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		if (watered)
			return AABB_BOUNDING;
		else if (state.getValue(FACING) == EnumFacing.UP)
			return AABB_BOUNDING3;
		return AABB_BOUNDING2;
	}

	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if (watered && entityIn.posY >= pos.getY() + 0.5625 && entityIn.posY < pos.getY() + 0.8125)
		{
			entityIn.extinguish();
		}
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
		return BlockRenderLayer.TRANSLUCENT;
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityPedestal)
		{
			TileEntityPedestal.dropInventoryItems(worldIn, pos, (TileEntityPedestal) tileentity);
		}

		super.breakBlock(worldIn, pos, state);
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
			return true;
		ItemStack itemstack = playerIn.getHeldItem(hand);

		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityPedestal)
		{
			TileEntityPedestal teep = (TileEntityPedestal) tileentity;
			if (itemstack.isEmpty())
			{
				return false;
			}
			else
			{
				Item item = itemstack.getItem();
				if (item == NeoOresItems.spell)
				{
					return false;
				}
				else if (item == NeoOresItems.mana_wrench)
				{
					teep.spellCreation(worldIn, pos, state, playerIn, hand, facing, hitZ, hitZ, hitZ);
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

		if (tileentity instanceof TileEntityPedestal)
		{
			TileEntityPedestal teep = (TileEntityPedestal) tileentity;
			ItemStack stack = teep.getStackInSlot(0).copy();
			if (player.isSneaking())
			{
				if (stack.getCount() > stack.getMaxStackSize())
				{
					stack.setCount(stack.getMaxStackSize());
					this.addStackToPlayer(player, stack.copy());
					teep.decrStackSize(0, stack.getMaxStackSize());
				}
				else
				{
					stack.setCount(stack.getCount());
					this.addStackToPlayer(player, stack.copy());
					teep.removeStackFromSlot(0);
				}
			}
			else
			{
				if (stack.getCount() > 1)
				{
					stack.setCount(1);
					this.addStackToPlayer(player, stack.copy());
					teep.decrStackSize(0, 1);
				}
				else
				{
					stack.setCount(1);
					this.addStackToPlayer(player, stack.copy());
					teep.removeStackFromSlot(0);
				}
			}
		}
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

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		TileEntityPedestal teep = new TileEntityPedestal();
		if (!watered)
			teep.offset = (this.getStateFromMeta(meta).getValue(FACING) == EnumFacing.UP) ? -0.5000D : 0.0625D;
		teep.setDisplay(ItemStack.EMPTY);
		return teep;
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		this.setDefaultFacing(worldIn, pos, state);
	}

	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote && !watered)
		{
			EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);

			worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
		}
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		if (!watered)
			return this.getDefaultState().withProperty(FACING, (placer.rotationPitch < 0) ? EnumFacing.UP : EnumFacing.DOWN);
		return this.getDefaultState();
	}

	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (!watered)
			worldIn.setBlockState(pos, state.withProperty(FACING, (placer.rotationPitch < 0) ? EnumFacing.UP : EnumFacing.DOWN), 2);
	}

	public IBlockState getStateFromMeta(int meta)
	{
		if (!watered)
		{
			EnumFacing enumfacing = EnumFacing.getFront(meta);

			if (enumfacing.getAxis() == EnumFacing.Axis.X || enumfacing.getAxis() == EnumFacing.Axis.Z)
			{
				enumfacing = EnumFacing.UP;
			}
			return this.getDefaultState().withProperty(FACING, enumfacing);
		}

		return this.getDefaultState();
	}

	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing) state.getValue(FACING)).getIndex();
	}

	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
	}

	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
	}

	public int damageDropped(IBlockState state)
	{
		return 0;
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.SOLID;
	}
}
