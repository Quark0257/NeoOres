package neo_ores.spell.effect;

import java.util.Map;

import neo_ores.api.ICompareBlockState;
import neo_ores.api.InventoryUtils;
import neo_ores.main.NeoOresData;
import neo_ores.spell.SpellItemInterfaces.HasChain;
import neo_ores.spell.SpellItemInterfaces.HasGather;
import neo_ores.spell.SpellItemInterfaces.HasHarvestLevel;
import neo_ores.spell.SpellItemInterfaces.HasLuck;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.spell.SpellItemInterfaces.HasSilk;
import neo_ores.spell.SpellItemInterfaces.HasSmelt;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.ForgeEventFactory;

public class SpellDig extends SpellEffectItemFiltered implements HasSilk, HasLuck, HasHarvestLevel, HasGather, HasSmelt
{
	private int fortune = 0;
	private boolean isSilktouch = false;
	private int harvestlevel = 0;
	private boolean canGather = false;
	private boolean smelting = false;

	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result != null && result.typeOfHit == Type.BLOCK)
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

			EnumFacing face = result.sideHit;
			for (BlockPos pos : this.rangeMode ? HasRange.rangedPos(result.getBlockPos(), face, this.range) : HasChain.getChainedPos(world, this.chain, result.getBlockPos(), ICompareBlockState.ITEM))
			{
				SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(1, 1, 1), SpellUtils.getColor(stack), 8);
				if (!world.isRemote)
				{
					IBlockState state = world.getBlockState(pos);
					@SuppressWarnings("deprecation")
					ItemStack itemS = state.getBlock().getItem(world, pos, state);
					if (!this.match(itemS, stack))
					{
						continue;
					}
					if (runner instanceof EntityPlayer)
					{
						this.breakBlock(state, world, pos, runner, xpvalue, item);
					}
					else
					{
						this.breakBlockByMob(state, world, pos, runner);
					}
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
			int xp = 0;
			if (state.getBlock().getBlockHardness(state, world, pos) < 0.0F)
			{
				if (this.harvestlevel == 11)
				{
					ItemStack itemS = state.getBlock().getItem(world, pos, state);
					EntityItem eitem = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, itemS.copy());
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
					
					ItemStack target = eitem.getItem();
					if (this.smelting)
					{
						ItemStack copied = target.copy();
						copied.setCount(1);
						ItemStack smeltingResult = FurnaceRecipes.instance().getSmeltingResult(target).copy();
						if (!smeltingResult.isEmpty())
						{
							int fortuneMag = world.rand.nextInt(this.fortune + 1) + 1;
							xp += target.getCount() * FurnaceRecipes.instance().getSmeltingExperience(target);
							smeltingResult.setCount(fortuneMag * target.getCount() * smeltingResult.getCount());
							eitem.setItem(smeltingResult);
							target = eitem.getItem();
						}
					}
					if (this.canGather)
					{
						ItemStack result = InventoryUtils.addInventoryfromStack(target, InventoryUtils.getPlayerInventory((EntityPlayer) runner), EnumFacing.UP);
						if (!target.isEmpty() && result.getCount() != target.getCount())
						{
							eitem.setItem(result);
							if (eitem.getItem().isEmpty())
								eitem.setDead();
						}
					}
				}
			}
			else
			{
				state.getBlock().harvestBlock(world, (EntityPlayer) runner, pos, state, world.getTileEntity(pos), item);
				if (!this.isSilktouch)
				{
					xp += state.getBlock().getExpDrop(state, world, pos, this.fortune);
				}

				if (!world.isRemote)
				{
					world.destroyBlock(pos, false);
					for (EntityItem entity : world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)))
					{
						ItemStack target = entity.getItem();
						if (this.smelting)
						{
							ItemStack copied = target.copy();
							copied.setCount(1);
							ItemStack smeltingResult = FurnaceRecipes.instance().getSmeltingResult(target).copy();
							if (!smeltingResult.isEmpty())
							{
								int fortuneMag = world.rand.nextInt(this.fortune + 1) + 1;
								xp += target.getCount() * FurnaceRecipes.instance().getSmeltingExperience(target);
								smeltingResult.setCount(fortuneMag * target.getCount() * smeltingResult.getCount());
								entity.setItem(smeltingResult);
								target = entity.getItem();
							}
						}
						if (this.canGather)
						{
							ItemStack result = InventoryUtils.addInventoryfromStack(target, InventoryUtils.getPlayerInventory((EntityPlayer) runner), EnumFacing.UP);
							if (!target.isEmpty() && result.getCount() != target.getCount())
							{
								entity.setItem(result);
								if (entity.getItem().isEmpty())
									entity.setDead();
							}
						}
					}

					if (runner instanceof EntityPlayerMP)
					{
						PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
						pmds.addMXP(1L + (long) Math.pow(2, harvestlevel) + (long) Math.pow(3, fortune) + (long) silk_xp);
					}
				}
			}
			
			if (xp > 0)
			{
				EntityXPOrb exp = new EntityXPOrb(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, xp);
				world.spawnEntity(exp);
			}
		}
	}

	private void breakBlockByMob(IBlockState state, World world, BlockPos pos, EntityLivingBase runner)
	{
		if (state.getBlock().getHarvestLevel(state) <= this.harvestlevel || state.getBlock() instanceof IShearable)
		{
			if (state.getBlock().canEntityDestroy(state, world, pos, runner) && ForgeEventFactory.onEntityDestroyBlock(runner, pos, state))
			{
				world.destroyBlock(pos, true);
			}
		}
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
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		BlockPos pos = new BlockPos(runner.posX, runner.posY, runner.posZ);
		return RayTraceUtils.getSimpleResult(pos, null);
	}

	@Override
	public void setSmelt()
	{
		this.smelting = true;
	}
}
