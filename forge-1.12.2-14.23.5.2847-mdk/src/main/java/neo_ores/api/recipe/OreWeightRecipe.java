package neo_ores.api.recipe;

import com.google.gson.JsonObject;

import neo_ores.main.Reference;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistryEntry;

@ObjectHolder(Reference.MOD_ID)
public class OreWeightRecipe extends IForgeRegistryEntry.Impl<OreWeightRecipe> 
{
	private final JsonObject object;
	public OreWeightRecipe(JsonObject object) 
	{
		this.object = object;
	}
	
	public JsonObject getObject()
	{
		return this.object;
	}
}
