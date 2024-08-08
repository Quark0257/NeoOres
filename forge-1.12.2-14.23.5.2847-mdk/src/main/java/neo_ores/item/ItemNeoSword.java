package neo_ores.item;

import java.util.List;

import neo_ores.api.TierUtils;
import neo_ores.main.NeoOresData;
import neo_ores.util.PlayerMagicData;
import neo_ores.world.dimension.DimensionHelper.ToolType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemNeoSword extends ItemSword implements IItemNeoTool, INeoOresItem
{
	public ItemNeoSword(ToolMaterial material)
	{
		super(material);
	}

	public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag)
	{
		super.addInformation(itemStack, world, list, flag);
		this.addTierInfo(itemStack, world, list, flag);
	}

	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (entityIn.ticksExisted % 200 == 0 && entityIn instanceof EntityPlayer && !worldIn.isRemote && 0 <= itemSlot && itemSlot < 9)
		{
			PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) entityIn);
			if (pmds.getMana() > 0L)
			{
				stack.damageItem(-1, (EntityPlayer) entityIn);
			}
			pmds.addMana(-1L);
		}
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

	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		super.getSubItems(tab, items);
		if (this.isInCreativeTab(tab))
		{
			for (ItemStack stack : items)
			{
				if (stack.getItem() instanceof ItemNeoSword)
				{
					if (((IItemNeoTool) stack.getItem()).getToolType() == ToolType.CREATIVE)
					{
						ItemStack stack1 = stack.copy();
						items.remove(stack);
						TierUtils tier = new TierUtils(stack1);
						tier.setTier(11, 11, 11, 11);
						items.add(stack1);
						break;
					}
				}
			}
		}
	}
}
