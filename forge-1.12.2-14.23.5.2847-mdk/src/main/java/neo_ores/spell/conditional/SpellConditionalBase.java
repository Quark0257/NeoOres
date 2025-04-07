package neo_ores.spell.conditional;

import neo_ores.api.spell.Spell.SpellConditional;
import neo_ores.main.NeoOresData;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class SpellConditionalBase extends SpellConditional
{
	@Override
	public boolean checkRunnableAndRun(Event event, World world, EntityLivingBase runner, ItemStack stack, NBTTagCompound spells, long mana)
	{
		RayTraceResult result = this.getTarget(event, world, runner, stack, spells);
		if (result == null)
		{
			return false;
		}

		if (runner instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) runner;
			if (!player.isCreative())
			{
				if (!player.world.isRemote)
				{
					PlayerMagicData pmd = NeoOresData.instance.getPMD((EntityPlayerMP) player);
					if (mana > pmd.getMana())
					{
						return false;
					}
					else
					{
						pmd.addMana(-mana);
					}
				}
			}
		}

		SpellUtils.run(SpellUtils.getListFromItemStackNBT(spells), world, runner, stack, result.typeOfHit == Type.MISS ? null : result);
		return true;
	}

	/**
	 * 
	 * @param event
	 * @param world
	 * @param runner
	 * @param stack
	 * @param spells
	 * @return null means running will be canceled, miss means result is null
	 */
	public abstract RayTraceResult getTarget(Event event, World world, EntityLivingBase runner, ItemStack stack, NBTTagCompound spells);
}
