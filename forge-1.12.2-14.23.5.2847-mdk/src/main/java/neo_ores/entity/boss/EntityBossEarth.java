package neo_ores.entity.boss;

import java.util.Arrays;

import neo_ores.api.ColorUtils;
import neo_ores.api.spell.SpellItem;
import neo_ores.main.NeoOresSpells;
import neo_ores.util.SpellData;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public class EntityBossEarth extends AbstractNeoOresBoss
{
	private static NBTTagCompound getPlaceAdditional() 
	{
		NBTTagCompound additional = new NBTTagCompound();
		NBTTagCompound block = new NBTTagCompound();
		block.setString("id", "minecraft:cobblestone");
		block.setInteger("metadata", 0);
		additional.setTag("storedBlock", block);
		return additional;
	}
	
	private static final SpellData PROXIMITY = new SpellData(Arrays.asList(new SpellItem[] { NeoOresSpells.spell_touch, NeoOresSpells.spell_earth_damage, NeoOresSpells.spell_damageLv3 }),
			ColorUtils.getColorWithWhite(DimensionName.EARTH.getColor(), 1.0), new NBTTagCompound());
	
	private static final SpellData BREAK = new SpellData(Arrays.asList(new SpellItem[] { NeoOresSpells.spell_touch, NeoOresSpells.spell_dig, NeoOresSpells.spell_harvestLv3 }),
			ColorUtils.getColorWithWhite(DimensionName.EARTH.getColor(), 1.0), new NBTTagCompound());
	private static final SpellData PLACE = new SpellData(Arrays.asList(new SpellItem[] { NeoOresSpells.spell_touch, NeoOresSpells.spell_place_infinity }),
			ColorUtils.getColorWithWhite(DimensionName.EARTH.getColor(), 1.0), getPlaceAdditional());

	public EntityBossEarth(World worldIn)
	{
		super(worldIn);
		this.setSize(0.9F, 1.6F);
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(128.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
	}

	@Override
	protected BossInfoServer getBossInfo()
	{
		return (BossInfoServer) (new BossInfoServer(this.getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
	}

	@Override
	public void setSwingingArms(boolean swingingArms)
	{
	}

	@Override
	protected boolean isWaterType()
	{
		return false;
	}

	@Override
	protected SpellData getProximitySpell()
	{
		return PROXIMITY;
	}

	@Override
	protected SpellData getTargetHideEntitySpell()
	{
		return SpellData.EMPTY;
	}

	@Override
	protected boolean isTargetHideTargetEntity()
	{
		return false;
	}

	@Override
	public SpellData getSpreadSpell()
	{
		// TODO Auto-generated method stub
		return SpellData.EMPTY;
	}
	
	@Override
	public SpellData getSelfSpell()
	{
		// TODO Auto-generated method stub
		return SpellData.EMPTY;
	}

	@Override
	protected boolean isMelee()
	{
		return true;
	}

	@Override
	public SpellData getBreakSpell()
	{
		return BREAK;
	}

	@Override
	public SpellData getPlaceSpell()
	{
		return PLACE;
	}

	@Override
	public double peaceModeDist()
	{
		return 128.0D;
	}
}
