package neo_ores.pi;

import java.util.List;

import neo_ores.api.StackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;

public class PICommand
{
	public static final String INSERT = "insert";
	public static final String EXTRACT = "extract";
	public static final String SYNC = "sync";
	protected ItemStack result = ItemStack.EMPTY;
	protected boolean executed = false;
	protected ItemStack target = ItemStack.EMPTY;
	protected String command = "";
	protected boolean pickup = false;

	protected int count = Integer.MAX_VALUE;
	protected boolean th = false;
	protected boolean transfer = false;
	protected int slot = -999;

	public PICommand()
	{
	}

	public void execute(List<IItemHandler> handlers)
	{
		if (this.command.equals(INSERT))
		{
			int min = Math.min(this.target.getCount(), this.count);
			ItemStack stack = this.target.copy();
			this.result = this.target.copy();
			this.target.setCount(min);
			this.result.setCount(this.result.getCount() - min);
			loop0: for (IItemHandler handler : handlers)
			{
				for (int i = 0; i < handler.getSlots(); i++)
				{
					if (StackUtils.compareWith(handler.getStackInSlot(i), this.target))
					{
						this.target = handler.insertItem(i, this.target, false);
						if (this.target.isEmpty())
						{
							break loop0;
						}
					}

				}
				for (int i = 0; i < handler.getSlots(); i++)
				{
					this.target = handler.insertItem(i, this.target, false);
					if (this.target.isEmpty())
					{
						break loop0;
					}
				}
			}
			
			if (!this.result.isEmpty())
			{
				this.result.setCount(this.result.getCount() + this.target.getCount());
			}
			else
			{
				this.result = stack;
				this.result.setCount(this.target.getCount());
			}

			if (this.result.isEmpty())
			{
				this.result = ItemStack.EMPTY;
			}
		}
		else if (this.command.equals(EXTRACT))
		{
			int count = this.target.getCount();
			for (IItemHandler handler : handlers)
			{
				for (int i = 0; i < handler.getSlots(); i++)
				{
					if (StackUtils.compareWith(handler.getStackInSlot(i), this.target))
					{
						ItemStack stack = handler.extractItem(i, count, false);
						if (!stack.isEmpty())
						{
							if (this.result.isEmpty())
							{
								this.result = stack.copy();
							}
							else
							{
								this.result.setCount(this.result.getCount() + stack.getCount());
							}
							count -= stack.getCount();
						}
					}
					if (count <= 0)
					{
						return;
					}
				}
			}
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setTag("targetItem", StackUtils.getNBT(this.target, false));
		compound.setString("command", this.command);
		compound.setBoolean("pickup", this.pickup);
		compound.setInteger("count", this.count);
		compound.setBoolean("transfer", this.transfer);
		compound.setInteger("slot", this.slot);
		compound.setBoolean("throw", this.th);
		return compound;
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		this.target = StackUtils.getItem(compound.getCompoundTag("targetItem"));
		this.command = compound.getString("command");
		this.pickup = compound.getBoolean("pickup");
		this.count = compound.getInteger("count");
		this.transfer = compound.getBoolean("transfer");
		this.slot = compound.getInteger("slot");
		this.th = compound.getBoolean("throw");
	}

	public NBTTagCompound syncToClient(NBTTagCompound data)
	{
		data.setTag("resultItem", StackUtils.getNBT(this.result, false));
		return data;
	}

	public void receiveFromServer(NBTTagCompound data)
	{
		if (data.hasKey("resultItem"))
		{
			this.result = StackUtils.getItem(data.getCompoundTag("resultItem"));
			this.executed = true;
		}
	}

	public boolean isExecuted()
	{
		return this.executed;
	}

	public ItemStack getResult()
	{
		return this.result;
	}

	public PICommand setTarget(ItemStack stack)
	{
		this.target = stack;
		return this;
	}

	public PICommand setCommand(String type)
	{
		this.command = type;
		return this;
	}

	public PICommand setPickup(boolean flag)
	{
		this.pickup = flag;
		return this;
	}

	public PICommand setCount(int size)
	{
		this.count = size;
		return this;
	}

	public PICommand setThrow(boolean flag)
	{
		this.th = flag;
		return this;
	}

	public PICommand setSlot(int slot)
	{
		this.slot = slot;
		return this;
	}

	public PICommand setTransfer(boolean flag)
	{
		this.transfer = flag;
		return this;
	}

	public boolean getPickup()
	{
		return this.pickup;
	}

	public int getCount()
	{
		return this.count;
	}

	public boolean getTransfer()
	{
		return this.transfer;
	}

	public boolean getThrow()
	{
		return this.th;
	}

	public int getSlot()
	{
		return this.slot;
	}
}
