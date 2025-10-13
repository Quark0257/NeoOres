package neo_ores.world.gen.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import neo_ores.api.MathUtils;
import neo_ores.world.gen.structures.StructurePiece.Type;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLLog;

public class RecursiveGenerator
{

	public static final int range = 255;
	public final List<StructurePieceAndOption> list;
	public final List<StructureBoundingBox> bblist;
	public int chestCount;
	public StructureBoundingBox maxBox;

	public RecursiveGenerator()
	{
		this.list = new ArrayList<StructurePieceAndOption>();
		this.bblist = new ArrayList<StructureBoundingBox>();
		this.chestCount = 0;
		this.maxBox = null;
	}

	/*
	public synchronized void getRecursiveList(StructurePiece startPiece, BlockPos initialPos, Rotation rot, @Nullable BlockPos usedPort, Random rand, int limit)
	{
		if (startPiece == null)
			return;
		if (this.maxBox == null)
			this.maxBox = new StructureBoundingBox(initialPos.getX() - range, initialPos.getY() - range, initialPos.getZ() - range, initialPos.getX() + range, initialPos.getY() + range,
					initialPos.getZ() + range);
		if (initialPos.getY() < 0 || 256 <= initialPos.getY() + startPiece.size.getY())
			return;
		if (startPiece.hasChest)
			this.chestCount++;
		StructureBoundingBox sbb = new StructureBoundingBox(initialPos, startPiece.size, rot);
		if (sbb.maxY > 255 || sbb.minY < 0)
			return;
		this.list.add(new StructurePieceAndOption(startPiece, initialPos, rot, usedPort, false));
		this.bblist.add(sbb);
		if (limit == 0)
			return;
		for (Entry<Type, Map<EnumFacing, List<BlockPos>>> facePoss : startPiece.getPorts().entrySet())
		{
			for (Entry<EnumFacing, List<BlockPos>> poss : facePoss.getValue().entrySet())
			{
				for (BlockPos pos : poss.getValue())
				{
					if (usedPort != null)
					{
						if (usedPort.equals(pos))
							continue;
					}
					if (startPiece.getNextConnectable(facePoss.getKey()) == null)
						return;
					List<SPOsAndWeight> spoaws0 = StructurePieceUtils.getConnectablePieces(pos, facePoss.getKey(), poss.getKey(), startPiece.getNextConnectable(facePoss.getKey()));
					List<SPOsAndWeight> spoaws = new ArrayList<SPOsAndWeight>();
					for (SPOsAndWeight spoaw : spoaws0)
					{
						if (spoaw == null)
							continue;
						List<StructurePieceAndOption> spaos = new ArrayList<StructurePieceAndOption>();
						loop0: for (StructurePieceAndOption spao : spoaw.list)
						{
							if (spao == null)
							{
								spoaws.add(spoaw);
								break;
							}
							for (StructureBoundingBox bb : bblist)
							{
								StructureBoundingBox sbb_part = new StructureBoundingBox(initialPos.add(MathUtils.rot(spao.relativePos, rot)), spao.piece.size, rot.add(spao.rotation));
								if (!bb.doesExclude(sbb_part))
									continue loop0;
								if (!this.maxBox.doesIncludeAll(sbb_part))
									continue loop0;
							}
							spaos.add(spao);
						}
						if (!spaos.isEmpty())
							spoaws.add(new SPOsAndWeight(spaos, spoaw.weight));
					}
					if (spoaws.isEmpty())
						continue;
					int weights = 0;
					for (SPOsAndWeight spoaw : spoaws)
					{
						weights += spoaw.weight;
					}
					int w = rand.nextInt(weights);
					weights = 0;
					SPOsAndWeight nextSpoaw = null;
					for (SPOsAndWeight spoaw : spoaws)
					{
						nextSpoaw = spoaw;
						weights += spoaw.weight;
						if (weights > w)
							break;
					}
					if (nextSpoaw == null)
						return;
					StructurePieceAndOption spao = nextSpoaw.list.get(rand.nextInt(nextSpoaw.list.size()));
					if (spao == null)
						return;
					this.getRecursiveList(spao.piece, initialPos.add(MathUtils.rot(spao.relativePos, rot)), rot.add(spao.rotation), spao.usedPortPos, rand, limit - 1);
				}
			}
		}
	}
	*/

