package neo_ores.api;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class FakePlayerNeoOres extends FakePlayer implements IMagicExperienceContainer
{
	public static final GameProfile neo_ores_profile = new GameProfile(UUID.fromString("43b3f040-0d77-4416-8c8c-f7cff76a88dd"),"[Neo Ores]");
	
	private long magic_xp;
	
	public FakePlayerNeoOres(WorldServer world) 
	{
		super(world, neo_ores_profile);
	}

	@Override
	public void setMagicXp(long value) {
		magic_xp = value;
	}

	@Override
	public long getMagicXp() {
		return magic_xp;
	}
}
