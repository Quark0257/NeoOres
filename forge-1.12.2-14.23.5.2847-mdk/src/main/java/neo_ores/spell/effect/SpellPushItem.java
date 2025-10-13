package neo_ores.spell.effect;

import java.util.Map;

import neo_ores.api.ICompareBlockState;
import neo_ores.api.InventoryUtils;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasChain;
import neo_ores.spell.SpellItemInterfaces.HasPI;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class SpellPushItem extends SpellEffectItemFilteredOrFluid implements HasPI
{
	private boolean piMode = false;
	
	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result == null)
			return;
		if (!(runner instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) runner;
		IInventory target = InventoryUtils.getPlayerInventory(player);
		if (result.typeOfHit == Type.BLOCK)
		{
			EnumFacing face = result.sideHit;
			for (BlockPos pos : this.piMode ? HasPI.getPIPos(world, result.getBlockPos())
					: (this.rangeMode ? HasRange.rangedPos(result.getBlockPos(), face, this.range) : HasChain.getChainedPos(world, this.chain, result.getBlockPos(), ICompareBlockState.ITEM)))
			{
				TileEntity te = world.getTileEntity(pos);
				if (te != null && te instanceof IInventory && !this.liquidMode)
				{
					SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(1, 1, 1), SpellUtils.getColor(stack), 8);
					IInventory inventory = (IInventory) te;
					Map<Integer, ItemStack> map = InventoryUtils.getInventoryStackList(target, true, EnumFacing.UP);
					for (int i : map.keySet())
					{
						if (!target.getStackInSlot(i).isEmpty() && this.match(inventory.getStackInSlot(i), stack))
						{
							if (InventoryUtils.addInventoryfromInventorySlot(i, target, inventory, EnumFacing.UP, face))
							{
								if (runner instanceof EntityPlayerMP)
								{
									PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
									pmds.addMXP(1L);
								}
								break;
							}
						}
					}
				}
				if (this.liquidMode)
				{
					Map<Integer, FluidStack> map = InventoryUtils.getFluidFromInventory(target, false, EnumFacing.UP);
					IFluidHandler handler = getFluidHandler(te, face);
					if (handler != null)
					{
						for (int i : map.keySet())
						{
							FluidStack fluid = map.get(i);
							if (!this.match(fluid.getFluid(), stack))
							{
								continue;
							}
							int willFill = handler.fill(fluid, false);
							if (willFill == fluid.amount)
							{
								handler.fill(fluid, true);
								target.getStackInSlot(i).setCount(0);
								InventoryUtils.addInventoryfromStack(new ItemStack(Items.BUCKET), target, EnumFacing.UP);
								if (runner instanceof EntityPlayerMP)
								{
									PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
									pmds.addMXP(1L);
								}
								break;
							}
						}
					}
					else
					{
						for (int i : map.keySet())
						{
							if (!this.match(map.get(i).getFluid(), stack))
							{
								continue;
							}
							BlockPos nextPos = pos.add(face.getDirectionVec());
							if (FluidUtil.tryPlaceFluid(player, world, nextPos, target.getStackInSlot(i), map.get(i)) != FluidActionResult.FAILURE)
							{
								target.getStackInSlot(i).setCount(0);
								InventoryUtils.addInventoryfromStack(new ItemStack(Items.BUCKET), target, EnumFacing.UP);
								SpellUtils.onDisplayParticleTypeA(world, new Vec3d(nextPos.getX(), nextPos.getY(), nextPos.getZ()), new Vec3d(1, 1, 1), SpellUtils.getColor(stack), 8);
								if (runner instanceof EntityPlayerMP)
								{
									PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
									pmds.addMXP(1L);
								}
								break;
							}
						}
					}
				}
			}
		}
		else if (!this.liquidMode)
		{
			Entity entity = result.entityHit;
			if (entity == null)
				return;
			int trial = 2 * this.range + 1;
			Map<Integer, ItemStack> map = InventoryUtils.getInventoryStackList(target, true, EnumFacing.UP);
			int count = 0;
			for (int i : map.keySet())
			{
				ItemStack original = map.get(i);
				if (!original.isEmpty() && this.match(original, stack))
				{
					EntityItem entityItem = new EntityItem(world, entity.posX, entity.posY, entity.posZ, original.copy());
					entityItem.motionX = 0.0;
					entityItem.motionY = 0.0;
					entityItem.motionZ = 0.0;
					world.spawnEntity(entityItem);
					SpellUtils.onDisplayParticleTypeAEntity(world, entityItem, SpellUtils.getColor(stack), 16);
					original.setCount(0);
					count++;
					if (runner instanceof EntityPlayerMP)
					{
						PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
						pmds.addMXP(1L);
					}
				}
				if (trial <= count)
				{
					break;
				}
			}
		}
	}

	private static IFluidHandler getFluidHandler(TileEntity te, EnumFacing face)
	{
		if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face))
		{
			return te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face);
		}
		return null;
	}

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		BlockPos pos = new BlockPos(runner.posX, runner.posY, runner.posZ);
		return RayTraceUtils.getSimpleResult(pos, null);
	}

	@Override
	public void setPIMode()
	{
		this.piMode = true;
	}
}
