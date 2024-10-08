package neo_ores.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import neo_ores.api.spell.Spell;
import neo_ores.api.spell.SpellItem;
import neo_ores.api.spell.SpellItemType;
import neo_ores.client.particle.ParticleMagic1;
import neo_ores.api.ILifeContainer;
import neo_ores.api.MathUtils;
import neo_ores.api.MathUtils.Surface;
import neo_ores.api.RecipeOreStack;
import neo_ores.api.recipe.SpellRecipe;
import neo_ores.api.spell.KnowledgeTab;
import neo_ores.main.NeoOres;
import neo_ores.main.Reference;
import neo_ores.packet.PacketParticleToClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
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
			boolean flag = false;
			for (KnowledgeTab tab : list)
			{
				if (tab == spell.getTab())
					flag = true;
			}
			if (!flag)
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
		NBTTagCompound output = nbt.copy();
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

	/*
	 * public static NBTTagCompound putStudyIntArrayToNBT(String modid,int
	 * value,NBTTagCompound neo_ores) { if(neo_ores.hasKey(NBTTagUtils.STUDY, 10)) {
	 * NBTTagCompound study = neo_ores.getCompoundTag(NBTTagUtils.STUDY).copy();
	 * if(study.hasKey(modid, 11)) { int[] array = study.getIntArray(modid);
	 * study.setIntArray(modid, SpellUtils.addValueToIntArray(value, array));
	 * neo_ores.setTag(NBTTagUtils.STUDY, study); } }
	 * 
	 * return neo_ores; }
	 */
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

	public static void run(List<SpellItem> initializedSpellList, World world, EntityLivingBase runner, ItemStack stack, @Nullable RayTraceResult target)
	{
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

		/*
		 * if (result != null) { result = new RayTraceResult(targetEntity); }
		 */

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

	public static RayTraceResult rayTrace(World worldIn, Entity playerIn, double reach, boolean useLiquids, boolean collidedFilter)
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
		List<Entity> list = worldIn.getEntitiesInAABBexcluding(playerIn, playerIn.getEntityBoundingBox().expand(look.x * reach, look.y * reach, look.z * reach).grow(1.0D, 1.0D, 1.0D),
				Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
				{
					public boolean apply(@Nullable Entity entity)
					{
						return entity != null && (collidedFilter ? entity.canBeCollidedWith() : true);
					}
				}));
		for (int j = 0; j < list.size(); ++j)
		{
			Entity entity1 = list.get(j);
			// System.out.println(entity1.getName());
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

		/*
		 * if (pointedEntity != null && flag && vec3d.distanceTo(vec3d3) > 3.0D) {
		 * pointedEntity = null; this.mc.objectMouseOver = new
		 * RayTraceResult(RayTraceResult.Type.MISS, vec3d3, (EnumFacing)null, new
		 * BlockPos(vec3d3)); }
		 */

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

	public static List<BlockPos> rangedPos(BlockPos target, EnumFacing face, int range)
	{
		List<BlockPos> list = new ArrayList<BlockPos>();
		if (face == EnumFacing.DOWN || face == EnumFacing.UP)
		{
			int x = target.getX() - range;
			int z = target.getZ() - range;
			for (int i = 0; i < range * 2 + 1; i++)
			{
				for (int j = 0; j < range * 2 + 1; j++)
				{
					BlockPos pos = new BlockPos(x + i, target.getY(), z + j);
					list.add(pos);
				}
			}
		}
		else if (face == EnumFacing.WEST || face == EnumFacing.EAST)
		{
			int y = target.getY() - range;
			int z = target.getZ() - range;
			for (int i = 0; i < range * 2 + 1; i++)
			{
				for (int j = 0; j < range * 2 + 1; j++)
				{
					BlockPos pos = new BlockPos(target.getX(), y + i, z + j);
					list.add(pos);
				}
			}
		}
		else
		{
			int x = target.getX() - range;
			int y = target.getY() - range;
			for (int i = 0; i < range * 2 + 1; i++)
			{
				for (int j = 0; j < range * 2 + 1; j++)
				{
					BlockPos pos = new BlockPos(x + i, y + j, target.getZ());
					list.add(pos);
				}
			}
		}
		return list;
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

	public static void onDisplayParticleTypeA(World world, Vec3d target, Vec3d size, TextureAtlasSprite[] texture, int color, int particleVolume, boolean isSendPacket)
	{
		if (world.isRemote)
		{
			SpellUtils.displayParticleTypeA(world, target, size, texture, color, particleVolume);
		}
		else if (isSendPacket)
		{
			PacketParticleToClient ppc = new PacketParticleToClient(target, size, texture, color, particleVolume);
			NeoOres.PACKET.sendToAll(ppc);
		}
	}

	public static void onDisplayParticleTypeAEntity(World world, Entity targetEntity, TextureAtlasSprite[] texture, int color, int particleVolume, boolean isSendPacket)
	{
		AxisAlignedBB aabb = targetEntity.getRenderBoundingBox();
		Vec3d target = new Vec3d(aabb.minX, aabb.minY, aabb.minZ);
		Vec3d size = new Vec3d(aabb.maxX - aabb.minX, aabb.maxY - aabb.minY, aabb.maxZ - aabb.minZ);
		SpellUtils.onDisplayParticleTypeA(world, target, size, texture, color, particleVolume, isSendPacket);
	}

	@SideOnly(Side.CLIENT)
	public static void displayParticleTypeA(World world, Vec3d target, Vec3d size, TextureAtlasSprite[] texture, int color, int particleVolume)
	{
		for (Pair<Vec3d, Vec3d> entry : SpellUtils.getPosVelOnParallelepiped(target, size, size))
		{
			Vec3d start = entry.getKey();
			Vec3d velocity = entry.getValue();
			for (int j = 0; j < particleVolume; j++)
			{
				int d = (int) (10.0D / (Math.random() + 0.5D));
				ParticleMagic1 png = new ParticleMagic1(world, start.x, start.y, start.z, velocity.x / d, velocity.y / d, velocity.z / d, color, d, 0.0005F, texture);
				Minecraft.getMinecraft().effectRenderer.addEffect(png);
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
}
