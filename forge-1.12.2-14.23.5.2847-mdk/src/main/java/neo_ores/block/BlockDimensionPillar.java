package neo_ores.block;

import java.util.Random;

import neo_ores.main.Reference;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockDimensionPillar extends BlockDimension implements INeoOresBlock
{
	public static final PropertyEnum<BlockDimensionPillar.EnumAxis> LOG_AXIS = PropertyEnum.<BlockDimensionPillar.EnumAxis>create(
			"axis", BlockDimensionPillar.EnumAxis.class);

	public BlockDimensionPillar(String registername, Material materialIn, float hardness, float resistant,
			String harvest_key, int harvest_level, float light, SoundType sound)
	{
		super(registername, materialIn, hardness, resistant, harvest_key, harvest_level, light, sound);
		this.setDefaultState(this.blockState.getBaseState().withProperty(DIM, DimensionName.EARTH)
				.withProperty(LOG_AXIS, EnumAxis.Y));
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(this);
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(worldIn.getBlockState(pos)) % 4);
	}

	protected ItemStack getSilkTouchDrop(IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(state) % 4);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return this.getSilkTouchDrop(state).getMetadata() % 4;
	}

	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState iblockstate = this.getDefaultState().withProperty(DIM, DimensionName.getFromMeta((meta & 3) % 4));

		switch (meta & 12)
		{
			case 0:
				iblockstate = iblockstate.withProperty(LOG_AXIS, BlockDimensionPillar.EnumAxis.Y);
				break;
			case 4:
				iblockstate = iblockstate.withProperty(LOG_AXIS, BlockDimensionPillar.EnumAxis.X);
				break;
			case 8:
				iblockstate = iblockstate.withProperty(LOG_AXIS, BlockDimensionPillar.EnumAxis.Z);
				break;
		}

		return iblockstate;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@SuppressWarnings("incomplete-switch")
	public int getMetaFromState(IBlockState state)
	{
		int i = 0;
		i = i | (state.getValue(DIM)).getMeta();

		switch ((BlockDimensionPillar.EnumAxis) state.getValue(LOG_AXIS))
		{
			case X:
				i |= 4;
				break;
			case Z:
				i |= 8;
				break;
		}

		return i;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)) % 4);
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> stacks)
	{
		for (DimensionName name : DimensionName.values())
		{
			stacks.add(new ItemStack(Item.getItemFromBlock(this), 1, name.getMeta()));
		}
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { DIM, LOG_AXIS });
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return "tile." + DimensionName.getFromMeta(stack.getItemDamage()).getName() + "_"
				+ this.getRegistryName().getResourcePath();
	}

	public int getMaxMeta()
	{
		return 3;
	}

	public ModelResourceLocation getModel(int meta)
	{
		return new ModelResourceLocation(
				new ResourceLocation(Reference.MOD_ID,
						DimensionName.getFromMeta(meta % 4).getName() + "_" + this.getRegistryName().getResourcePath()),
				"inventory");
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
	{
		IBlockState state = world.getBlockState(pos);
		for (IProperty<?> prop : state.getProperties().keySet())
		{
			if (prop.getName().equals("axis"))
			{
				world.setBlockState(pos, state.cycleProperty(prop));
				return true;
			}
		}
		return false;
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getStateFromMeta(meta).withProperty(LOG_AXIS,
				BlockDimensionPillar.EnumAxis.fromFacingAxis(facing.getAxis()));
	}

	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		switch (rot)
		{
			case COUNTERCLOCKWISE_90:
			case CLOCKWISE_90:

				switch ((BlockDimensionPillar.EnumAxis) state.getValue(LOG_AXIS))
				{
					case X:
						return state.withProperty(LOG_AXIS, BlockDimensionPillar.EnumAxis.Z);
					case Z:
						return state.withProperty(LOG_AXIS, BlockDimensionPillar.EnumAxis.X);
					default:
						return state;
				}

			default:
				return state;
		}
	}

	public static enum EnumAxis implements IStringSerializable
	{
		X("x"), Y("y"), Z("z");

		private final String name;

		private EnumAxis(String name)
		{
			this.name = name;
		}

		public String toString()
		{
			return this.name;
		}

		public static BlockDimensionPillar.EnumAxis fromFacingAxis(EnumFacing.Axis axis)
		{
			switch (axis)
			{
				case X:
					return X;
				case Y:
					return Y;
				case Z:
					return Z;
				default:
					return Y;
			}
		}

		public String getName()
		{
			return this.name;
		}
	}
}
