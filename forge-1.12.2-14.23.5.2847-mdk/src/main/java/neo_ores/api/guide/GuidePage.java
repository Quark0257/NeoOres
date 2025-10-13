package neo_ores.api.guide;

import java.util.ArrayList;
import java.util.List;

import neo_ores.main.Reference;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistryEntry;

@ObjectHolder(Reference.MOD_ID)
public class GuidePage extends IForgeRegistryEntry.Impl<GuidePage>
{
	protected final GuideTab tab;
	protected final List<AbstractPageComponent> list;
	protected final int pagePriority;
	
	public GuidePage(GuideTab tab, int pagePriority) 
	{
		this.tab = tab;
		this.list = new ArrayList<>();
		this.pagePriority = pagePriority;
	}
	
	public GuidePage addComponent(AbstractPageComponent component) 
	{
		this.list.add(component);
		return this;
	}
	
	public GuideTab getTab() 
	{
		return this.tab;
	}
	
	public List<AbstractPageComponent> getList() 
	{
		return this.list;
	}
	
	public int getPriority() 
	{
		return this.pagePriority;
	}
}
