package neo_ores.spell.effect;

import java.util.List;

import com.google.common.base.Predicate;

import neo_ores.api.ICompareBlockState;
import neo_ores.api.IFunction;
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
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

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
			List<BlockPos> blockPoss = this.piMode ? HasPI.getPIPos(world, result.getBlockPos())
					: (this.rangeMode ? HasRange.rangedPos(result.getBlockPos(), face, this.range) : HasChain.getChainedPos(world, this.chain, result.getBlockPos(), ICompareBlockState.ITEM));
			for (BlockPos pos : blockPoss)
			{
				SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(1, 1, 1), SpellUtils.getColor(stack), 8);
			}
			for (BlockPos pos : blockPoss)
			{
				TileEntity te = world.getTileEntity(pos);
				if (te != null && te instanceof IInventory && !this.liquidMode)
				{
					IInventory inventory = (IInventory) te;
					if (InventoryUtils.addInventoryFromInventorySlot(target, inventory, EnumFacing.UP, face, new Predicate<ItemStack>()
					{
						@Override
						public boolean apply(ItemStack input)
						{
							return match(input, stack);
						}
					}))
					{
						if (runner instanceof EntityPlayerMP)
						{
							PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
							pmds.addMXP(1L);
						}
					}
				}
				if (this.liquidMode)
				{
					IFluidHandler handler = getFluidHandler(te, face);
					if (InventoryUtils.outputFluidFromInventory(target, EnumFacing.UP, handler, pos, face, new IFunction<BlockPos>()
					{
						@Override
						public BlockPos function(BlockPos nextPos)
						{
							SpellUtils.onDisplayParticleTypeA(world, new Vec3d(nextPos.getX(), nextPos.getY(), nextPos.getZ()), new Vec3d(1, 1, 1), SpellUtils.getColor(stack), 8);
							return nextPos;
						}
					}, new Predicate<Fluid>()
					{

						@Override
						public boolean apply(Fluid input)
						{
							return match(input, stack);
						}
					}, world, player))
					{
						if (runner instanceof EntityPlayerMP)
						{
							PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
							pmds.addMXP(1L);
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

			IItemHandler handler = InventoryUtils.getInventoryStackList(target, EnumFacing.UP);
			if (handler == null)
			{
				return;
			}
			int count = 0;
			for (int i = 0; i < handler.getSlots(); i++)
			{
				ItemStack original = handler.getStackInSlot(i);
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
