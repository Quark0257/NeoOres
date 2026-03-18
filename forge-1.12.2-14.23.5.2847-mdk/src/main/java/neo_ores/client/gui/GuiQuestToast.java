package neo_ores.client.gui;

import java.util.List;

import neo_ores.api.PlayerTrigger;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiQuestToast implements IToast
{
	private final PlayerTrigger trigger;
	
	public GuiQuestToast(PlayerTrigger trigger) 
	{
		this.trigger = trigger;
	}
	

	@Override
	public Visibility draw(GuiToast toastGui, long delta)
	{
		toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        toastGui.drawTexturedModalRect(0, 0, 0, 0, 160, 32);
        int margin = 7;
        
        List<String> list = toastGui.getMinecraft().fontRenderer.listFormattedStringToWidth(I18n.format(this.trigger.getTranslateKey()), 160 - margin * 2);
        int i = 16776960;

        if (list.size() == 1)
        {
            toastGui.getMinecraft().fontRenderer.drawString(I18n.format("neo_ores.toast.quest_complete"), margin, 7, i | -16777216);
            toastGui.getMinecraft().fontRenderer.drawString(I18n.format(this.trigger.getTranslateKey()), margin, 18, -1);
        }
        else
        {
            int j = 1500;
            float f = 300.0F;

            if (delta < j)
            {
                int k = MathHelper.floor(MathHelper.clamp((float)(j - delta) / f, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                toastGui.getMinecraft().fontRenderer.drawString(I18n.format("neo_ores.toast.quest_complete"), margin, 11, i | k);
            }
            else
            {
                int i1 = MathHelper.floor(MathHelper.clamp((float)(delta - j) / f, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                int l = 16 - list.size() * toastGui.getMinecraft().fontRenderer.FONT_HEIGHT / 2;

                for (String s : list)
                {
                    toastGui.getMinecraft().fontRenderer.drawString(s, margin, l, 16777215 | i1);
                    l += toastGui.getMinecraft().fontRenderer.FONT_HEIGHT;
                }
            }
        }

        // RenderHelper.enableGUIStandardItemLighting();
        // toastGui.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase)null, displayinfo.getIcon(), 8, 8);
        return delta >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
	}
}
