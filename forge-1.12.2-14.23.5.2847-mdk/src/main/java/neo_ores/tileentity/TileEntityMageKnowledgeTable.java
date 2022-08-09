package neo_ores.tileentity;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;

public class TileEntityMageKnowledgeTable extends TileEntity implements ITickable
{
	public float book_rot;
	public int tickCount;
    public float pageFlip;
    public float pageFlipPrev;
    public float flipT;
    public float flipA;
    public float bookSpread;
    public float bookSpreadPrev;
    private static final Random rand = new Random();
	
	public void setFacing(EnumFacing face)
	{
		this.book_rot = this.getRotate(face);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		if (nbt.hasKey("book_rot")) this.book_rot = nbt.getFloat("book_rot");
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
		compound.setFloat("book_rot", book_rot);
        super.writeToNBT(compound);
        return compound;
    }
	
	public void update()
    {
        this.bookSpreadPrev = this.bookSpread;

        this.bookSpread += 0.1F;

        if (this.bookSpread < 0.5F || rand.nextInt(40) == 0)
        {
            float f1 = this.flipT;

            while (true)
            {
                this.flipT += (float)(rand.nextInt(4) - rand.nextInt(4));

                if (f1 != this.flipT)
                {
                    break;
                }
            }
        }

        this.bookSpread = MathHelper.clamp(this.bookSpread, 0.0F, 1.0F);
        ++this.tickCount;
        this.pageFlipPrev = this.pageFlip;
        float f = (this.flipT - this.pageFlip) * 0.4F;
        f = MathHelper.clamp(f, -0.2F, 0.2F);
        this.flipA += (f - this.flipA) * 0.9F;
        this.pageFlip += this.flipA;
    }
	
	private float getRotate(EnumFacing facing)
    {
    	if(facing == EnumFacing.EAST) return 0.0F;
    	if(facing == EnumFacing.SOUTH) return -90.0F;
    	if(facing == EnumFacing.WEST) return 180.0F;
    	return 90.0F;
    }
}
