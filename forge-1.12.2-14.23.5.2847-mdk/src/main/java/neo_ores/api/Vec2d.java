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
}
