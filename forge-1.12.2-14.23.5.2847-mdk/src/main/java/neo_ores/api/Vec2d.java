package neo_ores.api;

public class Vec2d
{
	private final double x;
	private final double y;

	public Vec2d(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public double getX()
	{
		return this.x;
	}

	public double getY()
	{
		return this.y;
	}

	public float getXAsFloat()
	{
		return (float) this.x;
	}

	public float getYAsFloat()
	{
		return (float) this.y;
	}
	
	public static Vec2d getFromPolar(double r, double arg) 
	{
		return new Vec2d(r * Math.cos(arg), r * Math.sin(arg));
	}
	
	public Vec2d add(Vec2d x) 
	{
		return new Vec2d(this.x + x.x, this.y + x.y);
	}
	
	public Vec2d subtract(Vec2d x) 
	{
		return new Vec2d(this.x - x.x, this.y - x.y);
	}
	
	public Vec2d negate() 
	{
		return new Vec2d(-this.x, -this.y);
	}
}
