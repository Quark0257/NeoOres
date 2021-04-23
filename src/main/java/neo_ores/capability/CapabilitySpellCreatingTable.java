package neo_ores.capability;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

import neo_ores.main.NeoOres;
import neo_ores.spell.SpellItem;
import neo_ores.spell.SpellUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilitySpellCreatingTable //implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> 
{
	/*
    private List<SpellItemManager> activeSpells = new ArrayList<SpellItemManager>();
    
    public void update(TileEntity tileentity) 
    {
    	
    }

    public List<SpellItemManager> getKeyList() 
    {
        return activeSpells;
    }

    public void setActivateSpell(SpellItemManager spell) 
    {
    	if(activeSpells.contains(spell)) return;
        this.activeSpells.add(spell);
    }

    public boolean hasSelectedSpellKey() 
    {
        return !this.activeSpells.isEmpty();
    }

	@Override
	public NBTTagCompound serializeNBT() 
	{
		NBTTagCompound nbt = new NBTTagCompound();

        if(this.hasSelectedSpellKey()) 
        {
        	SpellUtils utils = new SpellUtils();
        	List<Integer> ids = new ArrayList<Integer>();
        	for (SpellItemManager spell : this.activeSpells) 
        	{
        		if(utils.getID(spell) != -1)
        		{
        			ids.add(utils.getID(spell));
        		}
            }
        	
        	nbt.setIntArray("activeSpell", ArrayUtils.toPrimitive(ids.toArray(new Integer[] {})));
        }
        
        return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) 
	{
		if(nbt.hasKey("activeSpells"))
		{
			int[] id_list = nbt.getIntArray("activeSpells");
			for (int id : id_list) 
        	{
				SpellUtils utils = new SpellUtils();
				if(utils.getFromID(id) != null)
				{
					this.activeSpells.add(utils.getFromID(id));
				}
            }
		}
	}
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) 
	{
		return capability == NeoOres.SPELL_CAP;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) 
	{
		if(capability == NeoOres.SPELL_CAP)
		{
			return (T)this;
		}
		return null;
	}
	*/
}
