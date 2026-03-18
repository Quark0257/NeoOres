package neo_ores.main;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import neo_ores.api.IChunkLoader;
import neo_ores.api.IMagicContainer;
import neo_ores.api.IPlayerRunnable;
import neo_ores.api.NBTUtils;
import neo_ores.api.Structure;
import neo_ores.packet.PacketSyncConstantDataToServer;
import neo_ores.pi.PIServerData;
import neo_ores.util.NeoOresChunkManager;
import neo_ores.util.NeoOresChunkManager.ChunkPosLoading;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.PlayerMagicDataClient;
import neo_ores.util.PlayerStatusData;
import neo_ores.util.Tuple3;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NeoOresData
{
	public final MinecraftServer server;

	public static NeoOresData instance = null;
	public long time;
	private WorldServer world;
	private boolean needSaving;
	private List<ChunkPosLoading> listChunk = new ArrayList<ChunkPosLoading>();
	private Map<UUID, PlayerMagicData> mapPlayers = new HashMap<UUID, PlayerMagicData>();
	private Map<UUID, PlayerStatusData> mapPlayerStatus = new HashMap<UUID, PlayerStatusData>();
	private Map<UUID, Map<Integer, Tuple3<ItemStack, NBTTagCompound, Long>>> mapPassiveSpellList = new HashMap<UUID, Map<Integer, Tuple3<ItemStack, NBTTagCompound, Long>>>();
	private Map<UUID, Map<Integer, Tuple3<ItemStack, NBTTagCompound, Long>>> bufferPassiveSpells = new HashMap<UUID, Map<Integer, Tuple3<ItemStack, NBTTagCompound, Long>>>();
	private static final Map<UUID, PlayerMagicDataClient> mapPlayersClient = new HashMap<UUID, PlayerMagicDataClient>();
	private final Map<UUID, PIServerData> currentTargetPedestals = new HashMap<>();
	private final Map<UUID, Deque<IPlayerRunnable>> tasks = new HashMap<>();
	private final Map<UUID, List<UUID>> keys = new HashMap<>();
	private static final Map<ResourceLocation, Template> structureTemplates = new HashMap<>();

	public NeoOresData(MinecraftServer server)
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

	public Map<Integer, Tuple3<ItemStack, NBTTagCompound, Long>> getPassiveSpells(UUID uuid)
	{
		synchronized (this.mapPassiveSpellList)
		{
			if (this.mapPassiveSpellList.containsKey(uuid))
			{
				return new HashMap<Integer, Tuple3<ItemStack, NBTTagCompound, Long>>(this.mapPassiveSpellList.get(uuid));
			}
			return new HashMap<Integer, Tuple3<ItemStack, NBTTagCompound, Long>>();
		}
	}

	public void addPassiveSpell(EntityLivingBase runner, int slot, ItemStack stack, NBTTagCompound spellNbt, long mana)
	{
		synchronized (this.bufferPassiveSpells)
		{
			if (runner instanceof FakePlayer)
				return;
			UUID uuid = runner instanceof EntityPlayerMP ? EntityPlayer.getUUID(((EntityPlayerMP) runner).getGameProfile()) : runner.getUniqueID();
			if (!this.bufferPassiveSpells.containsKey(uuid))
			{
				this.bufferPassiveSpells.put(uuid, new HashMap<Integer, Tuple3<ItemStack, NBTTagCompound, Long>>());
			}
			this.bufferPassiveSpells.get(uuid).put(slot, new Tuple3<ItemStack, NBTTagCompound, Long>(stack, spellNbt, mana));
		}
	}

	public void addKey(UUID uuid, @Nonnull UUID key)
	{
		synchronized (this.keys)
		{
			if (!this.keys.containsKey(uuid))
			{
				this.keys.put(uuid, new ArrayList<>());
			}
			this.keys.get(uuid).add(key);
		}
	}

	public void removeKey(EntityPlayerMP runner, @Nonnull UUID key)
	{
		synchronized (this.keys)
		{
			if (runner instanceof FakePlayer)
				return;
			UUID uuid = EntityPlayer.getUUID(((EntityPlayerMP) runner).getGameProfile());
			if (!this.keys.containsKey(uuid))
			{
				this.keys.put(uuid, new ArrayList<>());
			}
			this.keys.get(uuid).remove(key);
		}
	}

	public boolean hasKey(EntityPlayerMP runner, UUID key)
	{
		synchronized (this.keys)
		{
			if (runner instanceof FakePlayer)
				return false;
			UUID uuid = EntityPlayer.getUUID(((EntityPlayerMP) runner).getGameProfile());
			if (this.tasks.containsKey(uuid))
			{
				return this.keys.get(uuid).contains(key);
			}
			return false;
		}
	}

	public void setCurrentTargetPedestals(EntityPlayerMP runner, int dim, EnumFacing facing, List<BlockPos> poses)
	{
		synchronized (this.currentTargetPedestals)
		{
			if (runner instanceof FakePlayer)
				return;
			UUID uuid = EntityPlayer.getUUID(((EntityPlayerMP) runner).getGameProfile());
			List<BlockPos> newList = new ArrayList<>();
			for (BlockPos pos : poses)
			{
				newList.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
			}
			if (!this.currentTargetPedestals.containsKey(uuid))
			{
				this.currentTargetPedestals.put(uuid, new PIServerData());
			}
			this.currentTargetPedestals.get(uuid).setPosList(newList);
			this.currentTargetPedestals.get(uuid).setDim(dim);
			this.currentTargetPedestals.get(uuid).setFace(facing);
			
			this.currentTargetPedestals.get(uuid).update(uuid, runner, this.server);
		}
	}

	public void addPlayerTask(EntityPlayerMP runner, @Nonnull IPlayerRunnable exe)
	{
		synchronized (this.tasks)
		{
			if (runner instanceof FakePlayer)
				return;
			UUID uuid = EntityPlayer.getUUID(((EntityPlayerMP) runner).getGameProfile());
			if (!this.tasks.containsKey(uuid))
			{
				this.tasks.put(uuid, new ArrayDeque<>());
			}
			this.tasks.get(uuid).addLast(exe);
		}
	}

	@Nullable
	public IPlayerRunnable pollTask(EntityPlayerMP runner)
	{
		synchronized (this.tasks)
		{
			if (runner instanceof FakePlayer)
				return null;
			UUID uuid = EntityPlayer.getUUID(((EntityPlayerMP) runner).getGameProfile());
			if (!this.tasks.containsKey(uuid))
			{
				return null;
			}
			if (!this.tasks.get(uuid).isEmpty())
			{
				return this.tasks.get(uuid).pollFirst();
			}
			return null;
		}
	}

	public void removeTask(UUID key, EntityPlayerMP runner)
	{
		synchronized (this.tasks)
		{
			if (runner instanceof FakePlayer)
				return;
			UUID uuid = EntityPlayer.getUUID(((EntityPlayerMP) runner).getGameProfile());
			if (this.tasks.containsKey(uuid))
			{
				this.tasks.get(uuid).removeIf(new Predicate<IPlayerRunnable>()
				{
					@Override
					public boolean test(IPlayerRunnable t)
					{
						return t.getKey().equals(key);
					}
				});
			}
		}
	}

	public PIServerData getCurrentTargetPedestals(EntityPlayerMP runner)
	{
		synchronized (this.currentTargetPedestals)
		{
			if (runner instanceof FakePlayer)
				return new PIServerData();
			UUID uuid = EntityPlayer.getUUID(((EntityPlayerMP) runner).getGameProfile());
			if (this.currentTargetPedestals.containsKey(uuid))
			{
				return this.currentTargetPedestals.get(uuid);
			}
			return new PIServerData();
		}
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
		if (this.listChunk.stream().anyMatch(new Predicate<ChunkPosLoading>()
		{
			@Override
			public boolean test(ChunkPosLoading arg0)
			{
				return arg0.keyPos == pos;
			}
		}))
			return false;
		if (!isLoadable(world, pos))
			return false;
		ChunkPosLoading chunkPos = new ChunkPosLoading(world.getChunkFromBlockCoords(pos).getPos(), world.provider.getDimension(), pos);
		this.listChunk.add(chunkPos);
		this.markDirty();
		return true;
	}

	public PlayerMagicData getPMD(EntityPlayerMP player)
	{
		if (player instanceof FakePlayer)
		{
			if (player instanceof IMagicContainer)
			{
				if (((IMagicContainer) player).getPMD() == null)
					((IMagicContainer) player).setPMD(new PlayerMagicData(true));
				return ((IMagicContainer) player).getPMD();
			}
			return new PlayerMagicData(true);
		}
		UUID uuid = EntityPlayer.getUUID(player.getGameProfile());
		if (!this.mapPlayers.containsKey(uuid))
		{
			PlayerMagicData playerData = new PlayerMagicData(false);
			playerData.readFromNBT(new NBTTagCompound());
			playerData.markDirty();
			this.mapPlayers.put(uuid, playerData);
		}
		return this.mapPlayers.get(uuid);
	}

	public PlayerStatusData getPSD(EntityPlayerMP player)
	{
		if (player instanceof FakePlayer)
		{
			return new PlayerStatusData();
		}

		UUID uuid = EntityPlayer.getUUID(player.getGameProfile());
		if (!this.mapPlayerStatus.containsKey(uuid))
		{
			this.mapPlayerStatus.put(uuid, new PlayerStatusData());
			this.mapPlayerStatus.get(uuid).markDirty();
		}
		return this.mapPlayerStatus.get(uuid);
	}

	public PlayerStatusData getPSD(UUID uuid)
	{
		if (!this.mapPlayerStatus.containsKey(uuid))
		{
			return new PlayerStatusData();
		}
		return this.mapPlayerStatus.get(uuid);
	}

	@SideOnly(Side.CLIENT)
	public static PlayerMagicDataClient getPMDC(UUID uuid)
	{
		if (!NeoOresData.mapPlayersClient.containsKey(uuid))
		{
			NeoOresData.putPMDC(uuid, new PlayerMagicDataClient());
			NeoOresData.mapPlayersClient.get(uuid).sendPacketRequest();
		}
		return NeoOresData.mapPlayersClient.get(uuid);
	}

	@SideOnly(Side.CLIENT)
	private static void putPMDC(UUID uuid, PlayerMagicDataClient pmdc)
	{
		NeoOresData.mapPlayersClient.put(uuid, pmdc);
	}
	
	@SideOnly(Side.CLIENT)
	public static void clearAllPMDC()
	{
		NeoOresData.mapPlayersClient.clear();
	}

	public void update()
	{
		synchronized (this.mapPassiveSpellList)
		{
			synchronized (this.bufferPassiveSpells)
			{
				this.mapPassiveSpellList.clear();
				for (UUID uuid : this.bufferPassiveSpells.keySet())
				{
					this.mapPassiveSpellList.put(uuid, this.bufferPassiveSpells.get(uuid));
				}
				this.bufferPassiveSpells.clear();
			}
		}

		List<ChunkPosLoading> removeList = new ArrayList<ChunkPosLoading>();
		for (ChunkPosLoading entry : this.listChunk)
		{
			if (!isLoadable(this.server.getWorld(entry.dimension), entry.keyPos))
			{
				NeoOresChunkManager.INSTANCE.unforceChunk(entry);
				removeList.add(entry);
			}
		}

		if (!removeList.isEmpty())
		{
			for (ChunkPosLoading pos : removeList)
			{
				this.listChunk.remove(pos);
			}
			this.markDirty();
		}

		if (this.needSaving)
		{
			for (ChunkPosLoading entry : this.listChunk)
			{
				NeoOresChunkManager.INSTANCE.forceChunk(this.server, entry);
			}
		}

		for (Map.Entry<UUID, PlayerMagicData> entry : this.mapPlayers.entrySet())
		{
			EntityPlayerMP player = this.server.getPlayerList().getPlayerByUUID(entry.getKey());
			if (player == null)
			{
				continue;
			}
			entry.getValue().sendToOtherSide(entry.getKey(), player);
		}
		
		synchronized (this.currentTargetPedestals) 
		{
			for (UUID key : this.currentTargetPedestals.keySet()) 
			{
				EntityPlayerMP player = this.server.getPlayerList().getPlayerByUUID(key);
				if (player == null)
				{
					continue;
				}
				this.currentTargetPedestals.get(key).update(key, player, this.server);
			}
		}
	}

	public static void onServerToStart(FMLServerAboutToStartEvent event)
	{
		instance = new NeoOresData(event.getServer());
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
			for (Map.Entry<UUID, PlayerStatusData> entry : instance.mapPlayerStatus.entrySet())
			{
				entry.getValue().setLoggedIn(false);
			}
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
			this.markDirty();
		}
		NBTTagList list = chunkData.getTagList("loadedChunks", 10);
		if (list != null)
		{
			for (int i = 0; i < list.tagCount(); i++)
			{
				NBTTagCompound chunk = list.getCompoundTagAt(i);
				BlockPos pos = new BlockPos(chunk.getInteger("posX"), chunk.getInteger("posY"), chunk.getInteger("posZ"));
				ChunkPosLoading load = new ChunkPosLoading(chunk.getInteger("chunkX"), chunk.getInteger("chunkZ"), chunk.getInteger("dimension"), pos);
				this.listChunk.add(load);
				this.markDirty();
			}
		}

		File playerDir = new File(dir + "/players");
		if (playerDir.listFiles() == null)
			return;
		for (File file : playerDir.listFiles())
		{
			if (file.getName().contains(".dat"))
			{
				UUID uuid = UUID.fromString(file.getName().split(".dat")[0]);
				NBTTagCompound playerData = NBTUtils.readFromFile(file);
				if (playerData == null)
					continue;
				PlayerMagicData player = new PlayerMagicData(false);
				player.readFromNBT(playerData);
				this.mapPlayers.put(uuid, player);
				PlayerStatusData status = new PlayerStatusData();
				status.readFromNBT(playerData);
				this.mapPlayerStatus.put(uuid, status);
			}
		}
	}

	private void save()
	{
		if (this.needSaving)
		{
			NBTTagCompound chunkData = new NBTTagCompound();
			NBTTagList list = new NBTTagList();
			for (ChunkPosLoading entry : this.listChunk)
			{
				NBTTagCompound chunk = new NBTTagCompound();
				chunk.setInteger("posX", entry.keyPos.getX());
				chunk.setInteger("posY", entry.keyPos.getY());
				chunk.setInteger("posZ", entry.keyPos.getZ());
				chunk.setInteger("chunkX", entry.posX);
				chunk.setInteger("chunkZ", entry.posZ);
				chunk.setInteger("dimension", entry.dimension);
				list.appendTag(chunk);
			}
			chunkData.setTag("loadedChunks", list);
			NBTUtils.writeToFileSafe(new File(instance.server.getWorld(0).getSaveHandler().getWorldDirectory(), "data/neo_ores/chunks.dat"), chunkData);

			this.needSaving = false;
		}

		for (Map.Entry<UUID, PlayerMagicData> entry : this.mapPlayers.entrySet())
		{
			NBTTagCompound playerData = entry.getValue().writeToNBT(new NBTTagCompound());
			PlayerStatusData psd = this.mapPlayerStatus.getOrDefault(entry.getKey(), new PlayerStatusData());
			psd.writeToNBT(playerData);
			if (entry.getValue().isDirty() || psd.isDirty())
			{
				NBTUtils.writeToFileSafe(new File(instance.server.getWorld(0).getSaveHandler().getWorldDirectory(), "data/neo_ores/players/" + entry.getKey().toString() + ".dat"), playerData);
			}
		}
	}
	
	@Nullable
	public NBTTagCompound getConstantValue(NBTTagCompound compound) 
	{
		if (compound.hasKey("neo_ores_structure")) 
		{
			NBTTagCompound result = new NBTTagCompound();
			NBTTagCompound structure = compound.getCompoundTag("neo_ores_structure").copy();
			NBTTagCompound nbt = new Structure(this.server.getWorld(0), new ResourceLocation(structure.getString("domain"), structure.getString("path"))).getTemplate().writeToNBT(new NBTTagCompound());
			structure.setTag("template", nbt);
			result.setTag("neo_ores_structure", structure);
			return result;
		}
		return null;
	}
	
	public static NBTTagCompound getStructureMessage(ResourceLocation location)
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("domain", location.getResourceDomain());
		compound.setString("path", location.getResourcePath());
		NBTTagCompound result = new NBTTagCompound();
		result.setTag("neo_ores_structure", compound);
		return result;
	}
	
	public static void setConstantValue(NBTTagCompound compound) 
	{
		if (compound.hasKey("neo_ores_structure")) 
		{
			NBTTagCompound structure = compound.getCompoundTag("neo_ores_structure");
			Template template = new Template();
			template.read(structure.getCompoundTag("template"));
			ResourceLocation key = new ResourceLocation(structure.getString("domain"), structure.getString("path"));
			structureTemplates.put(key, template);
		}
	}
	
	@Nullable
	public static Template getStructureTemplate(ResourceLocation location) 
	{
		return structureTemplates.get(location);
	}
	
	public static void setStructure(ResourceLocation location) 
	{
		if (!structureTemplates.containsKey(location)) 
		{
			structureTemplates.put(location, null);
		}
	}
	
	public static void syncStructures() 
	{
		for (ResourceLocation location : structureTemplates.keySet()) 
		{
			if (structureTemplates.get(location) == null) 
			{
				NeoOres.PACKET.sendToServer(new PacketSyncConstantDataToServer(getStructureMessage(location)));
				structureTemplates.put(location, new Template());
			}
		}
	}
	
	public static void resetStructures() 
	{
		for (ResourceLocation location : structureTemplates.keySet()) 
		{
			structureTemplates.put(location, null);
		}
	}
	
	public static int guidePage = 0;
	public static String guideIndexSearch = "";
	public static String guideQuestSearch = "";
}
