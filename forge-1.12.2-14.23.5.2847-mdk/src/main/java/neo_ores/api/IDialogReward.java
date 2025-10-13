package neo_ores.api;

import net.minecraft.entity.player.EntityPlayer;

public interface IDialogReward
{
	public String getDesc();
	
	public void takeRewardClient(EntityPlayer player);
	
	public default Object[] getFormats() 
	{
		return new Object[] {};
	}
}
