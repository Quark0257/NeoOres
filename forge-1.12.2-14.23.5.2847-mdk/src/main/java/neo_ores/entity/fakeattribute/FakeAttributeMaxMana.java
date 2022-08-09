package neo_ores.entity.fakeattribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import neo_ores.api.PlayerManaDataServer;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class FakeAttributeMaxMana
{
	private final EntityPlayerMP player;
	private final PlayerManaDataServer pmd;
	public static final IAttribute MAX_MANA = new FakeAttributeMaxMana.AttributeMaxMana();
	
	public FakeAttributeMaxMana(EntityPlayerMP target)
	{
		player = target;
		pmd = new PlayerManaDataServer(target);
	}
	
	public FakeAttributeMaxMana initialize()
	{
		this.pmd.setMaxManaAdd(0);
		this.pmd.setMaxManaMag(0.0F);
		return this;
	}
	
	public void modifierToPlayer(long addition,float magnification)
	{	
		if(this.player.getAttributeMap().getAttributeInstance(MAX_MANA) == null) this.player.getAttributeMap().registerAttribute(MAX_MANA);
		this.player.getAttributeMap().getAttributeInstance(MAX_MANA).applyModifier(new AttributeModifier("neo_ores.maxMana", addition, 0));
		this.player.getAttributeMap().getAttributeInstance(MAX_MANA).applyModifier(new AttributeModifier("neo_ores.maxMana", magnification, 1));
	}
	
	public void modifierToPlayerWithUUID(UUID uid,double value,int op)
	{	
		if(this.player.getAttributeMap().getAttributeInstance(MAX_MANA) == null) this.player.getAttributeMap().registerAttribute(MAX_MANA);
		this.player.getAttributeMap().getAttributeInstance(MAX_MANA).applyModifier(new AttributeModifier(uid,"neo_ores.maxMana", value, op));
	}
	
	public AttributeModifier getModifierByUUID(UUID uid)
	{	
		if(this.player.getAttributeMap().getAttributeInstance(MAX_MANA) == null) this.player.getAttributeMap().registerAttribute(MAX_MANA);
		return this.player.getAttributeMap().getAttributeInstance(MAX_MANA).getModifier(uid);
	}
	
	public void removeModifierByUUID(UUID uid)
	{	
		if(this.player.getAttributeMap().getAttributeInstance(MAX_MANA) == null) this.player.getAttributeMap().registerAttribute(MAX_MANA);
		this.player.getAttributeMap().getAttributeInstance(MAX_MANA).removeModifier(uid);;
	}
	
	public void applyToPlayer()
	{
		long addition = 0L;
		float magnification = 0F;
		for(EntityEquipmentSlot slot : EntityEquipmentSlot.values())
		{
			ItemStack trace = this.player.getItemStackFromSlot(slot).copy();
			for(Map.Entry<String,AttributeModifier> map : trace.getAttributeModifiers(slot).entries())
			{
				if(map.getKey().equals("neo_ores.maxMana"))
				{
					if(map.getValue().getOperation() == 0)
					{
						addition += (long)map.getValue().getAmount();
					}
					else
					{
						magnification += (float)map.getValue().getAmount();
					}
				}
			}
		}
		
		if(this.player.getAttributeMap().getAttributeInstance(MAX_MANA) != null)
		{
			for(AttributeModifier am : this.player.getAttributeMap().getAttributeInstance(MAX_MANA).getModifiers())
			{
				if(am.getOperation() == 0)
				{
					addition += (long)am.getAmount();
				}
				else
				{
					magnification += (float)am.getAmount();
				}
			}
		}
		if(magnification < -1) magnification = -1;
		if(this.pmd.getMaxManaAdd() != addition || this.pmd.getMaxManaMag() != magnification)
		{
			this.pmd.setMaxManaAdd(addition);
			this.pmd.setMaxManaMag(magnification);
			if(this.pmd.getMana() > this.pmd.getMaxMana())
			{
				this.pmd.setMana(this.pmd.getMaxMana());
			}
			
			this.pmd.sendToClient();
		}
	}
	
	public List<AttributeModifier> getModifiers()
	{
		List<AttributeModifier> list = new ArrayList<AttributeModifier>();
		for(EntityEquipmentSlot slot : EntityEquipmentSlot.values())
		{
			ItemStack trace = this.player.getItemStackFromSlot(slot).copy();
			for(Map.Entry<String,AttributeModifier> map : trace.getAttributeModifiers(slot).entries())
			{
				if(map.getKey().equals("neo_ores.maxMana"))
				{
					list.add(map.getValue());
				}
			}
		}
		
		if(this.player.getAttributeMap().getAttributeInstance(MAX_MANA) != null)
		{
			for(AttributeModifier am : this.player.getAttributeMap().getAttributeInstance(MAX_MANA).getModifiers())
			{
				list.add(am);
			}
		}
		
		return list;
	}
	
	public static void setToStack(ItemStack stack,EntityEquipmentSlot equipmentSlot,long addition,float magnification)
	{
		stack.addAttributeModifier("neo_ores.maxMana", new AttributeModifier("neo_ores.maxMana", addition, 0), equipmentSlot);
		stack.addAttributeModifier("neo_ores.maxMana", new AttributeModifier("neo_ores.maxMana", magnification, 1), equipmentSlot);
	}
	
	public static boolean hasModifier(ItemStack stack,EntityEquipmentSlot slot)
	{
		for(Map.Entry<String,AttributeModifier> map : stack.getAttributeModifiers(slot).entries())
		{
			if(map.getKey().equals("neo_ores.maxMana")) return true;
		}
		return false;
	}

	private static class AttributeMaxMana implements IAttribute
	{
		@Override
		public String getName() 
		{
			return "neo_ores.maxMana";
		}

		@Override
		public double clampValue(double value) 
		{
			return value;
		}

		@Override
		public double getDefaultValue() 
		{
			return 1.0D;
		}

		@Override
		public boolean getShouldWatch() 
		{
			return true;
		}

		@Override
		public IAttribute getParent() 
		{
			return null;
		}
	}
}
