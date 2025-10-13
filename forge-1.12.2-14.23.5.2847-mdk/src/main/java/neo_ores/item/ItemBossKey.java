package neo_ores.item;

import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemBossKey extends INeoOresItem.Impl
{
	public ItemBossKey() 
	{
		this.setMaxStackSize(1);
	}
	
	public boolean hasEffect(ItemStack stack) 
	{
		return this.hasKey(stack);
	}
	
	public boolean hasKey(ItemStack stack) 
	{
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("uuidKeyMost") && stack.getTagCompound().hasKey("uuidKeyLeast");
	}
	
	public UUID getKey(ItemStack stack) 
	{
		return this.hasKey(stack) ? stack.getTagCompound().getUniqueId("uuidKey") : null;
	}
	
	public void setKey(ItemStack stack, UUID uuid) 
	{
		if (!stack.hasTagCompound()) 
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setUniqueId("uuidKey", uuid);
	}
}
