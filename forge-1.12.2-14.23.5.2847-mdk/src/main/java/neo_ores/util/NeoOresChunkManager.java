package neo_ores.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import neo_ores.main.NeoOres;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class NeoOresChunkManager implements ForgeChunkManager.LoadingCallback
{
	public static final NeoOresChunkManager INSTANCE = new NeoOresChunkManager();
	public final Map<Integer, Ticket> mapTicket = new HashMap<Integer, Ticket>();

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world)
	{
		int dimension = world.provider.getDimension();
		for (Ticket ticket : tickets)
		{
			this.mapTicket.put(dimension, ticket);
			for (ChunkPos pos : ticket.getChunkList())
			{
				ForgeChunkManager.forceChunk(ticket, pos);
			}
		}
	}

	public Ticket getTicket(MinecraftServer server, int dimension)
	{
		Ticket ticket = this.mapTicket.get(dimension);
		if (ticket == null && DimensionManager.isDimensionRegistered(dimension))
		{
			WorldServer world = server.getWorld(dimension);
			ticket = ForgeChunkManager.requestTicket(NeoOres.instance, world, Type.NORMAL);
			if (ticket != null)
			{
				this.mapTicket.put(dimension, ticket);
			}
		}
		return ticket;
	}

	public void forceChunk(MinecraftServer server, ChunkPosLoading pos)
	{
		if (this.mapTicket.get(pos.dimension) != null && this.mapTicket.get(pos.dimension).getChunkList().contains(pos.pos))
			return;
		Ticket ticket = this.getTicket(server, pos.dimension);
		try
		{
			Objects.requireNonNull(ticket);
			ForgeChunkManager.forceChunk(ticket, pos.pos);
		}
		catch (Exception e)
		{
			System.out.println("Unknown error occured!");
		}
	}

	public void unforceChunk(ChunkPosLoading pos)
	{
		Ticket ticket = this.mapTicket.get(pos.dimension);
		if (ticket == null)
			return;
		ForgeChunkManager.unforceChunk(ticket, pos.pos);
		if (ticket.getChunkList().isEmpty())
		{
			this.mapTicket.remove(pos.dimension);
			ForgeChunkManager.releaseTicket(ticket);
		}
	}

	public static class ChunkPosLoading
	{
		public final int posX;
		public final int posZ;
		public final int dimension;
		public final ChunkPos pos;

		public ChunkPosLoading(int posX, int posZ, int dimension)
		{
			this.posX = posX;
			this.posZ = posZ;
			this.dimension = dimension;
			this.pos = new ChunkPos(posX, posZ);
		}
		
		public ChunkPosLoading(ChunkPos pos, int dimension)
		{
			this.posX = pos.x;
			this.posZ = pos.z;
			this.dimension = dimension;
			this.pos = pos;
		}
	}
}
