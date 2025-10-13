package neo_ores.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.ToIntFunction;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import neo_ores.event.NeoOresClientEvents;
import neo_ores.inventory.ContainerPedestalInterface;
import neo_ores.pi.IPIListener;
import neo_ores.pi.InventoryPI;
import neo_ores.pi.PICommand;
import neo_ores.util.NumberUtils;
import neo_ores.util.ServerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.common.MinecraftForge;

public class GuiPedestalInterface extends GuiContainer implements IPIListener
{
	private static final ResourceLocation INTERFACE_GUI_TEXTURES = new ResourceLocation("neo_ores:textures/gui/pedestal_interface.png");
	private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	private float currentScroll;
	private boolean isScrolling;
	private boolean wasClicking;
	private GuiTextField searchField;
	private boolean clearSearch;
	private final List<ItemStack> itemList = new ArrayList<>();
	protected ItemStack prevResult = ItemStack.EMPTY;
	private PICommand standbyCommand = null;
	private PICommand prevCommand = null;
	private boolean prevCommandExecuted = false;

	public GuiPedestalInterface(InventoryPlayer inventory, boolean isLocalWorld, EntityPlayer player)
	{
		super(new ContainerPedestalInterface(inventory, isLocalWorld, player));
		NeoOresClientEvents.getInstance().getPIClientData().setGui(this);
		this.xSize = 258;
		this.ySize = 208;
	}

	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		this.searchField = new GuiTextField(0, this.fontRenderer, (this.width - xSize) / 2 + 151, (this.height - ySize) / 2 + 8, 96, this.fontRenderer.FONT_HEIGHT);
		this.searchField.setTextColor(-1);
		this.searchField.setDisabledTextColour(-1);
		this.searchField.setEnableBackgroundDrawing(true);
		this.searchField.setMaxStringLength(127);
		this.prevCommandExecuted = true;
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if (this.clearSearch)
		{
			this.clearSearch = false;
			this.searchField.setText("");
		}

