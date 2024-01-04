package neo_ores.block;

import java.util.List;

import javax.annotation.Nullable;

import neo_ores.main.NeoOres;
import neo_ores.main.Reference;
import neo_ores.tileentity.TileEntityNeoPortal;
import neo_ores.world.dimension.NeoOresTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;

public class BlockNeoOresPortal extends BlockContainer implements INeoOresBlock
{
	private final int meta;
	protected static final AxisAlignedBB AABB_BOUNDING = new AxisAlignedBB(0.0D,0.0D,0.0D,1.0D,0.8125D,1.0D);
	protected static final AxisAlignedBB AABB_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 0.125D);
    protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 0.8125D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 0.825D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 0.8125D, 1.0D);
	
	public BlockNeoOresPortal(int meta) 
	{
		super(Material.PORTAL);
		this.setSoundType(SoundType.METAL);
		this.setLightLevel(1.0F);
		this.setHardness(5.0F);
		this.setHarvestLevel("pickaxe", 0);
		this.setResistance(Float.MAX_VALUE);
		this.meta = meta;
	}
	
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BOTTOM);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH);
    }
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB_BOUNDING;
	}
	
	@Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) 
	{
		if(entityIn.posY - (double)pos.getY() < 0.75D)
		{
			DimensionType dimension = NeoOres.THE_EARTH;
			
			switch(this.meta)
			{
				case 0: dimension = NeoOres.THE_EARTH;
				break;
				case 1: dimension = NeoOres.THE_WATER;
				break;
				case 2: dimension = NeoOres.THE_AIR;
				break;
				case 3: dimension = NeoOres.THE_FIRE;
				break;
			}
			
			if(!this.isMatch(entityIn.dimension, dimension.getId()) && entityIn.dimension != 0)
			{
				return;
			}
			
	        MinecraftServer server = worldIn.getMinecraftServer();
	        
	        if (server != null && entityIn.isSneaking()) 
	        {
	            PlayerList playerList = server.getPlayerList();
	            int dest = entityIn.dimension == DimensionType.OVERWORLD.getId() ? dimension.getId() : DimensionType.OVERWORLD.getId();

	            Teleporter teleporter = new NeoOresTeleporter(server.getWorld(dest),entityIn.dimension);

	            entityIn.setSneaking(false);
	            
	            if (entityIn instanceof EntityPlayerMP) 
	            {
	                playerList.transferPlayerToDimension((EntityPlayerMP) entityIn, dest, teleporter);
	            }
	            else 
	            {
	                int origin = entityIn.dimension;
	                entityIn.dimension = dest;
	                worldIn.removeEntityDangerously(entityIn);

	                entityIn.isDead = false;
	                
	                playerList.transferEntityToWorld(entityIn, origin, server.getWorld(origin), server.getWorld(dest), teleporter);
	            }
	        }
		}
    }
	
	private boolean isMatch(int source,int meta)
	{
		return source == meta;
	}
	
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
		return BlockFaceShape.SOLID;
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityNeoPortal();
	}
	
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	public boolean isFullCube(IBlockState iblockstate) 
	{
	    return false;
	}
	
	public boolean isOpaqueCube(IBlockState state)
	{
	    return false;
	}

	public ModelResourceLocation getModel(int meta)
	{
		return new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID,this.getRegistryName().getResourcePath()),"inventory");
	}
	
	//0~15 available
	public int getMaxMeta()
	{
		return 0;
	}
	
	public Item getItemBlock(Block block)
	{
		return new ItemBlock(block).setRegistryName(block.getRegistryName());
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return "tile." + this.getRegistryName().getResourcePath();
	}
}
