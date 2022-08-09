package neo_ores.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemRayTraceable extends Item
{
	public RayTraceResult rayTraceForPublic(World world, EntityPlayer player, boolean liquid)
	{
		return this.rayTrace(world, player, liquid);
	}
}
