package neo_ores.block;

import neo_ores.main.NeoOres;
import neo_ores.tileentity.TileEntitySpellRecipeCreationTable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSpellRecipeCreationTable extends NeoOresBlock implements ITileEntityProvider
{

	public BlockSpellRecipeCreationTable()
	{
		super(Material.WOOD);
		this.setHardness(5.0F);
		this.setResistance(Float.MAX_VALUE);
		this.setSoundType(SoundType.WOOD);
		this.setHarvestLevel("pickaxe", 0);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntitySpellRecipeCreationTable();
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		else
		{
			playerIn.openGui(NeoOres.instance, NeoOres.guiIDSRCT, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
	}
}
