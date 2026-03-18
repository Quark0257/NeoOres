package neo_ores.api;

import java.util.ArrayList;
import java.util.List;

import neo_ores.item.IItemNeoBauble;
import neo_ores.item.IItemNeoTool;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresItems;
import neo_ores.util.SpellUtils.NBTTagUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class RecipeOreStackWildCard extends RecipeOreStack
{
	public RecipeOreStackWildCard(Object object, int size)
	{
		super(object, size);
	}

	/**
	 * Uses as a wild card of the spell
	 */
	public static final RecipeOreStack ANY_ITEM = new RecipeOreStackWildCard("ANY ITEM", 1)
	{
		@Override
		public boolean compareWith(ItemStack target)
		{
			return !target.isEmpty() && (!target.hasTagCompound() || !target.getTagCompound().hasKey(NBTTagUtils.SPELL, 10));
		}

		@Override
		public ItemStack getRepresentative()
		{
			ItemStack rep = new ItemStack(NeoOresItems.spell_sheet);
			rep.addEnchantment(NeoOres.soulbound, 1);
			return rep;
		}
	};

	/**
	 * Uses as a wild card of the spell
	 */
	public static final RecipeOreStack NEO_ORES_ITEMS = new RecipeOreStackWildCard("TOOL, ARMOR or BAUBLE of Neo Ores II", 1)
	{
		@Override
		public boolean compareWith(ItemStack target)
		{
			Item item = target.getItem();
			return item instanceof IItemNeoTool || item instanceof IItemNeoBauble;
		}

		@Override
		public ItemStack getRepresentative()
		{
			ItemStack rep = new ItemStack(NeoOresItems.undite_helmet);
			rep.addEnchantment(NeoOres.soulbound, 1);
			return rep;
		}
	};

	public abstract boolean compareWith(ItemStack target);

	public abstract ItemStack getRepresentative();

	public boolean isItemStack()
	{
		return false;
	}

	public boolean isOreDic()
	{
		return false;
	}

	public List<ItemStack> getListTogether()
	{
		List<ItemStack> list = new ArrayList<ItemStack>();
		list.add(getRepresentative());
		return list;
	}
}
