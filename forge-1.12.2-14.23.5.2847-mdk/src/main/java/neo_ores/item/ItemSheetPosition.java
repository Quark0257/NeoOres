package neo_ores.item;

import java.util.List;

import neo_ores.api.NBTUtils;
import neo_ores.util.Tuple3;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSheetPosition extends INeoOresItem.Impl implements IPostscriptDataIntoSpell
{
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag)
	{
		super.addInformation(itemStack, world, list, flag);
		if (this.hasPosData(itemStack))
		{
			Tuple3<BlockPos, EnumFacing, Integer> data = this.getPosData(itemStack);
			BlockPos pos = data.getFirst();
			EnumFacing facing = data.getSecond();
			list.add(new TextComponentTranslation("tooltip.stored_position", String.valueOf(pos.getX()), String.valueOf(pos.getY()), String.valueOf(pos.getZ()), facing.toString(),
					data.getThird().toString()).getFormattedText());
		}
		else
		{
			list.add(I18n.format("tooltip.not_stored_position").trim());
		}
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (world.isRemote)
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
		ItemStack stack = player.getHeldItem(hand);
		RayTraceResult rayTrace = this.rayTrace(world, player, false);
		if (rayTrace == null || rayTrace.typeOfHit != RayTraceResult.Type.BLOCK)
		{
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
		}
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}

		NBTTagCompound storedPosition = new NBTTagCompound();
		BlockPos pos = rayTrace.getBlockPos();
		EnumFacing face = rayTrace.sideHit;
		storedPosition.setIntArray("pos", new int[] { pos.getX(), pos.getY(), pos.getZ() });
		storedPosition.setInteger("side", face.getIndex());
		storedPosition.setInteger("dim", world.provider.getDimension());

		stack.getTagCompound().setTag("storedPosition", storedPosition);

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public NBTTagCompound postscript(ItemStack stack, World world, NBTTagCompound nbt)
	{
		if (!stack.hasTagCompound())
			return nbt;
		NBTUtils.ForItemStack nbtutils = new NBTUtils.ForItemStack(stack);
		NBTUtils writing = new NBTUtils(nbt);
		writing.setTagCompound("storedPosition", nbtutils.getCompound("storedPosition"));
		return nbt;
	}

	@Override
	public NBTTagList addFormattedDesc(ItemStack stack, World world, NBTTagList nbt)
	{
		NBTUtils.ForItemStack nbtutils = new NBTUtils.ForItemStack(stack);
		NBTTagCompound tag = nbtutils.getCompound("storedPosition");
		if (!tag.hasKey("pos") || !tag.hasKey("side") || !tag.hasKey("dim"))
		{
			return nbt;
		}
		int[] pos = tag.getIntArray("pos");
		EnumFacing face = EnumFacing.getFront(tag.getInteger("side"));
		int dim = tag.getInteger("dim");
		NBTTagList list = new NBTTagList();
		list.appendTag(new NBTTagString("tooltip.stored_position"));
		list.appendTag(new NBTTagString(String.valueOf(pos[0])));
		list.appendTag(new NBTTagString(String.valueOf(pos[1])));
		list.appendTag(new NBTTagString(String.valueOf(pos[2])));
		list.appendTag(new NBTTagString(face.toString()));
		list.appendTag(new NBTTagString(String.valueOf(dim)));
		nbt.appendTag(list);
		return nbt;
	}

	public boolean hasPosData(ItemStack stack)
	{
		NBTUtils.ForItemStack nbtutils = new NBTUtils.ForItemStack(stack);
		NBTTagCompound tag = nbtutils.getCompound("storedPosition");
		return tag.hasKey("pos") && tag.hasKey("side") && tag.hasKey("dim");
	}

	public Tuple3<BlockPos, EnumFacing, Integer> getPosData(ItemStack stack)
	{
		NBTUtils.ForItemStack nbtutils = new NBTUtils.ForItemStack(stack);
		NBTTagCompound tag = nbtutils.getCompound("storedPosition");
		if (!tag.hasKey("pos") || !tag.hasKey("side") || !tag.hasKey("dim"))
		{
			return null;
		}
		int[] pos = tag.getIntArray("pos");
		EnumFacing face = EnumFacing.getFront(tag.getInteger("side"));
		int dim = tag.getInteger("dim");
		return new Tuple3<BlockPos, EnumFacing, Integer>(new BlockPos(pos[0], pos[1], pos[2]), face, dim);
	}

	@Override
	public void invertPostscript(ItemStack item, World world, NBTTagCompound additionalData)
	{
		if (additionalData.hasKey("storedPosition")) 
		{
			if (!item.hasTagCompound())
			{
				item.setTagCompound(new NBTTagCompound());
			}
			item.getTagCompound().setTag("storedPosition", additionalData.getCompoundTag("storedPosition"));
		}
	}
}
