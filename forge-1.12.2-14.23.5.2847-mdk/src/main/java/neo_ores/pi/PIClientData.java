package neo_ores.pi;

import neo_ores.api.StackUtils;
import neo_ores.main.NeoOres;
import neo_ores.packet.PacketPISyncToServer;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

public class PIClientData
{
	private PICommand currentCommand = null;
	private IPIListener pi;

	public PIClientData()
	{
		this.pi = null;
	}

	private boolean sendToServer()
	{
		if (this.currentCommand != null)
		{
			try
			{
				NBTTagCompound data = new NBTTagCompound();
				data = this.currentCommand.writeToNBT(data);
				NeoOres.PACKET.sendToServer(new PacketPISyncToServer(data));
			}
			catch (IllegalArgumentException e)
			{
				this.currentCommand = null;
				return false;
			}
		}
		return true;
	}

	// receivePacket
	public void readFromNBT(NBTTagCompound packetData)
	{
		if (this.currentCommand != null)
		{
			this.currentCommand.receiveFromServer(packetData);
			if (this.currentCommand.isExecuted())
			{
				if (this.pi != null)
				{
					this.pi.setItemList(StackUtils.convertNBTToItems(packetData));
					this.pi.setResult(this.currentCommand.result);
					this.pi.executed();
				}
				else if (!this.currentCommand.result.isEmpty())
				{
					Minecraft.getMinecraft().playerController.sendPacketDropItem(this.currentCommand.result);
				}
				this.currentCommand = null;
			}
		}
	}

	public PICommand getCurrentCommand()
	{
		return this.currentCommand;
	}

	public boolean setCommand(PICommand command)
	{
		if (this.currentCommand == null)
		{
			this.currentCommand = command;
			return this.sendToServer();
		}
		return false;
	}

	public void setNull()
	{
		if (this.currentCommand.isExecuted())
		{
			this.currentCommand = null;
		}
	}

	public void setGui(IPIListener pi)
	{
		this.pi = pi;
		if (this.pi == null)
		{
			this.currentCommand = null;
		}
	}
}
