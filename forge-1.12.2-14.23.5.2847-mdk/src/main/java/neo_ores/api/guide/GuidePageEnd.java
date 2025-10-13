package neo_ores.api.guide;

import java.util.Arrays;

import neo_ores.client.gui.GuiGuidebook;
import net.minecraft.util.text.TextFormatting;

public class GuidePageEnd extends GuidePage
{
	public GuidePageEnd()
	{
		super(null, 0);
		this.addComponent(new ComponentText(0, 90, GuiGuidebook.pageSizeX, 0, ComponentLayout.CENTER, Arrays.asList(TextFormatting.BLACK + "Made by Quark0257")));
	}
}