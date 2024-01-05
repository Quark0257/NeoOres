package neo_ores.potion;

import java.util.Random;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class PotionNeoOres extends Potion
{
	private static final ResourceLocation TEXTURE = new ResourceLocation("neo_ores:textures/gui/potion_icons.png");
	private int posX;
	private int posY;
	protected final Random random;

	public PotionNeoOres(boolean isBadEffectIn, int liquidColorIn, String name)
	{
		super(isBadEffectIn, liquidColorIn);
		this.setPotionName(name);
		long seed = 0L;
		try
		{
			long j = Long.parseLong(name);

			if (j != 0L)
			{
				seed = j;
			}
		}
		catch (NumberFormatException var7)
		{
			seed = (long) name.hashCode();
		}
		random = new Random(seed);
	}

	public Potion setIconIndex(int x, int y)
	{
		this.posX = x;
		this.posY = y;
		return this;
	}

	public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha)
	{
		this.renderInventoryEffect(effect, gui, x - 3, y - 4, z);
	}

	public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		gui.drawTexturedModalRect(x + 6, y + 7, posX * 18, posY * 18, 18, 18);
	}

	public static interface IFakeAttributeModified
	{
		public UUID getID();

		public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, int amplifier);
	}
}
