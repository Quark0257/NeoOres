package neo_ores.world.gen.structures;

import neo_ores.api.MathUtils;
import neo_ores.main.Reference;
import neo_ores.world.gen.structures.StructureNatural;
import neo_ores.world.gen.structures.StructurePieceAndOption;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public abstract class StructurePieceComponent extends StructureNatural
{

	protected boolean bossKey;
	protected String dimPath;

	public StructurePieceComponent()
	{
	}

	public StructurePieceComponent(WorldServer world, StructurePieceAndOption spao, String dimPath)
	{
		super(world, new ResourceLocation(Reference.MOD_ID, dimPath + spao.piece.structureName), spao.relativePos.add(MathUtils.rot(spao.piece.offset(), spao.rotation)), spao.rotation);
		this.bossKey = spao.bossKey;
		this.structureName = dimPath + spao.piece.structureName;
	}

	protected void writeStructureToNBT(NBTTagCompound tagCompound)
	{
		super.writeStructureToNBT(tagCompound);
		tagCompound.setString("Template", this.structureName);
		tagCompound.setString("Rot", this.rotation.name());
		tagCompound.setBoolean("bossKey", this.bossKey);
	}

	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager manager)
	{
		super.readStructureFromNBT(tagCompound, manager);
		this.structureName = tagCompound.getString("Template");
		this.bossKey = tagCompound.getBoolean("bossKey");
		this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
		this.placeSettings = new PlacementSettings().setRotation(this.rotation).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);
		Template template = manager.getTemplate((MinecraftServer) null, new ResourceLocation(Reference.MOD_ID, this.structureName));
		this.setup(template, this.templatePosition, this.placeSettings);
	}

	public void readStructureBaseNBT(World worldIn, NBTTagCompound tagCompound)
	{
		super.readStructureBaseNBT(worldIn, tagCompound);
		if (worldIn instanceof WorldServer)
			this.server = (WorldServer) worldIn;
	}

	public String toString()
	{
		return "SPC{" + this.structureName + "," + this.rotation.name() + "," + this.templatePosition + "," + this.getBoundingBox() + "}";
	}
}
