package neo_ores.mana;

public class LongUtils 
{
	public static final long E = 1000000000000000000L;
	public static final long P = 1000000000000000L;
	public static final long T = 1000000000000L;
	public static final long G = 1000000000L;
	public static final long M = 1000000L;
	public static final long k = 1000L;
	public static String convertString(long value)
	{
		String s = "";
		if(value >= LongUtils.E)
		{
			int i1 = (int)(value / LongUtils.E);
			int i2 = (int)((value - (long)i1 * LongUtils.E) / LongUtils.P);
			for(int i = 1;i < LongUtils.getDigits((long)i1);i++)
			{
				i2 /= 10;
			}
			s += i1 + "." + i2 + "E";
		}
		else if(value >= LongUtils.P)
		{
			int i1 = (int)(value / LongUtils.P);
			int i2 = (int)((value - (long)i1 * LongUtils.P) / LongUtils.T);
			for(int i = 1;i < LongUtils.getDigits((long)i1);i++)
			{
				i2 /= 10;
			}
			s += i1 + "." + i2 + "P";
		}
		else if(value >= LongUtils.T)
		{
			int i1 = (int)(value / LongUtils.T);
			int i2 = (int)((value - (long)i1 * LongUtils.T) / LongUtils.G);
			for(int i = 1;i < LongUtils.getDigits((long)i1);i++)
			{
				i2 /= 10;
			}
			s += i1 + "." + i2 + "T";
		}
		else if(value >= LongUtils.G)
		{
			int i1 = (int)(value / LongUtils.G);
			int i2 = (int)((value - (long)i1 * LongUtils.G) / LongUtils.M);
			for(int i = 1;i < LongUtils.getDigits((long)i1);i++)
			{
				i2 /= 10;
			}
			s += i1 + "." + i2 + "G";
		}
		else if(value >= LongUtils.M)
		{
			int i1 = (int)(value / LongUtils.M);
			int i2 = (int)((value - (long)i1 * LongUtils.M) / LongUtils.k);
			for(int i = 1;i < LongUtils.getDigits((long)i1);i++)
			{
				i2 /= 10;
			}
			s += i1 + "." + i2 + "M";
		}
		else if(value >= LongUtils.k)
		{
			int i1 = (int)(value / LongUtils.k);
			int i2 = (int)((value - (long)i1 * LongUtils.k));
			for(int i = 1;i < LongUtils.getDigits((long)i1);i++)
			{
				i2 /= 10;
			}
			s += i1 + "." + i2 + "k";
		}
		else
		{
			s += value;
		}
		return s;
	}
	
	private static int getDigits(long value)
	{
		int digits = 0;
		long l = value;
		for(digits = 0;l > 0;digits++)
		{
			l /= 10L;
		}
		return digits;
	}
	
	public static long trimAdd(long value,long addValue)
	{
		long copied = value;
		
		if(copied + addValue > Long.MAX_VALUE)
		{
			copied = Long.MIN_VALUE;
		}
		else if(copied + addValue < Long.MIN_VALUE)
		{
			copied = Long.MAX_VALUE;
		}
		else
		{
			copied += value;
		}
		
		return copied;
	}
}
