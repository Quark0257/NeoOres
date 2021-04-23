package neo_ores.items;

import java.util.List;

import neo_ores.mana.PlayerManaDataServer;
import neo_ores.world.dimension.DimensionHelper.ToolType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemNeoHoe extends ItemHoe implements ItemNeoTool
{
	public ItemNeoHoe(Item.ToolMaterial material)
    {
    	super(material);
    }
	
	public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag)
	{
		super.addInformation(itemStack, world, list, flag);
		this.addTierInfo(itemStack, world, list, flag);
	}
	
	private ToolType type;
	
	@Override
	public Item setToolType(ToolType name) 
	{
		this.type = name;
		return this;
	}
	@Override
	public ToolType getToolType() 
	{
		return this.type;
	}
	
	public boolean canApplyAtEnchantingTable(ItemStack stack,Enchantment enchantment)
	{
		return enchantment.type == EnumEnchantmentType.WEAPON ? true : super.canApplyAtEnchantingTable(stack, enchantment);
	}
	
	public int getItemEnchantability()
    {
        return this.toolMaterial.getEnchantability();
    }
	
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(entityIn.ticksExisted % 200 == 0 && entityIn instanceof EntityPlayer && !worldIn.isRemote && 0 <= itemSlot && itemSlot < 9)
		{
			PlayerManaDataServer pmds = new PlayerManaDataServer((EntityPlayerMP)entityIn);
			if(pmds.getMana() > 0L)
			{
				stack.damageItem(-1, (EntityPlayer)entityIn);
			}
			pmds.addMana(-1L);
		}
	}
}
