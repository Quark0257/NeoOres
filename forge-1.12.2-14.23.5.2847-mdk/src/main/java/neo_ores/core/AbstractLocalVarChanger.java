package neo_ores.core;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public abstract class AbstractLocalVarChanger extends AbstractClassAdaptor
{
	@Override
	public ClassVisitor getVisitor(ClassWriter cw)
	{
		return new ClassVisitor(Opcodes.ASM4, cw)
		{

			@Override
			public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
			{
				if ((getTargetMethodName().equals(NeoOresTransformer.mapMethodName(AbstractLocalVarChanger.this.getClassName(), name, desc))
						|| getTargetMethodObfuscationName().equals(NeoOresTransformer.mapMethodName(AbstractLocalVarChanger.this.getClassName(), name, desc))) && getDesc().equals(desc))
				{
					return getVisitor(super.visitMethod(access, name, desc, signature, exceptions), access, name, desc);
				}
				return super.visitMethod(access, name, desc, signature, exceptions);
			}
		};
	}

	public abstract String getTargetMethodName();

	public abstract String getTargetMethodObfuscationName();

	public abstract String getDesc();

	public abstract MethodVisitor getVisitor(MethodVisitor mv, int access, String name, String desc);
}
