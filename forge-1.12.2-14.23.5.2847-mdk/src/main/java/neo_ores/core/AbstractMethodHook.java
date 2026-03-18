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
				if (behaveReplace()) 
				{
					preHook(mv);
					super.visitMethodInsn(getOpcodes(), getHookClassName(), getHookMethodName(), hookDescriptor(), isTransfromingInterface());
					postHook(mv);
				} 
				else
				{
					if (isPre())
					{
						preHook(mv);
						super.visitMethodInsn(getOpcodes(), getHookClassName(), getHookMethodName(), hookDescriptor(), isTransfromingInterface());
						postHook(mv);
					}
					super.visitMethodInsn(opcode, owner, methodName, desc, it);
					if (!isPre())
					{
						preHook(mv);
						super.visitMethodInsn(getOpcodes(), getHookClassName(), getHookMethodName(), hookDescriptor(), isTransfromingInterface());
						postHook(mv);
					}
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
	
	public String hookDescriptor() 
	{
		return Type.getMethodDescriptor(Type.VOID_TYPE);
	}
	
	public void preHook(MethodVisitor mv) 
	{
	}
	
	public void postHook(MethodVisitor mv) 
	{
	}

	/**
	 * @return Whether this hook fire at pre or post
	 */
	public boolean isPre()
	{
		return true;
	}
	
	public boolean behaveReplace() 
	{
		return false;
	}
	
	public boolean isTransfromingInterface() 
	{
		return false;
	}
}
