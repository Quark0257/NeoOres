package neo_ores.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
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
}
