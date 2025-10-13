package neo_ores.api.guide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import neo_ores.api.IDialogReward;
import neo_ores.api.PlayerTrigger;
import neo_ores.client.gui.GuiGuidebook;
import neo_ores.util.PlayerMagicDataClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;

public class GuidePageQuest extends GuidePage
{
	public static final int maxIndex = 21;
	public static final int initIndex = 18;

	public GuidePageQuest(List<PlayerTrigger> tiggers, int index, boolean isInit, Minecraft mc, PlayerMagicDataClient pmdc)
	{
		super(null, 0);
		if (isInit) 
		{
			this.addComponent(new ComponentText(0, 4 + mc.fontRenderer.FONT_HEIGHT, GuiGuidebook.pageSizeX, 0, ComponentLayout.UP_CENTER, Arrays.asList(I18n.format("guide.quest.name"))));
		}
		for (int i = 0; i < (isInit ? initIndex : maxIndex); i++)
		{
			if (index + i < tiggers.size())
			{
				PlayerTrigger trigger = tiggers.get(index + i);
				boolean triggered = pmdc.checkTrigger(trigger);
				boolean rewardFlag = pmdc.checkReward(trigger);
				List<String> hoveredText = new ArrayList<>();
				hoveredText.add(I18n.format(trigger.getUnlocalizedName()));
				if (!triggered) 
				{
					hoveredText.add(TextFormatting.DARK_RED + I18n.format("quest.incompleted"));
				}
				else if (rewardFlag) 
				{
					hoveredText.add(TextFormatting.GREEN + I18n.format("quest.claimable"));
				}
				else
				{
					hoveredText.add(TextFormatting.GREEN + I18n.format("quest.completed"));
				}
				hoveredText.add(TextFormatting.GRAY + I18n.format(trigger.getDesc()));
				hoveredText.add("");
				hoveredText.add(TextFormatting.BLUE + I18n.format("quest.reward"));
				for (IDialogReward reward : trigger.getRewards())
				{
					hoveredText.add(TextFormatting.BLUE + I18n.format(reward.getDesc(), reward.getFormats()));
				}
				this.addComponent(new ComponentActiveString(8, (isInit ? (maxIndex - initIndex) * mc.fontRenderer.FONT_HEIGHT + 4 : 4) + i * mc.fontRenderer.FONT_HEIGHT,
						GuiGuidebook.pageSizeX - 8 * 2, mc.fontRenderer.FONT_HEIGHT, I18n.format(trigger.getUnlocalizedName()), hoveredText, new Runnable()
						{
							@Override
							public void run()
							{
								for (IDialogReward reward : trigger.getRewards())
								{
									reward.takeRewardClient(mc.player);
								}
								pmdc.takeReward(trigger);
								pmdc.sendToOtherSide(null);
								mc.world.playSound(mc.player, mc.player.getPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0F, 1.0F);
							}
						}, triggered && rewardFlag) {
					public void clicked(GuiGuidebook guide) 
					{
						guide.reloadQuest();
					}
				});
			}
		}
	}
}
