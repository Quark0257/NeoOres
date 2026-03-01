package neo_ores.spell.effect;

import neo_ores.api.ICompareBlockState;
import neo_ores.api.InventoryUtils;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasChain;
import neo_ores.spell.SpellItemInterfaces.HasOffsetDown;
import neo_ores.spell.SpellItemInterfaces.HasOffsetUp;
import neo_ores.spell.SpellItemInterfaces.HasPlantable;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import net.minecraftforge.items.IItemHandler;

public class SpellPlace extends SpellEffectItemFiltered implements HasPlantable, HasOffsetUp, HasOffsetDown
{
	private boolean plantable = false;
	private boolean offsetUp = false;
	private boolean offsetDown = false;

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result1, ItemStack stack)
	{
		RayTraceResult result = this.getResultBlockFromEntity(world, result1, stack, this.offsetUp, this.offsetDown);
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
			for (BlockPos pos : this.rangeMode ? HasRange.rangedPos(result.getBlockPos(), face, this.range) : HasChain.getChainedPos(world, chain, result.getBlockPos(), ICompareBlockState.ITEM))
			{
				if (!this.canEditBlocksBySpells(runner, stack, world, pos, face))
				{
					continue;
				}
				BlockPos targetPos = posAir ? pos : pos.add(face.getDirectionVec());
				IItemHandler handler = InventoryUtils.getInventoryStackList(target, null);
				for (int i = 0; i < handler.getSlots(); i++)
				{
					ItemStack item = handler.getStackInSlot(i);
					if (item.isEmpty())
						continue;
					if (item.getItem() instanceof ItemBlock && this.match(item, stack))
					{
						ItemStack itemSim = handler.extractItem(i, 1, true);
						if (itemSim.isEmpty())
						{
							continue;
						}
						SpellUtils.onDisplayParticleTypeA(world, new Vec3d(targetPos.getX(), targetPos.getY(), targetPos.getZ()), new Vec3d(1, 1, 1), SpellUtils.getColor(stack), 8);
						IBlockState state = ((ItemBlock) item.getItem()).getBlock().getStateForPlacement(world, targetPos, face, (float) result.hitVec.x, (float) result.hitVec.y,
								(float) result.hitVec.z, item.getMetadata(), player, player.getActiveHand());
						if (world.mayPlace(state.getBlock(), targetPos, false, face, player)
								&& ((ItemBlock) item.getItem()).placeBlockAt(item, player, world, targetPos, face, (float) result.hitVec.x, (float) result.hitVec.y, (float) result.hitVec.z, state))
						{
							handler.extractItem(i, 1, false);
							if (runner instanceof EntityPlayerMP)
							{
								PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
								pmds.addMXP(1);
							}
							break;
						}
					}
					else if (item.getItem() instanceof IPlantable && this.match(item, stack) && this.plantable)
					{
						ItemStack itemSim = handler.extractItem(i, 1, true);
						if (itemSim.isEmpty())
						{
							continue;
						}
						IPlantable plantable = (IPlantable) item.getItem();
						IBlockState state = world.getBlockState(pos);
						if (face == EnumFacing.UP && state.getBlock().canSustainPlant(state, world, pos, EnumFacing.UP, plantable) && world.isAirBlock(pos.up()))
						{
							SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.up().getX(), pos.up().getY(), pos.up().getZ()), new Vec3d(1, 1, 1), SpellUtils.getColor(stack), 8);
							world.setBlockState(pos.up(), plantable.getPlant(world, pos.up()));
							handler.extractItem(i, 1, false);
							if (runner instanceof EntityPlayerMP)
							{
								PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
								pmds.addMXP(1);
							}
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

	@Override
	public void setOffsetDown()
	{
		this.offsetDown = true;
	}

	@Override
	public void setOffsetUp()
	{
		this.offsetUp = true;
	}
}
