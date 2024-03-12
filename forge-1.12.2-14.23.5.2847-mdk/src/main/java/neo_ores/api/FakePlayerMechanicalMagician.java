package neo_ores.api;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class FakePlayerMechanicalMagician extends FakePlayer implements IMagicExperienceContainer
{
	public static final GameProfile neo_ores_profile = new GameProfile(UUID.fromString("43b3f040-0d77-4416-8c8c-f7cff76a88dd"), "[Neo Ores]");

	private long magic_xp;

	public FakePlayerMechanicalMagician(WorldServer world, BlockPos pos, BlockPos distination)
	{
		super(world, neo_ores_profile);
		this.magic_xp = 0L;
		this.posX = pos.getX() + 0.5F;
		this.posY = pos.getY() + 0.5F;
		this.posZ = pos.getZ() + 0.5F;
		this.lastTickPosX = pos.getX() + 0.5F;
		this.lastTickPosY = pos.getY() + 0.5F;
		this.lastTickPosZ = pos.getZ() + 0.5F;
		Vec2f direction = MathUtils.getYawPitch(distination.getX() - pos.getX(), distination.getY() - pos.getY(), distination.getZ() - pos.getZ());
		this.rotationPitch = direction.y;
		this.rotationYaw = direction.x;
	}
	
	public BlockPos getBlockPos() {
		return new BlockPos(MathHelper.floor(this.posX),MathHelper.floor(this.posY),MathHelper.floor(this.posZ));
	}

	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);

		this.magic_xp = compound.getLong("magicXp");
	}

	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);

		compound.setLong("magicXp", this.magic_xp);
	}

	@Override
	public void setMagicXp(long value)
	{
		magic_xp = value;
	}

	@Override
	public Vec3d getPositionVector()
	{
		return new Vec3d(this.posX, this.posY, this.posZ);
	}

	@Override
	public long getMagicXp()
	{
		return magic_xp;
	}
	
	public float getEyeHeight() {
		return 0.0F;
	}
}
