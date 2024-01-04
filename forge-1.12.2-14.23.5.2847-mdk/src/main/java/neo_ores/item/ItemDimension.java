package neo_ores.item;

import neo_ores.main.NeoOres;
import neo_ores.main.Reference;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ItemDimension extends INeoOresItem.Impl
{
	public ItemDimension()
    {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(NeoOres.neo_ores_tab);
    }
	
	public String getUnlocalizedName(ItemStack stack)
    {
        int i = stack.getMetadata();
        return super.getUnlocalizedName() + "." + DimensionName.getFromMeta(i);
    }
	
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            for (int i = 0; i < 4; ++i)
            {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }
	
	public ModelResourceLocation getModel(Item item, int meta)
	{
		return new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, this.getRegistryName().getResourcePath() + "_" + DimensionName.getFromMeta(meta).getName()),"inventory");
	}
	
	public int getMaxMeta()
	{
		return 3;
	}
}
