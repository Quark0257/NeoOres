package neo_ores.api;

public class ColorUtils
{

	public static class RGB
	{
		public final int red;
		public final int green;
		public final int blue;

		public RGB(int red, int green, int blue)
		{
			this.red = red < 256 ? red : 255;
			this.green = green < 256 ? green : 255;
			this.blue = blue < 256 ? blue : 255;
		}

		public RGB(int color)
		{
			this(color / (256 * 256), (color % (256 * 256)) / 256, color % 256);
		}

		public int getColor()
		{
			return this.red * 256 * 256 + this.green * 256 + this.blue;
		}

		public String toString()
		{
			return "[" + this.red + "," + this.green + "," + this.blue + "]";
		}
	}

	public static int getColorWithWhite(int baseColor, double factor)
	{
		if (baseColor < 0)
			return -1;
		RGB base = new RGB(baseColor);
		int redDiff = (int) ((255 - base.red) * factor);
		int greenDiff = (int) ((255 - base.green) * factor);
		int blueDiff = (int) ((255 - base.blue) * factor);
		return new RGB(255 - redDiff, 255 - greenDiff, 255 - blueDiff).getColor();
	}

	public static RGB getThreePhase(double phase)
	{
		return new RGB((int) (255 * (Math.sin(phase) + 1.0) / 2.0), (int) (255 * (Math.sin(phase - 2.0 * Math.PI / 3.0) + 1.0) / 2.0), (int) (255 * (Math.sin(phase - 4 * Math.PI / 3.0) + 1.0) / 2.0));
	}

	public static double theta(RGB color)
	{
		return Math.atan2(2.0 * color.red - color.blue - color.green, Math.sqrt(3.0) * (color.blue - color.green));
	}

	public static double thetaDegree(RGB color)
	{
		return theta(color) / Math.PI * 180.0;
	}

	public static RGB makeColor(double index)
	{
		return makeThreeDegreePhase(90.0 * index - 88.0 + 16.0 * Math.sin(Math.PI * (index - 1.0) / 2.0));
	}

	public static RGB makeThreePhase(double phase)
	{
		RGB color = getThreePhase(phase);
		int max = Math.max(color.red, color.green);
		max = Math.max(max, color.blue);
		int min = Math.min(color.red, color.green);
		min = Math.min(min, color.blue);
		double factor = 255.0 / (double) (max - min);
		return new RGB((int) ((color.red - min) * factor), (int) ((color.green - min) * factor), (int) ((color.blue - min) * factor));
	}

	public static RGB makeThreeDegreePhase(double phase)
	{
		RGB color = getThreePhase(phase * Math.PI / 180.0);
		int max = Math.max(color.red, color.green);
		max = Math.max(max, color.blue);
		int min = Math.min(color.red, color.green);
		min = Math.min(min, color.blue);
		double factor = 255.0 / (double) (max - min);
		return new RGB((int) ((color.red - min) * factor + 0.5), (int) ((color.green - min) * factor + 0.5), (int) ((color.blue - min) * factor + 0.5));
	}
}
