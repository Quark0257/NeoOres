package neo_ores.block;

import neo_ores.main.Reference;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

public class BlockDimensionPillarSingleModel extends BlockDimensionPillar
{
	public BlockDimensionPillarSingleModel(String registername, Material materialIn, float hardness, float resistant,
			String harvest_key, int harvest_level, float light, SoundType sound)
	{
		super(registername, materialIn, hardness, resistant, harvest_key, harvest_level, light, sound);
	}

	public ModelResourceLocation getModel(int meta)
	{
		return new ModelResourceLocation(
				new ResourceLocation(Reference.MOD_ID, this.getRegistryName().getResourcePath()), "inventory");
	}
}
