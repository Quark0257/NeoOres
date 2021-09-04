package neo_ores.registry;

import neo_ores.main.Reference;
import neo_ores.recipes.ManaCraftingRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ManaCraftingRecipeRegister 
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SubscribeEvent
	public static void create(RegistryEvent.NewRegistry event)
	{
		RegistryBuilder<ManaCraftingRecipe> spell = new RegistryBuilder();
		spell.setType(ManaCraftingRecipe.class);
		ResourceLocation key = new ResourceLocation(Reference.MOD_ID, "manaRecipes");
		spell.setName(key);
		spell.setDefaultKey(key);
		spell.create();
	}
}
