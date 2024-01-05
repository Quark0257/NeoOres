package neo_ores.spell.effect;

import java.util.Map.Entry;
import java.util.Random;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.main.NeoOresRegisterEvent;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.PlayerManaDataServer;
import neo_ores.util.SpellUtils;
import neo_ores.util.UtilSpellOreGen;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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

public class SpellOreGen extends SpellEffect implements HasRange
{
	/*
	 * private Set<IWorldGenerator> oreGenList; private static final Random random =
	 * new Random(); private Map<IBlockState,Integer> oreList = new
	 * HashMap<IBlockState,Integer>();
	 */

	private static final Random random = new Random();

	private int range = 0;

	@Override
	public void setRange(int value)
	{
		range = value;
	}

	@Override
	public void onEffectRunToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onEffectRunToOther(World world, RayTraceResult result, ItemStack stack)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onEffectRunToSelfAndOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result != null && result.typeOfHit == Type.BLOCK && runner instanceof EntityPlayer)
		{
			ItemStack item = stack.copy();
			EnumFacing face = EnumFacing.getFacingFromVector((float) (result.hitVec.x - runner.posX), (float) (result.hitVec.y - runner.posY - runner.getEyeHeight()),
					(float) (result.hitVec.z - runner.posZ));
			for (BlockPos pos : SpellUtils.rangedPos(result.getBlockPos(), face, this.range))
			{
				if (world.isRemote)
					SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(1.0, 1.0, 1.0), NeoOresRegisterEvent.particle0, SpellUtils.getColor(stack), 8);
				else
				{
					IBlockState state = world.getBlockState(pos);
					this.transform(state, world, pos, runner, item);
				}
			}
		}
	}

	private void transform(IBlockState target, World world, BlockPos pos, EntityLivingBase runner, ItemStack stack)
	{
		ItemStack output = ItemStack.EMPTY;

		int weights = 0;
		for (Entry<ItemStack, Integer> ore : UtilSpellOreGen.getOres(target).entrySet())
		{
			weights += ore.getValue();
		}

		if (weights <= 0)
			return;

		int index = random.nextInt(weights);

		for (Entry<ItemStack, Integer> ore : UtilSpellOreGen.getOres(target).entrySet())
		{
			if (index >= ore.getValue())
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
		if (!output.isEmpty())
		{
			PlayerManaDataServer pmds = new PlayerManaDataServer((EntityPlayerMP) runner);
			pmds.addMXP(10L);
		}
	}
	/*
	 * public static class FakeChunkGenerator implements IChunkGenerator { private
	 * final World world; private final IBlockState state; public
	 * FakeChunkGenerator(World worldIn,IBlockState target) { world = worldIn;
	 * this.state = target; }
	 * 
	 * @Override public Chunk generateChunk(int x, int z) { ChunkPrimer chunkprimer
	 * = new ChunkPrimer();
	 * 
	 * for(int i = 0;i < 256;i++) { for(int j = 0;j < 16;j++) { for(int k = 0;k <
	 * 16;k++) { chunkprimer.setBlockState(j, i, k, state); } } }
	 * 
	 * return new Chunk(this.world, chunkprimer,x, z); }
	 * 
	 * @Override public void populate(int x, int z) {
	 * 
	 * }
	 * 
	 * @Override public boolean generateStructures(Chunk chunkIn, int x, int z) {
	 * return false; }
	 * 
	 * @Override public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType
	 * creatureType, BlockPos pos) { return new ArrayList<SpawnListEntry>(); }
	 * 
	 * @Override public BlockPos getNearestStructurePos(World worldIn, String
	 * structureName, BlockPos position,boolean findUnexplored) { return null; }
	 * 
	 * @Override public void recreateStructures(Chunk chunkIn, int x, int z) { }
	 * 
	 * @Override public boolean isInsideStructure(World worldIn, String
	 * structureName, BlockPos pos) { return false; }
	 * 
	 * }
	 * 
	 * public static class FakeChunkProvider implements IChunkProvider { private
	 * final IChunkGenerator gen;
	 * 
	 * public FakeChunkProvider(World worldIn,IChunkGenerator generator) { gen =
	 * generator; }
	 * 
	 * @Override public Chunk getLoadedChunk(int x, int z) { return
	 * gen.generateChunk(x, z); }
	 * 
	 * @Override public Chunk provideChunk(int x, int z) { return
	 * gen.generateChunk(x, z); }
	 * 
	 * @Override public boolean tick() { return false; }
	 * 
	 * @Override public String makeString() { return "ore"; }
	 * 
	 * @Override public boolean isChunkGeneratedAt(int x, int z) { return false; } }
	 */
}