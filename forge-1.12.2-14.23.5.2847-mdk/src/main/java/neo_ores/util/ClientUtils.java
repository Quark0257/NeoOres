package neo_ores.util;

import neo_ores.api.Vec2I;
import neo_ores.api.Vec2d;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientUtils
{
	public static void drawLine(Vec2I start, Vec2I end, float width, float red, float green, float blue, float alpha) 
	{
		double x0 = start.getX();
		double y0 = start.getY();
		Vec2I rVec = end.subtract(start);
		double r = rVec.getNorm();
		float arg = (float) rVec.getArgument();
		double halfWidth = 0.5 * width;
		
		Vec2d pos0 = Vec2d.getFromPolar(halfWidth, arg + 0.5D * Math.PI).negate().add(new Vec2d(x0, y0));
		Vec2d pos3 = Vec2d.getFromPolar(halfWidth, arg + 0.5D * Math.PI).add(new Vec2d(x0, y0));
		Vec2d pos1 = Vec2d.getFromPolar(r, arg).add(pos0);
		Vec2d pos2 = Vec2d.getFromPolar(r, arg).add(pos3);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.color(red, green, blue, alpha);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos(pos0.getX(), pos0.getY(), 0.0D).endVertex();
		bufferbuilder.pos(pos3.getX(), pos3.getY(), 0.0D).endVertex();
		bufferbuilder.pos(pos2.getX(), pos2.getY(), 0.0D).endVertex();
		bufferbuilder.pos(pos1.getX(), pos1.getY(), 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
	
	public static void drawSmoothLine(Vec2I start, Vec2I end, float width, float red, float green, float blue, float alpha)
	{
		int x0 = start.getX();
		int y0 = start.getY();
		int x1 = end.getX();
		int y1 = end.getY();
		if (start.getX() > end.getX())
		{
			x0 = end.getX();
			y0 = end.getY();
			x1 = start.getX();
			y1 = start.getY();
		}

		int deltaX = x1 - x0;
		int deltaY = y1 - y0;
		if (deltaX == 0 && deltaY == 0)
		{
			drawPixel(x0, y0, red, green, blue, alpha);
			return;
		}

		if (deltaX == 0)
		{
			if (width <= 1.0F)
			{
				drawRect(x0, y0, x0 + 1, y0 + deltaY, red, green, blue, alpha * width);
				return;
			}
			else
			{
				int pixel = ((int) width);
				for (int i = -pixel / 2; i <= pixel / 2; i++)
				{
					drawRect(x0 + i, y0, x0 + 1 + i, y0 + deltaY, red, green, blue, alpha);
				}
				float remain = (width - (float) pixel) * 0.5f;
				if (remain > 0)
				{
					int offset = pixel / 2 + 1;
					drawRect(x0 + offset, y0, x0 + 1 + offset, y0 + deltaY, red, green, blue, alpha * remain);
					drawRect(x0 - offset, y0, x0 + 1 - offset, y0 + deltaY, red, green, blue, alpha * remain);
				}
			}
			return;
		}

		if (deltaY == 0)
		{
			if (width <= 1.0F)
			{
				drawRect(x0, y0, x0 + deltaX, y0 + 1, red, green, blue, alpha * width);
				return;
			}
			else
			{
				int pixel = ((int) width);
				for (int i = -pixel / 2; i <= pixel / 2; i++)
				{
					drawRect(x0, y0 + i, x0 + deltaX, y0 + 1 + i, red, green, blue, alpha);
				}
				float remain = (width - (float) pixel) * 0.5f;
				if (remain > 0)
				{
					int offset = pixel / 2 + 1;
					drawRect(x0, y0 + offset, x0 + deltaX, y0 + 1 + offset, red, green, blue, alpha * remain);
					drawRect(x0, y0 - offset, x0 + deltaX, y0 + 1 - offset, red, green, blue, alpha * remain);
				}
			}
			return;
		}

		float m = (float) deltaY / (float) deltaX;
		float n = 1.0f / m;
		if (Math.abs(m) > 1.0f)
		{
			// vertical, y++
			n = m;
			m = 1.0f / m;
			float width0 = (float) Math.sqrt(m * m + 1) * width;

			int x = x0;
			int y = y0;
			if (m < 0.0)
			{
				y = y1;
				x = x1;
			}
			float xu = x + width0 * 0.5F + 0.5F - 0.5f * m;
			float xd = x - width0 * 0.5F + 0.5F - 0.5f * m;
			for (int dy = 0; dy <= Math.abs(deltaY); dy++)
			{
				int xuip = MathHelper.floor(xu);
				int xdip = MathHelper.floor(xd);
				xu += m;
				xd += m;
				int xui = MathHelper.floor(xu);
				int xdi = MathHelper.floor(xd);

				if (xuip == xui)
				{
					float remainU = ((xu - xui) + (xu - xui - m)) * 0.5f;
					drawPixel(xui, dy + y, red, green, blue, alpha * remainU);
				}
				else
				{
					if (m < 0.0f)
					{
						float dm = xuip - xu;
						float dn = dm * Math.abs(n);
						float remainUC = 1.0f - dn * dm * 0.5f;
						drawPixel(xui, dy + y, red, green, blue, alpha * remainUC);
						float remainUP = (1.0f - dn) * (Math.abs(m) - dm) * 0.5f;
						drawPixel(xuip, dy + y, red, green, blue, alpha * remainUP);
					}
					else
					{
						float dm = xu - xui;
						float dn = dm * Math.abs(n);
						float remainUC = dn * dm * 0.5f;
						drawPixel(xui, dy + y, red, green, blue, alpha * remainUC);
						float remainUP = 1.0f - (1.0f - dn) * (Math.abs(m) - dm) * 0.5f;
						drawPixel(xuip, dy + y, red, green, blue, alpha * remainUP);
						xui = xuip;
					}
				}

				if (xdip == xdi)
				{
					float remainD = 1.0f - ((xd - xdi) + (xd - xdi - m)) * 0.5f;
					drawPixel(xdi, dy + y, red, green, blue, alpha * remainD);
				}
				else
				{
					if (m < 0.0f)
					{
						float dm = xdip - xd;
						float dn = dm * Math.abs(n);
						float remainDC = dn * dm * 0.5f;
						drawPixel(xdi, dy + y, red, green, blue, alpha * remainDC);
						float remainDP = 1.0f - (1.0f - dn) * (Math.abs(m) - dm) * 0.5f;
						drawPixel(xdip, dy + y, red, green, blue, alpha * remainDP);
						xdi = xdip;
					}
					else
					{
						float dm = xd - xdi;
						float dn = dm * Math.abs(n);
						float remainDC = 1.0f - dn * dm * 0.5f;
						drawPixel(xdi, dy + y, red, green, blue, alpha * remainDC);
						float remainDP = (1.0f - dn) * (Math.abs(m) - dm) * 0.5f;
						drawPixel(xdip, dy + y, red, green, blue, alpha * remainDP);
					}
				}

				for (int dx = xdi + 1; dx <= xui - 1; dx++)
				{
					drawPixel(dx, y + dy, red, green, blue, alpha);
				}
			}
		}
		else
		{
			float width0 = (float) Math.sqrt(m * m + 1) * width;
			// horizon, x++
			int x = x0;
			int y = y0;
			float yu = y + width0 * 0.5F + 0.5F - 0.5f * m;
			float yd = y - width0 * 0.5F + 0.5F - 0.5f * m;
			for (int dx = 0; dx <= deltaX; dx++)
			{
				int yuip = MathHelper.floor(yu);
				int ydip = MathHelper.floor(yd);
				yu += m;
				yd += m;
				int yui = MathHelper.floor(yu);
				int ydi = MathHelper.floor(yd);

				if (yuip == yui)
				{
					float remainU = ((yu - yui) + (yu - yui - m)) * 0.5f;
					drawPixel(dx + x0, yui, red, green, blue, alpha * remainU);
				}
				else
				{
					if (m < 0.0f)
					{
						float dm = yuip - yu;
						float dn = dm * Math.abs(n);
						float remainUC = 1.0f - dn * dm * 0.5f;
						drawPixel(dx + x0, yui, red, green, blue, alpha * remainUC);
						float remainUP = (1.0f - dn) * (Math.abs(m) - dm) * 0.5f;
						drawPixel(dx + x0, yuip, red, green, blue, alpha * remainUP);
					}
					else
					{
						float dm = yu - yui;
						float dn = dm * Math.abs(n);
						float remainUC = dn * dm * 0.5f;
						drawPixel(dx + x0, yui, red, green, blue, alpha * remainUC);
						float remainUP = 1.0f - (1.0f - dn) * (Math.abs(m) - dm) * 0.5f;
						drawPixel(dx + x0, yuip, red, green, blue, alpha * remainUP);
						yui = yuip;
					}
				}

				if (ydip == ydi)
				{
					float remainD = 1.0f - ((yd - ydi) + (yd - ydi - m)) * 0.5f;
					drawPixel(dx + x0, ydi, red, green, blue, alpha * remainD);
				}
				else
				{
					if (m < 0.0f)
					{
						float dm = ydip - yd;
						float dn = dm * Math.abs(n);
						float remainDC = dn * dm * 0.5f;
						drawPixel(dx + x0, ydi, red, green, blue, alpha * remainDC);
						float remainDP = 1.0f - (1.0f - dn) * (Math.abs(m) - dm) * 0.5f;
						drawPixel(dx + x0, ydip, red, green, blue, alpha * remainDP);
						ydi = ydip;
					}
					else
					{
						float dm = yd - ydi;
						float dn = dm * Math.abs(n);
						float remainDC = 1.0f - dn * dm * 0.5f;
						drawPixel(dx + x0, ydi, red, green, blue, alpha * remainDC);
						float remainDP = (1.0f - dn) * (Math.abs(m) - dm) * 0.5f;
						drawPixel(dx + x0, ydip, red, green, blue, alpha * remainDP);
					}
				}

				for (int dy = ydi + 1; dy <= yui - 1; dy++)
				{
					drawPixel(x + dx, dy, red, green, blue, alpha);
				}
			}
		}
	}
	

	public static void drawPixel(int x, int y, float red, float green, float blue, float alpha)
	{
		drawRect(x, y, x + 1, y + 1, red, green, blue, alpha);
	}

	public static void drawRect(int left, int top, int right, int bottom, float red, float green, float blue, float alpha)
	{
		if (left < right)
		{
			int i = left;
			left = right;
			right = i;
		}

		if (top < bottom)
		{
			int j = top;
			top = bottom;
			bottom = j;
		}

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.color(red, green, blue, alpha);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos((double) left, (double) bottom, 0.0D).endVertex();
		bufferbuilder.pos((double) right, (double) bottom, 0.0D).endVertex();
		bufferbuilder.pos((double) right, (double) top, 0.0D).endVertex();
		bufferbuilder.pos((double) left, (double) top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
}
