package neo_ores.util;

import javax.annotation.Nullable;

public class NumberUtils
{
	public enum Prefix 
	{
		E(1000000000000000000L),
		P(1000000000000000L),
		T(1000000000000L),
		G(1000000000L),
		M(1000000L),
		k(1000L);
		
		private long number;
		
		Prefix(long number) 
		{
			this.number = number;
		}
		
		public long getNumber() 
		{
			return this.number;
		}
	}
	
	@Nullable
	public static Prefix next(Prefix p) 
	{		
		boolean flag = false;
		for (Prefix pre : Prefix.values()) 
		{
			if (flag) 
			{
				return pre;
			}
			if (p == pre) 
			{
				flag = true;
			}
		}
		return null;
	}
	
	public static String getPrefixedNumber(long number, int digits) 
	{
		digits = Math.max(digits, 3);
		Prefix p = null;
		for (Prefix pre : Prefix.values()) 
		{
			if (number / pre.getNumber() != 0) 
			{
				p = pre;
				break;
			}
		}
		
		if (p == null) 
		{
			return String.valueOf((int) number);
		}
		
		String prefix = p.toString();
		long q = number / p.getNumber();
		long r = number % p.getNumber();
		String result = String.valueOf((int) q);
		digits -= result.length();
		if (digits <= 0) 
		{
			return result + prefix;
		}
		return result + "." + makeDecimal(r, digits, p) + prefix;
	}
	
	public static String makeDecimal(long value, int digits, Prefix base) 
	{
		String result = "";
		int baseDigits = String.valueOf(base.getNumber()).length() - 1;
		String valueStr = String.valueOf(value);
		for (int i = 0; i < baseDigits - valueStr.length(); i++) 
		{
			result += "0";
		}
		result += valueStr;
		return result.substring(0, digits);
	}
}
