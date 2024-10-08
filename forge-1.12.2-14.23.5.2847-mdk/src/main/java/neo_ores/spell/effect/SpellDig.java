package neo_ores.spell.effect;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.client.particle.ParticleMagic1;
import neo_ores.event.NeoOresRegisterEvents;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasGather;
import neo_ores.spell.SpellItemInterfaces.HasHarvestLevel;
import neo_ores.spell.SpellItemInterfaces.HasLuck;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.spell.SpellItemInterfaces.HasSilk;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.SpellUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpellDig extends SpellEffect implements HasRange, HasSilk, HasLuck, HasHarvestLevel, HasGather
{
	private int range = 0;
	private int fortune = 0;
	private boolean isSilktouch = false;
	private int harvestlevel = 0;
	private boolean canGather = false;

	public void onEffectRunToSelfAndOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result != null && result.typeOfHit == Type.BLOCK && runner instanceof EntityPlayer)
		{
			ItemStack item = stack.copy();
			int xpvalue = 0;
			if (this.isSilktouch)
			{
				xpvalue = 5;
			}

			if (this.isSilktouch)
			{
				item.addEnchantment(Enchantments.SILK_TOUCH, 1);
			}
			else if (this.fortune > 0)
			{
				item.addEnchantment(Enchantments.FORTUNE, this.fortune);
			}

			EnumFacing face = EnumFacing.getFacingFromVector((float) (result.hitVec.x - runner.posX), (float) (result.hitVec.y - runner.posY - runner.getEyeHeight()),
					(float) (result.hitVec.z - runner.posZ));
			for (BlockPos pos : SpellUtils.rangedPos(result.getBlockPos(), face, this.range))
			{
				SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(1, 1, 1), NeoOresRegisterEvents.particle0, SpellUtils.getColor(stack), 8,
						runner instanceof FakePlayer);
				if (!world.isRemote)
				{
					IBlockState state = world.getBlockState(pos);
					this.breakBlock(state, world, pos, runner, xpvalue, item);
				}
			}

			Map<Enchantment, Integer> enchs = EnchantmentHelper.getEnchantments(item);
			if (enchs.containsKey(Enchantments.SILK_TOUCH))
			{
				enchs.remove(Enchantments.SILK_TOUCH);
			}
			else if (enchs.containsKey(Enchantments.FORTUNE))
			{
				enchs.remove(Enchantments.FORTUNE);
			}
			if (item.hasTagCompound())
				item.getTagCompound().removeTag("ench");
			;

			for (Map.Entry<Enchantment, Integer> entry : enchs.entrySet())
			{
				item.addEnchantment(entry.getKey(), entry.getValue());
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void breakBlock(IBlockState state, World world, BlockPos pos, EntityLivingBase runner, int silk_xp, ItemStack item)
	{
		if (state.getBlock().getHarvestLevel(state) <= this.harvestlevel || state.getBlock() instanceof IShearable)
		{
			if (state.getBlock().getBlockHardness(state, world, pos) < 0.0F)
			{
				if (this.harvestlevel == 11)
				{
					ItemStack itemS = state.getBlock().getItem(world, pos, state);
					EntityItem eitem = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, itemS);
					if (this.canGather)
					{
						eitem.setPosition(runner.posX, runner.posY, runner.posZ);
						eitem.setNoPickupDelay();
					}
					world.spawnEntity(eitem);
					if (!world.isRemote)
					{
						world.destroyBlock(pos, false);
						if (runner instanceof EntityPlayerMP)
						{
							PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
							pmds.addMXP(1L + (long) Math.pow(2, harvestlevel) + (long) Math.pow(3, fortune) + (long) silk_xp);
						}
					}
				}
			}
			else
			{
				state.getBlock().harvestBlock(world, (EntityPlayer) runner, pos, state, world.getTileEntity(pos), item);
				if (!this.isSilktouch)
				{
					int i = state.getBlock().getExpDrop(state, world, pos, this.fortune);
					if (i > 0)
					{
						EntityXPOrb exp = new EntityXPOrb(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, i);
						world.spawnEntity(exp);
					}
				}

				if (!world.isRemote)
				{
					world.destroyBlock(pos, false);
					if (this.canGather)
					{
						for (EntityItem entity : world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)))
						{
							entity.setPosition(runner.posX, runner.posY, runner.posZ);
							entity.setNoPickupDelay();
						}
					}

					if (runner instanceof EntityPlayerMP)
					{
						PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
						pmds.addMXP(1L + (long) Math.pow(2, harvestlevel) + (long) Math.pow(3, fortune) + (long) silk_xp);
					}
				}
			}
		}
	}

	public void setRange(int value)
	{
		range = value;
	}

	public void setSilkTouch()
	{
		this.isSilktouch = true;
	}

	public void setLuck(int value)
	{
		this.fortune = value;
	}

	@Override
	public void setHarvestLevel(int value)
	{
		this.harvestlevel = value;
	}

	@Override
	public void setCanGather()
	{
		this.canGather = true;
	}

	@Override
	public void onEffectRunToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
	}

	@Override
	public void onEffectRunToOther(World world, RayTraceResult result, ItemStack stack)
	{
	}

	@SideOnly(Side.CLIENT)
	private void onDisplay(World worldIn, BlockPos pos, EntityLivingBase runner)
	{
		double d1 = (double) ((float) pos.getX());
		double d2 = (double) ((float) pos.getY());
		double d3 = (double) ((float) pos.getZ());
		for (Pair<Vec3d, Vec3d> entry : SpellUtils.getPosVelOnParallelepiped(new Vec3d(d1, d2, d3), new Vec3d(1.0, 1.0, 1.0), new Vec3d(1.0, 1.0, 1.0)))
		{
			Vec3d start = entry.getKey();
			Vec3d velocity = entry.getValue();
			for (int j = 0; j < 8; j++)
			{
				int d = (int) (10.0D / (Math.random() + 0.5D));
				ParticleMagic1 png = new ParticleMagic1(worldIn, start.x, start.y, start.z, velocity.x / d, velocity.y / d, velocity.z / d, 0x80FFCE, d, 0.0005F, NeoOresRegisterEvents.particle0);
				Minecraft.getMinecraft().effectRenderer.addEffect(png);
			}
		}
	}
}
