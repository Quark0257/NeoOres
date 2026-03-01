package neo_ores.spell.effect;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.RecipeOreStack;
import neo_ores.api.RecipeOreStackWildCard;
import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.api.spell.SpellItem;
import neo_ores.item.IPostscriptDataIntoSpell;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.NeoOresData;
import neo_ores.main.NeoOresItems;
import neo_ores.tileentity.TileEntityPedestal;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.SpellUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class SpellDiscombine extends SpellEffect
{
	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result == null)
			return;
		if (result.typeOfHit == Type.ENTITY)
		{
			Entity entity = result.entityHit;
			if (entity != null && entity instanceof EntityItem)
			{
				ItemStack targetStack = ((EntityItem) entity).getItem();
				if (!SpellUtils.containsSpell(targetStack))
				{
					return;
				}
				SpellUtils.onDisplayParticleTypeAEntity(world, entity, SpellUtils.getColor(stack), 16);
				
				Tuple<List<ItemStack>, NBTTagCompound> pair = this.getFromSpell(targetStack);
				
				for (ItemStack temp : pair.getFirst()) 
				{
					if (temp.getItem() instanceof IPostscriptDataIntoSpell) 
					{
						((IPostscriptDataIntoSpell) temp.getItem()).invertPostscript(temp, world, pair.getSecond());
					}
					this.entityItemSpawn(temp, world, entity);
				}
				
				((EntityItem) entity).setItem(ItemStack.EMPTY);
				entity.setDead();
				
				if (runner instanceof EntityPlayerMP)
				{
					PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
					pmds.addMXP(100L);
				}
			}
		}
		else if (result.typeOfHit == Type.BLOCK) 
		{
			BlockPos pos = result.getBlockPos();
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock() == NeoOresBlocks.pedestal)
			{
				SpellUtils.onDisplayParticleTypeA(world, new Vec3d(result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ()), new Vec3d(1, 1, 1),
						SpellUtils.getColor(stack), 8);
				TileEntity tileEntity = world.getTileEntity(pos);
				if (tileEntity != null && tileEntity instanceof TileEntityPedestal)
				{
					TileEntityPedestal tep = (TileEntityPedestal) tileEntity;
					ItemStack targetStack = tep.getStackInSlot(0);
					if (!SpellUtils.containsSpell(targetStack))
					{
						return;
					}
					
					Tuple<List<ItemStack>, NBTTagCompound> pair = this.getFromSpell(targetStack);
					
					for (ItemStack temp : pair.getFirst()) 
					{
						if (temp.getItem() instanceof IPostscriptDataIntoSpell) 
						{
							((IPostscriptDataIntoSpell) temp.getItem()).invertPostscript(temp, world, pair.getSecond());
						}
						EntityItem entityItem = new EntityItem(world);
						entityItem.setItem(temp);
						entityItem.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
						world.spawnEntity(entityItem);
					}
					
					tep.setContents(ItemStack.EMPTY);
					
					if (runner instanceof EntityPlayerMP)
					{
						PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
						pmds.addMXP(100L);
					}
				}
			}
		}
	}
	
	public Tuple<List<ItemStack>, NBTTagCompound> getFromSpell(ItemStack targetStack) 
	{
		NBTTagCompound additionalData = new NBTTagCompound();
		ItemStack sheet = ItemStack.EMPTY;
		if (targetStack.getTagCompound().hasKey(SpellUtils.NBTTagUtils.ADDITIONAL, 10))
		{
			additionalData = targetStack.getTagCompound().getCompoundTag(SpellUtils.NBTTagUtils.ADDITIONAL);
		}
		List<SpellItem> list = SpellUtils.getListFromItemStackNBT(targetStack.getTagCompound());
		List<RecipeOreStack> recipe = SpellUtils.getRecipeFromList(list);
		List<ItemStack> resultItems = new ArrayList<ItemStack>();
		for (RecipeOreStack ros : recipe) 
		{
			if (ros instanceof RecipeOreStackWildCard && targetStack.getItem() != NeoOresItems.spell) 
			{
				sheet = targetStack.copy();
				sheet.getTagCompound().removeTag(SpellUtils.NBTTagUtils.ADDITIONAL);
				sheet.getTagCompound().removeTag(SpellUtils.NBTTagUtils.SPELL_DESC);
				sheet.getTagCompound().removeTag(SpellUtils.NBTTagUtils.SPELL);
			}
			else 
			{
				if (!ros.getListTogether().isEmpty()) 
				{
					ItemStack value = ros.getListTogether().get(0).copy();
					// SpellRecipe doesn't use damaged item;
					// if (value.getMetadata() == 32767)
					if (!value.isEmpty()) 
					{
						int size = ros.getSize();
						int maxStack = value.isStackable() ? value.getMaxStackSize() : 1;
						int count = size / maxStack;
						int fraction = size % maxStack;
						for (int i = 0; i < count; i++) 
						{
							ItemStack temp = value.copy();
							temp.setCount(maxStack);
							resultItems.add(temp);
						}
						if (fraction != 0) 
						{
							ItemStack temp = value.copy();
							temp.setCount(fraction);
							resultItems.add(temp);
						}
					}
				}
			}
		}
		
		if (sheet.isEmpty()) 
		{
			sheet = new ItemStack(NeoOresItems.spell_sheet);
		}
		
		resultItems.add(sheet);
		return new Tuple<>(resultItems, additionalData);
	}

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		return null;
	}
	
	private void entityItemSpawn(ItemStack stack, World world, Entity target)
	{
		EntityItem entity = new EntityItem(world, target.posX, target.posY, target.posZ, stack.copy());
		entity.motionX = 0.0;
		entity.motionY = 0.0;
		entity.motionZ = 0.0;
		world.spawnEntity(entity);
	}
}
