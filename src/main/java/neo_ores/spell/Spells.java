package neo_ores.spell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import neo_ores.items.ItemSpell;
import neo_ores.main.NeoOres;
import neo_ores.mana.PlayerManaDataServer;
import neo_ores.recipes.MCPRUtils;
import neo_ores.spell.ISpell.ISpellCorrection;
import neo_ores.spell.ISpell.ISpellEffect;
import neo_ores.spell.ISpell.ISpellFormNotEntity;
import neo_ores.spell.ISpell.ISpellFormSpellEntity;
import neo_ores.spell.SpellItemInterfaces.HasChanceLiquid;
import neo_ores.spell.SpellItemInterfaces.HasDamageLevel;
import neo_ores.spell.SpellItemInterfaces.HasGather;
import neo_ores.spell.SpellItemInterfaces.HasHarvestLevel;
import neo_ores.spell.SpellItemInterfaces.HasLuck;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.spell.SpellItemInterfaces.HasSilk;
import neo_ores.spell.SpellItemInterfaces.HasTier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class Spells 
{
	//Form
	public static class Touch implements ISpellFormNotEntity,HasChanceLiquid
	{
		private boolean liquid = false;
		
		@Override
		public void onSpellRunning(World world, EntityLivingBase runner, ItemStack stack,RayTraceResult result, NBTTagCompound spells) 
		{
			List<SpellItem> corrections = new ArrayList<SpellItem>();
			List<SpellItem> effects = new ArrayList<SpellItem>();
			
			for(SpellItem spell : SpellUtils.getListFromItemStackNBT(spells))
			{
				if(spell.getSpellClass().getSpellItemType() == SpellType.CORRECTION)
				{
					corrections.add(spell);
				}
				else if(spell.getSpellClass().getSpellItemType() == SpellType.EFFECT)
				{
					effects.add(spell);
				}
			}
			
			if(result != null && result.typeOfHit == RayTraceResult.Type.ENTITY)
			{
				for(SpellItem effect : effects)
				{
					ISpell spell = effect.getSpellClass();
					for(SpellItem correction : corrections)
					{
						correction.getSpellClass().onCorrection(spell);
					}
					spell.onEffectRun(world, runner, result);
				}
			}
			else
			{
				RayTraceResult blockresult = result;
				if(stack.getItem() instanceof ItemSpell && runner instanceof EntityPlayer)
				{
					ItemSpell item = (ItemSpell)stack.getItem();
					blockresult = item.rayTraceForPublic(world, (EntityPlayer)runner, this.liquid);
				}
				
				if(blockresult != null)
				{
					for(SpellItem effect : effects)
					{
						ISpell spell = effect.getSpellClass();
						for(SpellItem correction : corrections)
						{
							correction.getSpellClass().onCorrection(spell);
						}
						spell.onEffectRun(world, runner, blockresult);
					}
				}
			}
		}

		@Override
		public void setSupport() 
		{
			this.liquid = true;
		}

	}
	
	public static class Bullet implements ISpellFormSpellEntity
	{
		@Override
		public void onSpellRunning(World world, EntityLivingBase runner, ItemStack stack,RayTraceResult result, NBTTagCompound spells) 
		{
			//Create Entity
		}
	}
	
	//Correction
	public static class SupportLiquid implements ISpellCorrection
	{
		@Override
		public void onCorrection(ISpell spell) 
		{
			if(spell instanceof HasChanceLiquid)
			{
				((HasChanceLiquid) spell).setSupport();
			}
		}

		@Override
		public int getLevel() 
		{
			return 0;
		}
	}
	
	public static class HarvestLevel implements ISpellCorrection
	{
		private final int level;
		public HarvestLevel(int level)
		{
			this.level = level;
		}
		
		@Override
		public void onCorrection(ISpell spell) 
		{
			if(spell instanceof HasHarvestLevel)
			{
				((HasHarvestLevel) spell).setHavestLevel(this.level);
			}
		}

		@Override
		public int getLevel() 
		{
			return this.level;
		}
	}
	
	//Effect
	public static class SpellDig implements ISpellEffect,HasRange,HasSilk,HasLuck,HasHarvestLevel,HasGather
	{
		private int range = 0;
		private int fortune = 0;
		private boolean isSilktouch = false;
		private int harvestlevel = 0;
		private boolean canGather = false;
		
		public void onEffectRun(World world, EntityLivingBase runner,RayTraceResult result)
		{
			if(result != null && result.typeOfHit == Type.BLOCK && runner instanceof EntityPlayer && !world.isRemote)
			{
				ItemStack item = runner.getActiveItemStack();
				int xpvalue = 0;
				if(this.isSilktouch)
				{
					xpvalue = 5;
				}
				
				if(this.isSilktouch)
				{
					item.addEnchantment(Enchantments.SILK_TOUCH,1);
				}
				else if(this.fortune > 0)
				{
					item.addEnchantment(Enchantments.FORTUNE, this.fortune);
				}
				
				EnumFacing face = EnumFacing.getFacingFromVector((float)result.hitVec.x,(float)result.hitVec.y,(float)result.hitVec.z);
				if(face == EnumFacing.DOWN || face == EnumFacing.UP)
				{
					int x = result.getBlockPos().getX() - range;
					int z = result.getBlockPos().getZ() - range;
					for(int i = 0;i < range * 2 + 1;i++)
					{
						for(int j = 0;j < range * 2 + 1;j++)
						{
							BlockPos pos = new BlockPos(x + i,result.getBlockPos().getY(),z + j);
							IBlockState state = world.getBlockState(pos);
							this.breakBlock(state, world, pos, runner, xpvalue, item);						
						}
					}
				}
				else if(face == EnumFacing.WEST || face == EnumFacing.EAST)
				{
					int y = result.getBlockPos().getY() - range;
					int z = result.getBlockPos().getZ() - range;
					for(int i = 0;i < range * 2 + 1;i++)
					{
						for(int j = 0;j < range * 2 + 1;j++)
						{
							BlockPos pos = new BlockPos(result.getBlockPos().getX(),y + i,z + j);
							IBlockState state = world.getBlockState(pos);
							this.breakBlock(state, world, pos, runner, xpvalue, item);
						}
					}
				}
				else
				{
					int x = result.getBlockPos().getX() - range;
					int y = result.getBlockPos().getY() - range;
					for(int i = 0;i < range * 2 + 1;i++)
					{
						for(int j = 0;j < range * 2 + 1;j++)
						{
							BlockPos pos = new BlockPos(x + i,y + j,result.getBlockPos().getZ());
							IBlockState state = world.getBlockState(pos);
							this.breakBlock(state, world, pos, runner, xpvalue, item);
						}
					}
				}
				
				Map<Enchantment,Integer> enchs = EnchantmentHelper.getEnchantments(item);
				if(enchs.containsKey(Enchantments.SILK_TOUCH))
				{
					enchs.remove(Enchantments.SILK_TOUCH);
				}
				else if(enchs.containsKey(Enchantments.FORTUNE))
				{
					enchs.remove(Enchantments.FORTUNE);
				}
				item.removeSubCompound("ench");
				
				for(Map.Entry<Enchantment,Integer> entry : enchs.entrySet())
				{
					item.addEnchantment(entry.getKey(), entry.getValue());
				}
			}
			else
			{}
		}
		
		@SuppressWarnings("deprecation")
		private void breakBlock(IBlockState state,World world,BlockPos pos,EntityLivingBase runner,int silk_xp,ItemStack item)
		{
			if(state.getBlock().getHarvestLevel(state) <= this.harvestlevel || state.getBlock() instanceof IShearable)
			{
				if(state.getBlock().getBlockHardness(state, world, pos) < 0.0F)
				{
					if(this.harvestlevel == 11)
					{
						ItemStack itemS = state.getBlock().getItem(world, pos, state);
						EntityItem eitem = new EntityItem(world,pos.getX() + 0.5D,pos.getY() + 0.5D,pos.getZ() + 0.5D,itemS);
						if(this.canGather)
						{
							eitem.setPosition(runner.posX, runner.posY, runner.posZ);
							eitem.setNoPickupDelay();
						}
						world.spawnEntity(eitem);
						if(!world.isRemote)
						{
							world.destroyBlock(pos, false);
							PlayerManaDataServer pmds = new PlayerManaDataServer((EntityPlayerMP)runner);
							pmds.addMXP(1L + (long)harvestlevel + (long)fortune + (long)silk_xp);					
						}
					}
				}
				else
				{
					state.getBlock().harvestBlock(world, (EntityPlayer)runner,pos ,state,world.getTileEntity(pos),item);
					if(!world.isRemote)
					{
						world.destroyBlock(pos, false);
						if(this.canGather)
						{
							for(Entity entity : world.loadedEntityList)
							{
								if(pos.getX() < entity.posX && entity.posX < pos.getX() + 1 && pos.getY() < entity.posY && entity.posY < pos.getY() + 1 && pos.getZ() < entity.posZ && entity.posZ < pos.getZ() + 1 && entity instanceof EntityItem)
								{
									entity.setPosition(runner.posX, runner.posY, runner.posZ);
									((EntityItem)entity).setNoPickupDelay();
								}
							}
						}
						
						PlayerManaDataServer pmds = new PlayerManaDataServer((EntityPlayerMP)runner);
						pmds.addMXP(1L + (long)harvestlevel + (long)fortune + (long)silk_xp);
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
		public void setHavestLevel(int value) 
		{
			this.harvestlevel = value;
		}

		@Override
		public void setCanGather() 
		{
			this.canGather = true;	
		}
	}
	
	public static class SpellComposition implements ISpellEffect,HasTier
	{
		int tier = 0;
		
		@Override
		public void onEffectRun(World world, EntityLivingBase runner, RayTraceResult result) 
		{
			if(result != null && result.typeOfHit == Type.BLOCK && !world.isRemote)
			{
				if(world.getBlockState(result.getBlockPos()).getBlock() == NeoOres.empty_portal)
				{
					ItemStack stack = MCPRUtils.getResult(world, result.getBlockPos(), tier);
					if(!stack.isEmpty())
					{
						if(runner instanceof EntityPlayer)
						{
							((EntityPlayer)runner).addItemStackToInventory(stack.copy());
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
	}
	
	public static class SpellEarthDamage implements ISpellEffect,HasRange,HasLuck,HasHarvestLevel,HasGather,HasDamageLevel
	{
		private int damageLevel = 0;

		@Override
		public void setCanGather() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setHavestLevel(int value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setLuck(int value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setRange(int value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onEffectRun(World world, EntityLivingBase runner, RayTraceResult result) 
		{
			if(result != null && result.typeOfHit == Type.ENTITY && result.entityHit instanceof EntityLivingBase && !world.isRemote)
			{
				EntityLivingBase entity = (EntityLivingBase)result.entityHit;
				entity.attackEntityFrom(NeoOres.setDamageByEntity(NeoOres.EARTH,runner), this.damageLevel * 2 + 3);
			}
			
		}

		@Override
		public void setDamageLevel(int value) 
		{
			this.damageLevel = value;
			
		}
		
	}
}
