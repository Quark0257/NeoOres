package neo_ores.item;

import neo_ores.api.ManaLinkUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemMagicalPuppet extends INeoOresItem.Impl
{
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		ManaLinkUtils utils = new ManaLinkUtils(stack);
		if (player instanceof EntityPlayerMP)
		{
			utils.setPlayer((EntityPlayerMP) player);
			if (player.capabilities.isCreativeMode)
			{
				ItemStack copied = stack.copy();
				player.setHeldItem(hand, copied);
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
	}
}
