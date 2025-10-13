package neo_ores.spell.effect;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class SpellFilterWhiteList extends SpellEffect implements HasRange
{
	private int range = 0;

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result == null)
			return;
		if (result.typeOfHit == RayTraceResult.Type.ENTITY)
		{
			Entity entity = result.entityHit;
			if (entity instanceof EntityItem)
			{
				EntityItem entityitem = (EntityItem) entity;
				ItemStack target = entityitem.getItem();
				if (!SpellUtils.containsSpell(target))
				{
					return;
				}
				SpellUtils.onDisplayParticleTypeAEntity(world, entityitem, SpellUtils.getColor(stack), 16);
				List<Entity> list = HasRange.getRangedEntities(world, -1, 0.5 + this.range, entity, runner, false, true);
				List<ItemStack> whiteList = SpellUtils.getFilteredItems(target, false);
				List<ItemStack> blackList = SpellUtils.getFilteredItems(target, true);
				List<ItemStack> removeList = new ArrayList<ItemStack>();
				label: for (Entity temp : list)
				{
					if (temp instanceof EntityItem && entity != temp)
					{
						ItemStack tempStack = ((EntityItem) temp).getItem();
						for (ItemStack item : blackList)
						{
							if (SpellUtils.isMatch(tempStack, item))
							{
								removeList.add(item);
								continue label;
							}
						}
						for (ItemStack item : whiteList)
						{
							if (SpellUtils.isMatch(tempStack, item))
							{
								continue label;
							}
						}
						ItemStack copy = tempStack.copy();
						copy.setCount(1);
						whiteList.add(copy);
					}
				}
				blackList.removeAll(removeList);
				SpellUtils.setFilteredItems(target, whiteList, false);
				SpellUtils.setFilteredItems(target, blackList, true);
				
				if (runner instanceof EntityPlayerMP)
				{
					PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
					pmds.addMXP(1L);
				}
			}
		}
	}

	@Override
	public void setRange(int value)
	{
		this.range = value;
	}

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		return null;
	}
}
