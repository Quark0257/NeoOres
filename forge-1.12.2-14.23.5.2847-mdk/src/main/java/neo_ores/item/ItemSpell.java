package neo_ores.item;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import neo_ores.api.LongUtils;
import neo_ores.api.NBTUtils;
import neo_ores.api.spell.SpellItem;
import neo_ores.config.NeoOresConfig;
import neo_ores.util.PlayerManaDataClient;
import neo_ores.util.PlayerManaDataServer;
import neo_ores.util.SpellUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemSpell extends Item
{
	public ItemSpell()
    {
		this.setMaxStackSize(1);
        this.setCreativeTab(null);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }
	
	public boolean hasEffect(ItemStack item)
	{
		return true;
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
			list.add(I18n.format("tooltip.mana").trim() + " : " + LongUtils.convertString((long)(manasum * manapro)));
			NBTUtils.ForItemStack util = new NBTUtils.ForItemStack(stack);
			NBTTagList desclist = util.getListAsList("desc");
			for(int i = 0;i < desclist.tagCount();i++)
			{	
				NBTTagList desc = (NBTTagList)desclist.get(i);
				String main = "";
				List<Object> format = new ArrayList<Object>();
				for(int j = 0;j < desc.tagCount();j++)
				{
					if(j == 0)
					{
						main = desc.getStringTagAt(j);
					}
					else
					{
						format.add(desc.getStringTagAt(j));
					}
				}
				TextComponentTranslation tct = new TextComponentTranslation(main,format.toArray());
				list.add(tct.getFormattedText());
			}
		}
	}
	
	public int getMetadata(int damage)
	{
		return damage;
	}
	
	public int getMetadata(ItemStack stack)
	{
		if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("metadata"))
		{
			return stack.getTagCompound().getInteger("metadata");
		}
		return 0;
	}
	
	public ActionResult<ItemStack> onRightClick(World world, EntityPlayer player, EnumHand hand, @Nullable EntityLivingBase target)
	{
		//player.setActiveHand(hand);
		//EnumActionResult actionResult = (NeoOresConfig.miscellaneous.repeatable) ? EnumActionResult.FAIL : EnumActionResult.SUCCESS;
		
		if(!NeoOresConfig.magic.repeatable)
		{
			player.setActiveHand(hand);
		}
		
		EnumActionResult actionResult = EnumActionResult.SUCCESS;
		ItemStack itemspell = player.getHeldItem(hand);
		if(itemspell.getTagCompound() != null && itemspell.getTagCompound().hasKey(SpellUtils.NBTTagUtils.SPELL))
		{
			List<SpellItem> rawSpellList = SpellUtils.getListFromItemStackNBT(itemspell.getTagCompound().copy());
			long manaConsume = SpellUtils.getMPConsume(rawSpellList);
			List<SpellItem> initializedSpellList = SpellUtils.getListInitialized(rawSpellList);
			
			if(!player.isCreative())
			{
				if(!player.world.isRemote)
				{
					PlayerManaDataServer pmd = new PlayerManaDataServer((EntityPlayerMP)player);
					if(manaConsume > pmd.getMana())
					{
						return new ActionResult<ItemStack>(actionResult, player.getHeldItem(hand));
					}
					else
					{
						pmd.addMana(-manaConsume);
					}
				}
				else
				{
					PlayerManaDataClient pmdc = new PlayerManaDataClient((EntityPlayerSP)player);
					if(manaConsume > pmdc.getMana())
					{
						return new ActionResult<ItemStack>(actionResult, player.getHeldItem(hand));
					}
				}
			}
			
			SpellUtils.run(initializedSpellList, player, player.getHeldItem(hand), target);
		}

        return new ActionResult<ItemStack>(actionResult, player.getHeldItem(hand));
		
		/*
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
			for(SpellItem spellitem : SpellUtils.getListInitialized(SpellUtils.getListFromItemStackNBT(itemspell.getTagCompound().copy())))
			{
				manasum += spellitem.getCostsum();
				manapro *= spellitem.getCostproduct();
				if(spellitem.getSpellClass() instanceof Spell.SpellForm && ((Spell.SpellForm)spellitem.getSpellClass()).needPrimaryForm())
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
					Spell entityspell = entityspellitem.getSpellClass();
					for(SpellItem spell : spells)
					{
						if(spell.getSpellClass() instanceof Spell.SpellCorrection)
						{
							((Spell.SpellCorrection)spell.getSpellClass()).onCorrection(entityspell);
						}
					}
					if(entityspell instanceof Spell.SpellForm)
					{
						((Spell.SpellForm)entityspell).onSpellRunning(world, (EntityLivingBase)player,player.getHeldItem(hand),result,SpellUtils.getItemStackNBTFromList(spells, new NBTTagCompound()));				
					}
				}
			}
			else
			{
				List<SpellItem> formspells = new ArrayList<SpellItem>();
				List<SpellItem> notformspells = new ArrayList<SpellItem>();
				for(SpellItem spell : spells)
				{
					if(spell.getSpellClass() instanceof Spell.SpellForm)
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
					Spell form = formspell.getSpellClass();
					for(SpellItem notformspell : notformspells)
					{
						if(notformspell.getSpellClass() instanceof Spell.SpellCorrection)
						{
							((Spell.SpellCorrection)notformspell.getSpellClass()).onCorrection(form);
						}
					}
					
					if(form instanceof Spell.SpellForm)
					{
						((Spell.SpellForm)form).onSpellRunning(world, (EntityLivingBase)player,player.getHeldItem(hand),result,SpellUtils.getItemStackNBTFromList(notformspells, new NBTTagCompound()));
					}
				}
			}
		}

        return new ActionResult<ItemStack>(actionResult, player.getHeldItem(hand));
        */
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
	
	public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }
}
