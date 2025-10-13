package neo_ores.api.registry;

import neo_ores.api.guide.GuidePage;
import neo_ores.main.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class GuidePageRegister
{
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void create(RegistryEvent.NewRegistry event)
	{
		RegistryBuilder<GuidePage> guidePage = new RegistryBuilder<>();
		guidePage.setType(GuidePage.class);
		ResourceLocation key = new ResourceLocation(Reference.MOD_ID, "guidePages");
		guidePage.setName(key);
		guidePage.setDefaultKey(key);
		guidePage.create();
	}
}
