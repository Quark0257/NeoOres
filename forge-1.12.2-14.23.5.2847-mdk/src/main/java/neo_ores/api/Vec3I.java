package neo_ores.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class Vec3I
{
	public static final Vec3I X = new Vec3I(1, 0, 0);
	public static final Vec3I Y = new Vec3I(0, 1, 0);
	public static final Vec3I Z = new Vec3I(0, 0, 1);
	
	private final int x;
	private final int y;
	private final int z;
	
	public Vec3I(int x, int y, int z) 
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3I(Vec3i vec) 
	{
		this.x = vec.getX();
		this.y = vec.getY();
		this.z = vec.getZ();
	}
	
	public Vec3i getVec() 
	{
		return new Vec3i(x, y, z);
	}
	
	public BlockPos getPos() 
	{
		return new BlockPos(x, y, z);
	}
	
	public Vec3I subtract(Vec3I vec) 
	{
		return new Vec3I(x - vec.x, y - vec.y, z - vec.z);
	}
	
	public Vec3I add(Vec3I vec) 
	{
		return new Vec3I(x + vec.x, y + vec.y, z + vec.z);
	}
	
	public Vec3I scalarMultiply(int value) 
	{
		return new Vec3I(x * value, y * value, z * value);
	}
	
	public Vec3I scalarDivision(int value) 
	{
		return new Vec3I(x / value, y / value, z / value);
	}
	
	public Vec3I reverse() 
	{
		return new Vec3I(-x, -y, -z);
	}
	
	public int lengthSq() 
	{
		return x * x + y * y + z * z;
	}
	
	public int dotProduct(Vec3I vec) 
	{
		return x * vec.x + y * vec.y + z * vec.z;
	}
	
	public Vec3I normalized() 
	{
		int max = Math.max(Math.max(Math.abs(x), Math.abs(y)), Math.abs(z));
		if (max == Math.abs(x)) 
		{
			return X.scalarMultiply(max == 0 ? 1 : x / max);
		}
		if (max == Math.abs(y)) 
		{
			return Y.scalarMultiply(max == 0 ? 1 : y / max);
		}
		return Z.scalarMultiply(max == 0 ? 1 : z / max);
	}
	
	public int getX() 
	{
		return this.x;
	}
	
	
	public int getY() 
	{
		return this.y;
	}
	
	public int getZ() 
	{
		return this.z;
	}
}
