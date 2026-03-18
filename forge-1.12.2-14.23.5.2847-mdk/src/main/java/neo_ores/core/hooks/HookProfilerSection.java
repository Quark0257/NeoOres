package neo_ores.core.hooks;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import neo_ores.core.AbstractMethodHook;
import neo_ores.core.NeoOresTransformer;

public class HookProfilerSection extends AbstractMethodHook
{
	@Override
	public String getHookClassName()
	{
		return "neo_ores/core/hooks/HookProfilerSection";
	}

	@Override
	public String getHookMethodName()
	{
		return "hookEvent";
	}

	@Override
	public String getTargetMethodName()
	{
		return "endStartSection";
	}

	@Override
	public String getTargetMethodObfuscationName()
	{
		return "func_76318_c";
	}

	@Override
	public String getDesc()
	{
		return NeoOresTransformer.toDesc(void.class, String.class);
	}

	@Override
	public String getClassName()
	{
		return "net.minecraft.profiler.Profiler";
	}
	
	public static void hookEvent(String string)
	{
		// MinecraftForge.EVENT_BUS.post(new EventProfilerEndStart(string));
	}
	
	public String hookDescriptor() 
	{
		return NeoOresTransformer.toDesc(void.class, String.class);
	}
	
	public void preHook(MethodVisitor mv)
	{
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		// mv.visitLdcInsn();
	}
}
