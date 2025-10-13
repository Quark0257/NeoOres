package neo_ores.api.guide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import neo_ores.client.gui.GuiGuidebook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;

public class GuidePageIndex extends GuidePage
{
	public static final int maxIndex = 21;
	public static final int initIndex = 18;

	public GuidePageIndex(List<GuideTab> tabs, int index, boolean isInit, Minecraft mc)
	{
		super(null, 0);
		if (isInit) 
		{
			this.addComponent(new ComponentText(0, 4 + mc.fontRenderer.FONT_HEIGHT, GuiGuidebook.pageSizeX, 0, ComponentLayout.UP_CENTER, Arrays.asList(I18n.format("guide.index.name"))));
		}
		for (int i = 0; i < (isInit ? initIndex : maxIndex); i++)
		{
			if (index + i < tabs.size())
			{
				GuideTab tab = tabs.get(index + i);
				this.addComponent(new ComponentActiveString(8, (isInit ? (maxIndex - initIndex) * mc.fontRenderer.FONT_HEIGHT + 4 : 4) + i * mc.fontRenderer.FONT_HEIGHT,
						GuiGuidebook.pageSizeX - 8 * 2, mc.fontRenderer.FONT_HEIGHT, I18n.format(tab.getKey()), new ArrayList<>(), new Runnable()
						{
							@Override
							public void run()
							{
							}
						}, true)
				{
					public void clicked(GuiGuidebook guide)
					{
						guide.jumpToTabPage(tab);
						guide.mc.world.playSound(guide.mc.player, guide.mc.player.getPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0F, 1.0F);
					}
				});
			}
		}
	}
}
