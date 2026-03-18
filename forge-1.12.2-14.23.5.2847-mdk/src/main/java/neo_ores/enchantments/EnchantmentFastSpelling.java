package neo_ores.enchantments;

import neo_ores.main.NeoOres;
import neo_ores.main.Reference;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentFastSpelling extends Enchantment
{
	public EnchantmentFastSpelling()
	{
		super(Rarity.RARE, NeoOres.SPELLS, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
		this.setName("fast_spelling");
		this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "fast_spelling"));
	}

	public int getMinLevel()
	{
		return 1;
	}

	public int getMaxLevel()
	{
		return 10;
	}

	public int getMinEnchantability(int enchantmentLevel)
	{
		return 5 + (enchantmentLevel - 1) * 3;
	}

	public int getMaxEnchantability(int enchantmentLevel)
	{
		return super.getMinEnchantability(enchantmentLevel) + 50;
	}

	public boolean canApply(ItemStack stack)
	{
		return NeoOres.SPELLS.canEnchantItem(stack.getItem());
	}
}
