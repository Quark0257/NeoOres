package neo_ores.client.color;

import neo_ores.api.ColorUtils;
import neo_ores.world.dimension.DimensionHelper;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class ItemColorNeoOres implements IItemColor {
	private final int index;
	
	public ItemColorNeoOres(int index) {
		this.index = index;
	}
	
	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		return ColorUtils.getColorWithWhite(ColorUtils.makeColor(DimensionHelper.colors.get(this.index)).getColor(),0.75);
	}
	
}
