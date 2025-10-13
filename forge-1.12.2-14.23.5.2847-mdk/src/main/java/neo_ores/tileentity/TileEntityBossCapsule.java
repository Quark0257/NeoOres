package neo_ores.tileentity;

import java.util.UUID;

import javax.annotation.Nullable;

import neo_ores.entity.boss.IBoundingPos;
import neo_ores.item.ItemBossKey;
import neo_ores.main.NeoOresItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TileEntityBossCapsule extends TileEntity
{
	private UUID uuidKey = null;
	private String id = "";
	private NBTTagCompound entityTag = null;

	public TileEntityBossCapsule()
	{
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if (compound.hasKey("uuidKeyMost") && compound.hasKey("uuidKeyLeast"))
		{
			this.uuidKey = compound.getUniqueId("uuidKey");
		}
		this.id = compound.getString("entityId");
		if (compound.hasKey("entityTag"))
		{
			this.entityTag = compound.getCompoundTag("entityTag");
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		if (this.uuidKey != null)
		{
			compound.setUniqueId("uuidKey", this.uuidKey);
		}
		compound.setString("entityId", this.id);
		if (this.entityTag != null)
		{
			compound.setTag("entityTag", this.entityTag);
		}

		return compound;
	}

	public void setEntity(@Nullable UUID uuidKey, String id, @Nullable NBTTagCompound entityTag)
	{
		this.uuidKey = uuidKey;
		this.id = id;
		this.entityTag = entityTag;
		this.markDirty();
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (hitY < 1.0)
		{
			return false;
		}
		
		if (this.uuidKey != null) 
		{
			ItemStack stack = playerIn.getHeldItem(hand);
			if (stack.getItem() != NeoOresItems.boss_key) 
			{
				ITextComponent itextcomponent = new TextComponentTranslation("chat.needBossKey");
				if (playerIn != null)
					playerIn.sendStatusMessage(itextcomponent, true);
				return false;
			}
			
			ItemBossKey bossKey = (ItemBossKey) stack.getItem();
			if (!bossKey.hasKey(stack) || !bossKey.getKey(stack).equals(this.uuidKey)) 
			{
				ITextComponent itextcomponent = new TextComponentTranslation("chat.notMatchKey");
				if (playerIn != null)
					playerIn.sendStatusMessage(itextcomponent, true);
				return false;
			}
			
			stack.shrink(1);
		}
		
		if (!worldIn.isRemote) 
		{
			worldIn.setBlockToAir(pos);
			Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(this.id), worldIn);
			
			if (entity == null) 
			{
				return true;
			}
			
			if (this.entityTag != null) 
			{
				entity.deserializeNBT(this.entityTag);
			}
			entity.setPositionAndRotation(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, entity.rotationYaw, entity.rotationPitch);
			if (entity instanceof IBoundingPos) 
			{
				((IBoundingPos) entity).setBound(new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D));
			}
			worldIn.spawnEntity(entity);
		}

		return true;
	}
}
