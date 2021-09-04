package neo_ores.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class UnditeBlock extends Block
{
	public UnditeBlock() 
	{
		super(Material.IRON);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setSoundType(SoundType.METAL);
		this.setHarvestLevel("pickaxe", 2);
		this.setLightLevel(0.0F);
	}
}
