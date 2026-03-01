package neo_ores.spell.effect;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicate;

import neo_ores.api.ICompareBlockState;
import neo_ores.api.InventoryUtils;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasChain;
import neo_ores.spell.SpellItemInterfaces.HasPI;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.tileentity.DetectorWrapper;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class SpellPullItem extends SpellEffectItemFilteredOrFluid implements HasPI
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
		if (result.typeOfHit == Type.BLOCK)
		{
			EnumFacing face = result.sideHit;
			List<BlockPos> blockPoss = this.piMode ? HasPI.getPIPos(world, result.getBlockPos())
					: (this.rangeMode ? HasRange.rangedPos(result.getBlockPos(), face, this.range) : HasChain.getChainedPos(world, this.chain, result.getBlockPos(), ICompareBlockState.ITEM));
			List<BlockPos> detectors = new ArrayList<>();
			boolean successedProcess = false;
			for (BlockPos pos : blockPoss)
			{
				SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(1, 1, 1), SpellUtils.getColor(stack), 8);
			}
			for (BlockPos pos : blockPoss)
			{
				TileEntity te = world.getTileEntity(pos);
				boolean checkDetector = false;
				if (te != null && te instanceof ICapabilityProvider)
				{
					ICapabilityProvider cap = (ICapabilityProvider) te;
					IItemHandler handler = cap.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face);
					checkDetector = handler instanceof DetectorWrapper;
					if (checkDetector)
					{
						detectors.add(pos);
					}
				}
				if (te != null && te instanceof IInventory && !this.liquidMode && !checkDetector)
				{
					IInventory inventory = (IInventory) te;
					if (InventoryUtils.addInventoryFromInventorySlot(inventory, InventoryUtils.getPlayerInventory(player), face, EnumFacing.UP, new Predicate<ItemStack>()
					{

						@Override
						public boolean apply(ItemStack input)
						{
							return match(input, stack);
						}
					}))
					{
						successedProcess = true;
						if (runner instanceof EntityPlayerMP)
						{
							PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
							pmds.addMXP(1L);
						}
						break;
					}
				}
				IFluidHandler handler = FluidUtil.getFluidHandler(world, pos, face);
				if (handler != null && this.liquidMode)
				{
					FluidStack fluid = null;
					for (IFluidTankProperties tp : handler.getTankProperties())
					{
						if (tp.getContents() == null || tp.getContents().getFluid() == null)
						{
							continue;
						}
						if (this.match(tp.getContents().getFluid(), stack))
						{
							fluid = tp.getContents().copy();
						}
					}
					if (fluid == null)
					{
						continue;
					}
					fluid.amount = 1000;
					if (InventoryUtils.addFluidToInventoryFromTank(handler, InventoryUtils.getPlayerInventory(player), EnumFacing.UP, fluid))
					{
						successedProcess = true;
						if (runner instanceof EntityPlayerMP)
						{
							PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
							pmds.addMXP(1L);
						}
						break;
					}
				}
			}
			if (this.piMode && !successedProcess)
			{
				loop: for (BlockPos pos : detectors)
				{
					TileEntity te = world.getTileEntity(pos);
					if (te != null && te instanceof ICapabilityProvider)
					{
						ICapabilityProvider cap = (ICapabilityProvider) te;
						IItemHandler handler = cap.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face);
						for (int slot = 0; slot < handler.getSlots(); slot++)
						{
							if (!handler.getStackInSlot(slot).isEmpty() && this.match(handler.getStackInSlot(slot), stack))
							{
								handler.extractItem(slot, 1, false);
								break loop;
							}
						}
					}
				}
			}
		}
		else
		{
			Entity entity = result.entityHit;
			if (entity == null)
				return;
			for (Entity temp : this.rangeMode ? HasRange.getRangedEntities(world, this.range, entity, runner, false, true) : HasChain.getChainedEntity(world, this.chain, entity, runner, false, true))
			{
				this.entityFor(temp, player, world, stack);
			}
		}
	}

	private void entityFor(Entity entity, EntityPlayer player, World world, ItemStack stack)
	{
		if (entity instanceof EntityItem)
		{
			EntityItem entityitem = (EntityItem) entity;
			ItemStack target = entityitem.getItem();
			if (!this.match(target, stack))
			{
				return;
			}
			SpellUtils.onDisplayParticleTypeAEntity(world, entityitem, SpellUtils.getColor(stack), 16);
			ItemStack result = InventoryUtils.addInventoryFromStack(target, InventoryUtils.getPlayerInventory(player), EnumFacing.UP);
			if (!target.isEmpty() && result.getCount() != target.getCount())
			{
				entityitem.setItem(result);
				if (entityitem.getItem().isEmpty())
					entityitem.setDead();

				if (player instanceof EntityPlayerMP)
				{
					PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) player);
					pmds.addMXP(1L);
				}
			}
		}
	}

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		return RayTraceUtils.getSimpleResult(runner);
	}

	@Override
	public void setPIMode()
	{
		this.piMode = true;
	}
}
