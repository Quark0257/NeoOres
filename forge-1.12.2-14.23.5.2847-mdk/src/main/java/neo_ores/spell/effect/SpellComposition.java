package neo_ores.spell.effect;

import neo_ores.api.recipe.MCPRUtils;
import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.NeoOresData;
import neo_ores.main.NeoOresItems;
import neo_ores.spell.SpellItemInterfaces.HasTier;
import neo_ores.tileentity.TileEntityPedestal;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class SpellComposition extends SpellEffect implements HasTier
{
	int tier = 0;

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result != null && result.typeOfHit == Type.BLOCK)
		{
			if (world.getBlockState(result.getBlockPos()).getBlock() == NeoOresBlocks.enhanced_pedestal)
			{
				SpellUtils.onDisplayParticleTypeA(world, new Vec3d(result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ()), new Vec3d(1, 0.8125, 1),
						SpellUtils.getColor(stack), 8);
				ItemStack itemstack = MCPRUtils.getResult(world, result.getBlockPos(), tier);
				if (!itemstack.isEmpty())
				{
					if (runner instanceof EntityPlayerMP)
					{
						EntityPlayer entityplayer = (EntityPlayer) runner;
						boolean flag = (runner instanceof FakePlayer) ? false : entityplayer.inventory.addItemStackToInventory(itemstack);

						if (flag)
						{
							entityplayer.world.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
									((entityplayer.getRNG().nextFloat() - entityplayer.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
							entityplayer.inventoryContainer.detectAndSendChanges();
						}

						if (flag && itemstack.isEmpty())
						{
						}
						else
						{
							EntityItem entityitem = entityplayer.dropItem(itemstack, false);

							if (entityitem != null)
							{
								entityitem.setNoPickupDelay();
								entityitem.setOwner(entityplayer.getName());
							}
						}

						PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
						pmds.addMXP(1L + (long) Math.pow(4, tier));
					}
				}
			}
			else if (world.getBlockState(result.getBlockPos()).getBlock() == NeoOresBlocks.pedestal_water)
			{
				TileEntity te = world.getTileEntity(result.getBlockPos());
				if (te instanceof TileEntityPedestal)
				{
					SpellUtils.onDisplayParticleTypeA(world, new Vec3d(result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ()), new Vec3d(1, 0.8125, 1),
							SpellUtils.getColor(stack), 8);
					TileEntityPedestal tep = (TileEntityPedestal) te;
					Item item = tep.getStackInSlot(0).getItem();
					int meta = tep.getStackInSlot(0).getMetadata();
					int size = tep.getStackInSlot(0).getCount();
					if (size != 64)
						return;
					if (7 <= tier && item == NeoOresItems.essence && meta == 3)
					{
						runner.world.playSound(null, runner.posX, runner.posY, runner.posZ, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 1.0F, 1.0F);
						tep.removeStackFromSlot(0);
						world.setBlockState(result.getBlockPos(), NeoOresBlocks.air_portal.getDefaultState());
						if (runner instanceof EntityPlayerMP)
						{
							PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
							pmds.addMXP(1L + (long) Math.pow(4, tier));
						}
					}
					if (3 <= tier && item == NeoOresItems.essence && meta == 0)
					{
						runner.world.playSound(null, runner.posX, runner.posY, runner.posZ, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 1.0F, 1.0F);
						tep.removeStackFromSlot(0);
						world.setBlockState(result.getBlockPos(), NeoOresBlocks.earth_portal.getDefaultState());
						if (runner instanceof EntityPlayerMP)
						{
							PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
							pmds.addMXP(1L + (long) Math.pow(4, tier));
						}
					}
					if (9 <= tier && item == NeoOresItems.essence && meta == 2)
					{
						runner.world.playSound(null, runner.posX, runner.posY, runner.posZ, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 1.0F, 1.0F);
						tep.removeStackFromSlot(0);
						world.setBlockState(result.getBlockPos(), NeoOresBlocks.fire_portal.getDefaultState());
						if (runner instanceof EntityPlayerMP)
						{
							PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
							pmds.addMXP(1L + (long) Math.pow(4, tier));
						}
					}
					if (5 <= tier && item == NeoOresItems.essence && meta == 1)
					{
						runner.world.playSound(null, runner.posX, runner.posY, runner.posZ, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 1.0F, 1.0F);
						tep.removeStackFromSlot(0);
						world.setBlockState(result.getBlockPos(), NeoOresBlocks.water_portal.getDefaultState());
						if (runner instanceof EntityPlayerMP)
						{
							PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
							pmds.addMXP(1L + (long) Math.pow(4, tier));
						}
					}
				}
			}
		}
	}

	@Override
	public void setTier(int value)
	{
		tier = value;
	}

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		BlockPos pos = new BlockPos(runner.posX, runner.posY, runner.posZ);
		return RayTraceUtils.getSimpleResult(pos, null);
	}
}
