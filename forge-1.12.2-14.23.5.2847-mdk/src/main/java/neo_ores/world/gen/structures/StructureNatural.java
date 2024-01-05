package neo_ores.world.gen.structures;

import java.util.Random;

import neo_ores.api.Structure;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public abstract class StructureNatural extends Structure
{

	protected WorldServer server;
	protected ResourceLocation resource;
	protected String structureName;
	protected Rotation rotation;

	public StructureNatural()
	{
	}

	public StructureNatural(WorldServer world, ResourceLocation resource, BlockPos pos, Rotation rot)
	{
		super(world, resource);
		this.server = world;
		this.rotation = rot;
		this.placeSettings = new PlacementSettings().setRotation(rot).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);
		this.templatePosition = pos;
		this.setup(this.template, this.templatePosition, this.placeSettings);
	}

	public void generate(StructureBoundingBox sbb)
	{
		this.addComponentParts(this.server, this.placeSettings.getRandom(this.getPos()), sbb);
	}

	@Override
	protected abstract void handleDataMarker(String function, BlockPos pos, World worldIn, Random rand, StructureBoundingBox sbb);
}
