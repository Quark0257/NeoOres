package neo_ores.spell.effect;

import neo_ores.api.MathUtils;
import neo_ores.event.NeoOresRegisterEvents;
import neo_ores.util.SpellUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SpellBlink extends SpellEffectEntityBuffBase
{
	@Override
	protected void onBuff(World world, Entity elb, EntityLivingBase runner, ItemStack stack)
	{
		int length = 3 + this.amp * 2;
		Vec3d vec = MathUtils.getFromYawPitch(elb.rotationYaw, elb.rotationPitch);
		Vec3d pos = elb.getPositionVector().add(vec.normalize().scale(length));
		BlockPos scanBase = new BlockPos(pos);
		IBlockState state = world.getBlockState(scanBase);
		if (state.getMaterial() == Material.AIR || state.getMaterial().blocksMovement() || state.getMaterial() == Material.STRUCTURE_VOID)
		{
			if (elb.isRiding())
	        {
				elb.dismountRidingEntity();
	        }
			
			SpellUtils.onDisplayParticleTypeAEntity(world, elb, NeoOresRegisterEvents.particle0, SpellUtils.getColor(stack), 16);
			elb.setPositionAndUpdate(pos.x, pos.y, pos.z);
		}
	}
}
