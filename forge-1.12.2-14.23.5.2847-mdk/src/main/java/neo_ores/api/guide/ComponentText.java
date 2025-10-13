package neo_ores.api.guide;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.Vec2I;
import neo_ores.client.gui.GuiGuidebook;
import net.minecraft.client.gui.FontRenderer;

public class ComponentText extends AbstractPageComponent
{
	protected final ComponentLayout layout;
	protected final List<String> texts;
	
	public ComponentText(int x, int y, int width, int height, ComponentLayout layout, List<String> texts)
	{
		super(x, y, width, height);
		this.layout = layout;
		this.texts = texts;
	}

	@Override
	public List<ComponentHover> drawScreen(GuiGuidebook guide, int mouseX, int mouseY, float partialTicks, boolean isMouseLeftClicked, Vec2I draggedMouseDelta, boolean mouseDragging)
	{
		List<ComponentHover> list = new ArrayList<>();
		FontRenderer font = guide.getFont();
		List<String> wrappedTexts = new ArrayList<>();
		for (String text : this.texts) 
		{
			for (String splittedText : font.listFormattedStringToWidth(text, this.width)) 
			{
				wrappedTexts.add(splittedText);
			}
		}
		int baseY = this.layout.getTranslate().translate(new Vec2I(this.posX, this.posY), new Vec2I(this.width, this.height), font, "", wrappedTexts.size()).getY();
		for (String splittedText : wrappedTexts) 
		{
			int x = this.layout.getTranslate().translate(new Vec2I(this.posX, this.posY), new Vec2I(this.width, this.height), font, splittedText, 1).getX();
			font.drawString(splittedText, x, baseY, 4210752);
			baseY += font.FONT_HEIGHT;
		}
		return list;
	}
}
