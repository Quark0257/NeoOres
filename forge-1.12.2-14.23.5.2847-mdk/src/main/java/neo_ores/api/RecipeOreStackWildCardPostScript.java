package neo_ores.api;

import neo_ores.item.IPostscriptDataIntoSpell;
import neo_ores.util.SpellPlaceInfinityUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;

public abstract class RecipeOreStackWildCardPostScript extends RecipeOreStackWildCard implements IPostscriptDataIntoSpell
{
	public static final RecipeOreStackWildCardPostScript INFINITY_SPELL_MATERIAL = new RecipeOreStackWildCardPostScript("INFINITY SPELL MATERIAL", 1) 
	{
		@Override
		public NBTTagCompound postscript(ItemStack stack, World world, NBTTagCompound nbt)
		{
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("id", Item.REGISTRY.getNameForObject(stack.getItem()).toString());
			compound.setInteger("metadata", stack.getCount());
			nbt.setTag("storedBlock", compound);
			return nbt;
		}

		@Override
		public NBTTagList addFormattedDesc(ItemStack stack, World world, NBTTagList nbt)
		{
			NBTTagList list = new NBTTagList();
			list.appendTag(new NBTTagString(stack.getItem().getUnlocalizedName(stack) + ".name"));
			nbt.appendTag(list);
			return nbt;
		}

		@Override
		public void invertPostscript(ItemStack item, World world, NBTTagCompound additionalData)
		{
		}

		@Override
		public boolean compareWith(ItemStack target)
		{
			return SpellPlaceInfinityUtils.match(target);
		}

		@Override
		public ItemStack getRepresentative()
		{
			return new ItemStack(Blocks.COBBLESTONE);
		}

		@Override
		public ItemStack reverse(NBTTagCompound compound)
		{
			if (compound == null) 
			{
				return ItemStack.EMPTY;
			}
			NBTTagCompound tag = compound.getCompoundTag("storedBlock");
			if (tag == null) 
			{
				return ItemStack.EMPTY;
			}
			int meta = tag.getInteger("metadata");
			Item item = Item.getByNameOrId(tag.getString("id"));
			if (item == null) 
			{
				return ItemStack.EMPTY;
			}
			return new ItemStack(item, 1, meta);
		}
	};
	
	public RecipeOreStackWildCardPostScript(Object object, int size)
	{
		super(object, size);
	}

	public abstract ItemStack reverse(NBTTagCompound compound);
}
