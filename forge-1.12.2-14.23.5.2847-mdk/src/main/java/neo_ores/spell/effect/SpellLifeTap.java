package neo_ores.spell.effect;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.spell.SpellItem;
import neo_ores.main.NeoOresData;
import neo_ores.main.NeoOresSpells;
import neo_ores.spell.SpellItemInterfaces.HasAmplify;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SpellLifeTap extends SpellEffectEntityBase implements HasAmplify
{
	private int amp = 0;

	@Override
	public void setAmplify(int level)
	{
		this.amp = level;
	}

	@Override
	protected void onEffect(World world, Entity elb, EntityLivingBase runner, ItemStack stack)
	{
		float pay = (float)(Math.pow(2.0, this.amp + 1.0)) * 3.0F;
		SpellUtils.onDisplayParticleTypeAEntity(world, elb, SpellUtils.getColor(stack), 16);
		if (!SpellUtils.spellPay(runner, pay))
			return;
		if (elb instanceof EntityPlayerMP)
		{
			double rate = 2.0 * pay / (runner.getMaxHealth() * 3.0);
			PlayerMagicData pmd = NeoOresData.instance.getPMD((EntityPlayerMP) elb);
			pmd.addMana(Math.min((long) (rate * pmd.getMaxMana()), maxMana(this.amp)));
			if (runner instanceof EntityPlayerMP)
			{
				PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
				pmds.addMXP(1L + this.amp * 5);
			}
		}
	}
	
	public static long maxMana(int amp) 
	{
		List<SpellItem> spells = new ArrayList<>();
		spells.add(NeoOresSpells.spell_heal);
		if (amp >= 1) 
		{
			spells.add(NeoOresSpells.spell_amplify1);
		}
		if (amp >= 2) 
		{
			spells.add(NeoOresSpells.spell_amplify2);
		}
		if (amp >= 3) 
		{
			spells.add(NeoOresSpells.spell_amplify3);
		}
		long consume = SpellUtils.getMPConsume(spells);
		return consume / 2;
	}
}
