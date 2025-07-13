package neo_ores.spell.effect;

import java.util.ArrayList;
import java.util.List;

import neo_ores.util.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SpellDisarm extends SpellEffectEntityBase
{
	@Override
	protected void onEffect(World world, Entity entity, EntityLivingBase runner, ItemStack stack)
	{
		if (entity instanceof EntityLivingBase && !this.isFakePlayer(entity))
		{
			SpellUtils.onDisplayParticleTypeAEntity(world, entity, SpellUtils.getColor(stack), 16);
			EntityLivingBase elb = (EntityLivingBase) entity;
			List<EntityEquipmentSlot> slots = new ArrayList<EntityEquipmentSlot>();
			for (EntityEquipmentSlot slot : EntityEquipmentSlot.values())
			{
				if (elb.hasItemInSlot(slot))
				{
					slots.add(slot);
				}
			}

			if (!slots.isEmpty())
			{
				int i = world.rand.nextInt(slots.size());
				EntityEquipmentSlot slot = slots.get(i);
				ItemStack item = elb.getItemStackFromSlot(slot);
				EntityItem ei = elb.entityDropItem(item, 0.0f);
				if (ei != null) 
				{
					ei.setPickupDelay(40);
				}
				elb.setItemStackToSlot(slot, ItemStack.EMPTY);
			}
		}
	}
}
