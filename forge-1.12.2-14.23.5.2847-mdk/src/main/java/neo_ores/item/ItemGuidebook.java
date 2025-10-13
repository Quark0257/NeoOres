package neo_ores.item;

import neo_ores.main.NeoOres;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemGuidebook extends INeoOresItem.Impl 
{
	public ItemGuidebook() 
	{
		this.setMaxStackSize(1);
	}
	
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if (worldIn.isRemote)
		{
			playerIn.openGui(NeoOres.instance, NeoOres.guiIDGuide, worldIn, MathHelper.floor(playerIn.posX), MathHelper.floor(playerIn.posY), MathHelper.floor(playerIn.posZ));
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
}
