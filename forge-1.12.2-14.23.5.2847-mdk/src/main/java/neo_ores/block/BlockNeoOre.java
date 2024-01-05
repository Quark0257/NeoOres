package neo_ores.block;

import java.util.Random;

import javax.annotation.Nullable;

import neo_ores.item.ItemBlockNeoOre;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.Reference;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockNeoOre extends NeoOresBlock
{
	public static final PropertyEnum<DimensionName> DIM = PropertyEnum.<DimensionName>create("dimension", DimensionName.class);
	private boolean dropSelf;
	private int dropMin = 0;
	private Item dropItem = null;
	private int dropMax = 0;
	private int expMin = 0;
	private int expMax = 0;
	private int dropDamage = 0;
	private DimensionName dimension = null;

	public BlockNeoOre(String registername, int harvestLevel, @Nullable DimensionName dimension, float light, boolean dropSelf, @Nullable Item dropItem, int damage, int min, int max, int dropExpMin,
			int dropExpMax)
	{
		super(Material.ROCK);
		this.setHardness(3.0F);
		this.setLightLevel(light);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.STONE);
		this.setHarvestLevel("pickaxe", harvestLevel);
		this.setDefaultState(this.blockState.getBaseState().withProperty(DIM, DimensionName.EARTH));

		if (dimension != null)
		{
			this.setDefaultState(this.blockState.getBaseState().withProperty(DIM, dimension));
			this.dimension = dimension;
		}

		this.setUnlocalizedName(registername);
		this.setRegistryName(Reference.MOD_ID, registername);

		this.dropSelf = dropSelf;
		this.dropMin = min;
		this.dropMax = max;
		if (dropItem != null)
		{
			this.dropDamage = damage;
			this.dropItem = dropItem;
			this.expMax = dropExpMax;
			this.expMin = dropExpMin;
		}
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return this.dropSelf ? this.getSilkTouchDrop(state).getItem() : this.dropItem;
	}

	public int quantityDropped(Random random)
	{
		return this.dropMin < this.dropMax ? this.dropMin + random.nextInt(this.dropMax - this.dropMin + 1) : this.dropMin;
	}

	public int quantityDroppedWithBonus(int fortune, Random random)
	{
		if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped((IBlockState) this.getBlockState().getValidStates().iterator().next(), random, fortune))
		{
			int i = random.nextInt(fortune + 2) - 1;

			if (i < 0)
			{
				i = 0;
			}

			return this.quantityDropped(random) * (i + 1);
		}
		else
		{
			return this.quantityDropped(random);
		}
	}

	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
	{
		Random rand = world instanceof World ? ((World) world).rand : new Random();
		if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this))
		{
			return MathHelper.getInt(rand, this.expMin, this.expMax);
		}
		return 0;
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		if ((new ItemStack(this)).getItem() == Item.getItemFromBlock(NeoOresBlocks.custom_lit_redstone_ore))
		{
			return new ItemStack(NeoOresBlocks.custom_redstone_ore, 1, this.getMetaFromState(worldIn.getBlockState(pos)));
		}
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(worldIn.getBlockState(pos)));
	}

	protected ItemStack getSilkTouchDrop(IBlockState state)
	{
		if (state.getBlock() == NeoOresBlocks.custom_lit_redstone_ore)
			return new ItemStack(NeoOresBlocks.custom_redstone_ore, 1, this.getMetaFromState(state));
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(state));
	}

	public int tickRate(World worldIn)
	{
		return 30;
	}

	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
	{
		this.activate(worldIn, pos);
		super.onBlockClicked(worldIn, pos, playerIn);
	}

	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
	{
		this.activate(worldIn, pos);
		super.onEntityWalk(worldIn, pos, entityIn);
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		this.activate(worldIn, pos);
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	private void activate(World worldIn, BlockPos pos)
	{
		if (this.isRedstoneOre())
		{
			this.spawnParticles(worldIn, pos);
		}

		if (this == NeoOresBlocks.custom_redstone_ore)
			worldIn.setBlockState(pos, NeoOresBlocks.custom_lit_redstone_ore.getDefaultState().withProperty(DIM, worldIn.getBlockState(pos).getValue(DIM)));
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (state.getBlock() == NeoOresBlocks.custom_lit_redstone_ore)
			worldIn.setBlockState(pos, NeoOresBlocks.custom_redstone_ore.getDefaultState().withProperty(DIM, worldIn.getBlockState(pos).getValue(DIM)));
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (this.isLitRedstoneOre())
		{
			this.spawnParticles(worldIn, pos);
		}
	}

	private void spawnParticles(World worldIn, BlockPos pos)
	{
		Random random = worldIn.rand;

		for (int i = 0; i < 6; ++i)
		{
			double d1 = (double) ((float) pos.getX() + random.nextFloat());
			double d2 = (double) ((float) pos.getY() + random.nextFloat());
			double d3 = (double) ((float) pos.getZ() + random.nextFloat());

			if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube())
			{
				d2 = (double) pos.getY() + 0.0625D + 1.0D;
			}

			if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube())
			{
				d2 = (double) pos.getY() - 0.0625D;
			}

			if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube())
			{
				d3 = (double) pos.getZ() + 0.0625D + 1.0D;
			}

			if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube())
			{
				d3 = (double) pos.getZ() - 0.0625D;
			}

			if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube())
			{
				d1 = (double) pos.getX() + 0.0625D + 1.0D;
			}

			if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube())
			{
				d1 = (double) pos.getX() - 0.0625D;
			}

			if (d1 < (double) pos.getX() || d1 > (double) (pos.getX() + 1) || d2 < 0.0D || d2 > (double) (pos.getY() + 1) || d3 < (double) pos.getZ() || d3 > (double) (pos.getZ() + 1))
			{
				worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	private boolean isLitRedstoneOre()
	{
		return this == NeoOresBlocks.custom_lit_redstone_ore;
	}

	private boolean isRedstoneOre()
	{
		return this == NeoOresBlocks.custom_redstone_ore;
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		if (!this.dropSelf)
		{
			return this.dropDamage;
		}
		return this.getSilkTouchDrop(state).getMetadata();
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return this.dimension != null ? 0 : ((DimensionName) state.getValue(DIM)).getMeta();
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)));
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.dimension != null ? this.getDefaultState().withProperty(DIM, this.dimension) : this.getDefaultState().withProperty(DIM, DimensionName.getFromMeta(meta));
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> stacks)
	{
		if (this.dimension != null)
		{
			stacks.add(new ItemStack(Item.getItemFromBlock(this), 1, 0));
		}
		else
		{
			for (DimensionName name : DimensionName.values())
			{
				stacks.add(new ItemStack(Item.getItemFromBlock(this), 1, name.getMeta()));
			}
		}
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { DIM });
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return dimension != null ? "tile." + this.getRegistryName().getResourcePath()
				: "tile." + DimensionName.getFromMeta(stack.getItemDamage()).getName() + "_" + this.getRegistryName().getResourcePath();
	}

	public boolean getTickRandomly()
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	public ModelResourceLocation getModel(int meta)
	{
		return new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, DimensionName.getFromMeta(meta).getName() + "_" + this.getRegistryName().getResourcePath()), "inventory");
	}

	// 0~15 available
	public int getMaxMeta()
	{
		return 3;
	}

	public Item getItemBlock(Block block)
	{
		return new ItemBlockNeoOre(block).setRegistryName(block.getRegistryName());
	}
}
