package neo_ores.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import neo_ores.main.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistryEntry;

@ObjectHolder(Reference.MOD_ID)
public class ManaCraftingRecipe extends IForgeRegistryEntry.Impl<ManaCraftingRecipe>
{	
	private final ItemStack result;
	private final long mana;
	private boolean isShapeless;
	private List<Object[]> itemList;
	
	public ManaCraftingRecipe(String modid,int id, ItemStack result, long mana, Object... object)
	{
		this.result = result;
		this.mana = mana;
		this.isShapeless = false;
		this.itemList = new ArrayList<Object[]>();
		
		int l = 0;
		while(l < object.length)
		{
			if(object[l] instanceof Character)
			{
				this.isShapeless = false;
				break;
			}
			else
			{
				this.isShapeless = true;
			}
			l++;
		}
		
		if(this.isShapeless)
		{
			int i = 0;
			while(i < object.length && i < 9)
			{
				if(object[i] instanceof ItemStack || object[i] instanceof String)
				{
					this.itemList.add(new Object[] {object[i]});
				}
				i++;
			}
		}
		else
		{
			int i = 0;
			List<Character[]> c = new ArrayList<Character[]>();
			while(i < 3 && object[i] instanceof String)
			{
				String s = (String)object[i];
				int j = 0;
				List<Character> chars = new ArrayList<Character>();
				while(j < 3 && j < s.split("").length)
				{
					chars.add((s.split("")[j]).charAt(0));
					j++;
				}
				c.add(chars.toArray(new Character[] {}));
				i++;
			}
			
			int j = 0;
			Map<Character,Object> map = new HashMap<Character,Object>();
			while(i + 2 * j + 1 < object.length && object[i + 2 * j] instanceof Character && (object[i + 2 * j + 1] instanceof ItemStack || object[i + 2 * j + 1] instanceof String))
			{
				map.put((Character)object[i + 2 * j],object[i + 2 * j + 1]);
				j++;
			}
			
			for(int k = 0;k < c.size();k++)
			{
				List<Object> items = new ArrayList<Object>();
				for(int h = 0;h < c.get(k).length;h++)
				{
					if(c.get(k)[h] != null)
					{
						if(c.get(k)[h].equals(' '))
						{
							items.add(ItemStack.EMPTY);
						}
						else if(map.get(c.get(k)[h]) != null)
						{
							items.add(map.get(c.get(k)[h]));
						}
					}
				}
				
				itemList.add(items.toArray(new Object[] {}));
			}
		}
		
		this.setRegistryName(new ResourceLocation(modid, "manaRecipe." + id));
	}
	
	public boolean isShapeless()
	{
		return isShapeless;
	}
	
	public List<Object> getShapelessRecipe()
	{
		if(this.isShapeless)
		{
			List<Object> items = new ArrayList<Object>();
			for(Object[] item : this.itemList)
			{
				items.add(item[0]);
			}
			return items;
		}
		return null;
	}
	
	public List<Object[]> getShapedRecipe()
	{
		if(!this.isShapeless)
		{
			return this.itemList;
		}
		return null;
	}
	
	public ItemStack getResult()
	{
		return this.result;
	}
	
	public long mana()
	{
		return this.mana;
	}
}
