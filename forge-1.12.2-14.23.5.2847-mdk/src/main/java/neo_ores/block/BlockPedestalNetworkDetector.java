package neo_ores.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import neo_ores.main.NeoOresBlocks;
import neo_ores.main.Reference;
import neo_ores.tileentity.TileEntityPedestalNetworkDetector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPedestalNetworkDetector extends BlockContainer implements INeoOresBlock, IPedestalInterfaceComponent, IAcceptCreativeLeftClick
{
	protected static final AxisAlignedBB AABB_BOUNDING = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
	protected static final AxisAlignedBB AABB_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 0.125D);
	protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 0.8125D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 0.8125D, 1.0D);

	protected static final AxisAlignedBB AABB_BOUNDING2 = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D);
	protected static final AxisAlignedBB AABB_BOUNDING3 = new AxisAlignedBB(0.0D, 0.375D, 0.0D, 1.0D, 1.0D, 1.0D);

	public BlockPedestalNetworkDetector()
	{
		super(Material.ANVIL);
		this.setSoundType(SoundType.METAL);
		this.setHardness(5.0F);
		this.setHarvestLevel("pickaxe", 0);
		this.setResistance(Float.MAX_VALUE);
	}

	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
	{
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BOUNDING2);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB_BOUNDING2;
	}

	public boolean isFullCube(IBlockState iblockstate)
	{
		return false;
	}

	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		boolean flag = worldIn.isBlockPowered(pos);
		if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityPedestalNetworkDetector)
		{
			TileEntityPedestalNetworkDetector temm = (TileEntityPedestalNetworkDetector) worldIn.getTileEntity(pos);
			temm.setRedstone(flag);
		}
	}
	
	public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityPedestalNetworkDetector)
		{
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(worldIn, pos, state);
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
			return true;
		ItemStack itemstack = playerIn.getHeldItem(hand);

		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityPedestalNetworkDetector)
		{
			TileEntityPedestalNetworkDetector teep = (TileEntityPedestalNetworkDetector) tileentity;
			if (itemstack.isEmpty())
			{
				return false;
			}
			else
			{
				if (!playerIn.isSneaking() && teep.getStackInSlot(0).isEmpty())
				{
					teep.setItemStack(itemstack);
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
		if (tileentity instanceof TileEntityPedestalNetworkDetector)
		{
			TileEntityPedestalNetworkDetector teep = (TileEntityPedestalNetworkDetector) tileentity;
			if (player.isSneaking())
			{
				teep.clearItemStack();
			}
		}
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		TileEntityPedestalNetworkDetector teep = new TileEntityPedestalNetworkDetector();
		teep.setDisplay(ItemStack.EMPTY);
		return teep;
	}
	
	public static void setState(boolean active, World worldIn, BlockPos pos)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (active)
		{
			worldIn.setBlockState(pos, NeoOresBlocks.lit_detector_pedestal.getDefaultState(), 3);
		}
		else
		{
			worldIn.setBlockState(pos, NeoOresBlocks.detector_pedestal.getDefaultState(), 3);
		}

		if (tileentity != null)
		{
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}

	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}

	public int damageDropped(IBlockState state)
	{
		return 0;
	}

	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return face == EnumFacing.DOWN ? BlockFaceShape.CENTER_BIG : BlockFaceShape.UNDEFINED;
	}

    public ModelResourceLocation getModel(int meta)
	{
		return new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, this.getRegistryName().getResourcePath()), "inventory");
	}

	// 0~15 available
	public int getMaxMeta()
	{
		return 1;
	}

	public Item getItemBlock(Block block)
	{
		return new ItemBlock(block).setRegistryName(block.getRegistryName());
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return "tile." + this.getRegistryName().getResourcePath();
	}

	@Override
	public boolean isContent()
	{
		return true;
	}
	
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(NeoOresBlocks.detector_pedestal);
	}
	
	protected ItemStack getSilkTouchDrop(IBlockState state)
	{
		return new ItemStack(NeoOresBlocks.detector_pedestal);
	}
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(NeoOresBlocks.detector_pedestal);
	}
	
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}

	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return blockState.getBlock() == NeoOresBlocks.lit_detector_pedestal ? 15 : 0;
	}
}
