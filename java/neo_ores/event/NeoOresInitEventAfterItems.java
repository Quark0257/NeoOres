package neo_ores.event;

import java.util.ArrayList;
import java.util.List;

import neo_ores.main.NeoOres;
import neo_ores.main.Reference;
import neo_ores.recipes.ManaCompositionRecipe;
import neo_ores.recipes.ManaCraftingRecipe;
import neo_ores.spell.SpellItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class NeoOresInitEventAfterItems 
{	
	private List<ItemStack> resulta = new ArrayList<ItemStack>();
	private List<Integer> manaa = new ArrayList<Integer>();
	private List<Object[]> oa = new ArrayList<Object[]>();
	
	@SubscribeEvent
	public void registerSpellItems(final RegistryEvent.Register<SpellItem> event)
	{
		event.getRegistry().register(NeoOres.spell_dig);
		event.getRegistry().register(NeoOres.spell_touch);
		event.getRegistry().register(NeoOres.spell_support_liquid);
		event.getRegistry().register(NeoOres.spell_composition);
		event.getRegistry().register(NeoOres.spell_harvestLv1);
		event.getRegistry().register(NeoOres.spell_harvestLv2);
		event.getRegistry().register(NeoOres.spell_earth_damage);
	}

	@SubscribeEvent
	public void registerManaCraftingRecipe(final RegistryEvent.Register<ManaCraftingRecipe> event)
	{
		this.addManaCraftingRecipe(new ItemStack(NeoOres.undite,9),9, new ItemStack(NeoOres.undite_block));
		this.addManaCraftingRecipe(new ItemStack(NeoOres.undite_block), 9, "RRR", "RRR","RRR",'R', new ItemStack(NeoOres.undite));
		this.addManaCraftingRecipe(new ItemStack(NeoOres.undite_axe), 100, "RR", "TR","T ",'R', new ItemStack(NeoOres.undite), 'T',new ItemStack(Items.STICK));
		this.addManaCraftingRecipe(new ItemStack(NeoOres.undite_pickaxe), 100, "RRR", " T "," T ",'R', new ItemStack(NeoOres.undite), 'T',new ItemStack(Items.STICK));
		this.addManaCraftingRecipe(new ItemStack(NeoOres.undite_shovel), 100, "R", "T","T",'R', new ItemStack(NeoOres.undite), 'T',new ItemStack(Items.STICK));
		this.addManaCraftingRecipe(new ItemStack(Blocks.LOG), 20, "RR", "RR",'R', "plankWood");
		
		for(int i = 0;i < manaa.size();i++)
		{
			event.getRegistry().register(new ManaCraftingRecipe(Reference.MOD_ID,i,resulta.get(i),manaa.get(i),oa.get(i)));
		}
	}
	
	@SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
        event.getRegistry().register(new ShapedRecipes("", 3, 3, NonNullList.from(Ingredient.EMPTY, Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItem(Item.getItemFromBlock(Blocks.DRAGON_EGG)), Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItems(Items.NETHER_STAR),Ingredient.fromItems(Items.NETHER_STAR)),NeoOres.addName(NeoOres.addEnchantment(NeoOres.addRegacy(new ItemStack(Items.STICK)),Enchantments.KNOCKBACK,100),"homerunbat")).setRegistryName(Reference.MOD_ID,"homerunbat"));
    }
	
	@SubscribeEvent
	public void registerManaCompositionRecipe(final RegistryEvent.Register<ManaCompositionRecipe> event)
	{
		event.getRegistry().register(new ManaCompositionRecipe(Reference.MOD_ID,0,0,new ItemStack(Blocks.PLANKS,5),new ItemStack(Blocks.LOG),5));
	}
	
	private void addManaCraftingRecipe(ItemStack result,int mana,Object... objects)
	{
		resulta.add(result);
		manaa.add(mana);
		oa.add(objects);
	}
}
