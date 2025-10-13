package neo_ores.util;

import com.google.gson.JsonObject;

import neo_ores.main.NeoOres;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SpellPlaceInfinityUtils
{
	public static boolean match(ItemStack stack) 
	{
		for (JsonObject object : NeoOres.infinity_place_blocks) 
		{
			boolean flag = stack.getItem() == Item.getByNameOrId(object.get("id").getAsString()) && (object.has("metadata") ? object.get("metadata").getAsInt() == stack.getMetadata() : true);
			if (flag) 
			{
				return true;
			}
		}
		return false;
	}
}
