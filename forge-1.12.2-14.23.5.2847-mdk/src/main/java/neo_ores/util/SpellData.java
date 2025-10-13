package neo_ores.util;

import java.util.Arrays;
import java.util.List;

import neo_ores.api.spell.SpellItem;
import neo_ores.main.NeoOresItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;

public class SpellData
{
	public static final SpellData EMPTY = new SpellData(Arrays.asList(), 0xFFFFFF, new NBTTagCompound());
	
	private final int color;
	private final List<SpellItem> list;
	private final NBTTagCompound compound;

	public SpellData(List<SpellItem> spells, int color, NBTTagCompound spellData)
	{
		this.list = spells;
		this.color = color;
		this.compound = spellData;
	}

	public void run(EntityLivingBase runner, RayTraceResult result)
	{
		ItemStack stack = new ItemStack(NeoOresItems.spell, 1);
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setTag(SpellUtils.NBTTagUtils.ADDITIONAL, this.compound);
		stack.getTagCompound().setInteger("color", this.color);
		SpellUtils.run(this.list, runner.getEntityWorld(), runner, stack, result);
	}
}
