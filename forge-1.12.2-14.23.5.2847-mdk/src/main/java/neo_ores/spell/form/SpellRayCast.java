package neo_ores.spell.form;

import neo_ores.api.spell.Spell.SpellFormNotEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

//Target is on Ray or BlockSide; NOT entity living or block targeted; Pull, Push, Pipe, PlaceBlock, SetPosToItemEntity.... also Touch functions applied(Damages or Dig)
public class SpellRayCast extends SpellFormNotEntity
{
	@Override
	public boolean needConditional()
	{
		return false;
	}

	@Override
	public void onSpellRunningServer(World world, EntityLivingBase runner, ItemStack stack, RayTraceResult result, NBTTagCompound spells)
	{
		// TODO Auto-generated method stub
		// DO TOUCH rayCast is needed
	}
}
