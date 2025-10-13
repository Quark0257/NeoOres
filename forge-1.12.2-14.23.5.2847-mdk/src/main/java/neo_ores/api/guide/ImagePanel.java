package neo_ores.api.guide;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.Vec2I;
import neo_ores.client.gui.GuiGuidebook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ImagePanel extends AbstractPageComponent
{
	protected final ResourceLocation texture;
	protected final int textureWidth;
	protected final int textureHeight;

	public ImagePanel(int x, int y, int width, int height, ResourceLocation texture, int textureWidth, int textureHeight)
	{
		super(x, y, width, height);
		this.texture = texture;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
	}

	@Override
	public List<ComponentHover> drawScreen(GuiGuidebook guide, int mouseX, int mouseY, float partialTicks, boolean isMouseLeftClicked, Vec2I draggedMouseDelta, boolean mouseDragging)
	{
		List<ComponentHover> list = new ArrayList<>();
		GlStateManager.enableDepth();
		GlStateManager.enableBlend();
		guide.mc.getTextureManager().bindTexture(this.texture);
		guide.drawTexturedWithTextureSizeAndScaleModalRect(this.posX, this.posY, 0, 0, this.width, this.height, this.textureWidth, this.textureHeight,
				Math.min((float) this.width / (float) this.textureWidth, (float) this.height / (float) this.textureHeight));
		GlStateManager.disableDepth();
		return list;
	}
}
