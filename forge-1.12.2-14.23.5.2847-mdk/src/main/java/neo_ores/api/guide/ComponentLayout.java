package neo_ores.api.guide;

import neo_ores.api.Vec2I;
import net.minecraft.client.gui.FontRenderer;

public enum ComponentLayout
{
	CENTER(new ITranslate()
	{
		@Override
		public Vec2I translate(Vec2I basePos, Vec2I size, FontRenderer renderer, String rawText, int lines)
		{
			int transX = basePos.getX() + size.getX() / 2 - renderer.getStringWidth(rawText) / 2;
			int transY = basePos.getY() + size.getY() / 2 - renderer.FONT_HEIGHT * lines / 2;
			return new Vec2I(transX, transY);
		}
	}), LEFT(new ITranslate()
	{
		@Override
		public Vec2I translate(Vec2I basePos, Vec2I size, FontRenderer renderer, String rawText, int lines)
		{
			int transX = basePos.getX();
			int transY = basePos.getY() + size.getY() / 2 - renderer.FONT_HEIGHT * lines / 2;
			return new Vec2I(transX, transY);
		}
	}), RIGHT(new ITranslate()
	{

		@Override
		public Vec2I translate(Vec2I basePos, Vec2I size, FontRenderer renderer, String rawText, int lines)
		{
			int transX = basePos.getX() + size.getX() - renderer.getStringWidth(rawText);
			int transY = basePos.getY() + size.getY() / 2 - renderer.FONT_HEIGHT * lines / 2;
			return new Vec2I(transX, transY);
		}
	}), UP_CENTER(new ITranslate()
	{

		@Override
		public Vec2I translate(Vec2I basePos, Vec2I size, FontRenderer renderer, String rawText, int lines)
		{
			int transX = basePos.getX() + size.getX() / 2 - renderer.getStringWidth(rawText) / 2;
			int transY = basePos.getY();
			return new Vec2I(transX, transY);
		}
	}), UP_LEFT(new ITranslate()
	{

		@Override
		public Vec2I translate(Vec2I basePos, Vec2I size, FontRenderer renderer, String rawText, int lines)
		{
			return basePos;
		}
	}), UP_RIGHT(new ITranslate()
	{

		@Override
		public Vec2I translate(Vec2I basePos, Vec2I size, FontRenderer renderer, String rawText, int lines)
		{
			int transX = basePos.getX() + size.getX() - renderer.getStringWidth(rawText);
			int transY = basePos.getY();
			return new Vec2I(transX, transY);
		}
	}), DOWN_CENTER(new ITranslate()
	{

		@Override
		public Vec2I translate(Vec2I basePos, Vec2I size, FontRenderer renderer, String rawText, int lines)
		{
			int transX = basePos.getX() + size.getX() / 2 - renderer.getStringWidth(rawText) / 2;
			int transY = basePos.getY() + size.getY() - renderer.FONT_HEIGHT * lines;
			return new Vec2I(transX, transY);
		}
	}), DOWN_LEFT(new ITranslate()
	{

		@Override
		public Vec2I translate(Vec2I basePos, Vec2I size, FontRenderer renderer, String rawText, int lines)
		{
			int transX = basePos.getX();
			int transY = basePos.getY() + size.getY() - renderer.FONT_HEIGHT * lines;
			return new Vec2I(transX, transY);
		}
	}), DOWN_RIGHT(new ITranslate()
	{

		@Override
		public Vec2I translate(Vec2I basePos, Vec2I size, FontRenderer renderer, String rawText, int lines)
		{
			int transX = basePos.getX() + size.getX() - renderer.getStringWidth(rawText);
			int transY = basePos.getY() + size.getY() - renderer.FONT_HEIGHT * lines;
			return new Vec2I(transX, transY);
		}
	});

	ITranslate func;

	ComponentLayout(ITranslate function)
	{
		this.func = function;
	}

	public ITranslate getTranslate()
	{
		return this.func;
	}

	public static interface ITranslate
	{
		public Vec2I translate(Vec2I basePos, Vec2I size, FontRenderer renderer, String rawText, int lines);
	}
}
