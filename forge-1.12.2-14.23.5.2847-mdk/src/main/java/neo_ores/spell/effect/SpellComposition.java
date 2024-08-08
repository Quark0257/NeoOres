package neo_ores.spell.effect;

import neo_ores.api.recipe.MCPRUtils;
import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.client.particle.ParticleNoGravity;
import neo_ores.event.NeoOresRegisterEvents;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.NeoOresData;
import neo_ores.main.NeoOresItems;
import neo_ores.spell.SpellItemInterfaces.HasTier;
import neo_ores.tileentity.TileEntityPedestal;
import neo_ores.util.PlayerMagicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpellComposition extends SpellEffect implements HasTier
{
	int tier = 0;

	@Override
	public void onEffectRunToSelfAndOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result != null && result.typeOfHit == Type.BLOCK)
		{
			if (world.getBlockState(result.getBlockPos()).getBlock() == NeoOresBlocks.enhanced_pedestal)
			{
				if (world.isRemote)
				{
					this.onDisplay(world, result.getBlockPos(), runner);
					return;
				}
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
					if (world.isRemote)
					{
						this.onDisplay(world, result.getBlockPos(), runner);
						return;
					}
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
							pmds.addMXP(Integer.MAX_VALUE / 4);
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
							pmds.addMXP(Integer.MAX_VALUE / 16384);
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
							pmds.addMXP(Integer.MAX_VALUE);
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
							pmds.addMXP(Integer.MAX_VALUE / 64);
						}
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private void onDisplay(World worldIn, BlockPos pos, EntityLivingBase runner)
	{
		for (int i = 0; i < 12; ++i)
		{
			double d1 = (double) ((float) pos.getX());
			double d2 = (double) ((float) pos.getY());
			double d3 = (double) ((float) pos.getZ());
			Vec3d velocity = new Vec3d(0, 0, 0);
			Vec3d start = new Vec3d(0, 0, 0);

			switch (i)
			{
				case 0:
				{
					start = new Vec3d(d1, d2, d3);
					velocity = new Vec3d(1.0, 0.0D, 0.0D);
					break;
				}
				case 1:
				{
					start = new Vec3d(d1, d2, d3);
					velocity = new Vec3d(0.0D, 0.0D, 1.0);
					break;
				}
				case 2:
				{
					start = new Vec3d(d1, d2, d3 + 1.0);
					velocity = new Vec3d(0.0D, 0.8125D, 0.0D);
					break;
				}
				case 3:
				{
					start = new Vec3d(d1, d2 + 0.8125D, d3);
					velocity = new Vec3d(0.0D, -0.8125D, 0.0D);
					break;
				}
				case 4:
				{
					start = new Vec3d(d1, d2 + 0.8125D, d3 + 1.0);
					velocity = new Vec3d(0.0D, 0.0D, -1.0);
					break;
				}
				case 5:
				{
					start = new Vec3d(d1, d2 + 0.8125D, d3 + 1.0);
					velocity = new Vec3d(1.0D, 0.0D, 0.0D);
					break;
				}
				case 6:
				{
					start = new Vec3d(d1 + 1.0, d2, d3);
					velocity = new Vec3d(0.0D, 0.8125D, 0.0D);
					break;
				}
				case 7:
				{
					start = new Vec3d(d1 + 1.0, d2, d3 + 1.0);
					velocity = new Vec3d(0.0D, 0.0D, -1.0D);
					break;
				}
				case 8:
				{
					start = new Vec3d(d1 + 1.0, d2, d3 + 1.0);
					velocity = new Vec3d(-1.0D, 0.0D, 0.0D);
					break;
				}
				case 9:
				{
					start = new Vec3d(d1 + 1.0, d2 + 0.8125D, d3);
					velocity = new Vec3d(0.0D, 0.0D, 1.0D);
					break;
				}
				case 10:
				{
					start = new Vec3d(d1 + 1.0, d2 + 0.8125D, d3);
					velocity = new Vec3d(-1.0D, 0.0D, 0.0D);
					break;
				}
				case 11:
				{
					start = new Vec3d(d1 + 1.0, d2 + 0.8125D, d3 + 1.0);
					velocity = new Vec3d(0.0D, -0.8125D, 0.0D);
					break;
				}
			}
			for (int j = 0; j < 4; j++)
			{
				int d = (int) (5.0D / (Math.random() * 0.6D + 0.4D));
				ParticleNoGravity png = new ParticleNoGravity(worldIn, start.x, start.y, start.z, velocity.x / d, velocity.y / d, velocity.z / d, 0xFFFFFF, d, 0.02F,
						this.getTexture(worldIn.rand.nextInt(4)));
				Minecraft.getMinecraft().effectRenderer.addEffect(png);
			}
		}
	}

	private TextureAtlasSprite getTexture(int meta)
	{
		if (meta == 0)
			return NeoOresRegisterEvents.air0;
		else if (meta == 1)
			return NeoOresRegisterEvents.earth0;
		else if (meta == 2)
			return NeoOresRegisterEvents.fire0;
		return NeoOresRegisterEvents.water0;
	}

	@Override
	public void setTier(int value)
	{
		tier = value;
	}

	@Override
	public void onEffectRunToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
	}

	@Override
	public void onEffectRunToOther(World world, RayTraceResult result, ItemStack stack)
	{
	}
}
