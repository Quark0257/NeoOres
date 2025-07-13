package neo_ores.spell.effect;

import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasDimensionOver;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.SpellUtils;
import neo_ores.world.dimension.DestinationTeleporter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;

public class SpellTranslocate extends SpellEffectEntityBase implements HasDimensionOver
{
	private boolean allowDimension = false;
	
	@Override
	protected void onEffect(World world, Entity elb, EntityLivingBase runner, ItemStack stack)
	{
		if (this.isFakePlayer(elb)) 
		{
			return;
		}
		
		if (elb instanceof EntityLivingBase) 
		{
			EntityLivingBase living = (EntityLivingBase)elb;
			if (living.isPotionActive(NeoOres.antienderteleport) && living.getActivePotionEffect(NeoOres.antienderteleport).getAmplifier() >= 2)
			{
				return;
			}
		}
		
		if (stack.getTagCompound().hasKey(SpellUtils.NBTTagUtils.ADDITIONAL, 10) && stack.getTagCompound().getCompoundTag(SpellUtils.NBTTagUtils.ADDITIONAL).hasKey("storedPosition", 10))
		{
			NBTTagCompound tag = stack.getTagCompound().getCompoundTag(SpellUtils.NBTTagUtils.ADDITIONAL).getCompoundTag("storedPosition");
			if (!tag.hasKey("pos") || !tag.hasKey("side") || !tag.hasKey("dim"))
			{
				return;
			}
			int[] posArray = tag.getIntArray("pos");
			EnumFacing pushFace = EnumFacing.getFront(tag.getInteger("side"));
			BlockPos pushPos = new BlockPos(posArray[0], posArray[1], posArray[2]);
			int dim = tag.getInteger("dim");
			int currentDim = world.provider.getDimension();
			if (dim == currentDim) 
			{
				if (elb.isRiding())
		        {
					elb.dismountRidingEntity();
		        }
				
				SpellUtils.onDisplayParticleTypeAEntity(world, elb, SpellUtils.getColor(stack), 16);
				
				if (world.getBlockState(pushPos).getBlock() != Blocks.AIR)
				{
					if (pushFace == EnumFacing.DOWN)
					{
						pushPos = pushPos.add(0, -elb.height, 0);
					}
					else
					{
						pushPos = pushPos.add(pushFace.getDirectionVec());
					}
				}
				
				elb.setPositionAndUpdate(pushPos.getX() + 0.5D, pushPos.getY(), pushPos.getZ() + 0.5D);
				
				if (runner instanceof EntityPlayerMP)
				{
					PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
					pmds.addMXP(10L);
				}
			}
			else if (this.allowDimension) 
			{
				MinecraftServer server = world.getMinecraftServer();
				if (server != null) 
				{
					if (elb.isRiding())
			        {
						elb.dismountRidingEntity();
			        }
					
					SpellUtils.onDisplayParticleTypeAEntity(world, elb, SpellUtils.getColor(stack), 16);
					
					PlayerList playerList = server.getPlayerList();

					Teleporter teleporter = new DestinationTeleporter(server.getWorld(dim), pushPos, pushFace);

					if (elb instanceof EntityPlayerMP)
					{
						playerList.transferPlayerToDimension((EntityPlayerMP) elb, dim, teleporter);
					}
					else
					{
						int origin = elb.dimension;
						elb.dimension = dim;
						world.removeEntityDangerously(elb);

						elb.isDead = false;

						playerList.transferEntityToWorld(elb, origin, server.getWorld(origin), server.getWorld(dim), teleporter);
					}
					
					if (runner instanceof EntityPlayerMP)
					{
						PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
						pmds.addMXP(100L);
					}
				}
			}
		}
	}

	@Override
	public void setDimensionOver()
	{
		this.allowDimension = true;
	}
}
