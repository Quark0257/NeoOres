package neo_ores.spell.effect;

import neo_ores.api.ICompareBlockState;
import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasAmplify;
import neo_ores.spell.SpellItemInterfaces.HasChain;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class SpellTimeSkip extends SpellEffect implements HasRange, HasChain, HasAmplify
{
	protected int range = 0;
	protected int chain = 0;
	protected boolean rangeMode = true;
	protected int amp = 0;

	@Override
	public void setRange(int value)
	{
		this.range = value;
		this.rangeMode = true;
	}

	@Override
	public void setChain(int level)
	{
		this.rangeMode = false;
		this.chain = level;
	}

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		BlockPos pos = new BlockPos(runner.posX, runner.posY, runner.posZ);
		return RayTraceUtils.getSimpleResult(pos, null);
	}

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result != null && result.typeOfHit == Type.BLOCK)
		{
			EnumFacing face = result.sideHit;
			int count = 20 * (this.amp + 1);
			for (BlockPos pos : this.rangeMode ? HasRange.rangedPos(result.getBlockPos(), face, this.range) : HasChain.getChainedPos(world, this.chain, result.getBlockPos(), ICompareBlockState.ITEM))
			{
				SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(1, 1, 1), SpellUtils.getColor(stack), 8);
				TileEntity tile = world.getTileEntity(pos);
				if (tile != null && tile instanceof ITickable)
				{
					for (int i = 0; i < count; i++)
					{
						((ITickable) tile).update();
					}
					
					if (runner instanceof EntityPlayerMP)
					{
						PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
						pmds.addMXP(1L + (long) Math.pow(2, this.amp));
					}
				}
			}
		}
	}

	@Override
	public void setAmplify(int level)
	{
		this.amp = level;
	}
}
