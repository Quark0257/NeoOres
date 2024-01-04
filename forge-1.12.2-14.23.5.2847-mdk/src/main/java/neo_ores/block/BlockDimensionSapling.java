package neo_ores.block;

import java.util.Random;

import neo_ores.item.ItemBlockDimension;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.Reference;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import neo_ores.world.gen.WorldGenMegaTree1;
import neo_ores.world.gen.WorldGenMegaTree2;
import neo_ores.world.gen.WorldGenSeeweedTree;
import neo_ores.world.gen.WorldGenSimpleTree;
import neo_ores.world.gen.WorldGenTreeBig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.EnumPlantType;

public class BlockDimensionSapling extends BlockBush implements INeoOresBlock, IGrowable 
{
	protected final Block sustainState;
	public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
	protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
	
	public BlockDimensionSapling(String registername,Material materialIn, Block sustainStates, float hardness, float resistant, float light, SoundType sound) 
	{
		super(materialIn);
		this.setHardness(hardness);
		this.setResistance(resistant);
		this.setLightLevel(light);
		this.setSoundType(sound);
		this.setUnlocalizedName(registername);
		this.setRegistryName(Reference.MOD_ID,registername);
		this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE, 0));
		this.sustainState = sustainStates;
		this.setTickRandomly(true);
	}
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return SAPLING_AABB;
    }
	
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
		IBlockState soil = worldIn.getBlockState(pos.down());
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && (this.sustainState != null ? soil.getBlock() == this.sustainState : soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), EnumFacing.UP, this));
    }
	
	protected boolean canSustainBush(IBlockState state)
    {
        return this.sustainState != null ? state.getBlock() == this.sustainState : (state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT);
    }

    private boolean isTwoByTwoOfType(World worldIn, BlockPos pos, int p_181624_3_, int p_181624_4_)
    {
        return this.isTypeAt(worldIn, pos.add(p_181624_3_, 0, p_181624_4_)) && this.isTypeAt(worldIn, pos.add(p_181624_3_ + 1, 0, p_181624_4_)) && this.isTypeAt(worldIn, pos.add(p_181624_3_, 0, p_181624_4_ + 1)) && this.isTypeAt(worldIn, pos.add(p_181624_3_ + 1, 0, p_181624_4_ + 1));
    }
    
    public boolean isTypeAt(World worldIn, BlockPos pos)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock() == this;
    }
    
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) 
    {
    	WorldGenerator worldgenerator = null;
    	int i = 0;
    	int j = 0;
    	boolean flag = false;
    	label: 
    	{
    		for (i = 0; i >= -1; --i)
            {
                for (j = 0; j >= -1; --j)
                {
                    if (this == NeoOresBlocks.air_sapling && this.isTwoByTwoOfType(worldIn, pos, i, j))
                    {
                        worldgenerator = WorldGenMegaTree1.make(false, true, NeoOresBlocks.dim_log, NeoOresBlocks.dim_leaves, DimensionName.AIR);
                        flag = true;
                        break label;
                    }
                    
                    if (this == NeoOresBlocks.corroded_air_sapling && this.isTwoByTwoOfType(worldIn, pos, i, j))
                    {
                        worldgenerator = WorldGenMegaTree1.make(false, true, NeoOresBlocks.dim_log, NeoOresBlocks.corroded_dim_leaves, DimensionName.AIR);
                        flag = true;
                        break label;
                    }
                    
                    if (this == NeoOresBlocks.corroding_air_sapling && this.isTwoByTwoOfType(worldIn, pos, i, j))
                    {
                        worldgenerator = WorldGenMegaTree1.make(false, true, NeoOresBlocks.dim_log, NeoOresBlocks.corroding_dim_leaves, DimensionName.AIR);
                        flag = true;
                        break label;
                    }
                }
            }
    	}
    	i = 0;
    	j = 0;
    	flag = false;
    			
    	label: 
    	{
    		for (i = 0; i >= -1; --i)
            {
                for (j = 0; j >= -1; --j)
                {
                    if (this == NeoOresBlocks.earth_sapling && this.isTwoByTwoOfType(worldIn, pos, i, j))
                    {
                        worldgenerator = WorldGenMegaTree2.make(false, 10, 13, NeoOresBlocks.dim_log, NeoOresBlocks.dim_leaves, DimensionName.EARTH);
                        flag = true;
                        break label;
                    }
                    
                    if (this == NeoOresBlocks.corroded_earth_sapling && this.isTwoByTwoOfType(worldIn, pos, i, j))
                    {
                        worldgenerator = WorldGenMegaTree2.make(false, 10, 13, NeoOresBlocks.dim_log, NeoOresBlocks.corroded_dim_leaves, DimensionName.EARTH);
                        flag = true;
                        break label;
                    }
                    
                    if (this == NeoOresBlocks.corroding_earth_sapling && this.isTwoByTwoOfType(worldIn, pos, i, j))
                    {
                        worldgenerator = WorldGenMegaTree2.make(false, 10, 13, NeoOresBlocks.dim_log, NeoOresBlocks.corroding_dim_leaves, DimensionName.EARTH);
                        flag = true;
                        break label;
                    }
                }
            }
    	}
        i = 0;
        j = 0;
        
        if (!flag && this == NeoOresBlocks.earth_sapling)
        {
            worldgenerator = WorldGenSimpleTree.make(false, 6, 3, NeoOresBlocks.dim_log, NeoOresBlocks.dim_leaves, DimensionName.EARTH);
        }
        
        if (!flag && this == NeoOresBlocks.corroded_earth_sapling)
        {
            worldgenerator = WorldGenSimpleTree.make(false, 6, 3, NeoOresBlocks.dim_log, NeoOresBlocks.corroded_dim_leaves, DimensionName.EARTH);
        }
        
        if (!flag && this == NeoOresBlocks.corroding_earth_sapling)
        {
            worldgenerator = WorldGenSimpleTree.make(false, 6, 3, NeoOresBlocks.dim_log, NeoOresBlocks.corroding_dim_leaves, DimensionName.EARTH);
        }
    	
    	if(this == NeoOresBlocks.corroding_fire_sapling) {
    		worldgenerator = WorldGenTreeBig.make(true, NeoOresBlocks.dim_log, NeoOresBlocks.corroding_dim_leaves, DimensionName.FIRE);
    	}
    	
    	if(this == NeoOresBlocks.fire_sapling) {
    		worldgenerator = WorldGenTreeBig.make(true, NeoOresBlocks.dim_log, NeoOresBlocks.dim_leaves, DimensionName.FIRE);
    	}
    	
    	if(this == NeoOresBlocks.corroded_fire_sapling) {
    		worldgenerator = WorldGenTreeBig.make(true, NeoOresBlocks.dim_log, NeoOresBlocks.corroded_dim_leaves, DimensionName.FIRE);
    	}
    	
    	if(this == NeoOresBlocks.corroding_water_sapling) {
    		worldgenerator = WorldGenSeeweedTree.make(true, 7, 5, NeoOresBlocks.dim_log, NeoOresBlocks.corroding_dim_leaves, DimensionName.WATER);
    	}
    	
    	if(this == NeoOresBlocks.water_sapling) {
    		worldgenerator = WorldGenSeeweedTree.make(true, 7, 5, NeoOresBlocks.dim_log, NeoOresBlocks.dim_leaves, DimensionName.WATER);
    	}
    	
    	if(this == NeoOresBlocks.corroded_water_sapling) {
    		worldgenerator = WorldGenSeeweedTree.make(true, 7, 5, NeoOresBlocks.dim_log, NeoOresBlocks.corroded_dim_leaves, DimensionName.WATER);
    	}

    	if(worldgenerator == null) return;
    	IBlockState iblockstate2 = Blocks.AIR.getDefaultState();

        if (flag)
        {
            worldIn.setBlockState(pos.add(i, 0, j), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i, 0, j + 1), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j + 1), iblockstate2, 4);
        }
        else
        {
            worldIn.setBlockState(pos, iblockstate2, 4);
        }

        if (!worldgenerator.generate(worldIn, rand, pos.add(i, 0, j)))
        {
            if (flag)
            {
                worldIn.setBlockState(pos.add(i, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i, 0, j + 1), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j + 1), state, 4);
            }
            else
            {
                worldIn.setBlockState(pos, state, 4);
            }
        }
    }
    
    public String getUnlocalizedName(ItemStack stack)
	{
		return "tile." + this.getRegistryName().getResourcePath();
	}
	
	public int getMaxMeta()
	{
		return 0;
	}
	
	public ModelResourceLocation getModel(int meta)
	{
		return new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "base_sapling"),"inventory");
	}

	public Item getItemBlock(Block block)
	{
		return new ItemBlockDimension((BlockDimensionSapling)block).setRegistryName(block.getRegistryName());
	}
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(this);
	}
	
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(STAGE, Integer.valueOf((meta % 2)));
    }

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i += ((Integer)state.getValue(STAGE)).intValue();
        return i;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {STAGE});
    }
    
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return (double)worldIn.rand.nextFloat() < 0.45D;
    }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        this.grow(worldIn, pos, state, rand);
    }
    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            super.updateTick(worldIn, pos, state, rand);

            if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
            {
                this.grow(worldIn, pos, state, rand);
            }
        }
    }

    public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (((Integer)state.getValue(STAGE)).intValue() == 0)
        {
            worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
        }
        else
        {
            this.generateTree(worldIn, pos, state, rand);
        }
    }
    
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getBlock() == this) //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
        {
            IBlockState soil = worldIn.getBlockState(pos.down());
            return (this.sustainState != null ? soil.getBlock() == this.sustainState : soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), EnumFacing.UP, this));
        }
        return this.canSustainBush(worldIn.getBlockState(pos.down()));
    }
    
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
    	return EnumPlantType.Plains;
    }
}
