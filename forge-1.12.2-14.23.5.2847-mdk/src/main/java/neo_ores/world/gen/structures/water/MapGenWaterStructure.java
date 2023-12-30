package neo_ores.world.gen.structures.water;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenWaterStructure extends MapGenStructure
{
    private final int spacing = 30;
    private final int separation = 11;

    public MapGenWaterStructure() {}

    public String getStructureName()
    {
        return "GabrySanctuary";
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= (this.spacing - 1);
        }

        if (chunkZ < 0)
        {
            chunkZ -= (this.spacing - 1);
        }

        int k = chunkX / this.spacing;
        int l = chunkZ / this.spacing;
        Random random = this.world.setRandomSeed(k, l, 10387313);
        k = k * this.spacing;
        l = l * this.spacing;
        int weight = this.spacing / 2 - 1;
        k = k + (random.nextInt(weight) + random.nextInt(weight)) / 2;
        l = l + (random.nextInt(weight) + random.nextInt(weight)) / 2;

        if (i == k && j == l)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new WaterStructureStart(this.world, this.rand, chunkX, chunkZ);
    }

    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored)
    {
        this.world = worldIn;
        return findNearestStructurePosBySpacing(worldIn, this, pos, this.spacing, this.separation, 10387313, true, 100, findUnexplored);
    }
}
