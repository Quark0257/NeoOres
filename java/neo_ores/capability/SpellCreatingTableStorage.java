package neo_ores.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class SpellCreatingTableStorage // <T extends CapabilitySpellCreatingTable> implements Capability.IStorage<T> 
{
	/*
	@Override
	public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) 
	{
		return instance.serializeNBT();
	}

	@Override
	public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) 
	{
		instance.deserializeNBT((NBTTagCompound) nbt);
	}
	*/
}
