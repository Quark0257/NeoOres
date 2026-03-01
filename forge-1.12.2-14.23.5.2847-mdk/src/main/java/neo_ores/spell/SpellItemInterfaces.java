package neo_ores.spell;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import neo_ores.api.ICompareBlockState;
import neo_ores.block.IPedestalInterfaceComponent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellItemInterfaces
{
	public static interface ICorrectingBase
	{
	}

	public static interface HasRange extends ICorrectingBase
	{
		public void setRange(int value);

		public static List<Entity> getRangedEntities(World world, int range, @Nonnull Entity target, EntityLivingBase runner, boolean collided, boolean nonCollided)
		{
			int maxEntity = 2 * range + 1;
			double rangeAdjust = range + 0.5D;
			return getRangedEntities(world, maxEntity, rangeAdjust, target, runner, collided, nonCollided);
		}

		public static List<Entity> getRangedEntities(World world, int maxEntities, double rangeAdjust, @Nonnull Entity target, EntityLivingBase runner, boolean collided, boolean nonCollided)
		{
			List<Entity> temp = new ArrayList<Entity>();
			List<Entity> results = new ArrayList<Entity>();
			AxisAlignedBB aabb = new AxisAlignedBB(target.posX - rangeAdjust, target.posY - rangeAdjust, target.posZ - rangeAdjust, target.posX + rangeAdjust, target.posY + rangeAdjust,
					target.posZ + rangeAdjust);
			for (Entity entity : world.getEntitiesWithinAABB(Entity.class, aabb))
			{
				if (entity != runner && entity.isEntityAlive())
				{
					if (collided && entity.canBeCollidedWith())
					{
						temp.add(entity);
					}
					if (nonCollided && !entity.canBeCollidedWith())
					{
						temp.add(entity);
					}
				}
			}
			if (temp.isEmpty())
			{
				temp.add(target);
			}
			List<Entity> sorted = temp.stream().sorted(new Comparator<Entity>()
			{
				@Override
				public int compare(Entity o1, Entity o2)
				{
					boolean flag = target.getPositionVector().subtract(o1.getPositionVector()).lengthSquared() < target.getPositionVector().subtract(o2.getPositionVector()).lengthSquared();
					return flag ? -1 : 1;
				}
			}).collect(Collectors.toList());

			if (maxEntities == -1)
			{
				maxEntities = sorted.size();
			}
			for (int count = 0; count < Math.min(sorted.size(), maxEntities); count++)
			{
				results.add(sorted.get(count));
			}
			return results;
		}

		public static List<BlockPos> rangedPos(BlockPos target, EnumFacing face, int range)
		{
			List<BlockPos> list = new ArrayList<BlockPos>();
			if (face == EnumFacing.DOWN || face == EnumFacing.UP)
			{
				int x = target.getX() - range;
				int z = target.getZ() - range;
				for (int i = 0; i < range * 2 + 1; i++)
				{
					for (int j = 0; j < range * 2 + 1; j++)
					{
						BlockPos pos = new BlockPos(x + i, target.getY(), z + j);
						list.add(pos);
					}
				}
			}
			else if (face == EnumFacing.WEST || face == EnumFacing.EAST)
			{
				int y = target.getY() - range;
				int z = target.getZ() - range;
				for (int i = 0; i < range * 2 + 1; i++)
				{
					for (int j = 0; j < range * 2 + 1; j++)
					{
						BlockPos pos = new BlockPos(target.getX(), y + i, z + j);
						list.add(pos);
					}
				}
			}
			else
			{
				int x = target.getX() - range;
				int y = target.getY() - range;
				for (int i = 0; i < range * 2 + 1; i++)
				{
					for (int j = 0; j < range * 2 + 1; j++)
					{
						BlockPos pos = new BlockPos(x + i, y + j, target.getZ());
						list.add(pos);
					}
				}
			}
			return list;
		}
	}

	public static interface HasSpeed extends ICorrectingBase
	{
		public void setSpeed(int value);
	}

	public static interface HasLuck extends ICorrectingBase
	{
		public void setLuck(int value);
	}

	public static interface HasHarvestLevel extends ICorrectingBase
	{
		public void setHarvestLevel(int value);
	}

	public static interface HasDamageLevel extends ICorrectingBase
	{
		public void setDamageLevel(int value);
	}

	public static interface HasChanceLiquid extends ICorrectingBase
	{
		public void setSupport();
	}

	public static interface HasSilk extends ICorrectingBase
	{
		public void setSilkTouch();
	}

	public static interface HasGather extends ICorrectingBase
	{
		public void setCanGather();
	}

	public static interface HasTier extends ICorrectingBase
	{
		public void setTier(int value);
	}

	public static interface HasNoGravity extends ICorrectingBase
	{
		public void setNoGravity();
	}

	public static interface HasNoAnyResistance extends ICorrectingBase
	{
		public void setNoAnyResistance();
	}

	public static interface HasContinuation extends ICorrectingBase
	{
		public void setContinuation(int value);
	}

	public static interface HasContinuationDown extends ICorrectingBase
	{
		public void setContinuationDown(int value);
	}

	public static interface HasCanApplyNBT extends ICorrectingBase
	{
		public void setCanApplyNBT();
	}

	public static interface HasNoInertia extends ICorrectingBase
	{
		public void setNoInertia();
	}

	public static interface HasUncollidable extends ICorrectingBase
	{
		public void setUncollidable();
	}

	public static interface HasVanished extends ICorrectingBase
	{
		public void setVanished();
	}

	public static interface HasReach extends ICorrectingBase
	{
		public void setReach(int value);
	}

	public static interface HasPlantable extends ICorrectingBase
	{
		public void setPlantable();
	}

	public static interface HasOffsetDown extends ICorrectingBase
	{
		public void setOffsetDown();
	}

	public static interface HasOffsetUp extends ICorrectingBase
	{
		public void setOffsetUp();
	}

	public static interface HasDimensionOver extends ICorrectingBase
	{
		public void setDimensionOver();
	}

	public static interface HasAmplify extends ICorrectingBase
	{
		public void setAmplify(int level);
	}

	public static interface HasDuration extends ICorrectingBase
	{
		public void setDuration(int level);
	}

	public static interface HasChain extends ICorrectingBase
	{
		public void setChain(int level);

		public static List<Entity> getChainedEntity(World world, int tier, @Nonnull Entity target, EntityLivingBase runner, boolean collided, boolean nonCollided)
		{
			double range = 3.0 + tier * 0.2;
			AxisAlignedBB aabb = new AxisAlignedBB(target.posX - range, target.posY - range, target.posZ - range, target.posX + range, target.posY + range, target.posZ + range);
			List<Entity> candidates = new ArrayList<Entity>();
			candidates.add(target);
			while (candidates.size() <= tier)
			{
				Entity lastEntity = candidates.get(candidates.size() - 1);
				Entity temp = null;
				double distSq = Double.MAX_VALUE;
				for (Entity entity : world.getEntitiesWithinAABB(Entity.class, aabb))
				{
					if (entity != runner && !candidates.contains(entity) && entity.isEntityAlive())
					{
						double tempDist = lastEntity.getPositionVector().subtract(entity.getPositionVector()).lengthSquared();
						if (collided && entity.canBeCollidedWith())
						{
							if (distSq > tempDist)
							{
								distSq = tempDist;
								temp = entity;
							}
						}
						if (nonCollided && !entity.canBeCollidedWith())
						{
							if (distSq > tempDist)
							{
								distSq = tempDist;
								temp = entity;
							}
						}
					}
				}
				if (temp != null)
				{
					candidates.add(temp);
				}
				else
				{
					break;
				}
			}
			return candidates;
		}

		public static List<BlockPos> getChainedPos(World world, int tier, BlockPos target, ICompareBlockState comparator)
		{
			List<BlockPos> candidates = new ArrayList<BlockPos>();
			List<BlockPos> tempList = new ArrayList<BlockPos>();
			IBlockState initState = world.getBlockState(target);
			BlockPos targetPos = new BlockPos(target.getX(), target.getY(), target.getZ());
			candidates.add(targetPos);
			tempList.add(targetPos);
			if (initState.getBlock() != Blocks.AIR)
			{
				List<BlockPos> offsets = getOffsets();
				int maxAmount = (2 * tier + 1) * (2 * tier + 1);
				while (!tempList.isEmpty())
				{
					List<BlockPos> lastAdded = new ArrayList<>(tempList);
					tempList.clear();
					for (BlockPos tempTarget : lastAdded)
					{
						for (BlockPos offset : offsets)
						{
							BlockPos pos = tempTarget.add(offset);
							if (!candidates.contains(pos))
							{
								if (comparator.compare(world, pos, initState, world.getBlockState(pos)))
								{
									tempList.add(pos);
									candidates.add(pos);
								}
							}
							if (candidates.size() >= maxAmount)
							{
								break;
							}
						}
					}
					if (candidates.size() >= maxAmount)
					{
						break;
					}
				}
			}
			return candidates;
		}

		public static List<BlockPos> getOffsets()
		{
			List<BlockPos> result = new ArrayList<BlockPos>();
			for (int j = -1; j <= 1; j += 2)
			{
				for (int i = 0; i < 3; i++)
				{
					int[] pos = new int[] { 0, 0, 0 };
					pos[i] = j;
					result.add(new BlockPos(pos[0], pos[1], pos[2]));
				}
			}

			for (int j = -1; j <= 1; j += 2)
			{
				for (int k = -1; k <= 1; k += 2)
				{
					for (int i = 0; i < 3; i++)
					{
						int[] pos = new int[] { j, j, j };
						pos[i] = 0;
						pos[(i + 1) > 2 ? 0 : i + 1] *= k;
						result.add(new BlockPos(pos[0], pos[1], pos[2]));
					}
				}
			}

			for (int j = -1; j <= 1; j += 2)
			{
				for (int k = -1; k <= 1; k += 2)
				{
					for (int i = -1; i <= 1; i += 2)
					{
						result.add(new BlockPos(i, j, k));
					}
				}
			}
			return result;
		}
	}

	public static interface HasSmelt extends ICorrectingBase
	{
		public void setSmelt();
	}

	public static interface HasPI extends ICorrectingBase
	{
		public void setPIMode();

		public static List<BlockPos> getPIPos(World world, BlockPos target)
		{
			List<BlockPos> candidates = new ArrayList<BlockPos>();
			List<BlockPos> tempList = new ArrayList<BlockPos>();
			IBlockState initState = world.getBlockState(target);
			BlockPos targetPos = new BlockPos(target.getX(), target.getY(), target.getZ());
			if (!ICompareBlockState.PI.compare(world, targetPos, world.getBlockState(targetPos), world.getBlockState(targetPos)))
			{
				return candidates;
			}
			candidates.add(targetPos);
			tempList.add(targetPos);
			List<BlockPos> offsets = getOffsets();
			int maxAmount = Integer.MAX_VALUE;
			while (!tempList.isEmpty())
			{
				List<BlockPos> lastAdded = new ArrayList<>(tempList);
				tempList.clear();
				for (BlockPos tempTarget : lastAdded)
				{
					for (BlockPos offset : offsets)
					{
						BlockPos pos = tempTarget.add(offset);
						if (!candidates.contains(pos))
						{
							if (!world.isAreaLoaded(pos, pos))
							{
								continue;
							}
							if (ICompareBlockState.PI.compare(world, pos, initState, world.getBlockState(pos)))
							{
								tempList.add(pos);
								candidates.add(pos);
							}
						}
						if (candidates.size() >= maxAmount)
						{
							break;
						}
					}
				}
				if (candidates.size() >= maxAmount)
				{
					break;
				}
			}
			List<BlockPos> result = new ArrayList<BlockPos>();
			for (BlockPos pos : candidates)
			{
				Block block = world.getBlockState(pos).getBlock();
				if (block instanceof IPedestalInterfaceComponent && ((IPedestalInterfaceComponent) block).isContent())
				{
					result.add(pos);
				}
			}
			return result;
		}

		public static List<BlockPos> getOffsets()
		{
			List<BlockPos> result = new ArrayList<BlockPos>();
			for (int j = -1; j <= 1; j += 2)
			{
				for (int i = 0; i < 3; i++)
				{
					int[] pos = new int[] { 0, 0, 0 };
					pos[i] = j;
					result.add(new BlockPos(pos[0], pos[1], pos[2]));
				}
			}
			return result;
		}
	}

	public static interface HasNegative extends ICorrectingBase
	{
		public void setNegative();
	}

	public static interface HasPositive extends ICorrectingBase
	{
		public void setPositive();
	}

	public static interface HasDominant extends ICorrectingBase
	{
		public void setDominant();
	}
}