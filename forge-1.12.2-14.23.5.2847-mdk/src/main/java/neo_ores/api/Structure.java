package neo_ores.api;

import java.util.Random;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponentTemplate;
import net.minecraft.world.gen.structure.template.Template;

public class Structure extends StructureComponentTemplate
{
	public Structure()
	{
	}

	public Structure(WorldServer world, ResourceLocation resource)
	{
		super(0);
		this.template = world.getStructureTemplateManager().getTemplate(null, resource);
	}

	public Structure setPosition(BlockPos pos)
	{
		this.templatePosition = pos;
		this.setBoundingBoxFromTemplate();
		return this;
	}

	@Override
	protected void handleDataMarker(String function, BlockPos pos, World worldIn, Random rand, StructureBoundingBox sbb)
	{
	}

	private void setBoundingBoxFromTemplate()
	{
		Rotation rotation = this.placeSettings.getRotation();
		BlockPos blockpos = this.template.transformedSize(rotation);
		Mirror mirror = this.placeSettings.getMirror();
		this.boundingBox = new StructureBoundingBox(0, 0, 0, blockpos.getX(), blockpos.getY() - 1, blockpos.getZ());

		switch (rotation)
		{
			case NONE:
			default:
				break;
			case CLOCKWISE_90:
				this.boundingBox.offset(-blockpos.getX(), 0, 0);
				break;
			case COUNTERCLOCKWISE_90:
				this.boundingBox.offset(0, 0, -blockpos.getZ());
				break;
			case CLOCKWISE_180:
				this.boundingBox.offset(-blockpos.getX(), 0, -blockpos.getZ());
		}

		switch (mirror)
		{
			case NONE:
			default:
				break;
			case FRONT_BACK:
				BlockPos blockpos2 = BlockPos.ORIGIN;

				if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90)
				{
					if (rotation == Rotation.CLOCKWISE_180)
					{
						blockpos2 = blockpos2.offset(EnumFacing.EAST, blockpos.getX());
					}
					else
					{
						blockpos2 = blockpos2.offset(EnumFacing.WEST, blockpos.getX());
					}
				}
				else
				{
					blockpos2 = blockpos2.offset(rotation.rotate(EnumFacing.WEST), blockpos.getZ());
				}

				this.boundingBox.offset(blockpos2.getX(), 0, blockpos2.getZ());
				break;
			case LEFT_RIGHT:
				BlockPos blockpos1 = BlockPos.ORIGIN;

				if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90)
				{
					if (rotation == Rotation.CLOCKWISE_180)
					{
						blockpos1 = blockpos1.offset(EnumFacing.SOUTH, blockpos.getZ());
					}
					else
					{
						blockpos1 = blockpos1.offset(EnumFacing.NORTH, blockpos.getZ());
					}
				}
				else
				{
					blockpos1 = blockpos1.offset(rotation.rotate(EnumFacing.NORTH), blockpos.getX());
				}

				this.boundingBox.offset(blockpos1.getX(), 0, blockpos1.getZ());
		}

		this.boundingBox.offset(this.templatePosition.getX(), this.templatePosition.getY(),
				this.templatePosition.getZ());
	}

	public Template getTemplate()
	{
		return this.template;
	}

	public StructureTemplate getStrTemplate()
	{
		return new StructureTemplate(this.template);
	}

	public BlockPos getPos()
	{
		return this.templatePosition;
	}
}
