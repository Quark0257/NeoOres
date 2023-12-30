package neo_ores.world.gen.structures.earth;

import java.util.Random;

import neo_ores.world.gen.structures.RecursiveGenerator;
import neo_ores.world.gen.structures.StructurePieceAndOption;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureStart;

public class EarthStructureStart extends StructureStart {
	
	public EarthStructureStart()
    {
    }

    public EarthStructureStart(World worldIn, Random random, int chunkX, int chunkZ)
    {
        super(chunkX, chunkZ);
        this.create(worldIn, random, chunkX, chunkZ);
    }
    
    public void create(World worldIn, Random rnd, int chunkX, int chunkZ)
    {
        Random random = new Random((long)(chunkX + chunkZ * 10387313));
        Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
        
        int i = random.nextInt(64) + 64;
        
        if(worldIn instanceof WorldServer)
        {
            BlockPos blockpos = new BlockPos(chunkX * 16 + 8, i, chunkZ * 16 + 8);
            WorldServer server = (WorldServer)worldIn;
			RecursiveGenerator rg;
			while(true) {
				rg = new RecursiveGenerator();
				rg.getRecursiveList(EarthStructurePieces.BOSS, blockpos, rotation, null, random, 15);
				if(rg.chestCount > 0) break;
			}
			
			int bosskeyIndex = random.nextInt(rg.chestCount);
			int count = 0;
			for(StructurePieceAndOption sp : rg.list) {
				if(sp.piece.hasChest) {
					if(count == bosskeyIndex) {
						sp.bossKey = true;
					}
					count++;
				}
				EarthStructurePieceComponent sn = new EarthStructurePieceComponent(server, sp);
				this.components.add(sn);
			}
            this.updateBoundingBox();
        }
    }
    
    public boolean isSizeableStructure()
    {
        return true;
    }
}
