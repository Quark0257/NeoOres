package neo_ores.world.dimension;

import neo_ores.main.NeoOres;
import net.minecraft.block.BlockPane;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class NeoOresTeleporter extends Teleporter
{	
	protected final WorldServer worldIn;
	
	private int lastDim;

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
	
	public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
	{		
		if(entityIn.dimension == NeoOres.THE_WATER.getId() || entityIn.dimension == NeoOres.THE_EARTH.getId() || entityIn.dimension == NeoOres.THE_AIR.getId() || entityIn.dimension == NeoOres.THE_FIRE.getId() || entityIn.dimension == 0)
		{
			int j = MathHelper.floor(entityIn.posX);
		    int k = MathHelper.floor(entityIn.posY);
		    int l = MathHelper.floor(entityIn.posZ);
		    
		    for(int d = 0;d < 128;d++)
		    {
		    	for(int a = 0;a < 128;a++)
			    {
			    	for(int aC = 0;aC < 2;aC++)
			    	{
			    		int y = a;		    		
			    		if(aC == 1)
			    		{
			    			y = -a;	    			
			    		}
			    		
			    		for(int b = 0;b < 128;b++)
					    {
					    	for(int bC = 0;bC < 2;bC++)
					    	{
					    		int x = b;
					    		if(bC == 1)
					    		{
					    			x = -b;
					    		}
					    		
					    		for(int c = 0;c < 128;c++)
							    {
							    	for(int cC = 0;cC < 2;cC++)
							    	{
							    		int z = c;
							    		if(cC == 1)
							    		{
							    			z = -c;
							    		}
							    		
							    		if(x * x + y * y + z * z <= (d + 1) * (d + 1))
							    		{
							    			if(this.canTeleport(entityIn, new BlockPos(j + x, k + y, l + z)))
									    	{
									    		if (entityIn instanceof EntityPlayerMP)
									            {
									                ((EntityPlayerMP)entityIn).connection.setPlayerLocation(j + x + 0.5D, k + y + 1, l + z + 0.5D, entityIn.rotationYaw, entityIn.rotationPitch);
									            }
									            else
									            {               	
									                entityIn.setLocationAndAngles(j + x + 0.5D, k + y + 1, l + z + 0.5D, entityIn.rotationYaw, entityIn.rotationPitch);
									            }
									    		
									    		return true;
									    	}
							    		}
							    	}
							    }
					    	}
					    }
			    	}
			    }
		    }
		    
		    BlockPos tpp = this.makeWaterPortal(entityIn);
		    
		    if (entityIn instanceof EntityPlayerMP)
            {
                ((EntityPlayerMP)entityIn).connection.setPlayerLocation((double)tpp.getX() + 0.5D, tpp.getY(), (double)tpp.getZ() + 0.5D, entityIn.rotationYaw, entityIn.rotationPitch);
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
	
	public BlockPos makeWaterPortal(Entity entityIn)
	{
		int j = MathHelper.floor(entityIn.posX);
	    int k = MathHelper.floor(entityIn.posY);
	    int l = MathHelper.floor(entityIn.posZ);
	    
	    for(int d = 0;d < 16;d++)
	    {
	    	for(int a = 0;a < 16;a++)
		    {
		    	for(int aC = 0;aC < 2;aC++)
		    	{
		    		int y = a;
		    		if(aC == 1)
		    		{
		    			y = -a;	    			
		    		}
		    		
		    		for(int b = 0;b < 16;b++)
				    {
				    	for(int bC = 0;bC < 2;bC++)
				    	{
				    		int x = b;
				    		if(bC == 1)
				    		{
				    			x = -b;
				    		}
				    		
				    		for(int c = 0;c < 16;c++)
						    {
						    	for(int cC = 0;cC < 2;cC++)
						    	{
						    		int z = c;
						    		if(cC == 1)
						    		{
						    			z = -c;
						    		}
						    		
						    		if(x * x + y * y + z * z <= (d + 1) * (d + 1))
						    		{
						    			if(this.isSolidBlock(new BlockPos(j + x,k + y,l + z)) && !this.isSolidBlock(new BlockPos(j + x,k + y + 1,l + z)) && !this.isSolidBlock(new BlockPos(j + x,k + y + 2,l + z)) && !this.isSolidBlock(new BlockPos(j + x,k + y + 3,l + z)))
							    		{
							    			j = j + x;
							    			k = k + y + 1;
							    			l = l + z;
							    		}
						    		}
						    	}
						    }
				    	}
				    }
		    	}
		    }
	    }
		
		this.worldIn.setBlockToAir(new BlockPos(j,k,l));
	    this.worldIn.setBlockToAir(new BlockPos(j,k + 1,l));
	    
	    this.worldIn.setBlockState(new BlockPos(j - 1, k - 1, l - 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    this.worldIn.setBlockState(new BlockPos(j - 1, k - 1, l), Blocks.OBSIDIAN.getDefaultState(), 3);
	    this.worldIn.setBlockState(new BlockPos(j - 1, k - 1, l + 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    this.worldIn.setBlockState(new BlockPos(j, k - 1, l - 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    
	    if(this.isEarthPhase(entityIn)) 
	    {
	    	this.worldIn.setBlockState(new BlockPos(j, k - 1, l), NeoOres.earth_portal.getDefaultState(), 3);
	    }
	    else if(this.isWaterPhase(entityIn)) 
	    {
	    	this.worldIn.setBlockState(new BlockPos(j, k - 1, l), NeoOres.water_portal.getDefaultState(), 3);
	    }
	    else if(this.isAirPhase(entityIn)) 
	    {
	    	this.worldIn.setBlockState(new BlockPos(j, k - 1, l), NeoOres.air_portal.getDefaultState(), 3);
	    }
	    else if(this.isFirePhase(entityIn)) 
	    {
	    	this.worldIn.setBlockState(new BlockPos(j, k - 1, l), NeoOres.fire_portal.getDefaultState(), 3);
	    }
	    
	    this.worldIn.setBlockState(new BlockPos(j, k - 1, l + 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    this.worldIn.setBlockState(new BlockPos(j + 1, k - 1, l - 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    this.worldIn.setBlockState(new BlockPos(j + 1, k - 1, l), Blocks.OBSIDIAN.getDefaultState(), 3);
	    this.worldIn.setBlockState(new BlockPos(j + 1, k - 1, l + 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    
	    this.worldIn.setBlockState(new BlockPos(j - 1, k + 2, l - 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    this.worldIn.setBlockState(new BlockPos(j - 1, k + 2, l), Blocks.OBSIDIAN.getDefaultState(), 3);
	    this.worldIn.setBlockState(new BlockPos(j - 1, k + 2, l + 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    this.worldIn.setBlockState(new BlockPos(j, k + 2, l - 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    this.worldIn.setBlockState(new BlockPos(j, k + 2, l), Blocks.OBSIDIAN.getDefaultState(), 3);
	    this.worldIn.setBlockState(new BlockPos(j, k + 2, l + 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    this.worldIn.setBlockState(new BlockPos(j + 1, k + 2, l - 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    this.worldIn.setBlockState(new BlockPos(j + 1, k + 2, l), Blocks.OBSIDIAN.getDefaultState(), 3);
	    this.worldIn.setBlockState(new BlockPos(j + 1, k + 2, l + 1), Blocks.OBSIDIAN.getDefaultState(), 3);
	    
	    this.worldIn.setBlockState(new BlockPos(j - 1, k, l - 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(true)).withProperty(BlockPane.SOUTH, Boolean.valueOf(true)).withProperty(BlockPane.WEST, Boolean.valueOf(false)),3);
	    this.worldIn.setBlockState(new BlockPos(j - 1, k, l), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(true)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(true)).withProperty(BlockPane.WEST, Boolean.valueOf(false)),3);
	    this.worldIn.setBlockState(new BlockPos(j - 1, k, l + 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(true)).withProperty(BlockPane.EAST, Boolean.valueOf(true)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(false)),3);
	    this.worldIn.setBlockState(new BlockPos(j, k, l - 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(true)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(true)),3);
	    this.worldIn.setBlockState(new BlockPos(j, k, l + 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(true)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(true)),3);
	    this.worldIn.setBlockState(new BlockPos(j + 1, k, l - 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(true)).withProperty(BlockPane.WEST, Boolean.valueOf(true)),3);
	    this.worldIn.setBlockState(new BlockPos(j + 1, k, l), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(true)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(true)).withProperty(BlockPane.WEST, Boolean.valueOf(false)),3);
	    this.worldIn.setBlockState(new BlockPos(j + 1, k, l + 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(true)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(true)),3);
	    
	    this.worldIn.setBlockState(new BlockPos(j - 1, k + 1, l - 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(true)).withProperty(BlockPane.SOUTH, Boolean.valueOf(true)).withProperty(BlockPane.WEST, Boolean.valueOf(false)),3);
	    this.worldIn.setBlockState(new BlockPos(j - 1, k + 1, l), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(true)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(true)).withProperty(BlockPane.WEST, Boolean.valueOf(false)),3);
	    this.worldIn.setBlockState(new BlockPos(j - 1, k + 1, l + 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(true)).withProperty(BlockPane.EAST, Boolean.valueOf(true)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(false)),3);
	    this.worldIn.setBlockState(new BlockPos(j, k + 1, l - 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(true)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(true)),3);
	    this.worldIn.setBlockState(new BlockPos(j, k + 1, l + 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(true)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(true)),3);
	    this.worldIn.setBlockState(new BlockPos(j + 1, k + 1, l - 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(false)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(true)).withProperty(BlockPane.WEST, Boolean.valueOf(true)),3);
	    this.worldIn.setBlockState(new BlockPos(j + 1, k + 1, l), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(true)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(true)).withProperty(BlockPane.WEST, Boolean.valueOf(false)),3);
	    this.worldIn.setBlockState(new BlockPos(j + 1, k + 1, l + 1), Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.NORTH, Boolean.valueOf(true)).withProperty(BlockPane.EAST, Boolean.valueOf(false)).withProperty(BlockPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockPane.WEST, Boolean.valueOf(true)),3);
	    
	    return new BlockPos(j,k,l);
	}
	
	private boolean isEarthPhase(Entity entity)
	{
		return (this.worldIn.provider.getDimension() == NeoOres.THE_EARTH.getId() && this.lastDim == 0) || (this.worldIn.provider.getDimension() == 0 && this.lastDim == NeoOres.THE_EARTH.getId());
	}
	
	private boolean isWaterPhase(Entity entity)
	{
		return (this.worldIn.provider.getDimension() == NeoOres.THE_WATER.getId() && this.lastDim == 0) || (this.worldIn.provider.getDimension() == 0 && this.lastDim == NeoOres.THE_WATER.getId());
	}
	
	private boolean isAirPhase(Entity entity)
	{
		return (this.worldIn.provider.getDimension() == NeoOres.THE_AIR.getId() && this.lastDim == 0) || (this.worldIn.provider.getDimension() == 0 && this.lastDim == NeoOres.THE_AIR.getId());
	}
	
	private boolean isFirePhase(Entity entity)
	{
		return (this.worldIn.provider.getDimension() == NeoOres.THE_FIRE.getId() && this.lastDim == 0) || (this.worldIn.provider.getDimension() == 0 && this.lastDim == NeoOres.THE_FIRE.getId());
	}
	
	private boolean canTeleport(Entity entity, BlockPos portalpos)
	{
		return (this.isEarthPhase(entity) && worldIn.getBlockState(portalpos).getBlock() == NeoOres.earth_portal) || (this.isWaterPhase(entity) && worldIn.getBlockState(portalpos).getBlock() == NeoOres.water_portal) || (this.isAirPhase(entity) && worldIn.getBlockState(portalpos).getBlock() == NeoOres.air_portal) || (this.isFirePhase(entity) && worldIn.getBlockState(portalpos).getBlock() == NeoOres.fire_portal);
	}
	
	private boolean isSolidBlock(BlockPos pos)
	{
		return (this.worldIn.getBlockState(pos).getBlock() != null && this.worldIn.getBlockState(pos).getBlock() != ChunkGeneratorTheAir.SUBBLOCK.getBlock() && this.worldIn.getBlockState(pos).getBlock() != ChunkGeneratorTheFire.SUBBLOCK.getBlock() && this.worldIn.getBlockState(pos).getBlock() != ChunkGeneratorTheWater.SUBBLOCK.getBlock() && this.worldIn.getBlockState(pos).getBlock() != ChunkGeneratorTheEarth.SUBBLOCK.getBlock());
	}
}
