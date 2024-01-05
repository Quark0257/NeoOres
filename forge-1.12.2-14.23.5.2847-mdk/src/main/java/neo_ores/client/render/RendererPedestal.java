package neo_ores.client.render;

import neo_ores.tileentity.AbstractTileEntityPedestal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class RendererPedestal extends TileEntitySpecialRenderer<AbstractTileEntityPedestal>
{
	public void render(AbstractTileEntityPedestal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		GlStateManager.pushMatrix();
		GlStateManager.popMatrix();
		ItemStack itemstack = te.getDisplay().copy();
		itemstack.setCount(1);

		if (te != null && !itemstack.isEmpty())
		{
			EntityItem entityitem = null;
			float ticks = te.tickCount;
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) x + 0.5F, (float) y + 0.3125F + 0.03125F * (float) Math.sin(ticks / 10.0F) + ((te.offset < -0.4325D) ? (float) te.offset + 0.8125F : (float) te.offset),
					(float) z + 0.5F);
			GlStateManager.scale(1.25D, 1.25D, 1.25D);
			GlStateManager.rotate(ticks % 360.0F, 0.0F, 1.0F, 0.0F);
			if (te.offset < -0.4375D)
				GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
			entityitem = new EntityItem(te.getWorld(), 0.0D, 0.0D, 0.0D, itemstack);
			entityitem.hoverStart = 0.0F;
			RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
			rendermanager.renderEntity((Entity) entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, false);
			GlStateManager.popMatrix();
		}
	}
}
