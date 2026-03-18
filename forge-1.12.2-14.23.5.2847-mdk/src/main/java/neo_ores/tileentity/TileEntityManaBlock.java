package neo_ores.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityManaBlock extends TileEntity
{
	@SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Double.MAX_VALUE;
    }
}
