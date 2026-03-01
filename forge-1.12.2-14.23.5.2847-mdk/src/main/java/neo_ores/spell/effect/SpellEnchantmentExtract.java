package neo_ores.spell.effect;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import neo_ores.api.ICompareBlockState;
import neo_ores.api.Structure;
import neo_ores.api.StructureUtils;
import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.NeoOresData;
import neo_ores.main.Reference;
import neo_ores.tileentity.TileEntityPedestal;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.ServerUtils;
import neo_ores.util.SpellUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class SpellEnchantmentExtract extends SpellEffect
{

	@Override
	public RayTraceResult getResultAsRunningToSelf(World world, EntityLivingBase runner, ItemStack stack)
	{
		return null;
	}

	@Override
	public void onEffectRunToOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack)
	{
		if (!(world instanceof WorldServer))
		{
			return;
		}
		if (result.typeOfHit != Type.BLOCK) 
		{
			return;
		}

		BlockPos pos = result.getBlockPos();
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == NeoOresBlocks.pedestal)
		{
			Structure str = new Structure((WorldServer) world, new ResourceLocation(Reference.MOD_ID, "enchantment_extractor")).setPosition(result.getBlockPos().add(0, -1, 0));
			if (StructureUtils.isMatch(world, str, ICompareBlockState.DEFAULT))
			{
				SpellUtils.onDisplayParticleTypeA(world, new Vec3d(result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ()), new Vec3d(1, 1, 1),
						SpellUtils.getColor(stack), 8);
				TileEntity tileEntity = world.getTileEntity(pos);
				if (tileEntity != null && tileEntity instanceof TileEntityPedestal)
				{
					TileEntityPedestal tep = (TileEntityPedestal) tileEntity;
					ItemStack enchantedItem = tep.getStackInSlot(0);
					Deque<Tuple<Enchantment, Integer>> enchants = new ArrayDeque<>(ServerUtils.mapToList(EnchantmentHelper.getEnchantments(enchantedItem)));
					if (enchants.isEmpty())
					{
						return;
					}
					List<ItemStack> outputs = new ArrayList<>();
					for (int i = 0; i < 3; i++)
					{
						if (enchants.isEmpty())
						{
							outputs.add(new ItemStack(Items.BOOK));
						}
						else
						{
							Tuple<Enchantment, Integer> pair = enchants.pollLast();
							ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
							EnchantmentHelper.setEnchantments(ServerUtils.listToMap(Arrays.asList(pair)), output);
							outputs.add(output);
						}
					}

					if (enchantedItem.getItem() == Items.ENCHANTED_BOOK)
					{
						if (enchantedItem.hasTagCompound())
						{
							enchantedItem.getTagCompound().removeTag("StoredEnchantments");
							if (enchantedItem.getTagCompound().hasNoTags())
							{
								enchantedItem.setTagCompound(null);
							}
						}

					}

					if (!enchants.isEmpty())
					{
						EnchantmentHelper.setEnchantments(ServerUtils.listToMap(new ArrayList<>(enchants)), enchantedItem);
					}
					else if (enchantedItem.getItem() != Items.ENCHANTED_BOOK)
					{
						if (enchantedItem.hasTagCompound())
						{
							enchantedItem.getTagCompound().removeTag("ench");
							if (enchantedItem.getTagCompound().hasNoTags())
							{
								enchantedItem.setTagCompound(null);
							}
						}
					}
					else
					{
						tep.setInventorySlotContents(0, new ItemStack(Items.BOOK));
					}

					BlockPos shelfPos = pos.offset(EnumFacing.DOWN);
					world.destroyBlock(shelfPos, false);
					double x = shelfPos.getX() + 0.5D;
					double y = shelfPos.getY();
					double z = shelfPos.getZ() + 0.5D;
					for (ItemStack output : outputs)
					{
						EntityItem entityItem = new EntityItem(world, x, y, z, output);
						world.spawnEntity(entityItem);
					}

					if (runner instanceof EntityPlayerMP)
					{
						PlayerMagicData pmds = NeoOresData.instance.getPMD((EntityPlayerMP) runner);
						pmds.addMXP(10L);
					}
				}
			}
		}
	}
}
