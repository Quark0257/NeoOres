package neo_ores.world.dimension;

import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresBlocks;
import net.minecraft.block.BlockPane;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class NeoOresTeleporter extends Teleporter
{	
	protected final WorldServer worldIn;
	
	private int lastDim;
	private static final String[] reflectionField = new String[] {"invulnerableDimensionChange","field_184851_cj"};

	public NeoOresTeleporter(WorldServer worldIn)
    {
		super(worldIn);
        this.worldIn = worldIn;
    }
	
	public NeoOresTeleporter(WorldServer worldIn,int lastDim)
    {
		super(worldIn);
        this.worldIn = worldIn;
        this.lastDim = lastDim;
    }
	
	@SuppressWarnings("deprecation")
	public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
	{		
		if(entityIn.dimension == NeoOres.THE_WATER.getId() || entityIn.dimension == NeoOres.THE_EARTH.getId() || entityIn.dimension == NeoOres.THE_AIR.getId() || entityIn.dimension == NeoOres.THE_FIRE.getId() || entityIn.dimension == 0)
		{	
			int j = MathHelper.floor(entityIn.posX);
		    int k = MathHelper.floor(entityIn.posY);
		    int l = MathHelper.floor(entityIn.posZ);
	        long pos = ChunkPos.asLong(j, k);
	        
	        if(this.destinationCoordinateCache.containsKey(pos))
	        {
	        	Teleporter.PortalPosition teleporter$portalposition = (Teleporter.PortalPosition)this.destinationCoordinateCache.get(pos);
                BlockPos blockpos = teleporter$portalposition;
                teleporter$portalposition.lastUpdateTime = this.world.getTotalWorldTime();
                if(this.canTeleport(entityIn, blockpos))
                {
                	if (entityIn instanceof EntityPlayerMP)
		    		{
                		ObfuscationReflectionHelper.setPrivateValue(EntityPlayerMP.class, (EntityPlayerMP)entityIn, true, reflectionField);
		    			((EntityPlayerMP)entityIn).setPositionAndUpdate(blockpos.getX() + 0.5D, blockpos.getY() + 1.5D, blockpos.getZ() + 0.5D);
		    			((EntityPlayerMP)entityIn).connection.sendPacket(new SPacketCustomSound(SoundEvents.BLOCK_PORTAL_TRAVEL.getRegistryName().getResourcePath(), SoundCategory.PLAYERS, entityIn.posX, entityIn.posY, entityIn.posZ, 0.5F, worldIn.provider.getDimension() == 0 ? 1.0F : this.soundPitch()));
		            }
		            else
		            {               	
		                entityIn.setLocationAndAngles(blockpos.getX() + 0.5D, blockpos.getY() + 1.5D, blockpos.getZ() + 0.5D, entityIn.rotationYaw, entityIn.rotationPitch);
		            }
		    		return true;
                }
                else
                {
                	this.destinationCoordinateCache.remove(pos);
                }
	        }
	        
			int range = 64;
			int d = 0;
			int x = 0;
			int y = 0;
			int z= 0;
			for(d = 0;d < range;d++)
			{
				for(x = -d;x < d + 1;x++)
				{
					for(z = -(d - Math.abs(x));z < d - Math.abs(x) + 1;z++)
					{
						for(int flagY = -1;flagY < 2;flagY += 2)
						{
							y = d - Math.abs(x) - Math.abs(z);
							y *= flagY;
							if(canTeleport(entityIn, new BlockPos(j + x, k + y, l + z)))
					    	{
			    				this.destinationCoordinateCache.put(pos, new PortalPosition(new BlockPos(j + x, k + y, l + z), this.world.getTotalWorldTime()));
					    		if (entityIn instanceof EntityPlayerMP)
					    		{
					    			ObfuscationReflectionHelper.setPrivateValue(EntityPlayerMP.class, (EntityPlayerMP)entityIn, true, reflectionField);
					    			((EntityPlayerMP)entityIn).setPositionAndUpdate(j + x + 0.5D, k + y + 1.5D, l + z + 0.5D);
					    			//((EntityPlayerMP)entityIn).connection.setPlayerLocation(j + x + 0.5D, k + y + 1.5D, l + z + 0.5D, entityIn.rotationYaw, entityIn.rotationPitch);
					    			((EntityPlayerMP)entityIn).connection.sendPacket(new SPacketCustomSound(SoundEvents.BLOCK_PORTAL_TRAVEL.getRegistryName().getResourcePath(), SoundCategory.PLAYERS, entityIn.posX, entityIn.posY, entityIn.posZ, 0.5F, worldIn.provider.getDimension() == 0 ? 1.0F : this.soundPitch()));
					            }
					            else
					            {               	
					                entityIn.setLocationAndAngles(j + x + 0.5D, k + y + 1.5D, l + z + 0.5D, entityIn.rotationYaw, entityIn.rotationPitch);
					            }
					    		return true;
					    	}
						}
					}
				}
			}

		    BlockPos tpp = makeCustomPortal(entityIn,this.lastDim);
		    this.destinationCoordinateCache.put(pos, new PortalPosition(tpp.add(0, -1, 0), this.world.getTotalWorldTime()));
		    if (entityIn instanceof EntityPlayerMP)
            {
		    	ObfuscationReflectionHelper.setPrivateValue(EntityPlayerMP.class, (EntityPlayerMP)entityIn, true, reflectionField);
                ((EntityPlayerMP)entityIn).setPositionAndUpdate((double)tpp.getX() + 0.5D, tpp.getY() + 0.5D, (double)tpp.getZ() + 0.5D);
		    	//((EntityPlayerMP)entityIn).connection.setPlayerLocation((double)tpp.getX() + 0.5D, tpp.getY() + 0.5D, (double)tpp.getZ() + 0.5D, entityIn.rotationYaw, entityIn.rotationPitch);
                ((EntityPlayerMP)entityIn).connection.sendPacket(new SPacketCustomSound(SoundEvents.BLOCK_PORTAL_TRAVEL.getRegistryName().getResourcePath(), SoundCategory.PLAYERS, entityIn.posX, entityIn.posY, entityIn.posZ, 0.5F, worldIn.provider.getDimension() == 0 ? 1.0F : this.soundPitch()));
            }
            else
            {               	
                entityIn.setLocationAndAngles((double)tpp.getX() + 0.5D, tpp.getY(), (double)tpp.getZ() + 0.5D, entityIn.rotationYaw, entityIn.rotationPitch);
            }
			return true;
		}
		
		return false;
	}
	
	public boolean makePortal(Entity entityIn)
	{ 
		return true;
	}
	
	public BlockPos makeCustomPortal(Entity entityIn,int lastDim)
	{
		int j = MathHelper.floor(entityIn.posX);
	    int k = MathHelper.floor(entityIn.posY);
	    int l = MathHelper.floor(entityIn.posZ);
	    
	    int range = 20;
		int d = 0;
		int x = 0;
		int y = 0;
		int z= 0;
		boolean flag = false;
		for(d = 0;d < range;d++)
		{
			for(x = -d;x < d + 1;x++)
			{
				for(z = -(d - Math.abs(x));z < d - Math.abs(x) + 1;z++)
				{
					for(int flagY = -1;flagY < 2;flagY += 2)
					{
						y = d - Math.abs(x) - Math.abs(z);
						y *= flagY;
						if(isSolidBlock(new BlockPos(j + x,k + y,l + z)) && !isSolidBlock(new BlockPos(j + x,k + y + 1,l + z)) && !isSolidBlock(new BlockPos(j + x,k + y + 2,l + z)) && !isSolidBlock(new BlockPos(j + x,k + y + 3,l + z)))
			    		{
			    			j = j + x;
			    			k = k + y + 1;
			    			l = l + z;
			    			flag = true;
			    			break;
			    		}
					}
					if(flag) break;
				}
				if(flag) break;
			}
			if(flag) break;
		}
		
		worldIn.setBlockToAir(new BlockPos(j,k,l));
		worldIn.setBlockToAir(new BlockPos(j,k + 1,l));
	    
	    worldIn.setBlockState(new BlockPos(j - 1, k - 1, l - 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    worldIn.setBlockState(new BlockPos(j - 1, k - 1, l), Blocks.OBSIDIAN.getDefaultState(), 3);
	    worldIn.setBlockState(new BlockPos(j - 1, k - 1, l + 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    worldIn.setBlockState(new BlockPos(j, k - 1, l - 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    
	    if(isEarthPhase()) 
	    {
	    	worldIn.setBlockState(new BlockPos(j, k - 1, l), NeoOresBlocks.earth_portal.getDefaultState(), 3);
	    }
	    else if(isWaterPhase()) 
	    {
	    	worldIn.setBlockState(new BlockPos(j, k - 1, l), NeoOresBlocks.water_portal.getDefaultState(), 3);
	    }
	    else if(isAirPhase()) 
	    {
	    	worldIn.setBlockState(new BlockPos(j, k - 1, l), NeoOresBlocks.air_portal.getDefaultState(), 3);
	    }
	    else if(isFirePhase()) 
	    {
	    	worldIn.setBlockState(new BlockPos(j, k - 1, l), NeoOresBlocks.fire_portal.getDefaultState(), 3);
	    }
	    
	    worldIn.setBlockState(new BlockPos(j, k - 1, l + 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    worldIn.setBlockState(new BlockPos(j + 1, k - 1, l - 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    worldIn.setBlockState(new BlockPos(j + 1, k - 1, l), Blocks.OBSIDIAN.getDefaultState(), 3);
	    worldIn.setBlockState(new BlockPos(j + 1, k - 1, l + 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    
	    worldIn.setBlockState(new BlockPos(j - 1, k + 2, l - 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    worldIn.setBlockState(new BlockPos(j - 1, k + 2, l), Blocks.OBSIDIAN.getDefaultState(), 3);
	    worldIn.setBlockState(new BlockPos(j - 1, k + 2, l + 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    worldIn.setBlockState(new BlockPos(j, k + 2, l - 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    worldIn.setBlockState(new BlockPos(j, k + 2, l), Blocks.GLASS.getDefaultState(), 3);
	    worldIn.setBlockState(new BlockPos(j, k + 2, l + 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    worldIn.setBlockState(new BlockPos(j + 1, k + 2, l - 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    worldIn.setBlockState(new BlockPos(j + 1, k + 2, l), Blocks.OBSIDIAN.getDefaultState(), 3);
	    worldIn.setBlockState(new BlockPos(j + 1, k + 2, l + 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    
	    worldIn.setBlockState(new BlockPos(j - 1, k, l - 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(true)).withProperty(BlockPane.SOUTH, Boolean.valueOf(true)).withProperty(BlockPane.WEST, Boolean.valueOf(false)),3);
	    worldIn.setBlockState(new BlockPos(j - 1, k, l), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(true)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(true)).withProperty(BlockPane.WEST, Boolean.valueOf(false)),3);
	    worldIn.setBlockState(new BlockPos(j - 1, k, l + 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(true)).withProperty(BlockPane.EAST, Boolean.valueOf(true)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(false)),3);
	    worldIn.setBlockState(new BlockPos(j, k, l - 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(true)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(true)),3);
	    worldIn.setBlockState(new BlockPos(j, k, l + 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(true)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(true)),3);
	    worldIn.setBlockState(new BlockPos(j + 1, k, l - 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(true)).withProperty(BlockPane.WEST, Boolean.valueOf(true)),3);
	    worldIn.setBlockState(new BlockPos(j + 1, k, l), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(true)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(true)).withProperty(BlockPane.WEST, Boolean.valueOf(false)),3);
	    worldIn.setBlockState(new BlockPos(j + 1, k, l + 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(true)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(true)),3);
	    
	    worldIn.setBlockState(new BlockPos(j - 1, k + 1, l - 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(true)).withProperty(BlockPane.SOUTH, Boolean.valueOf(true)).withProperty(BlockPane.WEST, Boolean.valueOf(false)),3);
	    worldIn.setBlockState(new BlockPos(j - 1, k + 1, l), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(true)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(true)).withProperty(BlockPane.WEST, Boolean.valueOf(false)),3);
	    worldIn.setBlockState(new BlockPos(j - 1, k + 1, l + 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(true)).withProperty(BlockPane.EAST, Boolean.valueOf(true)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(false)),3);
	    worldIn.setBlockState(new BlockPos(j, k + 1, l - 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(true)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(true)),3);
	    worldIn.setBlockState(new BlockPos(j, k + 1, l + 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(true)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(true)),3);
	    worldIn.setBlockState(new BlockPos(j + 1, k + 1, l - 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(true)).withProperty(BlockPane.WEST, Boolean.valueOf(true)),3);
	    worldIn.setBlockState(new BlockPos(j + 1, k + 1, l), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(true)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(true)).withProperty(BlockPane.WEST, Boolean.valueOf(false)),3);
	    worldIn.setBlockState(new BlockPos(j + 1, k + 1, l + 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(true)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(true)),3);
	    
	    return new BlockPos(j,k,l);
	}
	
	private boolean isEarthPhase()
	{
		return (worldIn.provider.getDimension() == NeoOres.THE_EARTH.getId() && lastDim == 0) || (worldIn.provider.getDimension() == 0 && lastDim == NeoOres.THE_EARTH.getId());
	}
	
	private boolean isWaterPhase()
	{
		return (worldIn.provider.getDimension() == NeoOres.THE_WATER.getId() && lastDim == 0) || (worldIn.provider.getDimension() == 0 && lastDim == NeoOres.THE_WATER.getId());
	}
	
	private boolean isAirPhase()
	{
		return (worldIn.provider.getDimension() == NeoOres.THE_AIR.getId() && lastDim == 0) || (worldIn.provider.getDimension() == 0 && lastDim == NeoOres.THE_AIR.getId());
	}
	
	private boolean isFirePhase()
	{
		return (worldIn.provider.getDimension() == NeoOres.THE_FIRE.getId() && lastDim == 0) || (worldIn.provider.getDimension() == 0 && lastDim == NeoOres.THE_FIRE.getId());
	}
	
	private boolean canTeleport(Entity entity, BlockPos portalpos)
	{
		return (isEarthPhase() && worldIn.getBlockState(portalpos).getBlock() == NeoOresBlocks.earth_portal) || (isWaterPhase() && worldIn.getBlockState(portalpos).getBlock() == NeoOresBlocks.water_portal) || (isAirPhase() && worldIn.getBlockState(portalpos).getBlock() == NeoOresBlocks.air_portal) || (isFirePhase() && worldIn.getBlockState(portalpos).getBlock() == NeoOresBlocks.fire_portal);
	}
	
	private boolean isSolidBlock(BlockPos pos)
	{
		return (worldIn.getBlockState(pos).getBlock() != null && worldIn.getBlockState(pos).getBlock() != ChunkGeneratorTheAir.SUBBLOCK.getBlock() && worldIn.getBlockState(pos).getBlock() != ChunkGeneratorTheFire.SUBBLOCK.getBlock() && worldIn.getBlockState(pos).getBlock() != ChunkGeneratorTheWater.SUBBLOCK.getBlock() && worldIn.getBlockState(pos).getBlock() != ChunkGeneratorTheEarth.SUBBLOCK.getBlock());
	}
	
	private float soundPitch()
	{
		if(isEarthPhase()) 
	    {
	    	return 0.5F;
	    }
	    else if(isWaterPhase()) 
	    {
	    	return 0.75F;
	    }
	    else if(isAirPhase()) 
	    {
	    	return 1.25F;
	    }
	    else if(isFirePhase()) 
	    {
	    	return 1.5F;
	    }
		return 1.0F;
	}
}
