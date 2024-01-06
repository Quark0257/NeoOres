package neo_ores.world.gen.structures.fire;

import java.util.Random;

import neo_ores.world.gen.structures.StructureMobSpawnerUtils;
import neo_ores.world.gen.structures.StructurePieceAndOption;
import neo_ores.world.gen.structures.StructurePieceComponent;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.loot.LootTableList;

public class FireStructurePieceComponent extends StructurePieceComponent
{
	public FireStructurePieceComponent()
	{
	}

	public FireStructurePieceComponent(WorldServer world, StructurePieceAndOption spao)
	{
		super(world, spao, "fire/");
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
			worldIn.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), 2);
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityMobSpawner)
            {
                ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().setEntityId(StructureMobSpawnerUtils.getMobId(rand));
            }
		}
		else if (function.startsWith("Boss"))
		{
			worldIn.setBlockToAir(pos);
		}
	}

}
