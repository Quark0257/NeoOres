package neo_ores.api;

import java.util.ArrayList;
import java.util.List;

public class ListUtils
{
	@SafeVarargs
	public static final <T> List<T> getCombinedList(final List<T>... lists)
	{
		List<T> output = new ArrayList<T>();
		for (List<T> value : lists)
		{
			output.addAll(value);
		}
		return output;
	}
}
