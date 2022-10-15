package neo_ores.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public abstract class AbstractTileEntityPedestal extends TileEntity implements ITickable{
	protected ItemStack display;
	public int tickCount;
	public double offset;
	public void setDisplay(ItemStack stack)
	{
		this.display = stack.copy();
		this.display.setCount(1);
	}
	
	public ItemStack getDisplay()
	{
		this.display.setCount(1);
		return this.display;
	}
	
	public void update() 
	{
		this.tickCount++;
	}
	
	public abstract boolean canExtract(int index, ItemStack stack, EnumFacing direction);
	
	public abstract boolean canInsert(int index, ItemStack stack, EnumFacing direction);
}
