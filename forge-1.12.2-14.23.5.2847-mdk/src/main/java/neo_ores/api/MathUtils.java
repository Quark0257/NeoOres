package neo_ores.api;

import net.minecraft.util.math.Vec2f;

public class MathUtils 
{
	public static Vec2f getYawPitch(double x,double y,double z)
	{
		double dh = Math.sqrt(x * x + z * z);
		return new Vec2f((float)(-x * Math.toDegrees(Math.acos(z / dh)) / Math.abs(x)),(float)(-Math.toDegrees(Math.atan(y / dh))));
	}
	
	public static boolean isInteger(double value)
	{
		return value == (int)value;
	}
	
	/*
	 * 0 - 1 - 2 - 3 made 0 - 1 - 0 - (-1)
	 */
	public static int sin(int i) {
		return (int)Math.sin(Math.PI * (i % 4) / 2);
	}
}
