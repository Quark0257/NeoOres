package neo_ores.spell.effect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.main.NeoOres;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SpellOreGen extends SpellEffect implements HasRange
{
	/*
	private Set<IWorldGenerator> oreGenList;
	private static final Random random = new Random();
	private Map<IBlockState,Integer> oreList = new HashMap<IBlockState,Integer>();
	*/

	@Override
	public void setRange(int value) {
		// TODO Auto-generated method stub
		
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
		/*
		IBlockState target = NeoOres.dim_stone.getDefaultState();
		FakeChunkGenerator fcg = new FakeChunkGenerator(world,target);
		FakeChunkProvider fcp = new FakeChunkProvider(world, fcg);
		for(IWorldGenerator gen : oreGenList)
		{
			gen.generate(random, 0, 0, world, fcg, fcp);
		}
		Chunk generated = fcp.provideChunk(0, 0);
		for(int i = 0;i < 256;i++)
		{
			for(int j = 0;j < 16;j++)
			{
				for(int k = 0;k < 16;k++)
				{
					IBlockState state = generated.getBlockState(j, i, k);
					if(state != target)
					{
						if(this.oreList.containsKey(state))
						{
							this.oreList.put(state, this.oreList.get(state) + 1);
						}
						else
						{
							this.oreList.put(state, 1);
						}
					}
				}
			}
		}
		System.out.println(this.oreList);
		*/
		//Set<IWorldGenerator> o = ReflectionHelper.getPrivateValue(GameRegistry.class,new GameRegistry(), "worldGenerators");
	}

	@Override
	public void initialize() 
	{
		/*
		oreGenList = ObfuscationReflectionHelper.getPrivateValue(GameRegistry.class,new GameRegistry(), "worldGenerators");
		System.out.println(oreGenList);
		*/
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