package neo_ores.api;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTUtils
{
	private final NBTTagCompound nbt;

	public NBTUtils(NBTTagCompound target)
	{
		this.nbt = target;
	}

	public NBTTagCompound get()
	{
		return nbt;
	}

	public byte getByte(String key)
	{
		return nbt.hasKey(key, 1) ? nbt.getByte(key) : 0;
	}

	public boolean getBoolean(String key)
	{
		return nbt.hasKey(key, 1) ? nbt.getBoolean(key) : false;
	}

	public short getShort(String key)
	{
		return nbt.hasKey(key, 2) ? nbt.getShort(key) : 0;
	}

	public int getInt(String key)
	{
		return nbt.hasKey(key, 3) ? nbt.getInteger(key) : 0;
	}

	public long getLong(String key)
	{
		return nbt.hasKey(key, 4) ? nbt.getLong(key) : 0L;
	}

	public float getFloat(String key)
	{
		return nbt.hasKey(key, 5) ? nbt.getFloat(key) : 0.0F;
	}

	public double getDouble(String key)
	{
		return nbt.hasKey(key, 6) ? nbt.getDouble(key) : 0.0;
	}

	public byte[] getByteArray(String key)
	{
		return nbt.hasKey(key, 7) ? nbt.getByteArray(key) : new byte[0];
	}

	public String getString(String key)
	{
		return nbt.hasKey(key, 8) ? nbt.getString(key) : "";
	}

	public NBTTagList getListAsByte(String key)
	{
		return nbt.hasKey(key, 9) ? nbt.getTagList(key, 1) : new NBTTagList();
	}

	public NBTTagList getListAsShort(String key)
	{
		return nbt.hasKey(key, 9) ? nbt.getTagList(key, 2) : new NBTTagList();
	}

	public NBTTagList getListAsInt(String key)
	{
		return nbt.hasKey(key, 9) ? nbt.getTagList(key, 3) : new NBTTagList();
	}

	public NBTTagList getListAsLong(String key)
	{
		return nbt.hasKey(key, 9) ? nbt.getTagList(key, 4) : new NBTTagList();
	}

	public NBTTagList getListAsFloat(String key)
	{
		return nbt.hasKey(key, 9) ? nbt.getTagList(key, 5) : new NBTTagList();
	}

	public NBTTagList getListAsDouble(String key)
	{
		return nbt.hasKey(key, 9) ? nbt.getTagList(key, 6) : new NBTTagList();
	}

	public NBTTagList getListAsByteArray(String key)
	{
		return nbt.hasKey(key, 9) ? nbt.getTagList(key, 7) : new NBTTagList();
	}

	public NBTTagList getListAsString(String key)
	{
		return nbt.hasKey(key, 9) ? nbt.getTagList(key, 8) : new NBTTagList();
	}

	public NBTTagList getListAsList(String key)
	{
		return nbt.hasKey(key, 9) ? nbt.getTagList(key, 9) : new NBTTagList();
	}

	public NBTTagList getListAsCompound(String key)
	{
		return nbt.hasKey(key, 9) ? nbt.getTagList(key, 10) : new NBTTagList();
	}

	public NBTTagList getListAsIntArray(String key)
	{
		return nbt.hasKey(key, 9) ? nbt.getTagList(key, 11) : new NBTTagList();
	}

	public NBTTagCompound getCompound(String key)
	{
		return nbt.hasKey(key, 10) ? nbt.getCompoundTag(key) : new NBTTagCompound();
	}

	public int[] getIntArray(String key)
	{
		return nbt.hasKey(key, 11) ? nbt.getIntArray(key) : new int[0];
	}

	public void setByte(String key, byte value)
	{
		this.nbt.setByte(key, value);
	}

	public void setBoolean(String key, boolean value)
	{
		this.nbt.setBoolean(key, value);
	}

	public void setShort(String key, short value)
	{
		this.nbt.setShort(key, value);
	}

	public void setInt(String key, int value)
	{
		this.nbt.setInteger(key, value);
	}

	public void setLong(String key, long value)
	{
		this.nbt.setLong(key, value);
	}

	public void setFloat(String key, float value)
	{
		this.nbt.setFloat(key, value);
	}

	public void setDouble(String key, double value)
	{
		this.nbt.setDouble(key, value);
	}

	public void setByteArray(String key, byte[] value)
	{
		this.nbt.setByteArray(key, value);
	}

	public void setString(String key, String value)
	{
		this.nbt.setString(key, value);
	}

	public void setTagList(String key, NBTTagList value)
	{
		this.nbt.setTag(key, value);
	}

	public void setTagCompound(String key, NBTTagCompound value)
	{
		this.nbt.setTag(key, value);
	}

	public void setIntArray(String key, int[] value)
	{
		this.nbt.setIntArray(key, value);
	}

	public static class ForEntity extends NBTUtils
	{
		public ForEntity(Entity target)
		{
			super(target.getEntityData() == null ? new NBTTagCompound() : target.getEntityData());
		}
	}

	public static class ForItemStack extends NBTUtils
	{
		private ItemStack stack;

		public ForItemStack(ItemStack target)
		{
			super(target.hasTagCompound() ? target.getTagCompound() : new NBTTagCompound());
			stack = target;
		}

		/*
		 * return false if target has a tag compound. return true and set an empty tag
		 * compound if target doesn't have a tag;
		 */
		public boolean setEmptyNBT()
		{
			if (stack.hasTagCompound())
				return false;
			this.stack.setTagCompound(new NBTTagCompound());
			return true;
		}
	}
}
