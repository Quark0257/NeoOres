package neo_ores.api.guide;

import neo_ores.api.Vec2I;
import neo_ores.client.gui.GuiGuidebook;

public abstract class ComponentHover
{
	public static final ComponentHover EMPTY = new ComponentHover()
	{
		@Override
		public void drawHover(GuiGuidebook guide, int mouseX, int mouseY, float partialTicks, boolean isMouseLeftClicked, Vec2I draggedMouseDelta, boolean mouseDragging)
		{
		}
	};

	public abstract void drawHover(GuiGuidebook guide, int mouseX, int mouseY, float partialTicks, boolean isMouseLeftClicked, Vec2I draggedMouseDelta, boolean mouseDragging);
}
