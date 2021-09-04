package neo_ores.spell;

import java.util.List;

import javax.annotation.Nullable;

import neo_ores.gui.StudyTabs;
import neo_ores.main.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import net.minecraftforge.registries.IForgeRegistryEntry;

@ObjectHolder(Reference.MOD_ID)
public class SpellItem extends IForgeRegistryEntry.Impl<SpellItem>
{
	private final long costsum;
	private final float costproduct;
	private final int tier;
	private final List<ItemStack> consumption;
	private final String translateKey;
	private final ResourceLocation texturePath;
	private final ISpell runnable;
	private final String mod_id;
	private final String id;
	private final SpellItemType type;
	private final int table_x;
	private final int table_y;
	private final SpellItem parent;
	private final StudyTabs tab;
	
	/*
	 * Texture size is 31*31
	 */
	public SpellItem(String modid,String registerID,int tier, SpellItemType type,long costsum, float costproduct,List<ItemStack> consumption, String translateKey,@Nullable SpellItem parent,int table_x,int table_y, ResourceLocation texture,StudyTabs tab, ISpell runnable)
	{
		this.costsum = costsum;
		this.costproduct = costproduct;
		this.consumption = consumption;
		this.translateKey = translateKey;
		this.texturePath = texture;
		this.runnable = runnable;
		this.tier = tier;
		this.id = registerID;
		this.mod_id = modid;
		this.type = type;
		this.table_x = table_x;
		this.table_y = table_y;
		this.parent = parent;
		this.tab = tab;
		
		this.setRegistryName(new ResourceLocation(this.mod_id, "spellItem." + id));
	}
	
	public String getModId()
	{
		return mod_id;
	}
	
	public String getRegisteringId()
	{
		return this.id;
	}
	
	public SpellItemType getType()
	{
		return this.type;
	}
	
	public long getCostsum()
	{
		return costsum;
	}
	
	public float getCostproduct()
	{
		return costproduct;
	}
	
	public List<ItemStack> getConsumption()
	{
		return consumption;
	}
	
	public String getTranslateKey()
	{
		return translateKey;
	}
	
	/*
	 * when draw texture,if spell type is CORRECTION, start drawing from (16,20) on template.(else from (16,16))
	 */
	public ResourceLocation getTexturePath()
	{
		return texturePath;
	}
	
	public ISpell getSpellClass()
	{
		return this.runnable;
	}
	
	public int getTier()
	{
		return this.tier;
	}
	
	public StudyTabs getTab()
	{
		return this.tab;
	}
	
	@Nullable
	public SpellItem getParent()
	{
		return this.parent;
	}
	
	public int getPositionX()
	{
		return this.table_x;
	}
	
	public int getPositionY()
	{
		return this.table_y;
	}
}
