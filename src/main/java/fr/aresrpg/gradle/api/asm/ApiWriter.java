package fr.aresrpg.gradle.api.asm;

import org.objectweb.asm.*;

public class ApiWriter extends ClassWriter{
	private static final String OBJECT_INIT = "<init>";
	private static final String CLASS_INIT = "<clinit>";

	private String superName;

	public ApiWriter() {
		super(Opcodes.ASM5);
	}

	public MethodVisitor writeApiMethod(int access, String name, String desc, String signature, String[] exceptions){
		if(CLASS_INIT.equals(name))
			return visitMethod(access , name , desc , signature , exceptions);
		else
			return new ApiMethodWriter(visitMethod(access , name , desc , signature , exceptions), signature == null ? null : Type.getReturnType(signature), OBJECT_INIT.equals(name), superName);
	}

	public void setSuperName(String superName) {
		this.superName = superName;
	}

	private static class ApiMethodWriter extends MethodVisitor{
		private final MethodVisitor delegate;
		private final Type returnType;
		private final boolean init;
		private final String superName;
		private Label first;
		private int stacks;

		public ApiMethodWriter(MethodVisitor delegate, Type returnType, boolean init, String superName) {
			super(Opcodes.ASM5);
			this.delegate = delegate;
			this.returnType = returnType;
			this.init = init;
			this.superName = superName;
			this.stacks = 1;//For this
		}

		@Override
		public void visitParameter(String name, int access) {
			delegate.visitParameter(name , access);
		}

		@Override
		public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
			return delegate.visitParameterAnnotation(parameter , desc , visible);
		}

		@Override
		public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
			return delegate.visitTypeAnnotation(typeRef, typePath, desc, visible);
		}

		@Override
		public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
			if("this".equals(name)){
				delegate.visitLocalVariable(name, desc, signature, start, end, 0);
			} else if(first == start){
				delegate.visitLocalVariable(name, desc, signature, start, end, this.stacks++);
				first = start;
			}
		}

		@Override
		public void visitCode() {
			delegate.visitCode();
			Label l0 = new Label();
			delegate.visitLabel(l0);
			if(init){
				delegate.visitVarInsn(Opcodes.ALOAD, 0);
				delegate.visitMethodInsn(Opcodes.INVOKESPECIAL, superName, OBJECT_INIT, "()V", false);//Ignore heritage
				delegate.visitInsn(Opcodes.RETURN);
			} else if(returnType == null){
				delegate.visitInsn(Opcodes.RETURN);
			} else {
				delegate.visitInsn(Opcodes.ACONST_NULL);
				delegate.visitInsn(Opcodes.ARETURN);
			}
		}

		@Override
		public void visitEnd() {
			Label l1 = new Label();
			delegate.visitLabel(l1);
			visitMaxs();
			delegate.visitEnd();
		}

		@Override
		public void visitLabel(Label label) {
			if(first == null)
				first = label;
		}

		public void visitMaxs() {
			delegate.visitMaxs(init ? 1 : (returnType == null ? 0 : 1), stacks);
		}
	}
}
