package neo_ores.spell.effect;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.main.NeoOresData;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.RayTraceUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class SpellNight extends SpellEffect
{
	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		return RayTraceUtils.getMissResult();
	}

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (world.isRemote) 
		{
			return;
		}
		
		world.setWorldTime((world.getWorldTime() / 24000L) * 24000L + 13800L);
		
		if (runner instanceof EntityPlayerMP)
		{
			PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
			pmds.addMXP(10L);
		}
	}
}
