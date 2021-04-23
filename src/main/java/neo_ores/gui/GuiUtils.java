package neo_ores.gui;

import javax.annotation.Nullable;

import neo_ores.spell.SpellItem;
import neo_ores.spell.SpellType;
import neo_ores.spell.SpellUtils;
import neo_ores.study.StudyItemManagerClient;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiUtils 
{
	public static class StudyTableUtils
	{
		public static StudyTabs tab;
		
		private StudyTableUtils(StudyTabs tab)
		{
			StudyTableUtils.tab = tab;
		}
		
		public static StudyTableUtils set(StudyTabs tab)
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
			System.out.println(mouseX+ ":" + mouseY);
			int X = mouseX - scrolledX - (width - GuiStudyTable.insideSizeX) / 2;//-24
			int Y = mouseY - scrolledY - (height - GuiStudyTable.insideSizeY) / 2;//-32
			if(canGetProvisionalSpell(X, Y))
			{
				if(getProvisionalSpell(X, Y).getSpellClass().getSpellItemType() == SpellType.CORRECTION)
				{
					int x = getProvisionalSpell(X, Y).getPositionX() * GuiStudyTable.interval + 16;
					int y = getProvisionalSpell(X, Y).getPositionY() * GuiStudyTable.interval + 18;
					if((X - x) * (X - x) + (Y - y) * (Y - y) < 110)
					{
						return getProvisionalSpell(X, Y);
					}
				}
				else if(getProvisionalSpell(X, Y).getSpellClass().getSpellItemType() == SpellType.EFFECT)
				{
					int x = getProvisionalSpell(X, Y).getPositionX() * GuiStudyTable.interval + 16;
					int y = getProvisionalSpell(X, Y).getPositionY() * GuiStudyTable.interval + 16;
					if((X - x) * (X - x) + (Y - y) * (Y - y) < 121)
					{
						return getProvisionalSpell(X, Y);
					}
				}
				else
				{
					int x = getProvisionalSpell(X, Y).getPositionX() * GuiStudyTable.interval + 16;
					int y = getProvisionalSpell(X, Y).getPositionY() * GuiStudyTable.interval + 16;
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
			
			return SpellUtils.getFromXY(x / GuiStudyTable.interval + offsetX, y / GuiStudyTable.interval + offsetY);
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
			
			return (SpellUtils.getFromXY(x / GuiStudyTable.interval + offsetX, y / GuiStudyTable.interval + offsetY) != null && SpellUtils.getFromXY(x / GuiStudyTable.interval + offsetX, y / GuiStudyTable.interval + offsetY).getTab() == StudyTableUtils.tab);
		}
	}
}
