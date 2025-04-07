package neo_ores.spell.effect;

import neo_ores.api.InventoryUtils;
import neo_ores.event.NeoOresRegisterEvents;
import neo_ores.main.NeoOres;
import neo_ores.packet.PacketLineParticleToClient;
import neo_ores.spell.SpellItemInterfaces.HasDimensionOver;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.spell.SpellItemInterfaces.HasReach;
import neo_ores.util.RayTraceUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class SpellPipeItem extends SpellEffectItemFiltered implements HasReach, HasDimensionOver
{
	private int reachValue = 0;
	private boolean dimensionOver = false;

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (result == null)
			return;
		if (!(runner instanceof EntityPlayer))
			return;
		if (stack.getTagCompound().hasKey(SpellUtils.NBTTagUtils.ADDITIONAL, 10) && stack.getTagCompound().getCompoundTag(SpellUtils.NBTTagUtils.ADDITIONAL).hasKey("storedPosition", 10))
		{
			NBTTagCompound tag = stack.getTagCompound().getCompoundTag(SpellUtils.NBTTagUtils.ADDITIONAL).getCompoundTag("storedPosition");
			if (!tag.hasKey("pos") || !tag.hasKey("side") || !tag.hasKey("dim"))
			{
				return;
			}
			double reachDist = 5.0 * (this.reachValue + 1);
			int[] posArray = tag.getIntArray("pos");
			EnumFacing pushFace = EnumFacing.getFront(tag.getInteger("side"));
			int dim = tag.getInteger("dim");
			BlockPos pushPos = new BlockPos(posArray[0], posArray[1], posArray[2]);
			Vec3d pushVec = new Vec3d(posArray[0] + 0.5, posArray[1] + 0.5, posArray[2] + 0.5);
			Vec3d runnerVec = new Vec3d(runner.posX, runner.posY, runner.posZ);
			World pushWorld = world;
			if (dim != world.provider.getDimension())
			{
				if (!this.dimensionOver)
				{
					return;
				}
				else
				{
					MinecraftServer server = world.getMinecraftServer();
					if (server != null)
					{
						pushWorld = server.getWorld(dim);
					}
					else
					{
						return;
					}
				}
			}
			if (!this.dimensionOver && pushVec.subtract(runnerVec).lengthSquared() > reachDist * reachDist)
			{
				return;
			}
			if (!pushWorld.isAreaLoaded(pushPos, pushPos))
			{
				return;
			}
			TileEntity pushTe = pushWorld.getTileEntity(pushPos);
			if (pushTe != null && pushTe instanceof IInventory)
			{
				// push to target
				IInventory pushInv = (IInventory) pushTe;
				if (result.typeOfHit == Type.BLOCK)
				{
					EnumFacing face = result.sideHit;
					for (BlockPos pos : HasRange.rangedPos(result.getBlockPos(), face, this.range))
					{
						if (dim == world.provider.getDimension() && pos.equals(pushPos)) 
						{
							continue;
						}
						SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(1, 1, 1), NeoOresRegisterEvents.particle0, SpellUtils.getColor(stack), 8);
						TileEntity te = world.getTileEntity(pos);
						if (te != null && te instanceof IInventory)
						{
							IInventory inventory = (IInventory) te;
							int size = inventory.getSizeInventory();
							for (int i = 0; i < size; i++)
							{
								if (!inventory.getStackInSlot(i).isEmpty() && this.match(inventory.getStackInSlot(i), stack))
								{
									if (InventoryUtils.addInventoryfromInventorySlot(i, inventory, pushInv, face, pushFace))
									{
										if (dim == world.provider.getDimension())
										{
											int color = SpellUtils.getColor(stack);
											Vec3d start = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
											Vec3d vel = pushVec.subtract(start);
											NeoOres.PACKET.sendToAll(new PacketLineParticleToClient(start, vel, color, dim));
										}
										break;
									}
								}
							}
						}
					}
				}
				else
				{
					Entity entity = result.entityHit;
					if (entity == null)
						return;
					for (Entity temp : HasRange.getRangedEntities(world, this.range, entity, runner, false, true))
					{
						Vec3d start = new Vec3d(temp.posX, temp.posY, temp.posZ);
						Vec3d vel = pushVec.subtract(start);
						if (this.entityFor(temp, pushInv, world, stack, pushFace))
						{
							if (dim == world.provider.getDimension())
							{
								int color = SpellUtils.getColor(stack);
								NeoOres.PACKET.sendToAll(new PacketLineParticleToClient(start, vel, color, dim));
							}
						}
					}
				}
			}
			else if (pushWorld.getBlockState(pushPos).getBlock() == Blocks.AIR)
			{
				// spawn in air
				double x = pushPos.getX() + 0.5;
				double y = pushPos.getY() + 0.5;
				double z = pushPos.getZ() + 0.5;
				if (result.typeOfHit == Type.BLOCK)
				{
					EnumFacing face = result.sideHit;
					for (BlockPos pos : HasRange.rangedPos(result.getBlockPos(), face, this.range))
					{
						SpellUtils.onDisplayParticleTypeA(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(1, 1, 1), NeoOresRegisterEvents.particle0, SpellUtils.getColor(stack), 8);
						TileEntity te = world.getTileEntity(pos);
						if (te != null && te instanceof IInventory)
						{
							IInventory inventory = (IInventory) te;
							int size = inventory.getSizeInventory();
							for (int i = 0; i < size; i++)
							{
								if (!inventory.getStackInSlot(i).isEmpty() && this.match(inventory.getStackInSlot(i), stack))
								{
									this.entityItemSpawn(inventory.getStackInSlot(i), pushWorld, x, y, z);
									inventory.getStackInSlot(i).setCount(0);
									if (dim == world.provider.getDimension())
									{
										int color = SpellUtils.getColor(stack);
										Vec3d start = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
										Vec3d vel = pushVec.subtract(start);
										NeoOres.PACKET.sendToAll(new PacketLineParticleToClient(start, vel, color, dim));
									}
									break;
								}
							}
						}
					}
				}
				else
				{
					Entity entity = result.entityHit;
					if (entity == null)
						return;
					for (Entity temp : HasRange.getRangedEntities(world, this.range, entity, runner, false, true))
					{
						Vec3d start = new Vec3d(temp.posX, temp.posY, temp.posZ);
						Vec3d vel = pushVec.subtract(start);
						if (this.entityForSpawn(temp, world, pushWorld, x, y, z, stack))
						{
							if (dim == world.provider.getDimension())
							{
								int color = SpellUtils.getColor(stack);
								NeoOres.PACKET.sendToAll(new PacketLineParticleToClient(start, vel, color, dim));
							}
						}
					}
				}
			}
		}
	}

	private boolean entityFor(Entity entity, IInventory dist, World world, ItemStack stack, EnumFacing face)
	{
		if (entity instanceof EntityItem)
		{
			EntityItem entityitem = (EntityItem) entity;
			ItemStack target = entityitem.getItem();
			if (!this.match(target, stack))
			{
				return false;
			}
			SpellUtils.onDisplayParticleTypeAEntity(world, entityitem, NeoOresRegisterEvents.particle0, SpellUtils.getColor(stack), 16);
			ItemStack result = InventoryUtils.addInventoryfromStack(target, dist, face);
			if (!target.isEmpty() && result.getCount() != target.getCount())
			{
				entityitem.setItem(result);
				if (entityitem.getItem().isEmpty())
					entityitem.setDead();
				return true;
			}
		}
		return false;
	}

	private boolean entityForSpawn(Entity entity, World world, World pushWorld, double x, double y, double z, ItemStack stack)
	{
		if (entity instanceof EntityItem)
		{
			EntityItem entityitem = (EntityItem) entity;
			ItemStack target = entityitem.getItem();
			if (!this.match(target, stack))
			{
				return false;
			}
			SpellUtils.onDisplayParticleTypeAEntity(world, entityitem, NeoOresRegisterEvents.particle0, SpellUtils.getColor(stack), 16);
			if (!target.isEmpty())
			{
				this.entityItemSpawn(target.copy(), pushWorld, x, y, z);
				entityitem.setItem(ItemStack.EMPTY);
				entityitem.setDead();
				return true;
			}
		}
		return false;
	}

	private void entityItemSpawn(ItemStack stack, World world, double x, double y, double z)
	{
		EntityItem entity = new EntityItem(world, x, y, z, stack.copy());
		entity.motionX = 0.0;
		entity.motionY = 0.0;
		entity.motionZ = 0.0;
		world.spawnEntity(entity);
	}

	@Override
	public void setReach(int value)
	{
		this.reachValue = value;
	}

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		return RayTraceUtils.getSimpleResult(runner);
	}

	@Override
	public void setDimensionOver()
	{
		this.dimensionOver = true;
	}
}
