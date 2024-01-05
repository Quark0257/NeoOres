package neo_ores.world.gen.structures.water;

import java.util.Random;

import neo_ores.world.gen.structures.StructurePieceAndOption;
import neo_ores.world.gen.structures.StructurePieceComponent;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.loot.LootTableList;

public class WaterStructurePieceComponent extends StructurePieceComponent
{
	public WaterStructurePieceComponent()
	{
	}

	public WaterStructurePieceComponent(WorldServer world, StructurePieceAndOption spao)
	{
		super(world, spao, "water/");
	}

	@Override
	protected void handleDataMarker(String function, BlockPos pos, World worldIn, Random rand, StructureBoundingBox sbb)
	{
		if (function.startsWith("Chest"))
		{
			worldIn.setBlockState(pos, Blocks.CHEST.getDefaultState());
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityChest)
			{
				((TileEntityChest) tileentity).setLootTable(LootTableList.CHESTS_END_CITY_TREASURE, rand.nextLong());
			}
		}
		else if (function.startsWith("Spawner"))
		{
			BlockPos blockpos = pos.down();
		}
		else if (function.startsWith("Boss"))
		{
			BlockPos blockpos = pos.down();
		}
	}
}
