package neo_ores.item;

import java.util.List;

import javax.annotation.Nullable;

import neo_ores.api.spell.SpellItem;
import neo_ores.config.NeoOresConfig;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresData;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.PlayerMagicDataClient;
import neo_ores.util.SpellUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
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
						// TODO mana hunger effect
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
			
			player.getCooldownTracker().setCooldown(this, (int) ((double)NeoOresConfig.magic.base_cool_time * Math.max(1.0 - 0.1 * EnchantmentHelper.getEnchantmentLevel(NeoOres.fastspelling, itemspell), 0.0)));
			SpellUtils.run(rawSpellList, world, player, player.getHeldItem(hand), null);
		}

		return new ActionResult<ItemStack>(actionResult, player.getHeldItem(hand));
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		return this.onRightClick(world, player, hand, null);
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}
	
	public int getItemEnchantability(ItemStack stack) 
	{
		return 10;
	}
	
	public boolean isEnchantable(ItemStack stack)
    {
        return true;
    }
}
