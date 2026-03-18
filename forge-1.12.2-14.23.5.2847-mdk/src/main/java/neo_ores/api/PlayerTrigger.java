package neo_ores.api;

import java.util.List;

import javax.annotation.Nullable;

import neo_ores.main.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistryEntry;

@ObjectHolder(Reference.MOD_ID)
public class PlayerTrigger extends IForgeRegistryEntry.Impl<PlayerTrigger>
{
	private final String translateKey;
	private final Class<? extends IPlayerRunnable> runnable;
	private final List<IDialogReward> rewards;

	/**
	 * 
	 * @param translateKey
	 * @param runnable @Nullable If it is null, this needs to force to trigger 
	 * @param rewards @Nullable If it is null, this doesn't behave as a quest
	 * @param registerId
	 */
	public PlayerTrigger(String translateKey, @Nullable Class<? extends IPlayerRunnable> runnable, @Nullable List<IDialogReward> rewards, ResourceLocation registerId)
	{
		this.translateKey = translateKey;
		this.runnable = runnable;
		this.rewards = rewards;
		this.setRegistryName(registerId);
	}

	public String getTranslateKey()
	{
		return this.translateKey;
	}
	
	public String getUnlocalizedName()
	{
		return "trigger." + this.translateKey + ".name";
	}
	
	public String getDesc()
	{
		return "trigger." + this.translateKey + ".desc";
	}
	
	/**
	 * whether this behaves as a quest
	 * @return
	 */
	public boolean hasDialogRewards() 
	{
		return this.rewards != null;
	}
	
	public List<IDialogReward> getRewards() 
	{
		return this.rewards;
	}

	public void trigger(EntityPlayer player)
	{
		if (this.runnable == null)
		{
			return;
		}
		try
		{
			IPlayerRunnable runnable = this.runnable.newInstance();
			runnable.run(player);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean canTrigger(EntityPlayer player)
	{
		if (this.runnable == null)
		{
			return false;
		}
		try
		{
			IPlayerRunnable runnable = this.runnable.newInstance();
			return runnable.isRunnable(player);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public String getId()
	{
		ResourceLocation location = this.getRegistryName();
		return location.getResourceDomain() + "@" + location.getResourcePath();
	}
}
