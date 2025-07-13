package neo_ores.core;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public abstract class AbstractMethodHook extends AbstractMethodChanger
{
	@Override
	public MethodVisitor getVisitor(MethodVisitor mv)
	{
		return new MethodVisitor(Opcodes.ASM4, mv)
		{
			@Override
			public void visitMethodInsn(int opcode, String owner, String methodName, String desc, boolean it)
			{
				if (isPre())
				{
					super.visitMethodInsn(Opcodes.INVOKESTATIC, getHookClassName(), getHookMethodName(), Type.getMethodDescriptor(Type.VOID_TYPE), false);
				}
				super.visitMethodInsn(opcode, owner, methodName, desc, it);
				if (!isPre())
				{
					super.visitMethodInsn(Opcodes.INVOKESTATIC, getHookClassName(), getHookMethodName(), Type.getMethodDescriptor(Type.VOID_TYPE), false);
				}
			}
		};
	}

	public int getOpcodes()
	{
		return Opcodes.INVOKESTATIC;
	}

	public abstract String getHookClassName();

	public abstract String getHookMethodName();

	/**
	 * @return Whether this hook fire at pre or post
	 */
	public boolean isPre()
	{
		return true;
	}
}
