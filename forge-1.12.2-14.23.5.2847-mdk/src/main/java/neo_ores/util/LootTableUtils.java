package neo_ores.util;

import java.util.Collection;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraftforge.common.ForgeHooks;

public class LootTableUtils
{
	public static class LootEntryItemStack extends LootEntry
	{
		protected final ItemStack item;

		public LootEntryItemStack(ItemStack itemIn, int weightIn, int qualityIn, LootCondition[] conditionsIn, String entryName)
		{
			super(weightIn, qualityIn, conditionsIn, entryName);
			this.item = itemIn;
		}

		public void addLoot(Collection<ItemStack> stacks, Random rand, LootContext context)
		{
			ItemStack itemstack = this.item.copy();

			if (!itemstack.isEmpty())
			{
				if (itemstack.getCount() < itemstack.getItem().getItemStackLimit(itemstack))
				{
					stacks.add(itemstack);
				}
				else
				{
					int i = itemstack.getCount();

					while (i > 0)
					{
						ItemStack itemstack1 = itemstack.copy();
						itemstack1.setCount(Math.min(itemstack.getMaxStackSize(), i));
						i -= itemstack1.getCount();
						stacks.add(itemstack1);
					}
				}
			}
		}

		protected void serialize(JsonObject json, JsonSerializationContext context)
		{
			ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(this.item.getItem());
			NBTTagCompound compound = this.item.serializeNBT();

			if (resourcelocation == null)
			{
				throw new IllegalArgumentException("Can't serialize unknown item " + this.item.getItem());
			}
			else
			{
				json.addProperty("name", resourcelocation.toString());
				json.addProperty("tag", compound.toString());
				json.addProperty("count", this.item.getCount());
				json.addProperty("meta", this.item.getMetadata());
			}
		}

		public static LootEntryItemStack deserialize(JsonObject object, JsonDeserializationContext deserializationContext, int weightIn, int qualityIn, LootCondition[] conditionsIn)
		{
			String name = ForgeHooks.readLootEntryName(object, "item");
			Item item = JsonUtils.getItem(object, "name");
			int meta = JsonUtils.getInt(object, "meta");
			int count = JsonUtils.getInt(object, "count");
			ItemStack result = new ItemStack(item, count, meta);

			try
			{
				NBTTagCompound compound = JsonToNBT.getTagFromJson(JsonUtils.getString(object, "tag"));
				result.deserializeNBT(compound);
			}
			catch (NBTException e)
			{
				e.printStackTrace();
			}

			return new LootEntryItemStack(result, weightIn, qualityIn, conditionsIn, name);
		}
	}

	public static LootPool getSingleItemPool(ItemStack stack)
	{
		return new LootPool(new LootEntry[] { new LootEntryItemStack(stack, 1, 1, new LootCondition[] { new RandomChance(1.0F) }, "single_item_loot") }, new LootCondition[] { new RandomChance(1.0F) },
				new RandomValueRange(1.0F), new RandomValueRange(0.0F), "single_item_loot");
	}
}
