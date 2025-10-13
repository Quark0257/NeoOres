package neo_ores.api.guide;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.ColorUtils;
import neo_ores.api.Vec2I;
import neo_ores.client.gui.GuiGuidebook;
import net.minecraft.client.Minecraft;

public class ComponentActiveString extends AbstractPageComponent
{
	private final Runnable runnable;
	private final String name;
	private final List<String> hoveredText;
	private final boolean enable;

	public ComponentActiveString(int x, int y, int width, int height, String name, List<String> hoveredText, Runnable runnable, boolean enable)
	{
		super(x, y, width, height);
		this.name = name;
		this.hoveredText = hoveredText;
		this.runnable = runnable;
		this.enable = enable;
	}

	@Override
	public List<ComponentHover> drawScreen(GuiGuidebook guide, int mouseX, int mouseY, float partialTicks, boolean isMouseLeftClicked, Vec2I draggedMouseDelta, boolean mouseDragging)
	{
		List<ComponentHover> list = new ArrayList<>();
		if (this.isMouseOver(mouseX, mouseY))
		{
			GuiGuidebook.drawRect(this.posX, this.posY, this.posX + this.width, this.posY + this.height, ColorUtils.makeColor4d(0.9, 0.9, 0.9, 1.0));
		}

		guide.getFont().drawString(this.name, this.posX, this.posY, 4210752);

		if (this.isMouseOver(mouseX, mouseY))
		{
			GuiGuidebook.drawRect(this.posX, this.posY, this.posX + this.width, this.posY + this.height,
					ColorUtils.makeColor4d(0.9922F, 0.9725F, 0.9216F, 0.1F + 0.1F * (float) Math.sin(Minecraft.getSystemTime() / 100.0D)));
			if (!this.hoveredText.isEmpty())
			{
				list.add(new ComponentHover()
				{
					@Override
					public void drawHover(GuiGuidebook guide, int mouseX1, int mouseY1, float partialTicks, boolean isMouseLeftClicked, Vec2I draggedMouseDelta, boolean mouseDragging)
					{
						guide.drawHoveringText(hoveredText, mouseX1, mouseY1);
					}
				});
			}
			if (isMouseLeftClicked && this.enable)
			{
				this.runnable.run();
				this.clicked(guide);
			}
		}
		return list;
	}

	public void clicked(GuiGuidebook guide)
	{
	}
}
