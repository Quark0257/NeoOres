package neo_ores.event;

import java.util.ListIterator;
import java.util.Random;

import neo_ores.gui.GuiNeoGameOverlay;
import neo_ores.items.ItemPaxel;
import neo_ores.main.NeoOres;
import neo_ores.main.Reference;
import neo_ores.mana.PlayerManaDataServer;
import neo_ores.mana.packet.PacketManaDataToClient;
import neo_ores.spell.SpellUtils;
import neo_ores.study.StudyItemManagerServer;
import neo_ores.world.dimension.FromAirTeleporter;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.Teleporter;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class NeoPlayerEvent 
{	
	public static final String nbtsoulboundtag = "soulboundslot";
	
	/*
	@SubscribeEvent(priority=EventPriority.HIGH)
	public void onRightClickItem(PlayerInteractEvent.RightClickItem event)
	{
		if(!event.getEntityPlayer().world.isRemote && event.getEntityPlayer() != null && event.getItemStack().getItem() instanceof ItemArmor && event.getItemStack().equals(event.getEntityPlayer().getHeldItemMainhand()))
		{
			ItemArmor armor = (ItemArmor)event.getItemStack().getItem();
			ItemStack headItem = event.getEntityPlayer().getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			if(headItem != ItemStack.EMPTY && armor.getEquipmentSlot() == EntityEquipmentSlot.HEAD && headItem.getTagCompound() != null)
			{
				NBTTagList headNBTList = headItem.getTagCompound().getTagList("neo_ores", 10);
				if(headNBTList != null && headNBTList.getCompoundTagAt(0) != null)
				{
					ItemStack item = event.getItemStack().copy();
					NBTTagCompound headNBT = headNBTList.getCompoundTagAt(0);
					if(headNBT.hasKey("ML") && headNBT.hasKey("maxMP") && headNBT.hasKey("MP") && headNBT.hasKey("MXP") && headNBT.hasKey("isSoulBound"))
					{
						//set tag of held item from head item and put held item on head 
						int ML = headNBT.getInteger("ML");
						int maxMP = headNBT.getInteger("maxMP");
						int MP = headNBT.getInteger("MP");
						int MXP = headNBT.getInteger("MXP");
						int isSoulBound = headNBT.getInteger("isSoulBound");
						event.getEntityPlayer().inventory.setInventorySlotContents(39,item);
						ItemStack headItemFromHand = event.getEntityPlayer().getItemStackFromSlot(EntityEquipmentSlot.HEAD);
						if(headItemFromHand.getTagCompound() == null)
						{
							headItemFromHand.setTagCompound(new NBTTagCompound());
						}

						if(!headItemFromHand.getTagCompound().hasKey("neo_ores", 9))
						{
							headItemFromHand.getTagCompound().setTag("neo_ores", new NBTTagList());
						}
						NBTTagList tag = headItemFromHand.getTagCompound().getTagList("neo_ores", 10);
						NBTTagCompound itemNBT = new NBTTagCompound();
						itemNBT.setInteger("ML", ML);
						itemNBT.setInteger("maxMP", maxMP);
						itemNBT.setInteger("MP", MP);
						itemNBT.setInteger("MXP", MXP);
						itemNBT.setInteger("isSoulBound", isSoulBound);
						if(tag.tagCount() > 0)
						{
							tag.set(0, itemNBT);
						}
						else
						{
							tag.appendTag(itemNBT);
						}
						headItemFromHand.getTagCompound().setBoolean("Unbreakable", true);
						headItemFromHand.addEnchantment(Enchantment.getEnchantmentByID(10), 1);
						//clear held item
						NBTTagCompound nbtClear = new NBTTagCompound();
						nbtClear.setBoolean("clear", true);
						event.getEntityPlayer().getHeldItemMainhand().setTagCompound(nbtClear);
						event.getEntityPlayer().inventory.clearMatchingItems(event.getEntityPlayer().getHeldItemMainhand().getItem(), event.getEntityPlayer().getHeldItemMainhand().getMetadata(), event.getEntityPlayer().getHeldItemMainhand().getCount(), event.getEntityPlayer().getHeldItemMainhand().getTagCompound());
						//give old head item and clear tag
						NBTTagCompound headNBTOld = headItem.getTagCompound();
						headNBTOld.removeTag("ench");
						headNBTOld.removeTag("neo_ores");
						if(isSoulBound == 1)
						{
							headNBTOld.removeTag("Unbreakable");
						}
						event.getEntityPlayer().addItemStackToInventory(headItem.copy());
					}
				}
			}
		}
	}
	*/

	//TODO Fire:Reflection,Water:CancelDamage,Earth:CancelDeath(MP:N%),Air:CreativeFlying(with Increase Speed)
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityWasAttacked(LivingHurtEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer && event.getSource() instanceof EntityDamageSource)
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			if(!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemPaxel)
			{
				ItemPaxel paxel = (ItemPaxel)player.getHeldItem(EnumHand.MAIN_HAND).getItem();
				if(paxel.isShielding())
				{	
					if(this.rotationInVector(event.getSource().getDamageLocation(), player, 70.0F, 50.0F))
					{
						event.setAmount(event.getAmount() / 2.0F);
						player.getHeldItem(EnumHand.MAIN_HAND).damageItem(1, player);
					}
				}
				paxel.setShielding(false);
				
				if(event.getAmount() <= 0.0F)
				{
					paxel.setShielded(false);
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityKnockback(LivingKnockBackEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			if(!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemPaxel)
			{
				ItemPaxel paxel = (ItemPaxel)player.getHeldItem(EnumHand.MAIN_HAND).getItem();
				if(paxel.wasShielding())
				{	
					if(this.rotationInVector(new Vec3d(event.getAttacker().posX,event.getAttacker().posY,event.getAttacker().posZ), player, 70.0F, 50.0F))
					{
						event.setStrength(event.getStrength() / 2);
					}
				}
				paxel.setShielded(false);
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingEvent(LivingEvent event)
	{	
		if(event.getEntity() != null)
		{
			if(!event.getEntity().getEntityWorld().isRemote && event.getEntity() instanceof EntityPlayerMP)
			{
			
				EntityPlayerMP playermp = (EntityPlayerMP)event.getEntity();
				PlayerManaDataServer pmd = new PlayerManaDataServer(playermp);
				StudyItemManagerServer sim = new StudyItemManagerServer(playermp);
			
				if(playermp.ticksExisted % 40 == 0)
				{
					pmd.addMana((long)((float)pmd.getLevel() * ((float)pmd.getLevel() * 0.0001F) + 1.0F));
					//System.out.println(SpellUtils.getItemStackNBTFromList(Arrays.asList(new SpellItemManager[] {NeoOres.spell_dig,NeoOres.spell_touch}), new NBTTagCompound()).toString());
				}
				
				NBTTagCompound packetNBT = new NBTTagCompound();
				NBTTagCompound neo_ores = new NBTTagCompound();
          		NBTTagCompound magicData = new NBTTagCompound();
          		NBTTagCompound studyData = sim.alreadyGottenList().copy();
          		
          		magicData.setLong(SpellUtils.NBTTagUtils.LEVEL, pmd.getLevel());
          		magicData.setLong(SpellUtils.NBTTagUtils.MXP, pmd.getMXP());
          		magicData.setLong(SpellUtils.NBTTagUtils.MANA,pmd.getMana());
          		magicData.setLong(SpellUtils.NBTTagUtils.MAX_MANA, pmd.getMaxMana());
          		magicData.setLong(SpellUtils.NBTTagUtils.MAX_MXP, pmd.getMaxMXP());
          		magicData.setLong(SpellUtils.NBTTagUtils.MAGIC_POINT,pmd.getMagicPoint());
          		
          		neo_ores.setTag(SpellUtils.NBTTagUtils.MAGIC, (NBTBase)magicData);
          		neo_ores.setTag(SpellUtils.NBTTagUtils.STUDY, (NBTBase)studyData);
          		
          		packetNBT.setInteger("playerID", playermp.getEntityId());
          		packetNBT.setTag(Reference.MOD_ID, (NBTBase)neo_ores);
				
        		PacketManaDataToClient pmdc = new PacketManaDataToClient(packetNBT);
        		NeoOres.PACKET.sendToAll((IMessage)pmdc);
			}
			
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
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onLivingDeath(LivingDeathEvent event)
	{
		if ((event.getEntityLiving() == null) || ((event.getEntityLiving() instanceof FakePlayer)) || (event.isCanceled())) 
	    {
	    	return;
	    }
	    if (event.getEntityLiving().getEntityWorld().getGameRules().getBoolean("keepInventory")) 
	    {
	    	return;
	    }
		
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			for(int i = 0;i < player.inventory.getSizeInventory();i++)
			{
				if(EnchantmentHelper.getEnchantments(player.inventory.getStackInSlot(i)).containsKey(NeoOres.soulbound))
				{
					NBTTagCompound nbt = player.inventory.getStackInSlot(i).getTagCompound();
					nbt.setInteger(nbtsoulboundtag, i);
					player.inventory.getStackInSlot(i).setTagCompound(nbt);
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
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
	    
	    for(int i = 0;i < event.getEntityPlayer().inventory.mainInventory.size();i++)
	    {
	    	if(event.getEntityPlayer().inventory.mainInventory.get(i).isEmpty())
	    	{
	    		space++;
	    	}
	    }
	    
	    ListIterator<EntityItem> iter = event.getDrops().listIterator();
	    while (iter.hasNext())
	    {
	    	EntityItem ei = (EntityItem)iter.next();
	    	ItemStack stack = ei.getItem();
	    	if(EnchantmentHelper.getEnchantments(stack).containsKey(NeoOres.soulbound))
	    	{
	    		if(count < space)
	    		{
	    			if(stack.hasTagCompound() && stack.getTagCompound().hasKey(nbtsoulboundtag))
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
	    			if(stack.hasTagCompound() && stack.getTagCompound().hasKey(nbtsoulboundtag))
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
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
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
	    
	    for(int i = 0;i < evt.getOriginal().inventory.getSizeInventory();i++)
	    {
	    	ItemStack stack = (ItemStack)evt.getOriginal().inventory.getStackInSlot(i);
	    	if(EnchantmentHelper.getEnchantments(stack).containsKey(NeoOres.soulbound))
	    	{
	    		if(stack.hasTagCompound() && stack.getTagCompound().hasKey(nbtsoulboundtag))
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
		if(event.getEntityPlayer() != null)
		{
			Random rand = new Random();
			EntityPlayer player = event.getEntityPlayer();
			ItemStack stack = player.getHeldItemMainhand();
			if(stack.hasTagCompound())
			{
				NBTTagCompound nbt = stack.getTagCompound();
				if(nbt.hasKey(NeoOres.LEGACY) && nbt.getBoolean(NeoOres.LEGACY))
				{
					if(rand.nextInt(8) == 0)
					{
						player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
						if(!player.world.isRemote)
						{
						}
						else
						{
							player.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
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
	
	private boolean rotationInVector(Vec3d vec,Entity source,float yawWidth,float pitchWidth)
	{
		double vecX = vec.x - source.posX;
		double vecY = vec.y - (source.posY + source.getEyeHeight());
		double vecZ = vec.z - source.posZ;
		float sourceYaw = (float)Math.toDegrees(Math.atan(vecX / vecZ));
		if(vecZ < 0.0D)
		{
			sourceYaw = (float)(180.0D - Math.toDegrees(Math.atan(vecX / vecZ + Float.MIN_NORMAL)));
			if(vecX > 0.0D)
			{
				sourceYaw = (float)(-180.0D + Math.toDegrees(Math.atan(vecX / vecZ + Float.MIN_NORMAL)));
			}			
		}
		float sourcePitch = (float)Math.toDegrees(Math.atan(-vecY / Math.sqrt(Math.abs(vecX * vecX + vecZ * vecZ + Float.MIN_NORMAL)) + Float.MIN_NORMAL));
		float playerYaw = MathHelper.wrapDegrees(source.rotationYaw);
		float playerPitch = MathHelper.wrapDegrees(source.rotationPitch);
				
		return (playerYaw - 360.0F < sourceYaw + yawWidth || playerYaw < sourceYaw + yawWidth || playerYaw + 360.0F < sourceYaw + yawWidth) && (sourceYaw - yawWidth < playerYaw - 360.0F || sourceYaw - yawWidth < playerYaw || sourceYaw - yawWidth < playerYaw + 360.0F) && sourcePitch - pitchWidth < playerPitch && playerPitch < sourcePitch + pitchWidth;
	}
}