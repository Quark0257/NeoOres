package neo_ores.core.hooks;

import neo_ores.core.AbstractMethodHook;
import neo_ores.core.NeoOresTransformer;
import net.minecraft.client.renderer.GlStateManager;

public class DepthFixer extends AbstractMethodHook
{
	@Override
	public String getClassName()
	{
		return "net.minecraft.client.particle.ParticleManager";
	}

	@Override
	public String getHookClassName()
	{
		return "neo_ores/core/hooks/DepthFixer";
	}

	@Override
	public String getHookMethodName()
	{
		return "depthMaskFix";
	}

	@Override
	public String getTargetMethodName()
	{
		return "renderParticles";
	}

	@Override
	public String getTargetMethodObfuscationName()
	{
		return "func_78874_a";
	}

	@Override
	public String getDesc()
	{
		return NeoOresTransformer.toDesc(void.class, "net.minecraft.entity.Entity", float.class);
	}
	
	public static void depthMaskFix()
	{
		GlStateManager.depthMask(true);
	}
}
