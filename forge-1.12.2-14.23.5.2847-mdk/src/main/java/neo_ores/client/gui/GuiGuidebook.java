package neo_ores.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import org.lwjgl.input.Mouse;

import neo_ores.api.PlayerTrigger;
import neo_ores.api.Vec2I;
import neo_ores.api.guide.AbstractPageComponent;
import neo_ores.api.guide.ComponentHover;
import neo_ores.api.guide.GuidePage;
import neo_ores.api.guide.GuidePageEmpty;
import neo_ores.api.guide.GuidePageEnd;
import neo_ores.api.guide.GuidePageIndex;
import neo_ores.api.guide.GuidePageQuest;
import neo_ores.api.guide.GuidePageTitle;
import neo_ores.api.guide.GuideTab;
import neo_ores.main.NeoOresData;
import neo_ores.main.Reference;
import neo_ores.util.PlayerMagicDataClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiGuidebook extends GuiScreen
{
	public static final int windowSizeX = 395;
	public static final int windowSizeY = 208;
	public static final int pageSizeX = 146;
	public static final int pageSizeY = 189;
	public static final int page0X = 47;
	public static final int page0Y = 8;
	public static final int page1X = 198;
	public static final int page1Y = 8;
	public GuidePage page0 = new GuidePageEmpty();
	public GuidePage page1 = new GuidePageEmpty();
	public final List<GuideTab> tabs = new ArrayList<>();
	public final Map<GuideTab, List<GuidePage>> pageMap = new LinkedHashMap<>();
	public final Map<GuideTab, Integer> pageIndex = new LinkedHashMap<>();
	public final List<GuidePage> indexPages = new ArrayList<>();
	public int currentPage = 0;
	public int maxPage = 1;
	public final GuidePage titlePage;
	public final GuidePage endPage;
	private boolean leftbuttondowning = false;
	private boolean lastleftbuttondowning = false;
	private GuideTab reservedTab = null;
	private int prevPage = 0;
	private GuiButton buttonPrev;
	private GuiButton buttonNext;
	private GuiButton buttonIndex;
	private static final GuideTab quests = new GuideTab("guide.quest.name");
	private final List<GuidePage> reservedQuestPages = new ArrayList<>();
	public PlayerMagicDataClient pmdc;
	public Vec2I prevMousePos = Vec2I.ZERO;

	public GuiGuidebook()
	{
		this.titlePage = new GuidePageTitle();
		this.endPage = new GuidePageEnd();
		this.setFirstPage();
	}

	public void initGui()
	{
		super.initGui();
		this.pmdc = NeoOresData.getPMDC(EntityPlayer.getUUID(this.mc.player.getGameProfile()));
		this.pageMap.clear();
		this.tabs.clear();
		this.pageIndex.clear();
		this.indexPages.clear();
		List<GuidePage> registry = new ArrayList<>(GameRegistry.findRegistry(GuidePage.class).getValuesCollection());
		List<GuidePage> sorted = registry.stream().sorted(Comparator.comparingInt(new ToIntFunction<GuidePage>()
		{
			@Override
			public int applyAsInt(GuidePage arg0)
			{
				return arg0.getPriority();
			}
		})).collect(Collectors.toList());
		for (GuidePage page : sorted)
		{
			if (!this.tabs.contains(page.getTab()))
			{
				this.tabs.add(page.getTab());
			}
			if (!this.pageMap.containsKey(page.getTab()))
			{
				this.pageMap.put(page.getTab(), new ArrayList<>());
			}
			this.pageMap.get(page.getTab()).add(page);
		}

		this.reloadQuest();
		this.updateQuests();
		this.tabs.add(quests);

		int indexAll = this.tabs.size();
		if (indexAll > 0)
		{
			this.indexPages.add(new GuidePageIndex(this.tabs, 0, true, this.mc));
		}
		int currentIndex = GuidePageIndex.initIndex;
		while (indexAll > currentIndex)
		{
			this.indexPages.add(new GuidePageIndex(this.tabs, currentIndex, false, this.mc));
			currentIndex += GuidePageIndex.maxIndex;
		}

		this.maxPage = 1 + (this.indexPages.size() + 1) / 2;

		for (GuideTab tab : this.pageMap.keySet())
		{
			int pages = (this.pageMap.get(tab).size() + 1) / 2;
			this.pageIndex.put(tab, this.maxPage);
			this.maxPage += pages;
		}

		int i = (this.width - windowSizeX) / 2;
		int j = (this.height - windowSizeY) / 2;

		this.buttonList.clear();
		this.buttonIndex = this.addButton(new PageButton(0, i + 18, j + 30, 2));
		this.buttonNext = this.addButton(new PageButton(1, i + 360, j + 170, 0));
		this.buttonPrev = this.addButton(new PageButton(2, i + 18, j + 170, 1));
		this.updateButton();
		if (NeoOresData.guidePage != 0)
		{
			this.currentPage = NeoOresData.guidePage;
			NeoOresData.guidePage = 0;
		}
	}

	public void reloadQuest()
	{
		this.reservedQuestPages.clear();
		List<PlayerTrigger> questTriggers = new ArrayList<>();
		for (PlayerTrigger trigger : GameRegistry.findRegistry(PlayerTrigger.class).getValuesCollection())
		{
			if (trigger.hasDialogRewards())
			{
				questTriggers.add(trigger);
			}
		}
		questTriggers = questTriggers.stream().sorted(Comparator.comparingInt(new ToIntFunction<PlayerTrigger>()
		{
			@Override
			public int applyAsInt(PlayerTrigger arg0)
			{
				boolean triggered = pmdc.checkTrigger(arg0);
				boolean rewardFlag = pmdc.checkReward(arg0);
				return (rewardFlag ? 0 : 2) + (triggered ? 1 : 0);
			}
		})).collect(Collectors.toList());
		int indexAll = questTriggers.size();
		if (indexAll > 0)
		{
			this.reservedQuestPages.add(new GuidePageQuest(questTriggers, 0, true, this.mc, this.pmdc));
		}
		int currentIndex = GuidePageIndex.initIndex;
		while (indexAll > currentIndex)
		{
			this.reservedQuestPages.add(new GuidePageQuest(questTriggers, currentIndex, false, this.mc, this.pmdc));
			currentIndex += GuidePageIndex.maxIndex;
		}
	}

	public boolean updateQuests()
	{
		if (!this.reservedQuestPages.isEmpty())
		{
			if (!this.pageMap.containsKey(quests))
			{
				this.pageMap.put(quests, new ArrayList<>());
			}
			this.pageMap.get(quests).clear();
			this.pageMap.get(quests).addAll(this.reservedQuestPages);
			this.reservedQuestPages.clear();
			return true;
		}
		return false;
	}

	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.enabled)
		{
			if (button.id == 0)
			{
				this.jumpToIndex();
			}
			else if (button.id == 1)
			{
				this.currentPage = MathHelper.clamp(this.currentPage + 1, 0, this.maxPage);
			}
			else if (button.id == 2)
			{
				this.currentPage = MathHelper.clamp(this.currentPage - 1, 0, this.maxPage);
			}
			this.updateButton();
		}
	}

	public void updateButton()
	{
		if (this.currentPage <= 0)
		{
			this.buttonIndex.visible = false;
			this.buttonIndex.enabled = false;
			this.buttonPrev.visible = false;
			this.buttonPrev.enabled = false;
			this.buttonNext.visible = true;
			this.buttonNext.enabled = true;
		}
		else if (this.currentPage >= this.maxPage)
		{
			this.buttonIndex.visible = true;
			this.buttonIndex.enabled = true;
			this.buttonPrev.visible = true;
			this.buttonPrev.enabled = true;
			this.buttonNext.visible = false;
			this.buttonNext.enabled = false;
		}
		else if (this.currentPage - 1 < (this.indexPages.size() + 1) / 2)
		{
			this.buttonIndex.visible = false;
			this.buttonIndex.enabled = false;
			this.buttonPrev.visible = true;
			this.buttonPrev.enabled = true;
			this.buttonNext.visible = true;
			this.buttonNext.enabled = true;
		}
		else
		{
			this.buttonIndex.visible = true;
			this.buttonIndex.enabled = true;
			this.buttonPrev.visible = true;
			this.buttonPrev.enabled = true;
			this.buttonNext.visible = true;
			this.buttonNext.enabled = true;
		}
	}

	public void jumpToTabPage(GuideTab destination)
	{
		this.reservedTab = destination;
	}

	public void setFirstPage()
	{
		this.page0 = new GuidePageEmpty();
		this.page1 = this.titlePage;
		this.currentPage = 0;
	}

	public void setEndPage()
	{
		this.page0 = this.endPage;
		this.page1 = new GuidePageEmpty();
		this.currentPage = this.maxPage;
	}

	public FontRenderer getFont()
	{
		return this.fontRenderer;
	}

	public RenderItem getItemRenderer()
	{
		return this.itemRender;
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

	public void updateScreen()
	{
		super.updateScreen();

		if (!this.mc.player.isEntityAlive() || this.mc.player.isDead)
		{
			this.mc.player.closeScreen();
		}
	}

	public boolean doesGuiPauseGame()
	{
		return false;
	}

	public void onGuiClosed()
	{
		NeoOresData.guidePage = this.currentPage;
		super.onGuiClosed();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if (this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))
		{
			this.mc.player.closeScreen();
		}
		super.keyTyped(typedChar, keyCode);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.update();
		this.leftbuttondowning = Mouse.isButtonDown(0);
		boolean dragging = Mouse.isButtonDown(1);
		int i = (this.width - windowSizeX) / 2;
		int j = (this.height - windowSizeY) / 2;

		this.drawDefaultBackground();
		this.renderWindow(i, j);
		super.drawScreen(mouseX, mouseY, partialTicks);

		GlStateManager.translate(i, j, 0);
		int mX = mouseX - i;
		int mY = mouseY - j;

		Vec2I mousePos = new Vec2I(mX, mY);

		GlStateManager.translate(page0X, page0Y, 0);
		List<ComponentHover> hovers = new ArrayList<>();
		for (AbstractPageComponent component : this.page0.getList())
		{
			hovers.addAll(component.drawScreen(this, mX - page0X, mY - page0Y, partialTicks, !this.leftbuttondowning && this.lastleftbuttondowning, mousePos.subtract(this.prevMousePos), dragging));
		}
		GlStateManager.translate(-page0X, -page0Y, 0);
		GlStateManager.translate(page1X, page1Y, 0);
		List<ComponentHover> hovers1 = new ArrayList<>();
		for (AbstractPageComponent component : this.page1.getList())
		{
			hovers1.addAll(component.drawScreen(this, mX - page1X, mY - page1Y, partialTicks, !this.leftbuttondowning && this.lastleftbuttondowning, mousePos.subtract(this.prevMousePos), dragging));
		}
		GlStateManager.translate(-page1X, -page1Y, 0);
		GlStateManager.translate(page0X, page0Y, 0);
		for (ComponentHover hover : hovers)
		{
			hover.drawHover(this, mX - page0X, mY - page0Y, partialTicks, !this.leftbuttondowning && this.lastleftbuttondowning, mousePos.subtract(this.prevMousePos), dragging);
		}
		GlStateManager.translate(-page0X, -page0Y, 0);
		GlStateManager.translate(page1X, page1Y, 0);
		for (ComponentHover hover : hovers1)
		{
			hover.drawHover(this, mX - page1X, mY - page1Y, partialTicks, !this.leftbuttondowning && this.lastleftbuttondowning, mousePos.subtract(this.prevMousePos), dragging);
		}
		GlStateManager.translate(-page1X, -page1Y, 0);

		GlStateManager.translate(-i, -j, 0);

		this.prevMousePos = mousePos;
		this.lastleftbuttondowning = this.leftbuttondowning;
	}

	public void jumpToIndex()
	{
		this.currentPage = 1;
	}

	public void update()
	{
		this.pmdc = NeoOresData.getPMDC(EntityPlayer.getUUID(this.mc.player.getGameProfile()));

		boolean flag = this.updateQuests();

		if (this.reservedTab != null)
		{
			this.currentPage = this.pageIndex.get(this.reservedTab);
			this.reservedTab = null;
		}

		if (this.prevPage != this.currentPage || flag)
		{
			if (this.currentPage <= 0)
			{
				this.setFirstPage();
			}
			else if (this.currentPage >= this.maxPage)
			{
				this.setEndPage();
			}
			else
			{
				GuideTab currentTab = null;
				for (GuideTab tab : this.pageIndex.keySet())
				{
					int maxPages = this.pageIndex.get(tab);
					if (maxPages > this.currentPage)
					{
						break;
					}
					currentTab = tab;
				}

				if (currentTab != null)
				{
					int currentIndex = this.currentPage - this.pageIndex.get(currentTab);
					List<GuidePage> currentPages = this.pageMap.get(currentTab);
					this.page0 = currentPages.get(currentIndex * 2);
					if (currentPages.size() <= currentIndex * 2 + 1)
					{
						this.page1 = new GuidePageEmpty();
					}
					else
					{
						this.page1 = currentPages.get(currentIndex * 2 + 1);
					}
					for (AbstractPageComponent component : this.page0.getList())
					{
						component.init(this);
					}
					for (AbstractPageComponent component : this.page1.getList())
					{
						component.init(this);
					}
				}
				else if (this.currentPage - 1 < (this.indexPages.size() + 1) / 2)
				{
					int currentIndex = this.currentPage - 1;
					this.page0 = this.indexPages.get(currentIndex * 2);
					if (this.indexPages.size() <= currentIndex * 2 + 1)
					{
						this.page1 = new GuidePageEmpty();
					}
					else
					{
						this.page1 = this.indexPages.get(currentIndex * 2 + 1);
					}
					for (AbstractPageComponent component : this.page0.getList())
					{
						component.init(this);
					}
					for (AbstractPageComponent component : this.page1.getList())
					{
						component.init(this);
					}
				}
			}
			this.updateButton();
		}

		this.prevPage = this.currentPage;
	}

	public void renderWindow(int x, int y)
	{
		RenderHelper.disableStandardItemLighting();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.getBackgroundTexture());
		this.drawTexturedWithTextureSizeModalRect(x, y, 0, 0, windowSizeX, windowSizeY, 512, 512);
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

	public ResourceLocation getBackgroundTexture()
	{
		if (this.currentPage <= 0)
		{
			return new ResourceLocation(Reference.MOD_ID, "textures/gui/guide/guidebook_with_title.png");
		}
		else if (this.currentPage >= this.maxPage)
		{
			return new ResourceLocation(Reference.MOD_ID, "textures/gui/guide/guidebook_with_end.png");
		}
		else
		{
			return new ResourceLocation(Reference.MOD_ID, "textures/gui/guide/guidebook.png");
		}
	}

	public void renderToolTip(ItemStack stack, int x, int y)
	{
		super.renderToolTip(stack, x, y);
	}

	@SideOnly(Side.CLIENT)
	static class PageButton extends GuiButton
	{
		private final int shift;

		public PageButton(int buttonId, int x, int y, int shift)
		{
			super(buttonId, x, y, 16, 15, "");
			this.shift = shift;
		}

		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
		{
			if (this.visible)
			{
				boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(new ResourceLocation(Reference.MOD_ID, "textures/gui/guide/guidebook.png"));
				int i = 0;
				int j = 232;

				if (flag)
				{
					i += 16;
				}

				j += this.shift * 16;

				this.drawTexturedWithTextureSizeModalRect(this.x, this.y, i, j, 16, 15, 512, 512);
			}
		}

		public void drawTexturedWithTextureSizeModalRect(int x, int y, int textureX, int textureY, int width, int height, float textureWidth, float textureHeight)
		{
			float f = 1.0F / textureWidth;
			float f1 = 1.0F / textureHeight;
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
			bufferbuilder.pos((double) (x + 0), (double) (y + height), (double) this.zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1)).endVertex();
			bufferbuilder.pos((double) (x + width), (double) (y + height), (double) this.zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1))
					.endVertex();
			bufferbuilder.pos((double) (x + width), (double) (y + 0), (double) this.zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
			bufferbuilder.pos((double) (x + 0), (double) (y + 0), (double) this.zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
			tessellator.draw();
		}
	}
}
