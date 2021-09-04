package neo_ores.enchantments;

import neo_ores.main.Reference;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class EnchantmentOffensive extends Enchantment
{
	public EnchantmentOffensive() 
	{
		super(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
		this.setName("offensive");
		this.setRegistryName(new ResourceLocation(Reference.MOD_ID,"offensive"));
	}
	
	public int getMinLevel()
    {
        return 1;
    }
	
    public int getMaxLevel()
    {
        return 5;
    }
    
    public int getMinEnchantability(int enchantmentLevel)
    {
        return 15 + (enchantmentLevel - 1) * 7;
    }

    public int getMaxEnchantability(int enchantmentLevel)
    {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
    
    public void onEntityDamaged(EntityLivingBase user, Entity target, int level)
    {
    	target.hurtResistantTime = 10 / level - 2;
    }
}
