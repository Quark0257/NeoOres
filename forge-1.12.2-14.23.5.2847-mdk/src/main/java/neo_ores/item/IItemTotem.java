package neo_ores.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IItemTotem
{
	public boolean needsPlayer(ItemStack stack);
	public boolean isCreative(ItemStack stack);
	public String getPlayerUUID(ItemStack stack);
	public boolean hasPlayerUUID(ItemStack stack);
	public void damage(ItemStack stack, int damage, EntityLivingBase elb);
}
