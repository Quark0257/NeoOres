package neo_ores.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class RayTraceUtils
{
	public static RayTraceResult getSimpleResult(double x, double y, double z) {
		BlockPos pos = new BlockPos(x, y, z);
		Vec3d vec = new Vec3d(x, y, z);
		return new RayTraceResult(vec, EnumFacing.UP, pos);
	}
}
