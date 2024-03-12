package neo_ores.block;

import neo_ores.api.IChunkLoader;
import neo_ores.tileentity.TileEntityMechanicalMagician;
import neo_ores.util.NeoOresServer;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMechanicalMagician extends NeoOresBlock implements ITileEntityProvider, IWrenchUsed, IChunkLoader
{
	public BlockMechanicalMagician()
	{
		super(Material.IRON);
		this.setHardness(5.0F);
		this.setResistance(Float.MAX_VALUE);
		this.setSoundType(SoundType.METAL);
		this.setHarvestLevel("pickaxe", 2);
	}
	
	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();
		return (block == this) ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	public boolean isFullCube(IBlockState iblockstate)
	{
		return false;
	}

	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		TileEntityMechanicalMagician temm = new TileEntityMechanicalMagician();
		return temm;
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		boolean flag = worldIn.isBlockPowered(pos);
		if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityMechanicalMagician)
		{
			TileEntityMechanicalMagician temm = (TileEntityMechanicalMagician) worldIn.getTileEntity(pos);
			temm.setRedstone(flag);
		}
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		else
		{
			// playerIn.openGui(NeoOres.instance, NeoOres.guiIDManaFurnace, worldIn,
			// pos.getX(), pos.getY(), pos.getZ());

			return true;
		}
	}

	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileEntityMechanicalMagician)
		{
			TileEntityMechanicalMagician temm = (TileEntityMechanicalMagician) te;
			temm.update();
			temm.setDestination(pos.add(0, 1, 0));
		}
		
		if(!worldIn.isRemote) {
			NeoOresServer.instance.addLoadingChunk(worldIn, pos);
		}
	}

	@Override
	public void wrench(World worldIn, BlockPos pos, ItemStack stack)
	{
		if (stack.getTagCompound() == null)
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		/*
		if(stack.getTagCompound().hasKey("nextSetPos")) {
			NBTTagCompound nextPos = stack.getTagCompound().getCompoundTag("nextSetPos");
			BlockPos nextSetPos = new BlockPos(nextPos.getInteger("x"),nextPos.getInteger("y"),nextPos.getInteger("z"));
			if(nextSetPos.equals(pos)) return;
			TileEntity te = worldIn.getTileEntity(nextSetPos);
			if(te instanceof TileEntityMechanicalMagician) {
				TileEntityMechanicalMagician temm = (TileEntityMechanicalMagician) te;
				temm.setDestination(nextSetPos);
			}
		}
		*/
		if(!stack.getTagCompound().hasKey("nextSetPos")) {
			NBTTagCompound nextPos = new NBTTagCompound();
			nextPos.setInteger("x", pos.getX());
			nextPos.setInteger("y", pos.getY());
			nextPos.setInteger("z", pos.getZ());
			stack.getTagCompound().setTag("nextSetPos", nextPos);
		}
	}

	@Override
	public boolean isLoadable()
	{
		return true;
	}
}
