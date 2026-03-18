package neo_ores.core;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public abstract class AbstractClassAdaptor
{
	public abstract String getClassName();
	
	public abstract ClassVisitor getVisitor(ClassWriter cw); 
	
	public int getAcceptFlag() 
	{
		return 0;
	}
}
