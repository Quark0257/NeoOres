package neo_ores.block;

import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import neo_ores.client.particle.TexturedParticle;
import neo_ores.event.NeoOresClientEvents;
import neo_ores.main.NeoOres;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialTransparent;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLight extends NeoOresBlock
{
	private static final AxisAlignedBB LIGHT_AABB = new AxisAlignedBB(0.4D, 0.4D, 0.4D, 0.6D, 0.6D, 0.6D);
	private static final UUID PARTICLE_ID = UUID.fromString("e3d2c6e9-b431-40d3-96f2-c9951f46a883");
	public static final Material LIGHT = new MaterialTransparent(MapColor.AIR) {
		public boolean isLiquid()
	    {
	        return true;
	    }
	};

	public BlockLight()
	{
		super(LIGHT);
		this.setHardness(0.0F);
		this.setResistance(10.0F);
		this.setSoundType(SoundType.STONE);
		this.setLightLevel(1.0F);
		this.setHarvestLevel("pickaxe", 0);
	}

	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return LIGHT_AABB;
	}

	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public float getAmbientOcclusionLightValue(IBlockState state)
	{
		return 1.0F;
	}

	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
	{
	}

	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		return EnumPushReaction.DESTROY;
	}

	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		int color = 0x8B91DB;
		NeoOresClientEvents.getInstance().addParticle(
				new TexturedParticle(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0, 0.0, 0.0, 30, 10.0F, NeoOres.PARTICLE_MAGIC).setColor(color, 1.0F).setUUID(PARTICLE_ID).setPos(pos));
	}

	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
	{
		NeoOresClientEvents.getInstance().removeParticle(new Predicate<TexturedParticle>()
		{
			@Override
			public boolean test(TexturedParticle particle)
			{
				return particle.getUUID().equals(PARTICLE_ID) && particle.getPos().equals(pos);
			}
		});
		int color = 0x8B91DB;
		double x = pos.getX() + 0.5;
		double y = pos.getY() + 0.5;
		double z = pos.getZ() + 0.5;
		for (int i = 0; i < 8 + world.rand.nextInt(4); i++)
		{
			int time = 5;
			double d = 0.2;
			double vx = d * (world.rand.nextDouble() - 0.5);
			double vy = d * (world.rand.nextDouble() - 0.5);
			double vz = d * (world.rand.nextDouble() - 0.5);
			NeoOresClientEvents.getInstance()
					.addParticle(new TexturedParticle(x + vx, y + vy, z + vz, vx / time, vy / time, vz / time, time, 1.0F + world.rand.nextFloat(), NeoOres.PARTICLE_MAGIC).setColor(color, 1.0F));
		}
		return true;
	}
}
