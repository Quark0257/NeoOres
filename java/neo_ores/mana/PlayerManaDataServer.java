package neo_ores.mana;

import java.util.Random;

import neo_ores.mana.data.PlayerDataStorage;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerManaDataServer 
{
	public final float mxpBarCap = 1.007F;
	private final PlayerDataStorage eds;
	private final Random random = new Random();
	
	public PlayerManaDataServer(EntityPlayerMP player) 
	{
		this.eds = new PlayerDataStorage(player);
	}
	

	public void setMana(long value)
	{
		if(value > this.getMaxMana()) this.eds.setValue("mana", this.getMaxMana());
		else if(value >= 0) this.eds.setValue("mana", value);
	}
	
	public void setLevel(long value)
	{
		if(value >= 0) this.eds.setValue("level", value);
	}
	
	public void setMXP(long value)
	{
		long gotLevel = this.getLevel();
		float gotMXPBar = this.getMXPBar();
		long gotMaxMana = this.getMaxMana();
		long level = 0;
		long maxMana = 0;
		long magicpoint = 0;
		long mxp = value;
		while(mxp > (long)(16.0F + gotMXPBar))
		{
			if((16.0F + gotMXPBar) > Long.MAX_VALUE)
			{
				return;
			}
			
			mxp -= (long)(16.0F + gotMXPBar);
			level++;
			
			if((random.nextDouble() + 1.0D) / 2.0D > 1.0D / (gotLevel + level))
			{
				magicpoint++;
			}
			
			if(gotMaxMana + maxMana + ((gotLevel + level) * ((gotLevel + level) * 0.000007F) + 1.0F) > Long.MAX_VALUE)
			{
				maxMana = Long.MAX_VALUE - gotMaxMana;
			}
			else
			{
				maxMana += (long)((gotLevel + level) * ((gotLevel + level) * 0.000007F) + 1.0F);
			}
			
			if((gotLevel + level) * 270.0F - 572442.63F > Long.MAX_VALUE)
			{
				gotMXPBar = Long.MAX_VALUE;
			}
			else if(gotLevel + level > 3000)
			{
				gotMXPBar = (long)((gotLevel + level) * 270.0F - 572442.63F);
			}
			else if(gotLevel + level > 2000)
			{
				gotMXPBar = (long)((gotLevel + level) * ((gotLevel + level) * ((gotLevel + level) * 0.00001F)) - 32442.63F);
			}
			else if(gotLevel + level > 1000)
			{
				gotMXPBar = (long)((gotLevel + level) * ((gotLevel + level) * ((gotLevel + level) * ((gotLevel + level) * ((gotLevel + level) * 0.0000000000015F)))) - 442.63F);
			}
			else 
			{
				gotMXPBar *= this.mxpBarCap;
			}
		}
		
		this.setMXPBar(gotMXPBar);
		this.addLevel(level);
		this.addMagicPoint(magicpoint);
		this.addMaxMana(maxMana);
		if(level > 0)
		{
			this.setMana(this.getMaxMana());
		}
		this.eds.setValue("mxp",mxp);
	}
	
	public void setMagicPoint(long value)
	{
		if(value >= 0) eds.setValue("magicpoint", value);
	}
	
	public void setMaxMana(long value)
	{
		if(value >= 0) this.eds.setValue("maxMana", value);
	}
	
	private void setMXPBar(float f)
	{
		this.eds.setValue("mxp_bar", f);
	}
	
	private float getMXPBar()
	{
		return this.getLevel() > 0 ? this.eds.getValue("mxp_bar") : 1.007F;
	}
	
	public long getMaxMXP()
	{
		return (16.0F + this.getMXPBar()) <= Long.MAX_VALUE ? (long)(16.0F + this.getMXPBar()) : Long.MAX_VALUE;
	}
	
	public long getMana()
	{
		return this.eds.getValue("mana") < Long.MAX_VALUE ? (long)this.eds.getValue("mana") : Long.MAX_VALUE;
	}
	
	public long getLevel()
	{
		return this.eds.getValue("level") < Long.MAX_VALUE ? (long)this.eds.getValue("level") : Long.MAX_VALUE;
	}
	
	public long getMXP()
	{
		return this.eds.getValue("mxp") < Long.MAX_VALUE ? (long)this.eds.getValue("mxp") : Long.MAX_VALUE;
	}
	
	public long getMaxMana()
	{
		return this.eds.getValue("maxMana") < Long.MAX_VALUE ? (long)this.eds.getValue("maxMana") : Long.MAX_VALUE;
	}
	
	public long getMagicPoint()
	{
		return  this.eds.getValue("magicpoint") < Long.MAX_VALUE ? (long)this.eds.getValue("magicpoint") : Long.MAX_VALUE;
	}
	
	public void addMana(long value)
	{
		if(value + (float)this.getMana() > Long.MAX_VALUE) this.setMana(Long.MAX_VALUE);
		else if(value + this.getMana() >= 0) this.setMana(value + this.getMana());
		else this.setMana(0);
	}
	
	public void addLevel(long value)
	{
		if(value + this.getLevel() > Long.MAX_VALUE) this.setLevel(Long.MAX_VALUE);
		else if(value + this.getLevel() >= 0) this.setLevel(value + this.getLevel());
		else this.setLevel(0);
	}
	
	public void addMXP(long value)
	{
		if(value + this.getMXP() > Long.MAX_VALUE) this.setMXP(Long.MAX_VALUE);
		else if(value + this.getMXP() >= 0) this.setMXP(value + this.getMXP());
		else this.setMXP(0);
	}
	
	public void addMaxMana(long value)
	{
		if(value + this.getMaxMana() > Long.MAX_VALUE) this.setMaxMana(Long.MAX_VALUE);
		else if(value + this.getMaxMana() >= 0) this.setMaxMana(value + this.getMaxMana());
		else this.setMaxMana(0);
	}
	
	public void addMagicPoint(long value)
	{
		if(value + this.getMagicPoint() > Long.MAX_VALUE) this.setMagicPoint(Long.MAX_VALUE);
		else if(value + this.getMagicPoint() >= 0) this.setMagicPoint(value + this.getMagicPoint());
		else this.setMagicPoint(0);
	}
}
