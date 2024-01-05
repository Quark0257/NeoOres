package neo_ores.client.gui;

import java.io.IOException;

import neo_ores.api.LongUtils;
import neo_ores.inventory.ContainerManaWorkbench;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiManaWorkbench extends GuiContainer implements IContainerListener
{
	private static final ResourceLocation RESOURCE = new ResourceLocation("neo_ores:textures/gui/mana_workbench.png");
	private final ContainerManaWorkbench mana_workbench;
	private final InventoryPlayer playerInventory;

	public GuiManaWorkbench(InventoryPlayer inventoryIn, World worldIn)
	{
		super(new ContainerManaWorkbench(inventoryIn, worldIn, Minecraft.getMinecraft().player));
		this.playerInventory = inventoryIn;
		this.mana_workbench = (ContainerManaWorkbench) this.inventorySlots;
	}

	public void initGui()
	{
		super.initGui();
		this.inventorySlots.removeListener(this);
		this.inventorySlots.addListener(this);
	}

	public void onGuiClosed()
	{
		super.onGuiClosed();
		this.inventorySlots.removeListener(this);
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
		this.fontRenderer.drawString(I18n.format("container.mana_workbench"),
				(this.xSize - this.fontRenderer.getStringWidth(I18n.format("container.mana_workbench"))) / 2, 6,
				4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2,
				4210752);

		if (this.mana_workbench.cost > 0)
		{
			int i = 32255;
			boolean flag = true;
			String s = I18n.format("container.mana_workbench.cost") + " : "
					+ LongUtils.convertString(this.mana_workbench.cost);

			if (!this.mana_workbench.getSlot(9).getHasStack())
			{
				flag = false;
			}
			else if (!this.mana_workbench.getSlot(9).canTakeStack(this.playerInventory.player))
			{
				i = 16736352;
			}

			if (flag)
			{
				int j = -16777216 | (i & 16579836) >> 2 | i & -16777216;
				int k = this.xSize - 34 - this.fontRenderer.getStringWidth(s);
				int l = 64;

				if (this.fontRenderer.getUnicodeFlag())
				{
					drawRect(k - 3, l - 2, k + this.fontRenderer.getStringWidth(s) + 3, l + 10, -16777216);
					drawRect(k - 2, l - 1, k + 2 + this.fontRenderer.getStringWidth(s), l + 9, -12895429);
				}
				else
				{
					this.fontRenderer.drawString(s, k, l + 1, j);
					this.fontRenderer.drawString(s, k + 1, l, j);
					this.fontRenderer.drawString(s, k + 1, l + 1, j);
				}

				this.fontRenderer.drawString(s, k, l, i);
			}
		}

		GlStateManager.enableLighting();
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(RESOURCE);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		if (this.mana_workbench.getSlot(9).getHasStack()
				&& !this.mana_workbench.getSlot(9).canTakeStack(this.playerInventory.player))
		{
			this.drawTexturedModalRect(i + 83, j + 33, 176, 0, 28, 21);
		}
	}

	public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList)
	{
	}

	public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
	{
	}

	public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue)
	{
	}

	public void sendAllWindowProperties(Container containerIn, IInventory inventory)
	{
	}
}
