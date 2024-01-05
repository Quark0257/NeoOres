package neo_ores.client.gui;

import neo_ores.api.LongUtils;
import neo_ores.util.PlayerManaDataClient;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiNeoGameOverlay extends Gui
{
	public static final ResourceLocation MPIcon = new ResourceLocation("neo_ores:textures/gui/mana_bar.png");
	protected final Minecraft mc;
	protected final GuiNewChat persistantChatGUI;
	private final PlayerManaDataClient pmdc;
	private final long level;
	private final long mxp;
	private final long mana;
	private final long maxMana;
	private final long maxMXP;

	public GuiNeoGameOverlay(Minecraft minecraft)
	{
		this.mc = minecraft;
		this.persistantChatGUI = new GuiNewChat(minecraft);
		this.pmdc = new PlayerManaDataClient(mc.player);
		this.level = pmdc.getLevel();
		this.mxp = pmdc.getMXP();
		this.mana = pmdc.getMana();
		this.maxMana = pmdc.getMaxMana();
		this.maxMXP = pmdc.getMaxMXP();
		this.renderGameOverlay();
	}

	@SuppressWarnings("static-access")
	public void renderGameOverlay()
	{

		ScaledResolution scaled = new ScaledResolution(mc);
		int width = scaled.getScaledWidth() / 2 + 10;
		int height = scaled.getScaledHeight() - 49;
		FontRenderer fontrenderer = this.mc.fontRenderer;
		mc.getTextureManager().bindTexture(this.MPIcon);

		if (this.mc.getRenderViewEntity() instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer) this.mc.getRenderViewEntity();
			if (mc.player != null && mc.playerController.gameIsSurvivalOrAdventure() && this.level > 0)
			{
				int mp_bar = (int) (((float) this.mana / (float) this.maxMana) * 81.0F);
				int mxp_bar = (int) (((float) this.mxp / (float) this.maxMXP) * 81.0F);
				this.mc.mcProfiler.startSection("manaBar");
				if (entityplayer.isInsideOfMaterial(Material.WATER) && mc.player.getAir() > 0)
				{
					height -= 10;
					fontrenderer.drawString("", width, height, Integer.parseInt("FFFFFF", 16));
					for (int i = 0; i < 10; i++)
					{
						this.drawTexturedModalRect(width + i * 8, height, 0, 9, 9, 9);
					}

					if (this.mana > 0)
					{
						for (int i = 0; i <= mp_bar; i += 8)
						{
							if (i + 8 > mp_bar)
							{
								this.drawTexturedModalRect(width + i, height, 0, 0, mp_bar - i, 9);
							}
							else
							{
								this.drawTexturedModalRect(width + i, height, 0, 0, 9, 9);
							}
						}
					}

					if (this.mxp > 0)
					{
						for (int i = 0; i <= mxp_bar; i += 8)
						{
							if (i + 8 > mxp_bar)
							{
								this.drawTexturedModalRect(width + i, height, 0, 18, mxp_bar - i, 9);
							}
							else
							{
								this.drawTexturedModalRect(width + i, height, 0, 18, 9, 9);
							}
						}
					}
					width += 83;
					String level = I18n.format("gui.overlay.level") + " : " + LongUtils.convertString(this.level);
					fontrenderer.drawString(level, width - 1, height, Integer.parseInt("000000", 16));
					fontrenderer.drawString(level, width + 1, height, Integer.parseInt("000000", 16));
					fontrenderer.drawString(level, width, height - 1, Integer.parseInt("000000", 16));
					fontrenderer.drawString(level, width, height + 1, Integer.parseInt("000000", 16));
					fontrenderer.drawString(level, width, height, Integer.parseInt("66AAFF", 16));

					height += 10;
					String mana = I18n.format("gui.overlay.mana") + " : " + LongUtils.convertString(this.mana) + "/" + LongUtils.convertString(this.maxMana);
					fontrenderer.drawString(mana, width - 1, height, Integer.parseInt("000000", 16));
					fontrenderer.drawString(mana, width + 1, height, Integer.parseInt("000000", 16));
					fontrenderer.drawString(mana, width, height - 1, Integer.parseInt("000000", 16));
					fontrenderer.drawString(mana, width, height + 1, Integer.parseInt("000000", 16));
					fontrenderer.drawString(mana, width, height, Integer.parseInt("66AAFF", 16));
				}
				else
				{
					fontrenderer.drawString("", width, height, Integer.parseInt("FFFFFF", 16));
					for (int i = 0; i < 10; i++)
					{
						this.drawTexturedModalRect(width + i * 8, height, 0, 9, 9, 9);
					}

					if (this.mana > 0)
					{
						for (int i = 0; i <= mp_bar; i += 8)
						{
							if (i + 8 > mp_bar)
							{
								this.drawTexturedModalRect(width + i, height, 0, 0, mp_bar - i, 9);
							}
							else
							{
								this.drawTexturedModalRect(width + i, height, 0, 0, 9, 9);
							}
						}
					}

					if (this.mxp > 0)
					{
						for (int i = 0; i <= mxp_bar; i += 8)
						{
							if (i + 8 > mxp_bar)
							{
								this.drawTexturedModalRect(width + i, height, 0, 18, mxp_bar - i, 9);
							}
							else
							{
								this.drawTexturedModalRect(width + i, height, 0, 18, 9, 9);
							}
						}
					}
					width += 83;
					String level = I18n.format("gui.overlay.level") + " : " + LongUtils.convertString(this.level);
					fontrenderer.drawString(level, width - 1, height, Integer.parseInt("000000", 16));
					fontrenderer.drawString(level, width + 1, height, Integer.parseInt("000000", 16));
					fontrenderer.drawString(level, width, height - 1, Integer.parseInt("000000", 16));
					fontrenderer.drawString(level, width, height + 1, Integer.parseInt("000000", 16));
					fontrenderer.drawString(level, width, height, Integer.parseInt("66AAFF", 16));

					height += 10;
					String mana = I18n.format("gui.overlay.mana") + " : " + LongUtils.convertString(this.mana) + "/" + LongUtils.convertString(this.maxMana);
					fontrenderer.drawString(mana, width - 1, height, Integer.parseInt("000000", 16));
					fontrenderer.drawString(mana, width + 1, height, Integer.parseInt("000000", 16));
					fontrenderer.drawString(mana, width, height - 1, Integer.parseInt("000000", 16));
					fontrenderer.drawString(mana, width, height + 1, Integer.parseInt("000000", 16));
					fontrenderer.drawString(mana, width, height, Integer.parseInt("66AAFF", 16));
				}
				this.mc.mcProfiler.endSection();
			}
			mc.getTextureManager().bindTexture(Gui.ICONS);

		}
	}
}
