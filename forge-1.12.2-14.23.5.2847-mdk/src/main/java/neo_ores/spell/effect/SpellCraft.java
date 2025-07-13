package neo_ores.spell.effect;

import neo_ores.api.ICompareBlockState;
import neo_ores.api.Structure;
import neo_ores.api.StructureUtils;
import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.inventory.WrappedInventoryCrafting;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.Reference;
import neo_ores.spell.SpellItemInterfaces.HasTier;
import neo_ores.tileentity.TileEntityPedestal;
import neo_ores.util.SpellUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class SpellCraft extends SpellEffect implements HasTier
{
	private int tier = 0;

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		return null;
	}

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (!(world instanceof WorldServer))
		{
			return;
		}
		if (result != null && result.typeOfHit == Type.BLOCK)
		{
			BlockPos pos = result.getBlockPos();
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock() == Blocks.CRAFTING_TABLE)
			{
				Structure str = new Structure((WorldServer) world, new ResourceLocation(Reference.MOD_ID, "auto_crafter")).setPosition(result.getBlockPos().add(-1, 0, -1));
				if (StructureUtils.isMatch(world, str, ICompareBlockState.DEFAULT))
				{
					SpellUtils.onDisplayParticleTypeA(world, new Vec3d(result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ()), new Vec3d(1, 1, 1),
							SpellUtils.getColor(stack), 8);
					WrappedInventoryCrafting ic = new WrappedInventoryCrafting(3, 3);
					for (int x = -1; x <= 1; x++)
					{
						for (int z = 1; z >= -1; z--)
						{
							TileEntity te = world.getTileEntity(pos.add(x, 1, z));
							if (te instanceof TileEntityPedestal)
							{
								int slot = 3 * (1 - z) + (x + 1);
								ic.setInventorySlotContents(slot, ((TileEntityPedestal) te).getStackInSlot(0));
							}
						}
					}
					IRecipe recipe = CraftingManager.findMatchingRecipe(ic, world);
					if (recipe != null)
					{
						ItemStack recipeResult = recipe.getCraftingResult(ic);
						EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 2.5D, pos.getX() + 0.5D, recipeResult.copy());
						entityItem.motionX = 0.0;
						entityItem.motionY = 0.0;
						entityItem.motionZ = 0.0;
						world.spawnEntity(entityItem);
						for (int i = 0; i < ic.getSizeInventory(); i++) {
							ic.decrStackSize(i, 1);
						}
					}
				}
			}
			else if (this.tier > 0 && state.getBlock() == NeoOresBlocks.mana_workbench)
			{
				Structure str = new Structure((WorldServer) world, new ResourceLocation(Reference.MOD_ID, "auto_mana_crafter")).setPosition(result.getBlockPos().add(-1, 0, -1));
				if (StructureUtils.isMatch(world, str, ICompareBlockState.DEFAULT))
				{
					SpellUtils.onDisplayParticleTypeA(world, new Vec3d(result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ()), new Vec3d(1, 1, 1),
							SpellUtils.getColor(stack), 8);
				}
			}
		}

	}

	@Override
	public void setTier(int value)
	{
		this.tier = value;
	}
}
