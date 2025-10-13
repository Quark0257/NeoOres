package neo_ores.world.gen.structures.air;

import java.util.Random;
import java.util.UUID;

import neo_ores.item.ItemBossKey;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.NeoOresItems;
import neo_ores.main.Reference;
import neo_ores.tileentity.TileEntityBossCapsule;
import neo_ores.world.gen.structures.StructureMobSpawnerUtils;
import neo_ores.world.gen.structures.StructurePieceAndOption;
import neo_ores.world.gen.structures.StructurePieceComponent;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public class AirStructurePieceComponent extends StructurePieceComponent
{
	public static final ResourceLocation AIR_TRASURE = new ResourceLocation(Reference.MOD_ID, "chests/air_trasure");
	
	public AirStructurePieceComponent()
	{
	}

	public AirStructurePieceComponent(WorldServer world, StructurePieceAndOption spao, UUID uuid)
	{
		super(world, spao, "air/", uuid);
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
				if (this.bossKey && !this.chestFlag) 
				{
					LootTable loottable = worldIn.getLootTableManager().getLootTableFromLocation(AIR_TRASURE);
					ItemStack stack = new ItemStack(NeoOresItems.boss_key, 1, 0);
					((ItemBossKey) stack.getItem()).setKey(stack, this.uuidKey); 

		            LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)worldIn);
		            lootcontext$builder.withLuck(64.0F);
		            loottable.fillInventory(((TileEntityChest) tileentity), rand, lootcontext$builder.build());
					
		            ((TileEntityChest) tileentity).setInventorySlotContents(rand.nextInt(((TileEntityChest) tileentity).getSizeInventory()), stack);
		            
					this.chestFlag = true;
				}
				else 
				{
					((TileEntityChest) tileentity).setLootTable(AIR_TRASURE, rand.nextLong());
				}
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
			worldIn.setBlockState(pos, NeoOresBlocks.boss_capsule.getDefaultState());
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof TileEntityBossCapsule) 
			{
				((TileEntityBossCapsule) tileentity).setEntity(this.uuidKey, "minecraft:elder_guardian", null);
			}
		}
	}
}
