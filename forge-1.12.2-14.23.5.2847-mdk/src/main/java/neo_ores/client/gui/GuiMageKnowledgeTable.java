package neo_ores.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Mouse;

import neo_ores.api.spell.Spell;
import neo_ores.api.spell.SpellItem;
import neo_ores.api.PlayerTrigger;
import neo_ores.api.spell.KnowledgeTab;
import neo_ores.api.spell.Spell.SpellCorrection;
import neo_ores.config.NeoOresConfig;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresData;
import neo_ores.main.Reference;
import neo_ores.util.NumberUtils;
import neo_ores.util.PlayerMagicDataClient;
import neo_ores.util.SpellUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMageKnowledgeTable extends GuiScreen
{
	private KnowledgeTab tab;

	private int scrollMouseX;
	private int scrollMouseY;
	private boolean isScrolling = false;
	private int scrollX = 0;
	private int scrollY = 0;
	private int minX = Integer.MAX_VALUE;
	private int minY = Integer.MAX_VALUE;
	private int maxX = Integer.MIN_VALUE;
	private int maxY = Integer.MIN_VALUE;
	private final Minecraft mc;
	static final int windowSizeX = 395;
	static final int windowSizeY = 208;
	static final int insideSizeX = 379;
	static final int insideSizeY = 168;
	static final int interval = 48;
	private PlayerMagicDataClient pmdc;
	private boolean leftbuttondowning = false;
	private boolean lastleftbuttondowning = false;
	private double scale = 1.0;
	private double minScale = 1.0;
	private final List<KnowledgeTab> tabs;
	private final List<SpellItem> currentSpells;
	private int currentTabIndex = 0;
	private int prevMouseX;
	private int prevMouseY;

	public GuiMageKnowledgeTable()
	{
		this.mc = Minecraft.getMinecraft();
		this.tab = NeoOres.neo_ores;
		this.currentTabIndex = 0;
		this.currentSpells = new ArrayList<>();
		this.tabs = SpellUtils.getAllStudyTabs();
		this.prevMouseX = 0;
		this.prevMouseY = 0;
		this.update();
	}

	public void initGui()
	{
		this.scale = 1.0;
		this.buttonList.add(new GuiButton(101, (this.width - windowSizeX) / 2 + 10, (this.height - windowSizeY) / 2 + 7, 100, 20,
				I18n.format(this.tab.getKey()) + " (" + (this.currentTabIndex + 1) + "/" + this.tabs.size() + ")")
		{
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
			{
				boolean flag = this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				if (flag)
				{
					currentTabIndex = (currentTabIndex + 1 < tabs.size()) ? currentTabIndex + 1 : 0;
					GuiMageKnowledgeTable.this.update();
				}
				return flag;
			}
		});
	}

	public void update()
	{
		this.tab = this.tabs.get(this.currentTabIndex);
		this.currentSpells.clear();
		for (SpellItem spell : SpellUtils.registry)
		{
			if (spell.getTab() == this.tab)
			{
				this.currentSpells.add(spell);
			}
		}
		this.minX = Integer.MAX_VALUE;
		this.minY = Integer.MAX_VALUE;
		this.maxX = Integer.MIN_VALUE;
		this.maxY = Integer.MIN_VALUE;
		for (SpellItem spellitem : this.currentSpells)
		{
			this.minX = Math.min(this.minX, spellitem.getPositionX() * interval - 16);
			this.maxX = Math.max(this.maxX, spellitem.getPositionX() * interval + 48);
			this.minY = Math.min(this.minY, spellitem.getPositionY() * interval - 16);
			this.maxY = Math.max(this.maxY, spellitem.getPositionY() * interval + 48);
		}
		this.scale = 1.0;
		this.scrollX = -this.getCenterX() + this.getScaledInsideSize().getFirst() / 2;
		this.scrollY = -this.getCenterY() + this.getScaledInsideSize().getSecond() / 2;
		this.minScale = Math.min(1.0, Math.min((double) insideSizeX / (double) (this.maxX - this.minX), (double) insideSizeY / (double) (this.maxY - this.minY)));
	}

	public int getCenterX()
	{
		return (int) (this.minX + this.maxX) / 2;
	}

	public int getCenterY()
	{
		return (int) (this.minY + this.maxY) / 2;
	}

	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		int w = Mouse.getEventDWheel();
		if (w != 0)
		{
			if (w > 0)
			{
				w = 1;
			}

			if (w < 0)
			{
				w = -1;
			}
			Tuple<Integer, Integer> pos = this.getScaledMousePos(this.prevMouseX, this.prevMouseY);
			this.scale = MathHelper.clamp(this.scale + (double) w / (double) 16, this.minScale, 1.0);
			Tuple<Integer, Integer> pos2 = this.getScaledMousePos(this.prevMouseX, this.prevMouseY);
			this.scroll((int) ((pos2.getFirst() - pos.getFirst()) * this.scale), (int) ((pos2.getSecond() - pos.getSecond()) * this.scale));
		}
	}

	public Tuple<Integer, Integer> getScaledMousePos(int x, int y)
	{
		int i = (this.width - windowSizeX) / 2;
		int j = (this.height - windowSizeY) / 2;
		int a = i + 8;
		int c = j + 32;
		int sx = -this.scrollX + (int) ((x - a) / this.scale);
		int sy = -this.scrollY + (int) ((y - c) / this.scale);
		return new Tuple<>(sx, sy);
	}

	public Tuple<Integer, Integer> getScaledInsideSize()
	{
		int sx = (int) (insideSizeX / this.scale);
		int sy = (int) (insideSizeY / this.scale);
		return new Tuple<>(sx, sy);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.prevMouseX = mouseX;
		this.prevMouseY = mouseY;
		this.pmdc = NeoOresData.getPMDC(EntityPlayer.getUUID(this.mc.player.getGameProfile()));

		int i = (this.width - windowSizeX) / 2;
		int j = (this.height - windowSizeY) / 2;

		if (Mouse.isButtonDown(1))
		{
			if (!this.isScrolling)
			{
				this.isScrolling = true;
				this.scrollMouseX = mouseX;
				this.scrollMouseY = mouseY;
			}
			this.scroll(mouseX - this.scrollMouseX, mouseY - this.scrollMouseY);

			this.scrollMouseX = mouseX;
			this.scrollMouseY = mouseY;
		}
		else
		{
			leftbuttondowning = Mouse.isButtonDown(0);
			this.isScrolling = false;
		}

		if (!leftbuttondowning && lastleftbuttondowning)
			this.mouseLeftClicked(mouseX, mouseY);

		this.drawDefaultBackground();
		this.renderInside(mouseX, mouseY, i, j);
		this.renderWindow(i, j);
		this.drawTooltip(mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
		lastleftbuttondowning = leftbuttondowning;
	}

	public void scroll(int x, int y)
	{
		this.scrollX = MathHelper.clamp(this.scrollX + (int) (x / this.scale), -this.maxX + (int) (insideSizeX / this.scale), -this.minX);
		if (-this.maxX + (int) (insideSizeX / this.scale) > -this.minX || this.scale == this.minScale)
		{
			this.scrollX = -this.getCenterX() + this.getScaledInsideSize().getFirst() / 2;
		}
		this.scrollY = MathHelper.clamp(this.scrollY + (int) (y / this.scale), -this.maxY + (int) (insideSizeY / this.scale), -this.minY);
		if (-this.maxY + (int) (insideSizeY / this.scale) > -this.minY || this.scale == this.minScale)
		{
			this.scrollY = -this.getCenterY() + this.getScaledInsideSize().getSecond() / 2;
		}
	}

	private void renderInside(int mouseX, int mouseY, int p_191936_3_, int p_191936_4_)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) (p_191936_3_ + 8), (float) (p_191936_4_ + 32), -400.0F);
		GlStateManager.enableDepth();
		if (this.scale != 0.0)
		{
			GlStateManager.scale(this.scale, this.scale, 1.0);
		}
		this.drawContents();
		if (this.scale != 0.0)
		{
			GlStateManager.scale(1.0 / this.scale, 1.0 / this.scale, 1.0);
		}
		GlStateManager.popMatrix();
		GlStateManager.depthFunc(515);
		GlStateManager.disableDepth();
	}

	public void drawContents()
	{
		GlStateManager.depthFunc(518);
		drawRect(0, 0, (int) (insideSizeX / this.scale), (int) (insideSizeY / this.scale), -16777216);
		GlStateManager.depthFunc(515);

		this.mc.getTextureManager().bindTexture(new ResourceLocation(NeoOresConfig.miscellaneous.mkt_back));

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int i = this.scrollX % 16;
		int j = this.scrollY % 16;

		for (int k = -1; k <= (double) 24 / this.scale; ++k)
		{
			for (int l = -1; l <= (double) 12 / this.scale; ++l)
			{
				this.drawTexturedWithTextureSizeModalRect(i + 16 * k, j + 16 * l, 0, 0, 16, 16, 16, 16);
			}
		}

		this.drawConnectivity(this.scrollX, this.scrollY, true);
		this.drawConnectivity(this.scrollX, this.scrollY, false);
		this.draw(this.scrollX, this.scrollY);
	}

	public void drawConnectivity(int x, int y, boolean isWide)
	{
		for (SpellItem spellitem : this.currentSpells)
		{
			if (spellitem.getParent() != null && spellitem.getParent().getTab() == this.tab)
			{
				int startX = spellitem.getPositionX() * interval + x + 16;
				int endX = spellitem.getParent().getPositionX() * interval + x + 16;
				int startY = spellitem.getPositionY() * interval + y + 16;
				int endY = spellitem.getParent().getPositionY() * interval + y + 16;
				if (isWide)
				{
					this.drawLine(startX, endX, startY, endY, 0x000000);
					this.drawLine(startX + 1, endX + 1, startY, endY, 0x000000);
					this.drawLine(startX - 1, endX - 1, startY, endY, 0x000000);
					this.drawLine(startX, endX, startY + 1, endY + 1, 0x000000);
					this.drawLine(startX, endX, startY - 1, endY - 1, 0x000000);
				}
				else
				{
					if (this.canGet(spellitem))
					{
						this.drawLine(startX, endX, startY, endY, 0x0000FF);
					}
					else if (this.pmdc.didGet(spellitem.getModId(), spellitem.getRegisteringId()))
					{
						this.drawLine(startX, endX, startY, endY, 0x00FF00);
					}
					else
					{
						this.drawLine(startX, endX, startY, endY, 0xFF0000);
					}
				}
			}
		}
	}

	private boolean isMouseInsideWindow(int x, int y)
	{
		int i = (this.width - windowSizeX) / 2;
		int j = (this.height - windowSizeY) / 2;
		int a = i + 8;
		int b = i + windowSizeX - 8;
		int c = j + 32;
		int d = j + windowSizeY - 8;
		return (a < x && x < b && c < y && y < d);
	}

	private void drawTooltip(int mouseX, int mouseY)
	{
		if (this.isMouseInsideWindow(mouseX, mouseY) && this.getSpell(mouseX, mouseY) != null)
		{
			List<String> tooltip = new ArrayList<String>();
			SpellItem spellitem = this.getSpell(mouseX, mouseY);

			tooltip.add(TextFormatting.WHITE + getName(spellitem));

			tooltip.add(TextFormatting.GRAY + I18n.format("spell." + spellitem.getTranslateKey() + ".desc"));
			tooltip.add(TextFormatting.BLUE + I18n.format("spell.type") + " : " + SpellUtils.colorFromSpellItem(spellitem) + I18n.format(SpellUtils.typeFromSpellItem(spellitem)));
			tooltip.add(TextFormatting.BLUE + I18n.format("spell.cost") + " : +" + spellitem.getCostsum());
			tooltip.add(TextFormatting.BLUE + I18n.format("spell.cost") + " : x" + spellitem.getCostproduct());
			tooltip.add(TextFormatting.GRAY + I18n.format("spell.recipe"));
			for (String formatted : SpellUtils.getRecipe(spellitem))
			{
				tooltip.add(TextFormatting.GRAY + formatted);
			}
			tooltip.add("");

			if (this.pmdc.didGet(spellitem.getModId(), spellitem.getRegisteringId()))
			{
				tooltip.add(TextFormatting.BOLD + (TextFormatting.GREEN + I18n.format("spell.available")));
			}
			else
			{
				if (!this.canGetSpellItemByMagicPoint(spellitem, this.mc.player))
				{
					tooltip.add((TextFormatting.BOLD + (TextFormatting.DARK_RED + I18n.format("spell.required") + " : ")) + TextFormatting.BLUE + spellitem.getTier() + " "
							+ I18n.format("spell.magic_point"));
				}
				else
				{
					tooltip.add(
							(TextFormatting.BOLD + (TextFormatting.GREEN + I18n.format("spell.required") + " : ")) + TextFormatting.BLUE + spellitem.getTier() + " " + I18n.format("spell.magic_point"));
				}

				if (spellitem.getParent() != null) 
				{
					if (!this.canGetSpellItemByTree(spellitem, this.mc.player))
					{
						tooltip.add((TextFormatting.BOLD + (TextFormatting.DARK_RED + I18n.format("spell.required") + " : ")) + TextFormatting.BLUE + getName(spellitem.getParent()));
					}
					else 
					{
						tooltip.add((TextFormatting.BOLD + (TextFormatting.GREEN + I18n.format("spell.required") + " : ")) + TextFormatting.BLUE + getName(spellitem.getParent()));
					}
				}
				
				if (!this.canGetSpellItemByTrigger(spellitem, this.mc.player))
				{
					tooltip.add((TextFormatting.BOLD + (TextFormatting.DARK_RED + I18n.format("spell.required") + " : ")) + TextFormatting.BLUE + I18n.format(spellitem.getTrigger().getUnlocalizedName()));
				}
				else if (spellitem.getTrigger() != null)
				{
					tooltip.add((TextFormatting.BOLD + (TextFormatting.GREEN + I18n.format("spell.required") + " : ")) + TextFormatting.BLUE + I18n.format(spellitem.getTrigger().getUnlocalizedName()));
				}
			}

			this.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer);
		}
	}

	public void drawLine(int startX, int endX, int startY, int endY, int color)
	{
		int minX;
		int maxX;
		int minY;
		int maxY;
		if (startX < endX)
		{
			minX = startX;
			maxX = endX;
		}
		else
		{
			minX = endX;
			maxX = startX;
		}
		if (startY < endY)
		{
			minY = startY;
			maxY = endY;
		}
		else
		{
			minY = endY;
			maxY = startY;
		}

		if (minX == maxX)
		{
			this.drawVerticalLine(minX, minY, maxY, color - 0x1000000);
			return;
		}

		if (minY == maxY)
		{
			this.drawHorizontalLine(minX, maxX, minY, color - 0x1000000);
			return;
		}

		float m = (float) (endY - startY) / (float) (endX - startX);
		if (Math.abs(m) < 1.0F)
		{
			m = 1.0F / m;
			for (int i = 0; i < maxY - minY; i++)
			{
				int d = Math.sqrt(1 + m * m) > 1.5 ? 0 : 1;
				if (m < 0)
					this.drawHorizontalLine(minX + (int) (i * -m) - 1, minX + (int) ((i + 1) * -m + d), maxY - i, color - 0x1000000);
				else
					this.drawHorizontalLine(minX + (int) (i * m) - 1, minX + (int) ((i + 1) * m + d), minY + i, color - 0x1000000);
			}
		}
		else
		{
			for (int i = 0; i < maxX - minX; i++)
			{
				int d = Math.sqrt(1 + m * m) > 1.5 ? 0 : 1;
				if (m < 0)
					this.drawVerticalLine(maxX - i, minY + (int) (i * -m) - 1, minY + (int) ((i + 1) * -m + d), color - 0x1000000);
				else
					this.drawVerticalLine(minX + i, minY + (int) (i * m) - 1, minY + (int) ((i + 1) * m + d), color - 0x1000000);
			}
		}
	}

	public void draw(int x, int y)
	{
		GlStateManager.enableDepth();
		for (SpellItem spellitem : this.currentSpells)
		{
			this.mc.getTextureManager().bindTexture(SpellUtils.textureFromSpellItem(spellitem));
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableBlend();
			this.drawTexturedWithTextureSizeAndScaleModalRect(x + spellitem.getPositionX() * interval, y + spellitem.getPositionY() * interval, 0, 0, 32, 32, 64, 64, 0.5F);

			Spell sc = spellitem.getSpellClass();
			if (sc instanceof Spell.SpellCorrection)
			{
				SpellCorrection correction = (SpellCorrection) sc;
				if (correction.getLevel() != 0)
				{
					this.mc.getTextureManager().bindTexture(new ResourceLocation(Reference.MOD_ID, "textures/gui/spell/" + "correction." + correction.getLevel() + ".png"));
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					GlStateManager.enableBlend();
					this.drawTexturedWithTextureSizeModalRect(x + spellitem.getPositionX() * interval + SpellUtils.offsetX(spellitem),
							y + spellitem.getPositionY() * interval + SpellUtils.offsetY(spellitem), 0, 0, 16, 16, 16, 16);
				}
			}

			this.mc.getTextureManager()
					.bindTexture(new ResourceLocation(spellitem.getTexturePath().getResourceDomain(), "textures/gui/spellitems/" + spellitem.getTexturePath().getResourcePath() + ".png"));
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableBlend();
			this.drawTexturedWithTextureSizeModalRect(x + spellitem.getPositionX() * interval + SpellUtils.offsetX(spellitem), y + spellitem.getPositionY() * interval + SpellUtils.offsetY(spellitem),
					0, 0, 16, 16, 16, 16);

			if (this.canGet(spellitem))
			{
				this.mc.getTextureManager().bindTexture(SpellUtils.textureFromSpellItemInactive(spellitem));
				GlStateManager.color(1.0F, 1.0F, 1.0F, (float) Math.sin(Minecraft.getSystemTime() / 100.0D) / 2.5F + 0.6F);
				GlStateManager.enableBlend();
				this.drawTexturedWithTextureSizeAndScaleModalRect(x + spellitem.getPositionX() * interval, y + spellitem.getPositionY() * interval, 0, 0, 32, 32, 64, 64, 0.5F);
			}
			else if (!this.pmdc.didGet(spellitem.getModId(), spellitem.getRegisteringId()))
			{
				this.mc.getTextureManager().bindTexture(SpellUtils.textureFromSpellItemInactive(spellitem));
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.enableBlend();
				this.drawTexturedWithTextureSizeAndScaleModalRect(x + spellitem.getPositionX() * interval, y + spellitem.getPositionY() * interval, 0, 0, 32, 32, 64, 64, 0.5F);
			}
		}
		GlStateManager.disableDepth();
	}

	public void renderWindow(int x, int y)
	{
		RenderHelper.disableStandardItemLighting();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(new ResourceLocation(Reference.MOD_ID, "textures/gui/study_table_window.png"));
		this.drawTexturedWithTextureSizeModalRect(x, y, 0, 0, windowSizeX, windowSizeY, 512, 512);
		this.fontRenderer.drawString(I18n.format("gui.mage_knowledge_table"), x + (windowSizeX - this.fontRenderer.getStringWidth(I18n.format("gui.mage_knowledge_table"))) / 2, y + 14, 4210752);
		this.fontRenderer.drawString(TextFormatting.BLUE + I18n.format("spell.magic_point") + " : " + NumberUtils.getPrefixedNumber(this.pmdc.getMagicPoint(), 4),
				x - 28 + (windowSizeX - this.fontRenderer.getStringWidth(I18n.format("spell.magic_point") + ":" + NumberUtils.getPrefixedNumber(this.pmdc.getMagicPoint(), 4))), y + 14, 4210752);
	}

	public boolean doesGuiPauseGame()
	{
		return false;
	}

	private void mouseLeftClicked(int mouseX, int mouseY)
	{
		if (this.isMouseInsideWindow(mouseX, mouseY) && this.getSpell(mouseX, mouseY) != null)
		{
			SpellItem spellitem = this.getSpell(mouseX, mouseY);
			if (this.canGetSpellItemByMagicPoint(spellitem, this.mc.player) && this.canGetSpellItemByTree(spellitem, this.mc.player))
			{
				this.pmdc.addMagicPoint((long) -spellitem.getTier());
				this.pmdc.set(spellitem.getModId(), spellitem.getRegisteringId());
				this.pmdc.sendToOtherSide(null);
				this.mc.world.playSound(this.mc.player, this.mc.player.getPosition(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0F, 2.0F);
			}
		}
	}

	public SpellItem getSpell(int mouseX, int mouseY)
	{
		Tuple<Integer, Integer> pos = this.getScaledMousePos(mouseX, mouseY);
		int x = pos.getFirst();
		int y = pos.getSecond();
		int indexX = x / interval + (x < 0 ? -1 : 0);
		int indexY = y / interval + (y < 0 ? -1 : 0);
		for (SpellItem spell : this.currentSpells)
		{
			if (spell.getPositionX() == indexX && spell.getPositionY() == indexY)
			{
				int dx = x - (indexX * interval + 16);
				int dy = y - (indexY * interval + 16);
				int lSq = dx * dx + dy * dy;
				Spell sc = spell.getSpellClass();
				int l = 0;
				if (sc instanceof Spell.SpellCorrection)
				{
					l = 110;
				}
				else if (sc instanceof Spell.SpellEffect)
				{
					l = 121;
				}
				else
				{
					l = 109;
				}
				if (lSq < l)
				{
					return spell;
				}
			}
		}
		return null;
	}

	public void drawTexturedWithTextureSizeModalRect(int x, int y, int textureX, int textureY, int width, int height, float textureWidth, float textureHeight)
	{
		float f = 1.0F / textureWidth;
		float f1 = 1.0F / textureHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double) (x + 0), (double) (y + height), (double) this.zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1)).endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + height), (double) this.zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1)).endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + 0), (double) this.zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
		bufferbuilder.pos((double) (x + 0), (double) (y + 0), (double) this.zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
		tessellator.draw();
	}

	public void drawTexturedWithTextureSizeAndScaleModalRect(int x, int y, int textureX, int textureY, int width, int height, float textureWidth, float textureHeight, float scale)
	{
		float f = 1.0F / (textureWidth * scale);
		float f1 = 1.0F / (textureHeight * scale);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double) (x + 0), (double) (y + height), (double) this.zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1)).endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + height), (double) this.zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1)).endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + 0), (double) this.zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
		bufferbuilder.pos((double) (x + 0), (double) (y + 0), (double) this.zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
		tessellator.draw();
	}

	private boolean canGetSpellItemByTree(SpellItem spellitem, EntityPlayerSP player)
	{
		return (spellitem.getParent() != null) ? this.pmdc.canGet(spellitem.getParent().getModId(), spellitem.getParent().getRegisteringId(), spellitem.getModId(), spellitem.getRegisteringId())
				: this.pmdc.canGetRoot(spellitem.getModId(), spellitem.getRegisteringId());
	}

	private boolean canGetSpellItemByMagicPoint(SpellItem spellitem, EntityPlayerSP player)
	{
		if (spellitem == null)
			return false;
		return this.pmdc.getMagicPoint() >= (long) spellitem.getTier();
	}
	
	private boolean canGetSpellItemByTrigger(SpellItem spellitem, EntityPlayerSP player)
	{
		if (spellitem == null)
			return false;
		PlayerTrigger trigger = spellitem.getTrigger();
		return trigger == null || this.pmdc.checkTrigger(trigger);
	}

	public void updateScreen()
	{
		super.updateScreen();

		if (!this.mc.player.isEntityAlive() || this.mc.player.isDead)
		{
			this.mc.player.closeScreen();
		}
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if (this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))
		{
			this.mc.player.closeScreen();
		}
		super.keyTyped(typedChar, keyCode);
	}

	private static String getName(SpellItem spellitem)
	{
		Spell sc = spellitem.getSpellClass();
		if (sc instanceof Spell.SpellCorrection)
		{
			SpellCorrection correction = (SpellCorrection) sc;
			return I18n.format("spell." + spellitem.getTranslateKey() + ".name") + I18n.format("correction." + correction.getLevel());
		}
		else
		{
			return I18n.format("spell." + spellitem.getTranslateKey() + ".name");
		}
	}

	public void onResize(Minecraft mcIn, int w, int h)
	{
		double scale = this.scale;
		super.onResize(mcIn, w, h);
		this.scale = scale;
		this.scroll(0, 0);
	}
	
	private boolean canGet(SpellItem item) 
	{
		return this.canGetSpellItemByMagicPoint(item, this.mc.player) && this.canGetSpellItemByTree(item, this.mc.player) && this.canGetSpellItemByTrigger(item, this.mc.player);
	}
}
