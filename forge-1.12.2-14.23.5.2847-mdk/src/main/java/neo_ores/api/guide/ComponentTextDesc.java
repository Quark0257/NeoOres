package neo_ores.api.guide;

import java.util.List;

import neo_ores.client.gui.GuiGuidebook;

public class ComponentTextDesc extends ComponentText
{
	public ComponentTextDesc(int y, List<String> texts)
	{
		super(8, y, GuiGuidebook.pageSizeX - 16, 0, ComponentLayout.UP_LEFT, texts);
	}
}
