package neo_ores.item;

import neo_ores.api.NBTUtils;
import neo_ores.main.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemMobBottle extends INeoOresItem.Impl implements IPostscriptDataIntoSpell
{
	private boolean isMaster;

	public ItemMobBottle(boolean isMaster)
	{
		this.isMaster = isMaster;
		this.setMaxStackSize(1);
	}

	/**
	 * required runner life equals summoning entity life. it means when spell runs,
	 * player is damaged as much as summoning entity life.
	 */
	@Override
	public NBTTagCompound postscript(ItemStack stack, World world, NBTTagCompound nbt)
	{
		if (!stack.hasTagCompound())
			return nbt;
		NBTUtils.ForItemStack nbtutils = new NBTUtils.ForItemStack(stack);
		NBTUtils writing = new NBTUtils(nbt);
		writing.setTagCompound("storedEntity", nbtutils.getCompound("storedEntity"));
		return nbt;
	}

	public boolean hasEffect(ItemStack stack)
	{
		NBTUtils.ForItemStack nbtutils = new NBTUtils.ForItemStack(stack);
		NBTTagCompound entityTag = nbtutils.getCompound("storedEntity");
		return entityTag.hasKey("id");
	}

	@Override
	public NBTTagList addFormattedDesc(ItemStack stack, World world, NBTTagList nbt)
	{
		NBTUtils.ForItemStack nbtutils = new NBTUtils.ForItemStack(stack);
		NBTTagCompound entityTag = nbtutils.getCompound("storedEntity");
		Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(entityTag.getString("id")), world);
		if (entity == null || !(entity instanceof EntityLivingBase))
			return nbt;
		EntityLivingBase entityliving = (EntityLivingBase) entity;
		NBTTagList list = new NBTTagList();
		list.appendTag(new NBTTagString("spell.required.health"));
		list.appendTag(new NBTTagString(Integer.toString((int) entityliving.getMaxHealth())));
		nbt.appendTag(list);
		return nbt;
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (world.isRemote)
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
		ItemStack stack = player.getHeldItem(hand);
		if (!stack.hasTagCompound())
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
		NBTUtils.ForItemStack nbtutils = new NBTUtils.ForItemStack(stack);
		NBTTagCompound entityTag = nbtutils.getCompound("storedEntity");
		Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(entityTag.getString("id")), world);
		if (entity == null)
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
		entity.deserializeNBT(entityTag.getCompoundTag("tag"));
		RayTraceResult result = this.rayTrace(world, player, false);
		if (result == null || result.typeOfHit != RayTraceResult.Type.BLOCK)
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		BlockPos entitySpawn = result.getBlockPos();
		if (result.sideHit == EnumFacing.DOWN)
		{
			entitySpawn = entitySpawn.add(0, -entity.height, 0);
		}
		else if (result.sideHit == EnumFacing.EAST)
		{
			entitySpawn = entitySpawn.add(1, 0, 0);
		}
		else if (result.sideHit == EnumFacing.WEST)
		{
			entitySpawn = entitySpawn.add(-1, 0, 0);
		}
		else if (result.sideHit == EnumFacing.NORTH)
		{
			entitySpawn = entitySpawn.add(0, 0, -1);
		}
		else if (result.sideHit == EnumFacing.SOUTH)
		{
			entitySpawn = entitySpawn.add(0, 0, 1);
		}
		else
		{
			entitySpawn = entitySpawn.add(0, 1, 0);
		}
		entity.setPositionAndRotation(entitySpawn.getX() + 0.5, entitySpawn.getY(), entitySpawn.getZ() + 0.5, entity.rotationYaw, entity.rotationPitch);
		world.spawnEntity(entity);
		stack.removeSubCompound("storedEntity");
		world.playSound(player, entitySpawn.getX() + 0.5, entitySpawn.getY(), entitySpawn.getZ() + 0.5, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1.0F, 0.75F);
		if (stack.getTagCompound().getSize() <= 0)
			stack.setTagCompound(null);
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
	}

	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand)
	{
		player.swingArm(hand);
		if (!player.isServerWorld())
			return false;
		if (target instanceof EntityPlayer)
			return false;
		if (!this.isMaster && !target.isNonBoss())
		{
			return false;
		}
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		NBTUtils.ForItemStack nbtutils = new NBTUtils.ForItemStack(stack);
		if (nbtutils.getCompound("storedEntity") != null && !nbtutils.getCompound("storedEntity").equals(new NBTTagCompound()))
			return false;
		NBTTagCompound entity = new NBTTagCompound();
		entity.setTag("tag", target.writeToNBT(new NBTTagCompound()));
		entity.setString("id", EntityList.getKey(target).toString());
		nbtutils.setTagCompound("storedEntity", entity);

		if (player.capabilities.isCreativeMode)
		{
			ItemStack copied = stack.copy();
			copied.setTagCompound(nbtutils.get());
			player.setHeldItem(hand, copied);
		}
		target.setDead();

		return true;
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	public ModelResourceLocation getModel(Item item, int meta)
	{
		return new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "mob_bottle"), "inventory");
	}
}
