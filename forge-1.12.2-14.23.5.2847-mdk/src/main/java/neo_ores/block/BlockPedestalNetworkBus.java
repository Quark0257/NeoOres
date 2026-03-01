package neo_ores.block;

import javax.annotation.Nullable;

import neo_ores.main.NeoOresItems;
import neo_ores.tileentity.TileEntityPedestalNetworkBus;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPedestalNetworkBus extends NeoOresBlock implements ITileEntityProvider, IPedestalInterfaceComponent
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockPedestalNetworkBus()
	{
		super(Material.ANVIL);
		this.setSoundType(SoundType.METAL);
		this.setHardness(5.0F);
		this.setHarvestLevel("pickaxe", 0);
		this.setResistance(Float.MAX_VALUE);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityPedestalNetworkBus();
	}
	
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	
	public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite()), 2);
    }
    
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite());
    }
    
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @Nullable
    public static EnumFacing getFacing(int meta)
    {
        int i = meta & 7;
        return i > 5 ? null : EnumFacing.getFront(i);
    }
    
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, getFacing(meta));
    }
    
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getIndex();

        return i;
    }
    
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }
    
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        return super.rotateBlock(world, pos, axis);
    }

	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.SOLID;
    }

	@Override
	public boolean isContent()
	{
		return true;
	}
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
			return true;
		ItemStack itemstack = playerIn.getHeldItem(hand);

		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityPedestalNetworkBus)
		{
			TileEntityPedestalNetworkBus tepnb = (TileEntityPedestalNetworkBus) tileentity;
			if (itemstack.getItem() == NeoOresItems.mana_wrench)
			{
				tepnb.changePriorityMode();
				return true;
			}
		}
		return false;
	}
	
	public int getPowerOnSide(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (this.isAlternateInput(iblockstate))
        {
            if (block == Blocks.REDSTONE_BLOCK)
            {
                return 15;
            }
            else
            {
                return block == Blocks.REDSTONE_WIRE ? ((Integer)iblockstate.getValue(BlockRedstoneWire.POWER)).intValue() : worldIn.getStrongPower(pos, side);
            }
        }
        else
        {
            return 0;
        }
    }
	
	protected boolean isAlternateInput(IBlockState state)
    {
        return state.canProvidePower();
    }
}
