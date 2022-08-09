package neo_ores.client.gui;

import javax.annotation.Nullable;

import neo_ores.api.SpellUtils;
import neo_ores.api.StudyItemManagerClient;
import neo_ores.api.spell.Spell;
import neo_ores.api.spell.SpellItem;
import neo_ores.api.spell.KnowledgeTab;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiUtils 
{
	public static class StudyTableUtils
	{
		public static KnowledgeTab tab;
		
		private StudyTableUtils(KnowledgeTab tab)
		{
			StudyTableUtils.tab = tab;
		}
		
		public static StudyTableUtils set(KnowledgeTab tab)
		{
			return new StudyTableUtils(tab);
		}
		
		public static boolean canPlayerGetSpell(SpellItem spellitem,EntityPlayerSP player)
		{
			StudyItemManagerClient simc = new StudyItemManagerClient(player);
			if(spellitem.getParent() == null)
			{
				return simc.canGetRoot(spellitem.getModId(), spellitem.getRegisteringId());
			}
			
			return simc.canGet(spellitem.getParent().getModId(), spellitem.getParent().getRegisteringId(), spellitem.getModId(), spellitem.getRegisteringId());
		}
		
		@Nullable
		public static SpellItem getSpell(int mouseX,int mouseY,int scrolledX,int scrolledY,int width,int height)
		{
			int X = mouseX - scrolledX - (width - GuiMageKnowledgeTable.insideSizeX) / 2;//-24
			int Y = mouseY - scrolledY - (height - GuiMageKnowledgeTable.insideSizeY) / 2;//-32
			if(canGetProvisionalSpell(X, Y))
			{
				if(getProvisionalSpell(X, Y).getSpellClass() instanceof Spell.SpellCorrection)
				{
					int x = getProvisionalSpell(X, Y).getPositionX() * GuiMageKnowledgeTable.interval + 16;
					int y = getProvisionalSpell(X, Y).getPositionY() * GuiMageKnowledgeTable.interval + 18;
					if((X - x) * (X - x) + (Y - y) * (Y - y) < 110)
					{
						return getProvisionalSpell(X, Y);
					}
				}
				else if(getProvisionalSpell(X, Y).getSpellClass() instanceof Spell.SpellEffect)
				{
					int x = getProvisionalSpell(X, Y).getPositionX() * GuiMageKnowledgeTable.interval + 16;
					int y = getProvisionalSpell(X, Y).getPositionY() * GuiMageKnowledgeTable.interval + 16;
					if((X - x) * (X - x) + (Y - y) * (Y - y) < 121)
					{
						return getProvisionalSpell(X, Y);
					}
				}
				else
				{
					int x = getProvisionalSpell(X, Y).getPositionX() * GuiMageKnowledgeTable.interval + 16;
					int y = getProvisionalSpell(X, Y).getPositionY() * GuiMageKnowledgeTable.interval + 16;
					if((X - x) * (X - x) + (Y - y) * (Y - y) < 109)
					{
						return getProvisionalSpell(X, Y);
					}
				}
			}
			
			return null;
		}
		
		private static SpellItem getProvisionalSpell(int X,int Y)
		{
			int offsetX = 0;
			int offsetY = 0;
			int x = X;
			int y = Y;
			if(x < 0)
			{
				x++;
				offsetX = -1;
			}
			
			if(y < 0)
			{
				y++;
				offsetY = -1;
			}
			
			return SpellUtils.getFromXY(x / GuiMageKnowledgeTable.interval + offsetX, y / GuiMageKnowledgeTable.interval + offsetY);
		}
		
		private static boolean canGetProvisionalSpell(int X,int Y)
		{
			int offsetX = 0;
			int offsetY = 0;
			int x = X;
			int y = Y;
			if(x < 0)
			{
				x++;
				offsetX = -1;
			}
			
			if(y < 0)
			{
				y++;
				offsetY = -1;
			}
			
			return (SpellUtils.getFromXY(x / GuiMageKnowledgeTable.interval + offsetX, y / GuiMageKnowledgeTable.interval + offsetY) != null && SpellUtils.getFromXY(x / GuiMageKnowledgeTable.interval + offsetX, y / GuiMageKnowledgeTable.interval + offsetY).getTab() == StudyTableUtils.tab);
		}
	}
}
