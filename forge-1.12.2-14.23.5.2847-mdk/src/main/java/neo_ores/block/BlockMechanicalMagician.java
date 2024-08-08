package neo_ores.block;

import neo_ores.api.IChunkLoader;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresBlocks;
import neo_ores.tileentity.TileEntityMechanicalMagician;
import neo_ores.util.ManaTank;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
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
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityMechanicalMagician)
		{
			TileEntityMechanicalMagician temm = (TileEntityMechanicalMagician) te;
			ManaTank tank = temm.getTank();
			ItemStack stack = playerIn.getHeldItem(hand);
			if (stack.getItem() == Items.BUCKET)
			{
				if (tank.getFluidAmount() >= 1000)
				{
					temm.markDirty();
					if (playerIn.capabilities.isCreativeMode)
					{
						tank.drain(1000, true);
						return true;
					}

					tank.drain(1000, true);
					stack.shrink(1);
					ItemStack filled = FluidUtil.getFilledBucket(new FluidStack(NeoOresBlocks.fluid_mana, 1000));
					if (stack.isEmpty())
					{
						playerIn.setHeldItem(hand, filled);
						return true;
					}

					if (!playerIn.inventory.addItemStackToInventory(filled))
						playerIn.dropItem(filled, false);
				}
				return true;
			}
			else if (stack.getItem() == ForgeModContainer.getInstance().universalBucket)
			{
				if (FluidUtil.getFluidContained(stack).getFluid() == NeoOresBlocks.fluid_mana)
				{
					temm.markDirty();
					if (tank.fill(new FluidStack(NeoOresBlocks.fluid_mana, 1000), false) == 1000)
					{
						if (playerIn.capabilities.isCreativeMode)
						{
							tank.fill(new FluidStack(NeoOresBlocks.fluid_mana, 1000), true);
							return true;
						}

						tank.fill(new FluidStack(NeoOresBlocks.fluid_mana, 1000), true);
						stack.shrink(1);
						if (stack.isEmpty())
						{
							playerIn.setHeldItem(hand, new ItemStack(Items.BUCKET));;
							return true;
						}

						ItemStack filled = new ItemStack(Items.BUCKET);
						if (!playerIn.inventory.addItemStackToInventory(filled))
							playerIn.dropItem(filled, false);
					}
					return true;
				}
			}

		}

		if (worldIn.isRemote)
		{
			return true;
		}
		else
		{
			playerIn.openGui(NeoOres.instance, NeoOres.guiIDMM, worldIn, pos.getX(), pos.getY(), pos.getZ());

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
	}

	@Override
	public void wrench(World worldIn, BlockPos pos, ItemStack stack)
	{
		if (stack.getTagCompound() == null)
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		
		if (!stack.getTagCompound().hasKey("nextSetPos"))
		{
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
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityMechanicalMagician)
		{
			InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityMechanicalMagician) tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(worldIn, pos, state);
	}
}
