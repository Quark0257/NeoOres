package neo_ores.client.gui;

import neo_ores.inventory.ContainerManaFurnace;
import neo_ores.inventory.ContainerManaWorkbench;
import neo_ores.inventory.ContainerSpellRecipeCreationTable;
import neo_ores.main.NeoOres;
import neo_ores.tileentity.TileEntityManaFurnace;
import neo_ores.tileentity.TileEntitySpellRecipeCreationTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == NeoOres.guiIDManaWorkbench)
			return new ContainerManaWorkbench(player.inventory, world, new BlockPos(x, y, z), player);
		if (ID == NeoOres.guiIDManaFurnace)
			return new ContainerManaFurnace(player.inventory, ((TileEntityManaFurnace) world.getTileEntity(new BlockPos(x, y, z))));
		if (ID == NeoOres.guiIDSRCT)
			return new ContainerSpellRecipeCreationTable(player.inventory, ((TileEntitySpellRecipeCreationTable) world.getTileEntity(new BlockPos(x, y, z))));
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == NeoOres.guiIDManaWorkbench)
			return new GuiManaWorkbench(player.inventory, world);
		if (ID == NeoOres.guiIDManaFurnace)
			return new GuiManaFurnace(player.inventory, ((TileEntityManaFurnace) world.getTileEntity(new BlockPos(x, y, z))));
		if (ID == NeoOres.guiIDStudyTable)
			return new GuiMageKnowledgeTable();
		if (ID == NeoOres.guiIDSRCT)
			return new GuiSpellRecipeCreationTable(player.inventory, ((TileEntitySpellRecipeCreationTable) world.getTileEntity(new BlockPos(x, y, z))));
		return null;
	}
}
