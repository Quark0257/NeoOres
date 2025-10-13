package neo_ores.api.guide;

import neo_ores.client.gui.GuiGuidebook;

import java.util.List;

import neo_ores.api.Vec2I;

public abstract class AbstractPageComponent
{
	protected final int posX;
	protected final int posY;
	protected final int width;
	protected final int height;
	
	public AbstractPageComponent(int x, int y, int width, int height) 
	{
		this.posX = x;
		this.posY = y;
		this.width = width;
		this.height = height;
	}
	
	public abstract List<ComponentHover> drawScreen(GuiGuidebook guide, int mouseX, int mouseY, float partialTicks, boolean isMouseLeftClicked, Vec2I draggedMouseDelta, boolean mouseDragging);
	
	public boolean isMouseOver(int mouseX, int mouseY) 
	{
		return this.posX <= mouseX && mouseX <= this.posX + this.width && this.posY <= mouseY && mouseY <= this.posY + this.height;
	}
	
	public void init(GuiGuidebook guide) {}
}
