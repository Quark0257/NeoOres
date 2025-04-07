package neo_ores.util;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;

public class RayTraceUtils
{
	public static RayTraceResult getSimpleResult(double x, double y, double z)
	{
		BlockPos pos = new BlockPos(x, y, z);
		Vec3d vec = new Vec3d(x, y, z);
		return new RayTraceResult(vec, EnumFacing.UP, pos);
	}

	public static RayTraceResult getSimpleResult(double x, double y, double z, EnumFacing facing)
	{
		BlockPos pos = new BlockPos(x, y, z);
		Vec3d vec = new Vec3d(x, y, z);
		return new RayTraceResult(vec, facing, pos);
	}

	public static RayTraceResult getSimpleResult(BlockPos pos, @Nullable EnumFacing offset)
	{
		BlockPos tempPos = pos;
		if (offset != null)
		{
			tempPos = pos.add(offset.getDirectionVec());
		}
		EnumFacing face = offset != null ? offset.getOpposite() : EnumFacing.UP;
		Vec3d vec = new Vec3d(tempPos.getX() + 0.5D, (face == EnumFacing.UP ? 1.0D : 0.0D) + tempPos.getY(), tempPos.getZ() + 0.5D);
		return new RayTraceResult(vec, face, pos);
	}

	public static RayTraceResult getSimpleResult(Entity entity)
	{
		if (entity != null)
		{
			return new RayTraceResult(entity);
		}
		return null;
	}

	public static RayTraceResult getMissResult()
	{
		return new RayTraceResult(Type.MISS, Vec3d.ZERO, EnumFacing.DOWN, BlockPos.ORIGIN);
	}
}
