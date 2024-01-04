package neo_ores.spell.effect;

import java.util.Map;

import neo_ores.api.spell.Spell.SpellEffect;
import neo_ores.main.NeoOres;
import neo_ores.main.NeoOresRegisterEvent;
import neo_ores.spell.SpellItemInterfaces.HasDamageLevel;
import neo_ores.spell.SpellItemInterfaces.HasLuck;
import neo_ores.spell.SpellItemInterfaces.HasRange;
import neo_ores.util.EntityDamageSourceWithItem;
import neo_ores.util.PlayerManaDataServer;
import neo_ores.util.SpellUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class SpellEarthDamage  extends SpellEffect implements HasRange,HasLuck,HasDamageLevel
{
	private int damageLevel = 0;
	private int luck = 0;
	private int range0 = 0;

	@Override
	public void setLuck(int value) {
		luck = value;
	}

	@Override
	public void setRange(int value) {
		this.range0 = value;
	}

	@Override
	public void onEffectRunToSelfAndOther(World world, EntityLivingBase runner, RayTraceResult result, ItemStack stack) 
	{
		if(result != null && result.typeOfHit == Type.ENTITY)
		{
			ItemStack item = stack.copy();
			if(this.luck > 0)
			{
				item.addEnchantment(Enchantments.LOOTING, this.luck);
			}
			
			Entity entity = (Entity)result.entityHit;
			
			if(range0 > 0)
			{
				int range = range0 * 2;
				for(Entity elb : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(entity.posX - range,entity.posY - range,entity.posZ - range,entity.posX + range,entity.posY + range,entity.posZ + range)))
				{
					if(elb != entity && elb != runner)
					{
						this.onDamage(world,elb, runner,item);
					}
				}
			}
			
			this.onDamage(world,entity, runner,item);
			
			Map<Enchantment,Integer> enchs = EnchantmentHelper.getEnchantments(item);
			if(enchs.containsKey(Enchantments.LOOTING))
			{
				enchs.remove(Enchantments.LOOTING);
			}
			if(item.hasTagCompound())item.getTagCompound().removeTag("ench");;
			
			for(Map.Entry<Enchantment,Integer> entry : enchs.entrySet())
			{
				item.addEnchantment(entry.getKey(), entry.getValue());
			}
		}
		
	}
	
	private void onDamage(World world,Entity elb,EntityLivingBase runner,ItemStack stack)
	{
		if(elb.canBeCollidedWith())
		{
			elb.attackEntityFrom(EntityDamageSourceWithItem.setDamageByEntityWithItem(NeoOres.EARTH,runner,stack), (int)(3.5 * Math.pow(1.5,this.damageLevel)) + 3);
			if(world.isRemote) SpellUtils.onDisplayParticleTypeAEntity(world, elb,NeoOresRegisterEvent.particle0, SpellUtils.getColor(stack), 16);
			if(runner instanceof EntityPlayerMP)
			{
				PlayerManaDataServer pmds = new PlayerManaDataServer((EntityPlayerMP)runner);
				pmds.addMXP(10L + (long)Math.pow(3,luck));	
			}
		}
	}

	@Override
	public void setDamageLevel(int value) 
	{
		this.damageLevel = value;
		
	}

	@Override
	public void onEffectRunToSelf(World world, EntityLivingBase runner, ItemStack stack) {}

	@Override
	public void onEffectRunToOther(World world, RayTraceResult result, ItemStack stack) {}

	@Override
	public void initialize() {
		damageLevel = 0;
		luck = 0;
		range0 = 0;
	}
}
