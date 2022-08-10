package neo_ores.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import neo_ores.api.SpellUtils;
import neo_ores.api.StudyItemManagerClient;
import neo_ores.api.spell.Spell;
import neo_ores.api.spell.SpellItem;
import neo_ores.api.spell.Spell.SpellConditional;
import neo_ores.api.spell.Spell.SpellCorrection;
import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.api.spell.Spell.SpellForm;
import neo_ores.inventory.ContainerSpellRecipeCreationTable;
import neo_ores.item.ISpellRecipeWritable;
import neo_ores.main.NeoOres;
import neo_ores.main.Reference;
import neo_ores.packet.PacketSRCTToServer;
import neo_ores.tileentity.TileEntitySpellRecipeCreationTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class GuiSpellRecipeCreationTable extends GuiContainer
{

	private static final ResourceLocation TEXTURES = new ResourceLocation("neo_ores:textures/gui/spell_recipe_creation_table.png");
    private final InventoryPlayer playerInventory;
    private final TileEntitySpellRecipeCreationTable tileSRCT;
    private String search = "";
    
    private boolean leftbuttondowning = false;
    private boolean lastleftbuttondowning = false;
    private List<SpellItem> selectedSpells = new ArrayList<SpellItem>();
    private List<SpellItem> matchedSpells = new ArrayList<SpellItem>();
    private StudyItemManagerClient simc;
    private int page = 0;
    private int maxPage = 0;
    private GuiTextField nameField;

    public GuiSpellRecipeCreationTable(InventoryPlayer playerInv, TileEntitySpellRecipeCreationTable srct)
    {
        super(new ContainerSpellRecipeCreationTable(playerInv, srct));
        this.playerInventory = playerInv;
        this.tileSRCT = srct;
        this.xSize = 395;
        this.ySize = 208;
    }

    public void initGui()
    {
    	super.initGui();
    	this.page = 0;
    	this.simc = new StudyItemManagerClient(this.mc.player);
    	this.search = this.tileSRCT.srctSearch;
    	this.selectedSpells = this.tileSRCT.getSpellItems();
    	this.changed();
    	this.buttonList.add(new GuiButton(101, (this.width - xSize) / 2 + 86, (this.height - ySize) / 2 + 148, 20, 20, ">")
		{
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
		    {
				boolean flag = this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				if(flag)
				{
					if(page < maxPage) page++;
					else page = 0;
					changed();
				}
				return flag;
		    }
		});
    	this.buttonList.add(new GuiButton(102, (this.width - xSize) / 2 + 8, (this.height - ySize) / 2 + 148, 20, 20, "<")
		{
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
		    {
				boolean flag = this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				if(flag)
				{
					if(0 < page) page--;
					else page = maxPage;
					changed();
				}
				return flag;
		    }
		});
    	
    	this.buttonList.add(new GuiButton(103, (this.width - xSize) / 2 + 8, (this.height - ySize) / 2 + 181, 100, 20, I18n.format("gui.srct.all_delete"))
		{
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
		    {
				boolean flag = this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				if(flag)
				{
					selectedSpells = new ArrayList<SpellItem>();
					changed();
				}
				return flag;
		    }
		});
    	
    	this.buttonList.add(new GuiButton(104, (this.width - xSize) / 2 + 284, (this.height - ySize) / 2 + 181, 100, 20, I18n.format("gui.srct.write"))
		{
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
		    {
				boolean flag = this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				if(flag)
				{
					if(tileSRCT.getStackInSlot(0).getItem() instanceof ISpellRecipeWritable)
					{
						((ISpellRecipeWritable)tileSRCT.getStackInSlot(0).getItem()).writeRecipeSpells(selectedSpells, tileSRCT.getStackInSlot(0));
						changed();
					}
				}
				return flag;
		    }
		});
    	this.nameField = new GuiTextField(0, this.fontRenderer, (this.width - xSize) / 2 + 9, (this.height - ySize) / 2 + 15, 96, 12);
    	this.nameField.setTextColor(-1);
        this.nameField.setDisabledTextColour(-1);
        this.nameField.setEnableBackgroundDrawing(true);
        this.nameField.setMaxStringLength(127);
    }
    
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (this.nameField.textboxKeyTyped(typedChar, keyCode))
        {
            this.search = this.nameField.getText();
            this.changed();
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
		
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        leftbuttondowning = Mouse.isButtonDown(0);
        
        if(!leftbuttondowning && lastleftbuttondowning) this.mouseLeftClicked(mouseX, mouseY);
        
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.draw(i, j);
        this.nameField.drawTextBox();
        this.renderHoveredToolTip(mouseX, mouseY);
        this.renderHoveredSpellToolTip(mouseX, mouseY);
        
        lastleftbuttondowning = leftbuttondowning;
    }
    
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
    }

	private void mouseLeftClicked(int mouseX, int mouseY)
	{
		int m = (this.width - this.xSize) / 2;
        int n = (this.height - this.ySize) / 2;
		SpellItem spellitem = this.getFromCoord(mouseX, mouseY);
		if(spellitem !=null)
		{
			if(m + 9 <= mouseX && mouseX < m + 9 + 96 && n + 30 <= mouseY && mouseY < n + 30 + 112 && this.canSelect(spellitem))
			{
				this.selectedSpells.add(spellitem);
			}
			else if(m + 117 <= mouseX && mouseX < m + 117 + 256 && n + 16 <= mouseY && mouseY < n + 16 + 96 && this.selectedSpells.contains(spellitem))
			{
				this.selectedSpells.remove(spellitem);
				List<SpellItem> removes = new ArrayList<SpellItem>();
				for(SpellItem item : this.selectedSpells)
				{
					if(!canSelected(item))
					{
						removes.add(item);
					}
				}
				this.selectedSpells.removeAll(removes);
			}
			this.changed();
		}
	}
	
	private boolean canSelect(SpellItem spell)
	{
		boolean flag = spell.getSpellClass() instanceof SpellForm || spell.getSpellClass() instanceof SpellConditional;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		boolean need = false;
		
		boolean conditional = false;
		boolean entityForm = false;
		boolean mainForm = false;
		for(SpellItem selected : this.selectedSpells)
		{
			if(selected.getSpellClass() instanceof SpellForm)
			{
				if(((SpellForm)selected.getSpellClass()).needConditional())
				{
					need = true;
				}
				
				if(((SpellForm)selected.getSpellClass()).needPrimaryForm())
				{
					entityForm = true;
				}
				
				if(!((SpellForm)selected.getSpellClass()).needPrimaryForm())
				{
					mainForm = true;
				}
			}
			
			if(selected.getSpellClass() instanceof SpellConditional)
			{
				conditional = true;
			}
		}
		
		if(conditional && need)
		{
			need = false;
		}
		
		if(entityForm && !mainForm)
		{
			return !this.selectedSpells.contains(spell) && flag && this.selectedSpells.size() <= 96;
		}
		
		for(SpellItem selected : this.selectedSpells)
		{
			if(spell.getSpellClass() instanceof SpellEffect)
			{
				if(selected.getSpellClass() instanceof SpellForm && !need)
				{
					flag = true;
					break;
				}
			}
			else if(spell.getSpellClass() instanceof SpellCorrection)
			{
				if(selected.getSpellClass() instanceof SpellEffect)
				{
					flag1 = true;
				}
				
				if(selected.getSpellClass() instanceof SpellForm)
				{
					flag2 = true;
				}
				
				SpellCorrection correction = ((SpellCorrection)spell.getSpellClass());
				if(correction.getLevel() <= 1)
				{
					flag3 = true;
				}
				else if(selected.getSpellClass() instanceof SpellCorrection)
				{
					SpellCorrection corr = ((SpellCorrection)selected.getSpellClass());
					if(corr.getClass() == correction.getClass() &&  corr.getLevel() == correction.getLevel() - 1)
					{
						flag3 = true;
					}
				}
				
				if(flag1 && flag2 && flag3 && !need)
				{
					flag = true;
					break;
				}
			}
		}
		
		return !this.selectedSpells.contains(spell) && flag && this.selectedSpells.size() <= 96;
	}
	
	private boolean canSelected(SpellItem spell)
	{
		boolean flag = spell.getSpellClass() instanceof SpellForm || spell.getSpellClass() instanceof SpellConditional;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		boolean need = false;
		
		boolean conditional = false;
		boolean entityForm = false;
		boolean mainForm = false;
		for(SpellItem selected : this.selectedSpells)
		{
			if(selected.getSpellClass() instanceof SpellForm)
			{
				if(((SpellForm)selected.getSpellClass()).needConditional())
				{
					need = true;
				}
				
				if(((SpellForm)selected.getSpellClass()).needPrimaryForm())
				{
					entityForm = true;
				}
				
				if(!((SpellForm)selected.getSpellClass()).needPrimaryForm())
				{
					mainForm = true;
				}
			}
			
			if(selected.getSpellClass() instanceof SpellConditional)
			{
				conditional = true;
			}
		}
		
		if(conditional && need)
		{
			need = false;
		}
		
		if(entityForm && !mainForm)
		{
			return flag && this.selectedSpells.size() <= 96;
		}
		
		for(SpellItem selected : this.selectedSpells)
		{
			if(spell.getSpellClass() instanceof SpellEffect)
			{
				if(selected.getSpellClass() instanceof SpellForm && !need)
				{
					flag = true;
					break;
				}
			}
			else if(spell.getSpellClass() instanceof SpellCorrection)
			{
				if(selected.getSpellClass() instanceof SpellEffect)
				{
					flag1 = true;
				}
				
				if(selected.getSpellClass() instanceof SpellForm)
				{
					flag2 = true;
				}
				
				SpellCorrection correction = ((SpellCorrection)spell.getSpellClass());
				if(correction.getLevel() <= 1)
				{
					flag3 = true;
				}
				else if(selected.getSpellClass() instanceof SpellCorrection)
				{
					SpellCorrection corr = ((SpellCorrection)selected.getSpellClass());
					if(corr.getClass() == correction.getClass() &&  corr.getLevel() == correction.getLevel() - 1)
					{
						flag3 = true;
					}
				}
				
				if(flag1 && flag2 && flag3 && !need)
				{
					flag = true;
					break;
				}
			}
		}
		
		return flag && this.selectedSpells.size() <= 96;
	}
	
	private void renderHoveredSpellToolTip(int mouseX, int mouseY)
	{
		if(this.getFromCoord(mouseX, mouseY) != null)
		{
			List<String> tooltip = new ArrayList<String>();
			SpellItem spellitem = this.getFromCoord(mouseX, mouseY);
			int m = (this.width - this.xSize) / 2;
	        int n = (this.height - this.ySize) / 2; 
			
			tooltip.add(TextFormatting.WHITE + getName(spellitem));
			
			tooltip.add(TextFormatting.GRAY + I18n.format("spell." + spellitem.getTranslateKey() + ".desc"));
			tooltip.add(TextFormatting.BLUE + I18n.format("spell.type") + " : " + SpellUtils.colorFromSpellItem(spellitem) + I18n.format(SpellUtils.typeFromSpellItem(spellitem)));
			tooltip.add(TextFormatting.BLUE + I18n.format("spell.cost") + " : +" + spellitem.getCostsum());
			tooltip.add(TextFormatting.BLUE + I18n.format("spell.cost") + " : x" + spellitem.getCostproduct());
			if(this.canSelect(spellitem)) 
			{
				tooltip.add("");
				tooltip.add(TextFormatting.GREEN + "" + TextFormatting.ITALIC + TextFormatting.UNDERLINE + I18n.format("spell.canClick"));
			}
			else if(m + 117 <= mouseX && mouseX < m + 117 + 256 && n + 16 <= mouseY && mouseY < n + 16 + 96 &&this.selectedSpells.contains(spellitem))
			{
				tooltip.add("");
				tooltip.add(TextFormatting.DARK_RED + "" + TextFormatting.ITALIC + TextFormatting.UNDERLINE + I18n.format("spell.canClickDelete"));
			}
	        this.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer);
		}
	}
	
	private SpellItem getFromCoord(int x,int y)
	{
		int m = (this.width - this.xSize) / 2;
        int n = (this.height - this.ySize) / 2;
        
        for(int j = 0;j < 7;j++)
		{
			for(int i = 0;i < 6;i++)
			{
				int number = this.page * 42 + j * 6 + i,posX = m + 9 + i * 16,posY = n + 30 + j * 16;
				if(number < matchedSpells.size())
				{
					SpellItem spellitem = matchedSpells.get(number);
					if(spellitem !=null && posX <= x && x < posX + 16 && posY <= y && y < posY + 16)
					{
						return spellitem;
					}
				}
			}
		}
		
		for(int j = 0;j < 6;j++)
		{
			for(int i = 0;i < 16;i++)
			{
				int number = j * 16 + i,posX = m + 117 + i * 16,posY = n + 16 + j * 16;
				
				if(number < this.selectedSpells.size())
				{
					SpellItem spellitem = this.selectedSpells.get(number);
					if(posX <= x && x < posX + 16 && posY <= y && y < posY + 16)
					{
						return spellitem;
					}
				}
		      	
			}
		}
        
        return null;
	}

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = this.tileSRCT.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 118, this.ySize - 96 + 3, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedWithTextureSizeModalRect(i, j, 0, 0, this.xSize, this.ySize,512,512);
    }
    
    private boolean matched(SpellItem spell)
    {
    	if(spell == null) return false;
    	String[] ss = this.search.split(" ");
    	boolean flag = true;
    	for(String s : ss)
    	{
    		if(!(spell.getModId().contains(s) || spell.getRegisteringId().contains(s) || getName(spell).contains(s) || I18n.format("spell." + spell.getTranslateKey() + ".desc").contains(s) || I18n.format(SpellUtils.typeFromSpellItem(spell)).contains(s)))
    		{
    			flag = false;
    			break;
    		}
    	}
    	return flag;
    }
    
    private void changed()
    {
		List<String> ids = new ArrayList<String>();
		List<SpellItem> sortedSpells = new ArrayList<SpellItem>();
		matchedSpells = new ArrayList<SpellItem>();
		
		for(SpellItem spellitem : SpellUtils.registry)
		{
			ids.add(spellitem.getModId() + ":" + spellitem.getRegisteringId());
		}
		
		ids.sort(String.CASE_INSENSITIVE_ORDER);
		for(String id : ids)
		{
			String[] ss = id.split(":",2);
			if(ss.length == 2)
			{
				SpellItem spellitem = SpellUtils.getFromID(ss[0], ss[1]);
				if(spellitem != null && this.simc.didGet(ss[0], ss[1]))
				{
					sortedSpells.add(spellitem);
				}
			}
		}
		
		
		for(SpellItem spellitem : sortedSpells)
		{
			 if(this.matched(spellitem))
			 {
				 matchedSpells.add(spellitem);
			 }
		}
		this.tileSRCT.srctSearch = this.search;
		this.tileSRCT.setSpellItems(this.selectedSpells);
		this.maxPage = this.matchedSpells.size() / 42;
		if(this.maxPage < page)
		{
			page = this.maxPage;
		}
		
		if(this.page < 0)
		{
			this.page = 0;
		}
		//this.page = 0;
		
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("x", tileSRCT.getPos().getX());
		nbt.setInteger("y", tileSRCT.getPos().getY());
		nbt.setInteger("z", tileSRCT.getPos().getZ());
		nbt.setTag("recipeSpells", SpellUtils.getNBTFromList(selectedSpells));
		NBTTagCompound item = tileSRCT.getStackInSlot(0).writeToNBT(new NBTTagCompound());
		nbt.setTag("recipeItem", item);
		
		PacketSRCTToServer psrcts = new PacketSRCTToServer(nbt);
		NeoOres.PACKET.sendToServer(psrcts);
    }

	public void draw(int x, int y)
    {	
		for(int j = 0;j < 7;j++)
		{
			for(int i = 0;i < 6;i++)
			{
				int number = this.page * 42 + j * 6 + i,posX = i * 16,posY = j * 16;
				if(number < matchedSpells.size())
				{
					SpellItem spellitem = matchedSpells.get(number);
					this.drawSpell(x + 9 + posX,y + 30 + posY, spellitem, !this.canSelect(spellitem));
				}
			}
		}
		
		for(int j = 0;j < 6;j++)
		{
			for(int i = 0;i < 16;i++)
			{
				int number = j * 16 + i,posX = i * 16,posY = j * 16;
				
				if(number < this.selectedSpells.size())
				{
					SpellItem spellitem = this.selectedSpells.get(number);
					this.drawSpell(x + 117 + posX, y + 16 + posY, spellitem, false);
				}
		      	
			}
		}
    }
	
	private void drawSpell(int x,int y,SpellItem spell,boolean inactive)
	{
		this.mc.getTextureManager().bindTexture(SpellUtils.textureFromSpellItem(spell));
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		this.drawTexturedWithTextureSizeAndScaleModalRect(x, y, 0, 0, 16, 16,64,64,0.25F);
		RenderHelper.enableGUIStandardItemLighting();
		
		if(spell.getSpellClass() instanceof Spell.SpellCorrection)
		{
			SpellCorrection correction = (SpellCorrection)spell.getSpellClass();
			if(correction.getLevel() != 0)
			{
				this.mc.getTextureManager().bindTexture(new ResourceLocation(Reference.MOD_ID,"textures/gui/spell/" + "correction." + correction.getLevel() + ".png"));
        		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        		GlStateManager.enableBlend();
        		this.drawTexturedWithTextureSizeAndScaleModalRect(x + SpellUtils.offsetX(spell) / 2, y + SpellUtils.offsetY(spell) / 2, 0, 0, 8, 8,16,16,0.5F);
        		RenderHelper.enableGUIStandardItemLighting();
			}
		}
		
		this.mc.getTextureManager().bindTexture(new ResourceLocation(spell.getTexturePath().getResourceDomain(),"textures/gui/spellitems/" + spell.getTexturePath().getResourcePath() + ".png"));
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		this.drawTexturedWithTextureSizeAndScaleModalRect(x + SpellUtils.offsetX(spell) / 2, y + SpellUtils.offsetY(spell) / 2, 0, 0, 8, 8,16,16,0.5F);
		RenderHelper.enableGUIStandardItemLighting();
		
		if(inactive)
		{
			this.mc.getTextureManager().bindTexture(SpellUtils.textureFromSpellItemInactive(spell));
    		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    		GlStateManager.enableBlend();
    		this.drawTexturedWithTextureSizeAndScaleModalRect(x, y, 0, 0, 16, 16,64,64,0.25F);
    		RenderHelper.enableGUIStandardItemLighting();
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
	
	private static String getName(SpellItem spellitem)
	{
		if(spellitem.getSpellClass() instanceof Spell.SpellCorrection)
		{
			SpellCorrection correction = (SpellCorrection)spellitem.getSpellClass();
			return I18n.format("spell." + spellitem.getTranslateKey() + ".name") + I18n.format("correction." + correction.getLevel());
		}
		else
		{
			return I18n.format("spell." + spellitem.getTranslateKey() + ".name");
		}
	}
}
