package neo_ores.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import neo_ores.gui.GuiUtils.StudyTableUtils;
import neo_ores.main.NeoOres;
import neo_ores.main.Reference;
import neo_ores.mana.PlayerManaDataClient;
import neo_ores.spell.ISpell.ISpellCorrection;
import neo_ores.spell.SpellItem;
import neo_ores.spell.SpellType;
import neo_ores.spell.SpellItemType;
import neo_ores.spell.SpellUtils;
import neo_ores.study.StudyItemManagerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.advancements.AdvancementState;
import net.minecraft.client.gui.advancements.GuiAdvancement;
import net.minecraft.client.gui.advancements.GuiAdvancementTab;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiStudyTable extends GuiScreen
{	
	private StudyTabs tab;
	
	private int scrollMouseX;
    private int scrollMouseY;
    private boolean isScrolling = false;
    private int scrollX = 0;
    private int scrollY = 0;
    private int minX = Integer.MAX_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int maxY = Integer.MIN_VALUE;
    @SuppressWarnings("unused")
	private boolean centered;
    private final Minecraft mc;
    static final int windowSizeX = 395;
    static final int windowSizeY = 208;
    static final int insideSizeX = 379;
    static final int insideSizeY = 188;
    static final int interval = 48;
    private StudyItemManagerClient simc;
    private PlayerManaDataClient pmdc;
	
	public GuiStudyTable()
	{
		this.mc = Minecraft.getMinecraft();
		this.tab = NeoOres.neo_ores;
		this.centered = false;
		this.setXYPosition();
	}
	
	public void initGui()
    {
    }
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
		this.simc = new StudyItemManagerClient(this.mc.player);
		this.pmdc = new PlayerManaDataClient(this.mc.player);
		
        int i = (this.width - windowSizeX) / 2;
        int j = (this.height - (windowSizeY + 14)) / 2;

        if (Mouse.isButtonDown(1))
        {
        	//System.out.println(this.scrollX + ":" + this.scrollY);
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
            this.isScrolling = false;
        }

        this.drawDefaultBackground();
        this.renderInside(mouseX, mouseY, i, j);
        this.renderWindow(i, j);
        this.drawTooltip(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
	
	public void scroll(int x, int y)
    {
        if (this.maxX - this.minX > insideSizeX)
        {
            this.scrollX = MathHelper.clamp(this.scrollX + x, -this.maxX + insideSizeX, -this.minX);
        }

        if (this.maxY - this.minY > insideSizeY)
        {
            this.scrollY = MathHelper.clamp(this.scrollY + y, -this.maxY + insideSizeY, -this.minY);
        }
    }
	
	private void renderInside(int p_191936_1_, int p_191936_2_, int p_191936_3_, int p_191936_4_)
    {
		GlStateManager.pushMatrix();
        GlStateManager.translate((float)(p_191936_3_ + 9), (float)(p_191936_4_ + 18), -400.0F);
        GlStateManager.enableDepth();
        this.drawContents();
        GlStateManager.popMatrix();
        GlStateManager.depthFunc(515);
        GlStateManager.disableDepth();
    }
	
	public void drawContents()
    {
		/*
        if (!this.centered)
        {
            this.scrollX = insideSizeX / 2 - (this.maxX + this.minX) / 2 - 32;
            this.scrollY = insideSizeY / 2 - (this.maxY + this.minY) / 2 - 32;
            this.centered = true;
        }
        */

        GlStateManager.depthFunc(518);
        drawRect(0,0, insideSizeX, insideSizeY, -16777216);
        GlStateManager.depthFunc(515);

        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/options_background.png"));

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.scrollX % 16;
        int j = this.scrollY % 16;

        for (int k = -1; k <= 24; ++k)
        {
            for (int l = -1; l <= 12; ++l)
            {
            	this.drawTexturedWithTextureSizeModalRect(i + 16 * k, j + 16 * l,0,0,16,16,16,16);   
            }
        }

        this.drawConnectivity(this.scrollX, this.scrollY, true);
        this.drawConnectivity(this.scrollX, this.scrollY, false);
        this.draw(this.scrollX, this.scrollY);
    }
	
	public void drawConnectivity(int x, int y, boolean isWide)
    {
        for(SpellItem spellitem : SpellUtils.registry)
        {
        	if(spellitem.getTab() == this.tab && spellitem.getParent() != null)
        	{
        		int startX = spellitem.getPositionX() * interval + x + 16;
        		int endX = spellitem.getParent().getPositionX() * interval + x + 16;
        		int startY = spellitem.getPositionY() * interval + y + 16;
        		int endY = spellitem.getParent().getPositionY() * interval + y + 16;
        		if(isWide)
        		{
        			this.drawLine(startX, endX, startY, endY, 0x000000);
        			this.drawLine(startX + 1, endX + 1, startY, endY, 0x000000);
        			this.drawLine(startX - 1, endX - 1, startY, endY, 0x000000);
        			this.drawLine(startX, endX, startY + 1, endY + 1, 0x000000);
        			this.drawLine(startX, endX, startY - 1, endY - 1, 0x000000);
        		}
        		else
        		{
        			if(this.canGetSpellItemByMagicPoint(spellitem, this.mc.player) && this.canGetSpellItemByTree(spellitem, this.mc.player))
        			{
        				this.drawLine(startX, endX, startY, endY, 0x0000FF);
        			}
        			else if(this.simc.didGet(spellitem.getModId(), spellitem.getRegisteringId()))
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
	
	private boolean isMouseInsideWindow(int x,int y)
	{
		int i = (this.width - windowSizeX) / 2;
        int j = (this.height - windowSizeY) / 2;
        int a = i + 8;
        int b = i + windowSizeX - 8;
        int c = j + 32;
        int d = j + windowSizeY - 8;
		return (a < x && x < b && c < y && y < d);
	}
	
	@SuppressWarnings("static-access")
	private void drawTooltip(int mouseX,int mouseY)
	{
		GuiUtils.StudyTableUtils stUtils = StudyTableUtils.set(tab);
		if(this.isMouseInsideWindow(mouseX, mouseY) && stUtils.getSpell(mouseX, mouseY, this.scrollX, this.scrollY,this.width,this.height) != null)
		{
			List<String> tooltip = new ArrayList<String>();
			SpellItem spellitem = stUtils.getSpell(mouseX, mouseY, this.scrollX, this.scrollY,this.width,this.height);
			if(spellitem.getSpellClass().getSpellItemType() == SpellType.CORRECTION)
			{
				ISpellCorrection correction = (ISpellCorrection)spellitem.getSpellClass();
				tooltip.add(TextFormatting.WHITE + I18n.format("spell." + spellitem.getTranslateKey() + ".name") + " " + I18n.format("correction." + correction.getLevel()));
			}
			else
			{
				tooltip.add(TextFormatting.WHITE + I18n.format("spell." + spellitem.getTranslateKey() + ".name"));
			}
			
			tooltip.add(TextFormatting.WHITE + I18n.format("spell." + spellitem.getTranslateKey() + ".desc"));
			tooltip.add(TextFormatting.BLUE + I18n.format("spell.type") + ":" + SpellUtils.colorFromSpellItem(spellitem) + SpellUtils.typeFromSpellItem(spellitem));
			tooltip.add(TextFormatting.BLUE + I18n.format("spell.cost_product") + ":" + spellitem.getCostproduct());
			tooltip.add(TextFormatting.BLUE + I18n.format("spell.cost_sum") + ":" + spellitem.getCostsum());
			
			if(this.simc.didGet(spellitem.getModId(), spellitem.getRegisteringId()))
			{
				tooltip.add(TextFormatting.BOLD + (TextFormatting.GREEN + I18n.format("spell.avalable")));
			}
			else
			{
				if(!this.canGetSpellItemByMagicPoint(spellitem, this.mc.player))
				{
					tooltip.add((TextFormatting.BOLD + (TextFormatting.DARK_RED + I18n.format("spell.required") + ":")) + TextFormatting.BLUE + spellitem.getTier() + " " + I18n.format("spell.magic_point"));
				}
				
				if(!this.canGetSpellItemByTree(spellitem, this.mc.player))
				{
					tooltip.add((TextFormatting.BOLD + (TextFormatting.DARK_RED + I18n.format("spell.required") + ":")) + TextFormatting.BLUE + I18n.format("spell." + spellitem.getParent().getTranslateKey() + ".name"));
				}
			}
			
	        this.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer);
		}
	}
	
	public void drawLine(int startX,int endX,int startY,int endY,int color)
	{
		int minX;
		int maxX;
		int minY;
		int maxY;
		if(startX < endX)
		{
			minX = startX;
			maxX = endX;
		}
		else
		{
			minX = endX;
			maxX = startX;
		}
		if(startY < endY)
		{
			minY = startY;
			maxY = endY;
		}
		else
		{
			minY = endY;
			maxY = startY;
		}

		if(minX == maxX)
		{
			this.drawVerticalLine(minX, minY, maxY, color - 0x1000000);
			return;
		}
		
		if(minY == maxY)
		{
			this.drawHorizontalLine(minX, maxX, minY, color - 0x1000000);
			return;
		}
		
		float m = (float)(maxY-minY)/(float)(maxX-minX);
		if(m < 1.0F)
		{
			m = 1.0F / m;
			for(int i = 0;i < maxY - minY;i++)
			{
				this.drawHorizontalLine(minX + (int)(i * m) - 1, minX + (int)((i + 2) * m), minY + i, color - 0x1000000);
			}
		}
		else
		{
			for(int i = 0;i < maxX - minX;i++)
			{
				this.drawVerticalLine(minX + i, minY + (int)(i * m) - 1, minY + (int)((i + 1) * m + 1), color - 0x1000000);
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public void draw(int x, int y)
    {
        for(SpellItem spellitem : SpellUtils.registry)
        {
        	if(spellitem.getTab() == this.tab)
        	{
        		this.mc.getTextureManager().bindTexture(SpellUtils.textureFromSpellItem(spellitem));
        		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        		GlStateManager.enableBlend();
        		this.drawTexturedWithTextureSizeAndScaleModalRect(x + spellitem.getPositionX() * interval, y + spellitem.getPositionY() * interval, 0, 0, 32, 32,64,64,0.5F);
        		RenderHelper.enableGUIStandardItemLighting();
        		
        		if(spellitem.getSpellClass().getSpellItemType() == SpellType.CORRECTION)
    			{
    				ISpellCorrection correction = (ISpellCorrection)spellitem.getSpellClass();
    				if(correction.getLevel() != 0)
    				{
    					this.mc.getTextureManager().bindTexture(new ResourceLocation(Reference.MOD_ID,"textures/gui/study_table/" + correction.getLevel() + ".png"));
                		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                		GlStateManager.enableBlend();
                		this.drawTexturedWithTextureSizeModalRect(x + spellitem.getPositionX() * interval + SpellUtils.offsetX(spellitem), y + spellitem.getPositionY() * interval + SpellUtils.offsetY(spellitem), 0, 0, 16, 16,16,16);
                		RenderHelper.enableGUIStandardItemLighting();
    				}
    			}
        		
        		this.mc.getTextureManager().bindTexture(new ResourceLocation(spellitem.getTexturePath().getResourceDomain(),"textures/spellitems/" + spellitem.getTexturePath().getResourcePath() + ".png"));
        		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        		GlStateManager.enableBlend();
        		this.drawTexturedWithTextureSizeModalRect(x + spellitem.getPositionX() * interval + SpellUtils.offsetX(spellitem), y + spellitem.getPositionY() * interval + SpellUtils.offsetY(spellitem), 0, 0, 16, 16,16,16);
        		RenderHelper.enableGUIStandardItemLighting();
        		
        		if(this.canGetSpellItemByMagicPoint(spellitem, this.mc.player) && this.canGetSpellItemByTree(spellitem, this.mc.player))
        		{
        			this.mc.getTextureManager().bindTexture(SpellUtils.textureFromSpellItemInactive(spellitem));
            		GlStateManager.color(1.0F, 1.0F, 1.0F, (float)Math.sin(this.mc.getSystemTime() / 100.0D) / 2.5F + 0.6F);
            		GlStateManager.enableBlend();
            		this.drawTexturedWithTextureSizeAndScaleModalRect(x + spellitem.getPositionX() * interval, y + spellitem.getPositionY() * interval, 0, 0, 32, 32,64,64,0.5F);
            		RenderHelper.enableGUIStandardItemLighting();
        		}
        		else if(!this.simc.didGet(spellitem.getModId(), spellitem.getRegisteringId()))
        		{
        			this.mc.getTextureManager().bindTexture(SpellUtils.textureFromSpellItemInactive(spellitem));
            		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            		GlStateManager.enableBlend();
            		this.drawTexturedWithTextureSizeAndScaleModalRect(x + spellitem.getPositionX() * interval, y + spellitem.getPositionY() * interval, 0, 0, 32, 32,64,64,0.5F);
            		RenderHelper.enableGUIStandardItemLighting();
        		}
        	}
        }
    }
	
	public void renderWindow(int x, int y)
    {
		RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Reference.MOD_ID,"textures/gui/study_table_window.png"));
        this.drawTexturedWithTextureSizeModalRect(x, y, 0, 0, windowSizeX, windowSizeY,512,512);
        this.fontRenderer.drawString(I18n.format("gui.advancements"), x + 8, y + 6, 4210752);
    }
	
	public boolean doesGuiPauseGame()
	{
	    return false;
	}
	
	@SuppressWarnings("static-access")
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		GuiUtils.StudyTableUtils stUtils = StudyTableUtils.set(tab);
		if(mouseButton == 0 && this.isMouseInsideWindow(mouseX, mouseY) && stUtils.getSpell(mouseX, mouseY, this.scrollX, this.scrollY,this.width,this.height) != null);
		{
			SpellItem spellitem = stUtils.getSpell(mouseX, mouseY, this.scrollX, this.scrollY,this.width,this.height);
			if(this.canGetSpellItemByMagicPoint(spellitem, this.mc.player) && this.canGetSpellItemByTree(spellitem, this.mc.player))
			{
				this.pmdc.addMagicPoint((long)-spellitem.getTier());
				this.simc.set(spellitem.getModId(), spellitem.getRegisteringId());
			}
		}
	}
	
	public void drawTexturedWithTextureSizeModalRect(int x, int y, int textureX, int textureY, int width, int height,float textureWidth,float textureHeight)
    {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)(x + 0), (double)(y + height), (double)this.zLevel).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), (double)this.zLevel).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + 0), (double)this.zLevel).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        bufferbuilder.pos((double)(x + 0), (double)(y + 0), (double)this.zLevel).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        tessellator.draw();
    }
	
	public void drawTexturedWithTextureSizeAndScaleModalRect(int x, int y, int textureX, int textureY, int width, int height,float textureWidth,float textureHeight,float scale)
    {
        float f = 1.0F / (textureWidth * scale);
        float f1 = 1.0F / (textureHeight * scale);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)(x + 0), (double)(y + height), (double)this.zLevel).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), (double)this.zLevel).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + 0), (double)this.zLevel).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        bufferbuilder.pos((double)(x + 0), (double)(y + 0), (double)this.zLevel).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        tessellator.draw();
    }
	
	public void setXYPosition()
	{
		for(SpellItem spellitem : SpellUtils.registry)
		{
			if(spellitem.getTab() == this.tab)
			{
				this.minX = Math.min(this.minX, spellitem.getPositionX() * interval - 16);
				this.maxX = Math.max(this.maxX, spellitem.getPositionX() * interval + 48);
				this.minY = Math.min(this.minY, spellitem.getPositionY() * interval - 24);
				this.maxY = Math.max(this.maxY, spellitem.getPositionY() * interval + 48);
			}
		}
		this.scrollX = -(this.minX + this.maxX - insideSizeX) / 2;
		this.scrollY = -(this.minY + this.maxY - insideSizeY) / 2;
	}
	
	private boolean canGetSpellItemByTree(SpellItem spellitem,EntityPlayerSP player)
	{
		StudyItemManagerClient simc = new StudyItemManagerClient(player);
		return (spellitem.getParent() != null) ? simc.canGet(spellitem.getParent().getModId(), spellitem.getParent().getRegisteringId(), spellitem.getModId(), spellitem.getRegisteringId()) : simc.canGetRoot(spellitem.getModId(), spellitem.getRegisteringId());
	}
	
	private boolean canGetSpellItemByMagicPoint(SpellItem spellitem,EntityPlayerSP player)
	{
		PlayerManaDataClient pmdc = new PlayerManaDataClient(player);
		if(spellitem == null) return false;
		return pmdc.getMagicPoint() >= (long)spellitem.getTier();
	}
	
	public void updateScreen()
    {
        super.updateScreen();

        if (!this.mc.player.isEntityAlive() || this.mc.player.isDead)
        {
            this.mc.player.closeScreen();
        }
    }
}
