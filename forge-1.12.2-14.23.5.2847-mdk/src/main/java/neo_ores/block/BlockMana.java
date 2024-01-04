package neo_ores.block;

import neo_ores.item.ItemManaBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMana extends NeoOresBlock
{
	public BlockMana() 
	{
		super(Material.IRON);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setSoundType(SoundType.METAL);
		this.setHarvestLevel("pickaxe", 0);
		this.setLightLevel(1.0F);
	}
	  
	public EnumPushReaction getMobilityFlag(IBlockState state) 
	{
	    return EnumPushReaction.NORMAL;
	}
	  
	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) 
	{
	    IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
	    Block block = iblockstate.getBlock();
	    return (block == this) ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}
	  
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() 
	{
	    return BlockRenderLayer.TRANSLUCENT;
	}
	
	public boolean isFullCube(IBlockState iblockstate) 
	{
	    return false;
	}
	
	public boolean isOpaqueCube(IBlockState state)
	{
	    return false;
	}
	
	public Item getItemBlock(Block block)
	{
		return new ItemManaBlock(block).setRegistryName(block.getRegistryName());
	}
}
