package neo_ores.world.gen.structures;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.MathUtils;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class StructureBoundingBox
{

	public final int minX;
	public final int minY;
	public final int minZ;
	public final int maxX;
	public final int maxY;
	public final int maxZ;

	public StructureBoundingBox(BlockPos pos, Vec3i size, Rotation rot)
	{
		Vec3i rot_pos = MathUtils.rot(MathUtils.add(size, new Vec3i(-1, -1, -1)), rot);
		this.minX = Math.min(pos.getX(), rot_pos.getX() + pos.getX());
		this.maxX = Math.max(pos.getX(), rot_pos.getX() + pos.getX());
		this.minY = Math.min(pos.getY(), rot_pos.getY() + pos.getY());
		this.maxY = Math.max(pos.getY(), rot_pos.getY() + pos.getY());
		this.minZ = Math.min(pos.getZ(), rot_pos.getZ() + pos.getZ());
		this.maxZ = Math.max(pos.getZ(), rot_pos.getZ() + pos.getZ());
	}

	public StructureBoundingBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
	{
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public boolean doesInclude(StructureBoundingBox compared)
	{
		boolean flag0 = false;
		for (BlockPos pos : compared.getVertexes())
		{
			if (this.minX <= pos.getX() && pos.getX() <= this.maxX && this.minY <= pos.getY() && pos.getY() <= this.maxY && this.minZ <= pos.getZ() && pos.getZ() <= this.maxZ)
			{
				flag0 = true;
				break;
			}
		}
		for (BlockPos pos : this.getVertexes())
		{
			if (compared.minX <= pos.getX() && pos.getX() <= compared.maxX && compared.minY <= pos.getY() && pos.getY() <= compared.maxY && compared.minZ <= pos.getZ() && pos.getZ() <= compared.maxZ)
			{
				if (flag0)
					return true;
			}
		}
		return false;
	}

	public boolean doesIncludeAll(StructureBoundingBox included)
	{
		return included.maxX <= this.maxX && this.minX <= included.minX && included.maxY <= this.maxY && this.minY <= included.minY && included.maxZ <= this.maxZ && this.minZ <= included.minZ;
	}

	public boolean doesExclude(StructureBoundingBox compared)
	{
		return compared.maxX < this.minX || this.maxX < compared.minX || compared.maxY < this.minY || this.maxY < compared.minY || compared.maxZ < this.minZ || this.maxZ < compared.minZ;
	}

	public List<BlockPos> getVertexes()
	{
		List<BlockPos> list = new ArrayList<BlockPos>();
		for (int i = 0; i < 8; i++)
		{
			list.add(new BlockPos(((i / 4) % 2 == 0) ? this.minX : this.maxX, ((i / 2) % 2 == 0) ? this.minY : this.maxY, (i % 2 == 0) ? this.minZ : this.maxZ));
		}
		return list;
	}

	public String toString()
	{
		return "BB[{" + this.minX + "," + this.minY + "," + this.minZ + "},{" + this.maxX + "," + this.maxY + "," + this.maxZ + "}]";
	}
}
