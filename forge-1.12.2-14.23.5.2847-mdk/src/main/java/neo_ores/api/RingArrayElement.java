package neo_ores.api;

public class RingArrayElement<T> extends RingArray<T>
{
	@SuppressWarnings("unchecked")
	public RingArrayElement(T air,T earth,T fire,T water)
	{
		super(air,earth,water,fire);
	}
}
