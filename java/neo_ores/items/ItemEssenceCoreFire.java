package neo_ores.items;

import java.util.List;

import neo_ores.main.NeoOres;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemEssenceCoreFire extends ItemEffected
{
	public ItemEssenceCoreFire()
    {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(NeoOres.neo_ores_tab);
    }
	
	public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag)
	{
		int i = itemStack.getMetadata() + 1;
		list.add("Tier:" + i);
	}
	
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            for (int i = 0; i < 11; ++i)
            {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }
}