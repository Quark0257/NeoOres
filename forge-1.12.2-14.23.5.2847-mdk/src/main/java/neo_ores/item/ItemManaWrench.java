package neo_ores.item;

import neo_ores.block.IWrenchUsed;
import neo_ores.tileentity.TileEntityMechanicalMagician;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemManaWrench extends INeoOresItem.Impl
{
	public ItemManaWrench()
	{
		this.setMaxStackSize(1);
	}

	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player)
	{
		return false;
	}

	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		
		if(worldIn.getBlockState(pos).getBlock() instanceof IWrenchUsed) {
			((IWrenchUsed)worldIn.getBlockState(pos).getBlock()).wrench(worldIn, pos, stack);
			return EnumActionResult.SUCCESS;
		}
		
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("nextSetPos"))
		{
			NBTTagCompound nextPos = stack.getTagCompound().getCompoundTag("nextSetPos");
			BlockPos nextSetPos = new BlockPos(nextPos.getInteger("x"), nextPos.getInteger("y"), nextPos.getInteger("z"));
			if (nextSetPos.equals(pos))
				return EnumActionResult.PASS;
			TileEntity te = worldIn.getTileEntity(nextSetPos);
			if (te instanceof TileEntityMechanicalMagician)
			{
				TileEntityMechanicalMagician temm = (TileEntityMechanicalMagician) te;
				temm.setDestination(pos);
				stack.getTagCompound().removeTag("nextSetPos");
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.PASS;
	}
}
