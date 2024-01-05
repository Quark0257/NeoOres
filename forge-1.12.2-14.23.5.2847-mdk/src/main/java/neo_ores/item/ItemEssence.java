package neo_ores.item;

import neo_ores.config.NeoOresConfig;
import neo_ores.main.NeoOres;
import neo_ores.main.Reference;
import neo_ores.util.PlayerManaDataClient;
import neo_ores.util.PlayerManaDataServer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemEssence extends INeoOresItem.Impl
{
	public ItemEssence()
	{
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(NeoOres.neo_ores_tab);
	}

	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		int i = stack.getMetadata();
		return super.getUnlocalizedName() + "." + i;
	}

	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (this.isInCreativeTab(tab))
		{
			for (int i = 0; i < 5; ++i)
			{
				items.add(new ItemStack(this, 1, i));
			}
		}
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (player.world.isRemote)
		{
			PlayerManaDataClient pmdc = new PlayerManaDataClient((EntityPlayerSP) player);
			if (pmdc.getLevel() > 0)
			{
				return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
			}
			else
			{
				player.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, 1.0F, 1.0F);
			}
		}
		else
		{
			EntityPlayerMP playermp = (EntityPlayerMP) player;
			PlayerManaDataServer pmd = new PlayerManaDataServer(playermp);

			if (pmd.getLevel() > 0)
			{
				return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
			}
			else
			{
				pmd.setLevel(NeoOresConfig.magic.init_level);
				pmd.setTrueMaxMana(NeoOresConfig.magic.init_max_mana);
				pmd.setMana(NeoOresConfig.magic.init_mana);
				pmd.setMXP(NeoOresConfig.magic.init_mxp);
				pmd.setMagicPoint(NeoOresConfig.magic.init_magic_point);
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	public ModelResourceLocation getModel(Item item, int meta)
	{
		String path = "mana_essence.obj";
		if (meta == 0)
		{
			path = "earth_essence.obj";
		}
		else if (meta == 1)
		{
			path = "water_essence.obj";
		}
		else if (meta == 2)
		{
			path = "fire_essence.obj";
		}
		else if (meta == 3)
		{
			path = "air_essence.obj";
		}
		return new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, path), "inventory");
	}

	public int getMaxMeta()
	{
		return 4;
	}
}