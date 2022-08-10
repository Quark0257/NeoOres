package neo_ores.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import neo_ores.api.TierUtils;
import neo_ores.main.NeoOresItems;
import neo_ores.main.Reference;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class NeoItemEvent 
{
	private static Random random = new Random();
	@SubscribeEvent
	public static void onHarvestBlock(BlockEvent.HarvestDropsEvent event)
	{
		if(event.getHarvester() != null)
		{
			ItemStack heldItem = event.getHarvester().getHeldItemMainhand().copy();
			float totalXp = 0;
			TierUtils tier = new TierUtils(heldItem);
			if(heldItem !=null)
			{
				if(heldItem.getItem() == NeoOresItems.undite_axe || heldItem.getItem() == NeoOresItems.undite_hoe || heldItem.getItem() == NeoOresItems.undite_pickaxe || heldItem.getItem() == NeoOresItems.undite_shovel || heldItem.getItem() == NeoOresItems.undite_sword)
				{
					int level = tier.getWater();
					if(level == 1)
					{
						event.getHarvester().addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 0));
					}
				}
				else if(heldItem.getItem() == NeoOresItems.salamite_axe || heldItem.getItem() == NeoOresItems.salamite_hoe || heldItem.getItem() == NeoOresItems.salamite_pickaxe || heldItem.getItem() == NeoOresItems.salamite_shovel || heldItem.getItem() == NeoOresItems.salamite_sword)
				{
					List<ItemStack> drops = new ArrayList<ItemStack>();
					drops.clear();
					for(int i = 0;i < event.getDrops().size();i++)
					{
						ItemStack itemStack = FurnaceRecipes.instance().getSmeltingResult(event.getDrops().get(i));
						
						if(itemStack == ItemStack.EMPTY)
						{
							drops.add(event.getDrops().get(i));
						}
						else if(Block.getBlockFromItem(event.getDrops().get(i).getItem()) == Blocks.AIR)
						{
							float xp = FurnaceRecipes.instance().getSmeltingExperience(event.getDrops().get(i));
							drops.add(itemStack);
							event.getHarvester().addExperience((int)xp);
						}
						else
						{
							float xp = FurnaceRecipes.instance().getSmeltingExperience(event.getDrops().get(i));
							int count = itemStack.getCount();
							int fortuneLevel = 0; 
							fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, heldItem);
							if(fortuneLevel > 0)
							{
								xp *= (float)(1 + random.nextInt(fortuneLevel));
								count += random.nextInt(fortuneLevel);
							}
							ItemStack copyItem = itemStack.copy();
							copyItem.setCount(count);
							//drops.add(new ItemStack(itemStack.getItem(),count,itemStack.getMetadata()));
							drops.add(copyItem);
							totalXp += xp;
						}
					}
					event.getDrops().clear();
					event.getDrops().addAll(drops);
					event.getHarvester().addExperience((int)totalXp);
				}
			}
		}
	}
}
