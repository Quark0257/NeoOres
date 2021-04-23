package neo_ores.world.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class FromAirTeleporter extends Teleporter
{
	protected final WorldServer worldIn;
	
	public FromAirTeleporter(WorldServer worldIn) 
	{
		super(worldIn);
		this.worldIn = worldIn;
	}
	
	public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
	{
		if (entityIn instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP)entityIn).connection.setPlayerLocation(entityIn.posX, 320.0D, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
        }
        else
        {               	
            entityIn.setLocationAndAngles(entityIn.posX, 320.0D, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
        }
		
		return true;
	}
	
	public boolean makePortal(Entity entityIn)
	{
		return false;
	}
}
