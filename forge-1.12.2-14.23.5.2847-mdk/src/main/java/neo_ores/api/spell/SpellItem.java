package neo_ores.api.spell;

import javax.annotation.Nullable;

import neo_ores.main.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import net.minecraftforge.registries.IForgeRegistryEntry;

@ObjectHolder(Reference.MOD_ID)
public class SpellItem extends IForgeRegistryEntry.Impl<SpellItem>
{
	private final BasicData bdata;
	private final String translateKey;
	private final Spell runnable;
	private final MageKnowledgeTableData stdata;
	// private List<ItemStackWithSizeForRecipe> consumption;

	public SpellItem(BasicData bdata, String translateKey, MageKnowledgeTableData stdata, Spell runnable)
	{
		this.bdata = bdata;
		this.translateKey = translateKey;
		this.runnable = runnable;
		this.stdata = stdata;
		this.setRegistryName(new ResourceLocation(this.bdata.modid, "spellItem." + this.bdata.registerID));
	}

	public String getModId()
	{
		return this.bdata.modid;
	}

	public String getRegisteringId()
	{
		return this.bdata.registerID;
	}

	public SpellItemType getType()
	{
		return this.bdata.type;
	}

	public long getCostsum()
	{
		return this.bdata.costsum;
	}

	public float getCostproduct()
	{
		return this.bdata.costproduct;
	}
	/*
	 * public List<ItemStackWithSizeForRecipe> getConsumption() { return
	 * this.consumption; }
	 */

	public String getTranslateKey()
	{
		return translateKey;
	}

	/*
	 * public SpellItem setConsumption(List<ItemStackWithSizeForRecipe> recipe) {
	 * this.consumption = recipe; return this; }
	 */
	/*
	 * when draw texture,if spell type is CORRECTION, start drawing from (16,20) on
	 * template.(else from (16,16))
	 */
	public ResourceLocation getTexturePath()
	{
		return this.stdata.texture;
	}

	public Spell getSpellClass()
	{
		return this.runnable;
	}

	public int getTier()
	{
		return this.bdata.tier;
	}

	public KnowledgeTab getTab()
	{
		return this.stdata.tab;
	}

	@Nullable
	public SpellItem getParent()
	{
		return this.stdata.parent;
	}

	public int getPositionX()
	{
		return this.stdata.x;
	}

	public int getPositionY()
	{
		return this.stdata.y;
	}

	public String toString()
	{
		return this.bdata.modid + ":" + this.bdata.registerID;
	}
}
