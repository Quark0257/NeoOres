package neo_ores.util;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerStatusData
{
	private boolean login;
	private boolean hasInitialItems;
	private boolean isDirty;
	private boolean isSneak;
	
	public PlayerStatusData() 
	{
		this.login = false;
		this.hasInitialItems = false;
		this.isDirty = false;
		this.isSneak = false;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.login = nbt.getBoolean("loggedIn");
		this.hasInitialItems = nbt.getBoolean("hasInitialItems");
		this.isSneak = nbt.getBoolean("isSneak");
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setBoolean("loggedIn", this.login);
		nbt.setBoolean("hasInitialItems", this.hasInitialItems);
		nbt.setBoolean("isSneak", this.isSneak);
	}
	
	public boolean isLoggedIn() 
	{
		return this.login;
	}
	
	public boolean hasInitialItems() 
	{
		return this.hasInitialItems;
	}
	
	public void setLoggedIn(boolean value) 
	{
		this.login = value;
		this.isDirty = true;
	}
	
	public void setInitialItems(boolean value) 
	{
		this.hasInitialItems = value;
		this.isDirty = true;
	}
	
	public boolean isSneak() 
	{
		return this.isSneak;
	}
	
	public void setSneak(boolean value) 
	{
		this.isSneak = value;
		this.isDirty = true;
	}
	
	public void markDirty() 
	{
		this.isDirty = true;
	}
	
	public boolean isDirty() 
	{
		return this.isDirty;
	}
}
