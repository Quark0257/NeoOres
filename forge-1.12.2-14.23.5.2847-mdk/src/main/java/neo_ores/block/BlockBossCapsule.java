package neo_ores.block;

import neo_ores.main.Reference;
import neo_ores.tileentity.TileEntityBossCapsule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBossCapsule extends BlockContainer implements INeoOresBlock
{
	public BlockBossCapsule()
	{
		super(Material.IRON, MapColor.SILVER);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
		this.setResistance(Float.MAX_VALUE);
		this.setSoundType(SoundType.METAL);
		this.setHarvestLevel("pickaxe", 0);
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityBossCapsule)
				? ((TileEntityBossCapsule) worldIn.getTileEntity(pos)).onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ)
				: false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityBossCapsule();
	}
	
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public ModelResourceLocation getModel(int meta)
	{
		return new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, this.getRegistryName().getResourcePath()), "inventory");
	}

	@Override
	public int getMaxMeta()
	{
		return 0;
	}

	@Override
	public Item getItemBlock(Block block)
	{
		return new ItemBlock(block).setRegistryName(block.getRegistryName());
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return "tile." + this.getRegistryName().getResourcePath();
	}
}
