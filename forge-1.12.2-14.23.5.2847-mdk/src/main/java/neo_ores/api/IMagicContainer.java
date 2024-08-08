package neo_ores.api;

import neo_ores.util.PlayerMagicData;

public interface IMagicContainer
{	
	public void setPMD(PlayerMagicData data);
	
	/**
	 * DO NOT use this!
	 * Use NeoOresData.instance.getPMD(EntityPlayerMP player)
	 */
	public PlayerMagicData getPMD();
}
