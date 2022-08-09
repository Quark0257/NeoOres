package neo_ores.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemManaWrench extends INeoOresItem.Impl 
{
	public ItemManaWrench()
	{
		this.setMaxStackSize(1);
	}
	
	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player)
    {
        return false;
    }
}
