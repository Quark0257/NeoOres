package neo_ores.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import neo_ores.api.IPlayerRunnable;
import neo_ores.api.InventoryUtils;
import neo_ores.api.PlayerTrigger;
import neo_ores.api.spell.Spell;
import neo_ores.api.spell.SpellItem;
import neo_ores.block.BlockDimension;
import neo_ores.block.BlockEnhancedPedestal;
import neo_ores.block.BlockPedestal;
import neo_ores.client.gui.GuiNeoGameOverlay;
import neo_ores.config.NeoOresConfig;
import neo_ores.entity.fakeattribute.FakeAttributeMaxMana;
import neo_ores.item.ItemPaxel;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.NeoOresData;
import neo_ores.main.NeoOresItems;
import neo_ores.main.Reference;
import neo_ores.potion.PotionNeoOres;
import neo_ores.spell.form.IPassiveSpell;
import neo_ores.util.EntityDamageSourceWithItem;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.ServerUtils;
import neo_ores.util.SpellUtils;
import neo_ores.world.dimension.FromAirTeleporter;
import neo_ores.world.dimension.DimensionHelper.DimensionName;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class NeoOresEntityEvents
{
	public static final String nbtsoulboundtag = "soulboundslot";

	// TODO
	// Fire:Reflection,Water:CancelDamage,Earth:CancelDeath(MP:N%),Air:CreativeFlying(with
	// Increase Speed)

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEntityJump(LivingEvent.LivingJumpEvent event)
	{
		if (event.getEntityLiving() != null && event.getEntityLiving().isServerWorld())
		{
			if (event.getEntityLiving().isPotionActive(NeoOres.freeze))
			{
				event.getEntityLiving().motionX = 0.0D;
				event.getEntityLiving().motionY = -1.0D;
				event.getEntityLiving().motionZ = 0.0D;
				return;
			}

			if (event.getEntityLiving().isPotionActive(NeoOres.gravity))
			{
				event.getEntityLiving().motionY = -1.0D;
				return;
			}

			SpellUtils.run(event.getEntityLiving().getEntityWorld(), event.getEntityLiving(), event);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEntityWasAttacked(LivingHurtEvent event)
	{
		if (event.getEntityLiving() == null || event.isCanceled())
			return;

		for (PotionEffect effect : event.getEntityLiving().getActivePotionEffects())
		{
			if (effect.getPotion() == NeoOres.shield)
			{
				event.setCanceled(true);
				if (!event.getEntityLiving().world.isRemote)
				{
					event.getEntityLiving().removePotionEffect(effect.getPotion());
					if (effect.getAmplifier() > 0)
					{
						event.getEntityLiving()
								.addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier() - 1, effect.getIsAmbient(), effect.doesShowParticles()));
					}
				}
				return;
			}
		}

		if (event.getEntityLiving() instanceof EntityPlayer && event.getSource() instanceof EntityDamageSource)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			if (!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemPaxel)
			{
				ItemPaxel paxel = (ItemPaxel) player.getHeldItem(EnumHand.MAIN_HAND).getItem();
				if (paxel.isShielding(player.getHeldItem(EnumHand.MAIN_HAND)))
				{
					if (this.rotationInVector(event.getSource().getDamageLocation(), player, 70.0F, 50.0F))
					{
						event.setAmount(0.5F * event.getAmount());
						player.getHeldItem(EnumHand.MAIN_HAND).damageItem(1, player);
					}
				}
				paxel.setShielding(player.getHeldItem(EnumHand.MAIN_HAND), false);

				if (event.getAmount() <= 0.0F)
				{
					paxel.setShielded(player.getHeldItem(EnumHand.MAIN_HAND), false);
				}
			}
		}

		if (!event.getEntityLiving().getEntityWorld().isRemote)
		{
			SpellUtils.run(event.getEntityLiving().getEntityWorld(), event.getEntityLiving(), event);
			Entity entity = event.getSource().getTrueSource();
			if (entity != null && entity instanceof EntityLivingBase)
			{
				SpellUtils.run(event.getEntityLiving().getEntityWorld(), (EntityLivingBase) entity, event);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEntityKnockback(LivingKnockBackEvent event)
	{
		if (event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			if (!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemPaxel)
			{
				ItemPaxel paxel = (ItemPaxel) player.getHeldItem(EnumHand.MAIN_HAND).getItem();
				if (paxel.wasShielding(player.getHeldItem(EnumHand.MAIN_HAND)))
				{
					if (this.rotationInVector(new Vec3d(event.getAttacker().posX, event.getAttacker().posY, event.getAttacker().posZ), player, 70.0F, 50.0F))
					{
						event.setStrength(0.3F * event.getStrength());
					}
				}
				paxel.setShielded(player.getHeldItem(EnumHand.MAIN_HAND), false);
			}
		}
	}

	@SubscribeEvent
	public void onEntitySpawnEvent(EntityJoinWorldEvent event)
	{
		if (event.getEntity() instanceof EntityPlayerMP)
		{
			EntityPlayerMP playermp = (EntityPlayerMP) event.getEntity();
			for (PotionEffect effect : playermp.getActivePotionEffects())
			{
				if (effect.getPotion() instanceof PotionNeoOres.IFakeAttributeModified)
				{
					((PotionNeoOres.IFakeAttributeModified) effect.getPotion()).applyAttributesModifiersToEntity(playermp, effect.getAmplifier());
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLoggin(PlayerLoggedInEvent event)
	{
		if (event.player instanceof EntityPlayerMP)
		{
			NeoOresData.instance.getPSD((EntityPlayerMP) event.player).setLoggedIn(true);
			if (NeoOresConfig.miscellaneous.allowInitialItems && !NeoOresData.instance.getPSD((EntityPlayerMP) event.player).hasInitialItems())
			{
				InventoryUtils.addStackToPlayer(event.player, new ItemStack(NeoOresBlocks.instant_alter));
				NeoOresData.instance.getPSD((EntityPlayerMP) event.player).setInitialItems(true);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event)
	{
		if (event.player instanceof EntityPlayerMP && NeoOresData.instance != null)
		{
			NeoOresData.instance.getPSD((EntityPlayerMP) event.player).setLoggedIn(false);
		}
		else
		{
			NeoOresData.resetStructures();
			NeoOresData.clearAllPMDC();
		}
	}

	@SubscribeEvent
	public void onEntityTravelToDimensionEvent(EntityTravelToDimensionEvent event)
	{
	}

	@SubscribeEvent
	public void onPlayerInteractEvent(PlayerInteractEvent.LeftClickBlock event)
	{
		if (!event.getWorld().isRemote && event.getEntityPlayer() != null && event.getEntityPlayer().capabilities.isCreativeMode && event.getItemStack().getItem() == NeoOresItems.mana_wrench)
		{
			IBlockState state = event.getWorld().getBlockState(event.getPos());
			if (state != null && ((state.getBlock() instanceof BlockEnhancedPedestal) || state.getBlock() instanceof BlockPedestal))
			{
				state.getBlock().onBlockClicked(event.getWorld(), event.getPos(), event.getEntityPlayer());
			}
		}
	}

	@SubscribeEvent
	public void onLivingEvent(LivingUpdateEvent event)
	{
		if (event.getEntity() != null)
		{
			World worldIn = event.getEntity().getEntityWorld();

			// Passive Spells
			if (!worldIn.isRemote)
			{
				if (event.getEntityLiving() != null)
				{
					EntityLivingBase runner = event.getEntityLiving();
					Map<Integer, ItemStack> itemSpells = new HashMap<Integer, ItemStack>();
					for (EntityEquipmentSlot slot : EntityEquipmentSlot.values())
					{
						ItemStack stack = runner.getItemStackFromSlot(slot);
						if (!stack.isEmpty())
						{
							itemSpells.put(slot.getSlotIndex(), stack);
						}
					}

					if (runner instanceof EntityPlayerMP)
					{
						EntityPlayerMP player = (EntityPlayerMP) runner;
						IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
						for (int slot = 0; slot < handler.getSlots(); slot++)
						{
							ItemStack stack = handler.getStackInSlot(slot);
							if (!stack.isEmpty())
							{
								itemSpells.put(slot + EntityEquipmentSlot.values().length, stack);
							}
						}
					}

					for (int slot : itemSpells.keySet())
					{
						ItemStack stack = itemSpells.get(slot);
						List<SpellItem> list = SpellUtils.getListFromItemStackNBT(stack.getTagCompound());
						if (!list.isEmpty())
						{
							Spell passiveSpell = null;
							List<Spell> correctionSpells = new ArrayList<Spell>();
							List<SpellItem> spells = new ArrayList<SpellItem>();
							for (SpellItem raw : list)
							{
								Spell sc = raw.getSpellClass();
								if (sc instanceof IPassiveSpell)
								{
									if (sc instanceof Spell.SpellForm)
									{
										passiveSpell = sc;
									}
								}
								else if (sc instanceof Spell.SpellCorrection)
								{
									correctionSpells.add(sc);
									spells.add(raw);
								}
								else
								{
									spells.add(raw);
								}
							}

							if (passiveSpell != null)
							{
								for (Spell correction : correctionSpells)
								{
									((Spell.SpellCorrection) correction).onCorrection(passiveSpell);
								}
								((IPassiveSpell) passiveSpell).setSlot(slot);
								((IPassiveSpell) passiveSpell).setMana(SpellUtils.getMPConsume(list));
								((Spell.SpellForm) passiveSpell).onSpellRunning(worldIn, runner, stack, null, SpellUtils.getItemStackNBTFromList(spells, new NBTTagCompound()));
							}
						}
					}
				}

				ServerUtils.resetEntityTarget(event.getEntity());
			}

			// MP updater
			if (!event.getEntity().getEntityWorld().isRemote && event.getEntity() instanceof EntityPlayerMP)
			{
				EntityPlayerMP playermp = (EntityPlayerMP) event.getEntity();
				PlayerMagicData pmd = NeoOresData.instance.getPMD(playermp);
				if (playermp.ticksExisted % 20 == 0)
				{
					double x = pmd.getLevel();
					pmd.addMana((long) ((0.000007 * x * (x + 1) * (2 * x + 1) / 6 + x + NeoOresConfig.magic.init_max_mana) * (0.01 * Math.pow(2, -0.0007 * x) + 0.01)));
				}

				FakeAttributeMaxMana famm = new FakeAttributeMaxMana(playermp);
				famm.initialize();
				famm.applyToPlayer();
			}

			// Environmental Controller
			if (!event.getEntity().getEntityWorld().isRemote && event.getEntityLiving() != null)
			{
				if (event.getEntityLiving().getEntityBoundingBox() != null)
				{
					for (BlockPos pos : getBlockPositions(event.getEntityLiving().getEntityBoundingBox(), 0.3))
					{
						IBlockState state = event.getEntityLiving().getEntityWorld().getBlockState(pos);
						if (state.getBlock() == NeoOresBlocks.corroded_dim_leaves || state.getBlock() == NeoOresBlocks.corroding_dim_leaves || state.getBlock() == NeoOresBlocks.dim_leaves
								|| state.getBlock() == NeoOresBlocks.dim_log || state.getBlock() == NeoOresBlocks.dim_planks)
						{
							if (state.getValue(BlockDimension.DIM) == DimensionName.WATER)
							{
								event.getEntityLiving().extinguish();
								break;
							}
						}
						if (NeoOresBlocks.color_saplings.subList(3, 6).contains(state.getBlock()))
						{
							event.getEntityLiving().extinguish();
							break;
						}
					}
					for (BlockPos pos : getBlockPositions(event.getEntityLiving().getEntityBoundingBox(), 0.3))
					{
						IBlockState state = event.getEntityLiving().getEntityWorld().getBlockState(pos);
						if (state.getBlock() == NeoOresBlocks.corroded_dim_leaves || state.getBlock() == NeoOresBlocks.corroding_dim_leaves || state.getBlock() == NeoOresBlocks.dim_leaves
								|| state.getBlock() == NeoOresBlocks.dim_log || state.getBlock() == NeoOresBlocks.dim_planks)
						{
							if (state.getValue(BlockDimension.DIM) == DimensionName.FIRE)
							{
								event.getEntityLiving().setFire(worldIn.rand.nextInt(10) + 5);
								break;
							}
						}
						if (NeoOresBlocks.color_saplings.subList(6, 9).contains(state.getBlock()))
						{
							event.getEntityLiving().setFire(worldIn.rand.nextInt(10) + 5);
							break;
						}
					}
				}
			}

			// Air Teleporter
			MinecraftServer server = event.getEntity().getServer();
			if (server != null && event.getEntity().dimension == NeoOres.THE_AIR.getId() && event.getEntity().posY < -64)
			{
				PlayerList playerList = server.getPlayerList();
				int dest = DimensionType.OVERWORLD.getId();

				Teleporter teleporter = new FromAirTeleporter(server.getWorld(dest));

				if (event.getEntity() instanceof EntityPlayerMP)
				{
					playerList.transferPlayerToDimension((EntityPlayerMP) event.getEntity(), dest, teleporter);
				}
				else
				{
					int origin = event.getEntity().dimension;
					event.getEntity().dimension = dest;
					event.getEntity().getEntityWorld().removeEntityDangerously(event.getEntity());

					event.getEntity().isDead = false;

					playerList.transferEntityToWorld(event.getEntity(), origin, server.getWorld(origin), server.getWorld(dest), teleporter);
				}
			}

			// SneakEvent
			if (event.getEntity() != null && event.getEntity() instanceof EntityPlayerMP)
			{
				EntityPlayerMP entityPlayerMP = (EntityPlayerMP) event.getEntity();
				if (!NeoOresData.instance.getPSD(entityPlayerMP).isSneak() && entityPlayerMP.isSneaking())
				{
					SpellUtils.run(worldIn, entityPlayerMP, new SneakEvent(entityPlayerMP));
				}
				NeoOresData.instance.getPSD(entityPlayerMP).setSneak(entityPlayerMP.isSneaking());
			}
			
			// Run player tasks
			if (event.getEntity() != null && event.getEntity() instanceof EntityPlayerMP) 
			{
				EntityPlayerMP entityPlayerMP = (EntityPlayerMP) event.getEntity();
				List<IPlayerRunnable> unrunnables = new ArrayList<>();
				while (true) 
				{
					IPlayerRunnable runnable = NeoOresData.instance.pollTask(entityPlayerMP);
					if (runnable == null) 
					{
						break;
					}
					if (runnable.isRunnable(entityPlayerMP)) 
					{
						runnable.run(entityPlayerMP);
					}
					else
					{
						unrunnables.add(runnable);
					}
				}
				for (IPlayerRunnable runnable : unrunnables) 
				{
					NeoOresData.instance.addPlayerTask(entityPlayerMP, runnable);
				}
			}
			
			// Player Triggers
			if (event.getEntity() != null && event.getEntity() instanceof EntityPlayerMP) 
			{
				EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
				PlayerMagicData pmd = NeoOresData.instance.getPMD(player);
				for (PlayerTrigger trigger : GameRegistry.findRegistry(PlayerTrigger.class).getValuesCollection()) 
				{
					pmd.triggerPlayerTrigger(trigger, player);
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onLivingDrop(LivingDropsEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		World worldIn = entity.world;
		if (worldIn.isRemote)
		{
			return;
		}
		NBTTagCompound tag = entity.getEntityData();
		if (tag.hasKey("ownerUUID"))
		{
			boolean isOwnerPlayer = tag.getBoolean("ownerPlayer");
			event.setCanceled(isOwnerPlayer);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onLivingDeath(LivingDeathEvent event)
	{
		if ((event.getEntityLiving() == null) || ((event.getEntityLiving() instanceof FakePlayer)) || (event.isCanceled()))
		{
			return;
		}

		for (PotionEffect effect : event.getEntityLiving().getActivePotionEffects())
		{
			if (effect.getPotion() == NeoOres.undying)
			{
				event.setCanceled(true);
				if (!event.getEntityLiving().world.isRemote)
				{
					event.getEntityLiving().removePotionEffect(effect.getPotion());
					if (effect.getAmplifier() > 0)
					{
						event.getEntityLiving()
								.addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier() - 1, effect.getIsAmbient(), effect.doesShowParticles()));
					}
				}
				event.getEntityLiving().setHealth(event.getEntityLiving().getMaxHealth() / 2);
				return;
			}
		}

		if (!event.getEntityLiving().world.isRemote)
		{
			SpellUtils.run(event.getEntityLiving().world, event.getEntityLiving(), event);
		}

		if (event.getEntityLiving().getEntityWorld().getGameRules().getBoolean("keepInventory"))
		{
			return;
		}

		if (event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			for (int i = 0; i < player.inventory.getSizeInventory(); i++)
			{
				if (EnchantmentHelper.getEnchantments(player.inventory.getStackInSlot(i)).containsKey(NeoOres.soulbound))
				{
					NBTTagCompound nbt = player.inventory.getStackInSlot(i).getTagCompound();
					nbt.setInteger(nbtsoulboundtag, i);
					player.inventory.getStackInSlot(i).setTagCompound(nbt);
				}
			}
		}

		if (event.getEntityLiving().isServerWorld())
		{
			SpellUtils.run(event.getEntityLiving().getEntityWorld(), event.getEntityLiving(), event);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerDrop(PlayerDropsEvent event)
	{
		if ((event.getEntityPlayer() == null) || ((event.getEntityPlayer() instanceof FakePlayer)) || (event.isCanceled()))
		{
			return;
		}
		if (event.getEntityPlayer().getEntityWorld().getGameRules().getBoolean("keepInventory"))
		{
			return;
		}

		int space = 0;
		int count = 0;

		for (int i = 0; i < event.getEntityPlayer().inventory.mainInventory.size(); i++)
		{
			if (event.getEntityPlayer().inventory.mainInventory.get(i).isEmpty())
			{
				space++;
			}
		}

		ListIterator<EntityItem> iter = event.getDrops().listIterator();
		while (iter.hasNext())
		{
			EntityItem ei = (EntityItem) iter.next();
			ItemStack stack = ei.getItem();
			if (EnchantmentHelper.getEnchantments(stack).containsKey(NeoOres.soulbound))
			{
				if (count < space)
				{
					if (stack.hasTagCompound() && stack.getTagCompound().hasKey(nbtsoulboundtag))
					{
						NBTTagCompound nbt = stack.getTagCompound();
						int slot = nbt.getInteger(nbtsoulboundtag);
						nbt.removeTag(nbtsoulboundtag);
						stack.setTagCompound(nbt);

						event.getEntityPlayer().inventory.setInventorySlotContents(slot, stack);
					}
					else
					{
						event.getEntityPlayer().inventory.addItemStackToInventory(stack);
					}

					iter.remove();
					count++;
				}
				else
				{
					if (stack.hasTagCompound() && stack.getTagCompound().hasKey(nbtsoulboundtag))
					{
						NBTTagCompound nbt = stack.getTagCompound();
						nbt.removeTag(nbtsoulboundtag);
						stack.setTagCompound(nbt);

						iter.remove();
						ei.setItem(stack);
						iter.add(ei);
					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerClone(PlayerEvent.Clone evt)
	{
		if ((!evt.isWasDeath()) || (evt.isCanceled()))
		{
			return;
		}
		if ((evt.getOriginal() == null) || (evt.getEntityPlayer() == null) || ((evt.getEntityPlayer() instanceof FakePlayer)))
		{
			return;
		}

		if (evt.getEntityPlayer().getEntityWorld().getGameRules().getBoolean("keepInventory"))
		{
			return;
		}

		for (int i = 0; i < evt.getOriginal().inventory.getSizeInventory(); i++)
		{
			ItemStack stack = (ItemStack) evt.getOriginal().inventory.getStackInSlot(i);
			if (EnchantmentHelper.getEnchantments(stack).containsKey(NeoOres.soulbound))
			{
				if (stack.hasTagCompound() && stack.getTagCompound().hasKey(nbtsoulboundtag))
				{
					NBTTagCompound nbt = stack.getTagCompound();
					nbt.removeTag(nbtsoulboundtag);
					stack.setTagCompound(nbt);
				}

				evt.getEntityPlayer().inventory.setInventorySlotContents(i, stack);
				evt.getOriginal().inventory.setInventorySlotContents(i, ItemStack.EMPTY);
			}
		}
	}

	@SubscribeEvent
	public void onAttackEntity(AttackEntityEvent event)
	{
		if (event.getEntityPlayer() != null)
		{
			Random rand = new Random();
			EntityPlayer player = event.getEntityPlayer();
			ItemStack stack = player.getHeldItemMainhand();
			if (stack.hasTagCompound())
			{
				NBTTagCompound nbt = stack.getTagCompound();
				if (nbt.hasKey(NeoOres.LEGACY) && nbt.getBoolean(NeoOres.LEGACY))
				{
					if (rand.nextInt(8) == 0)
					{
						player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
						if (!player.world.isRemote)
						{
						}
						else
						{
							player.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
						}
					}
				}
			}

			if (player instanceof EntityPlayerMP && !(player instanceof FakePlayer) && event.getTarget() instanceof EntityLivingBase)
			{
				if (NeoOresData.instance != null)
				{
					NeoOresData.instance.getPSD((EntityPlayerMP) player).setAttackingEntity((EntityLivingBase) event.getTarget());
				}
			}
		}
	}

	@SubscribeEvent
	public void onFallen(LivingFallEvent event)
	{
		if (event.getEntityLiving() != null && event.getEntityLiving().isServerWorld())
		{
			SpellUtils.run(event.getEntityLiving().getEntityWorld(), event.getEntityLiving(), event);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	@SideOnly(Side.CLIENT)
	public void onRenderGui(RenderGameOverlayEvent event)
	{
		if (event.getType() == ElementType.EXPERIENCE)
		{
			new GuiNeoGameOverlay(Minecraft.getMinecraft());
		}
		else
		{
			return;
		}
	}

	private boolean rotationInVector(Vec3d vec, Entity source, float yawWidth, float pitchWidth)
	{
		double vecX = vec.x - source.posX;
		double vecY = vec.y - (source.posY + source.getEyeHeight());
		double vecZ = vec.z - source.posZ;
		float sourceYaw = (float) Math.toDegrees(Math.atan(vecX / vecZ));
		if (vecZ < 0.0D)
		{
			sourceYaw = (float) (180.0D - Math.toDegrees(Math.atan(vecX / vecZ + Float.MIN_NORMAL)));
			if (vecX > 0.0D)
			{
				sourceYaw = (float) (-180.0D + Math.toDegrees(Math.atan(vecX / vecZ + Float.MIN_NORMAL)));
			}
		}
		float sourcePitch = (float) Math.toDegrees(Math.atan(-vecY / Math.sqrt(Math.abs(vecX * vecX + vecZ * vecZ + Float.MIN_NORMAL)) + Float.MIN_NORMAL));
		float playerYaw = MathHelper.wrapDegrees(source.rotationYaw);
		float playerPitch = MathHelper.wrapDegrees(source.rotationPitch);

		return (playerYaw - 360.0F < sourceYaw + yawWidth || playerYaw < sourceYaw + yawWidth || playerYaw + 360.0F < sourceYaw + yawWidth)
				&& (sourceYaw - yawWidth < playerYaw - 360.0F || sourceYaw - yawWidth < playerYaw || sourceYaw - yawWidth < playerYaw + 360.0F) && sourcePitch - pitchWidth < playerPitch
				&& playerPitch < sourcePitch + pitchWidth;
	}

	@SubscribeEvent
	public void onLootingLevelEvent(LootingLevelEvent event)
	{
		if (event.getDamageSource() != null && event.getDamageSource() instanceof EntityDamageSourceWithItem)
		{
			EntityDamageSourceWithItem edsw = (EntityDamageSourceWithItem) event.getDamageSource();
			event.setLootingLevel(EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, edsw.getStack()));
		}
	}

	public static List<BlockPos> getBlockPositions(AxisAlignedBB aabb, double margin)
	{
		List<BlockPos> poses = new ArrayList<BlockPos>();
		int minX = MathHelper.floor(aabb.minX - margin);
		int minY = MathHelper.floor(aabb.minY - margin);
		int minZ = MathHelper.floor(aabb.minZ - margin);
		int maxX = MathHelper.floor(aabb.maxX + margin);
		int maxY = MathHelper.floor(aabb.maxY + margin);
		int maxZ = MathHelper.floor(aabb.maxZ + margin);
		for (int x = minX; x <= maxX; x++)
		{
			for (int y = minY; y <= maxY; y++)
			{
				for (int z = minZ; z <= maxZ; z++)
				{
					if (0 <= y && y <= 255)
					{
						poses.add(new BlockPos(x, y, z));
					}
				}
			}
		}
		return poses;
	}

	@SubscribeEvent
	public void onMobGriefing(EntityMobGriefingEvent event)
	{
		if (event.getEntity() != null && event.getEntity() instanceof EntityLivingBase)
		{
			if (((EntityLivingBase) event.getEntity()).isPotionActive(NeoOres.antigriefing))
			{
				event.setResult(Result.DENY);
			}
		}
	}

	@SubscribeEvent
	public void onEnderTeleport(EnderTeleportEvent event)
	{
		if (event.getEntity() != null && event.getEntity() instanceof EntityLivingBase)
		{
			if (((EntityLivingBase) event.getEntity()).isPotionActive(NeoOres.antienderteleport))
			{
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onEntityTargetSet(LivingSetAttackTargetEvent event)
	{
		if (event.getEntity() != null && event.getTarget() != null)
		{
			ServerUtils.resetEntityTarget(event.getEntity());
		}
	}

	@SubscribeEvent
	public void onPotionRemove(PotionRemoveEvent event)
	{
		if (event.getPotion() == NeoOres.flying)
		{
			if (event.getEntityLiving() != null && event.getEntityLiving() instanceof EntityPlayerMP)
			{
				EntityPlayer player = (EntityPlayer) event.getEntityLiving();
				player.capabilities.allowFlying = false;
				player.capabilities.isFlying = false;
				player.sendPlayerAbilities();
			}
		}
	}

	@SubscribeEvent
	public void onPotionExpire(PotionExpiryEvent event)
	{
		if (event.getPotionEffect().getPotion() == NeoOres.flying)
		{
			if (event.getEntityLiving() != null && event.getEntityLiving() instanceof EntityPlayerMP)
			{
				EntityPlayer player = (EntityPlayer) event.getEntityLiving();
				player.capabilities.allowFlying = false;
				player.capabilities.isFlying = false;
				player.sendPlayerAbilities();
			}
		}
	}
}