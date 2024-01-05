package neo_ores.api;

import scala.Char;

public class PrimitiveUtils
{
	public static Class<?> getObjectClass(Class<?> primitive)
	{
		if (primitive == int.class)
		{
			return Integer.class;
		}
		else if (primitive == char.class)
		{
			return Char.class;
		}
		else if (primitive == byte.class)
		{
			return Byte.class;
		}
		else if (primitive == short.class)
		{
			return Short.class;
		}
		else if (primitive == double.class)
		{
			return Double.class;
		}
		else if (primitive == float.class)
		{
			return Float.class;
		}
		else if (primitive == long.class)
		{
			return Long.class;
		}

		return Boolean.class;
	}
}
