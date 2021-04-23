package neo_ores.recipes;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import neo_ores.main.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistryEntry;

@ObjectHolder(Reference.MOD_ID)
public class ManaCompositionRecipe extends IForgeRegistryEntry.Impl<ManaCompositionRecipe>
{
	public Map<Object,Integer> recipe;
	public int tier;
	public ItemStack result;
	
	public ManaCompositionRecipe(String modid,int id,int tier,@Nonnull ItemStack result,Object... objects)
	{
		for(int i = 0;i < objects.length / 2;i++)
		{
			recipe = new HashMap<Object,Integer>();
			if((!(objects[2 * i] instanceof ItemStack) && !(objects[2 * i] instanceof String)) || !(objects[2 * i + 1] instanceof Integer))
			{
				return;
			}
			else
			{
				recipe.put(objects[2 * i], (Integer)objects[2 * i + 1]);
			}
		}
		
		this.tier = tier;
		this.result = result;
		
		this.setRegistryName(new ResourceLocation(modid, "manaCompositionRecipe." + id));
	}
	
	public Map<Object,Integer> getRecipe()
	{
		return this.recipe;
	}
	
	public ItemStack getResult()
	{
		return this.result;
	}
	
	public int getTier()
	{
		return this.tier;
	}
}
