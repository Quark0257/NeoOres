package neo_ores.api.spell;

import javax.annotation.Nonnull;

import net.minecraft.util.ResourceLocation;

public class MageKnowledgeTableData
{
	final SpellItem parent;
	final int x;
	final int y;
	final ResourceLocation texture;
	final KnowledgeTab tab;

	public MageKnowledgeTableData(@Nonnull SpellItem parent, int table_x, int table_y, ResourceLocation texture, KnowledgeTab tab)
	{
		this.parent = parent;
		this.x = table_x;
		this.y = table_y;
		this.texture = texture;
		this.tab = tab;
	}
}
