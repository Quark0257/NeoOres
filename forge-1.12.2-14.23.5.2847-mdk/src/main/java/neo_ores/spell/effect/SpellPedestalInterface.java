package neo_ores.spell.effect;

import java.util.List;
import java.util.UUID;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasDimensionOver;
import neo_ores.spell.SpellItemInterfaces.HasPI;
import neo_ores.spell.SpellItemInterfaces.HasReach;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayer;

public class SpellPedestalInterface extends SpellEffect implements HasDimensionOver, HasReach
{
	public static final UUID KEY = UUID.fromString("582fb0dc-d3eb-4749-ae76-9ccd980e2ccf");
	private boolean dimensionOver = false;
	private int reachValue = 0;

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		return RayTraceUtils.getSimpleResult(runner);
	}

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result == null)
			return;
		if (!(runner instanceof EntityPlayer) || runner instanceof FakePlayer)
			return;
		if (stack.getTagCompound().hasKey(SpellUtils.NBTTagUtils.ADDITIONAL, 10) && stack.getTagCompound().getCompoundTag(SpellUtils.NBTTagUtils.ADDITIONAL).hasKey("storedPosition", 10))
		{
			NBTTagCompound tag = stack.getTagCompound().getCompoundTag(SpellUtils.NBTTagUtils.ADDITIONAL).getCompoundTag("storedPosition");
			if (!tag.hasKey("pos") || !tag.hasKey("side") || !tag.hasKey("dim"))
			{
				return;
			}
			double reachDist = 5.0 * (this.reachValue + 1);
			int[] posArray = tag.getIntArray("pos");
			int dim = tag.getInteger("dim");
			BlockPos targetPos = new BlockPos(posArray[0], posArray[1], posArray[2]);
			Vec3d targetVec = new Vec3d(posArray[0] + 0.5, posArray[1] + 0.5, posArray[2] + 0.5);
			Vec3d runnerVec = new Vec3d(runner.posX, runner.posY, runner.posZ);
			World targetWorld = world;
			if (dim != world.provider.getDimension())
			{
				if (!this.dimensionOver)
				{
					return;
				}
				else
				{
					if (!DimensionManager.isWorldQueuedToUnload(dim))
					{
						targetWorld = DimensionManager.getWorld(dim);
						if (targetWorld == null)
						{
							return;
						}
					}
					else
					{
						return;
					}
				}
			}

			if (!this.dimensionOver && targetVec.subtract(runnerVec).lengthSquared() > reachDist * reachDist)
			{
				return;
			}

			List<BlockPos> posResult = HasPI.getPIPos(targetWorld, targetPos);
			if (posResult.isEmpty()) 
			{
				return;
			}
			for (BlockPos pos : posResult)
			{
				SpellUtils.onDisplayParticleTypeA(targetWorld, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(1, 1, 1), SpellUtils.getColor(stack), 8);
			}
			EntityPlayerMP player = (EntityPlayerMP) runner;
			NeoOresData.instance.setCurrentTargetPedestals(player, dim, EnumFacing.DOWN, posResult);
			player.openGui(NeoOres.instance, NeoOres.guiIDPI, world, MathHelper.floor(player.posX), MathHelper.floor(player.posY), MathHelper.floor(player.posZ));
			NeoOresData.instance.getPMD(player).addMXP(1L);
		} 
		else if (result.typeOfHit == Type.BLOCK) 
		{
			List<BlockPos> posResult = HasPI.getPIPos(world, result.getBlockPos());
			if (posResult.isEmpty()) 
			{
				return;
			}
			for (BlockPos pos : posResult)
			{
				SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(1, 1, 1), SpellUtils.getColor(stack), 8);
			}
			EntityPlayerMP player = (EntityPlayerMP) runner;
			NeoOresData.instance.setCurrentTargetPedestals(player, world.provider.getDimension(), result.sideHit, posResult);
			player.openGui(NeoOres.instance, NeoOres.guiIDPI, world, MathHelper.floor(player.posX), MathHelper.floor(player.posY), MathHelper.floor(player.posZ));
			NeoOresData.instance.getPMD(player).addMXP(1L);
		}
	}

	@Override
	public void setDimensionOver()
	{
		this.dimensionOver = true;
	}

	@Override
	public void setReach(int value)
	{
		this.reachValue = value;
	}
}
