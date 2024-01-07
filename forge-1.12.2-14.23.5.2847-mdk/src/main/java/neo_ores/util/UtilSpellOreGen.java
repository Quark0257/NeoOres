package neo_ores.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import neo_ores.api.recipe.OreWeightRecipe;
import neo_ores.main.NeoOres;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class UtilSpellOreGen
{
	public static final Set<Entry<ResourceLocation, OreWeightRecipe>> oreWeightRecipes = GameRegistry.findRegistry(OreWeightRecipe.class).getEntries();

	public static List<JsonObject> getObjects()
	{
		/*
		List<JsonObject> ores = new ArrayList<JsonObject>();
		for (Entry<ResourceLocation, OreWeightRecipe> recipe : oreWeightRecipes)
		{
			ores.add(recipe.getValue().getObject());
		}
		return ores;
		*/
		List<JsonObject> ores = new ArrayList<JsonObject>();
		for (OreWeightRecipe recipe : NeoOres.ore_gen_recipes)
		{
			ores.add(recipe.getObject());
		}
		return ores;
	}

	public static Map<ItemStack, Integer> getOres(IBlockState target)
	{
		Map<ItemStack, Integer> ores = new HashMap<ItemStack, Integer>();
		try
		{
			for (JsonObject object : getObjects())
			{
				ItemStack output = ItemStack.EMPTY;
				String[] idSplitted = object.get("id").getAsString().split(":");
				String modid = idSplitted[0];
				String blockid = idSplitted[1];
				if (modid.equals("ore"))
				{
					if (object.has("acceptMods"))
					{
						JsonArray acceptMods = object.get("acceptMods").getAsJsonArray();
						oreDicSearch: for (ItemStack stack : OreDictionary.getOres(blockid))
						{
							for (JsonElement mod : acceptMods)
							{
								if (stack.getItem().getRegistryName().getResourceDomain().equals(mod.getAsString()))
								{
									output = stack.copy();
									break oreDicSearch;
								}
							}
						}
					}
					else
					{
						output = OreDictionary.getOres(blockid).get(0).copy();
					}

					if (output.isEmpty())
						continue;
				}
				else
				{
					output = new ItemStack(Item.getByNameOrId(modid + ":" + blockid), 1, (object.has("metadata") ? object.get("metadata").getAsInt() : 0));
				}

				JsonObject replace_block = object.get("replace_block").getAsJsonObject();
				String[] idSplittedReplace = replace_block.get("id").getAsString().split(":");
				String modidReplace = idSplittedReplace[0];
				String blockidReplace = idSplittedReplace[1];
				Item target_blockitem = Item.getItemFromBlock(target.getBlock());
				int target_blockmeta = target.getBlock().getMetaFromState(target);
				ItemStack target_stack = new ItemStack(target_blockitem, 1, target_blockmeta);
				if (modidReplace.equals("ore"))
				{
					for (int oreID : OreDictionary.getOreIDs(target_stack))
					{
						if (OreDictionary.getOreName(oreID).equals(blockidReplace))
						{
							ores.put(output, object.get("weight").getAsInt());
							break;
						}
					}
				}
				else
				{
					ItemStack replace = new ItemStack(Item.getByNameOrId(modidReplace + ":" + blockidReplace), 1, (replace_block.has("metadata") ? replace_block.get("metadata").getAsInt() : 0));
					if (replace.getItem() == target_blockitem && replace.getMetadata() == target_blockmeta)
					{
						ores.put(output, object.get("weight").getAsInt());
					}
				}
			}
		}
		catch (Exception e)
		{
			FMLLog.log.error("Unable to load oreWeightRecipe");
		}
		return ores;
	}
}
