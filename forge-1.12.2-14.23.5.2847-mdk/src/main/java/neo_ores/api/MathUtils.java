package neo_ores.api;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3i;

public class MathUtils
{
	public static Vec2f getYawPitch(double x, double y, double z)
	{
		double dh = Math.sqrt(x * x + z * z);
		return new Vec2f((float) (-x * Math.toDegrees(Math.acos(z / dh)) / Math.abs(x)), (float) (-Math.toDegrees(Math.atan(y / dh))));
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
}
