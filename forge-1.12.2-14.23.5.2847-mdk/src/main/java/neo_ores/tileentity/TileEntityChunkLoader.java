package neo_ores.tileentity;

import neo_ores.main.NeoOresData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityChunkLoader extends TileEntity implements ITickable
{
	public long tickCount = 0;
	
	public void update() 
	{
		this.tickCount++;
		if (!this.getWorld().isRemote)
		{
			NeoOresData.instance.addLoadingChunk(this.getWorld(), this.getPos());
		}
	}
	
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		BlockPos pos = getPos();
		return new AxisAlignedBB(pos.getX() + 0.125D, pos.getY() + 0.0D, pos.getZ() + 0.125D, pos.getX() + 0.875D, pos.getY() + 0.875D, pos.getZ() + 0.875D);
	}
}
