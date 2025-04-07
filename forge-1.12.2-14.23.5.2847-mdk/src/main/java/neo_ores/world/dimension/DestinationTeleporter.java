package neo_ores.world.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class DestinationTeleporter extends Teleporter
{
	private static final String[] reflectionField = new String[] { "invulnerableDimensionChange", "field_184851_cj" };
	private BlockPos destination;
	private EnumFacing face;
	
	public DestinationTeleporter(WorldServer worldIn)
	{
		super(worldIn);
		this.destination = BlockPos.ORIGIN;
		this.face = EnumFacing.UP;
	}
	
	public DestinationTeleporter(WorldServer worldIn, BlockPos destination, EnumFacing side)
	{
		super(worldIn);
		this.destination = destination;
		this.face = side;
	}
	
	public void placeInPortal(Entity entityIn, float rotationYaw)
    {
		if (!this.placeInExistingPortal(entityIn, rotationYaw))
        {
            this.makePortal(entityIn);
            this.placeInExistingPortal(entityIn, rotationYaw);
        }
    }
	
	public boolean makePortal(Entity entityIn)
	{
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) 
	{
		BlockPos pushPos = this.destination;
		if (world.getBlockState(pushPos).getBlock() != Blocks.AIR)
		{
			if (this.face == EnumFacing.DOWN)
			{
				pushPos = pushPos.add(0, -entityIn.height, 0);
			}
			else
			{
				pushPos = pushPos.add(this.face.getDirectionVec());
			}
		}
		
		if (entityIn instanceof EntityPlayerMP)
		{
			ObfuscationReflectionHelper.setPrivateValue(EntityPlayerMP.class, (EntityPlayerMP) entityIn, true, reflectionField);
			((EntityPlayerMP) entityIn).setPositionAndUpdate(pushPos.getX() + 0.5D, pushPos.getY(), pushPos.getZ() + 0.5D);
		}
		else
		{
			entityIn.setLocationAndAngles(pushPos.getX() + 0.5D, pushPos.getY(), pushPos.getZ() + 0.5D, entityIn.rotationYaw, entityIn.rotationPitch);
		}
		return true;
	}
}
