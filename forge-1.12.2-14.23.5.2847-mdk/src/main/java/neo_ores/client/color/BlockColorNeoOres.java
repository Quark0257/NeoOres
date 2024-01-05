package neo_ores.client.color;

import neo_ores.api.ColorUtils;
import neo_ores.world.dimension.DimensionHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockColorNeoOres implements IBlockColor
{
	private final int index;

	public BlockColorNeoOres(int index)
	{
		this.index = index;
	}

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
	{
		if (!(worldIn != null && pos != null))
			return -1;
		return ColorUtils.getColorWithWhite(ColorUtils.makeColor(DimensionHelper.colors.get(this.index)).getColor(), 0.75);
	}
}
