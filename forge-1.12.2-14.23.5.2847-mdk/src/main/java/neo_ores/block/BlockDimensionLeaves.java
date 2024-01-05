package neo_ores.block;

import java.util.Random;

import javax.annotation.Nullable;

import neo_ores.item.ItemBlockDimension;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.Reference;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDimensionLeaves extends Block implements INeoOresBlock, IShearable
{

	public static final PropertyBool DECAYABLE = PropertyBool.create("decayable");
	public static final PropertyBool CHECK_DECAY = PropertyBool.create("check_decay");
	int[] surroundings;

	public BlockDimensionLeaves(Material materialIn, String registername)
	{
		super(materialIn);
		this.setTickRandomly(true);
		this.setHardness(0.2F);
		this.setLightOpacity(1);
		this.setResistance(0.0F);
		this.setSoundType(SoundType.PLANT);
		this.setUnlocalizedName(registername);
		this.setRegistryName(Reference.MOD_ID, registername);
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(BlockDimension.DIM, DimensionName.EARTH).withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
	}

	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		if (world.getBlockState(pos).getValue(BlockDimension.DIM) == DimensionName.FIRE || world.getBlockState(pos).getValue(BlockDimension.DIM) == DimensionName.WATER)
			return 0;
		return 30;
	}

	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		if (world.getBlockState(pos).getValue(BlockDimension.DIM) == DimensionName.FIRE || world.getBlockState(pos).getValue(BlockDimension.DIM) == DimensionName.WATER)
			return 0;
		return 60;
	}

	public ModelResourceLocation getModel(int meta)
	{
		return new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, this.getRegistryName().getResourcePath()), "inventory");
	}

	@Override
	public int getMaxMeta()
	{
		return 3;
	}

	public Item getItemBlock(Block block)
	{
		return new ItemBlockDimension((BlockDimensionLeaves) block).setRegistryName(block.getRegistryName());
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		int k = pos.getX();
		int l = pos.getY();
		int i1 = pos.getZ();

		if (worldIn.isAreaLoaded(new BlockPos(k - 2, l - 2, i1 - 2), new BlockPos(k + 2, l + 2, i1 + 2)))
		{
			for (int j1 = -1; j1 <= 1; ++j1)
			{
				for (int k1 = -1; k1 <= 1; ++k1)
				{
					for (int l1 = -1; l1 <= 1; ++l1)
					{
						BlockPos blockpos = pos.add(j1, k1, l1);
						IBlockState iblockstate = worldIn.getBlockState(blockpos);

						if (iblockstate.getBlock().isLeaves(iblockstate, worldIn, blockpos))
						{
							iblockstate.getBlock().beginLeavesDecay(iblockstate, worldIn, blockpos);
						}
					}
				}
			}
		}
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			if (((Boolean) state.getValue(CHECK_DECAY)).booleanValue() && ((Boolean) state.getValue(DECAYABLE)).booleanValue())
			{
				int k = pos.getX();
				int l = pos.getY();
				int i1 = pos.getZ();

				if (this.surroundings == null)
				{
					this.surroundings = new int[32768];
				}

				if (!worldIn.isAreaLoaded(pos, 1))
					return; // Forge: prevent decaying leaves from updating neighbors and loading unloaded
							// chunks
				if (worldIn.isAreaLoaded(pos, 6)) // Forge: extend range from 5 to 6 to account for neighbor checks in
													// world.markAndNotifyBlock -> world.updateObservingBlocksAt
				{
					BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

					for (int i2 = -4; i2 <= 4; ++i2)
					{
						for (int j2 = -4; j2 <= 4; ++j2)
						{
							for (int k2 = -4; k2 <= 4; ++k2)
							{
								IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos.setPos(k + i2, l + j2, i1 + k2));
								Block block = iblockstate.getBlock();

								if (!block.canSustainLeaves(iblockstate, worldIn, blockpos$mutableblockpos.setPos(k + i2, l + j2, i1 + k2)))
								{
									if (block.isLeaves(iblockstate, worldIn, blockpos$mutableblockpos.setPos(k + i2, l + j2, i1 + k2)))
									{
										this.surroundings[(i2 + 16) * 1024 + (j2 + 16) * 32 + k2 + 16] = -2;
									}
									else
									{
										this.surroundings[(i2 + 16) * 1024 + (j2 + 16) * 32 + k2 + 16] = -1;
									}
								}
								else
								{
									this.surroundings[(i2 + 16) * 1024 + (j2 + 16) * 32 + k2 + 16] = 0;
								}
							}
						}
					}

					for (int i3 = 1; i3 <= 4; ++i3)
					{
						for (int j3 = -4; j3 <= 4; ++j3)
						{
							for (int k3 = -4; k3 <= 4; ++k3)
							{
								for (int l3 = -4; l3 <= 4; ++l3)
								{
									if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + l3 + 16] == i3 - 1)
									{
										if (this.surroundings[(j3 + 16 - 1) * 1024 + (k3 + 16) * 32 + l3 + 16] == -2)
										{
											this.surroundings[(j3 + 16 - 1) * 1024 + (k3 + 16) * 32 + l3 + 16] = i3;
										}

										if (this.surroundings[(j3 + 16 + 1) * 1024 + (k3 + 16) * 32 + l3 + 16] == -2)
										{
											this.surroundings[(j3 + 16 + 1) * 1024 + (k3 + 16) * 32 + l3 + 16] = i3;
										}

										if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16 - 1) * 32 + l3 + 16] == -2)
										{
											this.surroundings[(j3 + 16) * 1024 + (k3 + 16 - 1) * 32 + l3 + 16] = i3;
										}

										if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16 + 1) * 32 + l3 + 16] == -2)
										{
											this.surroundings[(j3 + 16) * 1024 + (k3 + 16 + 1) * 32 + l3 + 16] = i3;
										}

										if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + (l3 + 16 - 1)] == -2)
										{
											this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + (l3 + 16 - 1)] = i3;
										}

										if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + l3 + 16 + 1] == -2)
										{
											this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + l3 + 16 + 1] = i3;
										}
									}
								}
							}
						}
					}
				}

				int l2 = this.surroundings[16912];

				if (l2 >= 0)
				{
					worldIn.setBlockState(pos, state.withProperty(CHECK_DECAY, Boolean.valueOf(false)), 4);
				}
				else
				{
					this.destroy(worldIn, pos);
				}
			}
		}
	}

	private void destroy(World worldIn, BlockPos pos)
	{
		this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
		worldIn.setBlockToAir(pos);
	}

	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (worldIn.isRainingAt(pos.up()) && !worldIn.getBlockState(pos.down()).isTopSolid() && rand.nextInt(15) == 1)
		{
			double d0 = (double) ((float) pos.getX() + rand.nextFloat());
			double d1 = (double) pos.getY() - 0.05D;
			double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
			worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	public int quantityDropped(Random random)
	{
		return random.nextInt(20) == 0 ? 1 : 0;
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		int i = 0;
		if (NeoOresBlocks.dim_leaves == state.getBlock())
			i++;
		if (NeoOresBlocks.corroding_dim_leaves == state.getBlock())
			i = i + 2;
		i = state.getValue(BlockDimension.DIM).getColorIndex() * 3 + i;
		for (int j = 0; j < NeoOresBlocks.color_saplings.size(); j++)
		{
			if (i == j)
				return Item.getItemFromBlock(NeoOresBlocks.color_saplings.get(j));
		}
		return Items.APPLE;
	}

	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
	{
		super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
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

	public boolean causesSuffocation(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public void beginLeavesDecay(IBlockState state, World world, BlockPos pos)
	{
		if (!(Boolean) state.getValue(CHECK_DECAY))
		{
			world.setBlockState(pos, state.withProperty(CHECK_DECAY, true), 4);
		}
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		Random rand = world instanceof World ? ((World) world).rand : new Random();
		int chance = this.getSaplingDropChance(state);

		if (fortune > 0)
		{
			chance -= 2 << fortune;
			if (chance < 10)
				chance = 10;
		}

		if (rand.nextInt(chance) == 0)
		{
			ItemStack drop = new ItemStack(getItemDropped(state, rand, fortune), 1, damageDropped(state));
			if (!drop.isEmpty())
				drops.add(drop);
		}

		chance = 200;
		if (fortune > 0)
		{
			chance -= 10 << fortune;
			if (chance < 40)
				chance = 40;
		}

		this.captureDrops(true);
		if (world instanceof World)
			this.dropApple((World) world, pos, state, chance); // Dammet mojang
		drops.addAll(this.captureDrops(false));
	}

	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance)
	{
	}

	protected int getSaplingDropChance(IBlockState state)
	{
		return 20;
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> stacks)
	{
		for (DimensionName name : DimensionName.values())
		{
			stacks.add(new ItemStack(Item.getItemFromBlock(this), 1, name.getMeta()));
		}
	}

	protected ItemStack getSilkTouchDrop(IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(BlockDimension.DIM).getMeta());
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(BlockDimension.DIM).getMeta());
	}

	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(BlockDimension.DIM, DimensionName.getFromMeta(meta % 4)).withProperty(DECAYABLE, Boolean.valueOf((meta & 4) == 0)).withProperty(CHECK_DECAY,
				Boolean.valueOf((meta & 8) > 0));
	}

	public int getMetaFromState(IBlockState state)
	{
		int i = 0;
		i = i | state.getValue(BlockDimension.DIM).getMeta();

		if (!((Boolean) state.getValue(DECAYABLE)).booleanValue())
		{
			i |= 4;
		}

		if (((Boolean) state.getValue(CHECK_DECAY)).booleanValue())
		{
			i |= 8;
		}

		return i;
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { BlockDimension.DIM, CHECK_DECAY, DECAYABLE });
	}

	public int damageDropped(IBlockState state)
	{
		return 0;
	}

	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
		{
			player.addStat(StatList.getBlockStats(this));
		}
		else
		{
			super.harvestBlock(worldIn, player, pos, state, te, stack);
		}
	}

	@Override
	public NonNullList<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		return NonNullList.withSize(1, new ItemStack(this, 1, world.getBlockState(pos).getValue(BlockDimension.DIM).getMeta()));
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return "tile." + DimensionName.getFromMeta(stack.getItemDamage()).getName() + "_" + this.getRegistryName().getResourcePath();
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getStateFromMeta(meta).withProperty(DECAYABLE, false).withProperty(CHECK_DECAY, false);
	}
}
