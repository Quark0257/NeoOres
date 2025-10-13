package neo_ores.api.guide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;

public abstract class StructureWorldRenderer implements IBlockAccess
{
	private final Map<BlockPos, BlockInfo> blocks;
	private final AxisAlignedBB boundingBox;
	private int slice;

	public StructureWorldRenderer(List<BlockInfo> blocks)
	{
		this.blocks = new HashMap<>();
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int minZ = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int maxZ = Integer.MIN_VALUE;
		for (BlockInfo info : blocks)
		{
			minX = Math.min(info.pos.getX(), minX);
			minY = Math.min(info.pos.getY(), minY);
			minZ = Math.min(info.pos.getZ(), minZ);
			maxX = Math.max(info.pos.getX(), maxX);
			maxY = Math.max(info.pos.getY(), maxY);
			maxZ = Math.max(info.pos.getZ(), maxZ);
		}
		this.boundingBox = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
		for (BlockInfo info : blocks)
		{
			BlockPos newPos = info.pos.subtract(new Vec3i((minX + maxX + 1) / 2, (minY + maxY + 1) / 2, (minZ + maxZ + 1) / 2));
			BlockInfo newInfo = new BlockInfo(newPos, info.blockState, info.tileentityData);
			this.blocks.put(newPos, newInfo);
		}
		this.slice = 0;
	}

	public Map<BlockPos, BlockInfo> getBlockMap()
	{
		return this.blocks;
	}

	public AxisAlignedBB getBoundingBox()
	{
		return this.boundingBox;
	}

	@Override
	public TileEntity getTileEntity(BlockPos pos)
	{
		return null;
	}

	@Override
	public int getCombinedLight(BlockPos pos, int lightValue)
	{
		return 0;
	}

	@Override
	public IBlockState getBlockState(BlockPos pos)
	{
		if (this.slice > 0)
		{
			if (pos.getY() != this.getLayer())
			{
				return Blocks.AIR.getDefaultState();
			}
		}
		if (this.blocks.containsKey(pos))
		{
			return this.blocks.get(pos).blockState;
		}
		return Blocks.AIR.getDefaultState();
	}

	public boolean hasSlice()
	{
		return this.slice != 0;
	}

	public int getLayer()
	{
		return -(int) (this.boundingBox.minY + this.boundingBox.maxY + 1) / 2 - 1 + this.slice;
	}

	@Override
	public boolean isAirBlock(BlockPos pos)
	{
		return this.getBlockState(pos).getBlock() == Blocks.AIR;
	}

	@Override
	public Biome getBiome(BlockPos pos)
	{
		return Biomes.PLAINS;
	}

	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction)
	{
		return 0;
	}

	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default)
	{
		return this.getBlockState(pos).isSideSolid(this, pos, side);
	}

	public void setSlice(int slice)
	{
		this.slice = slice;
	}
}
