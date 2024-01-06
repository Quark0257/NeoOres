package neo_ores.block;

import java.util.Random;

import neo_ores.api.Structure;
import neo_ores.item.ItemBlockInstantAlter;
import neo_ores.main.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockInstantAlter extends NeoOresBlock
{
	public BlockInstantAlter()
	{
		super(Material.WOOD);
		this.setHardness(5.0F);
		this.setResistance(Float.MAX_VALUE);
		this.setSoundType(SoundType.WOOD);
		this.setHarvestLevel("axe", 0);
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
			return true;
		WorldServer server = (WorldServer) worldIn;
		Structure str = new Structure(server, new ResourceLocation(Reference.MOD_ID, "alter/alter")).setPosition(pos.add(-4, 0, -4));
		str.addComponentParts(worldIn, new Random(), str.getBoundingBox());
		return true;
	}
	
	public Item getItemBlock(Block block)
	{
		return new ItemBlockInstantAlter((BlockInstantAlter)block).setRegistryName(block.getRegistryName());
	}
}