		if (!this.checkHotbarKeys(keyCode))
		{
			if (this.searchField.textboxKeyTyped(typedChar, keyCode))
			{
				this.updateSearch();
			}
			else
			{
				super.keyTyped(typedChar, keyCode);
			}
		}
	}

	private void updateSearch()
	{
		this.updateItems();
		this.currentScroll = 0.0F;
		this.getPIContainer().scrollTo(0.0F);
	}

	private void updateItems()
	{
		this.getPIContainer().itemList.clear();
		this.getPIContainer().itemList.addAll(this.itemList);
		if (!this.searchField.getText().isEmpty())
		{
			String search = this.searchField.getText().toLowerCase(Locale.ROOT);
			Iterator<ItemStack> itr = this.getPIContainer().itemList.iterator();
			while (itr.hasNext())
			{
				ItemStack stack = itr.next();
				boolean matches = false;
				for (String line : stack.getTooltip(this.mc.player, this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL))
				{
					if (TextFormatting.getTextWithoutFormattingCodes(line).toLowerCase(Locale.ROOT).contains(search))
					{
						matches = true;
						break;
					}
				}
				if (!matches)
					itr.remove();
			}
		}
		this.getPIContainer().itemList.sort(Comparator.comparingInt(new ToIntFunction<ItemStack>()
		{
			@Override
			public int applyAsInt(ItemStack arg0)
			{
				return -arg0.getCount();
			}
		}));
		this.getPIContainer().scrollTo(this.currentScroll);
	}

	private ContainerPedestalInterface getPIContainer()
	{
		return (ContainerPedestalInterface) this.inventorySlots;
	}

	private boolean needsScrollBars()
	{
		return this.getPIContainer().canScroll();
	}

	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();

		if (i != 0 && this.needsScrollBars())
		{
			int j = (this.getPIContainer().itemList.size() + 12 - 1) / 12 - 5;

			if (i > 0)
			{
				i = 1;
			}

			if (i < 0)
			{
				i = -1;
			}

			this.currentScroll = (float) ((double) this.currentScroll - (double) i / (double) j);
			this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
			this.getPIContainer().scrollTo(this.currentScroll);
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		boolean flag = Mouse.isButtonDown(0);
		int i = this.guiLeft;
		int j = this.guiTop;
		int k = i + 235;
		int l = j + 24;
		int i1 = k + 14;
		int j1 = l + 178;

		if (!this.wasClicking && flag && mouseX >= k && mouseY >= l && mouseX < i1 && mouseY < j1)
		{
			this.isScrolling = this.needsScrollBars();
		}

		if (!flag)
		{
			this.isScrolling = false;
		}

		this.wasClicking = flag;

		if (this.isScrolling)
		{
			this.currentScroll = ((float) (mouseY - l) - 7.5F) / ((float) (j1 - l) - 15.0F);
			this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
			this.getPIContainer().scrollTo(this.currentScroll);
		}

		this.drawScreenWrapper(mouseX, mouseY, partialTicks);
		this.searchField.drawTextBox();
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		this.update();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(INTERFACE_GUI_TEXTURES);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedWithTextureSizeModalRect(i, j, 0, 0, this.xSize, this.ySize, 512, 512);
		this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);
		int ik = this.guiLeft + 235;
		int jk = this.guiTop + 24;
		int k = jk + 178;
		this.drawTexturedModalRect(ik, jk + (int) ((float) (k - jk - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
	}

	protected void update()
	{
		if (this.prevCommandExecuted)
		{
			this.prevCommand = this.standbyCommand == null ? new PICommand().setCommand(PICommand.SYNC) : this.standbyCommand;
			boolean customCommand = NeoOresClientEvents.getInstance().getPIClientData().setCommand(this.prevCommand);
			if (!customCommand)
			{
				this.prevCommand = new PICommand().setCommand(PICommand.SYNC);
				NeoOresClientEvents.getInstance().getPIClientData().setCommand(this.prevCommand);
			}
			this.prevCommandExecuted = false;
			this.standbyCommand = null;
		}
	}

	protected void mouseReleased(int mouseX, int mouseY, int state)
	{
		super.mouseReleased(mouseX, mouseY, state);
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

	protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type)
	{
		if (this.standbyCommand != null)
		{
			return;
		}
		boolean touchSc = this.mc.gameSettings.touchscreen;
		if (slotIn != null)
		{
			slotId = slotIn.slotNumber;
		}
		int playerSlotSize = this.getPIContainer().inventorySlots.size() - this.getPIContainer().basicInventory.getSizeInventory();
		if (slotId >= playerSlotSize)
		{
			if (type != ClickType.SWAP && type != ClickType.CLONE && type != ClickType.QUICK_CRAFT)
			{
				if (mouseButton == 0 || mouseButton == 1)
				{
					Slot slot = this.getPIContainer().getSlot(slotId);
					ItemStack target = slot.getStack().copy();
					boolean left = touchSc || mouseButton == 0;
					boolean right = !touchSc && mouseButton == 1;
					boolean th = type == ClickType.THROW;
					boolean isCtrl = mouseButton == 1;
					if (type == ClickType.QUICK_MOVE)
					{
						target.setCount(target.getMaxStackSize());
						this.standbyCommand = new PICommand().setCommand(PICommand.EXTRACT).setTarget(target).setTransfer(true);
					}
					else if (th)
					{
						if (isCtrl)
						{
							target.setCount(target.getMaxStackSize());
						}
						else
						{
							target.setCount(1);
						}
						this.standbyCommand = new PICommand().setCommand(PICommand.EXTRACT).setTarget(target).setThrow(true);
					}
					else if (left || right)
					{
						if (this.mc.player.inventory.getItemStack().isEmpty())
						{
							if (slot.getHasStack())
							{
								if (left)
								{
									target.setCount(target.getMaxStackSize());
								}
								else
								{
									target.setCount(Math.min(target.getMaxStackSize(), target.getCount()) / 2);
								}
								this.standbyCommand = new PICommand().setCommand(PICommand.EXTRACT).setTarget(target).setPickup(true);
							}
						}
						else
						{
							ItemStack targetStack = this.mc.player.inventory.getItemStack().copy();
							this.standbyCommand = new PICommand().setCommand(PICommand.INSERT).setTarget(targetStack).setPickup(true);
							if (right)
							{
								this.standbyCommand.setCount(1);
							}
						}
					}
				}
			}
		}
		else if (slotId >= 0 && slotId < playerSlotSize && type == ClickType.QUICK_MOVE)
		{
			this.standbyCommand = new PICommand().setCommand(PICommand.INSERT).setTarget(this.getPIContainer().getSlot(slotId).getStack().copy()).setSlot(slotId).setTransfer(true);
		}
		else
		{
			super.handleMouseClick(slotIn, slotId, mouseButton, type);
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void onGuiClosed()
	{
		super.onGuiClosed();
		NeoOresClientEvents.getInstance().getPIClientData().setGui(null);
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void setItemList(List<ItemStack> list)
	{
		this.itemList.clear();
		this.itemList.addAll(list);
		this.updateItems();
	}

	@Override
	public void setResult(ItemStack stack)
	{
		if (this.prevCommand.getPickup())
		{
			this.mc.player.inventory.setItemStack(stack);
		}
		else if (this.prevCommand.getTransfer())
		{

		}
		this.prevResult = stack;
	}

	@Override
	public void executed()
	{
		this.prevCommandExecuted = true;
	}

	public void drawScreenWrapper(int mouseX, int mouseY, float partialTicks)
	{
		int i = this.guiLeft;
		int j = this.guiTop;
		this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		for (int i1 = 0; i1 < this.buttonList.size(); ++i1)
		{
			((GuiButton) this.buttonList.get(i1)).drawButton(this.mc, mouseX, mouseY, partialTicks);
		}

		for (int j1 = 0; j1 < this.labelList.size(); ++j1)
		{
			((GuiLabel) this.labelList.get(j1)).drawLabel(this.mc, mouseX, mouseY);
		}
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) i, (float) j, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableRescaleNormal();
		ServerUtils.setPrivateValue(GuiContainer.class, (GuiContainer) this, null, "hoveredSlot", "field_147006_u");
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1)
		{
			Slot slot = this.inventorySlots.inventorySlots.get(i1);

			if (slot.isEnabled())
			{
				this.drawSlot(slot);
			}

			if (this.isPointInRegion(slot.xPos, slot.yPos, 16, 16, mouseX, mouseY) && slot.isEnabled())
			{
				ServerUtils.setPrivateValue(GuiContainer.class, (GuiContainer) this, slot, "hoveredSlot", "field_147006_u");
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				int j1 = slot.xPos;
				int k1 = slot.yPos;
				GlStateManager.colorMask(true, true, true, false);
				this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
				GlStateManager.colorMask(true, true, true, true);
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
			}
		}

		RenderHelper.disableStandardItemLighting();
		this.drawGuiContainerForegroundLayer(mouseX, mouseY);
		RenderHelper.enableGUIStandardItemLighting();
		MinecraftForge.EVENT_BUS.post(new GuiContainerEvent.DrawForeground(this, mouseX, mouseY));
		InventoryPlayer inventoryplayer = this.mc.player.inventory;
		ItemStack draggedStack = (ItemStack) ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "draggedStack", "field_147012_x");
		ItemStack itemstack = draggedStack.isEmpty() ? inventoryplayer.getItemStack() : draggedStack;

		if (!itemstack.isEmpty())
		{
			int k2 = draggedStack.isEmpty() ? 8 : 16;
			String s = null;

			if (!draggedStack.isEmpty() && (boolean) ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "isRightMouseClick", "field_147004_w"))
			{
				itemstack = itemstack.copy();
				itemstack.setCount(MathHelper.ceil((float) itemstack.getCount() / 2.0F));
			}
			else if (this.dragSplitting && this.dragSplittingSlots.size() > 1)
			{
				itemstack = itemstack.copy();
				itemstack.setCount(ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "dragSplittingRemnant", "field_146996_I"));

				if (itemstack.isEmpty())
				{
					s = "" + TextFormatting.YELLOW + "0";
				}
			}

			this.drawItemStack(itemstack, mouseX - i - 8, mouseY - j - k2, s);
		}

		if (!((ItemStack) ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "returningStack", "field_146991_C")).isEmpty())
		{
			float f = (float) (Minecraft.getSystemTime() - (long) ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "returningStackTime", "field_146990_B")) / 100.0F;

			if (f >= 1.0F)
			{
				f = 1.0F;
				ServerUtils.setPrivateValue(GuiContainer.class, (GuiContainer) this, ItemStack.EMPTY, "returningStack", "field_146991_C");
			}

			int touchUpX = ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "touchUpX", "field_147011_y");
			int touchUpY = ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "touchUpY", "field_147010_z");
			Slot returningStackDestSlot = ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "returningStackDestSlot", "field_146989_A");
			int l2 = returningStackDestSlot.xPos - touchUpX;
			int i3 = returningStackDestSlot.yPos - touchUpY;
			int l1 = touchUpX + (int) ((float) l2 * f);
			int i2 = touchUpY + (int) ((float) i3 * f);
			this.drawItemStack(ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "returningStack", "field_146991_C"), l1, i2, (String) null);
		}

		GlStateManager.popMatrix();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(I18n.format("gui.pedestal_interface"), 13, 10, 4210752);
		this.fontRenderer.drawString(this.mc.player.inventory.getDisplayName().getUnformattedText(), 13, this.ySize - 96 + 3, 4210752);
	}

	protected void drawSlot(Slot slotIn)
	{
		int i = slotIn.xPos;
		int j = slotIn.yPos;
		ItemStack itemstack = slotIn.getStack();
		boolean flag = false;
		ItemStack draggedStack = (ItemStack) ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "draggedStack", "field_147012_x");
		boolean isRightMouseClick = ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "isRightMouseClick", "field_147004_w");
		Slot clickedSlot = ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "clickedSlot", "field_147005_v");
		boolean flag1 = slotIn == clickedSlot && !draggedStack.isEmpty() && !isRightMouseClick;
		ItemStack itemstack1 = this.mc.player.inventory.getItemStack();
		int s = Integer.MIN_VALUE;
		String color = "";

		if (slotIn == clickedSlot && !draggedStack.isEmpty() && isRightMouseClick && !itemstack.isEmpty())
		{
			itemstack = itemstack.copy();
			itemstack.setCount(itemstack.getCount() / 2);
		}
		else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && !itemstack1.isEmpty())
		{
			if (this.dragSplittingSlots.size() == 1)
			{
				return;
			}

			if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn))
			{
				itemstack = itemstack1.copy();
				flag = true;
				Container.computeStackSize(this.dragSplittingSlots, ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "dragSplittingLimit", "field_146987_F"), itemstack,
						slotIn.getStack().isEmpty() ? 0 : slotIn.getStack().getCount());
				int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));

				if (itemstack.getCount() > k)
				{
					color = TextFormatting.YELLOW.toString();
					s = k;
					itemstack.setCount(k);
				}
			}
			else
			{
				this.dragSplittingSlots.remove(slotIn);
				this.updateDragSplitting();
			}
		}

		this.zLevel = 100.0F;
		this.itemRender.zLevel = 100.0F;

		if (itemstack.isEmpty() && slotIn.isEnabled())
		{
			TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();

			if (textureatlassprite != null)
			{
				GlStateManager.disableLighting();
				this.mc.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
				this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
				GlStateManager.enableLighting();
				flag1 = true;
			}
		}

		if (!flag1)
		{
			if (flag)
			{
				drawRect(i, j, i + 16, j + 16, -2130706433);
			}

			GlStateManager.enableDepth();
			this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, itemstack, i, j);
			if (s == Integer.MIN_VALUE)
			{
				s = itemstack.getCount();
			}
			String num = NumberUtils.getPrefixedNumber(s, 3);
			boolean unicodeFlag = this.fontRenderer.getUnicodeFlag();
			this.fontRenderer.setUnicodeFlag(slotIn.inventory instanceof InventoryPI || unicodeFlag);
			this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, i, j, s == 1 ? null : color + num);
			this.fontRenderer.setUnicodeFlag(unicodeFlag);
		}

		this.itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}

	protected void drawItemStack(ItemStack stack, int x, int y, String altText)
	{
		GlStateManager.translate(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		this.itemRender.zLevel = 200.0F;
		FontRenderer font = stack.getItem().getFontRenderer(stack);
		if (font == null)
			font = fontRenderer;
		this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		ItemStack draggedStack = (ItemStack) ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "draggedStack", "field_147012_x");
		this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (draggedStack.isEmpty() ? 0 : 8), altText);
		this.zLevel = 0.0F;
		this.itemRender.zLevel = 0.0F;
	}

	protected void updateDragSplitting()
	{
		ItemStack itemstack = this.mc.player.inventory.getItemStack();

		if (!itemstack.isEmpty() && this.dragSplitting)
		{
			if ((int) ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "dragSplittingLimit", "field_146987_F") == 2)
			{
				ServerUtils.setPrivateValue(GuiContainer.class, (GuiContainer) this, itemstack.getMaxStackSize(), "dragSplittingRemnant", "field_146996_I");
			}
			else
			{
				ServerUtils.setPrivateValue(GuiContainer.class, (GuiContainer) this, itemstack.getCount(), "dragSplittingRemnant", "field_146996_I");

				for (Slot slot : this.dragSplittingSlots)
				{
					ItemStack itemstack1 = itemstack.copy();
					ItemStack itemstack2 = slot.getStack();
					int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
					Container.computeStackSize(this.dragSplittingSlots, ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "dragSplittingLimit", "field_146987_F"), itemstack1, i);
					int j = Math.min(itemstack1.getMaxStackSize(), slot.getItemStackLimit(itemstack1));

					if (itemstack1.getCount() > j)
					{
						itemstack1.setCount(j);
					}

					ServerUtils.setPrivateValue(GuiContainer.class, (GuiContainer) this,
							(int) ServerUtils.getPrivateValue(GuiContainer.class, (GuiContainer) this, "dragSplittingRemnant", "field_146996_I") - (itemstack1.getCount() - i), "dragSplittingRemnant",
							"field_146996_I");
				}
			}
		}
	}
}
