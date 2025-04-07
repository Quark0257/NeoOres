package neo_ores.spell.effect;

import java.util.Map;

import neo_ores.api.InventoryUtils;
import neo_ores.event.NeoOresRegisterEvents;
import neo_ores.spell.SpellItemInterfaces.HasPlantable;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class SpellPlace extends SpellEffectItemFiltered implements HasPlantable
{
	private boolean plantable = false;

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result == null)
			return;
		if (!(runner instanceof EntityPlayer))
			return;
		if (world.isRemote)
		{
			return;
		}
		EntityPlayer player = (EntityPlayer) runner;
		IInventory target = InventoryUtils.getPlayerInventory(player);
		if (result.typeOfHit == Type.BLOCK)
		{
			EnumFacing face = result.sideHit;
			boolean posAir = world.isAirBlock(result.getBlockPos());
			for (BlockPos pos : HasRange.rangedPos(result.getBlockPos(), face, this.range))
			{
				BlockPos targetPos = posAir ? pos : pos.add(face.getDirectionVec());
				Map<Integer, ItemStack> map = InventoryUtils.getInventoryStackList(target, false, null);
				for (int i : map.keySet())
				{
					ItemStack item = map.get(i);
					if (item.isEmpty())
						continue;
					if (item.getItem() instanceof ItemBlock && this.match(item, stack))
					{
						SpellUtils.onDisplayParticleTypeA(world, new Vec3d(targetPos.getX(), targetPos.getY(), targetPos.getZ()), new Vec3d(1, 1, 1), NeoOresRegisterEvents.particle0,
								SpellUtils.getColor(stack), 8);
						IBlockState state = ((ItemBlock) item.getItem()).getBlock().getStateForPlacement(world, targetPos, face, (float) result.hitVec.x, (float) result.hitVec.y,
								(float) result.hitVec.z, item.getMetadata(), player, player.getActiveHand());
						if (state.getBlock().canPlaceBlockOnSide(world, targetPos, face)
								&& ((ItemBlock) item.getItem()).placeBlockAt(stack, player, world, targetPos, face, (float) result.hitVec.x, (float) result.hitVec.y, (float) result.hitVec.z, state))
						{
							map.get(i).shrink(1);
							break;
						}
					}
					else if (item.getItem() instanceof IPlantable && this.match(item, stack) && this.plantable)
					{
						IPlantable plantable = (IPlantable) item.getItem();
				        IBlockState state = world.getBlockState(pos);
				        if (face == EnumFacing.UP && player.canPlayerEdit(pos.offset(face), face, item) && state.getBlock().canSustainPlant(state, world, pos, EnumFacing.UP, plantable) && world.isAirBlock(pos.up()))
				        {
				        	SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.up().getX(), pos.up().getY(), pos.up().getZ()), new Vec3d(1, 1, 1), NeoOresRegisterEvents.particle0,
									SpellUtils.getColor(stack), 8);
				            world.setBlockState(pos.up(), plantable.getPlant(world, pos.up()));
				            item.shrink(1);
				            break;
				        }
					}
				}
			}
		}
	}

	@Override
	public void setPlantable()
	{
		this.plantable = true;
	}

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		BlockPos pos = new BlockPos(runner.posX, runner.posY, runner.posZ);
		return RayTraceUtils.getSimpleResult(pos, null);
	}
}
