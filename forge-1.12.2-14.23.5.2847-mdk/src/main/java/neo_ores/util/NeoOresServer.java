package neo_ores.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import neo_ores.api.IChunkLoader;
import neo_ores.api.NBTUtils;
import neo_ores.util.NeoOresChunkManager.ChunkPosLoading;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

public class NeoOresServer
{
	public final MinecraftServer server;

	public static NeoOresServer instance = null;
	public long time;
	private WorldServer world;
	private boolean needSaving;
	private Map<BlockPos, ChunkPosLoading> mapChunk = new HashMap<BlockPos, ChunkPosLoading>();

	public NeoOresServer(MinecraftServer server)
	{
		this.server = server;
		this.time = 0L;
		this.world = null;
		this.needSaving = false;
	}

	public static boolean isLoaded()
	{
		return instance != null;
	}

	public static boolean isLoadable(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		if (state == null)
			return false;
		if (state.getBlock() instanceof IChunkLoader)
		{
			if (((IChunkLoader) state.getBlock()).isLoadable())
				return true;
		}
		TileEntity te = world.getTileEntity(pos);
		if (te == null)
			return false;
		if (te instanceof IChunkLoader)
		{
			if (((IChunkLoader) te).isLoadable())
				return true;
		}
		return false;
	}

	public boolean addLoadingChunk(World world, BlockPos pos)
	{
		if (this.mapChunk.containsKey(pos))
			return false;
		if (!isLoadable(world, pos))
			return false;
		ChunkPosLoading chunkPos = new ChunkPosLoading(world.getChunkFromBlockCoords(pos).getPos(), world.provider.getDimension());
		this.mapChunk.put(pos, chunkPos);
		this.markDirty();
		return true;
	}

	public void update()
	{
		List<BlockPos> removeList = new ArrayList<BlockPos>();
		for (Map.Entry<BlockPos, ChunkPosLoading> entry : this.mapChunk.entrySet())
		{
			if (!isLoadable(this.server.getWorld(entry.getValue().dimension), entry.getKey()))
			{
				NeoOresChunkManager.INSTANCE.unforceChunk(entry.getValue());
				removeList.add(entry.getKey());
			}
		}

		if (!removeList.isEmpty())
		{
			for (BlockPos pos : removeList)
			{
				this.mapChunk.remove(pos);
			}
			this.markDirty();
		}

		if (this.needSaving)
		{
			for (Map.Entry<BlockPos, ChunkPosLoading> entry : this.mapChunk.entrySet())
			{
				NeoOresChunkManager.INSTANCE.forceChunk(this.server, entry.getValue());
			}
			this.save();
		}
	}

	public static void onServerToStart(FMLServerAboutToStartEvent event)
	{
		instance = new NeoOresServer(event.getServer());
	}

	public static void onServerStarted(FMLServerStartedEvent event)
	{
		instance.world = instance.server.getWorld(0);
		instance.time = instance.world.getTotalWorldTime();
		instance.load();
	}

	public static void onServerStopping(FMLServerStoppingEvent event)
	{
		if (isLoaded())
		{
			instance.save();
			instance = null;
		}
	}

	public static void onWorldSaved(WorldEvent.Save event)
	{
		if (isLoaded())
		{
			instance.save();
		}
	}

	public void markDirty()
	{
		this.needSaving = true;
	}

	private void load()
	{
		File dir = new File(instance.server.getWorld(0).getSaveHandler().getWorldDirectory(), "data/neo_ores/");
		NBTTagCompound chunkData = NBTUtils.readFromFile(new File(dir, "chunks.dat"));
		if (chunkData == null)
		{
			chunkData = new NBTTagCompound();
		}
		NBTTagList list = chunkData.getTagList("loadedChunks", 10);
		if (list != null)
		{
			for (int i = 0; i < list.tagCount(); i++)
			{
				NBTTagCompound chunk = list.getCompoundTagAt(i);
				BlockPos pos = new BlockPos(chunk.getInteger("posX"), chunk.getInteger("posY"), chunk.getInteger("posZ"));
				ChunkPosLoading load = new ChunkPosLoading(chunk.getInteger("chunkX"), chunk.getInteger("chunkZ"), chunk.getInteger("dimension"));
				this.mapChunk.put(pos, load);
			}
		}
	}

	private void save()
	{
		if (this.needSaving)
		{
			NBTTagCompound chunkData = new NBTTagCompound();
			NBTTagList list = new NBTTagList();
			for (Map.Entry<BlockPos, ChunkPosLoading> entry : this.mapChunk.entrySet())
			{
				NBTTagCompound chunk = new NBTTagCompound();
				chunk.setInteger("posX", entry.getKey().getX());
				chunk.setInteger("posY", entry.getKey().getY());
				chunk.setInteger("posZ", entry.getKey().getZ());
				chunk.setInteger("chunkX", entry.getValue().posX);
				chunk.setInteger("chunkZ", entry.getValue().posZ);
				chunk.setInteger("dimension", entry.getValue().dimension);
				list.appendTag(chunk);
				chunkData.setTag("loadedChunks", list);
			}
			NBTUtils.writeToFileSafe(new File(instance.server.getWorld(0).getSaveHandler().getWorldDirectory(), "data/neo_ores/chunks.dat"), chunkData);
			this.needSaving = false;
		}
	}
}
