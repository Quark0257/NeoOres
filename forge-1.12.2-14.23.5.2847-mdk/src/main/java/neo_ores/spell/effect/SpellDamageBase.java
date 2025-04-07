package neo_ores.spell.effect;

import java.util.Map;

import neo_ores.api.InventoryUtils;
import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.spell.SpellItemInterfaces.HasDamageLevel;
import neo_ores.spell.SpellItemInterfaces.HasGather;
import neo_ores.spell.SpellItemInterfaces.HasLuck;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.spell.SpellItemInterfaces.HasUncollidable;
import neo_ores.util.RayTraceUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public abstract class SpellDamageBase extends SpellEffect implements HasRange, HasLuck, HasDamageLevel, HasUncollidable, HasGather
{
	protected int damageLevel = 0;
	protected int luck = 0;
	protected int range0 = 0;
	protected boolean uncollidable = false;
	protected boolean gathering = false;

	@Override
	public void setLuck(int value)
	{
		luck = value;
	}

	@Override
	public void setRange(int value)
	{
		this.range0 = value;
	}

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result != null && result.typeOfHit == Type.ENTITY && result.entityHit != null)
		{
			ItemStack item = stack.copy();
			if (this.luck > 0)
			{
				item.addEnchantment(Enchantments.LOOTING, this.luck);
			}

			Entity entity = (Entity) result.entityHit;
			for (Entity elb : HasRange.getRangedEntities(world, this.range0, entity, runner, true, this.uncollidable))
			{
				this.onDamage(world, elb, runner, item);
				if (runner instanceof EntityPlayer && !elb.isEntityAlive() && this.gathering) 
				{
					for (EntityItem entityitem : world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(elb.posX - 0.5D, elb.posY, elb.posZ - 0.5D, elb.posX + 0.5D, elb.posY + 1.0D, elb.posZ + 0.5D)))
					{
						ItemStack target = entityitem.getItem();
						ItemStack resultStack = InventoryUtils.addInventoryfromStack(target, InventoryUtils.getPlayerInventory((EntityPlayer) runner), EnumFacing.UP);
						if (!target.isEmpty() && resultStack.getCount() != target.getCount())
						{
							entityitem.setItem(resultStack);
							if (entityitem.getItem().isEmpty())
								entityitem.setDead();
						}
					}
				}
			}

			Map<Enchantment, Integer> enchs = EnchantmentHelper.getEnchantments(item);
			if (enchs.containsKey(Enchantments.LOOTING))
			{
				enchs.remove(Enchantments.LOOTING);
			}
			if (item.hasTagCompound())
				item.getTagCompound().removeTag("ench");

			for (Map.Entry<Enchantment, Integer> entry : enchs.entrySet())
			{
				item.addEnchantment(entry.getKey(), entry.getValue());
			}
		}

	}

	protected abstract void onDamage(World world, Entity elb, EntityLivingBase runner, ItemStack stack);

	@Override
	public void setDamageLevel(int value)
	{
		this.damageLevel = value;

	}

	@Override
	public void setUncollidable()
	{
		this.uncollidable = true;
	}

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		return RayTraceUtils.getSimpleResult(runner);
	}

	@Override
	public void setCanGather()
	{
		this.gathering = true;
	}
}

