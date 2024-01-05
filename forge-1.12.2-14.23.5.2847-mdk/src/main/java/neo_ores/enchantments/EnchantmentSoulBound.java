package neo_ores.enchantments;

import neo_ores.main.Reference;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentSoulBound extends Enchantment
{
	public EnchantmentSoulBound()
	{
		super(Rarity.RARE, EnumEnchantmentType.ALL, EntityEquipmentSlot.values());
		this.setName("soulbound");
		this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "soulbound"));
	}

	public int getMinLevel()
	{
		return 1;
	}

	public int getMaxLevel()
	{
		return 1;
	}

	public int getMinEnchantability(int enchantmentLevel)
	{
		return 6;
	}

	public int getMaxEnchantability(int enchantmentLevel)
	{
		return super.getMinEnchantability(enchantmentLevel) + 60;
	}

	public boolean canApply(ItemStack stack)
	{
		return true;
	}
}
