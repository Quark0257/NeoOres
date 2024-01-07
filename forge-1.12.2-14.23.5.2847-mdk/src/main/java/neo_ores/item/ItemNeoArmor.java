package neo_ores.item;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.TierUtils;
import neo_ores.main.NeoOresItems;
import neo_ores.util.PlayerManaDataServer;
import neo_ores.world.dimension.DimensionHelper.ToolType;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemNeoArmor extends ItemArmor implements INeoOresItem, IItemNeoTool
{

	public ItemNeoArmor(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn)
	{
		super(materialIn, renderIndexIn, equipmentSlotIn);
		if (materialIn == NeoOresItems.armorCreative)
		{
			this.setMaxDamage(Integer.MAX_VALUE);
		}
	}
	
	public EntityEquipmentSlot getES()
    {
        return this.armorType;
    }

	public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
	{
		if (!world.isRemote && player.ticksExisted % 200 == 0)
		{
			PlayerManaDataServer pmds = new PlayerManaDataServer((EntityPlayerMP) player);
			if (pmds.getMana() > 0)
			{
				pmds.addMana(-1L);
				stack.damageItem(-1, player);
			}

			if (this.getES() == EntityEquipmentSlot.HEAD)
			{
				if (this.getArmorMaterial() == NeoOresItems.armorSylphite)
				{
					player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 320, 1, false, false));
				}

				if (this.getArmorMaterial() == NeoOresItems.armorSalamite)
				{
					player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 320, 1, false, false));
				}

				if (this.getArmorMaterial() == NeoOresItems.armorUndite)
				{
					player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 320, 1, false, false));
				}

				if (this.getArmorMaterial() == NeoOresItems.armorGnomite)
				{
					player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 320, 1, false, false));
				}

				if (this.getArmorMaterial() == NeoOresItems.armorCreative)
				{
					player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 320, 1, false, false));
					player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 320, 1, false, false));
					player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 320, 1, false, false));
					player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 320, 1, false, false));
				}
			}
		}
	}

	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag)
	{
		super.addInformation(stack, world, list, flag);
		if (stack.getItem() instanceof IItemNeoTool)
		{
			TierUtils utils = new TierUtils(stack);
			if (utils.getAir() == 11 && utils.getEarth() == 11 && utils.getFire() == 11 && utils.getWater() == 11)
			{
				list.add(TextFormatting.WHITE + I18n.format("armortip.space").trim());
				return;
			}

			if (utils.getAir() != 0)
			{
				list.add(TextFormatting.AQUA + I18n.format("armortip.air").trim() + " " + I18n.format("tier." + utils.getAir()).trim());
			}

			if (utils.getEarth() != 0)
			{
				list.add(TextFormatting.GREEN + I18n.format("armortip.earth").trim() + " " + I18n.format("tier." + utils.getEarth()).trim());
			}

			if (utils.getFire() != 0)
			{
				list.add(TextFormatting.GOLD + I18n.format("armortip.fire").trim() + " " + I18n.format("tier." + utils.getFire()).trim());
			}

			if (utils.getWater() != 0)
			{
				list.add(TextFormatting.DARK_PURPLE + I18n.format("armortip.water").trim() + " " + I18n.format("tier." + utils.getWater()).trim());
			}
		}
	}

	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		super.getSubItems(tab, items);
		if (this.isInCreativeTab(tab))
		{
			List<ItemStack> adds = new ArrayList<ItemStack>();
			List<ItemStack> removes = new ArrayList<ItemStack>();
			for (ItemStack stack : items)
			{
				if (stack.getItem() instanceof ItemNeoArmor)
				{
					if (((IItemNeoTool) stack.getItem()).getToolType() == ToolType.CREATIVE)
					{
						ItemStack stack1 = stack.copy();
						removes.add(stack);
						TierUtils tier = new TierUtils(stack1);
						tier.setTier(11, 11, 11, 11);
						adds.add(stack1);
					}
				}
			}
			items.removeAll(removes);
			items.addAll(adds);
		}
	}

	private ToolType type;

	@Override
	public Item setToolType(ToolType name)
	{
		type = name;
		return this;
	}

	@Override
	public ToolType getToolType()
	{
		return type;
	}
}
