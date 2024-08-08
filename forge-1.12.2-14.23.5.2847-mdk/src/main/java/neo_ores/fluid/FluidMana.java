package neo_ores.fluid;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidMana extends Fluid 
{
	public FluidMana(String fluidName, ResourceLocation still, ResourceLocation flowing) 
	{
		super(fluidName, still, flowing, still);
		this.setDensity(600);
		this.setViscosity(100);
		this.setGaseous(false);
		this.setLuminosity(15);
		this.setRarity(EnumRarity.EPIC);
		this.setTemperature(303);
		this.setFillSound(SoundEvents.ITEM_BUCKET_FILL);
	}
}
