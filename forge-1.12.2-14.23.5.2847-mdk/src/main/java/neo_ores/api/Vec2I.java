package neo_ores.api;

public class Vec2I
{
	public static final Vec2I ZERO = new Vec2I(0, 0);

	private final int x;
	private final int y;

	public Vec2I(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}

	public Vec2I add(Vec2I vec)
	{
		return new Vec2I(this.x + vec.x, this.y + vec.y);
	}
	
	public Vec2I subtract(Vec2I vec)
	{
		return new Vec2I(this.x - vec.x, this.y - vec.y);
	}
	
	public String toString() 
	{
		return "{" + this.x + "; " + this.y + "}";
	}
}
