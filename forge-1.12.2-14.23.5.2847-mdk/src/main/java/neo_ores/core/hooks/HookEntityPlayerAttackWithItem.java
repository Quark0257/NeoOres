package neo_ores.core.hooks;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import neo_ores.core.AbstractLocalVarChanger;
import neo_ores.core.CoreConfigManager;
import neo_ores.core.NeoOresTransformer;

public class HookEntityPlayerAttackWithItem extends AbstractLocalVarChanger
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
				if (count == CoreConfigManager.getInstance().getConfig().playerAttacksEntityHook[0])
				{
					super.visitMethodInsn(getOpcodes(), getHookClassName(), getHookMethodName(), hookDescriptor(), isTransfromingInterface());
				}
				super.visitVarInsn(code, index);
				count++;
			}

			@Override
			public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf)
			{
				if (methodCount == CoreConfigManager.getInstance().getConfig().playerAttacksEntityHook[1] || methodCount == CoreConfigManager.getInstance().getConfig().playerAttacksEntityHook[2])
				{
					// Removing methods
				}
				else
				{
					super.visitMethodInsn(opcode, owner, name, desc, itf);
				}
				methodCount++;
			}
		};
	}
	
	public int getOpcodes()
	{
		return Opcodes.INVOKESTATIC;
	}
	
	public boolean isTransfromingInterface() 
	{
		return false;
	}

	public String getHookClassName()
	{
		return "neo_ores/event/EntityAttackEvent";
	}

	public String getHookMethodName()
	{
		return "attackWithItem";
	}

	public String hookDescriptor()
	{
		return NeoOresTransformer.toDesc(boolean.class, "net.minecraft.entity.Entity", "net.minecraft.entity.player.EntityPlayer", float.class);
	}

	@Override
	public String getTargetMethodName()
	{
		return "attackTargetEntityWithCurrentItem";
	}

	@Override
	public String getTargetMethodObfuscationName()
	{
		return "func_71059_n";
	}

	@Override
	public String getDesc()
	{
		return NeoOresTransformer.toDesc(void.class, "net.minecraft.entity.Entity");
	}

	@Override
	public String getClassName()
	{
		return "net.minecraft.entity.player.EntityPlayer";
	}
	
	public int getAcceptFlag() 
	{
		return 0;
	}
}
