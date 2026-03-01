package neo_ores.spell.effect;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasDamageLevel;
import neo_ores.spell.SpellItemInterfaces.HasSilk;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.RayTraceUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class SpellExplode extends SpellEffect implements HasDamageLevel, HasSilk
{
	private int level = 0;
	private boolean isSilk = false;
	
	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		return RayTraceUtils.getSimpleResult(runner);
	}

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		if (result.typeOfHit == Type.BLOCK)
		{
			BlockPos pos = result.getBlockPos().add(result.sideHit.getDirectionVec());
			x = pos.getX() + 0.5D;
			y = pos.getY() + 0.5D;
			z = pos.getZ() + 0.5D;
		}
		else if (result.typeOfHit == Type.ENTITY)
		{
			Entity e = result.entityHit;
			x = e.posX;
			y = e.posY;
			z = e.posZ;
		}
		else
		{
			return;
		}
		
		boolean flag = this.canEditBlocksBySpells(runner, stack, world, null, null) && !this.isSilk;
		if (runner instanceof EntityPlayer) 
		{
			if (!this.canEditBlocksBySpells(runner, stack, world, null, null) && !this.isSilk) 
			{
				return;
			}
		}
		world.createExplosion(runner, x, y, z, this.level + 1.0F, flag);
		if (runner instanceof EntityPlayerMP)
		{
			PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
			pmds.addMXP(10L + (long) Math.pow(1.5, this.level));
		}
	}

	@Override
	public void setDamageLevel(int value)
	{
		this.level = value;
	}

	@Override
	public void setSilkTouch()
	{
		this.isSilk = true;
	}
}
