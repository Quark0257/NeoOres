package neo_ores.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import neo_ores.api.TierUtils;
import neo_ores.api.spell.SpellItem;
import neo_ores.item.ItemRecipeSheet;
import neo_ores.item.ItemSpell;
import neo_ores.main.NeoOresItems;
import neo_ores.main.Reference;
import neo_ores.util.SpellUtils;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class NeoOresItemEvent
{
	private static Random random = new Random();

	private static boolean isShift()
	{
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTooltip(ItemTooltipEvent e)
	{
		ItemStack stack = e.getItemStack();
		if (stack.getItem() instanceof ItemSpell)
		{
			e.getToolTip().add("");
			if (isShift())
			{
				for (SpellItem item : SpellUtils.getListFromItemStackNBT(stack.getTagCompound()))
				{
					e.getToolTip().add(TextFormatting.BLUE + ItemRecipeSheet.getName(item) + (e.getFlags().isAdvanced() ? " (" + item.toString() + ")" : ""));
				}
			}
			else
			{
				e.getToolTip().add(TextFormatting.BLUE + I18n.format("neo_ores.pressLShiftDesc"));
			}
		}
	}

	@SubscribeEvent
	public static void onHarvestBlock(BlockEvent.HarvestDropsEvent event)
	{
		if (event.getHarvester() != null)
		{
			ItemStack heldItem = event.getHarvester().getHeldItemMainhand().copy();
			float totalXp = 0;
			TierUtils tier = new TierUtils(heldItem);
			if (heldItem != null)
			{
				if (heldItem.getItem() == NeoOresItems.undite_axe || heldItem.getItem() == NeoOresItems.undite_hoe || heldItem.getItem() == NeoOresItems.undite_pickaxe
						|| heldItem.getItem() == NeoOresItems.undite_shovel || heldItem.getItem() == NeoOresItems.undite_sword)
				{
					int level = tier.getWater();
					if (level == 1)
					{
						event.getHarvester().addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 0));
					}
				}
				else if (heldItem.getItem() == NeoOresItems.salamite_axe || heldItem.getItem() == NeoOresItems.salamite_hoe || heldItem.getItem() == NeoOresItems.salamite_pickaxe
						|| heldItem.getItem() == NeoOresItems.salamite_shovel || heldItem.getItem() == NeoOresItems.salamite_sword)
				{
					List<ItemStack> drops = new ArrayList<ItemStack>();
					drops.clear();
					for (int i = 0; i < event.getDrops().size(); i++)
					{
						ItemStack itemStack = FurnaceRecipes.instance().getSmeltingResult(event.getDrops().get(i));

						if (itemStack == ItemStack.EMPTY)
						{
							drops.add(event.getDrops().get(i));
						}
						else if (Block.getBlockFromItem(event.getDrops().get(i).getItem()) == Blocks.AIR)
						{
							float xp = FurnaceRecipes.instance().getSmeltingExperience(event.getDrops().get(i));
							drops.add(itemStack);
							event.getHarvester().addExperience((int) xp);
						}
						else
						{
							float xp = FurnaceRecipes.instance().getSmeltingExperience(event.getDrops().get(i));
							int count = itemStack.getCount();
							int fortuneLevel = 0;
							fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, heldItem);
							if (fortuneLevel > 0)
							{
								xp *= (float) (1 + random.nextInt(fortuneLevel));
								count += random.nextInt(fortuneLevel);
							}
							ItemStack copyItem = itemStack.copy();
							copyItem.setCount(count);
							// drops.add(new ItemStack(itemStack.getItem(),count,itemStack.getMetadata()));
							drops.add(copyItem);
							totalXp += xp;
						}
					}
					event.getDrops().clear();
					event.getDrops().addAll(drops);
					event.getHarvester().addExperience((int) totalXp);
				}
			}
		}
	}
}
