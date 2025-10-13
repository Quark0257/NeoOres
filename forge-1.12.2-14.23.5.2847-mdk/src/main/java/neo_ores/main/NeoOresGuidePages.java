package neo_ores.main;

import java.util.Arrays;
import java.util.List;

import neo_ores.api.guide.ComponentTextDesc;
import neo_ores.api.guide.ComponentTextTitle;
import neo_ores.api.guide.GuidePage;
import neo_ores.api.guide.GuideTab;
import neo_ores.api.guide.StructurePanel;
import neo_ores.client.gui.GuiGuidebook;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class NeoOresGuidePages
{
	public static final GuideTab essenceTab = new GuideTab("guide.essence");
	public static final GuideTab structureTab = new GuideTab("guide.structure");

	public static final GuidePage essence1 = new GuidePage(essenceTab, 1).addComponent(new ComponentTextTitle(essenceTab.getKey()))
			.addComponent(new ComponentTextDesc(4 + 27, Arrays.asList(I18n.format("guide.page.essence1")))).setRegistryName(Reference.MOD_ID, "essence1");
	public static final GuidePage structure1 = new GuidePage(structureTab, 1).addComponent(new ComponentTextTitle(structureTab.getKey()))
			.addComponent(new ComponentTextDesc(4 + 27, Arrays.asList(I18n.format("guide.page.structure1")))).setRegistryName(Reference.MOD_ID, "structure1");
	public static final GuidePage structure2 = new GuidePage(structureTab, 2).addComponent(new ComponentTextTitle("guide.structure2"))
			.addComponent(new StructurePanel(8, 4 + 27, GuiGuidebook.pageSizeX - 16, 116, new ResourceLocation(Reference.MOD_ID, "alter/alter_tier2"))).setRegistryName(Reference.MOD_ID, "structure2");
	public static final GuidePage structure3 = new GuidePage(structureTab, 3).addComponent(new ComponentTextTitle("guide.structure3"))
			.addComponent(new StructurePanel(8, 4 + 27, GuiGuidebook.pageSizeX - 16, 116, new ResourceLocation(Reference.MOD_ID, "alter/alter_tier2_1"))).setRegistryName(Reference.MOD_ID, "structure3");
	public static final GuidePage structure4 = new GuidePage(structureTab, 4).addComponent(new ComponentTextTitle("guide.structure4"))
			.addComponent(new StructurePanel(8, 4 + 27, GuiGuidebook.pageSizeX - 16, 116, new ResourceLocation(Reference.MOD_ID, "alter/alter_tier3"))).setRegistryName(Reference.MOD_ID, "structure4");
	public static final GuidePage structure5 = new GuidePage(structureTab, 5).addComponent(new ComponentTextTitle("guide.structure5"))
			.addComponent(new StructurePanel(8, 4 + 27, GuiGuidebook.pageSizeX - 16, 116, new ResourceLocation(Reference.MOD_ID, "alter/alter_tier3_1"))).setRegistryName(Reference.MOD_ID, "structure5");
	public static final GuidePage structure6 = new GuidePage(structureTab, 6).addComponent(new ComponentTextTitle("guide.structure6"))
			.addComponent(new StructurePanel(8, 4 + 27, GuiGuidebook.pageSizeX - 16, 116, new ResourceLocation(Reference.MOD_ID, "alter/alter_tier4"))).setRegistryName(Reference.MOD_ID, "structure6");
	
	
	public static final List<GuidePage> registry = Arrays.asList(essence1, structure1, structure2, structure3, structure4, structure5, structure6);
}
