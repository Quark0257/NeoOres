package neo_ores.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import neo_ores.api.InventoryUtils;
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
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockEnhancedPedestal extends NeoOresBlock implements ITileEntityProvider, IPedestalInterfaceComponent, IAcceptCreativeLeftClick
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
		this.hasTileEntity = true;
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
			ItemStack stack = new ItemStack(this.getItemDropped(state, RANDOM, 0), 1, this.getMetaFromState(state));
			if (!stack.hasTagCompound())
			{
				stack.setTagCompound(new NBTTagCompound());
			}
			NBTTagCompound tileData = tileentity.serializeNBT();
			tileData.removeTag("x");
			tileData.removeTag("y");
			tileData.removeTag("z");
			stack.getTagCompound().setTag("BlockEntityTag", tileData);
			EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
			worldIn.spawnEntity(entityItem);
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(worldIn, pos, state);
	}

	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}

	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
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
					teep.markDirty();
					return true;
				}
				else if (playerIn.isSneaking() && item == NeoOresItems.mana_wrench)
				{
					teep.addSlot(-1);
					teep.markDirty();
					return true;
				}
				else if (item == NeoOresItems.spell)
				{
					return false;
				}
				else if (!playerIn.isSneaking())
				{
					ItemStack stack = InventoryUtils.addInventoryFromStack(itemstack, teep, facing);
					playerIn.setHeldItem(hand, stack);
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

			IItemHandler handler = teep.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
			if (handler == null) 
			{
				return;
			}
			ItemStack stack = handler.getStackInSlot(slot);
			if (player.isSneaking())
			{
				InventoryUtils.addStackToPlayer(player, handler.extractItem(slot, Math.min(stack.getCount(), stack.getMaxStackSize()), false));
			}
			else
			{
				InventoryUtils.addStackToPlayer(player, handler.extractItem(slot, 1, false));
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

	@Override
	public int damageDropped(IBlockState state)
	{
		return this.getMetaFromState(state);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((PedestalTiers) state.getValue(TIER)).getMeta();
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(TIER, PedestalTiers.getFromMeta(meta));
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(state));
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

	// 0~15 available
	public int getMaxMeta()
	{
		return 15;
	}

	public Item getItemBlock(Block block)
	{
		return new ItemBlockEnhancedPedestal(block).setRegistryName(block.getRegistryName());
	}
	
	public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
    	if (worldIn.getTileEntity(pos) == null || !(worldIn.getTileEntity(pos) instanceof TileEntityEnhancedPedestal)) 
    	{
    		return 0;
    	}
        return TileEntityEnhancedPedestal.calcRedstoneFromInventory((TileEntityEnhancedPedestal) worldIn.getTileEntity(pos));
    }

	@Override
	public boolean isContent()
	{
		return true;
	}
	
	public boolean isTopSolid(IBlockState state) 
	{
		return false;
	}
	
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return face == EnumFacing.DOWN ? BlockFaceShape.CENTER_BIG : BlockFaceShape.UNDEFINED;
	}
}
