package neo_ores.entity.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import com.google.common.base.Predicate;

import codechicken.lib.math.MathHelper;
import neo_ores.util.RayTraceUtils;
import net.jafama.FastMath;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAISpreadSpelling<T extends EntityCreature & ISpellingSpread> extends EntityAISpellingBase<T>
{
	protected final boolean isAttackTypeBlocks;
	protected final int count;
	protected final double affectRange;
	protected final Class<?>[] selectors;

	public EntityAISpreadSpelling(T entity, int intervalBase, int intervalRange, int spellingTime, boolean isAttackTypeBlocks, int count, double affectRange, Class<?>... entitySelector)
	{
		super(entity, intervalBase, intervalRange, spellingTime);
		this.isAttackTypeBlocks = isAttackTypeBlocks;
		this.count = count;
		this.affectRange = affectRange;
		this.selectors = entitySelector;
	}

	public EntityAISpreadSpelling(T entity, int intervalBase, int intervalRange, int spellingTime, int count, double affectRange)
	{
		this(entity, intervalBase, intervalRange, spellingTime, true, count, affectRange, new Class<?>[] {});
	}
	
	public EntityAISpreadSpelling(T entity, int intervalBase, int intervalRange, int spellingTime, int count, double affectRange, Class<?>... entitySelector)
	{
		this(entity, intervalBase, intervalRange, spellingTime, false, count, affectRange, entitySelector);
	}

	@Override
	public boolean shouldExecute()
	{
		return this.entity.allowSpread() && super.shouldExecute();
	}

	@Override
	public void runSpell()
	{
		World world = this.entity.getEntityWorld();
		double baseX = this.entity.posX;
		double baseY = this.entity.posY;
		double baseZ = this.entity.posZ;

		if (this.isAttackTypeBlocks)
		{
			for (int i = 0; i < this.count; i++)
			{
				double r = this.affectRange * world.rand.nextDouble();
				double theta = 2.0 * Math.PI * world.rand.nextDouble();
				double phi = Math.PI * world.rand.nextDouble();
				int x = MathHelper.floor(r * FastMath.cos(theta) * FastMath.sin(phi) + baseX);
				int y = MathHelper.clip(MathHelper.floor(r * FastMath.cos(theta) + baseY), 0, 255);
				int z = MathHelper.floor(r * FastMath.sin(theta) * FastMath.sin(phi) + baseZ);
				BlockPos target = new BlockPos(x, y, z);
				this.entity.getSpreadSpell().run(this.entity, RayTraceUtils.getSimpleResult(target, null));
			}
		}
		else
		{
			List<Entity> tempList = new ArrayList<>();
			for (Entity entity : world.getEntitiesInAABBexcluding(null,
					new AxisAlignedBB(baseX - this.affectRange, baseY - this.affectRange, baseZ - this.affectRange, baseX + this.affectRange, baseY + this.affectRange, baseZ + this.affectRange),
					new Predicate<Entity>()
					{

						@Override
						public boolean apply(Entity input)
						{
							for (Class<?> oclass : selectors)
							{
								if (input.getClass() == oclass)
								{
									return input.getPositionVector().subtract(entity.getPositionVector()).lengthSquared() < affectRange * affectRange;
								}
							}
							return false;
						}
					}))
			{
				tempList.add(entity);
			}

			tempList = tempList.stream().sorted(Comparator.comparingDouble(new ToDoubleFunction<Entity>()
			{

				@Override
				public double applyAsDouble(Entity input)
				{
					return input.getPositionVector().subtract(entity.getPositionVector()).lengthSquared();
				}
			})).collect(Collectors.toList());
			
			int n = Math.min(tempList.size(), this.count);
			for (int i = 0; i < n; i++) 
			{
				Entity target = tempList.get(i);
				this.entity.getSpreadSpell().run(this.entity, RayTraceUtils.getSimpleResult(target));
			}
		}
	}
}
