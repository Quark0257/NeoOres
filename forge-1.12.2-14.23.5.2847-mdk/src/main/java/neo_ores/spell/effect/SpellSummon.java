package neo_ores.spell.effect;

import neo_ores.api.NBTUtils;
import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasCanApplyNBT;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class SpellSummon extends SpellEffect implements HasCanApplyNBT
{
	private boolean applyNBT = false;

	@Override
	public void onEffectRunToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
	}

	@Override
	public void onEffectRunToOther(World world, RayTraceResult result, ItemStack stack)
	{
	}

	@Override
	public void onEffectRunToSelfAndOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (world.isRemote)
			return;

		if (stack.getTagCompound().hasKey("additionalData", 10) && stack.getTagCompound().getCompoundTag("additionalData").hasKey("storedEntity", 10))
		{
			NBTUtils nbtutils = new NBTUtils(stack.getTagCompound().getCompoundTag("additionalData"));
			NBTTagCompound entityTag = nbtutils.getCompound("storedEntity").copy();
			Entity rawentity = EntityList.createEntityByIDFromName(new ResourceLocation(entityTag.getString("id")), world);
			if (rawentity == null || !(rawentity instanceof EntityLivingBase))
				return;
			EntityLivingBase entity = (EntityLivingBase) rawentity;
			float pay = entity.getMaxHealth();
			if (!SpellUtils.spellPay(runner, pay))
				return;
			if (this.applyNBT && entityTag.getCompoundTag("tag") != null)
			{
				if (entityTag.getCompoundTag("tag").hasUniqueId("UUID"))
				{
					entityTag.getCompoundTag("tag").removeTag("UUID");
				}
				entity.deserializeNBT(entityTag.getCompoundTag("tag"));
			}
			else
				entity.setHealth(entity.getMaxHealth());
			if (result == null || result.typeOfHit != RayTraceResult.Type.BLOCK)
				return;
			BlockPos entitySpawn = result.getBlockPos();
			if (result.sideHit == EnumFacing.DOWN)
			{
				entitySpawn = entitySpawn.add(0, -entity.height, 0);
			}
			else if (result.sideHit == EnumFacing.EAST)
			{
				entitySpawn = entitySpawn.add(1, 0, 0);
			}
			else if (result.sideHit == EnumFacing.WEST)
			{
				entitySpawn = entitySpawn.add(-1, 0, 0);
			}
			else if (result.sideHit == EnumFacing.NORTH)
			{
				entitySpawn = entitySpawn.add(0, 0, -1);
			}
			else if (result.sideHit == EnumFacing.SOUTH)
			{
				entitySpawn = entitySpawn.add(0, 0, 1);
			}
			else
			{
				entitySpawn = entitySpawn.add(0, 1, 0);
			}
			entity.setPositionAndRotation(entitySpawn.getX() + 0.5, entitySpawn.getY(), entitySpawn.getZ() + 0.5, entity.rotationYaw, entity.rotationPitch);
			world.spawnEntity(entity);

			if (runner instanceof EntityPlayerMP)
			{
				PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
				pmds.addMXP(10);
			}
		}
	}

	@Override
	public void setCanApplyNBT()
	{
		applyNBT = true;
	}

}
