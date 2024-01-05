package neo_ores.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public interface IPostscriptDataIntoSpell
{
	public NBTTagCompound postscript(ItemStack stack, World world, NBTTagCompound nbt);

	/**
	 * Desc Style : NBTTagList(compound).NBTTagList(string) For example :
	 * <code> {[["tranlatekey1","format1"],["translatekey2"],["translatekey3","format2","format3"]]} </code>
	 * Example of translate :
	 * <code> "transtalekey1=This is a description of %s" </code>
	 */
	public NBTTagList addFormattedDesc(ItemStack stack, World world, NBTTagList nbt);
}
