package neo_ores.item;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import neo_ores.api.LongUtils;
import neo_ores.api.NBTUtils;
import neo_ores.api.spell.SpellItem;
import neo_ores.config.NeoOresConfig;
import neo_ores.main.NeoOresData;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.PlayerMagicDataClient;
import neo_ores.util.SpellUtils;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag)
	{
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey(SpellUtils.NBTTagUtils.SPELL))
		{
			List<SpellItem> spells = SpellUtils.getListFromItemStackNBT(stack.getTagCompound().copy());
			list.add(I18n.format("tooltip.mana").trim() + " : " + LongUtils.convertString(SpellUtils.getMPConsume(spells)));
			NBTUtils.ForItemStack util = new NBTUtils.ForItemStack(stack);
			NBTTagList desclist = util.getListAsList("desc");
			for (int i = 0; i < desclist.tagCount(); i++)
			{
				NBTTagList desc = (NBTTagList) desclist.get(i);
				String main = "";
				List<Object> format = new ArrayList<Object>();
				for (int j = 0; j < desc.tagCount(); j++)
				{
					if (j == 0)
					{
						main = desc.getStringTagAt(j);
					}
					else
					{
						format.add(desc.getStringTagAt(j));
					}
				}
				TextComponentTranslation tct = new TextComponentTranslation(main, format.toArray());
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
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("metadata"))
		{
			return stack.getTagCompound().getInteger("metadata");
		}
		return 0;
	}

	public ActionResult<ItemStack> onRightClick(World world, EntityPlayer player, EnumHand hand, @Nullable EntityLivingBase target)
	{
		if (!NeoOresConfig.magic.repeatable)
		{
			player.setActiveHand(hand);
		}

		EnumActionResult actionResult = EnumActionResult.SUCCESS;
		ItemStack itemspell = player.getHeldItem(hand);
		if (itemspell.getTagCompound() != null && itemspell.getTagCompound().hasKey(SpellUtils.NBTTagUtils.SPELL))
		{
			List<SpellItem> rawSpellList = SpellUtils.getListFromItemStackNBT(itemspell.getTagCompound().copy());
			long manaConsume = SpellUtils.getMPConsume(rawSpellList);

			if (!player.isCreative())
			{
				if (!player.world.isRemote)
				{
					PlayerMagicData pmd = NeoOresData.instance.getPMD((EntityPlayerMP) player);
					if (manaConsume > pmd.getMana())
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
					PlayerMagicDataClient pmdc = NeoOresData.getPMDC(EntityPlayer.getUUID(player.getGameProfile()));
					if (manaConsume > pmdc.getMana())
					{
						return new ActionResult<ItemStack>(actionResult, player.getHeldItem(hand));
					}
				}
			}

			SpellUtils.run(rawSpellList, world, player, player.getHeldItem(hand), null);
		}

		return new ActionResult<ItemStack>(actionResult, player.getHeldItem(hand));
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		return this.onRightClick(world, player, hand, null);
	}

	/*
	 * public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player,
	 * EntityLivingBase target, EnumHand hand) { player.swingArm(hand);
	 * this.onRightClick(player.getEntityWorld(), player, hand, target); return
	 * true; }
	 */

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}
}
