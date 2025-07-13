package neo_ores.spell.effect;

import java.util.Map.Entry;
import java.util.Random;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import neo_ores.util.UtilSpellOreGen;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
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
	}

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result != null && result.typeOfHit == Type.BLOCK)
		{
			ItemStack item = stack.copy();
			EnumFacing face = result.sideHit;
			for (BlockPos pos : HasRange.rangedPos(result.getBlockPos(), face, this.range))
			{
				SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(1.0, 1.0, 1.0), SpellUtils.getColor(stack), 8);
				if (!world.isRemote)
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
		if (!output.isEmpty() && runner instanceof EntityPlayerMP)
		{
			PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
			pmds.addMXP(10L);
		}
	}

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		BlockPos pos = new BlockPos(runner.posX, runner.posY, runner.posZ);
		return RayTraceUtils.getSimpleResult(pos, null);
	}
}