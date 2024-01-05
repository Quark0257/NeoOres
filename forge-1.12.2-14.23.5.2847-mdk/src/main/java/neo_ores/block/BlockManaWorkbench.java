package neo_ores.block;

import java.util.Random;

import neo_ores.inventory.ContainerManaWorkbench;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class BlockManaWorkbench extends NeoOresBlock
{
	public BlockManaWorkbench()
	{
		super(Material.WOOD);
		this.setHardness(2.5F);
		this.setSoundType(SoundType.WOOD);
		this.setHarvestLevel("axe", 0);
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(NeoOresBlocks.mana_workbench);
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(NeoOres.instance, NeoOres.guiIDManaWorkbench, worldIn, pos.getX(), pos.getY(), pos.getZ());
			playerIn.displayGui(new ThisGui(worldIn, pos));
		}

		return true;
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(NeoOresBlocks.mana_workbench);
	}

	public static class ThisGui implements IInteractionObject
	{
		private final World world;
		private final BlockPos position;

		public ThisGui(World worldIn, BlockPos pos)
		{
			this.world = worldIn;
			this.position = pos;
		}

		public String getName()
		{
			return "mana_workbench";
		}

		public boolean hasCustomName()
		{
			return false;
		}

		public ITextComponent getDisplayName()
		{
			return new TextComponentTranslation(NeoOresBlocks.mana_workbench.getUnlocalizedName() + ".name", new Object[0]);
		}

		public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
		{
			return new ContainerManaWorkbench(playerInventory, this.world, this.position, playerIn);
		}

		public String getGuiID()
		{
			return "neo_ores:mana_workbench";
		}
	}
}
