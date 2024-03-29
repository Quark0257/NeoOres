package neo_ores.client.gui;

import neo_ores.inventory.ContainerManaFurnace;
import neo_ores.tileentity.TileEntityManaFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiManaFurnace extends GuiContainer
{
	private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation("neo_ores:textures/gui/mana_furnace.png");
	private final InventoryPlayer playerInventory;
	private final TileEntityManaFurnace tileManaFurnace;

	public GuiManaFurnace(InventoryPlayer playerInv, TileEntityManaFurnace manaFurnace)
	{
		super(new ContainerManaFurnace(playerInv, manaFurnace));
		this.playerInventory = playerInv;
		this.tileManaFurnace = manaFurnace;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String s = this.tileManaFurnace.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

		if (TileEntityManaFurnace.isBurning(this.tileManaFurnace))
		{
			int k = this.getBurnLeftScaled(13);
			this.drawTexturedModalRect(i + 75, j + 54 + 12 - k, 176, 12 - k, 14, k + 1);
		}

		int l = this.getCookProgressScaled(24);
		this.drawTexturedModalRect(i + 97, j + 34, 176, 14, l + 1, 16);
	}

	private int getCookProgressScaled(int pixels)
	{
		int i = this.tileManaFurnace.getField(2);
		int j = this.tileManaFurnace.getField(3);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}

	private int getBurnLeftScaled(int pixels)
	{
		int i = this.tileManaFurnace.getField(1);

		if (i == 0)
		{
			i = 200;
		}

		return this.tileManaFurnace.getField(0) * pixels / i;
	}
}
