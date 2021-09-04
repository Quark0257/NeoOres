package neo_ores.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import neo_ores.main.NeoOres;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EmptyPortal extends Block
{
	private final boolean inWater;
	protected static final AxisAlignedBB AABB_BOUNDING = new AxisAlignedBB(0.0D,0.0D,0.0D,1.0D,0.8125D,1.0D);
	protected static final AxisAlignedBB AABB_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 0.125D);
    protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 0.8125D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 0.825D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 0.8125D, 1.0D);
	
	public EmptyPortal(boolean water) 
	{
		super(Material.PORTAL);
		this.setSoundType(SoundType.METAL);
		this.setHardness(5.0F);
		this.setHarvestLevel("pickaxe", 0);
		this.setResistance(Float.MAX_VALUE);
		this.inWater = water;
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
	
	public boolean isFullCube(IBlockState iblockstate) 
	{
	    return false;
	}
	
	public boolean isOpaqueCube(IBlockState state)
	{
	    return false;
	}
	
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() 
	{
	    return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if(entityIn.posY - (double)pos.getY() < 0.75D)
		{
			if(this.inWater)
			{
				entityIn.extinguish();
			}
		}
		
		if(entityIn.posY - (double)pos.getY() < 0.8125D && entityIn instanceof EntityItem)
		{
			entityIn.posX = pos.getX() + 0.5D;
			entityIn.posZ = pos.getZ() + 0.5D;
			EntityItem entity = (EntityItem)entityIn;
			entity.setPickupDelay(1);
			entity.setNoDespawn();
			entity.setEntityInvulnerable(true);
		}
	}
	
	public static List<EntityItem> getEntityItemCollided(World world,BlockPos pos)
	{
		List<EntityItem> items = new ArrayList<EntityItem>();
		for(Entity entity : world.getLoadedEntityList())
		{
			if(pos.getX() < entity.posX && entity.posX < pos.getX() + 1 && pos.getY() < entity.posY && entity.posY < pos.getY() + 0.8125D && pos.getZ() < entity.posZ && entity.posZ < pos.getZ() + 1)
			{
				if(entity instanceof EntityItem)
				{
					items.add((EntityItem)entity);
				}
			}
		}
		
		return items;
	}
	
	public static List<ItemStack> getItemCollided(World world,BlockPos pos)
	{
		List<ItemStack> items = new ArrayList<ItemStack>();
		for(EntityItem entity : getEntityItemCollided(world, pos))
		{
			items.add(entity.getItem());
		}
		
		return items;
	}
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		for(EntityItem item : EmptyPortal.getEntityItemCollided(worldIn, pos))
		{
			item.setEntityInvulnerable(false);
			item.lifespan = 6000;
		}
	}
	
	private boolean isCollided(BlockPos sourcepos,Vec3d pos)
	{
		return sourcepos.getX() < pos.x && pos.x < sourcepos.getX() + 1.0D && sourcepos.getY() < pos.y && pos.y < sourcepos.getY() + 0.8125D &&sourcepos.getZ() < pos.z && pos.z < sourcepos.getZ() + 1.0D;
	}
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack itemstack = playerIn.getHeldItem(hand);

        if (itemstack.isEmpty())
        {
        	for(Entity entityIn : worldIn.getLoadedEntityList())
            {
            	if(entityIn instanceof EntityItem && this.isCollided(pos, new Vec3d(entityIn.posX,entityIn.posY,entityIn.posZ)))
            	{
            		EntityItem entity = (EntityItem)entityIn;
            		entity.setNoPickupDelay();
            		entity.setLocationAndAngles(playerIn.posX, playerIn.posY, playerIn.posZ, entity.rotationYaw, entity.rotationPitch);
            	}
            }

            return true;
        }
        else 
        {
        	Item item = itemstack.getItem();
        	
        	if (item == Items.GLASS_BOTTLE)
            {
                if (this.inWater && !worldIn.isRemote)
                {
                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        ItemStack itemstack3 = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
                        playerIn.addStat(StatList.CAULDRON_USED);
                        itemstack.shrink(1);

                        if (itemstack.isEmpty())
                        {
                            playerIn.setHeldItem(hand, itemstack3);
                        }
                        else if (!playerIn.inventory.addItemStackToInventory(itemstack3))
                        {
                            playerIn.dropItem(itemstack3, false);
                        }
                        else if (playerIn instanceof EntityPlayerMP)
                        {
                            ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
                        }
                    }

                    worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    worldIn.setBlockState(pos, NeoOres.empty_portal.getDefaultState());
                }

                return true;
            }
        	else if (item == Items.POTIONITEM && PotionUtils.getPotionFromItem(itemstack) == PotionTypes.WATER)
            {
                if (!this.inWater && !worldIn.isRemote)
                {
                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        ItemStack itemstack2 = new ItemStack(Items.GLASS_BOTTLE);
                        playerIn.addStat(StatList.CAULDRON_USED);
                        playerIn.setHeldItem(hand, itemstack2);

                        if (playerIn instanceof EntityPlayerMP)
                        {
                            ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
                        }
                    }

                    worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    worldIn.setBlockState(pos, NeoOres.empty_portal_water.getDefaultState());
                }

                return true;
            }
        }
		
		return false;
	}
}
