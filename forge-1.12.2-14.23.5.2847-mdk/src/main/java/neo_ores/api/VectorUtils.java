package neo_ores.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class VectorUtils
{

	public static List<BlockPos> getBlocksFromVec(Vec3d origin, Vec3d destination)
	{
		List<BlockPos> list = new ArrayList<BlockPos>();
		double x = destination.x - origin.x;
		double y = destination.y - origin.y;
		double z = destination.z - origin.z;
		Vec3d direVec = new Vec3d(x, y, z);
		double offsetD = 0.25 * Math.sqrt(3);
		Vec3d[] offsets = getOffsets(direVec, offsetD);

		double baseD = Math.abs(x);
		int baseClamped = Math.abs(MathHelper.floor(destination.x) - MathHelper.floor(origin.x)) - 1;
		Vec3d[] clampedOrigins = new Vec3d[3];
		int clampedOrigin = 0;
		double[] t = new double[3];

		clampedOrigin = MathHelper.floor(origin.x);

		for (int i = 0; i < 3; i++)
		{
			t[i] = (clampedOrigin - origin.x - offsets[i].x) / x;
		}

		if (Math.abs(y) > baseD)
		{
			baseD = Math.abs(y);
			baseClamped = Math.abs(MathHelper.floor(destination.y) - MathHelper.floor(origin.y)) - 1;

			clampedOrigin = MathHelper.floor(origin.y);

			for (int i = 0; i < 3; i++)
			{
				t[i] = (clampedOrigin - origin.y - offsets[i].y) / y;
			}
		}

		if (Math.abs(z) > baseD)
		{
			baseD = Math.abs(z);
			baseClamped = Math.abs(MathHelper.floor(destination.z) - MathHelper.floor(origin.z)) - 1;

			clampedOrigin = MathHelper.floor(origin.z);

			for (int i = 0; i < 3; i++)
			{
				t[i] = (clampedOrigin - origin.z - offsets[i].z) / z;
			}
		}

		for (int i = 0; i < 3; i++)
		{
			clampedOrigins[i] = new Vec3d(origin.x + offsets[i].x + x * t[i], origin.y + offsets[i].y + y * t[i], origin.z + offsets[i].z + z * t[i]);
		}

		int i;
		Vec3d[] ranges = new Vec3d[3];
		for (i = 0; i < baseClamped; i++)
		{
			List<BlockPos> poss = new ArrayList<BlockPos>();
			Vec3d dvec = new Vec3d(i * x / baseD, i * y / baseD, i * z / baseD);
			for (int d = 0; d < 3; d++)
			{
				ranges[d] = dvec.add(clampedOrigins[d]);
				BlockPos pos = new BlockPos(MathHelper.floor(ranges[d].x), MathHelper.floor(ranges[d].y), MathHelper.floor(ranges[d].z));
				if (!poss.contains(pos))
					poss.add(pos);
			}

			if (poss.size() == 2)
			{
				for (BlockPos pos : getPosSq(poss.get(0), poss.get(1)))
				{
					if (!poss.contains(pos))
						poss.add(pos);
				}
			}
			else if (poss.size() == 3)
			{
				for (int u = 0; u < 2; u++)
				{
					for (int v = u + 1; v < 3; v++)
					{
						for (BlockPos pos : getPosSq(poss.get(u), poss.get(v)))
						{
							if (!poss.contains(pos))
								poss.add(pos);
						}
					}
				}
			}

			list.addAll(poss);
		}

		return list;
	}

	public static List<BlockPos> getPosSq(BlockPos pos1, BlockPos pos2)
	{
		List<BlockPos> list = new ArrayList<BlockPos>();

		double[] ps = new double[3];
		ps[0] = ((pos2.getX() - pos1.getX()) / 2) + 0.5D;
		ps[1] = ((pos2.getY() - pos1.getY()) / 2) + 0.5D;
		ps[2] = ((pos2.getZ() - pos1.getZ()) / 2) + 0.5D;

		int da = 0;
		int notInt = 0;
		for (int s = 0; s < 2; s++)
		{
			if (MathUtils.isInteger(ps[s]))
				da++;
			else
				notInt = s;
		}

		if (da == 2)
		{
			for (int b = 0; b < 4; b++)
			{
				double[] bs = new double[3];
				int count = 0;
				for (int s = 0; s < 3; s++)
				{
					bs[s] = ps[s];
					if (s != notInt)
					{
						bs[s] -= 0.5;
						if (count == 0)
						{
							bs[s] += b % 2;
						}
						else
						{
							bs[s] += b / 2;
						}
						count++;
					}
				}

				list.add(new BlockPos(MathHelper.floor(bs[0]), MathHelper.floor(bs[1]), MathHelper.floor(bs[2])));
			}
		}

		return list;
	}

	public static Vec3d[] getOffsets(Vec3d initial, double offsetR)
	{
		double x = initial.x;
		double y = initial.y;
		double z = initial.z;
		double r = Math.sqrt(x * x + y * y + z * z);
		double xzsq = Math.sqrt(x * x + z * z);

		double x0;
		double y0 = offsetR * xzsq / r;
		double z0;
		if (xzsq == 0)
		{
			x0 = offsetR;
			z0 = 0.0;
		}
		else
		{
			x0 = -offsetR * y * x / (r * xzsq);
			z0 = -offsetR * y * z / (r * xzsq);
		}

		double d12 = offsetR * Math.sqrt(3) / 2;
		double sq12 = Math.sqrt(x0 * x0 + z0 * z0);
		double y12 = -y0 / 2;

		double x1;
		double z1;
		double x2;
		double z2;
		if (sq12 == 0)
		{
			x1 = -d12 * z / xzsq;
			z1 = d12 * x / xzsq;
			x2 = d12 * z / xzsq;
			z2 = -d12 * x / xzsq;
		}
		else
		{
			x1 = -x0 / 2 - d12 * z0 / sq12;
			z1 = -z0 / 2 + d12 * x0 / sq12;
			x2 = -x0 / 2 + d12 * z0 / sq12;
			z2 = -z0 / 2 - d12 * x0 / sq12;
		}

		return new Vec3d[] { new Vec3d(x0, y0, z0), new Vec3d(x1, y12, z1), new Vec3d(x2, y12, z2) };
	}
}
