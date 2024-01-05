package neo_ores.block;

import neo_ores.main.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class NeoOresBlock extends Block implements INeoOresBlock
{

	public NeoOresBlock(Material materialIn)
	{
		super(materialIn);
	}

	public NeoOresBlock(Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(blockMaterialIn, blockMapColorIn);
	}

	public ModelResourceLocation getModel(int meta)
	{
		return new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, this.getRegistryName().getResourcePath()), "inventory");
	}

	// 0~15 available
	public int getMaxMeta()
	{
		return 0;
	}

	public Item getItemBlock(Block block)
	{
		return new ItemBlock(block).setRegistryName(block.getRegistryName());
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return "tile." + this.getRegistryName().getResourcePath();
	}
}
