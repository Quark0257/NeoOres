package neo_ores.spell.effect;

import neo_ores.api.MathUtils;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasAmplify;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.SpellUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SpellBlink extends SpellEffectEntityBase implements HasAmplify
{
	private int amp = 0;
	
	@Override
	protected void onEffect(World world, Entity elb, EntityLivingBase runner, ItemStack stack)
	{
		if (this.isFakePlayer(elb)) 
		{
			return;
		}
		
		if (elb instanceof EntityLivingBase) 
		{
			EntityLivingBase living = (EntityLivingBase)elb;
			if (living.isPotionActive(NeoOres.antienderteleport) && living.getActivePotionEffect(NeoOres.antienderteleport).getAmplifier() >= 1)
			{
				return;
			}
		}
		
		// TODO check space more
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
			
			SpellUtils.onDisplayParticleTypeAEntity(world, elb, SpellUtils.getColor(stack), 16);
			elb.setPositionAndUpdate(pos.x, pos.y, pos.z);
			
			if (runner instanceof EntityPlayerMP)
			{
				PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
				pmds.addMXP(1L);
			}
		}
	}

	@Override
	public void setAmplify(int level)
	{
		this.amp = level;
	}
}
