package neo_ores.client.gui;

import neo_ores.inventory.ContainerMechanicalMagician;
import neo_ores.tileentity.TileEntityMechanicalMagician;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiMechanicalMagician extends GuiContainer
{
	private static final ResourceLocation MM_GUI_TEXTURES = new ResourceLocation("neo_ores:textures/gui/mechanical_magician.png");
	private static final ResourceLocation FM_GUI_TEXTURES = new ResourceLocation("neo_ores:textures/blocks/liquid_mana_overlay.png");
	private final InventoryPlayer playerInventory;
	private final TileEntityMechanicalMagician temm;
	public GuiMechanicalMagician(InventoryPlayer playerInv, TileEntityMechanicalMagician tileentity)
	{
		super(new ContainerMechanicalMagician(playerInv, tileentity));
		this.playerInventory = playerInv;
		this.temm = tileentity;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String s = this.temm.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(MM_GUI_TEXTURES);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		
		if(this.isShowFluid()) 
		{
			this.drawTexturedModalRect(i + 8, j + 9, 192, 0, 16, 62);
			this.mc.getTextureManager().bindTexture(FM_GUI_TEXTURES);
			int maxPixels = this.getFluidProgress(60);
			int n = 0;
			for(n = 0;n < maxPixels / 16;n++) {
				this.drawTexturedModalRect(i + 8, j + 70 - 16 * n - 16, 0, 0, 16, 16);
			}
			this.drawTexturedModalRect(i + 8, j + 70 - 16 * n - (maxPixels - 16 * n), 0, 16 - (maxPixels - 16 * n), 16, maxPixels - 16 * n);
			
			this.mc.getTextureManager().bindTexture(MM_GUI_TEXTURES);
			this.drawTexturedModalRect(i + 8, j + 9, 176, 0, 16, 62);
		}
	}
	
	private int getFluidProgress(int pixels)
	{
		int i = this.temm.getField(0);
		int j = TileEntityMechanicalMagician.TANK_CAP;
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
	
	private boolean isShowFluid() 
	{
		return this.temm.getField(1) == 1;
	}
}
