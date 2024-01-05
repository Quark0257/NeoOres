package neo_ores.api;

public class RingArray<T>
{
	private int head = 0;
	private T[] o;

	@SuppressWarnings("unchecked")
	public RingArray(T... o)
	{
		this.o = o;
	}

	public void setTop(int head)
	{
		if (head < 0 && o.length <= head)
		{
			return;
		}
		this.head = head;
	}

	public T get(int index)
	{
		if (head + index < o.length)
		{
			return o[head + index];
		}
		else
		{
			return o[head + index - o.length];
		}
	}

	public int getGlobalIndex(T value)
	{
		int i = 0;
		for (T s : o)
		{
			if (value == s || (value instanceof String && value.equals(s)))
				break;
			i++;
		}
		return i;
	}

	public int getLocalIndex(T value)
	{
		int i = getGlobalIndex(value);
		return (i + head < o.length) ? head + i : head + i - o.length;
	}
}
