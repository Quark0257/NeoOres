package neo_ores.blocks;

import java.util.Random;

import neo_ores.main.Reference;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockDimension extends Block
{
	
	public static final PropertyEnum<DimensionName> DIM = PropertyEnum.<DimensionName>create("dimension", DimensionName.class);
	
	public BlockDimension(String registername,Material materialIn,float hardness,float resistant,String harvest_key,int harvest_level,float light,SoundType sound) 
	{
		super(materialIn);
		this.setHardness(hardness);
		this.setResistance(resistant);
		this.setHarvestLevel(harvest_key, harvest_level);
		this.setLightLevel(light);
		this.setSoundType(sound);
		this.setUnlocalizedName(registername);
		this.setRegistryName(Reference.MOD_ID,registername);
		this.setDefaultState(this.blockState.getBaseState().withProperty(DIM, DimensionName.EARTH));
	}	
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(this);
	}
	
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this),1,this.getMetaFromState(worldIn.getBlockState(pos)));
	}
	
	protected ItemStack getSilkTouchDrop(IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this),1,this.getMetaFromState(state));
	}
	
	@Override
	public int damageDropped(IBlockState state) 
	{
		return this.getSilkTouchDrop(state).getMetadata();
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((DimensionName)state.getValue(DIM)).getMeta();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(DIM, DimensionName.getFromMeta(meta));
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state,RayTraceResult target,World world,BlockPos pos,EntityPlayer player)
	{
		return new ItemStack(Item.getItemFromBlock(this),1,this.getMetaFromState(world.getBlockState(pos)));
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> stacks)
	{
		for(DimensionName name : DimensionName.values())
		{
			stacks.add(new ItemStack(Item.getItemFromBlock(this),1,name.getMeta()));
		}
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this,new IProperty[] {DIM});
	}
	
	public String getUnlocalizedName(ItemStack stack)
	{
		return "tile." + DimensionName.getFromMeta(stack.getItemDamage()).getName() + "_" + this.getRegistryName().getResourcePath();
	}
}
