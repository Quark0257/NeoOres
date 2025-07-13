package neo_ores.core;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class NeoOresTransformer implements IClassTransformer
{
	private static final AbstractClassAdaptor[] ADAPTORS = new AbstractClassAdaptor[] {};

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		for (AbstractClassAdaptor adaptor : ADAPTORS) {
			if (adaptor.getClassName().equals(transformedName)) {
				ClassReader cr = new ClassReader(basicClass);
				ClassWriter cw = new ClassWriter(1);
				cr.accept(adaptor.getVisitor(cw), 0);

				return cw.toByteArray();
			}
		}
		return basicClass;		
	}

	public static String unmapClassName(String name)
	{
		return FMLDeobfuscatingRemapper.INSTANCE.unmap(name.replace('.', '/')).replace('/', '.');
	}

	public static String mapMethodName(String owner, String methodName, String desc)
	{
		return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(unmapClassName(owner), methodName, desc);
	}

	public static String mapFieldName(String owner, String methodName, String desc)
	{
		return FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(unmapClassName(owner), methodName, desc);
	}

	public static String toDesc(Object returnType, Object... rawDesc)
	{
		StringBuilder sb = new StringBuilder("(");
		for (Object o : rawDesc)
		{
			sb.append(toDesc(o));
		}
		sb.append(')');
		sb.append(toDesc(returnType));
		return sb.toString();
	}

	public static String toDesc(Object raw)
	{
		if (raw instanceof Class)
		{
			Class<?> clazz = (Class<?>) raw;
			return Type.getDescriptor(clazz);
		}
		else if (raw instanceof String)
		{
			String desc = (String) raw;
			desc = desc.replace('.', '/');
			desc = desc.matches("L.+;") ? desc : "L" + desc + ";";
			return desc;
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
}
