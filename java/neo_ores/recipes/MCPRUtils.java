package neo_ores.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import neo_ores.blocks.EmptyPortal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MCPRUtils 
{
	@SuppressWarnings("deprecation")
	private static ItemStack getResultFromList(List<ItemStack> stacks, int tier,World world,BlockPos pos)
	{
		List<ItemStack> inputitems = new ArrayList<ItemStack>();
		Map<ItemStack,Integer> inputs = new HashMap<ItemStack,Integer>();
		for(ItemStack stack : stacks)
		{
			boolean ib = true;
			for(ItemStack item : inputitems)
			{
				if(ManaCraftingRecipes.compareItemStacks(item, stack))
				{
					ib = false;
				}
			}
			
			if(ib)
			{
				int number = 0;
				for(ItemStack input : stacks)
				{
					if(stack.getItem() == input.getItem() && stack.getMetadata() == input.getMetadata())
					{
						number += input.getCount();
					}
				}
				inputs.put(new ItemStack(stack.getItem(),1,stack.getMetadata()),number);
				inputitems.add(new ItemStack(stack.getItem(),1,stack.getMetadata()));
			}
		}
		
		for(ManaCompositionRecipe recipe : GameRegistry.findRegistry(ManaCompositionRecipe.class).getValues())
		{
			if(recipe.getTier() <= tier)
			{
				int i = 0;
				boolean b = false;
				for(Map.Entry<Object,Integer> list : recipe.getRecipe().entrySet())
				{
					for(Map.Entry<ItemStack, Integer> input : inputs.entrySet())
					{
						if(ManaCraftingRecipes.compareItemStacks(list.getKey(),input.getKey()) && input.getValue() % list.getValue() == 0)
						{
							if(i == 0)
							{
								i = input.getValue() / list.getValue();
								b = true;
							}
							else
							{
								if(i != input.getValue() / list.getValue())
								{
									b = false;
								}
							}
						}
						else
						{
							b = false;
						}
					}
				}
				
				if(b)
				{
					Map<Object,Integer> recipeCopy = new HashMap<>(recipe.getRecipe());
					for(Map.Entry<Object,Integer> list : recipeCopy.entrySet())
					{
						for(EntityItem ei : EmptyPortal.getEntityItemCollided(world, pos))
						{
							if(ManaCraftingRecipes.compareItemStacks(list.getKey(),ei.getItem()))
							{
								if(list.getValue() != 0)
								{
									if(list.getValue() < ei.getItem().getCount())
									{
										ei.getItem().shrink(list.getValue());
										list.setValue(list.getValue());
										break;
									}
									else if(list.getValue() == ei.getItem().getCount())
									{
										ei.setDead();
										list.setValue(list.getValue());
										break;
									}
									else if(ei.getItem().getCount() < list.getValue())
									{
										ei.setDead();
										list.setValue(ei.getItem().getCount());
									}
								}
							}
						}
					}
					
					return recipe.getResult().copy();
				}
			}
		}
		
		return ItemStack.EMPTY;
	}
	
	public static ItemStack getResult(World world, BlockPos pos, int tier)
	{
		return getResultFromList(EmptyPortal.getItemCollided(world, pos),tier,world,pos);
	}
}
