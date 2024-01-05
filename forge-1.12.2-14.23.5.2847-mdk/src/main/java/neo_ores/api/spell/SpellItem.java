package neo_ores.api.spell;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.annotation.Nullable;

import neo_ores.api.PrimitiveUtils;
import neo_ores.main.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import net.minecraftforge.registries.IForgeRegistryEntry;

@ObjectHolder(Reference.MOD_ID)
public class SpellItem extends IForgeRegistryEntry.Impl<SpellItem>
{
	private final BasicData bdata;
	private final String translateKey;
	private final Class<? extends Spell> runnable;
	private final MageKnowledgeTableData stdata;
	private final Object[] constructors;
	// private List<ItemStackWithSizeForRecipe> consumption;

	public SpellItem(BasicData bdata, String translateKey, MageKnowledgeTableData stdata, Class<? extends Spell> runnable, Object... spellConstrcutors)
	{
		this.bdata = bdata;
		this.translateKey = translateKey;
		this.runnable = runnable;
		this.stdata = stdata;
		this.constructors = spellConstrcutors;
		this.setRegistryName(new ResourceLocation(this.bdata.modid, "spellItem." + this.bdata.registerID));
		if (this.getSpellClass() == null)
		{
			FMLLog.log.warn(String.format("{%s}'s spell class is NULL!", this.getRegistryName().toString()));
		}
	}

	public SpellItem(BasicData bdata, String translateKey, MageKnowledgeTableData stdata, Class<? extends Spell> runnable)
	{
		this(bdata, translateKey, stdata, runnable, new Object[0]);
	}

	public String getModId()
	{
		return this.bdata.modid;
	}

	public String getRegisteringId()
	{
		return this.bdata.registerID;
	}

	public SpellItemType getType()
	{
		return this.bdata.type;
	}

	public long getCostsum()
	{
		return this.bdata.costsum;
	}

	public float getCostproduct()
	{
		return this.bdata.costproduct;
	}
	/*
	 * public List<ItemStackWithSizeForRecipe> getConsumption() { return
	 * this.consumption; }
	 */

	public String getTranslateKey()
	{
		return translateKey;
	}

	/*
	 * public SpellItem setConsumption(List<ItemStackWithSizeForRecipe> recipe) {
	 * this.consumption = recipe; return this; }
	 */
	/*
	 * when draw texture,if spell type is CORRECTION, start drawing from (16,20) on
	 * template.(else from (16,16))
	 */
	public ResourceLocation getTexturePath()
	{
		return this.stdata.texture;
	}

	public Spell getSpellClass()
	{
		try
		{
			if (this.constructors.length == 0)
			{
				return this.runnable.newInstance();
			}
			else
			{
				label: for (Constructor<?> c : this.runnable.getConstructors())
				{
					if (c.getParameterTypes().length == this.constructors.length)
					{
						for (int index = 0; index < this.constructors.length; index++)
						{
							Class<?> cls = c.getParameterTypes()[index];
							if (cls.isPrimitive())
							{
								cls = PrimitiveUtils.getObjectClass(cls);
							}
							if (!cls.isInstance(this.constructors[index]))
							{
								continue label;
							}
						}
					}
					return this.runnable.getConstructor(c.getParameterTypes()).newInstance(this.constructors);
				}
			}
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public int getTier()
	{
		return this.bdata.tier;
	}

	public KnowledgeTab getTab()
	{
		return this.stdata.tab;
	}

	@Nullable
	public SpellItem getParent()
	{
		return this.stdata.parent;
	}

	public int getPositionX()
	{
		return this.stdata.x;
	}

	public int getPositionY()
	{
		return this.stdata.y;
	}

	public String toString()
	{
		return this.bdata.modid + ":" + this.bdata.registerID;
	}
}
