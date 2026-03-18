package neo_ores.core.hooks;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import neo_ores.core.AbstractLocalVarChanger;
import neo_ores.core.NeoOresTransformer;

public class TestMethodContents extends AbstractLocalVarChanger
{
	@Override
	public MethodVisitor getVisitor(MethodVisitor mv, int access, String name, String desc)
	{
		return new MethodVisitor(Opcodes.ASM4, mv)
		{
			private int count = 0;
			private int methodCount = 0;

			public void visitVarInsn(int code, int index)
			{
				test(code, index, count);
				super.visitVarInsn(code, index);
				count++;
			}

			@Override
			public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf)
			{
				test(methodCount, owner, name);
				super.visitMethodInsn(opcode, owner, name, desc, itf);
				methodCount++;
			}
		};
	}

	public void test(int code, int index, int count)
	{
		System.out.println(count + ":" + code + ":" + index);
	}

	public void test(int methodCount, String owner, String name)
	{
		System.out.println(methodCount + ":" + owner + "#" + name);
	}
	
	public int getAcceptFlag()
	{
		return 0;
	}

	@Override
	public String getTargetMethodName()
	{
		return "attackEntityAsMob";
	}

	@Override
	public String getTargetMethodObfuscationName()
	{
		return "func_70652_k";
	}

	@Override
	public String getDesc()
	{
		return NeoOresTransformer.toDesc(boolean.class, "net.minecraft.entity.Entity");
	}

	@Override
	public String getClassName()
	{
		return "net.minecraft.entity.monster.EntityMob";
	}
}
