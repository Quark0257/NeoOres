package neo_ores.spell.effect;

import neo_ores.api.InventoryUtils;
import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.spell.SpellItemInterfaces.HasCollidableFilter;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class SpellPullItem extends SpellEffect implements HasCollidableFilter,HasRange
{
	private int range = 0;
	private boolean collidableFilter = false;
	
	@Override
	public void onEffectRunToSelf(World world, EntityLivingBase runner, ItemStack stack) {
		
	}

	@Override
	public void onEffectRunToOther(World world, RayTraceResult result, ItemStack stack) {
	}

	@Override
	public void onEffectRunToSelfAndOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack) 
	{
		if(result == null) return;
		if(!(runner instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer)runner;
		if(result.typeOfHit == Type.BLOCK)
		{
			EnumFacing face = EnumFacing.getFacingFromVector((float)(result.hitVec.x - runner.posX),(float)(result.hitVec.y - runner.posY - runner.getEyeHeight()),(float)(result.hitVec.z - runner.posZ));
			for(BlockPos pos : SpellUtils.rangedPos(result.getBlockPos(), face, this.range))
			{
				TileEntity te = world.getTileEntity(pos);
				if(te != null && te instanceof IInventory)
				{
					IInventory inventory = (IInventory)te;
					int size = inventory.getSizeInventory();
					for(int i = 0;i < size;i++)
					{
						//TODO set Filter
						if(!inventory.getStackInSlot(i).isEmpty() && InventoryUtils.addInventoryfromInventorySlot(i, inventory, player.inventory)) 
						{
							break;
						}
					}
				}
			}
		}
		else
		{
			Entity entity = result.entityHit;
			if(entity == null) return;
			
			if(range > 0)
			{
				int range0 = range * 2;
				for(Entity elb : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(entity.posX - range0,entity.posY - range0,entity.posZ - range0,entity.posX + range0,entity.posY + range0,entity.posZ + range0)))
				{
					if(elb != entity && elb != runner)
					{
						this.entityFor(elb, player,world,stack);
					}
				}
			}
			
			this.entityFor(entity, player,world,stack);
		}
	}

	private void entityFor(Entity entity,EntityPlayer player, World world, ItemStack stack)
	{
		if(this.collidableFilter) return;
		if(entity instanceof EntityItem)
		{
			EntityItem entityitem = (EntityItem)entity;
			ItemStack target = entityitem.getItem();
			//TODO set Filter
			ItemStack result = InventoryUtils.addInventoryfromStack(target, player.inventory);
			if(!target.isEmpty() && result.getCount() != target.getCount())
			{
				entityitem.setItem(result);
				if(entityitem.getItem().isEmpty()) entityitem.setDead();
			}
		}
	}

	@Override
	public void setCollidableFilter() 
	{
		this.collidableFilter = true;
	}

	@Override
	public void setRange(int value) {
		this.range = value;
	}
}
