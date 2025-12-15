package neo_ores.item;

import java.util.List;

import neo_ores.api.NBTUtils;
import neo_ores.main.Reference;
import neo_ores.util.ServerUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBiomeBottle extends INeoOresItem.Impl implements IPostscriptDataIntoSpell
{
	public ItemBiomeBottle()
	{
		this.setMaxStackSize(64);
	}

	@Override
	public NBTTagCompound postscript(ItemStack stack, World world, NBTTagCompound nbt)
	{
		if (!stack.hasTagCompound())
			return nbt;
		NBTUtils.ForItemStack nbtutils = new NBTUtils.ForItemStack(stack);
		NBTUtils writing = new NBTUtils(nbt);
		writing.setTagCompound("storedBiome", nbtutils.getCompound("storedBiome"));
		return nbt;
	}

	public boolean hasEffect(ItemStack stack)
	{
		NBTUtils.ForItemStack nbtutils = new NBTUtils.ForItemStack(stack);
		NBTTagCompound biomeTag = nbtutils.getCompound("storedBiome");
		return biomeTag.hasKey("id");
	}

	@Override
	public NBTTagList addFormattedDesc(ItemStack stack, World world, NBTTagList nbt)
	{
		NBTUtils.ForItemStack nbtutils = new NBTUtils.ForItemStack(stack);
		NBTTagCompound biomeTag = nbtutils.getCompound("storedBiome");
		if (!biomeTag.hasKey("id"))
		{
			return nbt;
		}
		Biome biome = Biome.getBiomeForId((int) biomeTag.getByte("id"));
		NBTTagList list = new NBTTagList();
		list.appendTag(new NBTTagString("tooltip.stored_biome"));
		list.appendTag(new NBTTagString(ServerUtils.getPrivateValue(Biome.class, biome, "biomeName", "field_76791_y")));
		nbt.appendTag(list);
		return nbt;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag)
	{
		super.addInformation(itemStack, world, list, flag);
		NBTUtils.ForItemStack nbtutils = new NBTUtils.ForItemStack(itemStack);
		NBTTagCompound biomeTag = nbtutils.getCompound("storedBiome");
		if (!biomeTag.hasKey("id"))
		{
			list.add(new TextComponentTranslation("tooltip.not_stored_biome").getFormattedText());
			return;
		}
		Biome biome = Biome.getBiomeForId((int) biomeTag.getByte("id"));
		list.add(new TextComponentTranslation("tooltip.stored_biome", biome.getBiomeName()).getFormattedText());
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (world.isRemote)
		{
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
		}
		RayTraceResult rayTrace = this.rayTrace(world, player, false);
		if (rayTrace == null || rayTrace.typeOfHit != RayTraceResult.Type.BLOCK)
		{
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
		}
		ItemStack stack = player.getHeldItem(hand);
		BlockPos pos = rayTrace.getBlockPos();
		if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("storedBiome"))
		{
			if (world.isBlockLoaded(pos))
			{
				ItemStack result = stack.copy();
				result.setCount(1);
				Chunk chunk = world.getChunkFromBlockCoords(pos);
				int x = pos.getX() & 15;
				int y = pos.getZ() & 15;
				byte id = chunk.getBiomeArray()[y << 4 | x];
				if (!result.hasTagCompound())
				{
					result.setTagCompound(new NBTTagCompound());
				}
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setByte("id", id);
				result.getTagCompound().setTag("storedBiome", nbt);
				if (stack.getCount() > 1)
				{
					stack.shrink(1);
					player.inventory.addItemStackToInventory(result);
				}
				else
				{
					player.setHeldItem(hand, result);
				}
				ServerUtils.playSound(player.getEntityWorld(), pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1.0F, 0.75F);
			}
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}
		NBTUtils.ForItemStack nbtutils = new NBTUtils.ForItemStack(stack);
		NBTTagCompound biomeTag = nbtutils.getCompound("storedBiome");
		if (!biomeTag.hasKey("id"))
		{
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
		}
		SpellUtils.displayParticleTypeB(world, new Vec3d(pos.getX() + 0.5D, pos.getY() + 1.1D, pos.getZ() + 0.5D), 0.5D, 5, 10, 2.0F, 5.0F, 0xFFFFFF, true);
		ItemStack result = stack.copy();
		result.setCount(1);
		result.removeSubCompound("storedBiome");
		if (result.getTagCompound().getSize() <= 0)
			result.setTagCompound(null);
		if (stack.getCount() > 1)
		{
			stack.shrink(1);
			player.inventory.addItemStackToInventory(result);
		}
		else
		{
			player.setHeldItem(hand, result);
		}
		ServerUtils.playSound(player.getEntityWorld(), pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.5F, 0.75F);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	public ModelResourceLocation getModel(Item item, int meta)
	{
		return new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "biome_bottle"), "inventory");
	}

	@Override
	public void invertPostscript(ItemStack item, World world, NBTTagCompound additionalData)
	{
		if (additionalData.hasKey("storedBiome"))
		{
			if (!item.hasTagCompound())
			{
				item.setTagCompound(new NBTTagCompound());
			}
			item.getTagCompound().setTag("storedBiome", additionalData.getCompoundTag("storedBiome"));
		}
	}
}
