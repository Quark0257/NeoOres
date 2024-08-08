package neo_ores.api;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import neo_ores.tileentity.TileEntityMechanicalMagician;
import neo_ores.util.PlayerMagicData;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class FakePlayerMechanicalMagician extends FakePlayer implements IMagicContainer, ILifeContainer, HasInventory
{
	public static final GameProfile neo_ores_profile = new GameProfile(UUID.fromString("43b3f040-0d77-4416-8c8c-f7cff76a88dd"), "[Neo Ores]");

	private PlayerMagicData pmd;
	private TileEntityMechanicalMagician tileentity;

	public FakePlayerMechanicalMagician(WorldServer world, BlockPos pos, BlockPos distination, TileEntityMechanicalMagician tileentity)
	{
		super(world, neo_ores_profile);
		this.posX = pos.getX() + 0.5F;
		this.posY = pos.getY() + 0.5F;
		this.posZ = pos.getZ() + 0.5F;
		this.lastTickPosX = pos.getX() + 0.5F;
		this.lastTickPosY = pos.getY() + 0.5F;
		this.lastTickPosZ = pos.getZ() + 0.5F;
		Vec2f direction = MathUtils.getYawPitch(distination.getX() - pos.getX(), distination.getY() - pos.getY(), distination.getZ() - pos.getZ());
		this.rotationPitch = direction.y;
		this.rotationYaw = direction.x;
		this.tileentity = tileentity;
	}

	public FakePlayerMechanicalMagician(WorldServer world)
	{
		super(world, neo_ores_profile);
	}

	public BlockPos getBlockPos()
	{
		return new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ));
	}

	@Override
	public Vec3d getPositionVector()
	{
		return new Vec3d(this.posX, this.posY, this.posZ);
	}

	public AxisAlignedBB getEntityBoundingBox()
	{
		return new AxisAlignedBB(this.posX - 0.5, this.posY - 0.5, this.posZ - 0.5, this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5);
	}

	public float getEyeHeight()
	{
		return 0.0F;
	}

	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);

		if (compound.hasKey("pmd"))
		{
			this.pmd = new PlayerMagicData(true);
			this.pmd.readFromNBT(compound.getCompoundTag("pmd"));
		}
	}

	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);

		if (this.pmd != null)
		{
			compound.setTag("pmd", this.pmd.writeToNBT(new NBTTagCompound()));
		}
	}

	@Override
	public void setPMD(PlayerMagicData data)
	{
		this.pmd = data;
	}

	@Override
	public PlayerMagicData getPMD()
	{
		return this.pmd;
	}

	@Override
	public boolean damageWith(DamageSource source, float amount)
	{
		ItemStack totem = this.tileentity.getStackInSlot(1);
		if (totem.getItem().getMaxDamage(totem) == 0)
			return true;
		totem.damageItem((int) Math.min(amount, Integer.MAX_VALUE), this);
		if (amount <= totem.getItem().getMaxDamage(totem) - totem.getItemDamage())
		{
			return true;
		}
		return false;
	}

	public float getFakeMaxHealth()
	{
		ItemStack totem = this.tileentity.getStackInSlot(1);
		return totem.getItem().getMaxDamage(totem);
	}

	@Override
	public IInventory getInventory()
	{
		return this.tileentity;
	}
}
