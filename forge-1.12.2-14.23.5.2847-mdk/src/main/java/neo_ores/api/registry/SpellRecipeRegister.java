package neo_ores.api.registry;

import neo_ores.api.recipe.SpellRecipe;
import neo_ores.main.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class SpellRecipeRegister 
{	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public static void create(RegistryEvent.NewRegistry event)
	{
		RegistryBuilder<SpellRecipe> spell = new RegistryBuilder();
		spell.setType(SpellRecipe.class);
		ResourceLocation key = new ResourceLocation(Reference.MOD_ID, "spellRecipes");
		spell.setName(key);
		spell.setDefaultKey(key);
		spell.create();
	}
}
