package neo_ores.api.recipe;

import java.util.ArrayList;
import java.util.List;

import neo_ores.api.LargeItemStack;
import neo_ores.api.RecipeOreStack;
import neo_ores.tileentity.TileEntityEnhancedPedestal;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MCPRUtils
{
	@SuppressWarnings("deprecation")
	private static ItemStack getResultFromList(int tier, World world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityEnhancedPedestal)
		{
			TileEntityEnhancedPedestal teep = (TileEntityEnhancedPedestal) te;
			List<LargeItemStack> inputitems = new ArrayList<LargeItemStack>();
			for (ItemStack stack : teep.getItems())
			{
				if (!stack.isEmpty())
				{
					boolean flag = true;
					int size = inputitems.size();
					for (int i = 0; i < size; i++)
					{
						LargeItemStack input = inputitems.get(i);
						if (input.compareWith(stack))
						{
							input.addSize(stack.getCount());
							inputitems.set(i, input);
							flag = false;
						}
					}
					if (flag)
						inputitems.add(new LargeItemStack(stack, stack.getCount()));
				}
			}

			for (ManaCompositionRecipe recipe : GameRegistry.findRegistry(ManaCompositionRecipe.class).getValues())
			{
				if (recipe.getTier() <= tier)
				{
					boolean b = false;
					for (RecipeOreStack list : recipe.getRecipe())
					{
						for (LargeItemStack input : inputitems)
						{
							if ((list.compareStackWith(input.getStack()) || list.compareOreDicWith(input.getStack())))
							{
								if (input.getSize() % list.getSize() == 0)
								{
									if (input.getSize() / list.getSize() > 0)
									{
										b = true;
									}
									else
									{
										b = false;
									}
								}
								else
								{
									b = false;
								}

							}

						}
					}

					if (b)
					{
						if (world.isRemote)
							return recipe.getResult().copy();
						List<RecipeOreStack> recipeCopy = new ArrayList<RecipeOreStack>();
						recipeCopy.addAll(recipe.getRecipe());
						for (RecipeOreStack list : recipeCopy)
						{
							int size = list.getSize();
							int n = 0;
							for (ItemStack stackI : teep.getItems())
							{
								LargeItemStack isws = new LargeItemStack(stackI, stackI.getCount());
								if (list.compareStackWith(isws.getStack()) || list.compareOreDicWith(isws.getStack()))
								{
									if (size != 0)
									{
										if (size < isws.getSize())
										{
											teep.decrStackSize(n, size);
											size = 0;
											break;
										}
										else if (size == isws.getSize())
										{
											teep.removeStackFromSlot(n);
											size = 0;
											break;
										}
										else if (isws.getSize() < size)
										{
											teep.removeStackFromSlot(n);
											size -= isws.getSize();
										}
									}
								}
								n++;
							}
						}

						return recipe.getResult().copy();
					}
				}
			}
		}

		return ItemStack.EMPTY;
	}

	public static ItemStack getResult(World world, BlockPos pos, int tier)
	{
		return getResultFromList(tier, world, pos);
	}
}
