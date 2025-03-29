package neo_ores.spell;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
			List<Entity> temp = new ArrayList<Entity>();
			List<Entity> results = new ArrayList<Entity>();
			AxisAlignedBB aabb = new AxisAlignedBB(target.posX - rangeAdjust, target.posY - rangeAdjust, target.posZ - rangeAdjust, target.posX + rangeAdjust, target.posY + rangeAdjust,
					target.posZ + rangeAdjust);
			for (Entity entity : world.getEntitiesWithinAABB(Entity.class, aabb))
			{
				if (entity != runner)
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
			List<Entity> sorted = temp.stream().sorted(new Comparator<Entity>()
			{
				@Override
				public int compare(Entity o1, Entity o2)
				{
					boolean flag = target.getPositionVector().subtract(o1.getPositionVector()).lengthSquared() < target.getPositionVector().subtract(o2.getPositionVector()).lengthSquared();
					return flag ? -1 : 1;
				}
			}).collect(Collectors.toList());

			for (int count = 0; count < Math.min(sorted.size(), maxEntity); count++)
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

	public static interface HasHealLevel extends ICorrectingBase
	{
		public void setHealLevel(int value);
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
}