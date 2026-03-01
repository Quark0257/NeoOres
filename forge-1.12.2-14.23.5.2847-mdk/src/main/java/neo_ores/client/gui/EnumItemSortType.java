package neo_ores.client.gui;

import java.util.Comparator;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public enum EnumItemSortType
{
	ALPHA_UP(Comparator.comparing(new Function<ItemStack, String>()
	{
		@Override
		public String apply(ItemStack arg0)
		{
			return TextFormatting.getTextWithoutFormattingCodes(arg0.getDisplayName()).toLowerCase(Locale.ROOT);
		}
	}, Comparator.naturalOrder()), "A>"), ALPHA_DOWN(Comparator.comparing(new Function<ItemStack, String>()
	{
		@Override
		public String apply(ItemStack arg0)
		{
			return TextFormatting.getTextWithoutFormattingCodes(arg0.getDisplayName()).toLowerCase(Locale.ROOT);
		}
	}, Comparator.reverseOrder()), ">A"), NUMBER_UP(Comparator.comparingInt(new ToIntFunction<ItemStack>()
	{
		@Override
		public int applyAsInt(ItemStack arg0)
		{
			return arg0.getCount();
		}
	}), ">1"), NUMBER_DOWN(Comparator.comparingInt(new ToIntFunction<ItemStack>()
	{
		@Override
		public int applyAsInt(ItemStack arg0)
		{
			return -arg0.getCount();
		}
	}), "1>"), MOD_UP(Comparator.comparing(new Function<ItemStack, String>()
	{
		@Override
		public String apply(ItemStack arg0)
		{
			return TextFormatting.getTextWithoutFormattingCodes(arg0.getItem().getCreatorModId(arg0)).toLowerCase(Locale.ROOT);
		}
	}, Comparator.naturalOrder()), "@>"), MOD_DOWN(Comparator.comparing(new Function<ItemStack, String>()
	{
		@Override
		public String apply(ItemStack arg0)
		{
			return TextFormatting.getTextWithoutFormattingCodes(arg0.getItem().getCreatorModId(arg0)).toLowerCase(Locale.ROOT);
		}
	}, Comparator.reverseOrder()), ">@");

	final Comparator<ItemStack> comparator;
	final String view;

	EnumItemSortType(Comparator<ItemStack> comparator, String view)
	{
		this.comparator = comparator;
		this.view = view;
	}

	public Comparator<ItemStack> getComparator()
	{
		return this.comparator;
	}

	public static EnumItemSortType getSortType(int id)
	{
		if (EnumItemSortType.values().length > id && id >= 0)
		{
			return EnumItemSortType.values()[id];
		}
		return EnumItemSortType.ALPHA_UP;
	}
	
	public EnumItemSortType getNext() 
	{
		return getSortType(getId(this) + 1);
	}
	
	public String getView() 
	{
		return this.view;
	}

	public static int getId(EnumItemSortType type)
	{
		for (int id = 0; id < EnumItemSortType.values().length; id++)
		{
			if (type == EnumItemSortType.values()[id])
			{
				return id;
			}
		}
		return 0;
	}
}
