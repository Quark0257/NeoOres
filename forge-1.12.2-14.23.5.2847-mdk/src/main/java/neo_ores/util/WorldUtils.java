package neo_ores.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import neo_ores.tileentity.TileEntityEnhancedPedestal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

public class WorldUtils
{
	@SuppressWarnings("unchecked")
	public static <T extends TileEntity> Map<BlockPos, T> getTileEntities(World world, List<BlockPos> poses)
	{
		Map<BlockPos, T> result = new HashMap<>();
		for (BlockPos pos : poses)
		{
			TileEntity te = world.getTileEntity(pos);
			if (te != null)
			{
				try
				{
					result.put(pos, (T) te);
				}
				catch (ClassCastException e)
				{
					FMLLog.log.error("getTileEntities has an unexpected error (a class cast error) and the position was skipped!");
				}
			}
		}
		return result;
	}

	public static List<TileEntityEnhancedPedestal> getTilePedestals(World world, List<BlockPos> poses)
	{
		List<TileEntityEnhancedPedestal> result = new ArrayList<>();
		for (BlockPos pos : poses)
		{
			TileEntityEnhancedPedestal te = new TileEntityEnhancedPedestal();
			te.setPos(pos);
			te.setWorld(world);
			te.validate();
			result.add(te);
		}
		return result;
	}

	public static <T extends TileEntity> NBTTagList getTileEntitiesData(Map<BlockPos, T> map)
	{
		NBTTagList result = new NBTTagList();
		for (BlockPos pos : map.keySet())
		{
			NBTTagCompound entryData = new NBTTagCompound();
			NBTTagCompound tileData = map.get(pos).serializeNBT();
			entryData.setIntArray("pos", new int[] { pos.getX(), pos.getY(), pos.getZ() });
			entryData.setTag("tileData", tileData);
			result.appendTag(entryData);
		}
		return result;
	}

	public static NBTTagList getNBT(List<BlockPos> poses)
	{
		NBTTagList result = new NBTTagList();
		for (BlockPos pos : poses)
		{
			NBTTagCompound entryData = new NBTTagCompound();
			entryData.setIntArray("pos", new int[] { pos.getX(), pos.getY(), pos.getZ() });
			result.appendTag(entryData);
		}
		return result;
	}

	public static List<BlockPos> getPosList(NBTTagList tagList)
	{
		List<BlockPos> result = new ArrayList<>();
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound entryData = tagList.getCompoundTagAt(i);
			int[] array = entryData.getIntArray("pos");
			BlockPos target = new BlockPos(array[0], array[1], array[2]);
			result.add(target);
		}
		return result;
	}

	public static void setTileEntitiesData(World world, NBTTagList tagList)
	{
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound entryData = tagList.getCompoundTagAt(i);
			NBTTagCompound tileData = entryData.getCompoundTag("tileData");
			int[] array = entryData.getIntArray("pos");
			BlockPos target = new BlockPos(array[0], array[1], array[2]);
			TileEntity te = world.getTileEntity(target);
			if (te != null)
			{
				te.deserializeNBT(tileData);
			}
		}
	}
}
