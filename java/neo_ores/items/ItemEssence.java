package neo_ores.items;

import neo_ores.main.NeoOres;
import neo_ores.mana.PlayerManaDataClient;
import neo_ores.mana.PlayerManaDataServer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemEssence extends Item
{
	public ItemEssence()
    {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(NeoOres.neo_ores_tab);
    }
	
	public String getUnlocalizedName(ItemStack stack)
    {
        int i = stack.getMetadata();
        return super.getUnlocalizedName() + "." + i;
    }
	
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            for (int i = 0; i < 5; ++i)
            {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
		if(player.world.isRemote)
		{
			PlayerManaDataClient pmdc = new PlayerManaDataClient((EntityPlayerSP)player);
			if(pmdc.getLevel() > 0)
			{
				return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
			}
			else
			{
				player.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, 1.0F, 1.0F);
			}
		}
		else
		{
			EntityPlayerMP playermp = (EntityPlayerMP)player;
			PlayerManaDataServer pmd = new PlayerManaDataServer(playermp);

			if(pmd.getLevel() > 0)
			{
				return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	    	}
			else
			{
				pmd.setLevel(1);
				pmd.setMaxMana(100);
				pmd.setMana(50);
				pmd.setMXP(0);
				pmd.setMagicPoint(3);
			}
		}
		
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}