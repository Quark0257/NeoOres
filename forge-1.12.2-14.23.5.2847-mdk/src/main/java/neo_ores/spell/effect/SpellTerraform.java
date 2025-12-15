package neo_ores.spell.effect;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.NBTUtils;
import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresData;
import neo_ores.packet.PacketBiomeChangeToClient;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class SpellTerraform extends SpellEffect implements HasRange
{
	private int range = 0;

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (world.isRemote)
			return;
		if (result == null || result.typeOfHit != RayTraceResult.Type.BLOCK)
			return;
		if (stack.getTagCompound().hasKey(SpellUtils.NBTTagUtils.ADDITIONAL, 10) && stack.getTagCompound().getCompoundTag(SpellUtils.NBTTagUtils.ADDITIONAL).hasKey("storedBiome", 10))
		{
			NBTUtils nbtutils = new NBTUtils(stack.getTagCompound().getCompoundTag(SpellUtils.NBTTagUtils.ADDITIONAL));
			NBTTagCompound biomeTag = nbtutils.getCompound("storedBiome").copy();
			BlockPos biomeSpawn = result.getBlockPos();

			byte biomeId = biomeTag.getByte("id");
			List<BlockPos> blockPos = new ArrayList<>();
			for (int i = -this.range; i <= this.range; i++)
			{
				for (int j = -this.range; j <= this.range; j++)
				{
					if (i * i + j * j <= this.range * this.range)
					{
						BlockPos pos = new BlockPos(biomeSpawn.getX() + i, biomeSpawn.getY(), biomeSpawn.getZ() + j);
						SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(1, 1, 1), SpellUtils.getColor(stack), 8);
						if (world.isBlockLoaded(pos))
						{
							blockPos.add(pos);
							Chunk chunk = world.getChunkFromBlockCoords(pos);
							int x = pos.getX() & 15;
							int y = pos.getZ() & 15;
							chunk.getBiomeArray()[y << 4 | x] = biomeId;
						}
					}
				}
			}

			if (!blockPos.isEmpty())
			{
				PacketBiomeChangeToClient ppc = new PacketBiomeChangeToClient(blockPos, biomeId, world.provider.getDimension());
				NeoOres.PACKET.sendToAll(ppc);
			}

			if (runner instanceof EntityPlayerMP)
			{
				PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
				pmds.addMXP(10 * this.range);
			}
		}
	}

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		return RayTraceUtils.getSimpleResult(runner.getPosition(), null);
	}

	@Override
	public void setRange(int value)
	{
		this.range = value;
	}
}
