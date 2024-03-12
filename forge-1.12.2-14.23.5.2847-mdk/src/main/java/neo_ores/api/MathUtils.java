package neo_ores.api;

import java.util.Arrays;
import java.util.List;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class MathUtils
{
	public static Vec2f getYawPitch(double x, double y, double z)
	{
		double dh = Math.sqrt(x * x + z * z);
		return new Vec2f((float) (Math.toDegrees(Math.atan2(-x, z))), (float) (-Math.toDegrees(Math.atan2(y, dh))));
	}
	
	public static double copySign(double value) {
		if(value == 0.0) return 1.0;
		return value / Math.abs(value);
	}

	public static boolean isInteger(double value)
	{
		return value == (int) value;
	}

	/*
	 * 0 - 1 - 2 - 3 made 0 - 1 - 0 - (-1)
	 */
	public static int sin(int i)
	{
		return (int) Math.sin(Math.PI * (i % 4) / 2);
	}

	public static double getRad(Rotation rot)
	{
		switch (rot)
		{
			case CLOCKWISE_90:
				return Math.PI * 3.0 / 2.0;
			case CLOCKWISE_180:
				return Math.PI;
			case COUNTERCLOCKWISE_90:
				return Math.PI / 2.0;
			default:
				return 0.0;
		}
	}

	public static BlockPos rot(BlockPos value, Rotation rot)
	{
		return new BlockPos(value.getZ() * (int) Math.sin(getRad(rot)) + value.getX() * (int) Math.cos(getRad(rot)), value.getY(),
				value.getZ() * (int) Math.cos(getRad(rot)) - value.getX() * (int) Math.sin(getRad(rot)));
	}

	public static Vec3i rot(Vec3i value, Rotation rot)
	{
		return new BlockPos(value.getZ() * (int) Math.sin(getRad(rot)) + value.getX() * (int) Math.cos(getRad(rot)), value.getY(),
				value.getZ() * (int) Math.cos(getRad(rot)) - value.getX() * (int) Math.sin(getRad(rot)));
	}

	public static Vec3i add(Vec3i a, Vec3i b)
	{
		return new Vec3i(a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ());
	}

	public static class Surface
	{
		private Vec3d vec1;
		private Vec3d vec2;
		private Vec3d vec3;

		/**
		 * f(x, y, z) = t * vec1 + s * vec2 + vec3
		 * 
		 * @param vec1
		 * @param vec2
		 * @param vec3
		 */
		public Surface(Vec3d vec1, Vec3d vec2, Vec3d vec3)
		{
			this.vec1 = vec1;
			this.vec2 = vec2;
			this.vec3 = vec3;
		}

		public double getDistance(Vec3d vec)
		{
			Vec3d base = this.vec1.crossProduct(this.vec2);
			Vec3d compare1 = base.crossProduct(this.vec1.crossProduct(vec));
			Vec3d base1 = base.crossProduct(this.vec1.crossProduct(this.vec3));
			Vec3d compare2 = base.crossProduct(this.vec2.crossProduct(vec));
			Vec3d base2 = base.crossProduct(this.vec2.crossProduct(this.vec3));
			if(compare1.x != 0.0) {
				return base1.x / compare1.x;
			}
			else if(compare1.y != 0.0) {
				return base1.y / compare1.y;
			}
			else if(compare1.z != 0.0) {
				return base1.z / compare1.z;
			}
			else if(compare2.x != 0.0) {
				return base2.x / compare2.x;
			}
			else if(compare2.y != 0.0) {
				return base2.y / compare2.y;
			}
			else if(compare2.z != 0.0) {
				return base2.z / compare2.z;
			}
			return Double.MAX_VALUE;
		}
	}

	public static final List<Surface> BASIC_CUBE = Arrays.asList(new Surface(new Vec3d(0, 0, 1), new Vec3d(0, 1, 0), new Vec3d(0.500000001, 0, 0)),
			new Surface(new Vec3d(0, 0, 1), new Vec3d(0, 1, 0), new Vec3d(-0.500000001, 0, 0)), new Surface(new Vec3d(0, 0, 1), new Vec3d(1, 0, 0), new Vec3d(0, -0.500000001, 0)),
			new Surface(new Vec3d(0, 0, 1), new Vec3d(1, 0, 0), new Vec3d(0, 0.500000001, 0)), new Surface(new Vec3d(1, 0, 0), new Vec3d(0, 1, 0), new Vec3d(0, 0, 0.500000001)),
			new Surface(new Vec3d(1, 0, 0), new Vec3d(0, 1, 0), new Vec3d(0, 0, -0.500000001)));
}
