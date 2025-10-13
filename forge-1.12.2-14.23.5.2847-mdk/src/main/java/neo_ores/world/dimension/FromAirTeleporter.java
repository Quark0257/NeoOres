package neo_ores.world.dimension;

import neo_ores.util.ServerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class FromAirTeleporter extends Teleporter
{
	protected final WorldServer worldIn;
	private static final String[] reflectionField = new String[] { "invulnerableDimensionChange", "field_184851_cj" };

	public FromAirTeleporter(WorldServer worldIn)
	{
		super(worldIn);
		this.worldIn = worldIn;
	}
	
	public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
	{
		if (entityIn instanceof EntityPlayerMP)
		{
			ServerUtils.setPrivateValue(EntityPlayerMP.class, (EntityPlayerMP) entityIn, true, reflectionField);
			((EntityPlayerMP) entityIn).connection.setPlayerLocation(entityIn.posX, 320.0D, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
			((EntityPlayerMP) entityIn).connection.sendPacket(new SPacketCustomSound(SoundEvents.BLOCK_PORTAL_TRAVEL.getRegistryName().getResourcePath(), SoundCategory.PLAYERS, entityIn.posX,
					entityIn.posY, entityIn.posZ, 0.5F, worldIn.provider.getDimension() == 0 ? 1.0F : this.soundPitch()));
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

	private float soundPitch()
	{
		return 1.0F;
	}
}
