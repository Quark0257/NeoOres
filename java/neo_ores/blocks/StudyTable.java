package neo_ores.blocks;

import neo_ores.main.NeoOres;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StudyTable extends Block
{
	public StudyTable() 
	{
		super(Material.IRON);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setSoundType(SoundType.METAL);
		this.setHarvestLevel("pickaxe", 0);
		this.setLightLevel(1.0F);
	}
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
        	playerIn.openGui(NeoOres.instance, NeoOres.guiIDStudyTable, worldIn, pos.getX(), pos.getY(), pos.getZ());
        	return true;
        }
        else
        {
            return true;
        }
    }
}
