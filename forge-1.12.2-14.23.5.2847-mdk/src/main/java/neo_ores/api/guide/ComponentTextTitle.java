package neo_ores.api.guide;

import java.util.Arrays;

import neo_ores.client.gui.GuiGuidebook;

public class ComponentTextTitle extends ComponentText
{
	public ComponentTextTitle(String text)
	{
		super(8, 4 + 9, GuiGuidebook.pageSizeX - 16, 0, ComponentLayout.UP_CENTER, Arrays.asList(text));
	}
}
