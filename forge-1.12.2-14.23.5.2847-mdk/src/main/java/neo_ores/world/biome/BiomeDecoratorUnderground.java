package neo_ores.world.biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenPumpkin;

public class BiomeDecoratorUnderground extends BiomeDecorator {
	
	private final boolean isWaterAir;
	
	public BiomeDecoratorUnderground(boolean isWaterAir) {
		this.isWaterAir = isWaterAir;
	}
	
	protected void genDecorations(Biome biomeIn, World worldIn, Random random)
    {
        net.minecraft.util.math.ChunkPos forgeChunkPos = new net.minecraft.util.math.ChunkPos(chunkPos); // actual ChunkPos instead of BlockPos, used for events
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Pre(worldIn, random, forgeChunkPos));
        this.generateOres(worldIn, random);

        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SAND))
        for (int i = 0; i < this.sandPatchesPerChunk; ++i)
        {
            int j = random.nextInt(16) + 8;
            int k = random.nextInt(16) + 8;
            this.sandGen.generate(worldIn, random, worldIn.getTopSolidOrLiquidBlock(this.chunkPos.add(j, 0, k)));
        }

        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.CLAY))
        for (int i1 = 0; i1 < this.clayPerChunk; ++i1)
        {
            int l1 = random.nextInt(16) + 8;
            int i6 = random.nextInt(16) + 8;
            this.clayGen.generate(worldIn, random, worldIn.getTopSolidOrLiquidBlock(this.chunkPos.add(l1, 0, i6)));
        }

        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SAND_PASS2))
        for (int j1 = 0; j1 < this.gravelPatchesPerChunk; ++j1)
        {
            int i2 = random.nextInt(16) + 8;
            int j6 = random.nextInt(16) + 8;
            this.gravelGen.generate(worldIn, random, worldIn.getTopSolidOrLiquidBlock(this.chunkPos.add(i2, 0, j6)));
        }

        int k1 = this.treesPerChunk;

        if (random.nextFloat() < this.extraTreeChance)
        {
            ++k1;
        }

        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.TREE))
        for (int j2 = 0; j2 < k1; ++j2)
        {
            int k6 = random.nextInt(16) + 8;
            int l = random.nextInt(16) + 8;
            WorldGenAbstractTree worldgenabstracttree = biomeIn.getRandomTreeFeature(random);
            worldgenabstracttree.setDecorationDefaults();
            
            List<BlockPos> pos = this.heightList(k6, l, worldIn, biomeIn);
            if(pos.size() <= 0) continue;
            BlockPos blockpos = pos.get(random.nextInt(pos.size()));
            if (worldgenabstracttree.generate(worldIn, random, blockpos))
            {
                worldgenabstracttree.generateSaplings(worldIn, random, blockpos);
            }
            
        }

        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.BIG_SHROOM))
        for (int k2 = 0; k2 < this.bigMushroomsPerChunk; ++k2)
        {
            int l6 = random.nextInt(16) + 8;
            int k10 = random.nextInt(16) + 8;
            List<BlockPos> pos = this.heightList(l6, k10, worldIn, biomeIn);
            if(pos.size() <= 0) continue;
            BlockPos blockpos = pos.get(random.nextInt(pos.size()));
            this.bigMushroomGen.generate(worldIn, random, blockpos);
        }

        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.FLOWERS))
        for (int l2 = 0; l2 < this.flowersPerChunk; ++l2)
        {
            int i7 = random.nextInt(16) + 8;
            int l10 = random.nextInt(16) + 8;
            List<BlockPos> pos = this.heightList(i7, l10, worldIn, biomeIn);
            if(pos.size() <= 0) continue;
            BlockPos blockpos = pos.get(random.nextInt(pos.size()));
            int j14 = blockpos.getY() + 32;

            if (j14 > 0)
            {
                int k17 = random.nextInt(j14);
                BlockPos blockpos1 = this.chunkPos.add(i7, k17, l10);
                BlockFlower.EnumFlowerType blockflower$enumflowertype = biomeIn.pickRandomFlower(random, blockpos1);
                BlockFlower blockflower = blockflower$enumflowertype.getBlockType().getBlock();

                if (blockflower.getDefaultState().getMaterial() != Material.AIR)
                {
                    this.flowerGen.setGeneratedBlock(blockflower, blockflower$enumflowertype);
                    this.flowerGen.generate(worldIn, random, blockpos1);
                }
            }
        }

        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.GRASS))
        for (int i3 = 0; i3 < this.grassPerChunk; ++i3)
        {
            int j7 = random.nextInt(16) + 8;
            int i11 = random.nextInt(16) + 8;
            List<BlockPos> pos = this.heightList(j7, i11, worldIn, biomeIn);
            if(pos.size() <= 0) continue;
            BlockPos blockpos = pos.get(random.nextInt(pos.size()));
            int k14 = blockpos.getY() * 2;

            if (k14 > 0)
            {
                int l17 = random.nextInt(k14);
                biomeIn.getRandomWorldGenForGrass(random).generate(worldIn, random, this.chunkPos.add(j7, l17, i11));
            }
        }

        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.DEAD_BUSH))
        for (int j3 = 0; j3 < this.deadBushPerChunk; ++j3)
        {
            int k7 = random.nextInt(16) + 8;
            int j11 = random.nextInt(16) + 8;
            List<BlockPos> pos = this.heightList(k7, j11, worldIn, biomeIn);
            if(pos.size() <= 0) continue;
            BlockPos blockpos = pos.get(random.nextInt(pos.size()));
            int l14 = blockpos.getY() * 2;

            if (l14 > 0)
            {
                int i18 = random.nextInt(l14);
                (new WorldGenDeadBush()).generate(worldIn, random, this.chunkPos.add(k7, i18, j11));
            }
        }

        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LILYPAD))
        for (int k3 = 0; k3 < this.waterlilyPerChunk; ++k3)
        {
            int l7 = random.nextInt(16) + 8;
            int k11 = random.nextInt(16) + 8;
            List<BlockPos> pos = this.heightList(l7, k11, worldIn, biomeIn);
            if(pos.size() <= 0) continue;
            BlockPos blockpos = pos.get(random.nextInt(pos.size()));
            int i15 = blockpos.getY() * 2;

            if (i15 > 0)
            {
                int j18 = random.nextInt(i15);
                BlockPos blockpos4;
                BlockPos blockpos7;

                for (blockpos4 = this.chunkPos.add(l7, j18, k11); blockpos4.getY() > 0; blockpos4 = blockpos7)
                {
                    blockpos7 = blockpos4.down();

                    if (!worldIn.isAirBlock(blockpos7))
                    {
                        break;
                    }
                }

                this.waterlilyGen.generate(worldIn, random, blockpos4);
            }
        }

        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SHROOM))
        {
        for (int l3 = 0; l3 < this.mushroomsPerChunk; ++l3)
        {
            if (random.nextInt(4) == 0)
            {
                int i8 = random.nextInt(16) + 8;
                int l11 = random.nextInt(16) + 8;
                List<BlockPos> pos = this.heightList(i8, l11, worldIn, biomeIn);
                if(pos.size() <= 0) continue;
                BlockPos blockpos2 = pos.get(random.nextInt(pos.size()));
                this.mushroomBrownGen.generate(worldIn, random, blockpos2);
            }

            if (random.nextInt(8) == 0)
            {
                int j8 = random.nextInt(16) + 8;
                int i12 = random.nextInt(16) + 8;
                List<BlockPos> pos = this.heightList(j8, i12, worldIn, biomeIn);
                if(pos.size() <= 0) continue;
                BlockPos blockpos = pos.get(random.nextInt(pos.size()));
                int j15 = blockpos.getY() * 2;

                if (j15 > 0)
                {
                    int k18 = random.nextInt(j15);
                    BlockPos blockpos5 = this.chunkPos.add(j8, k18, i12);
                    this.mushroomRedGen.generate(worldIn, random, blockpos5);
                }
            }
        }

        if (random.nextInt(4) == 0)
        {
            int i4 = random.nextInt(16) + 8;
            int k8 = random.nextInt(16) + 8;
            List<BlockPos> pos = this.heightList(i4, k8, worldIn, biomeIn);
            if(pos.size() > 0) {
            	BlockPos blockpos = pos.get(random.nextInt(pos.size()));
            	int j12 = blockpos.getY() * 2;

            	if (j12 > 0)
            	{
            		int k15 = random.nextInt(j12);
                	this.mushroomBrownGen.generate(worldIn, random, this.chunkPos.add(i4, k15, k8));
            	}
            }
        }

        if (random.nextInt(8) == 0)
        {
            int j4 = random.nextInt(16) + 8;
            int l8 = random.nextInt(16) + 8;
            List<BlockPos> pos = this.heightList(j4, l8, worldIn, biomeIn);
            if(pos.size() > 0) {
            	BlockPos blockpos = pos.get(random.nextInt(pos.size()));
            	int k12 = blockpos.getY() * 2;

            	if (k12 > 0)
            	{
            		int l15 = random.nextInt(k12);
                	this.mushroomRedGen.generate(worldIn, random, this.chunkPos.add(j4, l15, l8));
            	}
            }
        }
        } // End of Mushroom generation
        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.REED))
        {
        for (int k4 = 0; k4 < this.reedsPerChunk; ++k4)
        {
            int i9 = random.nextInt(16) + 8;
            int l12 = random.nextInt(16) + 8;
            List<BlockPos> pos = this.heightList(i9, l12, worldIn, biomeIn);
            if(pos.size() <= 0) continue;
            BlockPos blockpos = pos.get(random.nextInt(pos.size()));
            int i16 = blockpos.getY() * 2;

            if (i16 > 0)
            {
                int l18 = random.nextInt(i16);
                this.reedGen.generate(worldIn, random, this.chunkPos.add(i9, l18, l12));
            }
        }

        for (int l4 = 0; l4 < 10; ++l4)
        {
            int j9 = random.nextInt(16) + 8;
            int i13 = random.nextInt(16) + 8;
            List<BlockPos> pos = this.heightList(j9, i13, worldIn, biomeIn);
            if(pos.size() <= 0) continue;
            BlockPos blockpos = pos.get(random.nextInt(pos.size()));
            int j16 = blockpos.getY() * 2;

            if (j16 > 0)
            {
                int i19 = random.nextInt(j16);
                this.reedGen.generate(worldIn, random, this.chunkPos.add(j9, i19, i13));
            }
        }
        } // End of Reed generation
        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.PUMPKIN))
        if (random.nextInt(32) == 0)
        {
            int i5 = random.nextInt(16) + 8;
            int k9 = random.nextInt(16) + 8;
            List<BlockPos> pos = this.heightList(i5, k9, worldIn, biomeIn);
            if(pos.size() > 0) {
            	BlockPos blockpos = pos.get(random.nextInt(pos.size()));
            	int j13 = blockpos.getY() * 2;

            	if (j13 > 0)
            	{
            		int k16 = random.nextInt(j13);
            		(new WorldGenPumpkin()).generate(worldIn, random, this.chunkPos.add(i5, k16, k9));
            	}
            }
        }

        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.CACTUS))
        for (int j5 = 0; j5 < this.cactiPerChunk; ++j5)
        {
            int l9 = random.nextInt(16) + 8;
            int k13 = random.nextInt(16) + 8;
            List<BlockPos> pos = this.heightList(l9, k13, worldIn, biomeIn);
            if(pos.size() <= 0) continue;
            BlockPos blockpos = pos.get(random.nextInt(pos.size()));
            int l16 = blockpos.getY() * 2;

            if (l16 > 0)
            {
                int j19 = random.nextInt(l16);
                this.cactusGen.generate(worldIn, random, this.chunkPos.add(l9, j19, k13));
            }
        }

        if (this.generateFalls)
        {
            if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_WATER))
            for (int k5 = 0; k5 < 50; ++k5)
            {
                int i10 = random.nextInt(16) + 8;
                int l13 = random.nextInt(16) + 8;
                int i17 = random.nextInt(248) + 8;

                if (i17 > 0)
                {
                    int k19 = random.nextInt(i17);
                    BlockPos blockpos6 = this.chunkPos.add(i10, k19, l13);
                    (new WorldGenLiquids(Blocks.FLOWING_WATER)).generate(worldIn, random, blockpos6);
                }
            }

            if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_LAVA))
            for (int l5 = 0; l5 < 20; ++l5)
            {
                int j10 = random.nextInt(16) + 8;
                int i14 = random.nextInt(16) + 8;
                int j17 = random.nextInt(random.nextInt(random.nextInt(240) + 8) + 8);
                BlockPos blockpos3 = this.chunkPos.add(j10, j17, i14);
                (new WorldGenLiquids(Blocks.FLOWING_LAVA)).generate(worldIn, random, blockpos3);
            }
        }
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Post(worldIn, random, forgeChunkPos));
    }
	
	protected void generateOres(World worldIn, Random random) {
		super.generateOres(worldIn, random);
	}
	
	public List<BlockPos> heightList(int x, int z,World worldIn, Biome biomeIn) {
		List<BlockPos> poss = new ArrayList<BlockPos>();
        for(int y = 0;y < 255;y++) {
        	BlockPos blockpos = this.chunkPos.add(x, y, z);
        	if(worldIn.getBlockState(blockpos) == biomeIn.topBlock&& (this.isWaterAir ? (worldIn.getBlockState(blockpos.add(0, 1, 0)).getBlock() == Blocks.FLOWING_WATER 
        			|| worldIn.getBlockState(blockpos.add(0, 1, 0)).getBlock() == Blocks.WATER) 
					: worldIn.getBlockState(blockpos.add(0, 1, 0)).getBlock().isAir(worldIn.getBlockState(blockpos.add(0, 1, 0)), worldIn, blockpos.add(0, 1, 0)))) {
        		poss.add(blockpos.add(0, 1, 0));
        	}
        }
        return poss;
	}
}
