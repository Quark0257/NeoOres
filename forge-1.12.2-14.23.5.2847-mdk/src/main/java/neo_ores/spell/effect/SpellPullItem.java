package neo_ores.spell.effect;

import neo_ores.api.InventoryUtils;
import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.event.NeoOresRegisterEvents;
import neo_ores.spell.SpellItemInterfaces.HasChanceLiquid;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class SpellPullItem extends SpellEffect implements HasRange, HasChanceLiquid
{
	private int range = 0;
	private boolean liquidMode = false;

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
		if (result == null)
			return;
		if (!(runner instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) runner;
		if (result.typeOfHit == Type.BLOCK)
		{
			EnumFacing face = result.sideHit;
			for (BlockPos pos : HasRange.rangedPos(result.getBlockPos(), face, this.range))
			{
				SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(1, 1, 1), NeoOresRegisterEvents.particle0, SpellUtils.getColor(stack), 8);
				TileEntity te = world.getTileEntity(pos);
				if (te != null && te instanceof IInventory && !this.liquidMode)
				{
					IInventory inventory = (IInventory) te;
					int size = inventory.getSizeInventory();
					for (int i = 0; i < size; i++)
					{
						if (!inventory.getStackInSlot(i).isEmpty() && this.match(stack))
						{
							if (InventoryUtils.addInventoryfromInventorySlot(i, inventory, InventoryUtils.getPlayerInventory(player), face, null))
							{
								break;
							}
						}
					}
				}
				IFluidHandler handler = FluidUtil.getFluidHandler(world, pos, face);
				if (handler != null && this.liquidMode) {
					InventoryUtils.addFluidToInventoryFromTank(handler, InventoryUtils.getPlayerInventory(player), null);
				}
			}
		}
		else
		{
			Entity entity = result.entityHit;
			if (entity == null)
				return;
			for (Entity temp : HasRange.getRangedEntities(world, this.range, entity, runner, false, true))
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
			if (!this.match(target))
			{
				return;
			}
			SpellUtils.onDisplayParticleTypeAEntity(world, entityitem, NeoOresRegisterEvents.particle0, SpellUtils.getColor(stack), 16);
			ItemStack result = InventoryUtils.addInventoryfromStack(target, InventoryUtils.getPlayerInventory(player), null);
			if (!target.isEmpty() && result.getCount() != target.getCount())
			{
				entityitem.setItem(result);
				if (entityitem.getItem().isEmpty())
					entityitem.setDead();
			}
		}
	}

	private boolean match(ItemStack stack)
	{
		// TODO set Filter
		return true;
	}

	@Override
	public void setRange(int value)
	{
		this.range = value;
	}

	@Override
	public void setSupport()
	{
		this.liquidMode = true;
	}
}
