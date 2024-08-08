package neo_ores.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class ItemTotem extends INeoOresItem.Impl implements IItemTotem
{
	public ItemTotem(int maxDamage) 
	{
		this.setMaxStackSize(1);
		this.setMaxDamage(maxDamage);
	}
	
	@Override
	public boolean needsPlayer(ItemStack stack)
	{
		return true;
	}

	@Override
	public String getPlayerUUID(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null && nbt.hasKey("neo_ores") && nbt.getCompoundTag("neo_ores").hasKey("player"))
		{
			return nbt.getCompoundTag("neo_ores").getString("player");
		}
		return null;
	}

	@Override
	public void damage(ItemStack stack, int damage, EntityLivingBase elb)
	{
		stack.damageItem(damage, elb);
	}
	
	@SuppressWarnings("static-access")
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if(player instanceof FakePlayer || !this.needsPlayer(player.getHeldItem(hand)))
			return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
		ItemStack stack = player.getHeldItem(hand);
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		
		if(!stack.getTagCompound().hasKey("neo_ores")) {
			stack.getTagCompound().setTag("neo_ores", new NBTTagCompound());
		}
		
		stack.getTagCompound().getCompoundTag("neo_ores").setString("player", player.getUUID(player.getGameProfile()).toString());

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public boolean hasPlayerUUID(ItemStack stack)
	{
		return this.getPlayerUUID(stack) != null && !this.getPlayerUUID(stack).isEmpty();
	}

	@Override
	public boolean isCreative(ItemStack stack)
	{
		return false;
	}
}
