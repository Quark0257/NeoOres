package neo_ores.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ServerUtils
{
	public static void sendSoundToClient(World world, double x, double y, double z, SoundEvent event, SoundCategory category, float volume, float pitch) {
		for (EntityPlayer player : world.playerEntities) {
			if (player instanceof EntityPlayerMP) {
				((EntityPlayerMP)player).connection
				.sendPacket(new SPacketCustomSound(event.getRegistryName().toString(), category, x, y, z, volume, pitch));
			}
		}
	}
	
	public static boolean damageEntity(Entity entity, DamageSource source, float amount) {
		if (entity instanceof IEntityMultiPart) {
			return ((IEntityMultiPart)entity).attackEntityFromPart(null, source, amount);
		}
		if (entity instanceof MultiPartEntityPart) {
			IEntityMultiPart parent = ((MultiPartEntityPart)entity).parent;
			parent.attackEntityFromPart((MultiPartEntityPart)entity, source, amount);
		}
		return entity.attackEntityFrom(source, amount);
	}
}
