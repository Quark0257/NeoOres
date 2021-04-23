package neo_ores.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import neo_ores.config.NeoOresConfig;
import neo_ores.mana.LongUtils;
import neo_ores.mana.PlayerManaDataClient;
import neo_ores.mana.PlayerManaDataServer;
import neo_ores.spell.ISpell;
import neo_ores.spell.SpellItem;
import neo_ores.spell.SpellType;
import neo_ores.spell.SpellItemType;
import neo_ores.spell.SpellUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class ItemSpell extends ItemEffected
{
	public ItemSpell()
    {
		this.setMaxStackSize(1);
        this.setCreativeTab(null);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }
	
	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag)
	{
		if(stack.getTagCompound() != null && stack.getTagCompound().hasKey(SpellUtils.NBTTagUtils.SPELL))
		{
			long manasum = 0L;
			float manapro = 1.0F;
			for(SpellItem spellitem : SpellUtils.getListFromItemStackNBT(stack.getTagCompound().copy()))
			{
				manasum += spellitem.getCostsum();
				manapro *= spellitem.getCostproduct();
			}
			list.add(I18n.translateToLocal("tooltip.mana").trim() + ":" + LongUtils.convertString((long)(manasum * manapro)));
		}
	}
	
	public int getMetadata(int damage)
	{
		return damage;
	}
	
	public int getMetadata(ItemStack stack)
	{
		if(stack.getTagCompound() != null && stack.getTagCompound().hasKey(SpellUtils.NBTTagUtils.SPELL))
		{
			int air = 0;
			int earth = 0;
			int fire = 0;
			int water = 0;
			for(SpellItem spellitem : SpellUtils.getListFromItemStackNBT(stack.getTagCompound().copy()))
			{
				if(spellitem.getType() == SpellItemType.AIR)
				{
					air++;
				}
				else if(spellitem.getType() == SpellItemType.EARTH)
				{
					earth++;
				}
				else if(spellitem.getType() == SpellItemType.FIRE)
				{
					fire++;
				}
				else if(spellitem.getType() == SpellItemType.WATER)
				{
					water++;
				}
			}
			
			return this.compare(air, earth, fire, water);
		}
		return 0;
	}
	
	private int compare(int air,int earth,int fire,int water)
	{
		int i = air;
		if(i < earth) i = earth;
		if(i < fire) i = fire;
		if(i < water) i = water;
		
		if(i == air)
		{
			return 0;
		}
		else if(i == earth)
		{
			return 1;
		}
		else if(i == fire)
		{
			return 2;
		}
		else if(i == water)
		{
			return 3;
		}
		else
		{
			return 0;
		}
	}
	
	public ActionResult<ItemStack> onRightClick(World world, EntityPlayer player, EnumHand hand, @Nullable EntityLivingBase target)
    {
		System.out.println(NeoOresConfig.miscellaneous.repeatable);
		//player.setActiveHand(hand);
		//EnumActionResult actionResult = (NeoOresConfig.miscellaneous.repeatable) ? EnumActionResult.FAIL : EnumActionResult.SUCCESS;
		if(!NeoOresConfig.miscellaneous.repeatable)
		{
			player.setActiveHand(hand);
		}
		
		EnumActionResult actionResult = EnumActionResult.SUCCESS;
		RayTraceResult result = null;
		ItemStack itemspell = player.getHeldItem(hand);
		if(itemspell.getTagCompound() != null && itemspell.getTagCompound().hasKey(SpellUtils.NBTTagUtils.SPELL))
		{
			List<SpellItem> entityspells = new ArrayList<SpellItem>();
			List<SpellItem> spells = new ArrayList<SpellItem>();
			long manasum = 0L;
			float manapro = 1.0F;
			for(SpellItem spellitem : SpellUtils.getListFromItemStackNBT(itemspell.getTagCompound().copy()))
			{
				manasum += spellitem.getCostsum();
				manapro *= spellitem.getCostproduct();
				if(spellitem.getSpellClass().isCreateSpellEntity())
				{
					entityspells.add(spellitem);
				}
				else
				{
					spells.add(spellitem);
				}
			}
			
			if(!player.isCreative())
			{
				if(!player.world.isRemote)
				{
					PlayerManaDataServer pmd = new PlayerManaDataServer((EntityPlayerMP)player);
					if(manasum * manapro > pmd.getMana())
					{
						return new ActionResult<ItemStack>(actionResult, player.getHeldItem(hand));
					}
					else
					{
						pmd.addMana(-(long)(manasum * manapro));
					}
				}
				else
				{
					PlayerManaDataClient pmdc = new PlayerManaDataClient((EntityPlayerSP)player);
					if(manasum * manapro > pmdc.getMana())
					{
						return new ActionResult<ItemStack>(actionResult, player.getHeldItem(hand));
					}
				}
			}
			
			if(target != null)
			{
				result = new RayTraceResult(target);
			}
			
			if(!entityspells.isEmpty())
			{
				for(SpellItem entityspellitem : entityspells)
				{
					ISpell entityspell = entityspellitem.getSpellClass();
					for(SpellItem spell : spells)
					{
						if(spell.getSpellClass().getSpellItemType() == SpellType.CORRECTION)
						{
							spell.getSpellClass().onCorrection(entityspell);
						}
					}
					
					entityspell.onSpellRunning(world, (EntityLivingBase)player,player.getHeldItem(hand),result,SpellUtils.getItemStackNBTFromList(spells, new NBTTagCompound()));
				}
			}
			else
			{
				List<SpellItem> formspells = new ArrayList<SpellItem>();
				List<SpellItem> notformspells = new ArrayList<SpellItem>();
				for(SpellItem spell : spells)
				{
					if(spell.getSpellClass().getSpellItemType() == SpellType.FORM)
					{
						formspells.add(spell);
					}
					else
					{
						notformspells.add(spell);
					}
				}
				
				for(SpellItem formspell : formspells)
				{
					ISpell form = formspell.getSpellClass();
					for(SpellItem notformspell : notformspells)
					{
						if(notformspell.getSpellClass().getSpellItemType() == SpellType.CORRECTION)
						{
							notformspell.getSpellClass().onCorrection(form);
						}
					}
					
					form.onSpellRunning(world, (EntityLivingBase)player,player.getHeldItem(hand),result,SpellUtils.getItemStackNBTFromList(notformspells, new NBTTagCompound()));
				}
			}
		}

        return new ActionResult<ItemStack>(actionResult, player.getHeldItem(hand));
    }
	
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		return this.onRightClick(world, player, hand,null);
	}
	
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand)
	{
		player.swingArm(hand);
		this.onRightClick(player.getEntityWorld(), player, hand, target);
		return true;
	}
	
	public RayTraceResult rayTraceForPublic(World world, EntityPlayer player, boolean liquid)
	{
		return this.rayTrace(world, player, liquid);
	}
	
	public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }
}
