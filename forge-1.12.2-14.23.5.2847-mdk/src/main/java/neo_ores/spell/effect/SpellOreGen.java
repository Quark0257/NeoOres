package neo_ores.spell.effect;

import java.util.Map.Entry;
import java.util.Random;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.client.particle.ParticleMagic1;
import neo_ores.event.NeoOresRegisterEvent;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.PlayerManaDataServer;
import neo_ores.util.UtilSpellOreGen;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpellOreGen extends SpellEffect implements HasRange
{
	/*
	private Set<IWorldGenerator> oreGenList;
	private static final Random random = new Random();
	private Map<IBlockState,Integer> oreList = new HashMap<IBlockState,Integer>();
	*/
	
	private static final Random random = new Random();
	
	private int range;

	@Override
	public void setRange(int value) {
		range = value;
	}

	@Override
	public void onEffectRunToSelf(World world, EntityLivingBase runner, ItemStack stack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEffectRunToOther(World world, RayTraceResult result, ItemStack stack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEffectRunToSelfAndOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack) 
	{
		if(result != null && result.typeOfHit == Type.BLOCK && runner instanceof EntityPlayer)
		{
			ItemStack item = stack.copy();
			EnumFacing face = EnumFacing.getFacingFromVector((float)(result.hitVec.x - runner.posX),(float)(result.hitVec.y - runner.posY - runner.getEyeHeight()),(float)(result.hitVec.z - runner.posZ));
			if(face == EnumFacing.DOWN || face == EnumFacing.UP)
			{
				int x = result.getBlockPos().getX() - range;
				int z = result.getBlockPos().getZ() - range;
				for(int i = 0;i < range * 2 + 1;i++)
				{
					for(int j = 0;j < range * 2 + 1;j++)
					{
						BlockPos pos = new BlockPos(x + i,result.getBlockPos().getY(),z + j);
						if(world.isRemote) this.onDisplay(world,pos, runner);
						else
						{
							IBlockState state = world.getBlockState(pos);
							this.transform(state, world, pos, runner, item);	
						}


					}
				}
			}
			else if(face == EnumFacing.WEST || face == EnumFacing.EAST)
			{
				int y = result.getBlockPos().getY() - range;
				int z = result.getBlockPos().getZ() - range;
				for(int i = 0;i < range * 2 + 1;i++)
				{
					for(int j = 0;j < range * 2 + 1;j++)
					{
						BlockPos pos = new BlockPos(result.getBlockPos().getX(),y + i,z + j);
						if(world.isRemote) this.onDisplay(world,pos, runner);
						else
						{
							IBlockState state = world.getBlockState(pos);
							this.transform(state, world, pos, runner, item);
						}
					}
				}
			}
			else
			{
				int x = result.getBlockPos().getX() - range;
				int y = result.getBlockPos().getY() - range;
				for(int i = 0;i < range * 2 + 1;i++)
				{
					for(int j = 0;j < range * 2 + 1;j++)
					{
						BlockPos pos = new BlockPos(x + i,y + j,result.getBlockPos().getZ());
						if(world.isRemote) this.onDisplay(world,pos, runner);
						else
						{
							IBlockState state = world.getBlockState(pos);
							this.transform(state, world, pos, runner, item);
						}
					}
				}
			}
		}
	}
	
	private void transform(IBlockState target,World world,BlockPos pos, EntityLivingBase runner, ItemStack stack)
	{
		ItemStack output = ItemStack.EMPTY;
		
		int weights = 0;
		for(Entry<ItemStack,Integer> ore : UtilSpellOreGen.getOres(target).entrySet())
		{
			weights += ore.getValue();
		}
		
		if(weights <= 0) return;
		
		int index = random.nextInt(weights);
		
		for(Entry<ItemStack,Integer> ore : UtilSpellOreGen.getOres(target).entrySet())
		{
			if(index >= ore.getValue()) 
			{ 
				index -= ore.getValue();
			}
			else
			{
				output = ore.getKey().copy();
				break;
			}
		}
		
		@SuppressWarnings("deprecation")
		IBlockState out = Block.getBlockFromItem(output.getItem()).getStateFromMeta(output.getMetadata());
		world.setBlockState(pos, out);
		if(!output.isEmpty())
		{
			PlayerManaDataServer pmds = new PlayerManaDataServer((EntityPlayerMP)runner);
			pmds.addMXP(10L);
		}
	}
	
	@SideOnly(Side.CLIENT)
	private void onDisplay(World worldIn ,BlockPos pos,EntityLivingBase runner)
	{
        for (int i = 0; i < 12; ++i)
        {
            double d1 = (double)((float)pos.getX());
            double d2 = (double)((float)pos.getY());
            double d3 = (double)((float)pos.getZ());
            Vec3d velocity = new Vec3d(0, 0, 0);
            Vec3d start = new Vec3d(0, 0, 0);

            switch(i)
            {
            case 0:
            {
            	start = new Vec3d(d1, d2, d3);
            	velocity = new Vec3d( 1.0D, 0.0D, 0.0D);
            	break;
            }
            case 1:
            {
            	start = new Vec3d(d1, d2, d3);
            	velocity = new Vec3d( 0.0D, 0.0D, 1.0D);
            	break;
            }
            case 2:
            {
            	start = new Vec3d(d1, d2, d3 + 1.0);
            	velocity = new Vec3d( 0.0D, 1.0D, 0.0D);
            	break;
            }
            case 3:
            {
            	start = new Vec3d(d1, d2 + 1.0, d3);
            	velocity = new Vec3d( 0.0D, -1.0D, 0.0D);
            	break;
            }
            case 4:
            {
            	start = new Vec3d(d1, d2 + 1.0, d3 + 1.0);
            	velocity = new Vec3d( 0.0D, 0.0D, -1.0D);
            	break;
            }
            case 5:
            {
            	start = new Vec3d(d1, d2 + 1.0, d3 + 1.0);
            	velocity = new Vec3d( 1.0D, 0.0D, 0.0D);
            	break;
            }
            case 6:
            {
            	start = new Vec3d(d1 + 1.0, d2, d3);
            	velocity = new Vec3d( 0.0D, 1.0D, 0.0D);
            	break;
            }
            case 7:
            {
            	start = new Vec3d(d1 + 1.0, d2, d3 + 1.0);
            	velocity = new Vec3d( 0.0D, 0.0D, -1.0D);
            	break;
            }
            case 8:
            {
            	start = new Vec3d(d1 + 1.0, d2, d3 + 1.0);
            	velocity = new Vec3d( -1.0D, 0.0D, 0.0D);
            	break;
            }
            case 9:
            {
            	start = new Vec3d(d1 + 1.0, d2 + 1.0, d3);
            	velocity = new Vec3d( 0.0D, 0.0D, 1.0D);
            	break;
            }
            case 10:
            {
            	start = new Vec3d(d1 + 1.0, d2 + 1.0, d3);
            	velocity = new Vec3d( -1.0D, 0.0D, 0.0D);
            	break;
            }
            case 11:
            {
            	start = new Vec3d(d1 + 1.0, d2 + 1.0, d3 + 1.0);
            	velocity = new Vec3d( 0.0D, -1.0D, 0.0D);
            	break;
            }
            }
            for(int j = 0;j < 8;j++)
            {
            	int d = (int)(10.0D / (Math.random() + 0.5D));
            	ParticleMagic1 png = new ParticleMagic1(worldIn, start.x, start.y, start.z, velocity.x / d, velocity.y / d, velocity.z / d, 0x80FFCE, d,0.0005F, NeoOresRegisterEvent.particle0);
            	//ParticleMagic1 png = new ParticleMagic1(worldIn, start.x, start.y, start.z,300);
            	Minecraft.getMinecraft().effectRenderer.addEffect(png);
            }
        }
	}
	
	@Override
	public void initialize() 
	{
		this.range = 0;
	}
	/*
	public static class FakeChunkGenerator implements IChunkGenerator
	{
		private final World world;
		private final IBlockState state;
		public FakeChunkGenerator(World worldIn,IBlockState target)
		{
			world = worldIn;
			this.state = target;
		}
		
		@Override
		public Chunk generateChunk(int x, int z) 
		{
			ChunkPrimer chunkprimer = new ChunkPrimer();
			
			for(int i = 0;i < 256;i++)
			{
				for(int j = 0;j < 16;j++)
				{
					for(int k = 0;k < 16;k++)
					{
						chunkprimer.setBlockState(j, i, k, state);
					}
				}
			}
			
			return new Chunk(this.world, chunkprimer,x, z);
		}

		@Override
		public void populate(int x, int z) 
		{
			
		}

		@Override
		public boolean generateStructures(Chunk chunkIn, int x, int z) 
		{
			return false;
		}

		@Override
		public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) 
		{
			return new ArrayList<SpawnListEntry>();
		}

		@Override
		public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position,boolean findUnexplored) 
		{
			return null;
		}

		@Override
		public void recreateStructures(Chunk chunkIn, int x, int z) 
		{
		}

		@Override
		public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) 
		{
			return false;
		}
		
	}
	
	public static class FakeChunkProvider implements IChunkProvider
	{
		private final IChunkGenerator gen;
		
		public FakeChunkProvider(World worldIn,IChunkGenerator generator)
		{
			gen = generator;
		}
		
		@Override
		public Chunk getLoadedChunk(int x, int z) 
		{
			return gen.generateChunk(x, z);
		}

		@Override
		public Chunk provideChunk(int x, int z) 
		{
			return gen.generateChunk(x, z);
		}

		@Override
		public boolean tick() 
		{
			return false;
		}

		@Override
		public String makeString() 
		{
			return "ore";
		}

		@Override
		public boolean isChunkGeneratedAt(int x, int z) 
		{
			return false;
		}
	}
	*/
}