	public synchronized void getRecursiveList(StructurePiece startPiece, BlockPos initialPos, Rotation rot, @Nullable BlockPos usedPort, Random rand, int limit)
	{
		if (startPiece == null)
			return;
		if (this.maxBox == null)
			this.maxBox = new StructureBoundingBox(initialPos.getX() - range, initialPos.getY() - range, initialPos.getZ() - range, initialPos.getX() + range, initialPos.getY() + range,
					initialPos.getZ() + range);
		if (initialPos.getY() < 0 || 256 <= initialPos.getY() + startPiece.size.getY())
			return;
		StructureBoundingBox sbb = new StructureBoundingBox(initialPos, startPiece.size, rot);
		if (sbb.maxY > 255 || sbb.minY < 0)
			return;
		List<StructurePieceAndOption> prevList = new ArrayList<>();
		List<StructurePieceAndOption> result = new ArrayList<>();
		StructurePieceAndOption initValue = new StructurePieceAndOption(startPiece, initialPos, rot, usedPort, false);
		prevList.add(initValue);
		this.list.add(initValue);
		this.bblist.add(sbb);
		if (initValue.piece.hasChest)
		{
			this.chestCount++;
		}
		while (limit > 0)
		{
			List<StructurePieceAndOption> tempList = new ArrayList<>(prevList);
			prevList.clear();
			for (StructurePieceAndOption prevPiece : tempList)
			{
				for (Entry<Type, Map<EnumFacing, List<BlockPos>>> facePoss : prevPiece.piece.getPorts().entrySet())
				{
					for (Entry<EnumFacing, List<BlockPos>> poss : facePoss.getValue().entrySet())
					{
						for (BlockPos pos : poss.getValue())
						{
							if (usedPort != null && usedPort.equals(pos))
							{
								continue;
							}

							if (prevPiece.piece.getNextConnectable(facePoss.getKey()) == null)
							{
								FMLLog.log.error("Structure generation is failed and skipped");
								return;
							}

							List<SPOsAndWeight> rawCandidates = StructurePieceUtils.getConnectablePieces(pos, facePoss.getKey(), poss.getKey(), prevPiece.piece.getNextConnectable(facePoss.getKey()));
							List<SPOsAndWeight> filteredCandidates = new ArrayList<SPOsAndWeight>();
							for (SPOsAndWeight rawCandidate : rawCandidates)
							{
								if (rawCandidate == null)
									continue;
								List<StructurePieceAndOption> filteredPieces = new ArrayList<StructurePieceAndOption>();
								loop: for (StructurePieceAndOption candidatePiece : rawCandidate.list)
								{
									if (candidatePiece == null)
									{
										filteredCandidates.add(rawCandidate);
										break;
									}
									for (StructureBoundingBox bb : this.bblist)
									{
										StructureBoundingBox sbbPart = new StructureBoundingBox(prevPiece.relativePos.add(MathUtils.rot(candidatePiece.relativePos, prevPiece.rotation)),
												candidatePiece.piece.size, prevPiece.rotation.add(candidatePiece.rotation));
										if (!bb.doesExclude(sbbPart))
											continue loop;
										if (!this.maxBox.doesIncludeAll(sbbPart))
											continue loop;
									}
									filteredPieces.add(candidatePiece);
								}

								if (!filteredPieces.isEmpty())
								{
									filteredCandidates.add(new SPOsAndWeight(filteredPieces, rawCandidate.weight));
								}
							}

							if (filteredCandidates.isEmpty())
							{
								continue;
							}

							int weights = 0;
							for (SPOsAndWeight weight : filteredCandidates)
							{
								weights += weight.weight;
							}
							int w = rand.nextInt(weights);
							SPOsAndWeight nextWeight = filteredCandidates.get(0);
							for (SPOsAndWeight spoaw : filteredCandidates)
							{
								w -= spoaw.weight;
								if (w < 0)
								{
									nextWeight = spoaw;
									break;
								}
							}
							
							StructurePieceAndOption tempNext = nextWeight.list.get(rand.nextInt(nextWeight.list.size()));
							if (tempNext == null || tempNext.piece == null)
							{
								continue;
							}
							
							StructurePieceAndOption nextPiece = new StructurePieceAndOption(tempNext.piece, prevPiece.relativePos.add(MathUtils.rot(tempNext.relativePos, prevPiece.rotation)),
									prevPiece.rotation.add(tempNext.rotation), tempNext.usedPortPos, false);
							if (nextPiece.relativePos.getY() < 0 || 256 <= nextPiece.relativePos.getY() + nextPiece.piece.size.getY())
							{
								continue;
							}
							
							StructureBoundingBox nextSBB = new StructureBoundingBox(nextPiece.relativePos, nextPiece.piece.size, nextPiece.rotation);
							if (nextSBB.maxY > 255 || nextSBB.minY < 0)
							{
								continue;
							}

							prevList.add(nextPiece);
							this.list.add(nextPiece);
							this.bblist.add(nextSBB);
							if (nextPiece.piece.hasChest)
							{
								this.chestCount++;
							}
						}
					}
				}
			}
			result.addAll(prevList);
			limit--;
		}
	}
}
