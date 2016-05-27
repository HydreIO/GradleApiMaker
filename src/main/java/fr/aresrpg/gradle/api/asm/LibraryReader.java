package fr.aresrpg.gradle.api.asm;

import org.objectweb.asm.*;

public class LibraryReader extends ClassVisitor{

	public LibraryReader(ApiWriter writer) {
		super(Opcodes.ASM5 , writer);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		return ((ApiWriter)cv).writeApiMethod(access, name, desc, signature, exceptions);
	}

	@Override
	public void visitSource(String source, String debug) {
		//Ignore don't visit source for apis
	}

}
