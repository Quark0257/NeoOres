package neo_ores.client.render;

import neo_ores.main.NeoOresItems;
import neo_ores.tileentity.TileEntityChunkLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class RendererChunkLoader extends TileEntitySpecialRenderer<TileEntityChunkLoader>
{
	public void render(TileEntityChunkLoader te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		GlStateManager.pushMatrix();
		GlStateManager.popMatrix();
		ItemStack[] essences = new ItemStack[] { new ItemStack(NeoOresItems.essence, 1, 3), new ItemStack(NeoOresItems.essence, 1, 2), new ItemStack(NeoOresItems.essence, 1, 1),
				new ItemStack(NeoOresItems.essence, 1, 0) };

		if (te != null)
		{
			for (int i = 0; i < 4; i++)
			{
				EntityItem entityitem = null;
				float ticks = te.tickCount;
				GlStateManager.pushMatrix();
				GlStateManager.translate(x + 0.5F + 0.25F * Math.cos(Math.PI * 0.5D * i), (float) y - 0.35F + 0.125 * getHeight(essences[i].getMetadata()) + 0.03125F * (float) Math.sin(ticks / 10.0F) + 0.45F,
						(float) z + 0.5F + 0.25D * Math.sin(Math.PI * 0.5D * i));
				GlStateManager.scale(0.75D, 0.75D, 0.75D);
				GlStateManager.rotate(ticks % 360.0F, 0.0F, 1.0F, 0.0F);
				entityitem = new EntityItem(te.getWorld(), 0.0D, 0.0D, 0.0D, essences[i]);
				entityitem.hoverStart = 0.0F;
				RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
				rendermanager.renderEntity((Entity) entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, false);
				GlStateManager.popMatrix();
			}
		}
	}

	private static double getHeight(int i)
	{
		if (i == 2)
			return 3.25;
		if (i == 3)
			return 2;
		return i;
	}
}
