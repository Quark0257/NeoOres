package neo_ores.client.render;

import neo_ores.main.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class CustomModelFluid extends StateMapperBase implements ItemMeshDefinition
{
	private final Fluid fluid;
	public CustomModelFluid(Fluid fluid) 
	{
		this.fluid = fluid;
	}

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		return new ModelResourceLocation(Reference.MOD_ID + ":fluids", this.fluid.getName());
	}

	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state)
	{
		return new ModelResourceLocation(Reference.MOD_ID + ":fluids", this.fluid.getName());
	}

}
