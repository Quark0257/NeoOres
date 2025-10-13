package neo_ores.entity.ai;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.Vec3I;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityAIMeleeNoPath<T extends EntityCreature & IArrangeBlocks & IAttackNoPath> extends EntityAIAttackMelee
{
	protected final T attackOwner;
	protected final boolean arrangeBlock;
	
	public EntityAIMeleeNoPath(T creature, double speedIn, boolean arrangeBlock)
	{
		super(creature, speedIn, true);
		this.attackOwner = creature;
		this.arrangeBlock = arrangeBlock;
	}

	protected void checkAndPerformAttack(EntityLivingBase target, double distanceSq)
	{
		double d0 = this.getAttackReachSqr(target);

		if (this.attackTick <= 0)
		{
			if (this.attacker.getNavigator().noPath())
			{
				if (this.arrangeBlock && ForgeEventFactory.getMobGriefingEvent(target.world, target)) 
				{
					Vec3I attackerPos = new Vec3I(this.attacker.getPosition());
					Vec3I targetPos = new Vec3I(target.getPosition());
					Vec3I path = targetPos.subtract(attackerPos);
					Vec3I normalized = path.normalized();
					int len = path.dotProduct(normalized);
					List<BlockPos> posList = new ArrayList<>();
					for (int i = 0; i <= len; i++) 
					{
						if (Math.max(2 * d0, 4) >= i * i) 
						{
							BlockPos targetBlock = normalized.scalarMultiply(i).add(attackerPos).getPos();
							posList.add(targetBlock);
						}	
					}
					
					if (!posList.isEmpty()) 
					{
						this.attackOwner.arrangeBlocks(posList, normalized.getY() > 0);
						this.attackTick = 20;
					}
				}
				else 
				{
					this.attackOwner.attackNoPath(target);
					this.attackTick = 20;
				}
			}
			else if (distanceSq <= d0)
			{
				this.attackTick = 20;
				this.attacker.swingArm(EnumHand.MAIN_HAND);
				this.attacker.attackEntityAsMob(target);
			}
		}
	}
}
