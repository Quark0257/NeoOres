package neo_ores.core;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.*;

public abstract class AbstractLocalVariableHook extends AbstractLocalVarChanger
{
	@Override
	public MethodVisitor getVisitor(MethodVisitor mv, int access, String name, String desc)
	{
		return new AdviceAdapter(Opcodes.ASM4, mv, access, name, desc)
		{	
			private int count = 0;
			
			public void visitVarInsn(int code, int index) 
			{
				test(code, index, count);
				super.visitVarInsn(code, index);
				if (count == getApplyTimingCount()) 
				{
					preHook(mv);
					this.visitMethodInsn(getOpcodes(), getHookClassName(), getHookMethodName(), hookDescriptor(), isTransfromingInterface());
					super.visitVarInsn(getLocalVarType().getOpcode(Opcodes.ISTORE), getLocalVarIndex());
				}
				count++;
			}
		};
	}
	
	public int getLocalVarTiming() 
	{
		return Opcodes.ILOAD;
	}
	
	public int getLocalVarOpcode() 
	{
		return getLocalVarType().getOpcode(getLocalVarTiming());
	}
	
	public void loadPreviousValue(MethodVisitor mv) 
	{
		mv.visitVarInsn(getLocalVarType().getOpcode(Opcodes.ILOAD), getLocalVarIndex());
	}
	
	public void test(int code, int index, int count) 
	{
		// System.out.println(count + ":" + code + ":" + index);
	}
	
	public abstract int getApplyTimingCount();
	
	public abstract int getLocalVarIndex();
	
	public abstract Type getLocalVarType();

	public int getOpcodes()
	{
		return Opcodes.INVOKESTATIC;
	}

	public abstract String getHookClassName();

	public abstract String getHookMethodName();
	
	public abstract String hookDescriptor();
	
	public void preHook(MethodVisitor mv) 
	{
	}
	
	public boolean isTransfromingInterface() 
	{
		return false;
	}
	
	public int getAcceptFlag() 
	{
		return ClassReader.EXPAND_FRAMES;
	}
}
