package neo_ores.api;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;

public class ManaLinkUtils 
{
	public final ItemStack stack;
	
	public ManaLinkUtils(ItemStack item)
	{
		this.stack = item;
	}
	
	@SuppressWarnings("static-access")
	public void setPlayer(EntityPlayerMP player)
	{
		if(!this.stack.hasTagCompound())
		{
			this.stack.setTagCompound(new NBTTagCompound());
		}
		
		if(!stack.getTagCompound().hasKey("neo_ores", 10))
		{
			stack.getTagCompound().setTag("neo_ores", new NBTTagCompound());
		}

		stack.getTagCompound().getCompoundTag("neo_ores").setString("playerUUID", player.getOfflineUUID(player.getName()).toString());
	}
	
	@Nullable
	public EntityPlayer getPlayer(WorldServer world)
	{
		if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("neo_ores", 10) || !stack.getTagCompound().getCompoundTag("neo_ores").hasKey("playerUUID", 8))
		{
			return null;
		}
		return world.getPlayerEntityByUUID(UUID.fromString(stack.getTagCompound().getCompoundTag("neo_ores").getString("playerUUID")));
	}
	
	public boolean hasPlayer(WorldServer world)
	{
		return getPlayer(world) != null;
	}
}
