package neo_ores.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ManaCraftingRecipes 
{		
	static boolean compareItemStacks(Object reference, ItemStack target)
    {
		if(reference instanceof ItemStack)
		{
			return ((ItemStack)reference).getItem() == target.getItem() && (((ItemStack)reference).getMetadata() == 32767 || ((ItemStack)reference).getMetadata() == target.getMetadata());
		}
		else if(reference instanceof String)
		{
			if(!OreDictionary.getOres((String)reference).isEmpty())
			{
				for(ItemStack recipeitem : OreDictionary.getOres((String)reference))
				{
					if(recipeitem.getItem() == target.getItem() && (recipeitem.getMetadata() == 32767 || recipeitem.getMetadata() == target.getMetadata()))
					{
						return true;
					}
				}
			}
		}
		
		return false;
    }
	
	@SuppressWarnings({ "deprecation" })
	public Object[] getResult(List<ItemStack[]> items)
	{
		for(ManaCraftingRecipe recipe : GameRegistry.findRegistry(ManaCraftingRecipe.class).getValues())
		{		
			if(recipe.isShapeless())
			{
				List<Object> recipeItems = new ArrayList<Object>();
				recipeItems.addAll(recipe.getShapelessRecipe());
				int d = 0;
				for(int i = 0;i < 3;i++)
				{
					for(int j = 0;j < 3;j++)
					{
						if(!items.get(i)[j].isEmpty() && items.get(i)[j] != null)
						{
							for(int k = 0;k < recipeItems.size();k++)
							{
								if(compareItemStacks(recipeItems.get(k), items.get(i)[j]))
								{
									recipeItems.remove(k);
									d++;
									if(d == recipe.getShapelessRecipe().size())
									{
										return new Object[] {recipe.getResult().copy(),recipe.mana()};
									}
								}
							}
						}
					}
				}
			}
			else
			{
				if(recipe.getShapedRecipe().size() == 3 && recipe.getShapedRecipe().get(0).length == 3)
				{
					for(int n = 0;n < 2;n++)
					{
						int a = 0;
						for(int i = 0;i < 3;i++)
						{
							for(int j = 0;j < 3;j++)
							{
								if(compareItemStacks(recipe.getShapedRecipe().get(i)[(2-j)*n+j*(1-n)], items.get(i)[j]))
								{
									a++;
									if(a == 9)
									{
										return new Object[] {recipe.getResult().copy(),recipe.mana()};
									}
								}
							}
						}
					}
				}
				else if(recipe.getShapedRecipe().size() == 3 && recipe.getShapedRecipe().get(0).length == 2)
				{
					for(int n = 0;n < 2;n++)
					{
						for(int m = 0;m < 2;m++)
						{
							int a = 0;
							for(int i = 0;i < 3;i++)
							{
								for(int j = 0;j < 2;j++)
								{
									if(compareItemStacks(recipe.getShapedRecipe().get(i)[(1-j)*n+j*(1-n)], items.get(i)[j+m]) && items.get(i)[2-(2*m)].isEmpty())
									{
										a++;
										if(a == 6)
										{
											return new Object[] {recipe.getResult().copy(),recipe.mana()};
										}
									}
								}
							}
						}
					}
				}
				else if(recipe.getShapedRecipe().size() == 3 && recipe.getShapedRecipe().get(0).length == 1)
				{
					for(int m = 0;m < 3;m++)
					{
						int a = 0;
						for(int i = 0;i < 3;i++)
						{
							if(compareItemStacks(recipe.getShapedRecipe().get(i)[0], items.get(i)[m]) && items.get(i)[(2-m)/2].isEmpty() && items.get(i)[2-(m/2)].isEmpty())
							{
								a++;
								if(a == 3)
								{
									return new Object[] {recipe.getResult().copy(),recipe.mana()};
								}
							}
						}
					}
				}
				else if(recipe.getShapedRecipe().size() == 2 && recipe.getShapedRecipe().get(0).length == 3)
				{
					for(int n = 0;n < 2;n++)
					{
						for(int l = 0;l < 2;l++)
						{
							int a = 0;
							for(int i = 0;i < 2;i++)
							{
								for(int j = 0;j < 3;j++)
								{
									if(compareItemStacks(recipe.getShapedRecipe().get(i)[(2-j)*n+j*(1-n)], items.get(i+l)[j]) && items.get(2-(2*l))[j].isEmpty())
									{
										a++;
										if(a == 6)
										{
											return new Object[] {recipe.getResult().copy(),recipe.mana()};
										}
									}
								}
							}
						}
					}
				}
				else if(recipe.getShapedRecipe().size() == 1 && recipe.getShapedRecipe().get(0).length == 3)
				{
					for(int n = 0;n < 2;n++)
					{
						for(int l = 0;l < 3;l++)
						{
							int a = 0;
							for(int j = 0;j < 3;j++)
							{
								if(compareItemStacks(recipe.getShapedRecipe().get(0)[(2-j)*n+j*(1-n)], items.get(l)[j]) && items.get((2-l)/2)[j].isEmpty() && items.get(2-(l/2))[j].isEmpty())
								{
									a++;
									if(a == 3)
									{
										return new Object[] {recipe.getResult().copy(),recipe.mana()};
									}
								}
							}
						}
					}
				}
				else if(recipe.getShapedRecipe().size() == 2 && recipe.getShapedRecipe().get(0).length == 2)
				{					
					for(int n = 0;n < 2;n++)
					{
						for(int l = 0;l < 2;l++)
						{
							for(int m = 0;m < 2;m++)
							{
								int a = 0;
								for(int i = 0;i < 2;i++)
								{
									for(int j = 0;j < 2;j++)
									{
										if(compareItemStacks(recipe.getShapedRecipe().get(i)[((1-j)*n)+(j*(1-n))], items.get(i+l)[j+m]) && items.get(2-(2*l))[j].isEmpty() && items.get(i)[2-(2*m)].isEmpty() && items.get(2-(2*l))[2-(2*m)].isEmpty())
										{
											a++;
											if(a == 4)
											{
												return new Object[] {recipe.getResult().copy(),recipe.mana()};
											}
										}
									}
								}
							}
						}
					}
				}
				else if(recipe.getShapedRecipe().size() == 2 && recipe.getShapedRecipe().get(0).length == 1)
				{
					for(int l = 0;l < 2;l++)
					{
						for(int m = 0;m < 3;m++)
						{
							int a = 0;
							for(int i = 0;i < 2;i++)
							{
								if(compareItemStacks(recipe.getShapedRecipe().get(i)[0], items.get(i+l)[m]) && items.get(2-(2*l))[0].isEmpty() && items.get(i)[(2-m)/2].isEmpty() && items.get(i)[2-(m/2)].isEmpty() && items.get(2-(2*l))[(2-m)/2].isEmpty() && items.get(2-(2*l))[2-(m/2)].isEmpty())
								{
									a++;
									if(a == 2)
									{
										return new Object[] {recipe.getResult().copy(),recipe.mana()};
									}
								}
							}
						}
					}
				}
				else if(recipe.getShapedRecipe().size() == 1 && recipe.getShapedRecipe().get(0).length == 2)
				{
					for(int n = 0;n < 2;n++)
					{
						for(int l = 0;l < 3;l++)
						{
							for(int m = 0;m < 2;m++)
							{
								int a = 0;
								for(int j = 0;j < 2;j++)
								{
									if(compareItemStacks(recipe.getShapedRecipe().get(0)[(1-j)*n+j*(1-n)], items.get(l)[j+m]) && items.get((2-l)/2)[j].isEmpty() && items.get(2-(l/2))[j].isEmpty() && items.get(0)[2-(2*m)].isEmpty() && items.get((2-l)/2)[2-(2*m)].isEmpty() && items.get(2-(l/2))[2-(2*m)].isEmpty())
									{
										a++;
										if(a == 2)
										{
											return new Object[] {recipe.getResult().copy(),recipe.mana()};
										}
									}
								}
							}
						}
					}
				}
				else if(recipe.getShapedRecipe().size() == 1 && recipe.getShapedRecipe().get(0).length == 1)
				{
					for(int n = 0;n < 2;n++)
					{
						for(int l = 0;l < 3;l++)
						{
							for(int m = 0;m < 3;m++)
							{
								if(compareItemStacks(recipe.getShapedRecipe().get(0)[0], items.get(l)[m]) && items.get((2-l)/2)[0].isEmpty() && items.get(2-(l/2))[0].isEmpty() && items.get(0)[(2-m)/2].isEmpty() && items.get(0)[2-(m/2)].isEmpty() && items.get((2-l)/2)[(2-m)/2].isEmpty() && items.get(2-(l/2))[(2-m)/2].isEmpty() && items.get((2-l)/2)[2-(m/2)].isEmpty() && items.get(2-(l/2))[2-(m/2)].isEmpty())
								{
									return new Object[] {recipe.getResult().copy(),recipe.mana()};
								}
							}
						}
					}
				}
			}
		}
		return new Object[] {ItemStack.EMPTY,0L};	
	}
}
