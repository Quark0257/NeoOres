package neo_ores.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import neo_ores.api.spell.Spell;
import neo_ores.api.spell.SpellItem;
import neo_ores.api.spell.SpellItemType;
import neo_ores.client.particle.TexturedParticle;
import neo_ores.event.NeoOresClientEvents;
import neo_ores.api.ILifeContainer;
import neo_ores.api.MathUtils;
import neo_ores.api.MathUtils.Surface;
import neo_ores.api.NBTUtils;
import neo_ores.api.RecipeOreStack;
import neo_ores.api.recipe.SpellRecipe;
import neo_ores.api.spell.KnowledgeTab;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresData;
import neo_ores.main.Reference;
import neo_ores.packet.PacketParticleToClient;
import neo_ores.spell.form.IPassiveSpell;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpellUtils
{

	@SuppressWarnings("deprecation")
	public static final List<SpellItem> registry = GameRegistry.findRegistry(SpellItem.class).getValues();

	public static final Map<String, SpellItem> keySet = keySet();

	@SuppressWarnings("deprecation")
	public static final List<SpellRecipe> recipes = GameRegistry.findRegistry(SpellRecipe.class).getValues();

	public static final List<SpellItem> tier_sorted_spells = getSpellsSort();

	public static SpellItem getFromID(String modid, String id)
	{
		if (keySet.containsKey(modid + ":" + id))
		{
			return keySet.get(modid + ":" + id);
		}
		return null;
	}

	public static Map<String, SpellItem> keySet()
	{
		Map<String, SpellItem> keySets = new HashMap<String, SpellItem>();
		for (SpellItem spell : getSpellsSort())
		{
			keySets.put(spell.getModId() + ":" + spell.getRegisteringId(), spell);
		}
		return keySets;
	}

	public static Map<String, List<String>> getAll()
	{
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		Set<String> sset = new HashSet<String>();
		for (SpellItem spell : registry)
		{
			sset.add(spell.getModId());
		}

		for (String key : sset)
		{
			List<String> spellids = new ArrayList<String>();
			for (SpellItem spell : registry)
			{
				if (spell.getModId().equals(key))
				{
					spellids.add(spell.getRegisteringId());
				}
			}
			map.put(key, spellids);
		}

		return map;
	}

	public static List<KnowledgeTab> getAllStudyTabs()
	{
		List<KnowledgeTab> list = new ArrayList<KnowledgeTab>();
		for (SpellItem spell : registry)
		{
			if (!list.contains(spell.getTab()))
				list.add(spell.getTab());
		}

		return list;
	}

	public static List<SpellItem> getSpellsSort()
	{
		List<SpellItem> spells = new ArrayList<SpellItem>();
		for (int tier = 1; tier <= 11; tier++)
		{
			for (SpellItem spell : registry)
			{
				if (spell.getTier() == tier)
				{
					spells.add(spell);
				}
			}
		}
		return spells;
	}

	public static List<SpellItem> getListFromNBT(NBTTagCompound nbt)
	{
		List<SpellItem> spells = new ArrayList<SpellItem>();
		for (String key : nbt.getKeySet())
		{
			if (nbt.hasKey(key, 9))
			{
				NBTTagList list = nbt.getTagList(key, 8);
				for (int i = 0; i < list.tagCount(); i++)
				{
					String id = list.getStringTagAt(i);
					if (keySet.containsKey(key + ":" + id))
					{
						spells.add(keySet.get(key + ":" + id));
					}
				}
			}
		}
		return spells;
	}

	public static NBTTagCompound getNBTFromList(List<SpellItem> spells)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		Set<String> sset = new HashSet<String>();
		for (SpellItem spell : spells)
		{
			sset.add(spell.getModId());
		}

		for (String key : sset)
		{
			NBTTagList list = new NBTTagList();
			for (SpellItem spell : spells)
			{
				if (spell.getModId().equals(key))
				{
					list.appendTag(new NBTTagString(spell.getRegisteringId()));
				}
			}
			nbt.setTag(key, list);
		}

		return nbt;

	}

	public static List<SpellItem> getListFromItemStackNBT(NBTTagCompound nbt)
	{
		if (nbt != null)
		{
			NBTTagCompound copied = nbt.copy();
			if (copied != null && copied.hasKey(NBTTagUtils.SPELL, 10))
			{
				return getListFromNBT(copied.getCompoundTag(NBTTagUtils.SPELL));
			}
		}
		return new ArrayList<SpellItem>();
	}

	public static NBTTagCompound getItemStackNBTFromList(List<SpellItem> spells, NBTTagCompound nbt)
	{
		return getItemStackNBTFromList(spells, nbt, true);
	}

	public static NBTTagCompound getItemStackNBTFromList(List<SpellItem> spells, NBTTagCompound nbt, boolean copy)
	{
		NBTTagCompound output = copy ? nbt.copy() : nbt;
		output.setTag(NBTTagUtils.SPELL, (NBTBase) getNBTFromList(spells));
		return output;
	}

	public static SpellItem getFromXY(int x, int y)
	{
		for (SpellItem spell : registry)
		{
			if (spell.getPositionX() == x && spell.getPositionY() == y)
			{
				return spell;
			}
		}
		return null;
	}

	public static ResourceLocation textureFromSpellItem(SpellItem spellitem)
	{
		String path = "textures/gui/spell/spell_";
		Spell sc = spellitem.getSpellClass();
		if (sc instanceof Spell.SpellConditional)
			path += "conditional_";
		else if (sc instanceof Spell.SpellCorrection)
			path += "correction_";
		else if (sc instanceof Spell.SpellEffect)
			path += "effect_";
		else
			path += "form_";

		if (spellitem.getType() == SpellItemType.AIR)
			path += "air";
		else if (spellitem.getType() == SpellItemType.EARTH)
			path += "earth";
		else if (spellitem.getType() == SpellItemType.FIRE)
			path += "fire";
		else
			path += "water";

		path += ".png";

		return new ResourceLocation(Reference.MOD_ID, path);
	}

	public static ResourceLocation textureFromSpellItemInactive(SpellItem spellitem)
	{
		String path = "textures/gui/spell/spell_";

		Spell sc = spellitem.getSpellClass();
		if (sc instanceof Spell.SpellConditional)
			path += "conditional_";
		else if (sc instanceof Spell.SpellCorrection)
			path += "correction_";
		else if (sc instanceof Spell.SpellEffect)
			path += "effect_";
		else
			path += "form_";

		path += "inactive.png";
		return new ResourceLocation(Reference.MOD_ID, path);
	}

	public static int offsetX(SpellItem spellitem)
	{
		return 8;
	}

	public static int offsetY(SpellItem spellitem)
	{
		if (spellitem.getSpellClass() instanceof Spell.SpellCorrection)
			return 10;
		return 8;
	}

	public static TextFormatting colorFromSpellItem(SpellItem spellitem)
	{
		if (spellitem.getType() == SpellItemType.AIR)
			return TextFormatting.AQUA;
		else if (spellitem.getType() == SpellItemType.EARTH)
			return TextFormatting.GREEN;
		else if (spellitem.getType() == SpellItemType.FIRE)
			return TextFormatting.GOLD;
		else
			return TextFormatting.DARK_PURPLE;
	}

	public static String typeFromSpellItem(SpellItem spellitem)
	{
		if (spellitem.getType() == SpellItemType.AIR)
			return "spell.air";
		else if (spellitem.getType() == SpellItemType.EARTH)
			return "spell.earth";
		else if (spellitem.getType() == SpellItemType.FIRE)
			return "spell.fire";
		else
			return "spell.water";
	}

	public static class NBTTagUtils
	{
		public static final String MAGIC = "magicData";
		public static final String STUDY = "studyData";
		public static final String SPELL = "activeSpells";
		public static final String ADDITIONAL = "additionalData";
		public static final String SPELL_DESC = "desc";
		public static final String MANA = "mana";
		public static final String MAX_MANA = "maxMana";
		public static final String MXP = "mxp";
		public static final String MAX_MXP = "maxMXP";
		public static final String MAGIC_POINT = "magicpoint";
		public static final String LEVEL = "level";
	}

	public static List<RecipeOreStack> getRecipeFromList(List<SpellItem> spells)
	{
		List<RecipeOreStack> recipe = new ArrayList<RecipeOreStack>();
		for (SpellItem spell : spells)
		{
			for (SpellRecipe sr : recipes)
			{
				if (sr.getSpell().equals(spell))
				{
					recipe.addAll(sr.getRecipe());
				}
			}
		}
		return recipe;
	}

	public static List<RecipeOreStack> getClumpedRecipeFromList(List<SpellItem> spells)
	{
		List<RecipeOreStack> recipe = new ArrayList<RecipeOreStack>();
		for (SpellItem spell : spells)
		{
			for (SpellRecipe sr : recipes)
			{
				if (sr.getSpell().equals(spell))
				{
					for (RecipeOreStack iswsfr : sr.getRecipe())
					{
						int n = recipe.size();
						boolean flag = false;
						for (int i = 0; i < n; i++)
						{
							if (recipe.get(i).isItemStack() && iswsfr.isItemStack())
							{
								if (recipe.get(i).compareStackWith(iswsfr.getStack()))
								{
									recipe.set(i, new RecipeOreStack(recipe.get(i).getStack(), recipe.get(i).getSize() + iswsfr.getSize()));
									flag = true;
									break;
								}
							}
							else if (recipe.get(i).isOreDic() && iswsfr.isOreDic())
							{
								if (recipe.get(i).getOreDic().equals(iswsfr.getOreDic()))
								{
									recipe.set(i, new RecipeOreStack(recipe.get(i).getOreDic(), recipe.get(i).getSize() + iswsfr.getSize()));
									flag = true;
									break;
								}
							}
						}
						if (!flag)
							recipe.add(iswsfr);
					}
				}
			}
		}
		return recipe;
	}

	public static long getMPConsume(List<SpellItem> spellList)
	{
		long manasum = 0L;
		float manapro = 1.0F;
		for (SpellItem spellitem : spellList)
		{
			manasum += spellitem.getCostsum();
			manapro *= spellitem.getCostproduct();
		}

		return (long) (manasum * manapro);
	}

	public static void run(World world, EntityLivingBase runner, Event event)
	{
		if (world.isRemote)
			return;
		UUID uuid = runner instanceof EntityPlayerMP ? EntityPlayer.getUUID(((EntityPlayerMP) runner).getGameProfile()) : runner.getUniqueID();
		Map<Integer, Tuple3<ItemStack, NBTTagCompound, Long>> copiedMap = NeoOresData.instance.getPassiveSpells(uuid);
		for (int slot : copiedMap.keySet())
		{
			Tuple3<ItemStack, NBTTagCompound, Long> tuple = copiedMap.get(slot);
			List<SpellItem> rawSpellList = SpellUtils.getListFromItemStackNBT(tuple.getSecond().copy());
			List<Spell> conditionalSpells = new ArrayList<Spell>();
			List<SpellItem> spells = new ArrayList<SpellItem>();
			List<Spell> spellsCorrection = new ArrayList<Spell>();
			for (SpellItem raw : rawSpellList)
			{
				Spell sc = raw.getSpellClass();
				if (sc instanceof Spell.SpellConditional)
				{
					conditionalSpells.add(sc);
				}
				else if (sc instanceof Spell.SpellCorrection)
				{
					spellsCorrection.add(sc);
					spells.add(raw);
				}
				else if (!(sc instanceof Spell.SpellForm && ((Spell.SpellForm) sc).needPrimaryForm()))
				{
					spells.add(raw);
				}
			}

			for (Spell spell : conditionalSpells)
			{
				for (Spell correction : spellsCorrection)
				{
					((Spell.SpellCorrection) correction).onCorrection(spell);
				}
				((Spell.SpellConditional) spell).checkRunnableAndRun(event, world, runner, tuple.getFirst(), SpellUtils.getItemStackNBTFromList(spells, new NBTTagCompound()), tuple.getThird());
			}
		}
	}

	public static void run(List<SpellItem> initializedSpellList, World world, EntityLivingBase runner, ItemStack stack, @Nullable RayTraceResult target)
	{
		if (world.isRemote)
			return;
		RayTraceResult result = target;
		List<Spell> entityspells = new ArrayList<Spell>();
		List<SpellItem> spells = new ArrayList<SpellItem>();
		List<Spell> spellscs = new ArrayList<Spell>();
		for (SpellItem spellitem : initializedSpellList)
		{
			Spell sc = spellitem.getSpellClass();
			if (sc instanceof Spell.SpellForm && ((Spell.SpellForm) sc).needPrimaryForm())
			{
				entityspells.add(sc);
			}
			else
			{
				spells.add(spellitem);
				spellscs.add(sc);
			}
		}

		if (!entityspells.isEmpty())
		{
			for (Spell entityspellitem : entityspells)
			{
				for (Spell spell : spellscs)
				{
					if (spell instanceof Spell.SpellCorrection)
					{
						((Spell.SpellCorrection) spell).onCorrection(entityspellitem);
					}
				}
				if (entityspellitem instanceof Spell.SpellForm)
				{
					((Spell.SpellForm) entityspellitem).onSpellRunning(world, runner, stack, result, SpellUtils.getItemStackNBTFromList(spells, new NBTTagCompound()));
				}
			}
		}
		else
		{
			List<Spell> forms = new ArrayList<Spell>();
			List<Spell> notforms = new ArrayList<Spell>();
			List<SpellItem> notformspells = new ArrayList<SpellItem>();
			for (SpellItem spell : spells)
			{
				Spell sc = spell.getSpellClass();
				if (sc instanceof Spell.SpellForm)
				{
					forms.add(sc);
				}
				else
				{
					notformspells.add(spell);
					notforms.add(sc);
				}
			}

			for (Spell form : forms)
			{
				for (Spell notformspell : notforms)
				{
					if (notformspell instanceof Spell.SpellCorrection)
					{
						((Spell.SpellCorrection) notformspell).onCorrection(form);
					}
				}

				if (form instanceof Spell.SpellForm)
				{
					((Spell.SpellForm) form).onSpellRunning(world, runner, stack, result, SpellUtils.getItemStackNBTFromList(notformspells, new NBTTagCompound()));
				}
			}
		}
	}

	public static RayTraceResult rayTrace(World worldIn, Entity playerIn, double reach, boolean useLiquids, boolean collidedFilter, boolean alive)
	{
		RayTraceResult result = null;
		float f = playerIn.rotationPitch;
		float f1 = playerIn.rotationYaw;
		double x = playerIn.posX;
		double y = playerIn.posY + (double) playerIn.getEyeHeight();
		double z = playerIn.posZ;
		Vec3d pos = new Vec3d(x, y, z);
		float cosf1 = MathHelper.cos(-f1 * 0.017453292F - (float) Math.PI);
		float sinf1 = MathHelper.sin(-f1 * 0.017453292F - (float) Math.PI);
		float cosf = -MathHelper.cos(-f * 0.017453292F);
		float lookY = MathHelper.sin(-f * 0.017453292F);
		float lookX = sinf1 * cosf;
		float lookZ = cosf1 * cosf;
		Vec3d look = new Vec3d(lookX, lookY, lookZ);
		Vec3d start = pos;
		if (playerIn instanceof FakePlayer)
		{
			double startDistance = Double.MAX_VALUE;// Math.sqrt(3.0) / 2.0;
			for (Surface s : MathUtils.BASIC_CUBE)
			{
				if (s.getDistance(look) > 0)
				{
					startDistance = Math.min(startDistance, s.getDistance(look));
				}
			}
			if (reach > startDistance)
			{
				start = pos.addVector(startDistance * lookX, startDistance * lookY, startDistance * lookZ);
			}
		}
		Vec3d end = pos.addVector((double) lookX * reach, (double) lookY * reach, (double) lookZ * reach);
		result = worldIn.rayTraceBlocks(start, end, useLiquids, !useLiquids, false);
		double d1 = reach;
		double distanceToObj = reach;
		if (result != null)
		{
			distanceToObj = result.hitVec.distanceTo(pos);
			d1 = result.hitVec.distanceTo(pos);
		}

		Entity pointedEntity = null;
		Vec3d vec3d3 = null;
		Predicate<Entity> predicate = new Predicate<Entity>()
		{
			public boolean apply(@Nullable Entity entity)
			{
				return entity != null && (collidedFilter ? entity.canBeCollidedWith() : true);
			}
		};
		List<Entity> list = worldIn.getEntitiesInAABBexcluding(playerIn, playerIn.getEntityBoundingBox().expand(look.x * reach, look.y * reach, look.z * reach).grow(1.0D, 1.0D, 1.0D),
				alive ? Predicates.and(Predicates.and(EntitySelectors.NOT_SPECTATING, predicate), EntitySelectors.IS_ALIVE) : Predicates.and(EntitySelectors.NOT_SPECTATING, predicate));
		for (int j = 0; j < list.size(); ++j)
		{
			Entity entity1 = list.get(j);
			AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow((double) entity1.getCollisionBorderSize());
			RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(pos, end);

			if (axisalignedbb.contains(pos))
			{
				if (distanceToObj >= 0.0D)
				{
					pointedEntity = entity1;
					vec3d3 = raytraceresult == null ? pos : raytraceresult.hitVec;
					distanceToObj = 0.0D;
				}
			}
			else if (raytraceresult != null)
			{
				double d3 = pos.distanceTo(raytraceresult.hitVec);

				if (d3 < distanceToObj || distanceToObj == 0.0D)
				{
					if (entity1.getLowestRidingEntity() == playerIn.getLowestRidingEntity() && !entity1.canRiderInteract())
					{
						if (distanceToObj == 0.0D)
						{
							pointedEntity = entity1;
							vec3d3 = raytraceresult.hitVec;
						}
					}
					else
					{
						pointedEntity = entity1;
						vec3d3 = raytraceresult.hitVec;
						distanceToObj = d3;
					}
				}
			}
		}

		if (pointedEntity != null && (distanceToObj < d1 || result == null))
		{
			result = new RayTraceResult(pointedEntity, vec3d3);
		}
		return result;
	}

	public static int[] getSpellTypeValues(List<SpellItem> spells)
	{
		int air = 0;
		int earth = 0;
		int fire = 0;
		int water = 0;
		for (SpellItem spellitem : spells)
		{
			if (spellitem.getType() == SpellItemType.AIR)
			{
				air++;
			}
			else if (spellitem.getType() == SpellItemType.EARTH)
			{
				earth++;
			}
			else if (spellitem.getType() == SpellItemType.FIRE)
			{
				fire++;
			}
			else if (spellitem.getType() == SpellItemType.WATER)
			{
				water++;
			}
		}
		return new int[] { air, earth, fire, water };
	}

	public static int getSpellColor(List<SpellItem> spells)
	{
		int calculated_color = 0xFFFFFF;
		int calculated_red = 0;
		int calculated_green = 0;
		int calculated_blue = 0;
		int calculated_values = 0;
		int[] color_codes = new int[] { 0x00FFCE, 0xB5FF00, 0xFF5200, 0x8700FF };
		int[] type_rates = getSpellTypeValues(spells);
		for (int id = 0; id < 4; id++)
		{
			calculated_red += (color_codes[id] / 0x10000) * type_rates[id];
			calculated_blue += (color_codes[id] % 0x100) * type_rates[id];
			calculated_green += ((color_codes[id] % 0x10000) / 0x100) * type_rates[id];
			calculated_values += type_rates[id];
		}
		if (calculated_values != 0)
			calculated_color = (calculated_red / calculated_values) * 0x10000 + (calculated_green / calculated_values) * 0x100 + calculated_blue / calculated_values;
		return calculated_color;
	}

	public static int getSpellMetadata(List<SpellItem> spells)
	{
		int air = 0;
		int earth = 0;
		int fire = 0;
		int water = 0;
		for (SpellItem spellitem : spells)
		{
			if (spellitem.getType() == SpellItemType.AIR)
			{
				air++;
			}
			else if (spellitem.getType() == SpellItemType.EARTH)
			{
				earth++;
			}
			else if (spellitem.getType() == SpellItemType.FIRE)
			{
				fire++;
			}
			else if (spellitem.getType() == SpellItemType.WATER)
			{
				water++;
			}
		}

		return compare(air, earth, fire, water);
	}

	private static int compare(int air, int earth, int fire, int water)
	{
		int i = air;
		if (i < earth)
			i = earth;
		if (i < fire)
			i = fire;
		if (i < water)
			i = water;

		if (i == air)
		{
			return 0;
		}
		else if (i == earth)
		{
			return 1;
		}
		else if (i == fire)
		{
			return 2;
		}
		else if (i == water)
		{
			return 3;
		}
		else
		{
			return 0;
		}
	}

	public static List<Pair<Vec3d, Vec3d>> getPosVelOnParallelepiped(Vec3d target, Vec3d size, Vec3d velocity)
	{
		List<Pair<Vec3d, Vec3d>> list = new ArrayList<Pair<Vec3d, Vec3d>>();
		double d1 = target.x;
		double d2 = target.y;
		double d3 = target.z;
		double d4 = size.x;
		double d5 = size.y;
		double d6 = size.z;
		double d7 = velocity.x;
		double d8 = velocity.y;
		double d9 = velocity.z;
		list.add(Pair.of(new Vec3d(d1, d2, d3), new Vec3d(d7, 0.0D, 0.0D))); // 1
		list.add(Pair.of(new Vec3d(d1, d2, d3), new Vec3d(0.0D, 0.0D, d9))); // 2
		list.add(Pair.of(new Vec3d(d1, d2, d3 + d6), new Vec3d(0.0D, d8, 0.0D))); // 3
		list.add(Pair.of(new Vec3d(d1, d2 + d5, d3), new Vec3d(0.0D, -d8, 0.0D))); // 4
		list.add(Pair.of(new Vec3d(d1, d2 + d5, d3 + d6), new Vec3d(0.0D, 0.0D, -d9))); // 5
		list.add(Pair.of(new Vec3d(d1, d2 + d5, d3 + d6), new Vec3d(d7, 0.0D, 0.0D))); // 6
		list.add(Pair.of(new Vec3d(d1 + d4, d2, d3), new Vec3d(0.0D, d8, 0.0D))); // 7
		list.add(Pair.of(new Vec3d(d1 + d4, d2, d3 + d6), new Vec3d(0.0D, 0.0D, -d9))); // 8
		list.add(Pair.of(new Vec3d(d1 + d4, d2, d3 + d6), new Vec3d(-d7, 0.0D, 0.0D))); // 9
		list.add(Pair.of(new Vec3d(d1 + d4, d2 + d5, d3), new Vec3d(0.0D, 0.0D, d9))); // 10
		list.add(Pair.of(new Vec3d(d1 + d4, d2 + d5, d3), new Vec3d(-d7, 0.0D, 0.0D))); // 11
		list.add(Pair.of(new Vec3d(d1 + d4, d2 + d5, d3 + d6), new Vec3d(0.0D, -d8, 0.0D))); // 12
		return list;
	}

	private static void onDisplayParticleTypeA(World world, Vec3d target, Vec3d size, int color, int particleVolume, boolean isSendPacket)
	{
		if (world.isRemote)
		{
			SpellUtils.displayParticleTypeA(world, target, size, color, particleVolume);
		}
		else if (isSendPacket)
		{
			PacketParticleToClient ppc = new PacketParticleToClient(target, size, color, particleVolume, world.provider.getDimension());
			NeoOres.PACKET.sendToAll(ppc);
		}
	}

	public static void onDisplayParticleTypeA(World world, Vec3d target, Vec3d size, int color, int particleVolume)
	{
		SpellUtils.onDisplayParticleTypeA(world, target, size, color, particleVolume, true);
	}

	private static void onDisplayParticleTypeAEntity(World world, Entity targetEntity, int color, int particleVolume, boolean isSendPacket)
	{
		AxisAlignedBB aabb = targetEntity.getEntityBoundingBox();
		Vec3d target = new Vec3d(aabb.minX, aabb.minY, aabb.minZ);
		Vec3d size = new Vec3d(aabb.maxX - aabb.minX, aabb.maxY - aabb.minY, aabb.maxZ - aabb.minZ);
		SpellUtils.onDisplayParticleTypeA(world, target, size, color, particleVolume, isSendPacket);
	}

	public static void onDisplayParticleTypeAEntity(World world, Entity targetEntity, int color, int particleVolume)
	{
		SpellUtils.onDisplayParticleTypeAEntity(world, targetEntity, color, particleVolume, true);
	}

	@SideOnly(Side.CLIENT)
	public static void displayParticleTypeA(World world, Vec3d target, Vec3d size, int color, int particleVolume)
	{
		for (Pair<Vec3d, Vec3d> entry : SpellUtils.getPosVelOnParallelepiped(target, size, size))
		{
			Vec3d start = entry.getKey();
			Vec3d velocity = entry.getValue();
			for (int j = 0; j < particleVolume; j++)
			{
				int d = (int) (10.0D / (Math.random() + 0.5D));
				NeoOresClientEvents.getInstance().addParticle(new TexturedParticle(start.x, start.y, start.z, velocity.x / d, velocity.y / d, velocity.z / d,
						d, 1.0F, NeoOres.PARTICLE_MAGIC).setColor(color, 1.0F));
			}
		}
	}

	public static int getColor(ItemStack stack)
	{
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("color"))
		{
			return stack.getTagCompound().getInteger("color");
		}
		return 0xFFFFFF;
	}

	@SideOnly(Side.CLIENT)
	public static List<String> getRecipe(SpellItem spell)
	{
		List<String> list = new ArrayList<String>();
		for (SpellRecipe sr : recipes)
		{
			if (sr.getSpell().equals(spell))
			{
				for (RecipeOreStack recipe : sr.getRecipe())
				{
					if (recipe.isItemStack())
					{
						list.add(recipe.getStack().getDisplayName() + " (" + recipe.getStack().getItem().getRegistryName().getResourceDomain() + ")" + " : x" + recipe.getSize());
					}
					else if (recipe.isOreDic())
					{
						list.add(recipe.getOreDic() + I18n.format("chat.displayOreDic") + " : x" + recipe.getSize());
					}
					else
					{
						list.add(recipe.getRaw() + " : x" + recipe.getSize());
					}
				}
			}
		}
		return list;
	}

	public static boolean spellPay(EntityLivingBase runner, float amount)
	{
		if (runner instanceof FakePlayer && runner instanceof ILifeContainer)
		{
			return ((ILifeContainer) runner).damageWith(NeoOres.PAYMENT, amount);
		}
		else
		{
			runner.attackEntityFrom(NeoOres.PAYMENT, amount);
			if (runner.isEntityAlive())
			{
				return true;
			}
		}
		return false;
	}

	public static boolean spellHeal(EntityLivingBase runner, float amount)
	{
		if (runner instanceof FakePlayer && runner instanceof ILifeContainer)
		{
			return ((ILifeContainer) runner).healContainer(amount);
		}
		else
		{
			float d = runner.getMaxHealth() - runner.getHealth();
			runner.heal(Math.min(d, amount));
			return d > 0.0f;
		}
	}

	public static boolean isMatch(ItemStack target, ItemStack filterItem)
	{
		// item
		boolean flag1 = target.getItem() == filterItem.getItem();
		// tag
		boolean flag2 = filterItem.hasTagCompound() ? ItemStack.areItemStackShareTagsEqual(target, filterItem) : true;
		// ignore metadata
		boolean flag3 = !filterItem.getHasSubtypes() && filterItem.isItemStackDamageable() && !filterItem.isItemDamaged();
		// metadata
		boolean flag4 = flag3 ? true : filterItem.isItemEqual(target);
		return flag1 && flag2 && flag4;
	}

	public static boolean isMatch(Fluid target, ItemStack filterItem)
	{
		if (filterItem.getItem() == ForgeModContainer.getInstance().universalBucket)
		{
			FluidStack fluid = FluidUtil.getFluidContained(filterItem);
			if (fluid != null && fluid.getFluid() != null)
			{
				return fluid.getFluid() == target;
			}
		}
		if (filterItem.getItem() == Items.WATER_BUCKET)
		{
			return target == FluidRegistry.WATER;
		}
		if (filterItem.getItem() == Items.LAVA_BUCKET)
		{
			return target == FluidRegistry.LAVA;
		}
		return false;
	}
	
	public static boolean isFluidContainer(ItemStack stack) 
	{
		return stack.getItem() == ForgeModContainer.getInstance().universalBucket || stack.getItem() == Items.WATER_BUCKET || stack.getItem() == Items.LAVA_BUCKET;
	}

	public static List<ItemStack> getFilteredItems(ItemStack stack, boolean isBlackList)
	{
		List<ItemStack> result = new ArrayList<ItemStack>();
		if (!stack.hasTagCompound())
		{
			return result;
		}
		if (!stack.getTagCompound().hasKey("additionalData"))
		{
			return result;
		}
		String key = getFilterKey(isBlackList);
		NBTUtils nbt = new NBTUtils(stack.getTagCompound().getCompoundTag("additionalData"));
		NBTTagList tagList = nbt.getListAsCompound(key);
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound itemTag = tagList.getCompoundTagAt(i);
			result.add(new ItemStack(itemTag));
		}
		return result;
	}

	public static boolean containsSpell(ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			return false;
		}
		return stack.getTagCompound().hasKey(NBTTagUtils.SPELL, 10);
	}

	public static void setFilteredItems(ItemStack stack, List<ItemStack> list, boolean isBlackList)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		if (!stack.getTagCompound().hasKey("additionalData"))
		{
			stack.getTagCompound().setTag("additionalData", new NBTTagCompound());;
		}
		String key = getFilterKey(isBlackList);
		NBTUtils nbt = new NBTUtils(stack.getTagCompound().getCompoundTag("additionalData"));
		NBTTagList tagList = new NBTTagList();
		for (ItemStack item : list)
		{
			NBTTagCompound serial = item.serializeNBT();
			tagList.appendTag(serial);
		}
		nbt.setTagList(key, tagList);
	}

	public static String getFilterKey(boolean isBlackList)
	{
		return isBlackList ? "targetItemBlackList" : "targetItemWhiteList";
	}

	public static boolean canRunPassiveSpell(DamageSource source)
	{
		if (source instanceof EntityDamageSource && ((EntityDamageSource) source).getIsThornsDamage())
		{
			return false;
		}
		if (source instanceof EntityDamageSourceWithItem)
		{
			ItemStack stack = ((EntityDamageSourceWithItem) source).getStack();
			if (stack.hasTagCompound())
			{
				for (SpellItem spell : SpellUtils.getListFromItemStackNBT(stack.getTagCompound()))
				{
					Spell sc = spell.getSpellClass();
					if (sc instanceof IPassiveSpell)
					{
						return false;
					}
				}
			}
		}
		return source instanceof EntityDamageSource;
	}

	public static boolean canRunPassiveSpell2(DamageSource source, EntityLivingBase runner)
	{
		if (source instanceof EntityDamageSourceWithItem && ((EntityDamageSourceWithItem) source).getTrueSource() == runner)
		{
			ItemStack stack = ((EntityDamageSourceWithItem) source).getStack();
			if (stack.hasTagCompound())
			{
				for (SpellItem spell : SpellUtils.getListFromItemStackNBT(stack.getTagCompound()))
				{
					Spell sc = spell.getSpellClass();
					if (sc instanceof IPassiveSpell)
					{
						return false;
					}
				}
			}
		}
		return true;
	}
}
